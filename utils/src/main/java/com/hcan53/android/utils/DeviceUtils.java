package com.hcan53.android.utils;

import android.os.Build;

/**
 * Created by HC on 2018/12/14.
 * 设备相关工具类
 */

public class DeviceUtils {
    /**
     * 获取设备系统版本号
     * <p>e.g. 6.0</p>
     *
     * @return 系统版本号
     */
    public static String getSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备系统版本码
     * <p>e.g. 23</p>
     *
     * @return 系统版本码
     */
    public static int getSDKVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备厂商
     * <p>e.g. Xiaomi</p>
     *
     * @return 设备厂商名
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     * <p>e.g. MI2SC</p>
     *
     * @return 设备型号名
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }
}
