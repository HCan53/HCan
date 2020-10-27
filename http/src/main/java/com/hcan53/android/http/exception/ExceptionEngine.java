package com.hcan53.android.http.exception;

import android.net.ParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * 错误/异常处理工具
 */
public class ExceptionEngine {

    //未知错误
    public static final int UNKNOWN = 1000;
    //解析错误
    public static final int PARSE_ERROR = 1001;
    //网络错误
    public static final int NETWORK_ERROR = 1002;
    //证书出错
    public static final int SSL_ERROR = 1003;
    //连接超时
    public static final int TIMEOUT_ERROR = 1004;
    //无法解析该域名
    public static final int UNKNOWNHOST_ERROR = 1005;
    //协议出错
    public static final int HTTP_ERROR = 1006;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpExc = (HttpException) e;
//            ex = new ApiException(httpExc.code(), httpExc.message());
            ex = new ApiException(HTTP_ERROR, httpExc.message());
            return ex;
        } else if (e instanceof JSONException
                || e instanceof ParseException) {  //解析数据错误
            ex = new ApiException(PARSE_ERROR, "解析错误");
            return ex;
        } else if (e instanceof ConnectException) {//连接网络错误
            ex = new ApiException(NETWORK_ERROR, "连接失败");
            return ex;
        } else if (e instanceof SSLHandshakeException) {//连接网络错误
            ex = new ApiException(SSL_ERROR, "证书验证失败");
            return ex;
        } else if (e instanceof SocketTimeoutException
                || e instanceof ConnectTimeoutException) {//网络超时
            ex = new ApiException(TIMEOUT_ERROR, "连接超时");
            return ex;
        } else if (e instanceof UnknownHostException) {//连接网络错误
//            ex = new ApiException(UNKNOWNHOST_ERROR, "无法解析该域名");
            ex = new ApiException(NETWORK_ERROR, "连接失败");
            return ex;
        } else {  //未知错误
            ex = new ApiException(UNKNOWN, "未知错误");
            return ex;
        }
    }

}