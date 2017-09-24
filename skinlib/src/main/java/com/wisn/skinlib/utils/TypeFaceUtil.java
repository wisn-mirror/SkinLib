package com.wisn.skinlib.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by wisn on 2017/9/24.
 */

public class TypeFaceUtil {

    private static void replaceFont(View view, Typeface typeface) {
        if (view == null) return;
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        } else if (view instanceof ViewGroup) {
            ViewGroup vg = ((ViewGroup) view);
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                replaceFont(vg.getChildAt(i), typeface);
            }
        }
    }

    public static void replaceFont(Activity activity, Typeface typeface) {
        if (activity == null || typeface == null) return;
        ViewGroup view = (ViewGroup) activity.getWindow().getDecorView();
        FrameLayout content = (FrameLayout) view.findViewById(android.R.id.content);
        replaceFont(content.getChildAt(0), typeface);
    }
}
