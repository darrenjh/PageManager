package com.yang.pagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MyPageManager pageStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageStateManager = MyPageManager.init(this, new MyPageListener() {
            @Override
            protected void onReallyRetry() {

            }

            @Override
            public void onEmtptyViewClicked(View emptyView) {
                super.onEmtptyViewClicked(emptyView);
            }
        });
        pageStateManager.showLoading();
        final Handler mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageStateManager.showEmpty();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageStateManager.showError();


                    }
                },1000);

            }
        },1000);
    }
}
