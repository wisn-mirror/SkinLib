package com.wisn.skinlib.attr;

import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.utils.LogUtils;

/**
 * Created by wisn on 2017/9/7.
 */

public class TextColorAttr extends SkinAttr {
    private static final String TAG = "TextColorAttr";

    @Override
    public void applySkin(View view) {
        if (view instanceof TextView) {
            if (isColor()) {
                ((TextView) view).setTextColor(SkinManager.getInstance().getColor(attrValueRefId));
            }
            LogUtils.i(TAG, " applySkin view:" + view + " attrValueRefId:" + attrValueRefId);

        }
    }
}
