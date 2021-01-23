package com.hcan53.android.component.download.view;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.hcan53.android.product.R;
import com.hcan53.android.utils.DensityUtils;


/**
 * 下载进度
 */

public class DownLoadProgressDialog extends AlertDialog {
    private TextView progress;

    public DownLoadProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_progress);
        setCanceledOnTouchOutside(false);
        setCancelable(false);//点击返回键不消失
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager windowManager = window.getWindowManager();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        lp.width = DensityUtils.dp2px(100); //设置宽度
        lp.gravity = Gravity.CENTER;
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setAttributes(lp);
        progress = findViewById(R.id.txt_progress);
    }

    public void changePro(int pro) {
        progress.setText("已下载" + pro + "%");
        if (pro == 100) {
            dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
