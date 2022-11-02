package com.hcan53.android.product.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hcan53.android.product.component.splash.SplashActivity;
import com.hcan53.android.utils.ToastUtils;

/**
 * Created by HCan on 2021/5/31
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    //重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        ToastUtils.showShort("开机服务自动启动.....");
        Intent mBootIntent = new Intent(context, SplashActivity.class);
        //下面这句话必须加上才能开机自动运行app的界面
        mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mBootIntent);
    }

}
