package com.wisn.skinlib.attr.base;

import android.view.View;

/**
 * Created by wisn on 2017/9/5.
 */

public abstract class SkinAttr {
    protected static final String RES_TYPE_NAME_COLOR = "color";
    protected static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    protected static final String RES_TYPE_NAME_MIPMAP = "mipmap";

    public abstract void applySkin(View view);

    /**
     * Night skin
     * @param view
     */
    public  void applyNightSkin(View view){

    }

    public void apply(View view){

    }
}
