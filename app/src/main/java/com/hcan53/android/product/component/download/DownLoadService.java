package com.hcan53.android.product.component.download;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.hcan53.android.product.R;
import com.hcan53.android.http.HttpUtils;
import com.hcan53.android.http.callback.DownloadCallBack;
import com.hcan53.android.http.request.DownloadListener;
import com.hcan53.android.product.component.download.view.NotificationUtils;
import com.hcan53.android.utils.IntentUtils;
import com.hcan53.android.utils.SDCardUtils;
import com.hcan53.android.utils.ToastUtils;

import java.io.File;

public class DownLoadService extends JobIntentService {

    /**
     * 这个Service 唯一的id
     */
    static final int JOB_ID = 111111;
    static final String DOWNLOADURL = "DOWNLOADURL";
    private NotificationUtils notificationUtils;
    private int downloadNotifyId = 0;//下载通知栏id
    private int installNotifyId = 1;//安装通知栏id

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, String downLoadUrl) {
        Intent work = new Intent(context, DownLoadService.class);
        work.putExtra(DOWNLOADURL, downLoadUrl);
        enqueueWork(context, DownLoadService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent != null) {
            String url = intent.getStringExtra(DOWNLOADURL);
            notificationUtils = new NotificationUtils(DownLoadService.this);
            //下载文件存放路径
            notificationUtils.clearNoticeLoad(installNotifyId);
            SDCardUtils.getCacheDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            notificationUtils.sendNotificationProgress(downloadNotifyId, "更新提示", "客户端版本更新", 0, null);
            handleUpdate(url);
        }
    }

    private void handleUpdate(String url) {
        HttpUtils.downLoad(url, downloadListener)
                .setDirName(SDCardUtils.getCachePath(Environment.DIRECTORY_DOWNLOADS))
                .setFileName(getString(R.string.app_name) + ".apk")
                .execute(new DownloadCallBack<File>() {
                    @Override
                    public void onProgress(int progress, long total) {
                    }

                    @Override
                    public void onSuccess(File data) {
                        Intent bcintent = new Intent();
                        bcintent.putExtra("TYPE", "SUCCESS");
                        bcintent.putExtra("FILE", data);
                        bcintent.setAction("android.intent.action.asd");// action与接收器相同
                        sendBroadcast(bcintent);
                        Intent intent = IntentUtils.getInstallAppIntent(data);
                        startActivity(intent);
                        notificationUtils.clearNoticeLoad(downloadNotifyId);
                        PendingIntent pendingIntent = PendingIntent.getActivity(DownLoadService.this, 0, intent, 0);
                        notificationUtils.setIntent(installNotifyId, pendingIntent);
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtils.showShort("下载失败");
                    }
                });
    }

    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onStartDownload(long length) {
            ToastUtils.showShort("开始更新");
            Intent bcintent = new Intent();
            bcintent.putExtra("TYPE", "START");
            bcintent.setAction("android.intent.action.asd");// action与接收器相同
            sendBroadcast(bcintent);
        }

        @Override
        public void onProgress(int progress) {
            if (progress >= 0 && progress < 100) {
                notificationUtils.setProgress(downloadNotifyId, progress);
            }
            Intent bcintent = new Intent();
            bcintent.putExtra("TYPE", "PRO");
            bcintent.putExtra("PRO", progress);
            bcintent.setAction("android.intent.action.asd");// action与接收器相同
            sendBroadcast(bcintent);
        }

        @Override
        public void onFail(String errorInfo) {
        }
    };
}
