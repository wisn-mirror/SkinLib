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
import com.wisn.skinlib.interfaces.ISkinUpdate;
import com.wisn.skinlib.loader.SkinInflaterFactory;

import java.util.List;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinActivity extends AppCompatActivity implements ISkinUpdate, DynamicView {
    private SkinInflaterFactory skinInflaterFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        skinInflaterFactory = new SkinInflaterFactory();
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
