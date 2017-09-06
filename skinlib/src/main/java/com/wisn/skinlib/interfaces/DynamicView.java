package com.wisn.skinlib.interfaces;

import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.attr.base.DynamicAttr;

import java.util.List;

/**
 * Created by wisn on 2017/9/6.
 */

public interface DynamicView {
    void dynamicAddView(View view, List<DynamicAttr> attr);
    void dynamicAddView(View view, String attrName,int attrValueresId);
    void dynamicAddFontView(TextView textView);

}
