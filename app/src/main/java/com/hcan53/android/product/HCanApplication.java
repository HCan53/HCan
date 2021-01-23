package com.hcan53.android.product;

import android.app.Application;

import com.hcan53.android.http.HttpUtils;
import com.hcan53.android.utils.JLog;
import com.hcan53.android.utils.UtilsInit;


public class HCanApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new JLog.Builder().setGlobalTag("HCan");
        UtilsInit.init(this);
        HttpUtils.init(this);
    }
}
