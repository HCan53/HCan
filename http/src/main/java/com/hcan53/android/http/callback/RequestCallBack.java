package com.hcan53.android.http.callback;

/**
 * 请求接口回调
 * <p>Created by Fenghj on 2018/6/5.</p>
 */
public abstract class RequestCallBack<T> {
    public abstract void onSuccess(T data);

    public abstract void onFail(int errCode, String errMsg);
}