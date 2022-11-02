package com.hcan53.android.product.component.splash;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.hcan53.android.product.R;
import com.hcan53.android.product.application.MainActivity;
import com.hcan53.android.product.component.splash.mvp.SplashBean;
import com.hcan53.android.product.component.splash.mvp.SplashContract;
import com.hcan53.android.product.component.splash.mvp.SplashPresenter;
import com.hcan53.android.product.base.BaseActivity;
import com.hcan53.android.utils.BarUtils;
import com.hcan53.android.utils.StringUtils;
import com.hcan53.android.utils.ToastUtils;
import com.hcan53.android.views.progress.RoundProgressBar;

import java.util.List;


public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    private ImageView img_splash;
    private RelativeLayout jumpNext;
    private RoundProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar.cancelTimerCount();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.splash_activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        BarUtils.hideStatusBar(this, false);
        img_splash = findViewById(R.id.img_splash);
        jumpNext = findViewById(R.id.rl_jump_main_ac);
        progressBar = findViewById(R.id.rpb_go_next);


        img_splash.setOnClickListener(view -> {
            //广告跳转
        });
        jumpNext.setOnClickListener(view -> {
            //跳转至下一级页面
            goNextAc();
        });
        progressBar.setFinshListener(() -> {
            //自动跳转至下一级页面
            goNextAc();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initData() {
        presenter.requestSplash();
        progressBar.startTimerCount();
    }


    @Override
    public void setPresenter() {
        presenter = new SplashPresenter();
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void toastMessage(String message) {
        if (!StringUtils.isSpace(message))
            ToastUtils.showShort(message);
    }

    @Override
    public void showSplash(SplashBean splashBean) {
        if (splashBean != null) {
            List<SplashBean.SplashInfo> list = splashBean.getSplashList();
            if (list != null && list.size() > 0) {
                Glide.with(this).load(list.get(0).getImage()).into(img_splash);
            }
        }
    }

    /**
     * 跳转至主工程页面
     */
    private void goNextAc() {
        progressBar.cancelTimerCount();
        MainActivity.intentActivity(this);
        finish();
    }
}
