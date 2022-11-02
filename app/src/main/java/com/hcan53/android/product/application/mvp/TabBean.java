package com.hcan53.android.product.application.mvp;

import androidx.fragment.app.Fragment;

import java.io.Serializable;

/**
 * Created by HCan on 2021/5/13
 */
public class TabBean implements Serializable {
    private String tabName;
    private int tabIcon;
    private Fragment fragment;

    public TabBean() {
    }

    public TabBean(String tabName, int tabIcon, Fragment fragment) {
        this.tabName = tabName;
        this.tabIcon = tabIcon;
        this.fragment = fragment;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getTabIcon() {
        return tabIcon;
    }

    public void setTabIcon(int tabIcon) {
        this.tabIcon = tabIcon;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
