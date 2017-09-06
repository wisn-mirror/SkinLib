package com.wisn.skinlib.interfaces;

/**
 * Created by wisn on 2017/9/6.
 */

public interface IWatchObserver {
    void attach(ISkinUpdate observer);
    void detach(ISkinUpdate observer);
    void notifyUpdate();
}
