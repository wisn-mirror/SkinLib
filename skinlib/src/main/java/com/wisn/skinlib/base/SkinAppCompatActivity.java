package com.wisn.skinlib.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.DynamicAttr;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.attr.base.SkinItem;
import com.wisn.skinlib.interfaces.DynamicView;
import com.wisn.skinlib.interfaces.ISkinUpdateObserver;
import com.wisn.skinlib.interfaces.LayoutInflaterIns;
import com.wisn.skinlib.loader.SkinAppCompatInflaterFactory;
import com.wisn.skinlib.utils.TypeFaceUtil;

import java.util.List;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinAppCompatActivity extends AppCompatActivity implements ISkinUpdateObserver,
                                                                        DynamicView,
                                                                        LayoutInflaterIns {
    private SkinAppCompatInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinAppCompatInflaterFactory();
        mSkinInflaterFactory.setAppCompatActivity(this);
        LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
        super.onCreate(savedInstanceState);

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

    public SkinAppCompatInflaterFactory getSkinInflaterFactory() {
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
    public void dynamicAddView(SkinItem skinItem) {
        if (mSkinInflaterFactory == null) return;
        mSkinInflaterFactory.addSkinView(skinItem);
    }

    @Override
    public void dynamicAddView(View view, SkinAttr skinAttr) {
        if (mSkinInflaterFactory == null) return;
        mSkinInflaterFactory.addSkinView(view, skinAttr);
    }

    @Override
    public void dynamicAddFontView(TextView textView) {

    }
}
