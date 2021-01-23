package com.hcan53.android.views.roundwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

/**
 * <p>Created by Fenghj on 2018/3/16.</p>
 */

public class JmRoundButton extends AppCompatButton {
    public JmRoundButton(Context context) {
        super(context);
    }

    public JmRoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public JmRoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Drawable drawable = JmRoundDrawable.fromAttributeSet(context, attrs);
        setBackground(drawable);
    }
}
