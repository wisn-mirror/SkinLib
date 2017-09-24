package com.wisn.skinlib.interfaces;

import android.graphics.Typeface;

public interface ISkinUpdateObserver {
    void onThemUpdate();
    void onFontUpdate(Typeface typeface);
}