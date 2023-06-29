package com.hcan53.android.screen.shot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.hcan53.android.screen.ScreenPermissionActivity;
import com.hcan53.android.screen.utils.BitmapUtils;
import com.hcan53.android.screen.utils.OtherUtils;
import com.hcan53.android.screen.utils.SDCardUtils;
import com.hcan53.android.screen.utils.ScreenUtil;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

/**
 * HCan
 * 截屏工具
 */
public class ScreenShotUtil {

    public static MediaProjectionManager mMediaProjectionManager;
    static MediaProjection mMediaProjection;
    static ImageReader mImageReader;
    static VirtualDisplay mVirtualDisplay;
    static int mScreenDensity;
    static int mScreenWidth;
    static int mScreenHeight;
    WindowManager mWindowManager;
    Activity activity;
    String filePath;
    private OnScreenShotListener screenShotListener;

    public volatile boolean isInit;

    private ScreenShotUtil() {
    }

    public static ScreenShotUtil getInstance() {
        return ScreenRecordUtilHolder.screenRecord;
    }

    public void setScreenShotListener(OnScreenShotListener screenShotListener) {
        this.screenShotListener = screenShotListener;
    }

    /**
     * 获取第一次截图
     *
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void screenShot(Activity activity) {
        if (mVirtualDisplay != null) {
            return;
        }
        this.activity = activity;
        this.filePath = getFilePath();
        startShot();
    }

    /**
     * 获取第一次截图
     *
     * @param activity
     * @param filePath
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void screenShot(Activity activity, String filePath) {
        if (mVirtualDisplay != null) {
            return;
        }
        this.activity = activity;
        this.filePath = filePath;
        startShot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startShot() {
        if (!isInit) {
            init(activity);
            ScreenPermissionActivity.isScrennShot = true;
            activity.startActivity(new Intent(activity, ScreenPermissionActivity.class));
        } else {
            saveShot();
        }
    }

    /**
     * 获取截屏
     *
     * @return
     */
    public Bitmap getScreenShot() {
        if (isInit) {
            return startCapture();
        } else {
            return null;
        }

    }

    /**
     * 停止录屏
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void destroy() {
        if (mVirtualDisplay == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mVirtualDisplay.release();
        }
        mVirtualDisplay = null;

        if (mMediaProjection != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMediaProjection.stop();
            }
        }
        isInit = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent service = new Intent(activity, ScreenShotService.class);
            activity.stopService(service);
        }
    }

    /**
     * 初始化
     *
     * @param activity
     */
    @SuppressLint("WrongConstant")
    void init(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
        ScreenUtil.getScreenSize(activity);
        mScreenWidth = ScreenUtil.SCREEN_WIDTH;
        mScreenHeight = ScreenUtil.SCREEN_HEIGHT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mImageReader = ImageReader.newInstance(ScreenUtil.SCREEN_WIDTH,
                    ScreenUtil.SCREEN_HEIGHT, PixelFormat.RGBA_8888, 1);
        }
        mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
    }

    /**
     * 返回可以开始录屏的数据
     *
     * @param resultCode
     * @param data
     */
    public void permissionResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent service = new Intent(activity, ScreenShotService.class);
                service.putExtra("code", resultCode);
                service.putExtra("data", data);
                activity.startForegroundService(service);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                }
            }
            TimerTask task = new TimerTask() {
                @Override
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void run() {
                    //execute the task
                    virtualDisplay();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 300);
        } else {
            isInit = false;
            if (screenShotListener != null){
                screenShotListener.screenShot(false, "");
            }
        }
    }

    /**
     * 开始录屏
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void virtualDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                    mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
            isInit = true;
            saveShot();
            if (screenShotListener != null){
                screenShotListener.screenShot(true, filePath);
            }
        } else {
            isInit = false;
            if (screenShotListener != null){
                screenShotListener.screenShot(false, "");
            }
        }
    }

    /**
     * 获取文件存储路径及存储名称
     *
     * @return
     */
    private String getFilePath() {
        String time = OtherUtils.formatDate(System.currentTimeMillis());
        String filePath = SDCardUtils.getStorageDirectory().getPath() + "/record/" + time + ".png";
        return filePath;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveShot() {
        Bitmap bitmap = getScreenShot();
        BitmapUtils.save(bitmap, filePath, Bitmap.CompressFormat.PNG);
        this.destroy();
    }

    /**
     * 获取截图
     *
     * @return
     */
    private Bitmap startCapture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Image image = null;
            image = mImageReader.acquireLatestImage();
            while (image == null) {
                SystemClock.sleep(10);
                image = mImageReader.acquireLatestImage();
            }
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
            return bitmap;
        }
        return null;
    }

    private static class ScreenRecordUtilHolder {
        private static final ScreenShotUtil screenRecord = new ScreenShotUtil();
    }

}
