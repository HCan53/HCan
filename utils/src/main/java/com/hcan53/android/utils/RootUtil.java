package com.hcan53.android.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by HC on 2018/12/14.
 */
public class RootUtil {
    //判断手机是否root
//    public static boolean isRoot() {
//        String binPath = "/system/bin/su";
//        String xBinPath = "/system/xbin/su";
//
//        if (new File(binPath).exists() && isCanExecute(binPath)) {
//            return true;
//        }
//        if (new File(xBinPath).exists() && isCanExecute(xBinPath)) {
//            return true;
//        }
//        return false;
//    }

    public static boolean isRoot() {
        if (getRootAhth()) {
            return true;
        }else {
            return false;
        }

    }




    private static boolean isCanExecute(String filePath) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ls -l " + filePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    public synchronized static boolean getRootAhth()
    {
        Process process = null;
        DataOutputStream os = null;
        try
        {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0)
            {
                return true;
            } else
            {
                return false;
            }
        } catch (Exception e)
        {
            return false;
        }
        finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
