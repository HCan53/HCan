package com.hcan53.android.product.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * 懒加载的fragment
 * <p>Created by Fenghj on 2018/6/12.</p>
 */
public abstract class BaseLazyFragment<P extends BasePresenter> extends RxFragment implements IView {
    protected P presenter;
    //缓存Fragment view
    private View mRootView;
    private boolean mIsMulti = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getContentViewId(), container, false);

            setPresenter();
            if (presenter != null) {
                presenter.attachView(this);
                presenter.attachLifecycle(this);
            }
            initView();
            initData();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
            onFirstUserVisible();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && mIsMulti) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && mRootView != null && !mIsMulti) {
            mIsMulti = true;
            onFirstUserVisible();
        } else if (isVisibleToUser && mRootView != null && mIsMulti) {
            onUserVisible();
        } else if (!isVisibleToUser && mRootView != null && mIsMulti) {
            onUserInvisible();
        } else {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    protected abstract void onFirstUserVisible();

    /**
     * fragment可见（切换回来或者onResume）
     */
    protected abstract void onUserVisible();

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public abstract void onUserInvisible();

    protected abstract int getContentViewId();

    protected abstract void initView();

    protected abstract void initData();
}
