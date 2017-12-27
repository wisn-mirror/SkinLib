package com.wisn.skinlib.base;

import android.app.Application;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.task.SkinThreadPool;
import com.wisn.skinlib.utils.SkinFileUitls;

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

    public void configSkin() {
        SkinManager.getInstance().init(this, setSkinRootPath(),isSupplyRN());
        SkinThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                initSkinLoader();
            }
        });
    }


    private void initSkinLoader() {
        try {
            String[] skinFile = getAssets().list(SkinConfig.SkinDir);
            //拷贝assets到皮肤目录
            if (skinFile != null && skinFile.length != 0) {
                for (String fileName : skinFile) {
                    File toFile = new File(SkinFileUitls.getSkinPath(this, false), fileName);
                    if (!toFile.exists()) {
                        toFile.createNewFile();
                        SkinFileUitls.copySkinAssetsToSkinDir(this, fileName, toFile.getPath());
                    }
                }
            }
            if(isSupplyRN()){
                //检测皮肤目录和解压目录的一致性
                String skinPath = SkinFileUitls.getSkinPath(this, false);
                File file = new File(skinPath);
                if (file.exists()) {
                    String[] Skin = file.list();
                    if (Skin != null) {
                        for (String fileName : Skin) {
                            File skinRes = new File(SkinFileUitls.getSkinPath(this, true), fileName);
                            if (skinRes.exists() && skinRes.isDirectory()) {
                                continue;
                            }
                            SkinFileUitls.upZipSkin(this, skinPath + File.separator + fileName, fileName);
                        }
                    }
                }
            }
            String[] fontFile = getAssets().list(SkinConfig.FontDir);
            //拷贝assets font 到font目录
            if (fontFile == null || fontFile.length == 0) return;
            for (String fontName : fontFile) {
                File toFile = new File(SkinFileUitls.getSkinFontPath(this), fontName);
                if (!toFile.exists()) {
                    toFile.createNewFile();
                    SkinFileUitls.copyFontAssetsToSkinDir(this, fontName, toFile.getPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String setSkinRootPath() {
        return null;
    }

    public boolean isSupplyRN() {return false;}
}
