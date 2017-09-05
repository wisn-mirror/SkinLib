package com.wisn.skinlib.base;

import android.app.Application;

import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.utils.FileUitls;

import java.io.File;
import java.io.IOException;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initSkinLoader();
            }
        }).start();
    }

    private void initSkinLoader() {
        try {
            String[] skinFile=getAssets().list(SkinConfig.SkinDir);
            if(skinFile==null||skinFile.length==0)return ;
            for (String fileName:skinFile){
                File toFile=new File(FileUitls.getSkinCache(this), fileName);
                if(!toFile.exists()){
                    toFile.createNewFile();
                    FileUitls.CopyAssetsToDir(this,fileName,toFile.getPath());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
