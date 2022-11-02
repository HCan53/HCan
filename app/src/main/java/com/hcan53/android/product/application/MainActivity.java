package com.hcan53.android.product.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;

import com.HCan.android.draft.ThanDraftActivity;
import com.google.android.material.tabs.TabLayout;
import com.hcan53.android.product.R;
import com.hcan53.android.product.application.fragment.HomeFragment;
import com.hcan53.android.product.application.mvp.MainContract;
import com.hcan53.android.product.application.mvp.MainPresenter;
import com.hcan53.android.product.application.mvp.TabBean;
import com.hcan53.android.product.base.BaseActivity;
import com.hcan53.android.product.component.facecropper.FaceCropperActivity;
import com.hcan53.android.utils.BarUtils;
import com.hcan53.android.utils.ClipboardUtils;
import com.hcan53.android.utils.DoubleClickUtils;
import com.hcan53.android.utils.JLog;
import com.hcan53.android.utils.LogFileUtils;
import com.hcan53.android.utils.StringUtils;
import com.hcan53.android.utils.ToastUtils;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;

import java.util.Arrays;
import java.util.List;

/**
 * Created by HCan on 2021/5/12
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    private TabLayout tabLayout;

    private List<TabBean> tabBeans;

    private String TAG = "MainActivity";

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            String myStr = ClipboardUtils.getPasteString(MainActivity.this);
            if (!StringUtils.isEmpty(myStr)) {
//                ToastUtils.showShort(myStr);
                //读取剪切板数据
                ClipboardUtils.writePaste(MainActivity.this, "");
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initView() {
        BarUtils.hideStatusBar(this, false);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                for (TabBean tabBean : tabBeans) {
                    if (tabBean.getFragment().isAdded())
                        transaction.hide(tabBean.getFragment());
                }
                int index = (int) tab.getTag();
                TabBean tabBean = tabBeans.get(index);
                LogFileUtils.getInstace().writeLog(tabBean.getTabName());
                if (tabBean.getFragment() != null) {
                    if (!tabBean.getFragment().isAdded())
                        transaction.add(R.id.fl_main, tabBean.getFragment(), index + "");
                    else
                        transaction.show(tabBean.getFragment());
                    transaction.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @SuppressLint("NewApi")
    @Override
    protected void initData() {
        presenter.getTabBeans();
        addShortcuts();
        LogFileUtils.getInstace().writeLog("首页");
//        ThanDraftActivity.intentActivity(this);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android 8.0及以上
            NotificationChannel channel = mNotificationManager.getNotificationChannel(getResources().getString(R.string.notity_id));//CHANNEL_ID是自己定义的渠道ID
            if (channel != null && channel.getImportance() < NotificationManager.IMPORTANCE_HIGH) {//未开启
                ToastUtils.showShort("未开启横幅通知");
            }
        }
    }


    @Override
    public void setPresenter() {
        presenter = new MainPresenter();
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void toastMessage(String message) {

    }

    @Override
    public void showTabs(List<TabBean> beanList) {
        tabBeans = beanList;
        for (int i = 0; i < tabBeans.size(); i++) {
            TabLayout.Tab tab = getTabView(tabBeans.get(i), i);
            tabLayout.addTab(tab, i == 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    public void addShortcuts() {
        Intent[] intents = new Intent[]{
                new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, MainActivity.class),
                new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, FaceCropperActivity.class)
        };
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "id1")
                .setShortLabel("HCan")
                .setLongLabel("Open the HCan")
                .setIcon(Icon.createWithResource(this, R.mipmap.icon))
                .setIntents(intents)
                .build();
        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcutInfo));
    }

    private TabLayout.Tab getTabView(TabBean tabBean, int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.main_bottom_tab,
                tabLayout, false);
        ImageView tabIv = view.findViewById(R.id.tab_iv);
        TextView tabTv = view.findViewById(R.id.tab_tv);
        tabIv.setImageResource(tabBean.getTabIcon());
        tabTv.setText(tabBean.getTabName());
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setCustomView(view);
        tab.setTag(index);
        return tab;
    }

    public static void intentActivity(Activity ac) {
        ac.startActivity(new Intent(ac, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        //mBackHandedFragment为空或者不为空不消费返回事件
        if (DoubleClickUtils.isDoubleClick(2000)) {
            ToastUtils.cancel();
            this.finish();
        } else {
            ToastUtils.showShort(R.string.apply_exit);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == HomeFragment.REQUEST_CODE_SCAN_ONE) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj != null) {
                ToastUtils.showShort(obj.originalValue);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case KeyEvent.KEYCODE_ENTER:     //确定键enter
            case KeyEvent.KEYCODE_DPAD_CENTER:
                JLog.d(TAG, "enter--->");

                break;

            case KeyEvent.KEYCODE_BACK:    //返回键
                JLog.d(TAG, "back--->");
                onBackPressed();
                return true;   //这里由于break会退出，所以我们自己要处理掉 不返回上一层

            case KeyEvent.KEYCODE_SETTINGS: //设置键
                JLog.d(TAG, "setting--->");

                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:   //向下键

                /*    实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
                 *    exp:KeyEvent.ACTION_UP
                 */
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    JLog.d(TAG, "down--->");
                }

                break;

            case KeyEvent.KEYCODE_DPAD_UP:   //向上键
                JLog.d(TAG, "up--->");

                break;

            case KeyEvent.KEYCODE_0:   //数字键0
                JLog.d(TAG, "0--->");

                break;

            case KeyEvent.KEYCODE_DPAD_LEFT: //向左键

                JLog.d(TAG, "left--->");

                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:  //向右键
                JLog.d(TAG, "right--->");
                break;

            case KeyEvent.KEYCODE_INFO:    //info键
                JLog.d(TAG, "info--->");

                break;

            case KeyEvent.KEYCODE_PAGE_DOWN:     //向上翻页键
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                JLog.d(TAG, "page down--->");

                break;


            case KeyEvent.KEYCODE_PAGE_UP:     //向下翻页键
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                JLog.d(TAG, "page up--->");

                break;

            case KeyEvent.KEYCODE_VOLUME_UP:   //调大声音键
                JLog.d(TAG, "voice up--->");

                break;

            case KeyEvent.KEYCODE_VOLUME_DOWN: //降低声音键
                JLog.d(TAG, "voice down--->");

                break;
            case KeyEvent.KEYCODE_VOLUME_MUTE: //禁用声音
                JLog.d(TAG, "voice mute--->");
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
