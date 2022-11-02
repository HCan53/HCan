package com.hcan53.android.product.application.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hcan53.android.product.R;
import com.hcan53.android.product.base.BaseFragment;

/**
 * Created by HCan on 2021/5/13
 */
public class FrameworkFragment extends BaseFragment implements View.OnClickListener {
    private TextView txt_kraken;

    @Override
    protected int getContentViewId() {
        return R.layout.main_framework_fragment;
    }

    @Override
    protected void initView(View view) {
        txt_kraken = view.findViewById(R.id.txt_kraken);
    }

    @Override
    protected void initData() {
        txt_kraken.setOnClickListener(this);
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
        FrameworkFragment fragment = new FrameworkFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_kraken:
               
                break;
        }
    }
}
