package com.hcan53.android.http.callback;

public abstract class DownloadCallBack<T> extends RequestCallBack<T> {
    public abstract void onProgress(int progress, long total);
}