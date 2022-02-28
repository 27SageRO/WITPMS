package com.wittyly.witpms.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wittyly.witpms.R;
import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.Authorization;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.presenter.GetAuthorizationPresenter;
import com.wittyly.witpms.presenter.GetUserByUsernamePresenter;
import com.wittyly.witpms.ui.customviews.PixelFlowView;
import com.wittyly.witpms.util.ImageManager;
import com.wittyly.witpms.util.Utilities;
import com.wittyly.witpms.view.GetAuthorizationView;
import com.wittyly.witpms.view.GetUserView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.HttpException;

public class ActivityLogin extends BaseActivity implements View.OnClickListener {

    @Inject GetAuthorizationPresenter getAuthorizationPresenter;
    @Inject GetUserByUsernamePresenter getUserByUsernamePresenter;
    @Inject Realm realm;
    @Inject Authorization auth;

    @BindView(R.id.login) Button bLogin;
    @BindView(R.id.login_password) AppCompatEditText tPassword;
    @BindView(R.id.login_username) AppCompatEditText tUsername;
    @BindView(R.id.pixel_effect) PixelFlowView pixelFlowView;
    @BindView(R.id.login_user_image) ImageView userImg;
    @BindView(R.id.login_remember_password) TextView rememberPasswordMsg;

    private User userCache;

    private Handler typingHandler;
    private Runnable typingRunnable;

    private void callMainView() {
        startActivity(new Intent(this, ActivityTemp.class));
        finish();
    }

    @Override
    public void setup() {

        getActivityComponent().inject(this);

        /*
         * Let's check if
         * there is already a logged in user.
         **/

        if (auth.getAccessToken() != null && !auth.getAccessToken().equals("")) {
             callMainView();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        tUsername.setText("rogecp");
        tPassword.setText("Unicorns are real");

        bLogin.setOnClickListener(this);

        typingHandler = new Handler();
        typingRunnable = new Runnable() {
            @Override
            public void run() {
                Params params = Params.create();
                params.putString("username", tUsername.getText().toString());
                getUserByUsernamePresenter.start(params);
            }
        };

        rememberPasswordMsg.setText(Utilities.fromHtml("Forgot your password? <font color=\"#FFFFFF\">Remember</font>"));

        getAuthorizationPresenter.setView(new GetAuthorizationView() {

            @Override
            public void onResult(final Authorization authorization) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insert(authorization);
                        realm.insert(userCache);
                    }
                });


                getSharedPreferences("private", 0)
                        .edit()
                        .putString("Token", authorization.getAccessToken())
                        .apply();

                callMainView();
            }

            @Override
            public void onDone() {
                bLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHalt(Throwable e) {
                bLogin.setVisibility(View.VISIBLE);
                Toast.makeText(ActivityLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        getUserByUsernamePresenter.setView(new GetUserView() {

            @Override
            public void onResult(User user) {
                ImageManager.drawUserImg(ActivityLogin.this, user, userImg, 130);
                userCache = user;
            }

            @Override
            public void onDone() {}

            @Override
            public void onHalt(Throwable e) {
                Log.d("TEST", e.getMessage());
                userImg.setImageDrawable(null);
            }

        });
        tUsername.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                typingHandler.removeCallbacks(typingRunnable);
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                typingHandler.postDelayed(typingRunnable, 1500);
            }

        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        pixelFlowView.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pixelFlowView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAuthorizationPresenter.destroy();
        getUserByUsernamePresenter.destroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login:

                bLogin.setVisibility(View.INVISIBLE);

                Params params = Params.create();
                params.putString("username", tUsername.getText().toString());
                params.putString("password", tPassword.getText().toString());
                params.putString("scope", "*");

                getAuthorizationPresenter.start(params);
                break;

        }
    }

}
