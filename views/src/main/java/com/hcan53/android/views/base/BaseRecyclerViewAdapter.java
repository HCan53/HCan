package com.hcan53.android.views.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {
    protected List<T> mInfos = new ArrayList<>();
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void notifyRefresh(List<T> list) {
        mInfos = list;
        notifyDataSetChanged();
    }

    public void notifyMore(List<T> list) {
        mInfos.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 创建 {@link BaseHolder}
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public BaseHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        BaseHolder<T> holder = getHolder(view, viewType);
        return holder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseHolder<T> holder, int position) {
        holder.setData(mInfos.get(position), position);
        holder.itemView.setOnClickListener(view -> {
            if (mOnItemClickListener != null && mInfos.size() > 0) {
                mOnItemClickListener.onItemClick(mInfos.get(position), position);
            }
        });
    }


    /**
     * 返回数据的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mInfos.size();
    }


    public List<T> getInfos() {
        return mInfos;
    }

    /**
     * 获得某个 {@code position} 上的 item 的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mInfos == null ? null : mInfos.get(position);
    }

    /**
     * 让子类实现用以提供 {@link BaseHolder}
     *
     * @param v
     * @param viewType
     * @return
     */
    public abstract BaseHolder<T> getHolder(View v, int viewType);

    /**
     * 提供用于 {@code item} 布局的 {@code layoutId}
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);


    /**
     * 遍历所有{@link BaseHolder},释放他们需要释放的资源
     *
     * @param recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
//            if (viewHolder != null && viewHolder instanceof BaseHolder) {
//                ((BaseHolder) viewHolder).onRelease();
//            }
        }
    }


    public interface OnRecyclerViewItemClickListener<T> {
//        void onItemClick(View view, int viewType, T data, int position);
        void onItemClick(T data, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}