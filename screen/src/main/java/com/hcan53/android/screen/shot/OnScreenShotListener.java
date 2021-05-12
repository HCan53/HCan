package com.hcan53.android.screen.shot;

/**
 * 录屏开启监听
 */
public interface OnScreenShotListener {
    /**
     * 返回是否成功
     *
     * @param success
     * @param filePath 文件存储路径
     */
    void screenShot(boolean success, String filePath);
}
