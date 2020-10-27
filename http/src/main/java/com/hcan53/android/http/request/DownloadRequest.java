package com.hcan53.android.http.request;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.hcan53.android.http.api.ApiService;
import com.hcan53.android.http.callback.RequestCallBack;
import com.hcan53.android.http.common.RxSchedulers;
import com.hcan53.android.http.observer.CallbackObserver;
import com.hcan53.android.http.utils.SDCardUtils;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * <p>Created by Fenghj on 2018/6/7.</p>
 */
public class DownloadRequest extends BaseRequest {
    private DownloadListener downloadListener;
    private String dirName;
    private String fileName;
    protected Map<String, String> params = new LinkedHashMap<>();//请求参数

    public DownloadRequest(Context context, String url) {
        this(context, url, null);
    }

    public DownloadRequest(Context context, String url, DownloadListener listener) {
        super(url, listener);
        downloadListener = listener;
        dirName = SDCardUtils.getCachePath(context, Environment.DIRECTORY_DOWNLOADS);
        fileName = "download_file.tmp";
    }

    protected <T> Observable<ResponseBody> execute() {
        return create(ApiService.class).downloadFile(url, params);
    }

    public void execute(RequestCallBack<File> callback) {
        this.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())//需要
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return saveFile(responseBody.byteStream(), dirName, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallbackObserver<>(callback));
    }

    public <E> void execute(LifecycleProvider<E> lifecycleProvider, RequestCallBack<File> callback) {
        this.execute()
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(Schedulers.computation())//需要
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return saveFile(responseBody.byteStream(), dirName, fileName);
                    }
                })
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new CallbackObserver<>(callback));
    }

    public <E> void execute(LifecycleProvider<E> lifecycleProvider, E activityEvent, RequestCallBack<File> callback) {
        this.execute()
                .compose(lifecycleProvider.bindUntilEvent(activityEvent))
                .observeOn(Schedulers.computation())//需要
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return saveFile(responseBody.byteStream(), dirName, fileName);
                    }
                })
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new CallbackObserver<>(callback));
    }

    /**
     * 添加请求参数
     *
     * @param paramKey   请求参数
     * @param paramValue 参数值
     * @return DownloadRequest
     */
    public DownloadRequest addParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            this.params.put(paramKey, paramValue);
        }
        return this;
    }

    /**
     * 设置文件夹路径
     *
     * @param dirName 文件夹路径
     * @return DownloadRequest
     */
    public DownloadRequest setDirName(String dirName) {
        if (!TextUtils.isEmpty(dirName)) {
            this.dirName = dirName;
        }
        return this;
    }

    /**
     * 设置文件名称
     *
     * @param fileName 文件名称
     * @return DownloadRequest
     */
    public DownloadRequest setFileName(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            this.fileName = fileName;
        }
        return this;
    }

    public File saveFile(InputStream inputStream, String destFileDir, String destFileName) {
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();

            return file;

        } catch (IOException e) {
            if (downloadListener != null) downloadListener.onFail("IOException");
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}