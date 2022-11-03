package com.hcan53.android.http;


import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;

import com.hcan53.android.http.request.BaseRequest;
import com.hcan53.android.http.request.DownloadListener;
import com.hcan53.android.http.request.DownloadRequest;
import com.hcan53.android.http.request.GetRequest;
import com.hcan53.android.http.request.PostRequest;
import com.hcan53.android.http.request.UploadRequest;
import com.hcan53.android.http.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>Created by Fenghj on 2018/6/4.</p>
 */
public class HttpUtils {
    private String mBaseUrl = ""; //全局BaseUrl
    private volatile static HttpUtils singleton = null;
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private HttpUtils() {

    }

    public static HttpUtils getInstance() {
        if (singleton == null) {
            synchronized (HttpUtils.class) {
                if (singleton == null) {
                    singleton = new HttpUtils();
                }
            }
        }
        return singleton;
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(@NonNull final Application app) {
        if (sApplication == null) {
            HttpUtils.sApplication = app;
        }
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
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

    /**
     * 全局设置baseurl
     */
    public HttpUtils setBaseUrl(String baseUrl) {
        if(!StringUtils.isEmpty(baseUrl)) {
            mBaseUrl = baseUrl;
        }
        return this;
    }

    /**
     * 获取全局baseurl
     */
    public static String getBaseUrl() {
        return getInstance().mBaseUrl;
    }

    public static BaseRequest custom() {
        return new BaseRequest();
    }

    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    public static PostRequest post(String url) {
        return new PostRequest(url);
    }

    public static UploadRequest upload(String url) {
        return new UploadRequest(url);
    }
    public static DownloadRequest downLoad(String url) {
        return downLoad(url, null);
    }

    public static DownloadRequest downLoad(String url, DownloadListener listener) {
        return new DownloadRequest(url, listener);
    }

}
