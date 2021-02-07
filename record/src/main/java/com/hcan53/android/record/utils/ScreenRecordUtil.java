package com.hcan53.android.record.utils;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.hcan53.android.record.ScreenRecordActivity;

import static android.app.Activity.RESULT_OK;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordUtil {

    static ScreenRecorder mRecorder;

    private int mScreenDensity;
    private int mRecordWidth;
    private int mRecordheight;
    private int mScreenRecordBitrate = 30;
    private String savePath;

    public static MediaProjectionManager mMediaProjectionManager;

    private ScreenRecordUtil() {
    }


    public void init(int width, int height, int screenRecordBitrate) {
        mRecordWidth = width;
        mRecordheight = height;
        mScreenRecordBitrate = screenRecordBitrate;

    }


    public static ScreenRecordUtil getInstance() {
        return ScreenRecordHolder.instance;
    }

    public void start(Context activity, String savePath) {
        this.savePath = savePath;
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        ScreenRecordActivity.isScrennShot = false;
        Intent intent = new Intent(activity, ScreenRecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    //返回可以开始录屏的数据
    public void permissionResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            MediaProjection mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            mRecorder = new ScreenRecorder(mRecordWidth, mRecordheight, mScreenRecordBitrate, mScreenDensity, mMediaProjection, savePath);
            mRecorder.start();
        }
    }


    static class ScreenRecordHolder {
        private static ScreenRecordUtil instance = new ScreenRecordUtil();
    }

    public void destroy() {
        if (mRecorder != null) {
            mRecorder.quit();
            mRecorder = null;
            mMediaProjectionManager = null;
        }
    }

    public int getmScreenDensity() {
        return mScreenDensity;
    }

    public void setmScreenDensity(int mScreenDensity) {
        this.mScreenDensity = mScreenDensity;
    }

    public int getmRecordWidth() {
        return mRecordWidth;
    }

    public void setmRecordWidth(int mRecordWidth) {
        this.mRecordWidth = mRecordWidth;
    }

    public int getmRecordheight() {
        return mRecordheight;
    }

    public void setmRecordheight(int mRecordheight) {
        this.mRecordheight = mRecordheight;
    }

    public int getmScreenRecordBitrate() {
        return mScreenRecordBitrate;
    }

    public void setmScreenRecordBitrate(int mScreenRecordBitrate) {
        this.mScreenRecordBitrate = mScreenRecordBitrate;
    }
}
