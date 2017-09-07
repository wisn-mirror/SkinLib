package com.wisn.skinlib.attr;

import android.view.View;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.SkinAttr;

/**
 * Created by wisn on 2017/9/7.
 */

public class BackgroundAttr extends SkinAttr {
    @Override
    public void applySkin(View view) {
        if (isColor()) {
            view.setBackgroundColor(SkinManager.getInstance().getColor(attrValueRefId));
        } else {
            view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(attrValueRefId));
        }
    }
}
