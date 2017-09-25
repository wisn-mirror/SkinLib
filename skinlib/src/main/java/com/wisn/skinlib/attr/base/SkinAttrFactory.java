package com.wisn.skinlib.attr.base;

import com.wisn.skinlib.attr.BackgroundAttr;
import com.wisn.skinlib.attr.ImageDrawableAttr;
import com.wisn.skinlib.attr.TextColorAttr;
import com.wisn.skinlib.config.SkinConfig;

import java.util.HashMap;

/**
 * Created by wisn on 2017/9/7.
 */

public class SkinAttrFactory {
    private static HashMap<String, SkinAttr> attrs = new HashMap<>();

    static {
        attrs.put(SkinConfig.Attrs_Support_background, new BackgroundAttr());
        attrs.put(SkinConfig.Attrs_Support_src, new ImageDrawableAttr());
        attrs.put(SkinConfig.Attrs_Support_textColor, new TextColorAttr());
    }

    public static SkinAttr get(String attrName,
                               int attrValueRefId,
                               String attrValueRefName,
                               String attrValueTypeName) {
        SkinAttr skinAttr = attrs.get(attrName).clone();
        if (skinAttr == null) return null;
        skinAttr.attrName = attrName;
        skinAttr.attrValueRefId = attrValueRefId;
        skinAttr.attrValueRefName = attrValueRefName;
        skinAttr.attrValueTypeName = attrValueTypeName;
        return skinAttr;
    }

    public static boolean isSupport(String attrName) {
        return attrs.containsKey(attrName);
    }

    public static void addSupport(String attrName, SkinAttr skinAttr) {
        attrs.put(attrName, skinAttr);
    }

}
