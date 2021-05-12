package com.hcan53.android.product.application;

import android.annotation.SuppressLint;
import android.app.Application;

import com.hcan53.android.http.HttpUtils;
import com.hcan53.android.screen.RecordInit;
import com.hcan53.android.utils.JLog;
import com.hcan53.android.utils.UtilsInit;


public class HCanApplication extends Application {
    public static Application app;

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        new JLog.Builder().setGlobalTag("HCan");
        UtilsInit.init(this);
        HttpUtils.init(this);
        RecordInit.init(this);
    }
}
