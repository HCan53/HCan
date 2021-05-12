package com.hcan53.android.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.hcan53.android.views.R;
import com.hcan53.android.views.adapter.ArrayWheelAdapter;
import com.hcan53.android.views.utils.StringUtils;
import com.hcan53.android.views.wheelview.view.WheelView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Created by Fenghj on 2018/6/26.</p>
 */
public class JmWheelDialog extends Dialog {
    // 动画时长
    private final static int mAnimationDuration = 200;
    private View mContentView;
    private boolean mIsAnimating = false;

    public JmWheelDialog(@NonNull Context context) {
        this(context, R.style.BottomSheet);
    }

    public JmWheelDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wmLp.gravity = Gravity.BOTTOM | Gravity.CENTER;
            window.setAttributes(wmLp);
        }

        setCanceledOnTouchOutside(true);
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        super.setContentView(mContentView);
    }

    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        mContentView = view;
        super.setContentView(view, params);
    }

    @Override
    public void setContentView(@NonNull View view) {
        mContentView = view;
        super.setContentView(view);
    }

    /**
     * BottomSheet升起动画
     */
    private void animateUp() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        );
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        mContentView.startAnimation(set);
    }

    /**
     * BottomSheet降下动画
     */
    private void animateDown() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        );
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnimating = false;
                mContentView.post(new Runnable() {
                    @Override
                    public void run() {
                        // java.lang.IllegalArgumentException: View=com.android.internal.policy.PhoneWindow$DecorView{22dbf5b V.E...... R......D 0,0-1080,1083} not attached to window manager
                        // 在dismiss的时候可能已经detach了，简单try-catch一下
                        try {
                            JmWheelDialog.super.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContentView.startAnimation(set);
    }

    @Override
    public void show() {
        super.show();
        animateUp();
    }

    @Override
    public void dismiss() {
        if (mIsAnimating) {
            return;
        }
        animateDown();
    }

    public static class Builder {
        private WheelView wheelView;

        private Context mContext;
        private CharSequence mTitle;
        private String[] mItems;
        private int selectedIndex = 0;
        private OnItemClickListener mListener;

        public Builder(Context context) {
            mContext = context;
        }

        public interface OnItemClickListener {
            void onItemClick(String item, int position);
        }

        public Builder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder addItems(String[] items) {
            mItems = items;
            return this;
        }

        public Builder setCurrentItem(int index) {
            selectedIndex = index;
            return this;
        }

        public Builder setItemClickListener(OnItemClickListener listener) {
            mListener = listener;
            return this;
        }

        public JmWheelDialog create() {
            JmWheelDialog dialog = new JmWheelDialog(mContext);
            dialog.setContentView(R.layout.jm_wheel_dialog);
            //顶部标题
            TextView tvTitle = dialog.findViewById(R.id.tvTitle);
            //确定和取消按钮
            Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            //滚轮
            wheelView = dialog.findViewById(R.id.options);
            initWheelView();

            tvTitle.setVisibility(StringUtils.isEmpty(mTitle) ? View.GONE : View.VISIBLE);
            tvTitle.setText(mTitle);
            btnCancel.setOnClickListener(view -> dialog.dismiss());

            List<String> mOptionsItems = new ArrayList<>();
            if (mItems != null) {
                Collections.addAll(mOptionsItems, mItems);
            }

            wheelView.setAdapter(new ArrayWheelAdapter<>(mOptionsItems));
            wheelView.setOnItemSelectedListener(index -> selectedIndex = index);

            btnSubmit.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onItemClick(mOptionsItems.get(selectedIndex), selectedIndex);
                }
                dialog.dismiss();
            });
            return dialog;
        }

        private void initWheelView() {
            wheelView.setCyclic(false);
            wheelView.setDividerColor(Color.parseColor("#999999"));
            wheelView.setDividerType(WheelView.DividerType.FILL);
            wheelView.setTextColorCenter(Color.parseColor("#333333"));
            wheelView.setTextColorOut(Color.parseColor("#999999"));
            wheelView.setCurrentItem(selectedIndex);
        }

    }
}
