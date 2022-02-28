package com.wittyly.witpms;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Wittyly extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config0 = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(6)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config0);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

    }

}
