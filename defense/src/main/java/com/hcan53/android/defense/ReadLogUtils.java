package com.hcan53.android.defense;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HCan on 2021/5/13
 */
public class ReadLogUtils {
    public static String readThrowable(Throwable throwable) {
        //获取崩溃时的UNIX时间戳
        long timeMillis = System.currentTimeMillis();
        //将时间戳转换成人类能看懂的格式，建立一个String拼接器
        StringBuilder stringBuilder = new StringBuilder(
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(timeMillis)));
        stringBuilder.append(":\n");
        //获取错误信息
        stringBuilder.append(throwable.getMessage());
        stringBuilder.append("\n");
        //获取堆栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        stringBuilder.append(sw.toString());
        //这就是完整的错误信息了，你可以拿来上传服务器，或者做成本地文件保存等等等等
        String errorLog = stringBuilder.toString();
        return errorLog;
    }
}
