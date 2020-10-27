package com.hcan53.android.http.request;


import com.hcan53.android.http.api.ApiService;
import com.hcan53.android.http.callback.RequestCallBack;
import com.hcan53.android.http.common.RxSchedulers;
import com.hcan53.android.http.observer.CallbackObserver;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * <p>Created by Fenghj on 2018/6/6.</p>
 */
public class PostRequest extends BaseRequest {
    protected Map<String, Object> forms = new LinkedHashMap<>();
    protected String json;               //上传的Json
    protected RequestBody requestBody;   //自定义的请求体

    public PostRequest(String url) {
        super(url);
    }

    protected <T> Observable<String> execute() {
        if(json != null && !"".equals(json)) {
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), this.json);
            return create(ApiService.class).postBody(url, body);
        } else if(requestBody != null) {
            return create(ApiService.class).postBody(url, requestBody);
        } else {
            return create(ApiService.class).postForm(url, forms);
        }
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

    /**
     * post基本类型参数
     */
    public PostRequest upForms(Map<String, Object> forms) {
        if (forms != null) {
            this.forms.putAll(forms);
        }
        return this;
    }

    /**
     * post基本类型参数
     */
    public PostRequest upForms(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            this.forms.put(paramKey, paramValue);
        }
        return this;
    }

    /**
     * post json
     */
    public PostRequest upJson(String json) {
        this.json = json;
        return this;
    }

    /**
     * post json
     */
    public PostRequest upBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }
}
