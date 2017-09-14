package com.wisn.skinlib.loader;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.attr.base.SkinAttrFactory;
import com.wisn.skinlib.attr.base.SkinItem;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.utils.LogUtils;
import com.wisn.skinlib.utils.SkinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinAppCompatInflaterFactory extends SkinInflater implements LayoutInflaterFactory {
    private static final String TAG = "SkinAppCompatInflaterFactory";
    private Map<View, SkinItem> mSkinItemMap = new HashMap<>();
    private AppCompatActivity mAppCompatActivity;

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.mAppCompatActivity = appCompatActivity;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        boolean
                attributeBooleanValue =
                attrs.getAttributeBooleanValue(SkinConfig.NameSpace, SkinConfig.Attr_Skin_Enable, false);
        AppCompatDelegate delegate = mAppCompatActivity.getDelegate();
        View view = delegate.createView(parent, name, context, attrs);
        if (view instanceof TextView && SkinConfig.isChangeFont) {
            // TODO: 2017/9/7 change font style
            FontRepository.add(mAppCompatActivity, view);
        }
        if (attributeBooleanValue || SkinConfig.isGlobalChangeSkin) {
            if (view == null) {
                view = ViewInflat.createViewFromTag(context, name, attrs);
            }
            if (view == null) {
                return null;
            }
            LogUtils.i(TAG, "dealSkinAttr");
            dealSkinAttr(context, attrs, view);
        }
        return view;
    }

    private void dealSkinAttr(Context context, AttributeSet attrs, View view) {
        int attributeCount = attrs.getAttributeCount();
        List<SkinAttr> viewAttrs = new ArrayList<>();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            LogUtils.i(TAG, attributeName + " " + attributeValue);
            // TODO: 2017/9/7 style
            if (SkinConfig.Attrs_deal_char_style.equals(attributeName)) {
//                style="@style/AppTheme"
                LogUtils.i(TAG, "Attrs_deal_char_style");
                String styleName = attributeValue.substring(attributeValue.indexOf("/") + 1);
                int
                        identifier =
                        context.getResources()
                               .getIdentifier(styleName,
                                              SkinConfig.Attrs_deal_char_style,
                                              context.getPackageName());
                int[] skinAttrs = new int[]{android.R.attr.textColor, android.R.attr.background};
                TypedArray typedArray = context.getTheme().obtainStyledAttributes(identifier, skinAttrs);
                int textColor = typedArray.getResourceId(0, -1);
                int background = typedArray.getResourceId(1, -1);
                // TODO: 2017/9/7 deal textcolor
                if (textColor != -1) {
                    String resourceEntryName = context.getResources().getResourceEntryName(textColor);
                    String resourceTypeName = context.getResources().getResourceTypeName(textColor);
                    SkinAttr
                            skinAttr =
                            SkinAttrFactory.get(SkinConfig.Attrs_Support_textColor,
                                                textColor,
                                                resourceEntryName,
                                                resourceTypeName);
                    if (skinAttr != null) viewAttrs.add(skinAttr);
                }
                //TODO: 2017/9/7   deal  baground
                if (background != -1) {
                    String resourceEntryName = context.getResources().getResourceEntryName(background);
                    String resourceTypeName = context.getResources().getResourceTypeName(background);
                    SkinAttr
                            skinAttr =
                            SkinAttrFactory.get(SkinConfig.Attrs_Support_background,
                                                background,
                                                resourceEntryName,
                                                resourceTypeName);
                    if (skinAttr != null) viewAttrs.add(skinAttr);
                }
                typedArray.recycle();
                continue;
            }

            // TODO: 2017/9/7 endregion
            if (attributeValue.startsWith(SkinConfig.Attrs_deal_char_index) &&
                SkinAttrFactory.isSupport(attributeName)) {
                LogUtils.i(TAG, "Attrs_deal_char_index");
                try {
                    int id = Integer.parseInt(attributeValue.substring(1));
                    if (id == 0) continue;
                    String resourceEntryName = context.getResources().getResourceEntryName(id);
                    String resourceTypeName = context.getResources().getResourceTypeName(id);
                    LogUtils.i(TAG,
                               "attributeName:" +
                               attributeName +
                               " id" +
                               id +
                               " resourceEntryName:" +
                               resourceEntryName +
                               " resourceTypeName:" +
                               resourceTypeName);
                    SkinAttr
                            skinAttr =
                            SkinAttrFactory.get(attributeName,
                                                id,
                                                resourceEntryName,
                                                resourceTypeName);
                    if (skinAttr != null) viewAttrs.add(skinAttr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!SkinUtils.isEmpty(viewAttrs)) {
            // TODO: 2017/9/7 attrs add map
            for (SkinAttr skinAttr : viewAttrs) {
                LogUtils.e(TAG, "skinAttr:" + skinAttr.toString());
            }
            SkinItem skinItem = new SkinItem();
            skinItem.view = view;
            skinItem.attrs = viewAttrs;
            mSkinItemMap.put(view, skinItem);
            if (SkinManager.getInstance().isExternalSkin()) {
                skinItem.apply();
            }
        }
    }

    public void applySkin() {
        if (mSkinItemMap == null || mSkinItemMap.isEmpty()) return;
        for (View view : mSkinItemMap.keySet()) {
            if (view == null) {
                continue;
            }
            mSkinItemMap.get(view).apply();
        }
    }

    public void clear() {
        if (mSkinItemMap == null || mSkinItemMap.isEmpty()) return;
        FontRepository.remove(mAppCompatActivity);
        mSkinItemMap.clear();
        mSkinItemMap = null;
    }

    public void addSkinView(SkinItem skinItem) {
        if (skinItem == null || mSkinItemMap == null) return;
        if (mSkinItemMap.get(skinItem.view) != null) {
            mSkinItemMap.get(skinItem.view).attrs.addAll(skinItem.attrs);
        } else {
            mSkinItemMap.put(skinItem.view, skinItem);
        }
    }

    public void removeSkinView(View view) {
        if (view == null || mSkinItemMap == null) return;
        SkinItem skinItem = mSkinItemMap.remove(view);
        if (skinItem != null && SkinConfig.isChangeFont && view instanceof TextView) {
            FontRepository.remove(mAppCompatActivity, view);
        }
    }
}