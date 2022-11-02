package com.HCan.android.draft;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hcan.android.album.MultiImageSelector;
import com.hcan.android.album.MultiImageSelectorActivity;
import com.hcan53.android.permissions.RxPermissions;
import com.hcan53.android.utils.BarUtils;
import com.hcan53.android.utils.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class ThanDraftActivity extends RxAppCompatActivity {

    private ImageView img_than_draft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewId() != 0) {
            setContentView(getContentViewId());
        }
        BarUtils.setStatusBarColor(this, Color.parseColor("#FFFFFF"), true);
        initView();
        initData();
    }

    protected int getContentViewId() {
        return R.layout.than_draft_activity;
    }

    protected void initView() {
        BarUtils.hideStatusBar(this, false);
        img_than_draft = findViewById(R.id.img_than_draft);
    }

    protected void initData() {

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        intentSelectImg();
                    } else {
                        ToastUtils.showShort("您已拒绝权限，无法使用选择相册组件");
                    }
                });

    }

    public void intentSelectImg() {
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        startActivityForResult(intent, 1111);
    }


    private String locImgPath = "";

    /**
     * 选择媒体后的回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1) {
            if (requestCode == 1111) {
                ArrayList<String> mSelectPath = intent.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    locImgPath = mSelectPath.get(0);
                    Glide.with(this).load(Uri.fromFile(new File(locImgPath))).into(img_than_draft);
                }
            }
        }
    }


    public static void intentActivity(Activity ac) {
        ac.startActivity(new Intent(ac, ThanDraftActivity.class));
    }
}
