package com.hcan53.android.http.request;

public interface DownloadListener {
    void onStartDownload(long length);
    void onProgress(int progress);
    void onFail(String errorInfo);
}