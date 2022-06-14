package com.example.db;

import android.app.Application;


import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).name("MyRealm11.realm").build();
        Realm.setDefaultConfiguration(realmConfiguration);

        super.onCreate();
    }
}
