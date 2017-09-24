package com.wisn.skinlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.font.TypeFaceUtils;
import com.wisn.skinlib.interfaces.ISkinUpdateObserver;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.interfaces.SubObserver;
import com.wisn.skinlib.utils.ColorUtils;
import com.wisn.skinlib.utils.LogUtils;
import com.wisn.skinlib.utils.SkinFileUitls;
import com.wisn.skinlib.utils.SpUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinManager implements SubObserver {
    private static final String TAG = "SkinManager";
    public Context context;
    private List<ISkinUpdateObserver> mSkinObservers;
    private boolean mNightMode = false;
    public boolean isDefaultSkin = true;
    private Resources mResources;
    private String mPackageName;
    public String skinPath;
    public String skinPathRes;
    private LinkedHashMap<String, LinkedHashMap<String, String>> skinData = new LinkedHashMap<>();

    private SkinManager() {}

    public static SkinManager manager;

    public static SkinManager getInstance() {
        if (manager == null) {
            synchronized (SkinManager.class) {
                if (manager == null) {
                    manager = new SkinManager();
                }
            }
        }
        return manager;
    }

    public void init(Context ctx) {
        context = ctx.getApplicationContext();
        SkinConfig.Density = context.getResources().getDisplayMetrics().density;
        SkinConfig.FirstIndex = getFirstIndex();
        TypeFaceUtils.getTypeFace(context);
        if (SpUtils.isNightMode(context)) {
            SkinManager.getInstance().nightMode();
        } else {
            SkinManager.getInstance().loadSkin(null);
        }
        printInfo();
    }

    public void printInfo() {
        LogUtils.e(TAG, "printInfo:getCustomSkinName:" + SpUtils.getCustomSkinName(context));
        LogUtils.e(TAG, "printInfo:isDefaultSkin:" + SpUtils.isDefaultSkin(context));
        LogUtils.e(TAG, "printInfo:getSkinRootPath:" + SpUtils.getSkinRootPath(context));
        LogUtils.e(TAG, "printInfo:getSkinPath:" + SkinFileUitls.getSkinPath(context, false));
        LogUtils.e(TAG, "printInfo:getSkinPath:" + SkinFileUitls.getSkinPath(context, true));
    }


    public String getPathForRN(String imageName) {
        return getPath(imageName, true);
    }

    public String getPath(String imageName) {
        return getPath(imageName, false);
    }

    /**
     * updateSkinPath
     *
     * @param newSkinRootPath
     * @param skinLoaderListener
     */
    public void updateSkinPath(String newSkinRootPath, SkinLoaderListener skinLoaderListener) {
        SkinFileUitls.updateSkinPath(context, newSkinRootPath, skinLoaderListener);
    }

    /**
     * @param listener
     */
    public void loadSkin(SkinLoaderListener listener) {
        if (SpUtils.isDefaultSkin(context)) {
            skinPath = null;
            skinPathRes = null;
            LogUtils.e(TAG, "default skin");
            return;
        } else {
            String customSkinName = SpUtils.getCustomSkinName(context);
            loadSkin(customSkinName, listener);
        }
    }

    /**
     * @param skinFilePath
     * @param skinName
     *
     * @return
     */
    public boolean saveSkin(String skinFilePath,
                            String skinName) {
        List<String> skinListName = getSkinListName(false, false);
        List<String> skinResListName = getSkinListName(true, false);
        if (skinListName != null &&
            skinListName.contains(skinName) &&
            skinResListName != null &&
            skinResListName.contains(skinName)) return true;
        return (SkinFileUitls.saveSkinFile(context, skinFilePath, skinName) &&
                SkinFileUitls.upZipSkin(context, skinFilePath, skinName));
    }

    /**
     * @param skinName
     * @param listener
     */
    public void loadSkin(String skinName, final SkinLoaderListener listener) {
        if (skinName == null) {
            return;
        }
        new AsyncTask<String, Void, Resources>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (listener != null) {
                    listener.start();
                }
            }

            @Override
            protected Resources doInBackground(String... strings) {
                try {
                    if (strings != null && strings.length == 1 && strings[0] != null) {
                        String skinPath =
                                SkinFileUitls.getSkinPath(context, false) +
                                File.separator +
                                strings[0];
                        String skinPathRes =
                                SkinFileUitls.getSkinPath(context, true) +
                                File.separator +
                                strings[0] + "/res/";
                        LogUtils.i(TAG, skinPath);
                        File skinFile = new File(skinPath);
                        if (!skinFile.exists()) {
                            return null;
                        }
                        PackageManager packageManager = context.getPackageManager();
                        PackageInfo
                                packageArchiveInfo =
                                packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                        if (packageArchiveInfo == null) return null;
                        mPackageName = packageArchiveInfo.packageName;

                        AssetManager assetManager = AssetManager.class.newInstance();


                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

                        addAssetPath.invoke(assetManager, skinPath);

                        Resources superRes = context.getResources();
                        Resources
                                resource =
                                new Resources(assetManager,
                                              superRes.getDisplayMetrics(),
                                              superRes.getConfiguration());
                        SkinManager.this.skinPath = skinPath;
                        SkinManager.this.skinPathRes = skinPathRes;
                        LogUtils.e(TAG,
                                   "skinPath:" +
                                   SkinManager.this.skinPath +
                                   "   skinPathRes:" +
                                   SkinManager.this.skinPathRes);
                        loadSkinFileForRN(skinPathRes);
                        SpUtils.setCustomSkinName(context, strings[0]);
                        mResources = resource;
                        return resource;
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Resources resources) {
                if (resources != null) {
                    mResources = resources;
                    isDefaultSkin = false;
                    mNightMode = false;
                    SpUtils.setNightMode(context, false);
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {

                                    if (listener != null) listener.onSuccess();
                                    notifySkinUpdate();
                                }
                            }
                    );
                } else {
                    isDefaultSkin = true;
                    if (listener != null) listener.onFailed(" Resource is null ");
                }
            }
        }.execute(skinName);
    }

    public void loadSkinFileForRN(String skinPathRes) {
        File file = new File(skinPathRes);
        if (file.exists() && file.isDirectory()) {
            if (skinData != null) {
                skinData.clear();
                pasFileIndex(file);
            }
        }
    }

    public boolean isExternalSkin() {
        return mResources != null && !isDefaultSkin;
    }

    public boolean isNightMode() {
        return mNightMode;
    }

    public void nightMode() {
        resetDefaultThem();
        mNightMode = true;
        SpUtils.setNightMode(context, true);
        notifySkinUpdate();
    }

    public String getCurrentThemName() {
        if (SpUtils.isDefaultSkin(context)) {
            return null;
        } else {
            return SpUtils.getCustomSkinName(context);
        }
    }

    public List<String> getSkinListName(boolean isRes, boolean isPath) {
        return SkinFileUitls.getSkinListName(context, isRes, isPath);
    }

    public void resetDefaultThem() {
        isDefaultSkin = true;
        mNightMode = false;
        skinPath = null;
        skinPathRes = null;
        SpUtils.setNightMode(context, false);
        SpUtils.setDefaultSkin(context);
        notifySkinUpdate();
    }

    public void loadFont(String fontName) {

    }

    public void saveFont(String path, String fontName) {

    }

    @Override
    public void attach(ISkinUpdateObserver observer) {
        if (mSkinObservers == null) {
            mSkinObservers = new ArrayList<>();
        }
        mSkinObservers.add(observer);
    }

    @Override
    public void detach(ISkinUpdateObserver observer) {
        if (mSkinObservers == null) return;
        if (mSkinObservers.contains(observer)) {
            mSkinObservers.remove(observer);
        }
    }

    /**
     * @hide
     */
    @Override
    public void notifySkinUpdate() {
        if (mSkinObservers == null) return;
        for (ISkinUpdateObserver iSkinUpdate : mSkinObservers) {
            iSkinUpdate.onThemUpdate();
        }
    }

    /**
     * @hide
     */
    @Override
    public void notifyFontUpdate(Typeface typeface) {
        if (mSkinObservers == null) return;
        for (ISkinUpdateObserver iSkinUpdate : mSkinObservers) {
            iSkinUpdate.onFontUpdate(typeface);
        }
    }

    /**
     * @param resId
     *
     * @return
     */
    public int getColor(int resId) {
        int color = 0;
        if (mResources == null || isDefaultSkin) {
            color = ContextCompat.getColor(context, resId);
            return color;
        }
        int colorResId =
                mResources.getIdentifier(context.getResources().getResourceEntryName(resId),
                                         "color",
                                         mPackageName);
        if (colorResId == 0) {
            color = ContextCompat.getColor(context, resId);
        } else {
            color = mResources.getColor(colorResId);
        }
        return color;
    }

    /**
     * RN通过颜色名获取颜色
     *
     * @param colorName
     *
     * @return
     */
    public String getColorForRN(String colorName) {
        int color = 0;
        int colorResId = 0;
        if (mResources == null || isDefaultSkin) {
            colorResId = context.getResources().getIdentifier(colorName,
                                                              "color",
                                                              context.getPackageName());
            color = context.getResources().getColor(colorResId);
            LogUtils.e(TAG, "colorName aaa:" + colorName + "value:" + color);
        } else {
            colorResId =
                    mResources.getIdentifier(colorName,
                                             "color",
                                             mPackageName);
            color = mResources.getColor(colorResId);
            LogUtils.e(TAG, "colorName:" + colorName + "value:" + color);
        }
        if (color == 0) return null;
        return ColorUtils.colorToRGB(color);
    }

    /**
     * @param attrValueRefId
     *
     * @return
     */
    public Drawable getDrawable(int attrValueRefId) {
        Drawable drawable = null;
        if (mResources == null || isDefaultSkin) {
            LogUtils.i(TAG, "  mResources is null");
            drawable = ContextCompat.getDrawable(context, attrValueRefId);
            return drawable;
        }
        LogUtils.i(TAG, "  getDrawable drawableid");
        int drawableid =
                mResources.getIdentifier(context.getResources().getResourceEntryName(attrValueRefId),
                                         "drawable",
                                         mPackageName);
        LogUtils.i(TAG, "  drawableid :" + drawableid + " mPackageName:" + mPackageName);
        if (drawableid == 0) {
            drawableid = mResources.getIdentifier(context.getResources().getResourceEntryName(attrValueRefId),
                                                  "mipmap",
                                                  mPackageName);
            LogUtils.i(TAG, "  drawableid :" + drawableid);

        }
        if (drawableid == 0) {
            LogUtils.i(TAG, "  drawableid is 0 ");
            drawable = ContextCompat.getDrawable(context, attrValueRefId);
        } else {
            if (Build.VERSION.SDK_INT < 22) {
                drawable = mResources.getDrawable(drawableid);
            } else {
                drawable = mResources.getDrawable(drawableid, null);
            }
        }
        LogUtils.i(TAG, " drawable: " + drawable);
        return drawable;
    }


    /**
     * @param attrValueRefId
     * @param dir            drawable/mipmap
     *
     * @return
     */
    public Drawable getDrawable(int attrValueRefId, String dir) {
        Drawable drawable = null;
        if (mResources == null || isDefaultSkin) {
            drawable = ContextCompat.getDrawable(context, attrValueRefId);
            return drawable;
        }
        int drawableid =
                mResources.getIdentifier(context.getResources().getResourceEntryName(attrValueRefId),
                                         dir,
                                         mPackageName);
        if (drawableid == 0) {
            drawable = ContextCompat.getDrawable(context, attrValueRefId);
        } else {
            if (Build.VERSION.SDK_INT < 22) {
                drawable = mResources.getDrawable(drawableid);
            } else {
                drawable = mResources.getDrawable(drawableid, null);
            }
        }
        return drawable;
    }

    /**
     * @param newSkinRootPath
     */
    public void setSkinRootPath(String newSkinRootPath) {
        File file = new File(newSkinRootPath);
        file.mkdirs();
        SpUtils.setSkinRootPath(context, newSkinRootPath);
    }


    private int getFirstIndex() {
        int firstIndex = 1;
        if (SkinConfig.Density < 1) {
            //ldpi
            firstIndex = 0;
        } else if (SkinConfig.Density < 1.5) {
            //mdpi
            firstIndex = 1;
        } else if (SkinConfig.Density < 2) {
            //hdpi
            firstIndex = 3;
        } else if (SkinConfig.Density < 3) {
            //xhdpi
            firstIndex = 4;
        } else if (SkinConfig.Density < 4) {
            //xxhdpi
            firstIndex = 5;
        }
        return firstIndex;
    }

    private void pasFileIndex(File fileDir) {
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                pasFileIndex(file);
            } else {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
                    LinkedHashMap<String, String> strings = skinData.get(fileName);
                    if (strings != null) {
                        strings.put(file.getParentFile().getName(), file.getName());
                    } else {
                        LinkedHashMap<String, String> strings1 = new LinkedHashMap<>();
                        strings1.put(file.getParentFile().getName(), file.getName());
                        skinData.put(fileName, strings1);
                    }
                }
            }
        }
    }

    private String getPath(String imageName, boolean isRN) {
        if (SkinManager.getInstance().skinPathRes == null || SkinManager.getInstance().isDefaultSkin) {
            return imageName;
        }
        LinkedHashMap<String, String> skinImgRes = skinData.get(imageName);
        if (skinImgRes == null) {
            return imageName;
        } else {
            String indexFirst = getBestIndex(skinImgRes);
            String s = skinImgRes.get(indexFirst);
            if (TextUtils.isEmpty(s)) {
                return imageName;
            }
            if (isRN) {
                return "file://" +
                       SkinManager.getInstance().skinPathRes + indexFirst + "/" + s;
            } else {
                return SkinManager.getInstance().skinPathRes + indexFirst + "/" + s;
            }
        }
    }

    private String getBestIndex(LinkedHashMap<String, String> densityMap) {
        String[] index = new String[7];
        Iterator<String> iterator = densityMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.contains("-ldpi")) {
                index[0] = next;
            } else if (next.equals("drawable") || next.equals("mipmap")) {
                index[1] = next;
            } else if (next.contains("-mdpi")) {
                index[2] = next;
            } else if (next.contains("-hdpi")) {
                index[3] = next;
            } else if (next.contains("-xhdpi")) {
                index[4] = next;
            } else if (next.contains("-xxhdpi")) {
                index[5] = next;
            } else if (next.contains("-xxxhdpi")) {
                index[6] = next;
            }
        }
        String keyForindex = getIndexForHight(index, SkinConfig.FirstIndex);
        if (TextUtils.isEmpty(keyForindex)) {
            keyForindex = getIndexForLow(index, SkinConfig.FirstIndex - 1);
        }
        return keyForindex;
    }

    private String getIndexForLow(String[] index, int firstIndex) {
        if (firstIndex < 0) return null;
        String str0 = index[firstIndex];
        if (!TextUtils.isEmpty(str0)) {
            return str0;
        } else {
            return getIndexForLow(index, --firstIndex);
        }
    }

    private String getIndexForHight(String[] index, int firstIndex) {
        if (firstIndex >= 7) return null;
        String str0 = index[firstIndex];
        if (!TextUtils.isEmpty(str0)) {
            return str0;
        } else {
            return getIndexForHight(index, ++firstIndex);
        }
    }


    public void print() {
        Iterator<Map.Entry<String, LinkedHashMap<String, String>>> iterator = skinData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LinkedHashMap<String, String>> next = iterator.next();
            LinkedHashMap<String, String> value1 = next.getValue();
            Iterator<Map.Entry<String, String>> iterator2 = value1.entrySet().iterator();
            String value = " value :";
            while (iterator2.hasNext()) {
                Map.Entry<String, String> next1 = iterator2.next();
                value = value + " getKey:" + next1.getKey() + " getValue:" + next1.getValue();
            }
            LogUtils.e(TAG, next.getKey() + value);
        }
    }
}
