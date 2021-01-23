package com.hcan53.android.product;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import com.hcan53.android.component.download.DownLoadService;
import com.hcan53.android.component.download.view.DownLoadProgressDialog;
import com.hcan53.android.utils.StringUtils;
import com.hcan53.android.views.dialog.JmDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


public class MainActivity extends RxAppCompatActivity {
    private MyReceiver receiver;
    private DownLoadProgressDialog downLoadProgressDialog;
    private JmDialog mDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showUpdateDialog();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showUpdateDialog() {
        mDialog = new JmDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage("测试升级更新")
                .setPositiveButton("立即更新", (i, item, value) -> {
                    //检查是否已经授予权限
                    if (!Settings.canDrawOverlays(this)) {
                        //若未授权则请求权限
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 0);
                    } else {
                        checkVersion();
                        mDialog.dismiss();
                    }
                })
                .setNegativeButton("以后再说", (i, item, value) -> {
                    mDialog.dismiss();
                })
                .create();
        mDialog.show();
    }

    private void checkVersion() {
        downLoadProgressDialog = new DownLoadProgressDialog(this);
        if (receiver == null) {
            receiver = new MyReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.asd");
            registerReceiver(receiver, filter);
        }
        DownLoadService.enqueueWork(this, "https://download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk");
    }

    public class MyReceiver extends BroadcastReceiver {

        // 自定义一个广播接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String type = intent.getStringExtra("TYPE");
                if (!StringUtils.isSpace(type) && type.equals("START")) {
                    if (!downLoadProgressDialog.isShowing())
                        downLoadProgressDialog.show();
                } else if (!StringUtils.isSpace(type) && type.equals("PRO")) {
                    int progress = intent.getIntExtra("PRO", 0);
                    if (!downLoadProgressDialog.isShowing())
                        downLoadProgressDialog.show();
                    downLoadProgressDialog.changePro(progress);
                    if (progress == 100) {
                        downLoadProgressDialog.dismiss();
                    }
                }
            }
            // TODO  处理接收到的内容
        }

        public MyReceiver() {
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);
        receiver = null;
    }
}
