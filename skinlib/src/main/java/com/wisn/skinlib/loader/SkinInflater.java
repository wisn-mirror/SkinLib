package com.wisn.skinlib.loader;

import android.view.View;

import com.wisn.skinlib.attr.base.SkinItem;

/**
 * Created by wisn on 2017/9/14.
 */

public abstract class SkinInflater {
    public abstract void applySkin();

    public abstract void clear();

    public abstract void addSkinView(SkinItem skinItem);

    public abstract void removeSkinView(View view);
}
