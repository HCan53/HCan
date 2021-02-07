package com.hcan53.android.record;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.hcan53.android.record.utils.OtherUtils;
import com.hcan53.android.record.utils.SDCardUtils;
import com.hcan53.android.record.utils.ScreenRecordUtil;

import java.io.File;

/**
 * Created By HCan on 2021/2/1
 */
public class ScreenRecordBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "action";
    /**
     * 启动服务
     */
    public static final String START = "start";
    /**
     * 停止服务
     */
    public static final String STOP = "stop";
    /**
     * 截图
     */
    public static final String SHOT = "shot";

    /**
     * 录屏存储位置
     */
    private String videoDir = "";

    /**
     * 录屏文件存储名称
     */
    private String fileName = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            switch (action) {
                case START:
                    String time = OtherUtils.formatDate(System.currentTimeMillis());
                    videoDir = SDCardUtils.getStorageDirectory().getPath() + "/record";
                    File file = new File(videoDir);
                    //判断文件夹是否存在,如果不存在则创建文件夹
                    if (!file.exists()) {
                        file.mkdir();
                    } else {
                        if (file.isFile()) {
                            SDCardUtils.delAllFile(videoDir);
                            file.mkdir();
                        }
                    }
                    fileName = time + ".mp4";
                    ScreenRecordUtil.getInstance().start(context, videoDir + "/" + fileName);
                    break;
                case STOP:
                    ScreenRecordUtil.getInstance().destroy();
                    ContentResolver localContentResolver = context.getContentResolver();
                    ContentValues localContentValues =
                            OtherUtils.getVideoContentValues(new File(videoDir), System.currentTimeMillis());
                    Uri localUri = localContentResolver.insert(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));
                    break;
                default:
                    Toast.makeText(context, "未知操作", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}
