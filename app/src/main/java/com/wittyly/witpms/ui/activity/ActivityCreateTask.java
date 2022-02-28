package com.wittyly.witpms.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wittyly.witpms.R;
import com.wittyly.witpms.model.Ticket;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.presenter.GetMentionHashtagPresenter;
import com.wittyly.witpms.presenter.GetUserByNamePresenter;
import com.wittyly.witpms.ui.customviews.AssigneesView;
import com.wittyly.witpms.ui.customviews.MentionsEditText;
import com.wittyly.witpms.util.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ActivityCreateTask extends BaseActivity implements
        MentionsEditText.MentionsEditTextListener,
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    @Inject Realm realm;
    @Inject User user;
    @Inject GetUserByNamePresenter getUserByNamePresenter;
    @Inject GetMentionHashtagPresenter getMentionHashtagPresenter;

    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private Calendar mCalendar;

    @BindView(R.id.mention_edit_text) MentionsEditText mentions;
    @BindView(R.id.assignees) AssigneesView assigneesView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.to) TextView to;
    @BindView(R.id.due) TextView due;

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

        mentions.getAdapter().setTicketsVisibility(true);

        mCalendar = Calendar.getInstance();

        datePicker = new DatePickerDialog(this, this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        timePicker = new TimePickerDialog(this, this, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clock, menu);
        getMenuInflater().inflate(R.menu.check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_clock) {
            datePicker.show();
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
        to.setVisibility(View.VISIBLE);
        to.setText(Utilities.fromHtml("<b>To: </b>" + ticket.getGeneratedId() + " " + ticket.getTitle()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMentionHashtagPresenter.destroy();
        getUserByNamePresenter.destroy();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        timePicker.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        displayTime();
    }

    public void displayTime() {
        String date = new SimpleDateFormat("MMM d, yyyy 'at' h:mm a").format(mCalendar.getTime());
        due.setVisibility(View.VISIBLE);
        due.setText(Utilities.fromHtml("<b>Due: </b>" + date));
    }

}
