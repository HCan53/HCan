package com.hcan53.android.product.application.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.hcan53.android.product.R;
import com.hcan53.android.product.base.BaseFragment;
import com.hcan53.android.product.base.Typefaces;
import com.hcan53.android.views.titanic.Titanic;
import com.hcan53.android.views.titanic.TitanicTextView;

/**
 * Created by HCan on 2021/5/13
 */
public class DataFragment extends BaseFragment {
    TitanicTextView titanicTextView;
    @Override
    protected int getContentViewId() {
        return R.layout.main_data_fragment;
    }

    @Override
    protected void initView(View view) {
        titanicTextView = view.findViewById(R.id.txt_titanic);
        titanicTextView.setTypeface(Typefaces.get(getActivity(), "mb.TTF"));
        new Titanic().start(titanicTextView);
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

    public static Fragment newInstance(){
        DataFragment fragment = new DataFragment();
        return fragment;
    }
}
