package com.wittyly.witpms.ui.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wittyly.witpms.R;
import com.wittyly.witpms.model.Ticket;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.presenter.GetMentionHashtagPresenter;
import com.wittyly.witpms.presenter.GetUserByNamePresenter;
import com.wittyly.witpms.ui.customviews.AssigneesView;
import com.wittyly.witpms.ui.customviews.MentionsEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ActivityCreateTicket extends BaseActivity implements MentionsEditText.MentionsEditTextListener, View.OnClickListener {

    @Inject Realm realm;
    @Inject User user;
    @Inject GetUserByNamePresenter getUserByNamePresenter;
    @Inject GetMentionHashtagPresenter getMentionHashtagPresenter;

    @BindView(R.id.mention_edit_text) MentionsEditText mentions;
    @BindView(R.id.assignees) AssigneesView assigneesView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    public void setup() {

        getActivityComponent().inject(this);
        setContentView(R.layout.activity_creator);
        ButterKnife.bind(this);

        mentions.setGetUserByNamePresenter(getUserByNamePresenter);
        mentions.setGetMentionHashtagPresenter(getMentionHashtagPresenter);
        assigneesView.addAssignee(user);

        mentions.setListener(this);
        assigneesView.setOnClickListener(this);

        mentions.getAdapter().setProjectsVisibility(true);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AlertDialog.Builder modalBuilder = new AlertDialog.Builder(this);
        modalBuilder.setTitle("Hello!");
        modalBuilder.setMessage("This functionality is still in progress.");
        modalBuilder.setPositiveButton("OK", null);
        modalBuilder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.assignees:
                assigneesView.showAssignees();
                break;
        }
    }

    @Override
    public void onSelectMentionedUser(User user) {
        assigneesView.addAssignee(user);
    }

    @Override
    public void onSelectMentionedTicket(Ticket ticket) {
        // DO NOTHING
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMentionHashtagPresenter.destroy();
        getUserByNamePresenter.destroy();
    }
}
