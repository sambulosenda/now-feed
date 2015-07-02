package com.jaellysbales.nowfeed;

import android.app.Application;

/**
 * Created by jaellysbales on 7/1/15.
 */
public class NowFeedApplication extends Application {

    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }


    public static Application getInstance() {
        return mApplication;
    }
}
