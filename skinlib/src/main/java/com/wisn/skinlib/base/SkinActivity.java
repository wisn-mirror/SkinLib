package com.wisn.skinlib.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.DynamicAttr;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.attr.base.SkinItem;
import com.wisn.skinlib.interfaces.DynamicView;
import com.wisn.skinlib.interfaces.ISkinUpdateObserver;
import com.wisn.skinlib.loader.SkinInflaterFactory;

import java.util.List;

/**
 * Created by wisn on 2017/9/9.
 */

public class SkinActivity extends Activity implements ISkinUpdateObserver, DynamicView {
    private SkinInflaterFactory  skinInflaterFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        skinInflaterFactory = new SkinInflaterFactory();
        skinInflaterFactory.setActivity(this);
        getLayoutInflater().setFactory( skinInflaterFactory);
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
        skinInflaterFactory.clear();
    }
    @Override
    public void onThemUpdate() {
        skinInflaterFactory.applySkin();
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> attr) {

    }

    @Override
    public void dynamicAddView(View view, String attrName, int attrValueresId) {
        if(skinInflaterFactory==null)return ;
        skinInflaterFactory.addSkinView(this,view,attrName,attrValueresId);
    }

    @Override
    public void dynamicAddFontView(TextView textView) {

    }

    @Override
    public void dynamicAddView(SkinItem skinItem) {
        if(skinInflaterFactory==null)return ;
        skinInflaterFactory.addSkinView(skinItem);
    }

    @Override
    public void dynamicAddView(View view, SkinAttr skinAttr) {
        if(skinInflaterFactory==null)return ;
        skinInflaterFactory.addSkinView(view,skinAttr);
    }
}
