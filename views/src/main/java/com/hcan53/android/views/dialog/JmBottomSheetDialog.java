package com.hcan53.android.views.dialog;

import android.app.Dialog;
import android.content.Context;
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


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcan53.android.views.R;
import com.hcan53.android.views.adapter.BottomSheetListAdapter;
import com.hcan53.android.views.roundwidget.JmRoundButton;
import com.hcan53.android.views.roundwidget.JmRoundTextView;
import com.hcan53.android.views.utils.StringUtils;

import java.util.Arrays;

/**
 * <p>Created by Fenghj on 2018/6/26.</p>
 */
public class JmBottomSheetDialog extends Dialog {
    // 动画时长
    private final static int mAnimationDuration = 200;
    private View mContentView;
    private boolean mIsAnimating = false;

    public JmBottomSheetDialog(@NonNull Context context) {
        this(context, R.style.BottomSheet);
    }

    public JmBottomSheetDialog(@NonNull Context context, int themeResId) {
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
                            JmBottomSheetDialog.super.dismiss();
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
        private Context mContext;
        private CharSequence mTitle;
        private String[] mItems;
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

        public Builder setItemClickListener(OnItemClickListener listener) {
            mListener = listener;
            return this;
        }

        public JmBottomSheetDialog create() {
            JmBottomSheetDialog dialog = new JmBottomSheetDialog(mContext);
            dialog.setContentView(R.layout.jm_bottom_sheet_dialog);
            JmRoundTextView titleTv = dialog.findViewById(R.id.dialog_tilte_tv);
            JmRoundButton cancelBtn = dialog.findViewById(R.id.dialog_cancel_btn);
            RecyclerView listRv = dialog.findViewById(R.id.dialog_list_rv);
            View lineView = dialog.findViewById(R.id.dialog_line_view);

            titleTv.setVisibility(StringUtils.isEmpty(mTitle) ? View.GONE : View.VISIBLE);
            lineView.setVisibility(StringUtils.isEmpty(mTitle) ? View.GONE : View.VISIBLE);
            titleTv.setText(mTitle);
            cancelBtn.setOnClickListener(view -> dialog.dismiss());

            listRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            BottomSheetListAdapter adapter = new BottomSheetListAdapter(Arrays.asList(mItems), StringUtils.isEmpty(mTitle));
            listRv.setAdapter(adapter);
            adapter.setOnItemClickListener((data, position) -> {
                if(mListener != null) mListener.onItemClick((String) data, position);
                dialog.dismiss();
            });
            return dialog;
        }

    }
}
