package com.hcan53.android.product.component.splash.mvp;

import com.hcan53.android.http.HttpUtils;
import com.hcan53.android.http.callback.RequestCallBack;
import com.hcan53.android.product.base.BaseConfig;

/**
 * Created By HCan on 2021/2/6
 */
public class SplashModel {

    /**
     * 请求启动页数据
     */
    public void requestSplash(RequestCallBack<String> callBack) {
        String url = BaseConfig.SPLASH_URL;
        HttpUtils.get(url).execute(callBack);
    }
}
