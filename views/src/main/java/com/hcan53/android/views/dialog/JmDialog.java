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
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.hcan53.android.views.JmEditText;
import com.hcan53.android.views.R;
import com.hcan53.android.views.utils.ScreenUtils;
import com.hcan53.android.views.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by Fenghj on 2018/6/26.</p>
 */
public class JmDialog extends Dialog {
    private View mContentView;

    public JmDialog(@NonNull Context context) {
        this(context, R.style.BottomSheet);
    }

    public JmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = (int) (ScreenUtils.getScreenWidth() * 0.8);
            wmLp.gravity = Gravity.CENTER;
            window.setAttributes(wmLp);
        }
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

    public static class Builder {
        private Context mContext;
        private CharSequence mTitle;
        private CharSequence message;
        private boolean hasEditText = false;
        private CharSequence mHint;
        private CharSequence mPositive;
        private CharSequence mNegative;
        private OnPositiveListener mPositiveListener;
        private OnNegativeListener mNegativeListener;
        private List<CharSequence> list = new ArrayList<>();

        private Button positiveBtn;
        private Button negativeBtn;
        private View verticalDivier;
        private View horizontalDivier;

        public Builder(Context context) {
            mContext = context;
        }

        public interface OnPositiveListener {
            void onClick(int i, String item, String value);
        }

        public interface OnNegativeListener {
            void onClick(int i, String item, String value);
        }

        public Builder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(CharSequence positive, OnPositiveListener listener) {
            this.mPositive = positive;
            list.add(mPositive);
            mPositiveListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence negative, OnNegativeListener listener) {
            this.mNegative = negative;
            list.add(mNegative);
            mNegativeListener = listener;
            return this;
        }

        public Builder setEditText() {
            hasEditText = true;
            return this;
        }

        public Builder setEditText(CharSequence hint) {
            hasEditText = true;
            mHint = hint;
            return this;
        }

        public JmDialog create() {
            return create(true);
        }

        public JmDialog create(boolean cancelable) {
            JmDialog dialog = new JmDialog(mContext);
            dialog.setCancelable(cancelable);
            dialog.setContentView(R.layout.jm_dialog);
            TextView titleTv = dialog.findViewById(R.id.dialog_tilte_tv);
            TextView msgTv = dialog.findViewById(R.id.dialog_msg_tv);
            positiveBtn = dialog.findViewById(R.id.dialog_positive_btn);
            negativeBtn = dialog.findViewById(R.id.dialog_negative_btn);
            horizontalDivier = dialog.findViewById(R.id.dialog_horizontal_divier_view);
            verticalDivier = dialog.findViewById(R.id.dialog_vertical_divier_view);
            JmEditText editText = dialog.findViewById(R.id.dialog_et);

            titleTv.setVisibility(StringUtils.isEmpty(mTitle) ? View.GONE : View.VISIBLE);
            titleTv.setText(mTitle);
            msgTv.setVisibility(StringUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
            msgTv.setText(message);

            editText.setVisibility(hasEditText ? View.VISIBLE : View.GONE);
            editText.setHint(mHint);

            positiveBtn.setText(mPositive);
            negativeBtn.setText(mNegative);
            initButtons();

            negativeBtn.setOnClickListener(view -> {
                if (mNegativeListener != null) {
                    mNegativeListener.onClick(0, mNegative.toString(), editText.getText().toString());
                }
            });

            positiveBtn.setOnClickListener(view -> {
                if (mPositiveListener != null) {
                    mPositiveListener.onClick(1, mPositive.toString(), editText.getText().toString());
                }
            });
            return dialog;
        }

        private void initButtons() {
            if (list == null || list.size() == 0) {
                horizontalDivier.setVisibility(View.GONE);
                verticalDivier.setVisibility(View.GONE);
                positiveBtn.setVisibility(View.GONE);
                negativeBtn.setVisibility(View.GONE);
            } else {
                horizontalDivier.setVisibility(View.VISIBLE);
                positiveBtn.setVisibility(View.VISIBLE);
                negativeBtn.setVisibility(View.VISIBLE);
                if (list.size() == 1) {
                    verticalDivier.setVisibility(View.GONE);
                    if (!StringUtils.isEmpty(mPositive)) {
                        negativeBtn.setVisibility(View.GONE);
                        positiveBtn.setBackgroundResource(R.drawable.jm_dialog_button_selector);
                    }
                    if (!StringUtils.isEmpty(mNegative)) {
                        positiveBtn.setVisibility(View.GONE);
                        negativeBtn.setBackgroundResource(R.drawable.jm_dialog_button_selector);
                    }
                } else if (list.size() == 2) {
                    verticalDivier.setVisibility(View.VISIBLE);
                    positiveBtn.setBackgroundResource(R.drawable.jm_dialog_button_left_selector);
                    negativeBtn.setBackgroundResource(R.drawable.jm_dialog_button_right_selector);
                }
            }
        }

    }
}
