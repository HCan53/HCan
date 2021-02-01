package com.hcan53.android.product;

import android.os.Bundle;

import com.hcan53.android.component.download.activity.DownLoadActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class MainActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownLoadActivity.intentActivity(this);
    }
}
