package com.wisn.skinlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.DynamicAttr;
import com.wisn.skinlib.interfaces.DynamicView;
import com.wisn.skinlib.interfaces.ISkinUpdateObserver;
import com.wisn.skinlib.interfaces.LayoutInflaterIns;
import com.wisn.skinlib.loader.SkinAppCompatInflaterFactory;

import java.util.List;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinAppCompatActivity extends AppCompatActivity implements ISkinUpdateObserver, DynamicView, LayoutInflaterIns {
    private SkinAppCompatInflaterFactory skinInflaterFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        skinInflaterFactory = new SkinAppCompatInflaterFactory();
        skinInflaterFactory.setAppCompatActivity(this);
        LayoutInflaterCompat.setFactory(getLayoutInflater(), skinInflaterFactory);
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

    public SkinAppCompatInflaterFactory getSkinInflaterFactory(){
        return skinInflaterFactory;
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

    }

    @Override
    public void dynamicAddFontView(TextView textView) {

    }
}
