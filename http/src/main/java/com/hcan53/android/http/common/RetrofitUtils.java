package com.hcan53.android.http.common;



import com.hcan53.android.http.HttpUtils;
import com.hcan53.android.http.request.DownloadListener;
import com.hcan53.android.http.utils.HCLogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * <p>Created by Fenghj on 2018/6/5.</p>
 */
public class RetrofitUtils {
    private String baseUrl; //接口地址
    public static final String TAG                  = "complat_http";
    public static final int     CONNECT_TIME_OUT     = 30;   //连接超时时长x秒
    public static final int     READ_TIME_OUT        = 30;   //读数据超时时长x秒
    public static final int     WRITE_TIME_OUT       = 30;   //写数据接超时时长x秒

    private volatile static RetrofitUtils mInstance;

    private RetrofitUtils() {
        baseUrl = HttpUtils.getBaseUrl();
    }

    public static RetrofitUtils getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtils.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置okHttp
     *
     * @author Fenghj
     */
    private OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(createLogInterceptor())
                .build();
    }

    private OkHttpClient okHttpClient(DownloadListener downloadListener) {
        DownloadInterceptor mInterceptor = new DownloadInterceptor(downloadListener);
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(mInterceptor)
                .addInterceptor(createLogInterceptor())
                .build();
    }

    private HttpLoggingInterceptor createLogInterceptor() {
        HttpLoggingInterceptor.Logger logger = message -> HCLogUtil.dTag(TAG, message);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    public Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder()
                .client(okHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    public Retrofit.Builder getRetrofitBuilder(DownloadListener downloadListener) {
        return new Retrofit.Builder()
                .client(okHttpClient(downloadListener))
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    /**
     * 获取Retrofit
     *
     * @author Fenghj
     */
    public Retrofit retrofit() {
        return getRetrofitBuilder().build();
    }

    /**
     * 获取下载带进度监听的Retrofit
     * @param downloadListener 下载监听
     * @return Retrofit对象
     */
    public Retrofit retrofit(DownloadListener downloadListener) {
        return getRetrofitBuilder(downloadListener).build();
    }
}
