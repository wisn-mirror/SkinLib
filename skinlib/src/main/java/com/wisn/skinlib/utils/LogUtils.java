package com.wisn.skinlib.utils;

import android.util.Log;

import com.wisn.skinlib.config.SkinConfig;

/**
 * Created by wisn on 2017/9/6.
 */

public class LogUtils {

    public static void i(String tag, String msg) {
        if (SkinConfig.isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (SkinConfig.isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (SkinConfig.isDebug) {
            Log.e(tag, msg);
        }
    }
}
