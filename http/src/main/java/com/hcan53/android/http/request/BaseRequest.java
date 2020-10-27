package com.hcan53.android.http.request;

import android.text.TextUtils;


import com.hcan53.android.http.common.RetrofitUtils;

import retrofit2.Retrofit;

/**
 * <p>Created by Fenghj on 2018/6/5.</p>
 */
public class BaseRequest {
    protected Retrofit mRetrofit;
    protected String url = "";

    public BaseRequest() {
        this("");
    }

    public BaseRequest(String url) {
        this(url, null);
    }

    public BaseRequest(String url, DownloadListener listener) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
        }
        if(listener != null) {
            mRetrofit = RetrofitUtils.getInstance().retrofit(listener);
        } else {
            mRetrofit = RetrofitUtils.getInstance().retrofit();
        }
    }

    /**
     * 创建api服务  可以支持自定义的api，默认使用BaseApiService,上层不用关心
     *
     * @param service 自定义的apiservice class
     */
    public <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }

}
