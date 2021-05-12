package com.hcan53.android.screen.record;

/**
 * Created by HCan on 2021/5/12
 * 录屏监听
 */
public interface OnScreenRecordListener {
    /**
     * 返回是否成功
     *
     * @param success
     * @param filePath 文件存储路径
     */
    void screenRecord(boolean success, String filePath);
}
