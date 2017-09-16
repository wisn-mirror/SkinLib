package com.wisn.skinlib.utils;

import android.content.Context;

import com.wisn.skinlib.config.SkinConfig;

/**
 * Created by wisn on 2017/9/6.
 */

public class SpUtils {
    public static String getCustomSkinName(Context context) {
        return SkinPreferencesUtils.getString(context,
                                              SkinConfig.SP_Custom_Skin_Path,
                                              SkinConfig.SP_Default_Skin_Path);
    }

    public static boolean isDefaultSkin(Context context) {
        return SkinConfig.SP_Default_Skin_Path.equals(getCustomSkinName(context));
    }

    public static boolean isNightMode(Context context) {
        return SkinPreferencesUtils.getBoolean(context, SkinConfig.SP_Night_Mode, false);
    }

    public static void setDefaultSkin(Context context) {
        SkinPreferencesUtils.putString(context, SkinConfig.SP_Custom_Skin_Path, SkinConfig.SP_Default_Skin_Path);
    }
    public static void setCustomSkinName(Context context, String path) {
        SkinPreferencesUtils.putString(context, SkinConfig.SP_Custom_Skin_Path, path);
    }

    public static void setCustomFontName(Context context, String path) {
        SkinPreferencesUtils.putString(context, SkinConfig.SP_Font_Path, path);
    }

    public static void setNightMode(Context context, boolean isNightMode) {
        SkinPreferencesUtils.putBoolean(context, SkinConfig.SP_Night_Mode, isNightMode);
    }


}
