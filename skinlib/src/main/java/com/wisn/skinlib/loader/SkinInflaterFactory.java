package com.wisn.skinlib.loader;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater.Factory2;
import android.view.View;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.DynamicAttr;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.attr.base.SkinAttrFactory;
import com.wisn.skinlib.attr.base.SkinItem;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisn on 2017/9/9.
 */

public class SkinInflaterFactory extends SkinInflater implements Factory2 {
    private Map<View, SkinItem> mSkinItemMap = new HashMap<>();
    private Activity mActivity = null;

    /**
     * api <=11
     *
     * @param name
     * @param context
     * @param attrs
     *
     * @return
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = ViewInflat.createViewFromTag(context, name, attrs);
        if (view == null) {
            return null;
        }
        parseSkin(context, attrs, view);
        return view;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * api >11
     *
     * @param parent
     * @param name
     * @param context
     * @param attrs
     *
     * @return
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        parseSkin(context, attrs, parent);
        return null;
    }

    public void parseSkin(Context context, AttributeSet attrs, View view) {
        boolean
                attributeBooleanValue =
                attrs.getAttributeBooleanValue(SkinConfig.NameSpace, SkinConfig.Attr_Skin_Enable, false);
        if (attributeBooleanValue || SkinConfig.isGlobalChangeSkin) {
            dealSkinAttr(context, attrs, view);
        }
    }

    private void dealSkinAttr(Context context, AttributeSet attrs, View view) {
        int attributeCount = attrs.getAttributeCount();
        List<SkinAttr> viewAttrs = new ArrayList<>();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            // TODO: 2017/9/7 style
            if (SkinConfig.Attrs_deal_char_style.equals(attributeName)) {
//                style="@style/AppTheme"
                String styleName = attributeValue.substring(attributeValue.indexOf("/") + 1);
                int
                        identifier =
                        context.getResources()
                               .getIdentifier(styleName,
                                              SkinConfig.Attrs_deal_char_style,
                                              context.getPackageName());
                int[]
                        skinAttrs =
                        new int[]{android.R.attr.textColor,
                                  android.R.attr.background,
                                  android.R.attr.drawableTop};
                TypedArray typedArray = context.getTheme().obtainStyledAttributes(identifier, skinAttrs);
                int textColor = typedArray.getResourceId(0, -1);
                int background = typedArray.getResourceId(1, -1);
                int drawableTop = typedArray.getResourceId(2, -1);
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
                //TODO: 2017/9/7   deal  drawableTop
              /*  if (drawableTop != -1) {
                    String resourceEntryName = mContext.getResources().getResourceEntryName(drawableTop);
                    String resourceTypeName = mContext.getResources().getResourceTypeName(drawableTop);
                    SkinAttr
                            skinAttr =
                            SkinAttrFactory.get(SkinConfig.Attrs_Support_drawableTop,
                                                drawableTop,
                                                resourceEntryName,
                                                resourceTypeName);
                    if (skinAttr != null) viewAttrs.add(skinAttr);
                }*/
                typedArray.recycle();
                continue;
            }

            // TODO: 2017/9/7 endregion
            if (attributeValue.startsWith(SkinConfig.Attrs_deal_char_index) &&
                SkinAttrFactory.isSupport(attributeName)) {
                try {
                    int id = Integer.parseInt(attributeValue.substring(1));
                    if (id == 0) continue;
                    String resourceEntryName = context.getResources().getResourceEntryName(id);
                    String resourceTypeName = context.getResources().getResourceTypeName(id);
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
        if (!ArrayUtils.isEmpty(viewAttrs)) {
            // TODO: 2017/9/7 attrs add map
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
        mSkinItemMap.clear();
        mSkinItemMap = null;
    }

    public void addSkinView(View view, SkinAttr skinAttr) {
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;
        List<SkinAttr> viewAttrs = new ArrayList<>();
        viewAttrs.add(skinAttr);
        skinItem.attrs = viewAttrs;
        skinItem.apply();
        addSkinView(skinItem);
    }

    @Override
    public void addSkinView(View view, List<DynamicAttr> attr) {
        if (attr == null || attr.size() == 0) return;
        List<SkinAttr> viewAttrs = new ArrayList<>();
        SkinItem skinItem = new SkinItem();
        for (DynamicAttr dynamicAttr : attr) {
            String entryName = mActivity.getResources().getResourceEntryName(dynamicAttr.refResId);
            String typeName = mActivity.getResources().getResourceTypeName(dynamicAttr.refResId);
            SkinAttr
                    mSkinAttr =
                    SkinAttrFactory.get(dynamicAttr.attrName, dynamicAttr.refResId, entryName, typeName);
            viewAttrs.add(mSkinAttr);
        }
        skinItem.attrs = viewAttrs;
        skinItem.view = view;
        skinItem.apply();
        addSkinView(skinItem);
    }

    @Override
    public void addSkinView(View view, String attrName, int attrValueresId) {
        String entryName = mActivity.getResources().getResourceEntryName(attrValueresId);
        String typeName = mActivity.getResources().getResourceTypeName(attrValueresId);
        SkinAttr mSkinAttr = SkinAttrFactory.get(attrName, attrValueresId, entryName, typeName);
        addSkinView(view, mSkinAttr);
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
        mSkinItemMap.remove(view);
    }
}
