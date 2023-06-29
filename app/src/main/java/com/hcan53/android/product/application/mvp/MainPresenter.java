package com.hcan53.android.product.application.mvp;


import com.hcan53.android.product.R;
import com.hcan53.android.product.application.fragment.DataFragment;
import com.hcan53.android.product.application.fragment.FrameworkFragment;
import com.hcan53.android.product.application.fragment.HomeFragment;
import com.hcan53.android.product.application.fragment.SetFragment;
import com.hcan53.android.product.base.BasePresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HCan on 2021/5/13
 */
public class MainPresenter extends BasePresenter<MainContract.View, ActivityEvent> implements MainContract.Preserent {
    @Override
    public void getTabBeans() {
        List<TabBean> tabBeans = new ArrayList<>();
        TabBean tabBean1 = new TabBean("首页", R.drawable.main_tab_home_icon_selector, HomeFragment.newInstance());
        TabBean tabBean2 = new TabBean("数据", R.drawable.main_tab_data_icon_selector, DataFragment.newInstance());
        TabBean tabBean3 = new TabBean("框架", R.drawable.main_tab_framework_icon_selector, FrameworkFragment.newInstance());
        TabBean tabBean4 = new TabBean("设置", R.drawable.main_tab_set_icon_selector, SetFragment.newInstance());
        tabBeans.add(tabBean1);
        tabBeans.add(tabBean2);
        tabBeans.add(tabBean3);
        tabBeans.add(tabBean4);
        if (getView() != null){
            getView().showTabs(tabBeans);
        }
    }


}
