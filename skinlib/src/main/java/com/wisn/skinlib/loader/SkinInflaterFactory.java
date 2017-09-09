package com.wisn.skinlib.loader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater.Factory2;
import android.view.View;

/**
 * Created by wisn on 2017/9/9.
 */

public class SkinInflaterFactory implements Factory2 {
    /**
     * api <=11
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    /**
     * api >11
     * @param parent
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return null;
    }
}
