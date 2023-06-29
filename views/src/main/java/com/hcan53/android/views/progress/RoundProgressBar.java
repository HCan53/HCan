package com.hcan53.android.views.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import com.hcan53.android.views.R;

import java.lang.ref.WeakReference;


public class RoundProgressBar extends View {

    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环初始背景的画笔
    private Paint mRoundPaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 画字体的画笔
    private Paint mTextPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆环初始颜色
    private int roundColor;
    // 圆环颜色
    private int mRingColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress;
    private long millisInFuture = 4000;
    private long countDownInterval = 40;

    private OnFinshListener finshListener;
    private TimerCount timerCount;

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar, 0, 0);
        roundColor = typeArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        mRadius = typeArray.getDimension(R.styleable.RoundProgressBar_radiuss, 80);
        mStrokeWidth = typeArray.getDimension(R.styleable.RoundProgressBar_strokeWidths, 10);
        mCircleColor = typeArray.getColor(R.styleable.RoundProgressBar_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.RoundProgressBar_ringColor, 0xFFFFFFFF);
        millisInFuture = typeArray.getInteger(R.styleable.RoundProgressBar_millisInFuture, 4000);
        countDownInterval = typeArray.getInteger(R.styleable.RoundProgressBar_countDownInterval, 40);

        mRingRadius = mRadius + mStrokeWidth / 2;
    }

    private void initVariable() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mRoundPaint = new Paint();
        mRoundPaint.setAntiAlias(true);  //消除锯齿
        mRoundPaint.setColor(roundColor); //设置圆环的颜色
        mRoundPaint.setStyle(Paint.Style.STROKE); //设置空心
        mRoundPaint.setStrokeWidth(mStrokeWidth); //设置圆环的宽度

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setARGB(255, 255, 255, 255);
        mTextPaint.setTextSize(mRadius / 2);

        FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);

    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
        if (mProgress > 0) {
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRoundPaint);
            canvas.drawArc(oval, -90, ((float) mProgress / mTotalProgress) * 360, false, mRingPaint); //
            String txt = mProgress + "%";
            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
            canvas.drawText("", mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
        }
    }

    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();
    }

    public void startTimerCount() {
        timerCount = new TimerCount(millisInFuture, countDownInterval, this);
        timerCount.start();
    }

    public void cancelTimerCount() {
        if (timerCount != null) {
            timerCount.cancel();
        }
    }

    public void setFinshListener(OnFinshListener finshListener) {
        this.finshListener = finshListener;
    }

    public interface OnFinshListener {
        void finsh();
    }

    /**
     * 计时器内部类，为防止内存泄露，使用弱引用
     */
    private static class TimerCount extends CountDownTimer {
        private final WeakReference<RoundProgressBar> barWeakReference;
        private int progress = 0;

        TimerCount(long millisInFuture, long countDownInterval, RoundProgressBar progressBar) {
            super(millisInFuture, countDownInterval);
            barWeakReference = new WeakReference<>(progressBar);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (barWeakReference.get() != null) {
                progress += 1;
                barWeakReference.get().setProgress(progress);
            }
        }

        @Override
        public void onFinish() {
            if (barWeakReference.get() != null) {
                barWeakReference.get().setProgress(100);
                if (barWeakReference.get().finshListener != null) {
                    barWeakReference.get().finshListener.finsh();
                }
            }
        }
    }

}
