package com.wisn.skinlib.base;

import android.app.Application;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.task.SkinThreadPool;
import com.wisn.skinlib.utils.SkinFileUitls;

import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2021/3/5 上午11:21
 */
public class SkinInit {

    public static void initSkin(final Application context, final SkinSetting skinSetting) {
        SkinManager.getInstance().init(context, skinSetting.setSkinRootPath(),skinSetting.isSupplyRN());
        SkinThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                initSkinLoader(context,skinSetting);
            }
        });
    }


    private static void initSkinLoader(Application context,SkinSetting skinSetting) {
        try {
            String[] skinFile = context.getAssets().list(SkinConfig.SkinDir);
            //拷贝assets到皮肤目录
            if (skinFile != null && skinFile.length != 0) {
                for (String fileName : skinFile) {
                    File toFile = new File(SkinFileUitls.getSkinPath(context, false), fileName);
                    if (!toFile.exists()) {
                        toFile.createNewFile();
                        SkinFileUitls.copySkinAssetsToSkinDir(context, fileName, toFile.getPath());
                    }
                }
            }
            if(skinSetting.isSupplyRN()){
                //检测皮肤目录和解压目录的一致性
                String skinPath = SkinFileUitls.getSkinPath(context, false);
                File file = new File(skinPath);
                if (file.exists()) {
                    String[] Skin = file.list();
                    if (Skin != null) {
                        for (String fileName : Skin) {
                            File skinRes = new File(SkinFileUitls.getSkinPath(context, true), fileName);
                            if (skinRes.exists() && skinRes.isDirectory()) {
                                continue;
                            }
                            SkinFileUitls.upZipSkin(context, skinPath + File.separator + fileName, fileName);
                        }
                    }
                }
            }
            String[] fontFile = context.getAssets().list(SkinConfig.FontDir);
            //拷贝assets font 到font目录
            if (fontFile == null || fontFile.length == 0) return;
            for (String fontName : fontFile) {
                File toFile = new File(SkinFileUitls.getSkinFontPath(context), fontName);
                if (!toFile.exists()) {
                    toFile.createNewFile();
                    SkinFileUitls.copyFontAssetsToSkinDir(context, fontName, toFile.getPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
