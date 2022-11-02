package com.hcan53.android.product.component.splash.mvp;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created By HCan on 2021/2/6
 */
@SuppressLint("ParcelCreator")
public class SplashBean implements Parcelable {
    /**
     * 最新APP版本号
     */
    private String appVersion;
    /**
     * APP下载地址
     */
    private String appDownLoadUrl;
    /**
     * 是否为强制更新
     * 1：是
     * 0：不是
     */
    private String isForcible;
    /**
     * 用户服务协议与隐私政策版本号
     */
    private String protocolVersion;
    /**
     * 用户服务协议链接
     */
    private String userServiceUrl;
    /**
     * 隐私政策链接
     */
    private String privacyUrl;

    /**
     * 启动页信息
     */
    private List<SplashInfo> splashList;

    /**
     * 新手引导图片
     */
    private List<String> helpImgs;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppDownLoadUrl() {
        return appDownLoadUrl;
    }

    public void setAppDownLoadUrl(String appDownLoadUrl) {
        this.appDownLoadUrl = appDownLoadUrl;
    }

    public String getIsForcible() {
        return isForcible;
    }

    public void setIsForcible(String isForcible) {
        this.isForcible = isForcible;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getUserServiceUrl() {
        return userServiceUrl;
    }

    public void setUserServiceUrl(String userServiceUrl) {
        this.userServiceUrl = userServiceUrl;
    }

    public String getPrivacyUrl() {
        return privacyUrl;
    }

    public void setPrivacyUrl(String privacyUrl) {
        this.privacyUrl = privacyUrl;
    }

    public List<SplashInfo> getSplashList() {
        return splashList;
    }

    public void setSplashList(List<SplashInfo> splashList) {
        this.splashList = splashList;
    }

    public List<String> getHelpImgs() {
        return helpImgs;
    }

    public void setHelpImgs(List<String> helpImgs) {
        this.helpImgs = helpImgs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static class SplashInfo implements Parcelable {
        private String title;
        private String url;
        private String image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }
    }

}
