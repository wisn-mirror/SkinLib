package com.wisn.skinlib.loader;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;


/**
 * Created by wisn on 2017/9/6.
 */

public class ResourceCompat {
    public static Resources getResource(AssetManager assetManager, DisplayMetrics displayMetrics, Configuration configuration){
       return  new Resources(assetManager, displayMetrics, configuration);
    }
}
