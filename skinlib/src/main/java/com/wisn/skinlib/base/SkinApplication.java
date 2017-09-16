package com.wisn.skinlib.base;

import android.app.Application;
import android.os.Environment;

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

    public void configSkin() {
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().setSkinRootPath(Environment.getExternalStorageDirectory() +
                                                  File.separator +
                                                  "dd");
        initSkinLoader();
        //夜间模式使用
        if (SpUtils.isNightMode(this)) {
            SkinManager.getInstance().nightMode();
        } else {
            SkinManager.getInstance().loadSkin(null);
        }
    }

    private void initSkinLoader() {
        try {
            String[] skinFile = getAssets().list(SkinConfig.SkinDir);
            //拷贝assets到皮肤目录
            if (skinFile == null || skinFile.length == 0) return;
            for (String fileName : skinFile) {
                File toFile = new File(SkinFileUitls.getSkinPath(this,false), fileName);
                if (!toFile.exists()) {
                    toFile.createNewFile();
                    SkinFileUitls.copyAssetsToSkinDir(this, fileName, toFile.getPath());
                }
            }
            //检测皮肤目录和解压目录的一致性
            String skinPath = SkinFileUitls.getSkinPath(this,false);
            String[] Skin = new File(skinPath).list();
            for (String fileName : Skin) {
                File skinRes = new File(SkinFileUitls.getSkinPath(this,true), fileName);
                if (skinRes.exists() && skinRes.isDirectory()) {
                    continue;
                }
                SkinFileUitls.upZipSkin(this, skinPath + File.separator + fileName,fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
