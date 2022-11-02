package com.hcan53.android.product.application.fragment;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hcan53.android.product.R;
import com.hcan53.android.product.base.BaseFragment;
import com.hcan53.android.product.component.blur.FastBlurActivity;
import com.hcan53.android.product.component.download.activity.DownLoadActivity;

/**
 * Created by HCan on 2021/5/13
 */
public class SetFragment extends BaseFragment implements View.OnClickListener {
    private TextView txt_barrier_free;
    private TextView txt_open_window;
    private TextView txt_download_apk;

    @Override
    protected int getContentViewId() {
        return R.layout.main_set_fragment;
    }

    @Override
    protected void initView(View view) {
        txt_barrier_free = view.findViewById(R.id.txt_barrier_free);
        txt_open_window = view.findViewById(R.id.txt_open_window);
        txt_download_apk = view.findViewById(R.id.txt_download_apk);
        txt_barrier_free.setOnClickListener(this);
        txt_open_window.setOnClickListener(this);
        txt_download_apk.setOnClickListener(this);
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

    public static Fragment newInstance() {
        SetFragment fragment = new SetFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_barrier_free:
                Intent bfIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                this.startActivity(bfIntent);
                break;
            case R.id.txt_open_window:
                FastBlurActivity.intentActivity(getActivity());
                break;
            case R.id.txt_download_apk:
                DownLoadActivity.intentActivity(getActivity());
                break;
        }
    }
}
