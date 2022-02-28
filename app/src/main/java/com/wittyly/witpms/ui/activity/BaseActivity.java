package com.wittyly.witpms.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wittyly.witpms.di.component.ActivityComponent;
import com.wittyly.witpms.di.component.DaggerActivityComponent;
import com.wittyly.witpms.di.module.CacheModule;
import com.wittyly.witpms.di.module.NetworkModule;

import javax.inject.Inject;

import io.realm.Realm;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;
    @Inject Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // It is better to load all modules before the setup
        activityComponent = DaggerActivityComponent
                .builder()
                .networkModule(new NetworkModule(this))
                .cacheModule(new CacheModule(this))
                .build();
        setup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public abstract void setup();

}
