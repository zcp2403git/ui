//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xiaoka.xksupportui.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager.LayoutParams;

/**
 * Created by changping on 16/5/9.
 */

public class ScreenUtil {
    public ScreenUtil() {
    }

    public static int getWidth(Context context) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    public static int getHeigth(Context context) {
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        return screenHeight;
    }

    public static int dp2Px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5F);
    }

    public static int px2Dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5F);
    }

    public static void fullScreen(Activity act) {
        act.getWindow().setFlags(1024, 1024);
    }

    public static void quitFullScreen(Activity act) {
        LayoutParams attrs = act.getWindow().getAttributes();
        attrs.flags &= -1025;
        act.getWindow().setAttributes(attrs);
        act.getWindow().clearFlags(512);
    }

    public static void setRequestedOrientationLANDSCAPE(Activity act) {
        act.setRequestedOrientation(0);
    }

    public static void setRequestedOrientationPORTRAIT(Activity act) {
        act.setRequestedOrientation(1);
    }
}
