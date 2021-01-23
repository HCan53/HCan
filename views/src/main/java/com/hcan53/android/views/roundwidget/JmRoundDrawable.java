package com.hcan53.android.views.roundwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import com.hcan53.android.views.R;


/**
 * <p>Created by Fenghj on 2018/3/16.</p>
 */

public class JmRoundDrawable {

    public static Drawable fromAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JmRoundView);
        int colorBgNormal = typedArray.getColor(R.styleable.JmRoundView_jm_background_color_normal, Color.TRANSPARENT);
        int colorBgPressed = typedArray.getColor(R.styleable.JmRoundView_jm_background_color_pressed, -1);
        int colorBorder = typedArray.getColor(R.styleable.JmRoundView_jm_borderColor, 0);
        int borderWidth = typedArray.getDimensionPixelSize(R.styleable.JmRoundView_jm_borderWidth, 0);
        int mRadius = typedArray.getDimensionPixelSize(R.styleable.JmRoundView_jm_radius, 0);
        int mRadiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.JmRoundView_jm_radiusTopLeft, 0);
        int mRadiusTopRight = typedArray.getDimensionPixelSize(R.styleable.JmRoundView_jm_radiusTopRight, 0);
        int mRadiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.JmRoundView_jm_radiusBottomLeft, 0);
        int mRadiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.JmRoundView_jm_radiusBottomRight, 0);
        typedArray.recycle();

        if (colorBgPressed == -1) {
            return createGradientDrawable(colorBgNormal, colorBorder, borderWidth, mRadius,
                    mRadiusTopLeft, mRadiusTopRight, mRadiusBottomLeft, mRadiusBottomRight);
        } else {
            int[][] states = new int[2][];
            states[0] = new int[]{android.R.attr.state_pressed};
            states[1] = new int[]{-android.R.attr.state_pressed};

            GradientDrawable defaultDrawable = createGradientDrawable(colorBgNormal, colorBorder, borderWidth, mRadius,
                    mRadiusTopLeft, mRadiusTopRight, mRadiusBottomLeft, mRadiusBottomRight);

            GradientDrawable pressedDrawable = createGradientDrawable(colorBgPressed, colorBorder, borderWidth, mRadius,
                    mRadiusTopLeft, mRadiusTopRight, mRadiusBottomLeft, mRadiusBottomRight);

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(states[0], pressedDrawable);
            stateListDrawable.addState(states[1], defaultDrawable);

            return stateListDrawable;
        }
    }

    public static GradientDrawable createGradientDrawable(int colorBg, int colorBorder, int borderWidth,
                                                          int radius, int radiusTopLeft, int radiusTopRight,
                                                          int radiusBottomLeft, int radiusBottomRight) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (radiusTopLeft > 0 || radiusTopRight > 0 || radiusBottomLeft > 0 || radiusBottomRight > 0) {
            float[] radii = new float[]{
                    radiusTopLeft, radiusTopLeft,
                    radiusTopRight, radiusTopRight,
                    radiusBottomRight, radiusBottomRight,
                    radiusBottomLeft, radiusBottomLeft
            };
            gradientDrawable.setCornerRadii(radii);
        } else {
            gradientDrawable.setCornerRadius(radius);
        }
        gradientDrawable.setColor(colorBg);
        gradientDrawable.setStroke(borderWidth, colorBorder);
        return gradientDrawable;
    }
}
