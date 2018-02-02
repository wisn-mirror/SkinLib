package com.wisn.skinlib.attr;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.utils.LogUtils;

/**
 * Created by wisn on 2017/9/6.
 */

public class ImageDrawableAttr extends SkinAttr {
    private static final String TAG = "ImageDrawableAttr";

    @Override
    public void applySkin(View view) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            if (isDrawable()) {
                imageView.setImageDrawable(SkinManager.getInstance().getDrawable(attrValueRefId));
            } else {
                imageView.setImageDrawable(new ColorDrawable(SkinManager.getInstance()
                                                                        .getColor(attrValueRefId)));
            }
            LogUtils.i(TAG, " applySkin view:" + view + " attrValueRefId:" + attrValueRefId);
        }
    }

}
