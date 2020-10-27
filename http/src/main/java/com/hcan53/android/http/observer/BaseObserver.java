package com.hcan53.android.http.observer;



import com.hcan53.android.http.exception.ApiException;
import com.hcan53.android.http.exception.ExceptionEngine;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <p>Created by Fenghj on 2018/6/5.</p>
 */
public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) { }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFail(ExceptionEngine.handleException(e));
    }

    @Override
    public void onComplete() { }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);
    abstract public void onFail(ApiException apiException);
}
