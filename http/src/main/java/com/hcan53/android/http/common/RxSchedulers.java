package com.hcan53.android.http.common;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * <p>Created by Fenghj on 2018/4/11.</p>
 */

public class RxSchedulers {

    static final ObservableTransformer schedulersTransformer = upstream ->
            (upstream).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());


    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }
}
