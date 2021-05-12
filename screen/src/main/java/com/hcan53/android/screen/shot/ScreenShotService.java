package com.hcan53.android.screen.shot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.hcan53.android.screen.R;
import com.hcan53.android.utils.JLog;

import java.util.Objects;

public class ScreenShotService extends Service {
    private NotificationManager mManager;
    private String CHANNEL_ID = "screen_shot";
    public static final int NOTIFICATION_ID = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        JLog.d("ScreenShotService onCreate");
    }

    //    onServiceConnected
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startForeground(NOTIFICATION_ID, getNotification());
        int resultCode = intent.getIntExtra("code", -1);
        Intent data = intent.getParcelableExtra("data");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && data != null) {
            ScreenShotUtil.mMediaProjection = ScreenShotUtil.mMediaProjectionManager.getMediaProjection(resultCode, Objects.requireNonNull(data));
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopForeground(true);
        JLog.d("ScreenShotService onDestroy");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        builder.setContentIntent(null) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon)) // 设置下拉列表中的图标(大图标)
                .setSmallIcon(R.drawable.icon) // 设置状态栏内的小图标
                .setContentText("系统正在采集屏幕截图") // 设置上下文内容
                .setContentTitle("系统提示")
                .setAutoCancel(true)// 用户单击面板后消失
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getManager();
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Hcan_Shot", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        return notification;
    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

}
