package com.hcan53.android.screen;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.hcan53.android.screen.record.ScreenRecordUtil;
import com.hcan53.android.screen.utils.ScreenUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by HC on 2018/12/14.
 * Utils初始化类, getApp() 可获取 ApplicationContext 对象.
 */
public class RecordInit {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private RecordInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    @SuppressLint("NewApi")
    public static void init(@NonNull final Context context) {
        init((Application) context.getApplicationContext());
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void init(@NonNull final Application app) {
        if (sApplication == null) {
            RecordInit.sApplication = app;
        }
        ScreenUtil.getScreenSize(sApplication);
        ScreenRecordUtil.getInstance().init(ScreenUtil.SCREEN_WIDTH, ScreenUtil.SCREEN_HEIGHT,
                2000000);
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    @SuppressLint("NewApi")
    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        }

        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(at);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            init((Application) app);
            return sApplication;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        throw new NullPointerException("u should init first");
    }
}
