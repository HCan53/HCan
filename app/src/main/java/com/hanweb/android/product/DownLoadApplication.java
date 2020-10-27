package com.hanweb.android.product;

import android.app.Application;

import com.hcan53.android.utils.JLog;
import com.hcan53.android.utils.UtilsInit;


public class DownLoadApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new JLog.Builder().setGlobalTag("HCan");
        UtilsInit.init(this);
    }
}
