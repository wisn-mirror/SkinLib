package com.wisn.skinlib.utils;

import android.graphics.Color;

/**
 * Created by wisn on 2017/9/19.
 */

public class ColorUtils {

    public static String colorToARGB(int color) {
        String alpha = Integer.toHexString(Color.alpha(color));
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (alpha.length() == 1) alpha = "0" + alpha;
        if (red.length() == 1) red = "0" + red;
        if (green.length() == 1) green = "0" + green;
        if (blue.length() == 1) blue = "0" + blue;
        return "#" + alpha + red + green + blue;
    }

    public static String colorToRGB(int color) {
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (red.length() == 1) red = "0" + red;
        if (green.length() == 1) green = "0" + green;
        if (blue.length() == 1) blue = "0" + blue;
        return "#" + red + green + blue;
    }


    public static int colorToInt(String argb)
            throws IllegalArgumentException {
        if (!argb.startsWith("#")) argb = "#" + argb;
        return Color.parseColor(argb);
    }
}
