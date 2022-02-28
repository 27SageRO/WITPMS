package com.wittyly.witpms.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.wittyly.witpms.R;
import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.POJO.FeedModel;
import com.wittyly.witpms.model.Task;
import com.wittyly.witpms.presenter.GetFeedPresenter;
import com.wittyly.witpms.ui.adapter.FeedAdapter;
import com.wittyly.witpms.ui.customviews.FabCombo;
import com.wittyly.witpms.view.GetFeedView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmObject;

public class ActivityTemp extends BaseActivity implements View.OnClickListener, GetFeedView{

    @Inject GetFeedPresenter getFeedPresenter;

    @BindView(R.id.view_fab_combo) FabCombo fabCombo;
    @BindView(R.id.feed) RecyclerView mRecyclerView;
    @BindView(R.id.pull_to_refresh) SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private FeedAdapter mAdapter;
    private Params params;
    boolean loading;

    @Override
    public void onDone() {
        if (pullToRefresh.isRefreshing()) {
            pullToRefresh.setRefreshing(false);
        }
        loading = true;
    }

    @Override
    public void onHalt(Throwable e) {
        Log.wtf("Error", e);
    }

    @Override
    public void onResult(FeedModel feed) {
        mAdapter.add(feed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public void setup() {

        getActivityComponent().inject(this);
        setContentView(R.layout.activity_temp);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mAdapter = new FeedAdapter(new ArrayList<RealmObject>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setListener(new FeedAdapter.FeedListener() {
            @Override
            public void taskOnClick(Task task) {
                startActivity(
                        new Intent(ActivityTemp.this, ActivityComment.class)
                                .putExtra("task_series_no", task.getSeriesNo())
                );
            }
        });

        fabCombo.setListener(new FabCombo.CreateButtonListener() {

            @Override
            public void createTask() {
                startActivity(new Intent(ActivityTemp.this, ActivityCreateTask.class));
            }

            @Override
            public void createTicket() {
                startActivity(new Intent(ActivityTemp.this, ActivityCreateTicket.class));
            }

        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                params.putInt("page", 0);
                getFeedPresenter.start(params);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int visibleItemCount;
            int totalItemCount;
            int pastVisibleItems;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                    totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (loading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        next();
                    }
                }
            }
        });

        params = Params.create();
        params.putInt("page", 0);
        getFeedPresenter.setView(this);
        getFeedPresenter.start(params);

    }

    private void next() {
        params.putInt("page", params.getInt("page") + 1);
        getFeedPresenter.start(params);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getFeedPresenter.destroy();
    }
}
