package com.hcan53.android.product.application.mvp;

import com.hcan53.android.product.base.IView;

import java.util.List;

/**
 * Created by HCan on 2021/5/13
 */
public class MainContract {
    public interface View extends IView {
        void showTabs(List<TabBean> beanList);

        void addShortcuts();
    }

    public interface Preserent {
        void getTabBeans();

    }
}
