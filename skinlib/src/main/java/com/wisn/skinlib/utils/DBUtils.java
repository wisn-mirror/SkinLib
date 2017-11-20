package com.wisn.skinlib.utils;

import android.content.Context;

import com.wisn.skinlib.config.SkinConfig;

/**
 * Created by wisn on 2017/9/6.
 */

public class DBUtils {

    public static boolean isDefaultSkin(Context context) {
        return SkinConfig.SP_Default_Skin_Path.equals(getCustomSkinName(context));
    }

    public static void setDefaultSkin(Context context) {
        SkinPreferencesUtils.putString(context,
                                       SkinConfig.SP_Custom_Skin_Path,
                                       SkinConfig.SP_Default_Skin_Path);
    }

    public static void setCustomSkinName(Context context, String path) {
        SkinPreferencesUtils.putString(context, SkinConfig.SP_Custom_Skin_Path, path);
    }

    public static String getCustomSkinName(Context context) {
        return SkinPreferencesUtils.getString(context,
                                              SkinConfig.SP_Custom_Skin_Path,
                                              SkinConfig.SP_Default_Skin_Path);
    }

    public static void setCustomFontName(Context context, String fontName) {
        SkinPreferencesUtils.putString(context, SkinConfig.SP_Font_Path, fontName);
    }

    public static String getCustomFontName(Context context) {
        return SkinPreferencesUtils.getString(context,
                                              SkinConfig.SP_Font_Path,
                                              SkinConfig.SP_Font_Path);
    }

    public static void setSkinRootPath(Context context, String path) {
        SkinPreferencesUtils.putString(context, SkinConfig.SP_Skin_Root_Path, path);
    }

    public static String getSkinRootPath(Context context) {
        return SkinPreferencesUtils.getString(context,
                                              SkinConfig.SP_Skin_Root_Path,
                                              SkinConfig.SP_Default_Skin_Root_Path);
    }

    public static String getNightName(Context context) {
        return SkinPreferencesUtils.getString(context, SkinConfig.SP_Night_Name,"0");
    }

    public static void setNightName(Context context, String nightName) {
        SkinPreferencesUtils.putString(context, SkinConfig.SP_Night_Name, nightName);
    }

    public static boolean isNightMode(Context context) {
        return SkinPreferencesUtils.getBoolean(context, SkinConfig.SP_Night_Mode,false);
    }

    public static void setNightMode(Context context, boolean isNight) {
        SkinPreferencesUtils.putBoolean(context, SkinConfig.SP_Night_Mode, isNight);
    }

}
