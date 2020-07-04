package com.locker.plugin;

import android.app.Application;


/**
 * Created by xff on 2017/4/21.
 */

public class LockerApplication extends Application {
    static LockerApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }

    public static LockerApplication get(){
        return application;
    }
}
