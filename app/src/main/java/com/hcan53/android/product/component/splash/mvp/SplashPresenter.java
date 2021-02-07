package com.hcan53.android.product.component.splash.mvp;

import com.hcan53.android.http.callback.RequestCallBack;
import com.hcan53.android.product.base.BasePresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By HCan on 2021/2/6
 */
public class SplashPresenter extends BasePresenter<SplashContract.View, ActivityEvent> implements SplashContract.Presenter {

    private SplashModel splashModel;

    public SplashPresenter() {
        splashModel = new SplashModel();
    }

    @Override
    public void requestSplash() {
        splashModel.requestSplash(new RequestCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    if (object.optString("result", "false").equals("true")) {
                        SplashBean splashBean = new SplashBean();
                        splashBean.setAppVersion(object.optString("appVersion", ""));
                        splashBean.setAppDownLoadUrl(object.optString("appDownLoadUrl", ""));
                        splashBean.setIsForcible(object.optString("isForcible", ""));
                        splashBean.setProtocolVersion(object.optString("protocolVersion", ""));
                        splashBean.setUserUrl(object.optString("userUrl", ""));
                        splashBean.setPrivacyUrl(object.optString("privacyUrl", ""));
                        JSONArray helpArr = object.optJSONArray("help");
                        List<String> help = new ArrayList<>();
                        if (helpArr != null && helpArr.length() > 0) {
                            for (int i = 0; i < helpArr.length(); i++) {
                                help.add(helpArr.optString(i, ""));
                            }
                        }
                        splashBean.setHelpimgs(help);
                        JSONArray splashArr = object.optJSONArray("splash");
                        List<SplashBean.SplashInfo> splash = new ArrayList<>();
                        if (splashArr != null && splashArr.length() > 0) {
                            for (int i = 0; i < splashArr.length(); i++) {
                                JSONObject splashObj = splashArr.optJSONObject(i);
                                SplashBean.SplashInfo splashInfo = new SplashBean.SplashInfo();
                                splashInfo.setTitle(splashObj.optString("title", ""));
                                splashInfo.setUrl(splashObj.optString("url", ""));
                                splashInfo.setImage(splashObj.optString("image", ""));
                                splash.add(splashInfo);
                            }
                        }
                        splashBean.setList(splash);
                        if (getView() != null) {
                            getView().showSplash(splashBean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errCode, String errMsg) {
                if (getView() != null) {
                    getView().showSplash(null);
                    getView().toastMessage(errMsg);
                }
            }
        });
    }
}
