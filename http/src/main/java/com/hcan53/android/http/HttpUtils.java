package com.hcan53.android.http;


import com.hcan53.android.http.request.BaseRequest;
import com.hcan53.android.http.request.DownloadListener;
import com.hcan53.android.http.request.DownloadRequest;
import com.hcan53.android.http.request.GetRequest;
import com.hcan53.android.http.request.PostRequest;
import com.hcan53.android.http.request.UploadRequest;
import com.hcan53.android.http.utils.StringUtils;

/**
 * <p>Created by Fenghj on 2018/6/4.</p>
 */
public class HttpUtils {
    private String mBaseUrl = "http://jmpotal.hanweb.com/jmp/"; //全局BaseUrl
    private volatile static HttpUtils singleton = null;

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
