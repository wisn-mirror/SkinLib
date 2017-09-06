package com.wisn.skinlib.attr.base;

import android.view.View;

import com.wisn.skinlib.utils.SkinUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinItem {
    public View view;
    public List<SkinAttr> attrs;
    public SkinItem(){
        attrs=new ArrayList<>();
    }
    public void apply(){
        if(SkinUtils.isEmpty(attrs))
            return ;
        for (SkinAttr skinAttr:attrs) {

        }
    }
}
