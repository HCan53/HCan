package com.hcan53.android.record;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;

import com.hcan53.android.record.utils.OtherUtils;
import com.hcan53.android.record.utils.SDCardUtils;
import com.hcan53.android.record.utils.ScreenRecordUtil;

import java.io.File;


/**
 * Created By HCan on 2021/2/6
 * <p>
 * 录屏
 */
public class ScreenRecordService extends JobIntentService {

    /**
     * 这个Service 唯一的id
     */
    static final int JOB_ID = 222222;
    static final String ACTION = "ACTION";
    /**
     * 启动服务
     */
    public static final int START = 1;
    /**
     * 停止服务
     */
    public static final int STOP = 2;

    /**
     * 录屏存储位置
     */
    private String videoDir = "";

    /**
     * 录屏文件存储名称
     */
    private String fileName = "";


    /**
     * Convenience method for enqueuing work in to this service.
     *
     * @param context
     * @param action  动作--1：启动服务   2：停止服务
     */
    public static void enqueueWork(Context context, int action) {
        Intent work = new Intent(context, ScreenRecordService.class);
        work.putExtra(ACTION, action);
        enqueueWork(context, ScreenRecordService.class, JOB_ID, work);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(ACTION, 0);
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
                    ScreenRecordUtil.getInstance().start(this, videoDir + "/" + fileName);
                    break;
                case STOP:
                    ScreenRecordUtil.getInstance().destroy();
                    ContentResolver localContentResolver = this.getContentResolver();
                    ContentValues localContentValues =
                            OtherUtils.getVideoContentValues(new File(videoDir), System.currentTimeMillis());
                    Uri localUri = localContentResolver.insert(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                    this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));
                    break;
                default:
                    Toast.makeText(this, "未知操作", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
