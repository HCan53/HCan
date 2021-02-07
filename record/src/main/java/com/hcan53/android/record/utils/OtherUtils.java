package com.hcan53.android.record.utils;

import android.content.ContentValues;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created By HCan on 2021/2/6
 */
public class OtherUtils {
    /**
     * 时间戳转换
     *
     * @param t
     * @return
     */
    public static String formatDate(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.format(t);
    }

    /**
     * 获取录屏文件信息
     *
     * @param paramFile
     * @param paramLong
     * @return
     */
    public static ContentValues getVideoContentValues(File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/mp4");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        return localContentValues;
    }
}
