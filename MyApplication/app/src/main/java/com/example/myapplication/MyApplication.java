package com.example.myapplication;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    public static RealmConfiguration memoConfig;
    public static RealmConfiguration defaultConfig;
    public static RealmConfiguration healthConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        memoConfig = new RealmConfiguration.Builder()
                .name("Memo.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        healthConfig = new RealmConfiguration.Builder()
                .name("Memo.realm")
                .deleteRealmIfMigrationNeeded()
                .build();

        //Realm.setDefaultConfiguration(defaultConfig);
    }
}
