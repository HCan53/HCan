package com.hcan53.android.product.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxFragment;

public abstract class BaseFragment<P extends BasePresenter> extends RxFragment implements IView {
    protected P presenter;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater == null) return null;
        if(getContentViewId() != 0) {
            return inflater.inflate(getContentViewId(), container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPresenter();
        if (presenter != null) {
            presenter.attachView(this);
            presenter.attachLifecycle(this);
        }
        initView(view);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    protected abstract int getContentViewId();
    protected abstract void initView(View view);
    protected abstract void initData();
}