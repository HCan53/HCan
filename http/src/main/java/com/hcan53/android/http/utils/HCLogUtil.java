package com.hcan53.android.http.utils;

import android.util.Log;


public final class HCLogUtil {
    public static String TAG = "HCLOG";
    public static boolean isDebug = false;

    public static void v(String msg) {
        if (!isDebug) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (!isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (!isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (!isDebug) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (!isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void vTag(String tag, String msg) {
        if (!isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void dTag(String tag, String msg) {
        if (!isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void iTag(String tag, String msg) {
        if (!isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void wTag(String tag, String msg) {
        if (!isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void eTag(String tag, String msg) {
        if (!isDebug) {
            Log.e(tag, msg);
        }
    }
}
