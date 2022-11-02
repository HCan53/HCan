package com.hcan53.android.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

public class ClipboardUtils {
    /**
     * 获取剪切板中的文本数据
     * 该方法在安卓高版本中只能在Activity的onWindowFocusChanged中使用
     *
     * @param activity
     * @return
     */
    public static String getPasteString(Activity activity) {
        ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    /**
     * 文本写入剪切板中
     *
     * @param activity
     * @param argsString
     */
    public static void writePaste(Activity activity, String argsString) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", argsString.trim());
        clipboard.setPrimaryClip(clipData);
    }
}
