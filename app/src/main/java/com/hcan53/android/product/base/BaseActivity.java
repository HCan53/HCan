package com.hcan53.android.product.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hcan53.android.utils.StringUtils;
import com.hcan53.android.utils.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created By HCan on 2021/1/25
 */
public abstract class BaseActivity<P extends BasePresenter> extends RxAppCompatActivity implements IView {
    protected P presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewId() != 0) {
            setContentView(getContentViewId());
        }
        setPresenter();
        if (presenter != null) {
            presenter.attachView(this);
            presenter.attachLifecycle(this);
        }
        initView();
        initData();
    }

    protected abstract int getContentViewId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void setPresenter() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void toastMessage(String message) {
        if (!StringUtils.isEmpty(message))
            ToastUtils.showShort(message);
    }
}
