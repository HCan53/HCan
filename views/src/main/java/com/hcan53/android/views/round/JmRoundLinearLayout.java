package com.hcan53.android.views.round;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * <p>Created by Fenghj on 2018/3/16.</p>
 */

public class JmRoundLinearLayout extends LinearLayout {
    public JmRoundLinearLayout(Context context) {
        super(context);
    }

    public JmRoundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public JmRoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Drawable drawable = JmRoundDrawable.fromAttributeSet(context, attrs);
        setBackgroundDrawable(drawable);
    }
}
