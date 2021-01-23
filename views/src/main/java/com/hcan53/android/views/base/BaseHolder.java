package com.hcan53.android.views.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;


public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected OnViewClickListener mOnViewClickListener = null;
    protected final String TAG = this.getClass().getSimpleName();

    public BaseHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    /**
     * 设置数据
     *
     * @param data 数据
     * @param position 下标
     */
    public abstract void setData(T data, int position);


    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, this.getLayoutPosition());
        }
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }
}