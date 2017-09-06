package com.wisn.skinlib.attr;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.attr.base.SkinAttr;

/**
 * Created by wisn on 2017/9/6.
 */

public class ImageViewAttr extends SkinAttr {
    @Override
    public void applySkin(View view) {
        if(view instanceof ImageView){
            ImageView imageView=(ImageView)view;
            if(isDrawable()){
                imageView.setImageDrawable(SkinManager.getInstance().getDrawable(attrValueRefId));
            }else{
                imageView.setImageDrawable(new ColorDrawable(SkinManager.getInstance().getColor(attrValueRefId)));
            }
        }
    }
}
