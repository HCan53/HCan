package com.hanweb.android.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.hanweb.android.product.view.DownLoadProgressDialog;
import com.hcan53.android.utils.StringUtils;

public class MainActivity extends AppCompatActivity {
    private MyReceiver receiver;
    private DownLoadProgressDialog downLoadProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkVersion();
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
        unregisterReceiver(receiver);
        receiver = null;
    }
}
