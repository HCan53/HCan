package com.hcan53.android.http.request;

import com.hcan53.android.http.api.ApiService;
import com.hcan53.android.http.callback.RequestCallBack;
import com.hcan53.android.http.common.RxSchedulers;
import com.hcan53.android.http.observer.CallbackObserver;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * <p>Created by Fenghj on 2018/6/5.</p>
 */
public class GetRequest extends BaseRequest {
    protected Map<String, String> params = new LinkedHashMap<>();//请求参数

    public GetRequest(String url){
        super(url);
    }

    protected <T> Observable<String> execute() {
        return create(ApiService.class).get(url, params);
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
     * 添加请求参数
     *
     * @param paramKey   请求参数
     * @param paramValue 参数值
     * @return GetRequest
     */
    public GetRequest addParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            this.params.put(paramKey, paramValue);
        }
        return this;
    }

    public GetRequest addParams(Map<String, String> params) {
        if (params != null) {
            this.params.putAll(params);
        }
        return this;
    }
}
