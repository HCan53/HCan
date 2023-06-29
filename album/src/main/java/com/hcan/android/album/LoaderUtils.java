package com.hcan.android.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import java.io.File;

/**
 * <p>Created by Fenghj on 2018/6/11.</p>
 */
public class LoaderUtils {

    public static class Builder {
        private ImageView imageView;
        private Target<Bitmap> bitmapTarget;
        private String imageUrl;                 //图片地址
        private File mFile;                      //图片本地文件
        private Integer resourceId = -1;         //图片资源文件
        private int placeholderResId;            //加载前占位图
        private int errorResId;                  //加载失败占位图
        private boolean hasCrossFade = true;  //是否加载渐变动画
        private int duration = 400;   //渐变动画时间，ms
        private boolean isCircle = false; //是否加载为圆形，默认false
        private boolean isDiskCache = true;  //是否磁盘缓存，默认true
        private boolean isMemory = true;  //是否内存缓存，默认true
        private boolean isCenterCrop = false;  //是否centerCrop，默认false
        private RequestListener<Drawable> listener;

        public Builder into(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder load(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder load(File file) {
            mFile = file;
            return this;
        }

        public Builder load(Integer resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder placeholder(int placeholderResId) {
            this.placeholderResId = placeholderResId;
            return this;
        }

        public Builder error(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder hasCrossFade(boolean hasCrossFade) {
            this.hasCrossFade = hasCrossFade;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder isCircle(boolean isCircle) {
            this.isCircle = isCircle;
            return this;
        }

        public Builder isDiskCache(boolean isDiskCache) {
            this.isDiskCache = isDiskCache;
            return this;
        }

        public Builder isMemory(boolean isMemory) {
            this.isMemory = isMemory;
            return this;
        }

        public Builder centerCrop() {
            this.isCenterCrop = true;
            return this;
        }

        public Builder listener(RequestListener<Drawable> listener) {
            this.listener = listener;
            return this;
        }


        private RequestManager getRequestManager() {
            return Glide.with(imageView.getContext());
        }

        private RequestBuilder<Drawable> getRequestBuilder() {
            if (mFile != null) {
                return getRequestManager().load(mFile);
            }
            if (resourceId > 0) {
                return getRequestManager().load(resourceId);
            }
            return getRequestManager().load(imageUrl);
        }

        private RequestBuilder<Bitmap> getBitmapRequestBuilder() {
            return getRequestManager().asBitmap().load(imageUrl);
        }

        @SuppressLint("CheckResult")
        private RequestOptions getRequestOptions() {
            RequestOptions ro = new RequestOptions();
            if (placeholderResId != 0) {
                ro.placeholder(placeholderResId);
            }
            if (errorResId != 0) {
                ro.error(errorResId);
            }
            if (isCircle) {
                ro.circleCrop();
            } else if (isCenterCrop) {
                ro.centerCrop();
            }
            if (isDiskCache) {
                ro.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            } else {
                ro.diskCacheStrategy(DiskCacheStrategy.NONE);
            }
            ro.skipMemoryCache(!isMemory);
            return ro;
        }

        public void show() {
            build(getRequestBuilder());
        }

        public void show(Target<Bitmap> target) {
            this.bitmapTarget = target;
            buildAsBitmap(getBitmapRequestBuilder());
        }


        @SuppressLint("CheckResult")
        private void build(RequestBuilder<Drawable> requestBuilder) {
            requestBuilder.apply(getRequestOptions());
            if (hasCrossFade) {
                DrawableCrossFadeFactory crossFade = new DrawableCrossFadeFactory.Builder(duration)
                        .setCrossFadeEnabled(true).build();
                requestBuilder.transition(new DrawableTransitionOptions().crossFade(crossFade));
            } else {
                requestBuilder.transition(new DrawableTransitionOptions().dontTransition());
            }
            if (listener != null) {
                requestBuilder.listener(listener);
            }
            requestBuilder.into(imageView);
        }

        @SuppressLint("CheckResult")
        private void buildAsBitmap(RequestBuilder<Bitmap> requestBuilder) {
            requestBuilder.apply(getRequestOptions());
            requestBuilder.into(bitmapTarget);
        }
    }

    /**
     * 清除glide内存缓存，必须运行在主线程
     *
     * @param context 当前上下文
     */
    public static void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除glide磁盘缓存，必须运行在子线程
     *
     * @param context 当前上下文
     */
    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }
}
