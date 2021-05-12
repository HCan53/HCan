package com.hcan53.android.views.jm;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.hcan53.android.views.R;

/**
 * <p>Created by Fenghj on 2018/5/16.</p>
 */
public class JmStatusView extends RelativeLayout {
    public static final LayoutParams DEFAULT_LAYOUT_PARAMS =
            new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;
    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;

    private LayoutInflater mInflater;

    public JmStatusView(Context context) {
        this(context, null);
    }

    public JmStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JmStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JmStatusView);
        mLoadingViewResId = typedArray.getResourceId(R.styleable.JmStatusView_jm_loadingView, 0);
        mEmptyViewResId = typedArray.getResourceId(R.styleable.JmStatusView_jm_emptyView, R.layout.jm_status_empty_view);
        mErrorViewResId = typedArray.getResourceId(R.styleable.JmStatusView_jm_errorView, R.layout.jm_status_error_view);
        mNoNetworkViewResId = typedArray.getResourceId(R.styleable.JmStatusView_jm_noNetworkView, R.layout.jm_status_no_net_view);
        typedArray.recycle();

        mInflater = LayoutInflater.from(getContext());
    }

    private View inflateView(int layoutId) {
        return mInflater.inflate(layoutId, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear(mEmptyView, mErrorView, mLoadingView, mNoNetworkView);
        mInflater = null;
    }

    /**
     * 显示加载中视图
     */
    public void showLoading() {
        if(mLoadingViewResId == 0) {
            removeAllViews();
            JmLoadingView loadingView = new JmLoadingView(getContext());
            loadingView.setColor(ContextCompat.getColor(getContext(), R.color.status_view_color));
            LayoutParams lp =
                    new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT);
            lp.addRule(CENTER_IN_PARENT);
            addView(loadingView, lp);
        } else {
            showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS);
        }
    }

    /**
     * 显示加载中视图
     * @param layoutId 自定义布局文件
     */
    public void showLoading(int layoutId, LayoutParams layoutParams) {
        showLoading(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示加载中视图
     * @param view 自定义视图
     */
    public void showLoading(View view, LayoutParams layoutParams) {
        removeAllViews();
        if (null == mLoadingView) {
            mLoadingView = view;
        }
        showView(mLoadingView, layoutParams);
    }

    /**
     * 显示错误视图
     */
    public void showError() {
        showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示错误视图
     * @param layoutId 自定义布局文件
     */
    public void showError(int layoutId, LayoutParams layoutParams) {
        showError(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示错误视图
     * @param view 自定义视图
     */
    public void showError(View view, LayoutParams layoutParams) {
        removeAllViews();
        if (null == mErrorView) {
            mErrorView = view;
        }
        showView(mErrorView, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示空视图
     */
    public void showEmpty() {
        showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示空视图
     * @param layoutId 自定义布局文件
     */
    public void showEmpty(int layoutId, LayoutParams layoutParams) {
        showEmpty(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示空视图
     * @param view 自定义视图
     */
    public void showEmpty(View view, LayoutParams layoutParams) {
        removeAllViews();
        if (null == mEmptyView) {
            mEmptyView = view;
        }
        showView(mEmptyView, layoutParams);
    }

    /**
     * 显示无网络视图
     */
    public void showNoNetwork() {
        showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示无网络视图
     * @param layoutId 自定义布局文件
     */
    public void showNoNetwork(int layoutId, LayoutParams layoutParams) {
        showNoNetwork(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示无网络视图
     * @param view 自定义视图
     */
    public void showNoNetwork(View view, LayoutParams layoutParams) {
        removeAllViews();
        if (null == mNoNetworkView) {
            mNoNetworkView = view;
        }
        showView(mNoNetworkView, DEFAULT_LAYOUT_PARAMS);
    }

    public void showView(View child, LayoutParams params) {
        setVisibility(VISIBLE);
        addView(child, params);
    }

    public void hideView() {
        setVisibility(GONE);
    }

    private void clear(View... views) {
        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
