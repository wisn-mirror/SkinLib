package com.wisn.skinlib.utils;

import android.content.Context;
import android.os.Environment;

import com.wisn.skinlib.config.SkinConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinFileUitls {

    /**
     * copy skin from assets
     *
     * @param context
     * @param skinName
     * @param toFilePath
     *
     * @return
     */
    public static String CopyAssetsToSkinDir(Context context, String skinName, String toFilePath) {
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
     * get skin FilePath
     *
     * @param context
     *
     * @return
     */
    public static String getSkinPath(Context context) {
        File skinDir = new File(getCacherDir(context), SkinConfig.SkinDir);
        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }
        return skinDir.getAbsolutePath();
    }


    public static String getSkinResPath(Context context) {
        File skinDir = new File(getCacherDir(context), SkinConfig.SkinResDir);
        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }
        return skinDir.getAbsolutePath();
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
     * 保存新添加的皮肤
     *
     * @param fromFilePath
     * @param skinName
     *
     * @return
     */
    public static boolean saveSkinFile(Context context, String fromFilePath, String skinName) {
        if (fromFilePath == null || skinName == null) return false;
        return copyFile(new File(fromFilePath), new File(SkinFileUitls.getSkinPath(context), skinName));
    }

    public  static boolean upZipSkin(Context context, String zipFile) {
        if (zipFile == null) return false;
        return upZipFile(new File(zipFile), getSkinResPath(context));
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
