package com.wisn.skinlib.loader;

import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.attr.base.SkinItem;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinInflaterFactory implements LayoutInflaterFactory {
    private static final String TAG = "SkinInflaterFactory";
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
            //todo  change font style
        }
        if (attributeBooleanValue || SkinConfig.isGlobalChangeSkin) {
            if (view == null) {
                view = ViewInflat.createViewFromTag(context, name, attrs);
            }
            if (view == null) {
                return null;
            }
            dealSkinAttr(context, attrs, view);
        }
        return view;
    }

    private void dealSkinAttr(Context context, AttributeSet attrs, View view) {
       int attributeCount= attrs.getAttributeCount();
        List<SkinItem> viewAttrs=new ArrayList<>();
        for(int i=0;i<attributeCount;i++){
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            LogUtils.i(TAG,attributeName+" "+attributeValue);
        }

    }
    public void applySkin(){}

    public void clear() {}
}
