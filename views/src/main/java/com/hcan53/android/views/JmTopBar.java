package com.hcan53.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcan53.android.views.utils.DensityUtils;


/**
 * <p>Created by Fenghj on 2018/6/20.</p>
 */

public class JmTopBar extends RelativeLayout {
    private TextView titleView;
    private ImageView leftView;
    private ImageView rightView;
    private View lineView;

    private CharSequence title;
    private float titleSize;
    private int titleColor;
    private int leftImage;
    private int rightImage;
    private int leftViewPadding;
    private int leftViewPaddingLeft;
    private int leftViewPaddingTop;
    private int leftViewPaddingRight;
    private int leftViewPaddingBottom;
    private int rightViewPadding;
    private int rightViewPaddingLeft;
    private int rightViewPaddingTop;
    private int rightViewPaddingRight;
    private int rightViewPaddingBottom;
    private boolean darkTheme;

    private Drawable mTopBarBgWithSeparatorDrawableCache;
    private OnLeftClickListener mLeftClickListener;
    private OnRightClickListener mRightClickListener;

    public static final float    DEFAULT_BAR_HEIGHT           = 44;
    public static final int      DEFAULT_BAR_BACKGROUD_COLOR  = 0xFFFFFFFF;
    public static final int      DEFAULT_TITLE_COLOR          = 0xFF333333;
    public static final float    DEFAULT_TITLE_SIZE           = 17;
    public static final boolean  DEFAULT_DARK_THEME           = false;
    public static final boolean  DEFAULT_HAS_LINE             = true;
    public static final int      DEFAULT_LINE_COLOR           = 0xFFEAEAEA;
    public static final float    DEFAULT_LINE_HEIGHT          = 0.5f;


    public interface OnLeftClickListener {
        void onClick();
    }

    public interface OnRightClickListener {
        void onClick();
    }

    public void setOnLeftClickListener(OnLeftClickListener listener) {
        this.mLeftClickListener = listener;
    }

    public void setOnRightClickListener(OnRightClickListener listener) {
        this.mRightClickListener = listener;
    }

    public JmTopBar(Context context) {
        this(context, null);
    }

    public JmTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JmTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getAttrs(context, attrs);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.jm_topbar_layout, this, true);
        titleView = findViewById(R.id.topbar_title_tv);
        leftView = findViewById(R.id.topbar_left_iv);
        rightView = findViewById(R.id.topbar_right_iv);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JmTopBar);
        title = typedArray.getText(R.styleable.JmTopBar_jm_title);
        titleColor = typedArray.getColor(R.styleable.JmTopBar_jm_titleColor, DEFAULT_TITLE_COLOR);
        titleSize = typedArray.getDimension(R.styleable.JmTopBar_jm_titleSize, DensityUtils.sp2px(context, DEFAULT_TITLE_SIZE));
        leftImage = typedArray.getResourceId(R.styleable.JmTopBar_jm_leftImage, -1);
        rightImage = typedArray.getResourceId(R.styleable.JmTopBar_jm_rightImage, -1);
        leftViewPadding = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_padding, -1);
        leftViewPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_paddingLeft, 0);
        leftViewPaddingTop = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_paddingTop, 0);
        leftViewPaddingRight = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_paddingRight, 0);
        leftViewPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_paddingBottom, 0);

        rightViewPadding = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_rightView_padding, -1);
        rightViewPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_paddingLeft, 0);
        rightViewPaddingTop = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_paddingTop, 0);
        rightViewPaddingRight = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_paddingRight, 0);
        rightViewPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_leftView_paddingBottom, 0);
        darkTheme = typedArray.getBoolean(R.styleable.JmTopBar_jm_is_dark, DEFAULT_DARK_THEME);

        boolean hasLine = typedArray.getBoolean(R.styleable.JmTopBar_jm_hasLine, DEFAULT_HAS_LINE);
        int lineColor = typedArray.getColor(R.styleable.JmTopBar_jm_lineColor, DEFAULT_LINE_COLOR);
        int lineHeight = typedArray.getDimensionPixelSize(R.styleable.JmTopBar_jm_lineHeight, DensityUtils.dp2px(context, DEFAULT_LINE_HEIGHT));
        typedArray.recycle();

        initTitleView();
        initLeftView();
        initRightView();

        if(hasLine) {
            setLineView(lineColor, lineHeight);
        }
    }

    /**
     * 添加 JmTopBar 的标题
     *
     * @param resId JmTopBar 的标题 resId
     */
    public void setTitle(int resId) {
        setTitle(getContext().getString(resId));
    }
    /**
     * 添加 JmTopBar 的标题
     *
     * @param title JmTopBar 的标题
     */
    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

    /**
     * 设置标题的颜色
     * @param color 颜色值
     */
    public void setTitleColor(int color) {
        if (color == -1) return;
        titleView.setTextColor(color);
    }

    /**
     * 设置标题字号
     * @param sizePx 字号
     */
    public void setTitleSize(float sizePx) {
        titleView.setTextSize(sizePx);
    }

    public void setMarquee() {
        titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleView.setFocusable(true);
    }

    /**
     * 初始化TitleView
     */
    private void initTitleView() {
        titleView.setSingleLine(true);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setSelected(true);
        titleView.setText(title);
        titleView.setTextColor(titleColor);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
    }

    /**
     * 设置左边ImageView图片
     * @param leftImageResource 图片id
     */
    public void setLeftView(int leftImageResource) {
        if (leftImageResource == -1) {
            leftView.setVisibility(INVISIBLE);
        } else {
            leftView.setImageResource(leftImageResource);
            leftView.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置左边ImageView控件内边距，调整显示效果
     * @param left   左内边距
     * @param top    上内边距
     * @param right  右内边距
     * @param bottom 下内边距
     */
    public void setLeftViewPadding(int left, int top, int right, int bottom) {
        leftView.setPadding(left, top, right, bottom);
    }

    /**
     * 初始化左边ImageView控件
     */
    private void initLeftView() {
        leftView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setLeftView(leftImage);
        if (leftViewPadding > -1) {
            setLeftViewPadding(leftViewPadding, leftViewPadding, leftViewPadding, leftViewPadding);
        } else {
            setLeftViewPadding(leftViewPaddingLeft, leftViewPaddingTop, leftViewPaddingRight, leftViewPaddingBottom);
        }

        if(!isInEditMode()) {
            if (darkTheme) {
                leftView.setBackgroundResource(R.drawable.jm_topbar_imageview_bg_dark_selector);
            } else {
                leftView.setBackgroundResource(R.drawable.jm_topbar_imageview_bg_selector);
            }
        }

        leftView.setOnClickListener(v -> {
            if (mLeftClickListener != null) {
                mLeftClickListener.onClick();
            }
        });

        leftView.getLayoutParams().width = DensityUtils.dp2px(getContext(), 44);
    }

    public void setRightView(int rightImageResource) {
        if (rightImageResource == -1) {
            rightView.setVisibility(INVISIBLE);
        } else {
            rightView.setImageResource(rightImageResource);
            rightView.setVisibility(VISIBLE);
        }
    }

    public void setRightViewPadding(int left, int top, int right, int bottom) {
        rightView.setPadding(left, top, right, bottom);
    }

    private void initRightView() {
        rightView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setRightView(rightImage);
        if (rightViewPadding > -1) {
            setRightViewPadding(rightViewPadding, rightViewPadding, rightViewPadding, rightViewPadding);
        } else {
            setRightViewPadding(rightViewPaddingLeft, rightViewPaddingTop, rightViewPaddingRight, rightViewPaddingBottom);
        }
        if(!isInEditMode()) {
            if (darkTheme) {
                rightView.setBackgroundResource(R.drawable.jm_topbar_imageview_bg_dark_selector);
            } else {
                rightView.setBackgroundResource(R.drawable.jm_topbar_imageview_bg_selector);
            }
        }

        rightView.setOnClickListener(v -> {
            if (mRightClickListener != null) {
                mRightClickListener.onClick();
            }
        });

        rightView.getLayoutParams().width = DensityUtils.dp2px(getContext(), 44);
    }

    /**
     * 添加分割线
     * @param lineColor  分割线颜色，默认"#EAEAE"
     * @param lineHeight 分割线高度，默认0.5dp
     */
    private void setLineView(int lineColor, int lineHeight) {
        if(lineView == null) {
            lineView = new View(getContext());
        }
        lineView.setBackgroundColor(lineColor);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, lineHeight);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        addView(lineView, lp);
    }
}