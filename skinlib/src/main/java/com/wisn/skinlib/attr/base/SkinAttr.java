package com.wisn.skinlib.attr.base;

import android.view.View;

import com.wisn.skinlib.SkinManager;

/**
 * Created by wisn on 2017/9/5.
 */

public abstract class SkinAttr implements Cloneable {
    protected static final String RES_TYPE_NAME_COLOR = "color";
    protected static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    protected static final String RES_TYPE_NAME_MIPMAP = "mipmap";
    protected String attrName;
    protected int attrValueRefId;
    protected String attrValueRefName;
    protected String attrValueTypeName;

    public abstract void applySkin(View view);

    public void applyNightSkin(View view) {

    }

    public void apply(View view) {
        if (SkinManager.getInstance().isNightMode()) {
            applySkin(view);
        } else {
            applySkin(view);
        }
    }

    protected boolean isDrawable() {
        return RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)
               || RES_TYPE_NAME_MIPMAP.equals(attrValueTypeName);
    }

    protected boolean isColor() {
        return RES_TYPE_NAME_COLOR.equals(attrValueTypeName);
    }

    @Override
    protected SkinAttr clone() {
        try {
            return (SkinAttr) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "SkinAttr{" +
               "attrName='" + attrName + '\'' +
               ", attrValueRefId=" + attrValueRefId +
               ", attrValueRefName='" + attrValueRefName + '\'' +
               ", attrValueTypeName='" + attrValueTypeName + '\'' +
               '}';
    }
}
