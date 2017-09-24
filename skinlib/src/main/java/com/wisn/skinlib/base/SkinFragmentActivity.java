package com.wisn.skinlib.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.DynamicAttr;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.attr.base.SkinItem;
import com.wisn.skinlib.interfaces.DynamicView;
import com.wisn.skinlib.interfaces.ISkinUpdateObserver;
import com.wisn.skinlib.interfaces.LayoutInflaterIns;
import com.wisn.skinlib.loader.SkinInflater;
import com.wisn.skinlib.loader.SkinInflaterFactory;
import com.wisn.skinlib.utils.TypeFaceUtil;

import java.util.List;

/**
 * Created by wisn on 2017/9/14.
 */

public class SkinFragmentActivity extends FragmentActivity implements ISkinUpdateObserver,
                                                                      DynamicView,
                                                                      LayoutInflaterIns {

    private SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinInflaterFactory();
        mSkinInflaterFactory.setActivity(this);
        getLayoutInflater().setFactory(mSkinInflaterFactory);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TypeFaceUtil.replaceFont(this,SkinManager.getInstance().getTypeFace());
    }


    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clear();
    }

    public SkinInflater getSkinInflaterFactory() {
        return mSkinInflaterFactory;
    }

    @Override
    public void onThemUpdate() {
        mSkinInflaterFactory.applySkin();
    }

    @Override
    public void onFontUpdate(Typeface typeface) {
        TypeFaceUtil.replaceFont(this, typeface);
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> attr) {
        if (mSkinInflaterFactory == null) return;
        mSkinInflaterFactory.addSkinView(view, attr);
    }

    @Override
    public void dynamicAddView(View view, String attrName, int attrValueresId) {
        if (mSkinInflaterFactory == null) return;
        mSkinInflaterFactory.addSkinView(view, attrName, attrValueresId);
    }

    @Override
    public void dynamicAddFontView(TextView textView) {

    }

    @Override
    public void dynamicAddView(SkinItem skinItem) {
        if (mSkinInflaterFactory == null) return;
        mSkinInflaterFactory.addSkinView(skinItem);
    }

    @Override
    public void dynamicAddView(View view, SkinAttr skinAttr) {
        if (mSkinInflaterFactory == null) return;
        mSkinInflaterFactory.addSkinView(view, skinAttr);
    }
}

