package com.yang.pagemanager;

import android.app.Application;

/**
 * Created by yang on 2019/11/26.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyPageManager.initWhenAppOnCreate(getApplicationContext(),R.layout.pager_empty,R.layout.pager_loading,R.layout.pager_error);
    }
}
