package com.wittyly.witpms.di.module;

import android.content.Context;
import android.support.annotation.Nullable;

import com.wittyly.witpms.model.Authorization;
import com.wittyly.witpms.model.User;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class CacheModule {

    private final Context context;

    public CacheModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Authorization provideAuthorization(Realm realm) {

        Authorization auth = realm.where(Authorization.class).findFirst();

        if (auth == null) {
            auth = new Authorization();
        }

        return auth;

    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public User provideUser(Realm realm) {

        User user = realm.where(User.class).findFirst();

        if (user == null) {
            user = new User();
        }

        return user;

    }

}
