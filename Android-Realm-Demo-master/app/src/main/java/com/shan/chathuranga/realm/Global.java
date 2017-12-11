package com.shan.chathuranga.realm;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;

/**
 * Created by ChathurangaShan on 10/17/2017.
 */

public class Global extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);
    }
}
