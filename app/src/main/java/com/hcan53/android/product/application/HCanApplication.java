package com.hcan53.android.product.application;

import android.annotation.SuppressLint;
import android.app.Application;

import com.hcan53.android.defense.DefenseCrash;
import com.hcan53.android.defense.ReadLogUtils;
import com.hcan53.android.defense.handler.IExceptionHandler;
import com.hcan53.android.http.HttpUtils;
import com.hcan53.android.product.BuildConfig;
import com.hcan53.android.screen.RecordInit;
import com.hcan53.android.utils.JLog;
import com.hcan53.android.utils.UtilsInit;


/**
 *
 */
public class HCanApplication extends Application implements IExceptionHandler {
    public static Application app;

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        //防闪退
        if (!BuildConfig.DEBUG) {
            //初始化防crash
            DefenseCrash.initialize();
            DefenseCrash.install(this);
        }
        new JLog.Builder().setGlobalTag("HCan");
        UtilsInit.init(this);
        HttpUtils.init(this);
        RecordInit.init(this);
    }

    @Override
    public void onCaughtException(Thread thread, Throwable throwable, boolean isSafeMode) {
        JLog.e(ReadLogUtils.readThrowable(throwable));
    }

    @Override
    public void onEnterSafeMode() {

    }

    @Override
    public void onMayBeBlackScreen(Throwable throwable) {

    }
}
