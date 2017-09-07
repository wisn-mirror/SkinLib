package com.wisn.skinlib.attr;

import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.SkinAttr;

/**
 * Created by wisn on 2017/9/7.
 */

public class TextColorAttr extends SkinAttr {
    @Override
    public void applySkin(View view) {
        if (view instanceof TextView) {
            if (isColor()) {
                ((TextView) view).setTextColor(SkinManager.getInstance().getColor(attrValueRefId));
            }
        }
    }
}
