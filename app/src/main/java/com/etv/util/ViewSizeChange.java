package com.etv.util;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.RelativeLayout;

public class ViewSizeChange {

    public static void setGridViewSizeBig(Context context ,RelativeLayout view) {

        int height = SharedPerManager.getScreenHeight();
        int width = SharedPerManager.getScreenWidth();
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (isScreenOriatationPortrait(context)) {
            int widthShow = width * 4 / 5;
            localLayoutParams.width = widthShow;
            localLayoutParams.topMargin = width / 8;
            localLayoutParams.height = widthShow * 9 / 16;
            localLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            localLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            localLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }else {
            int widthShow = width * 9 / 19;
            localLayoutParams.width = widthShow;
            localLayoutParams.topMargin = width / 23;
            localLayoutParams.height = height * 9 / 19;
            localLayoutParams.rightMargin = width / 26;
            localLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            localLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            localLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        view.setLayoutParams(localLayoutParams);
    }

    public static void setGridViewSizeSmall(RelativeLayout view) {
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        localLayoutParams.width = 110;//小64  55寸竖屏90
        localLayoutParams.height = 110;//小64
        localLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        localLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        localLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view.setLayoutParams(localLayoutParams);
    }

    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
