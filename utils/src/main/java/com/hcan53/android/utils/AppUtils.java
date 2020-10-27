package com.hcan53.android.utils;

import android.content.Intent;
import android.text.TextUtils;

import java.io.File;

/**
 * App相关工具类
 * <p>Created by Fenghj on 2018/05/26.</p>
 */

public class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取App包名
     *
     * @return App包名
     */
    public static String getAppPackageName() {
        return UtilsInit.getApp().getPackageName();
    }

    /**
     * 判断App是否安装
     *
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(String packageName) {
        return !TextUtils.isEmpty(packageName) && IntentUtils.getLaunchAppIntent(packageName) != null;
    }

    /**
     * 打开App
     *
     * @param packageName 包名
     */
    public static void launchApp(String packageName) {
        if (TextUtils.isEmpty(packageName)) return;
        Intent intent = IntentUtils.getLaunchAppIntent(packageName);
        if(intent == null) return;
        UtilsInit.getApp().startActivity(intent);
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath The path of file.
     */
    public static void installApp(final String filePath) {
        installApp(FileUtils.getFileByPath(filePath));
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     */
    public static void installApp(final File file) {
        if (!FileUtils.isFileExists(file)) return;
        UtilsInit.getApp().startActivity(IntentUtils.getInstallAppIntent(file, true));
    }
}
