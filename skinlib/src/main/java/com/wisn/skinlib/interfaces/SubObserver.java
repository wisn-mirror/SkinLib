package com.wisn.skinlib.interfaces;

/**
 * Created by wisn on 2017/9/6.
 */

public interface SubObserver {
    void attach(ISkinUpdateObserver observer);
    void detach(ISkinUpdateObserver observer);
    void notifyUpdate();
}
