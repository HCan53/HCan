package com.hcan53.android.screen.record;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.hcan53.android.screen.ScreenPermissionActivity;
import com.hcan53.android.screen.utils.OtherUtils;
import com.hcan53.android.screen.utils.SDCardUtils;
import com.hcan53.android.utils.UtilsInit;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by HCan on 2021/5/12
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordUtil {

    static ScreenRecorder mRecorder;
    static MediaProjection mMediaProjection;

    static int mScreenDensity;
    static int mRecordWidth;
    static int mRecordheight;
    static int mScreenRecordBitrate = 30;
    static String savePath;
    String videoDir;
    Context activity;
    private OnScreenRecordListener recordListener;

    public void setRecordListener(OnScreenRecordListener recordListener) {
        this.recordListener = recordListener;
    }

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

    public void screenRecord(Context activity) {
        if (mRecorder != null && mRecorder.isAlive()) return;
        this.activity = activity;
        String time = OtherUtils.formatDate(System.currentTimeMillis());
        videoDir = SDCardUtils.getStorageDirectory().getPath() + "/record";
        File file = new File(videoDir);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName = time + ".mp4";
        this.savePath = videoDir + "/" + fileName;
        startRecord();
    }

    /**
     * 开始录屏
     *
     * @param activity
     * @param savePath
     */
    public void screenRecord(Context activity, String savePath) {
        if (mRecorder != null && mRecorder.isAlive()) return;
        this.activity = activity;
        this.savePath = savePath;
        startRecord();
    }


    private void startRecord() {
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        ScreenPermissionActivity.isScrennShot = false;
        Intent intent = new Intent(activity, ScreenPermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    //返回可以开始录屏的数据
    public void permissionResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (mMediaProjectionManager == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent service = new Intent(activity, ScreenRecordService.class);
                service.putExtra("code", resultCode);
                service.putExtra("data", data);
                activity.startForegroundService(service);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                    mRecorder = new ScreenRecorder(mRecordWidth, mRecordheight, mScreenRecordBitrate, mScreenDensity, mMediaProjection, savePath);
                    mRecorder.start();
                }
            }
        } else {
            if (recordListener != null)
                recordListener.screenRecord(false, "");
        }
    }


    static class ScreenRecordHolder {
        private static ScreenRecordUtil instance = new ScreenRecordUtil();
    }

    /**
     * 结束录屏
     */
    public void destroy() {
        if (mRecorder != null) {
            mRecorder.quit();
            mRecorder = null;
            mMediaProjectionManager = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent service = new Intent(activity, ScreenRecordService.class);
            activity.stopService(service);
        }
        if (recordListener != null)
            recordListener.screenRecord(true, savePath);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(videoDir));
        intent.setData(uri);
        UtilsInit.getApp().sendBroadcast(intent);
    }

}
