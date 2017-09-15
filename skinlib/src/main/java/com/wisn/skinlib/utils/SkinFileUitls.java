package com.wisn.skinlib.utils;

import android.content.Context;
import android.os.Environment;

import com.wisn.skinlib.config.SkinConfig;

import java.io.File;
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
    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    /**
     * copy skin from assets
     *
     * @param context
     * @param skinName
     * @param toFilePath
     *
     * @return
     */
    public static String CopyAssetsToDir(Context context, String skinName, String toFilePath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getAssets().open(SkinConfig.SkinDir + File.separator + skinName);
            File fileDir = new File(toFilePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            os = new FileOutputStream(fileDir);
            int index = 0;
            byte[] bytes = new byte[1024];

            while ((index = is.read(bytes)) != -1) {
                os.write(bytes, 0, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
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
     *
     * @param context
     *
     * @return
     */
    public static String getSkinCache(Context context) {
        File skinDir = new File(getCacherDir(context), SkinConfig.SkinDir);
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
     * 解压缩一个文件
     *
     * @param zipFile    压缩文件
     * @param folderPath 解压缩的目标目录
     */
    public static void upZipFile(File zipFile, String folderPath) {
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
                OutputStream out = new FileOutputStream(desFile);
                byte buffer[] = new byte[BUFF_SIZE];
                int realLength;
                while ((realLength = in.read(buffer)) > 0) {
                    out.write(buffer, 0, realLength);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
