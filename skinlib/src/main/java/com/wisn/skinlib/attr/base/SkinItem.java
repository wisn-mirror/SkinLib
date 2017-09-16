package com.wisn.skinlib.attr.base;

import android.view.View;

import com.wisn.skinlib.utils.ArrayUtils;

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
        if(ArrayUtils.isEmpty(attrs))
            return ;
        for (SkinAttr skinAttr:attrs) {
            skinAttr.apply(view);
        }
    }
    public void clear(){
        if(!ArrayUtils.isEmpty(attrs)){
            attrs.clear();
        }
    }
}
