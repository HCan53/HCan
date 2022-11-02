package com.hcan53.android.product.base;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hcan53.android.utils.BarUtils;
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
        BarUtils.setStatusBarColor(this, Color.parseColor("#FFFFFF"), true);
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
    public void onBackPressed() {
       finish();
    }
}
