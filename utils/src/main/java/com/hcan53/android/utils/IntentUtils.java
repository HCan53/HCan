package com.hcan53.android.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by HC on 2018/12/14.
 * 意图相关工具类
 */

public class IntentUtils {
    private IntentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取打开App的意图
     *
     * @param packageName 包名
     * @return intent
     */
    public static Intent getLaunchAppIntent(String packageName) {
        Intent intent = UtilsInit.getApp().getPackageManager().getLaunchIntentForPackage(packageName);
        return intent;
    }

    /**
     * 获取安装App意图.
     * <p>api大于等于25，需要权限
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath 安装文件路径.
     * @return 安装app intent
     */
    public static Intent getInstallAppIntent(final String filePath) {
        return getInstallAppIntent(FileUtils.getFileByPath(filePath), false);
    }

    /**
     * 获取安装App意图.
     * <p>api大于等于25，需要权限
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file 安装文件.
     * @return 安装app intent
     */
    public static Intent getInstallAppIntent(final File file) {
        return getInstallAppIntent(file, false);
    }

    /**
     * 获取安装App意图.
     * <p>api大于等于25，需要权限
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath 安装文件路径.
     * @param isNewTask true to add flag of new task, false otherwise.
     * @return 安装app intent
     */
    public static Intent getInstallAppIntent(final String filePath, final boolean isNewTask) {
        return getInstallAppIntent(FileUtils.getFileByPath(filePath), isNewTask);
    }

    /**
     * 获取安装App意图.
     * <p>api大于等于25，需要权限
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file 安装文件.
     * @param isNewTask true to add flag of new task, false otherwise.
     * @return 安装app intent
     */
    public static Intent getInstallAppIntent(final File file, final boolean isNewTask) {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < 24) {
            data = Uri.fromFile(file);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String authority = UtilsInit.getApp().getPackageName() + ".fileprovider";
            data = FileProvider.getUriForFile(UtilsInit.getApp(), authority, file);
        }
        intent.setDataAndType(data, type);
        return getIntent(intent, isNewTask);
    }

    /**
     * 获取跳至拨号界面意图
     *
     * @param phoneNumber 电话号码
     */
    public static Intent getDialIntent(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取拨打电话意图
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
     *
     * @param phoneNumber 电话号码
     */
    public static Intent getCallIntent(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取跳至发送短信界面的意图
     *
     * @param phoneNumber 接收号码
     */
    public static Intent getSendSmsIntent(final String phoneNumber) {
        return getSendSmsIntent(phoneNumber, "");
    }

    /**
     * 获取跳至发送短信界面的意图
     *
     * @param phoneNumber 接收号码
     * @param content     短信内容
     */
    public static Intent getSendSmsIntent(final String phoneNumber, final String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取跳至发邮件的意图
     *
     * @param mailAdress 接收邮箱地址
     */
    public static Intent getSendMailIntent(final String mailAdress) {
        Uri uri = Uri.parse("mailto:" + mailAdress);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取打开相机拍照的意图（兼容7.0）
     * <p>需添加权限 :
     *     <br>{@code <uses-permission android:name="android.permission.CAMERA"/>}</br>
     *     <br>{@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}</br>
     * </p>
     *
     * @param file 拍照保存图片的文件
     */
    @SuppressLint("QueryPermissionsNeeded")
    public static Intent getCameraIntent(File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(UtilsInit.getApp().getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT < 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            } else {
                //兼容7.0调用相机
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                Uri uri = UtilsInit.getApp().getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        } else {
            Toast.makeText(UtilsInit.getApp(), "没有系统相机", Toast.LENGTH_SHORT).show();
        }
        return intent;
    }

    private static Intent getIntent(final Intent intent, final boolean isNewTask) {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }
}
