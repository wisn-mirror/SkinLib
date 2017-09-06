package com.wisn.skinlib.font;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.utils.SkinPreferencesUtils;

import java.io.File;

/**
 * Created by wisn on 2017/9/6.
 */

public class TypeFaceUtils {

    public static Typeface Current_typeFace;

    public static Typeface createTypeFace(Context context, String fontName) {
        Typeface typeface;
        if (fontName != null && !TextUtils.isEmpty(fontName)) {
            typeface = Typeface.createFromAsset(context.getAssets(), getFontPath(fontName));
            SkinPreferencesUtils.putString(context, SkinConfig.SP_Font_Path, getFontPath(fontName));
        } else {
            typeface = Typeface.DEFAULT;
            SkinPreferencesUtils.putString(context, SkinConfig.SP_Font_Path, "");
        }
        Current_typeFace = typeface;
        return typeface;
    }

    public static Typeface getTypeFace(Context context) {
        String fontPath = SkinPreferencesUtils.getString(context, SkinConfig.SP_Font_Path);
        Typeface typeface;
        if (fontPath == null || TextUtils.isEmpty(fontPath)) {
            typeface = Typeface.DEFAULT;
            SkinPreferencesUtils.putString(context, SkinConfig.SP_Font_Path, "");
        } else {
            typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
        }
        return typeface;
    }

    private static String getFontPath(String fontName) {
        return SkinConfig.FontDir + File.separator + fontName;
    }
}
