package com.hcan53.android.views.round;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * <p>Created by Fenghj on 2018/3/16.</p>
 */

public class JmRoundTextView extends AppCompatTextView {
    public JmRoundTextView(Context context) {
        this(context, null);
    }

    public JmRoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JmRoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Drawable drawable = JmRoundDrawable.fromAttributeSet(context, attrs);
        setBackgroundDrawable(drawable);
    }
}
