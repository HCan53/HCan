package com.hcan53.android.product.component.splash;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hcan53.android.permissions.RxPermissions;
import com.hcan53.android.product.R;
import com.hcan53.android.product.component.splash.mvp.SplashBean;
import com.hcan53.android.product.component.splash.mvp.SplashContract;
import com.hcan53.android.product.component.splash.mvp.SplashPresenter;
import com.hcan53.android.product.base.BaseActivity;
import com.hcan53.android.record.ScreenRecordService;
import com.hcan53.android.utils.BarUtils;

import java.util.List;


public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    private ImageView img_splash;
    private TextView txt_splash;

    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.splash_activity;
    }

    @Override
    protected void initView() {
        BarUtils.hideStatusBar(this, true);
        img_splash = findViewById(R.id.img_splash);
        txt_splash = findViewById(R.id.txt_splash);
        rxPermissions = new RxPermissions(this);
    }

    @Override
    protected void initData() {
        presenter.requestSplash();
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {

        });
        img_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenRecordService.enqueueWork(SplashActivity.this, ScreenRecordService.START);
            }
        });
        txt_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenRecordService.enqueueWork(SplashActivity.this, ScreenRecordService.STOP);
            }
        });
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

    }

    @Override
    public void showSplash(SplashBean splashBean) {
        if (splashBean != null) {
            List<SplashBean.SplashInfo> list = splashBean.getList();
            if (list != null && list.size() > 0) {
                txt_splash.setText(list.get(0).getTitle());
                Glide.with(this).load(list.get(0).getImage()).into(img_splash);
            }
        }
    }
}
