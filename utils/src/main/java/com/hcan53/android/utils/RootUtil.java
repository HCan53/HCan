package com.hcan53.android.utils;

import java.io.DataOutputStream;

/**
 * Created by HC on 2018/12/14.
 * 设备root状态监测
 */
public class RootUtil {

    public static boolean isRoot() {
        if (getRootAhth()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @return
     */
    public synchronized static boolean getRootAhth() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
