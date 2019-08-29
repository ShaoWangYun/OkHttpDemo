package com.imswy.okhttpdemo.application;

import android.app.Application;
import android.content.Context;

public class OkHttpApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getInstance() {
        return mContext;
    }
}
