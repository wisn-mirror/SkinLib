package com.wisn.skinlib.base;

import android.app.Application;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.utils.SkinFileUitls;
import com.wisn.skinlib.utils.SpUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        configSkin();

    }
    public void configSkin(){
        initSkinLoader();
        SkinManager.getInstance().init(this);
        //夜间模式使用
        if(SpUtils.isNightMode(this)){
            SkinManager.getInstance().nightMode();
        }else{
            SkinManager.getInstance().loadSkin();
        }
    }

    private void initSkinLoader() {
        try {
            String[] skinFile=getAssets().list(SkinConfig.SkinDir);
            if(skinFile==null||skinFile.length==0)return ;
            for (String fileName:skinFile){
                File toFile=new File(SkinFileUitls.getSkinCache(this), fileName);
                if(!toFile.exists()){
                    toFile.createNewFile();
                    SkinFileUitls.CopyAssetsToDir(this, fileName, toFile.getPath());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
