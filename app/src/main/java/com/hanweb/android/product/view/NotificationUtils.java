package com.hanweb.android.product.view;


import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.hanweb.android.product.R;


public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    private NotificationCompat.Builder builder;
    private Context context;
    public String notityId = "";
    private String notityName = "";

    public NotificationUtils(Context base) {
        super(base);
        this.context = base;
        notityName = context.getResources().getString(R.string.app_name);
        notityId = context.getResources().getString(R.string.notity_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(notityId, notityName, importance);
        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        // 设置通知出现时的震动（如果 android 设备支持的话）
        mChannel.enableVibration(false);
        mChannel.setVibrationPattern(new long[]{0});
        //最后在notificationmanager中创建该通知渠道
        getManager().createNotificationChannel(mChannel);

    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }


    /**
     * 获取带有进度的Notification
     */
    private NotificationCompat.Builder getNotificationProgress(String title, String content,
                                                               int progress, PendingIntent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(getApplicationContext(), notityId);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setPriority(NotificationCompat.PRIORITY_LOW);
        }
        //标题
        builder.setContentTitle(title);
        //文本内容
        builder.setContentText(content);
        //小图标
        builder.setSmallIcon(R.mipmap.ic_logo);
        //设置大图标，未设置时使用小图标代替，拉下通知栏显示的那个图标
        //设置大图片 BitmpFactory.decodeResource(Resource res,int id) 根据给定的资源Id解析成位图
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo));
        if (progress >= 0 && progress < 100) {
            //一种是有进度刻度的（false）,一种是循环流动的（true）
            //设置为false，表示刻度，设置为true，表示流动
            builder.setProgress(100, progress, false);
        } else {
            //0,0,false,可以将进度条隐藏
            builder.setProgress(0, 0, false);
            builder.setContentText("下载完成");
        }
        //设置点击信息后自动清除通知
        builder.setAutoCancel(true);
        //通知的时间
        builder.setWhen(System.currentTimeMillis());
        //设置点击信息后的跳转（意图）
        if (intent != null)
            builder.setContentIntent(intent);
        builder.setOngoing(true);
        return builder;
    }


    /**
     * 发送带有进度的通知
     */
    public void sendNotificationProgress(String title, String content, int progress, PendingIntent intent) {
        builder = getNotificationProgress(title, content, progress, intent);
        builder.setOngoing(true);
        getManager().notify(0, builder.build());
    }

    public void setProgress(int progress) {
        if (builder == null) return;
        if (progress >= 0 && progress < 100) {
            //一种是有进度刻度的（false）,一种是循环流动的（true）
            //设置为false，表示刻度，设置为true，表示流动
            builder.setProgress(100, progress, false);
        } else {
            //0,0,false,可以将进度条隐藏
            builder.setProgress(100, 100, false);
            builder.setContentText("下载完成");
        }
        builder.setOngoing(true);
        getManager().notify(0, builder.build());
    }

    public void setIntent(PendingIntent intent) {
        if (builder != null) {
            builder.setContentIntent(intent);
            builder.setOngoing(true);
            getManager().notify(0, builder.build());
        }
    }

    /**
     * 清除进度通知
     */
    public void clearNoticeLoad() {
        getManager().cancel(0);
    }

}