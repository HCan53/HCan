package com.hcan53.android.product.component.splash.mvp;


import com.hcan53.android.product.base.IView;

/**
 * Created By HCan on 2021/2/6
 */
public class SplashContract {
    public interface View extends IView {
        void showSplash(SplashBean splashBean);
    }

    public interface Presenter {
        void requestSplash();
    }
}
