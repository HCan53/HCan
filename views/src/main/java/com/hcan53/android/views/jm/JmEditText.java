package com.hcan53.android.views.jm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.hcan53.android.views.R;

/**
 * <p>Created by Fenghj on 2018/6/20.</p>
 */

public class JmEditText extends AppCompatEditText {
    private Drawable ic_left = null;
    private int left_icon;
    private int left_width;
    private int left_height;

    private boolean hasDelete;
    private Drawable ic_delete = null;
    private int delete_icon;
    private int delete_width;
    private int delete_height;

    private int strokeColor;
    private int strokeWidth;
    private int leftStrokeWidth;
    private int topStrokeWidth;
    private int rightStrokeWidth;
    private int bottomStrokeWidth;
    private float cornerRadius;
    private int backgroundColor;
    private Paint mPaint = new Paint(); // 画笔

    public static final int DEFAULT_LINE_COLOR = 0xFFEAEAEA;

    public JmEditText(Context context) {
        super(context);
    }

    public JmEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public JmEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JmEditText);
        left_icon = typedArray.getResourceId(R.styleable.JmEditText_jm_left_icon, -1);
        left_width = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_left_width, 0);
        left_height = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_left_height, 0);

        delete_icon = typedArray.getResourceId(R.styleable.JmEditText_jm_delete_icon, R.drawable.jm_edittext_delete_icon);
        delete_width = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_delete_width, 0);
        delete_height = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_delete_height, 0);
        hasDelete = typedArray.getBoolean(R.styleable.JmEditText_jm_hasDelete, true);

        backgroundColor = typedArray.getColor(R.styleable.JmEditText_android_background, Color.TRANSPARENT);
        strokeColor = typedArray.getColor(R.styleable.JmEditText_jm_strokeColor, DEFAULT_LINE_COLOR);
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_strokeWidth, 0);
        leftStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_left_strokeWidth, 0);
        topStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_top_strokeWidth, 0);
        rightStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_right_strokeWidth, 0);
        bottomStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.JmEditText_jm_bottom_strokeWidth, 0);
        cornerRadius = typedArray.getDimension(R.styleable.JmEditText_jm_cornerRadius, 0);

        typedArray.recycle();

        if (left_icon != -1) {
            initLeftView();
        }
        if (hasDelete) {
            initDeleteView();
        }

        initBackground();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initLeftView() {
        ic_left = getResources().getDrawable(left_icon);
        if (left_width == 0) {
            left_width = ic_left.getIntrinsicWidth();
        }
        if (left_height == 0) {
            left_height = ic_left.getIntrinsicHeight();
        }
        ic_left.setBounds(0, 0, left_width, left_height);
        setCompoundDrawables(ic_left, null, null, null);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initDeleteView() {
        ic_delete = getResources().getDrawable(delete_icon);
        if (delete_width == 0) {
            delete_width = ic_delete.getIntrinsicWidth();
        }
        if (delete_height == 0) {
            delete_height = ic_delete.getIntrinsicHeight();
        }
        ic_delete.setBounds(0, 0, delete_width, delete_height);
    }

    private void initBackground() {
        GradientDrawable g1 = new GradientDrawable();
        g1.setCornerRadius(cornerRadius);
        g1.setColor(strokeColor);

        GradientDrawable g2 = new GradientDrawable();
        g2.setCornerRadius(cornerRadius);
        g2.setColor(backgroundColor);

        Drawable[] layers = {g1, g2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        if (strokeWidth != 0) {
            layerDrawable.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        } else {
            layerDrawable.setLayerInset(1, leftStrokeWidth, topStrokeWidth, rightStrokeWidth, bottomStrokeWidth);
        }
        setBackgroundDrawable(layerDrawable);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (hasDelete) {
            setDeleteIconVisible(hasFocus() && text.length() > 0);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (hasDelete) {
            setDeleteIconVisible(focused && length() > 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
        switch (event.getAction()) {
            // 判断动作 = 手指抬起时
            case MotionEvent.ACTION_UP:
                Drawable drawable = ic_delete;
                if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
                        && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                    // 判断条件说明
                    // event.getX() ：抬起时的位置坐标
                    // getWidth()：控件的宽度
                    // getPaddingRight():删除图标图标右边缘至EditText控件右边缘的距离
                    // 即：getWidth() - getPaddingRight() = 删除图标的右边缘坐标 = X1
                    // getWidth() - getPaddingRight() - drawable.getBounds().width() = 删除图标左边缘的坐标 = X2
                    // 所以X1与X2之间的区域 = 删除图标的区域
                    // 当手指抬起的位置在删除图标的区域（X2=<event.getX() <=X1），即视为点击了删除图标 = 清空搜索框内容
                    setText("");
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 控制删除按钮是否显示
     *
     * @param deleteVisible 删除图标是否显示
     */
    private void setDeleteIconVisible(boolean deleteVisible) {
        setCompoundDrawables(ic_left, null, deleteVisible ? ic_delete : null, null);
//        invalidate();
    }
}