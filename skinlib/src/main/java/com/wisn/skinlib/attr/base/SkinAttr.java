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
    public String attrName;
    public int attrValueRefId;
    public String attrValueRefName;
    public String attrValueTypeName;

    public abstract void applySkin(View view);

    public SkinAttr() {
        super();
    }

    public SkinAttr(String attrName,
                    int attrValueRefId) {
        setRes(attrName,attrValueRefId);
    }
    public void setRes(String attrName,
                       int attrValueRefId){
        this.attrName = attrName;
        this.attrValueRefId = attrValueRefId;
        this.attrValueRefName=SkinManager.getInstance().mContext.getResources().getResourceEntryName(attrValueRefId);
        this.attrValueTypeName=SkinManager.getInstance().mContext.getResources().getResourceTypeName(attrValueRefId);
    }


    public void apply(View view) {
        applySkin(view);
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
