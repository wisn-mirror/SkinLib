package com.wisn.skinlib.interfaces;

/**
 * Created by wisn on 2017/9/6.
 */

public interface SkinLoaderListener {
    void start();
    void onSuccess();
    void onFailed(String error);
    void onProgress(int progress,int sum);
}
