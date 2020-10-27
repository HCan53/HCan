package com.hcan53.android.http.request;
import com.hcan53.android.http.api.ApiService;
import com.hcan53.android.http.callback.RequestCallBack;
import com.hcan53.android.http.common.RxSchedulers;
import com.hcan53.android.http.observer.CallbackObserver;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * <p>Created by Fenghj on 2018/6/6.</p>
 */
public class UploadRequest extends BaseRequest {
    protected List<MultipartBody.Part> multipartBodyParts = new ArrayList<>();

    public UploadRequest(String url){
        super(url);
    }

    protected <T> Observable<String> execute() {
        return create(ApiService.class).uploadFiles(url, multipartBodyParts);
    }

    public void execute(RequestCallBack<String> callback) {
        this.execute()
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new CallbackObserver<>(callback));
    }

    public <E> void execute(LifecycleProvider<E> lifecycleProvider, RequestCallBack<String> callback) {
        this.execute()
                .compose(lifecycleProvider.bindToLifecycle())
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new CallbackObserver<>(callback));
    }

    public <E> void execute(LifecycleProvider<E> lifecycleProvider, E activityEvent, RequestCallBack<String> callback) {
        this.execute()
                .compose(lifecycleProvider.bindUntilEvent(activityEvent))
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new CallbackObserver<>(callback));
    }

    public UploadRequest addParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            multipartBodyParts.add(MultipartBody.Part.createFormData(paramKey, paramValue));
        }
        return this;
    }

    public UploadRequest addFiles(Map<String, File> fileMap) {
        if (fileMap == null) {
            return this;
        }
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            addFile(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public UploadRequest addFile(String key, File file) {
        if (key == null || file == null) {
            return this;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
        this.multipartBodyParts.add(part);
        return this;
    }

    public UploadRequest addImageFiles(Map<String, File> fileMap) {
        if (fileMap == null) {
            return this;
        }
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            addImageFile(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public UploadRequest addImageFile(String key, File file) {
        if (key == null || file == null) {
            return this;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
        this.multipartBodyParts.add(part);
        return this;
    }
}
