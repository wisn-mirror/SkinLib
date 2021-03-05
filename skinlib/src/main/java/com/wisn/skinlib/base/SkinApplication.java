package com.wisn.skinlib.base;

import android.app.Application;


/**
 * Created by wisn on 2017/9/5.
 */

public abstract class SkinApplication extends Application implements  SkinSetting {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinInit.initSkin(this,this);
    }

}
