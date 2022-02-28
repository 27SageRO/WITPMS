package com.wittyly.witpms.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.wittyly.witpms.R;
import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.Comment;
import com.wittyly.witpms.model.Task;
import com.wittyly.witpms.presenter.GetCommentPresenter;
import com.wittyly.witpms.presenter.StoreCommentPresenter;
import com.wittyly.witpms.ui.adapter.CommentAdapter;
import com.wittyly.witpms.util.Utilities;
import com.wittyly.witpms.view.BaseView;
import com.wittyly.witpms.view.GetCommentView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ActivityComment extends BaseActivity implements GetCommentView, BaseView, View.OnClickListener {

    @Inject Realm realm;
    @Inject GetCommentPresenter getCommentPresenter;
    @Inject StoreCommentPresenter storeCommentPresenter;

    @BindView(R.id.comments) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.input_comment) EditText in;
    @BindView(R.id.button_done) ImageView btn;

    private Params params;
    private Task task;
    private CommentAdapter mAdapter;
    private boolean storeCommentFlag;

    @Override
    public void setup() {

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        getActivityComponent().inject(this);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        String key = "task_series_no";
        params = Params.create();
        params.putInt(key, getIntent().getIntExtra(key, 0));

        task = realm.where(Task.class)
                .equalTo("seriesNo", params.getInt(key))
                .findFirst();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(
                task.getTicket().getProject().getShortName() + "-" +
                Utilities.withZeros(task.getTicket().getSeriesNo()) + " > #" +
                Integer.toString(
                        task.getSeriesNo()).replace(
                                Integer.toString(task.getTicket().getId()), "")
        );

        mAdapter = new CommentAdapter(task, new ArrayList<Comment>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        btn.setOnClickListener(this);
        storeCommentPresenter.setView(this);
        getCommentPresenter.setView(this);

        getCommentPresenter.start(params);
        storeCommentFlag = false;

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_done:
                if (!in.getText().toString().equals("")) {
                    params.putString("description", in.getText().toString());
                    storeCommentPresenter.start(params);
                    storeCommentFlag = !storeCommentFlag;
                    in.setText("");
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse);
    }

    @Override
    public void onDone() {
        if (storeCommentFlag) {
            mAdapter.clear();
            getCommentPresenter.start(params);
            storeCommentFlag = false;
        }
    }

    @Override
    public void onHalt(Throwable e) {
        Log.wtf("Error", e.getMessage());
    }

    @Override
    public void onResult(Comment comment) {
        mAdapter.add(comment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCommentPresenter.destroy();
    }

}
