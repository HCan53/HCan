package com.hcan53.android.http.observer;


import com.hcan53.android.http.callback.RequestCallBack;
import com.hcan53.android.http.exception.ApiException;

/**
 * <p>Created by Fenghj on 2018/6/5.</p>
 */
public class CallbackObserver<T> extends BaseObserver<T> {
    protected RequestCallBack<T> callBack;

    public CallbackObserver(RequestCallBack<T> callBack) {
        if (callBack == null) {
            throw new NullPointerException("this callback is null!");
        }
        this.callBack = callBack;
    }

    @Override
    public void onSuccess(T response) {
        callBack.onSuccess(response);
    }

    @Override
    public void onFail(ApiException apiException) {
        if (apiException == null) {
            callBack.onFail(-1, "This ApiException is Null.");
            return;
        }
        callBack.onFail(apiException.getCode(), apiException.getMessage());
    }
}
