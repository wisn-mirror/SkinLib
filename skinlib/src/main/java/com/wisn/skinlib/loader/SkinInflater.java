package com.wisn.skinlib.loader;

import android.view.View;

import com.wisn.skinlib.attr.base.DynamicAttr;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.attr.base.SkinItem;

import java.util.List;

/**
 * Created by wisn on 2017/9/14.
 */

public abstract class SkinInflater {
    public abstract void applySkin();

    public abstract void clear();

    public abstract void addSkinView(SkinItem skinItem);

    public abstract void addSkinView(View view, String attrName, int attrValueresId);

    public abstract void addSkinView(View view, SkinAttr skinAttr);

    public abstract void addSkinView(View view,  List<DynamicAttr> attr);

    public abstract void removeSkinView(View view);
}
