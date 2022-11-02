package com.hcan53.android.product.component.blur;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.hcan53.android.product.R;
import com.hcan53.android.product.base.BaseActivity;
import com.hcan53.android.utils.FastBlurUtils;

/**
 * @author: HCan
 * @date: 2021/7/8
 */
public class FastBlurActivity extends BaseActivity {
    private ImageView right_top;

    @Override
    protected int getContentViewId() {
        return R.layout.open_window_activity;
    }

    @Override
    protected void initView() {
        right_top = findViewById(R.id.right_top);

        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.mipmap.splash);
        bmp1 = FastBlurUtils.doBlur(bmp1, 0, true);
        right_top.setImageBitmap(bmp1);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void toastMessage(String message) {

    }

    public static void intentActivity(Activity ac) {
        ac.startActivity(new Intent(ac, FastBlurActivity.class));
    }
}
