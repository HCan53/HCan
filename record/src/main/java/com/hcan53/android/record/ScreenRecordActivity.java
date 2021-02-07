package com.hcan53.android.record;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hcan53.android.record.utils.ScreenRecordUtil;
import com.hcan53.android.record.utils.ScreenShotUtil;

public class ScreenRecordActivity extends AppCompatActivity {
    public static final int REQUEST_MEDIA_PROJECTION = 18;

    public static boolean isScrennShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaProjectionManager projectionManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        } else {
            requestCapturePermission();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (isScrennShot) {
                ScreenShotUtil.getInstance().permissionResult(resultCode, data);
            } else {
                ScreenRecordUtil.getInstance().permissionResult(resultCode, data);
            }
            finish();
        }
    }

    //请求权限
    private void requestCapturePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            return;
        }
        if (isScrennShot) {
            startActivityForResult(ScreenShotUtil.mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        } else {
            startActivityForResult(ScreenRecordUtil.mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ScreenRecordActivity：", "onDestroy");
    }
}
