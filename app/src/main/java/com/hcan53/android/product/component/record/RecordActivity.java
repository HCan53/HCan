package com.hcan53.android.product.component.record;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.hcan53.android.product.R;
import com.hcan53.android.product.base.BaseActivity;
import com.hcan53.android.screen.record.ScreenRecordUtil;
import com.hcan53.android.screen.shot.ScreenShotUtil;
import com.hcan53.android.views.jm.JmTopBar;

/**
 * 截屏录屏
 */
public class RecordActivity extends BaseActivity implements View.OnClickListener {
    private JmTopBar jmTopBar;
    private TextView shot;
    private TextView startRecord;
    private TextView stopRecord;

    @Override
    protected int getContentViewId() {
        return R.layout.record_activity;
    }

    @Override
    protected void initView() {
        jmTopBar = findViewById(R.id.jm_top_bar);
        shot = findViewById(R.id.txt_shot);
        startRecord = findViewById(R.id.txt_start_record);
        stopRecord = findViewById(R.id.txt_stop_record);
        jmTopBar.setOnLeftClickListener(() -> finish());
        shot.setOnClickListener(this);
        startRecord.setOnClickListener(this);
        stopRecord.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_shot:
                //截屏
                ScreenShotUtil.getInstance().screenShot(RecordActivity.this);
                break;
            case R.id.txt_start_record:
                //开始录屏
                Intent start = new Intent("com.hanweb.android.record");
                start.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                start.putExtra("action","start");
                sendBroadcast(start);
//                ScreenRecordUtil.getInstance().screenRecord(RecordActivity.this);
                break;
            case R.id.txt_stop_record:
                //停止录屏
                Intent stop = new Intent("com.hanweb.android.record");
                stop.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                stop.putExtra("action","stop");
                sendBroadcast(stop);
//                ScreenRecordUtil.getInstance().destroy();
                break;
            default:
                break;
        }
    }

    public static void intentActivity(Activity ac) {
        ac.startActivity(new Intent(ac, RecordActivity.class));
    }

    @Override
    public void setPresenter() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void toastMessage(String message) {

    }
}
