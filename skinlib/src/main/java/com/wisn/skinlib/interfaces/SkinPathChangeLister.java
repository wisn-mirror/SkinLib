package com.wisn.skinlib.interfaces;

/**
 * Created by wisn on 2017/9/16.
 */

public interface SkinPathChangeLister {
    void start();
    void progress(int current,int progress);
    void finish();
}
