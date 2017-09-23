package com.wisn.skinlib.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.interfaces.SkinLoaderListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinFileUitls {

    public static void updateSkinPath(final Context context,
                                      final String newSkinRootPath,
                                      final SkinLoaderListener skinLoaderListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (skinLoaderListener != null) {
                        skinLoaderListener.start();
                    }
                    String skinPath = getSkinPath(context, false);
                    if (skinPath.equals(newSkinRootPath + File.separator + SkinConfig.SkinDir)) {
                        if (skinLoaderListener != null) {
                            skinLoaderListener.onFailed("skin rootPath already set");
                        }
                        return;
                    }
                    File skinFile = new File(newSkinRootPath + File.separator + SkinConfig.SkinDir);
                    File skinFileRes = new File(newSkinRootPath + File.separator + SkinConfig.SkinResDir);
                    String[] Skin = new File(skinPath).list();
                    if (!skinFile.exists()) {
                        skinFile.mkdirs();
                    }
                    if (!skinFileRes.exists()) {
                        skinFileRes.mkdirs();
                    }
                    int i = 0;
                    for (String fileName : Skin) {
                        copyFile(new File(skinPath + File.separator + fileName),
                                 new File(skinFile, fileName));
                        upZipFile(new File(skinPath + File.separator + fileName),
                                  skinFileRes.getAbsolutePath() + File.separator
                                  + fileName);
                        i++;
                        if (skinLoaderListener != null) {
                            skinLoaderListener.onProgress(i, Skin.length);
                        }
                    }
                    SpUtils.setSkinRootPath(context, newSkinRootPath);
                    if (skinLoaderListener != null) {
                        skinLoaderListener.onSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (skinLoaderListener != null) {
                        skinLoaderListener.onFailed(e.getMessage());
                    }
                }
            }
        }).start();
    }

    /**
     * copy skin from assets
     *
     * @param context
     * @param skinName
     * @param toFilePath
     *
     * @return
     */
    public static String copyAssetsToSkinDir(Context context, String skinName, String toFilePath) {
        InputStream is = null;
        try {
            is = context.getAssets().open(SkinConfig.SkinDir + File.separator + skinName);
            File fileDir = new File(toFilePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            copyFileStream(is, new FileOutputStream(fileDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toFilePath;
    }

    /**
     * 保存新添加的皮肤
     *
     * @param fromFilePath
     * @param skinName
     *
     * @return
     */
    public static boolean saveSkinFile(Context context, String fromFilePath, String skinName) {
        if (fromFilePath == null || skinName == null) return false;
        return copyFile(new File(fromFilePath),
                        new File(SkinFileUitls.getSkinPath(context, false), skinName));
    }

    /**
     * @param context
     * @param zipFile
     * @param skinName
     *
     * @return
     */
    public static boolean upZipSkin(Context context, String zipFile, String skinName) {
        if (zipFile == null) return false;
        return upZipFile(new File(zipFile), getSkinPath(context, true) + File.separator + skinName);
    }

    /**
     * 拷贝单个文件到指定文件
     *
     * @param fromFile
     * @param toFile
     *
     * @return
     */
    private static boolean copyFile(File fromFile, File toFile) {
        if (fromFile == null || toFile == null) return false;
        try {
            if (!toFile.exists()) toFile.createNewFile();
            return copyFileStream(new FileInputStream(fromFile), new FileOutputStream(toFile));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * get skin FilePath
     *
     * @param context
     *
     * @return
     */
    public static String getSkinPath(Context context, boolean isRes) {
        String skinRootPath = SpUtils.getSkinRootPath(context);
        if (SkinConfig.SP_Default_Skin_Root_Path.equals(skinRootPath)) {
            skinRootPath = getCacherDir(context);
        }
        File skinDir = new File(skinRootPath, isRes ? SkinConfig.SkinResDir : SkinConfig.SkinDir);
        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }
        Log.d("SkinFileUtils", skinDir.getAbsolutePath());
        return skinDir.getAbsolutePath();
    }

    /**
     *  get SkinFontPath
     * @param context
     * @return
     */
    public static String getSkinFontPath(Context context) {
        String skinRootPath = SpUtils.getSkinRootPath(context);
        if (SkinConfig.SP_Default_Skin_Root_Path.equals(skinRootPath)) {
            skinRootPath = getCacherDir(context);
        }
        File skinDir = new File(skinRootPath,  SkinConfig.FontDir);
        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }
        Log.d("SkinFileUtils", skinDir.getAbsolutePath());
        return skinDir.getAbsolutePath();
    }

    /**
     * @param context
     * @param isRes
     *
     * @return
     */
    public static File[] getSkinListFile(Context context, boolean isRes) {
        String skinRootPath = SpUtils.getSkinRootPath(context);
        if (SkinConfig.SP_Default_Skin_Root_Path.equals(skinRootPath)) {
            skinRootPath = getCacherDir(context);
        }
        File skinDir = new File(skinRootPath, isRes ? SkinConfig.SkinResDir : SkinConfig.SkinDir);
        if (skinDir == null || !skinDir.exists()) {
            return null;
        }
        return skinDir.listFiles();
    }


    /**
     * 获取皮肤名称列表
     *
     * @param context
     * @param isRes
     *
     * @return
     */
    public static List<String> getSkinListName(Context context, boolean isRes, boolean isPath) {
        File[] skinListFile = getSkinListFile(context, isRes);
        if (skinListFile == null) return null;
        List<String> skinListName = new ArrayList<>();
        for (File file : skinListFile) {
            if (isPath) {
                skinListName.add(file.getAbsolutePath());
            } else {
                skinListName.add(file.getName());
            }
        }
        return skinListName;
    }


    /**
     * get CacheDir
     *
     * @param context
     *
     * @return
     */
    public static String getCacherDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                return cacheDir.getAbsolutePath();
            }
        }
        File cacheDir = context.getCacheDir();
        return cacheDir.getAbsolutePath();
    }


    /**
     * 解压缩皮肤
     *
     * @param zipFile    解压路径
     * @param folderPath 解压缩的目标目录
     */
    private static boolean upZipFile(File zipFile, String folderPath) {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = null;
        try {
            zf = new ZipFile(zipFile);
            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                InputStream in = zf.getInputStream(entry);
                String str = folderPath + File.separator + entry.getName();
                str = new String(str.getBytes("8859_1"), "GB2312");
                File desFile = new File(str);
                if (!desFile.exists()) {
                    File fileParentDir = desFile.getParentFile();
                    if (!fileParentDir.exists()) {
                        fileParentDir.mkdirs();
                    }
                    desFile.createNewFile();
                }
                copyFileStream(in, new FileOutputStream(desFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 拷贝流
     *
     * @param inputStream
     * @param outputStream
     *
     * @return
     */
    private static boolean copyFileStream(InputStream inputStream, OutputStream outputStream) {
        try {
            int index = 0;
            byte[] bytes = new byte[1024 * 1024];
            while ((index = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
