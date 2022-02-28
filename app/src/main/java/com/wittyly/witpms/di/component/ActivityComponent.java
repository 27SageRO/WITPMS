package com.wittyly.witpms.di.component;

import com.wittyly.witpms.di.module.CacheModule;
import com.wittyly.witpms.di.module.NetworkModule;
import com.wittyly.witpms.ui.activity.ActivityComment;
import com.wittyly.witpms.ui.activity.ActivityCreateTask;
import com.wittyly.witpms.ui.activity.ActivityCreateTicket;
import com.wittyly.witpms.ui.activity.ActivityLogin;
import com.wittyly.witpms.ui.activity.ActivityTemp;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, CacheModule.class})
public interface ActivityComponent {
    void inject(ActivityLogin activity);
    void inject(ActivityTemp activity);
    void inject(ActivityCreateTicket activity);
    void inject(ActivityCreateTask activity);
    void inject(ActivityComment activity);
}
