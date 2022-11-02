package com.hcan53.android.product.application.fragment;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hcan53.android.permissions.RxPermissions;
import com.hcan53.android.product.R;
import com.hcan53.android.product.base.BaseFragment;
import com.hcan53.android.product.component.facecropper.FaceCropperActivity;
import com.hcan53.android.product.component.record.RecordActivity;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;


/**
 * Created by HCan on 2021/5/13
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private TextView txt_screen;
    private TextView txt_scan;
    private TextView txt_face_croppper;

    public static final int REQUEST_CODE_SCAN_ONE = 100;
    private RxPermissions rxPermissions;

    @Override
    protected int getContentViewId() {
        return R.layout.main_home_fragment;
    }

    @Override
    protected void initView(View view) {
        txt_screen = view.findViewById(R.id.txt_screen);
        txt_scan = view.findViewById(R.id.txt_scan);
        txt_face_croppper = view.findViewById(R.id.txt_face_croppper);

        txt_screen.setOnClickListener(this);
        txt_scan.setOnClickListener(this);
        txt_face_croppper.setOnClickListener(this);

        rxPermissions = new RxPermissions(this);
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
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_screen:
                RecordActivity.intentActivity(getActivity());
                break;
            case R.id.txt_scan:
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                    ScanUtil.startScan(getActivity(), REQUEST_CODE_SCAN_ONE,
                            new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE).create());
                });
                break;
            case R.id.txt_face_croppper:
                FaceCropperActivity.intentActivity(getActivity());
                break;
        }
    }

}
