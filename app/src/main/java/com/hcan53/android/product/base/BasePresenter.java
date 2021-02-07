package com.hcan53.android.product.base;

import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * <p>Created by Fenghj on 2018/6/11.</p>
 */
public class BasePresenter<V extends IView, E> {
    private Reference<V> mViewRef;//View接口类型弱引用
    private Reference<LifecycleProvider<E>> mLifecycleRef;

    public void attachView(V view) {
        mViewRef = new WeakReference<>(view); //建立关联
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    protected V getView() {
        if (mViewRef != null) {
            return mViewRef.get();//获取View
        }
        return null;
    }

    public void attachLifecycle(LifecycleProvider<E> lifecycle) {
        mLifecycleRef = new WeakReference<>(lifecycle);
    }

    public void detachLifecycle() {
        if (mLifecycleRef != null) {
            mLifecycleRef.clear();
            mLifecycleRef = null;
        }
    }

    protected LifecycleProvider<E> getLifecycle() {
        if (mLifecycleRef != null) {
            return mLifecycleRef.get();//获取Lifecycle
        }
        return null;
    }
}
