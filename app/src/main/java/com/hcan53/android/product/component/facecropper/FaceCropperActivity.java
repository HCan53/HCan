package com.hcan53.android.product.component.facecropper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hcan53.android.facecropper.FaceCropper;
import com.hcan53.android.product.R;
import com.hcan53.android.product.base.BaseActivity;
import com.hcan53.android.views.jm.JmTopBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by HCan on 2021/5/14
 */
public class FaceCropperActivity extends BaseActivity {
    private JmTopBar jmTopBar;
    private Picasso mPicasso;
    private FaceCropper mFaceCropper;
    private ViewPager mViewPager;

    @Override
    protected int getContentViewId() {
        return R.layout.face_croppper_activity;
    }

    @Override
    protected void initView() {
        jmTopBar = findViewById(R.id.jm_top_bar);
        jmTopBar.setOnLeftClickListener(() -> finish());
        mFaceCropper = new FaceCropper(1f);
        mFaceCropper.setFaceMinSize(0);
        mFaceCropper.setDebug(false);
        mPicasso = Picasso.with(this);

        final ImageAdapter adapter = new ImageAdapter();
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                adapter.updateView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mFaceCropper.setEyeDistanceFactorMargin((float) i / 10);
                adapter.updateView(mViewPager.getCurrentItem());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar.setProgress(10);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void toastMessage(String message) {

    }

    private Transformation mCropTransformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            return mFaceCropper.getCroppedImage(source);
        }

        @Override
        public String key() {
            StringBuilder builder = new StringBuilder();

            builder.append("faceCrop(");
            builder.append("minSize=").append(mFaceCropper.getFaceMinSize());
            builder.append(",maxFaces=").append(mFaceCropper.getMaxFaces());

            FaceCropper.SizeMode mode = mFaceCropper.getSizeMode();
            if (FaceCropper.SizeMode.EyeDistanceFactorMargin.equals(mode)) {
                builder.append(",distFactor=").append(mFaceCropper.getEyeDistanceFactorMargin());
            } else if (FaceCropper.SizeMode.FaceMarginPx.equals(mode)) {
                builder.append(",margin=").append(mFaceCropper.getFaceMarginPx());
            }

            return builder.append(")").toString();
        }
    };
    private Transformation mDebugCropTransformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            return mFaceCropper.getFullDebugImage(source);
        }

        @Override
        public String key() {
            StringBuilder builder = new StringBuilder();

            builder.append("faceDebugCrop(");
            builder.append("minSize=").append(mFaceCropper.getFaceMinSize());
            builder.append(",maxFaces=").append(mFaceCropper.getMaxFaces());

            FaceCropper.SizeMode mode = mFaceCropper.getSizeMode();
            if (FaceCropper.SizeMode.EyeDistanceFactorMargin.equals(mode)) {
                builder.append(",distFactor=").append(mFaceCropper.getEyeDistanceFactorMargin());
            } else if (FaceCropper.SizeMode.FaceMarginPx.equals(mode)) {
                builder.append(",margin=").append(mFaceCropper.getFaceMarginPx());
            }

            return builder.append(")").toString();
        }
    };


    class ImageAdapter extends PagerAdapter {

        private int[] urls = new int[]{
                R.mipmap.img_face1
        };

        @Override
        public int getCount() {
            return (urls == null) ? 0 : urls.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = getLayoutInflater().inflate(R.layout.face_cropper_pager_item, null, false);

            setupView(v, position);

            v.setTag(position);
            container.addView(v);
            return v;
        }

        public void setupView(View v, int position) {
            if (v == null) {
                return;
            }
            ImageView image = (ImageView) v.findViewById(R.id.imageView);
            ImageView imageCropped = (ImageView) v.findViewById(R.id.imageViewCropped);
            mPicasso.load(urls[position]).transform(mDebugCropTransformation).into(image);
            mPicasso.load(urls[position])
                    .config(Bitmap.Config.RGB_565)
                    .transform(mCropTransformation)
                    .into(imageCropped);
        }

        public void updateView(int position) {
            setupView(mViewPager.findViewWithTag(position), position);
        }
    }

    public static void intentActivity(Activity ac) {
        ac.startActivity(new Intent(ac, FaceCropperActivity.class));
    }
}
