package com.hcan53.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/11/6.
 */

public class MyRecylerView extends RecyclerView {
    public MyRecylerView(Context context) {
        super(context);
    }

    public MyRecylerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecylerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
