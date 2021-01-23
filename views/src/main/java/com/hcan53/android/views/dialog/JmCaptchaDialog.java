package com.hcan53.android.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.hcan53.android.views.R;
import com.hcan53.android.views.utils.CaptchaUtils;
import com.hcan53.android.views.utils.ScreenUtils;
import com.hcan53.android.views.utils.StringUtils;
import com.hcan53.android.views.utils.ToastUtils;


/**
 * <p>Created by Fenghj on 2018/6/26.</p>
 */
public class JmCaptchaDialog extends Dialog {
    private View mContentView;

    public JmCaptchaDialog(@NonNull Context context) {
        this(context, R.style.BottomSheet);
    }

    public JmCaptchaDialog(@NonNull Context context, int themeResId) {
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

    public static class Builder {
        private Context mContext;
        private Bitmap bitmap;

        private OnPositiveListener mPositiveListener;

        public Builder(Context context) {
            mContext = context;
        }

        public interface OnPositiveListener {
            void onClick();
        }


        public Builder setPositiveButton(OnPositiveListener listener) {
            mPositiveListener = listener;
            return this;
        }

        public JmCaptchaDialog create() {
            JmCaptchaDialog dialog = new JmCaptchaDialog(mContext);
            dialog.setContentView(R.layout.jm_captcha_dialog);
            Button positiveBtn = dialog.findViewById(R.id.dialog_positive_btn);
            Button negativeBtn = dialog.findViewById(R.id.dialog_negative_btn);

            EditText editText = dialog.findViewById(R.id.captcha_et);
            ImageView imageView = dialog.findViewById(R.id.captcha_iv);

            CaptchaUtils captchaUtils = CaptchaUtils.getInstance();
            bitmap = captchaUtils.createBitmap();
            imageView.setImageBitmap(bitmap);


            imageView.setOnClickListener(view -> {
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
                bitmap = captchaUtils.createBitmap();
                imageView.setImageBitmap(bitmap);
            });

            positiveBtn.setOnClickListener(view -> {
                String text = editText.getText().toString().trim();
                if (StringUtils.isEmpty(text)) {
                    ToastUtils.showShort("请输入图片验证码");
                    return;
                }
                if (!text.equals(captchaUtils.getCode())) {
                    ToastUtils.showShort("图片验证码错误");
                    return;
                }
                if (mPositiveListener != null) {
                    mPositiveListener.onClick();
                }
                dialog.dismiss();
            });
            negativeBtn.setOnClickListener(view -> {
                dialog.dismiss();
            });

            return dialog;
        }
    }
}
