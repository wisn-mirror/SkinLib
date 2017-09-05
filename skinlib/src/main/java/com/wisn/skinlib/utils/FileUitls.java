package com.wisn.skinlib.utils;

import android.content.Context;
import android.os.Environment;

import com.wisn.skinlib.config.SkinConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wisn on 2017/9/5.
 */

public class FileUitls {
    /**
     * copy skin from assets
     * @param context
     * @param skinName
     * @param toFilePath
     * @return
     */
    public static String CopyAssetsToDir(Context context,String skinName,String toFilePath ){
        InputStream is = null;
        OutputStream os=null;
        try {
            is=context.getAssets().open(SkinConfig.SkinDir+File.separator+skinName);
            File fileDir=new File(toFilePath);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            os=new FileOutputStream(fileDir);
            int index=0;
            byte[] bytes=new byte[1024];

            while((index=is.read(bytes))!=-1){
                os.write(bytes,0,index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return toFilePath;
    }

    /**
     * get skin FilePath
     * @param context
     * @return
     */
    public static String getSkinCache(Context context){
        File skinDir=new File(getCacherDir(context),SkinConfig.SkinDir);
        if(!skinDir.exists()){
            skinDir.mkdirs();
        }
        return skinDir.getAbsolutePath();
    }

    /**
     * get CacheDir
     * @param context
     * @return
     */
    public static String getCacherDir(Context context){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File cacheDir=context.getCacheDir();
            if(cacheDir!=null&&cacheDir.exists()){
                return cacheDir.getAbsolutePath();
            }
        }
        File cacheDir=context.getCacheDir();
        return  cacheDir.getAbsolutePath();
    }
}
