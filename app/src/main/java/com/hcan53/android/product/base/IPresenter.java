package com.hcan53.android.product.base;

/**
 * <p>Created by Fenghj on 2018/6/11.</p>
 */
public interface IPresenter<V extends IView, E> {
    void attachView(V view);
    void detachView();
}
