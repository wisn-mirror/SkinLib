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
import com.wisn.skinlib.interfaces.ISkinUpdateObserver;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.interfaces.SubObserver;
import com.wisn.skinlib.utils.ColorUtils;
import com.wisn.skinlib.utils.DBUtils;
import com.wisn.skinlib.utils.LogUtils;
import com.wisn.skinlib.utils.SkinFileUitls;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinManager implements SubObserver {
    public static SkinManager skinManager;
    private LinkedHashMap<String, LinkedHashMap<String, String>> mSkinResDataIndex = new LinkedHashMap<>();
    private HashMap<String, String> sColorNameMap = new HashMap<>();
    private HashMap<String, String> sImageNameMap = new HashMap<>();
    private List<ISkinUpdateObserver> mSkinObservers;
    private boolean isNightMode = false;
    public boolean isDefaultSkin = true;
    private Resources mResources;
    private Typeface mTypeface;
    private String mPackageName;
    public Context mContext;
    public String mSkinPath;
    public String mSkinPathRes;

    private SkinManager() {}

    public static SkinManager getInstance() {
        if (skinManager == null) {
            synchronized (SkinManager.class) {
                if (skinManager == null) {
                    skinManager = new SkinManager();
                }
            }
        }
        return skinManager;
    }

    /**
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx.getApplicationContext();
        SkinConfig.Density = mContext.getResources().getDisplayMetrics().density;
        SkinConfig.FirstIndex = getFirstIndex();
        String fontName = DBUtils.getFontName(mContext);
        if (!SkinConfig.SP_Font_Path.equals(fontName)) {
            loadFont(fontName, null);
        }
        if (DBUtils.isNightMode(mContext)) {
            SkinManager.getInstance().nightMode();
        } else {
            SkinManager.getInstance().loadSkin(null);
        }
    }

    /**
     * @param imageName
     *
     * @return
     */
    public String getPathForRN(String imageName) {
        return getPath(imageName, true);
    }

    /**
     * @param imageName
     *
     * @return
     */
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
        SkinFileUitls.updateSkinPath(mContext, newSkinRootPath, skinLoaderListener);
    }

    /**
     * @return
     */
    public Typeface getTypeFace() {
        return mTypeface;
    }

    /**
     * @param listener
     */
    public void loadSkin(SkinLoaderListener listener) {
        if (DBUtils.isDefaultSkin(mContext)) {
            mSkinPath = null;
            mSkinPathRes = null;
            return;
        } else {
            String customSkinName = DBUtils.getCustomSkinName(mContext);
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
        return (SkinFileUitls.saveSkinFile(mContext, skinFilePath, skinName) &&
                SkinFileUitls.upZipSkin(mContext, skinFilePath, skinName));
    }

    /**
     * 保存字体
     *
     * @param fontPath
     * @param fontName
     *
     * @return
     */
    public boolean saveFont(String fontPath, String fontName) {
        List<String> fontListName = getFontListName(false);
        if (fontListName != null &&
            fontListName.contains(fontName)) return true;
        return SkinFileUitls.saveFontFile(mContext, fontPath, fontName);
    }

    /**
     * 设置默认字体
     */
    public void resetDefaultFont() {
        mTypeface = Typeface.DEFAULT;
        notifyFontUpdate(mTypeface);
    }

    /**
     * @param fontName
     * @param listener
     */
    public void loadFont(final String fontName, final SkinLoaderListener listener) {
        if (fontName == null) {
            if (listener != null) {
                listener.onFailed("fontName is null ");
            }
        }
        new AsyncTask<String, Void, Typeface>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (listener != null) {
                    listener.start();
                }
            }

            @Override
            protected Typeface doInBackground(String... strings) {
                Typeface typeface = null;
                try {
                    if (strings != null && strings.length == 1 && strings[0] != null) {
                        String fontPath =
                                SkinFileUitls.getSkinFontPath(mContext) +
                                File.separator +
                                strings[0];
                        typeface = Typeface.createFromFile(fontPath);
                        if (typeface != null) {
                            DBUtils.setCustomFontName(mContext, strings[0]);
                        }
                        return typeface;
                    }
                    return null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    return typeface;
                }
            }

            @Override
            protected void onPostExecute(Typeface typeface) {
                if (typeface != null) {
                    mTypeface = typeface;
                    notifyFontUpdate(typeface);
                    if (listener != null) {
                        listener.onSuccess();
                    }
                } else {
                    if (listener != null) {
                        listener.onFailed("typeface resource is null ");
                    }
                }
            }
        }.execute(fontName);

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
                Resources resource=null;
                try {
                    if (strings != null && strings.length == 1 && strings[0] != null) {
                        String skinPath =
                                SkinFileUitls.getSkinPath(mContext, false) +
                                File.separator +
                                strings[0];
                        String skinPathRes =
                                SkinFileUitls.getSkinPath(mContext, true) +
                                File.separator +
                                strings[0] + "/res/";
                        File skinFile = new File(skinPath);
                        if (!skinFile.exists()) {
                            LogUtils.e("loadSkin", "skinFile not exists");
                            return null;
                        }
                        PackageManager packageManager = mContext.getPackageManager();
                        PackageInfo
                                packageArchiveInfo =
                                packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                        if (packageArchiveInfo == null) {
                            LogUtils.e("loadSkin", "packageArchiveInfo is null");
                            return null;
                        }
                        mPackageName = packageArchiveInfo.packageName;
                        AssetManager assetManager = AssetManager.class.newInstance();
                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, skinPath);
                        Resources superRes = mContext.getResources();
                        resource= new Resources(assetManager,
                                              superRes.getDisplayMetrics(),
                                              superRes.getConfiguration());
                        SkinManager.this.mSkinPath = skinPath;
                        SkinManager.this.mSkinPathRes = skinPathRes;
                        loadSkinFileForRN(skinPathRes);
                        DBUtils.setCustomSkinName(mContext, strings[0]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    return resource;
                }
            }

            @Override
            protected void onPostExecute(Resources resources) {
                if (resources != null) {
                    mResources = resources;
                    sColorNameMap.clear();
                    sImageNameMap.clear();
                    isDefaultSkin = false;
                    isNightMode = false;
                    DBUtils.setNightMode(mContext, false);
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

    /**
     * @param skinPathRes
     */
    public void loadSkinFileForRN(String skinPathRes) {
        File file = new File(skinPathRes);
        if (file.exists() && file.isDirectory()) {
            if (mSkinResDataIndex != null) {
                mSkinResDataIndex.clear();
                pasFileIndex(file);
            }
        }
    }

    /**
     * @return
     */
    public boolean isExternalSkin() {
        return mResources != null && !isDefaultSkin;
    }

    /**
     * @return
     */
    public boolean isNightMode() {
        return isNightMode;
    }

    /**
     *
     */
    public void nightMode() {
        resetDefaultSkin();
        isNightMode = true;
        DBUtils.setNightMode(mContext, true);
        notifySkinUpdate();
    }

    /**
     * @return
     */
    public String getCurrentThemName() {
        if (DBUtils.isDefaultSkin(mContext)) {
            return null;
        } else {
            return DBUtils.getCustomSkinName(mContext);
        }
    }

    /**
     * @param isRes
     * @param isPath
     *
     * @return
     */
    public List<String> getSkinListName(boolean isRes, boolean isPath) {
        return SkinFileUitls.getSkinListName(mContext, isRes, isPath);
    }

    /**
     * @param isPath
     *
     * @return
     */
    public List<String> getFontListName(boolean isPath) {
        return SkinFileUitls.getFontListName(mContext, isPath);
    }


    /**
     *
     */
    public void resetDefaultSkin() {
        isDefaultSkin = true;
        isNightMode = false;
        mSkinPath = null;
        mSkinPathRes = null;
        sColorNameMap.clear();
        sImageNameMap.clear();
        DBUtils.setNightMode(mContext, false);
        DBUtils.setDefaultSkin(mContext);
        notifySkinUpdate();
    }


    /**
     * @param observer
     */
    @Override
    public void attach(ISkinUpdateObserver observer) {
        if (mSkinObservers == null) {
            mSkinObservers = new ArrayList<>();
        }
        mSkinObservers.add(observer);
    }

    /**
     * @param observer
     */
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
            color = ContextCompat.getColor(mContext, resId);
            return color;
        }
        int colorResId =
                mResources.getIdentifier(mContext.getResources().getResourceEntryName(resId),
                                         "color",
                                         mPackageName);
        if (colorResId == 0) {
            color = ContextCompat.getColor(mContext, resId);
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
    public synchronized String getColorForRN(String colorName) {
        String s = sColorNameMap.get(colorName);
        if (s != null) return s;
        int color = 0;
        int colorResId = 0;
        try {
            if (mResources == null || isDefaultSkin) {
                colorResId =
                        mContext.getResources().getIdentifier(colorName, "color", mContext.getPackageName());
                color = mContext.getResources().getColor(colorResId);
            } else {
                colorResId = mResources.getIdentifier(colorName, "color", mPackageName);
                color = mResources.getColor(colorResId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            colorResId = mContext.getResources().getIdentifier(colorName, "color", mContext.getPackageName());
            color = mContext.getResources().getColor(colorResId);
        } finally {
            s = ColorUtils.colorToRGB(color);
            sColorNameMap.put(colorName, s);
            return s;
        }
    }

    /**
     * @param attrValueRefId
     *
     * @return
     */
    public Drawable getDrawable(int attrValueRefId) {
        Drawable drawable = null;
        if (mResources == null || isDefaultSkin) {
            drawable = ContextCompat.getDrawable(mContext, attrValueRefId);
            return drawable;
        }
        int drawableid =
                mResources.getIdentifier(mContext.getResources().getResourceEntryName(attrValueRefId),
                                         "drawable",
                                         mPackageName);
        if (drawableid == 0) {
            drawableid =
                    mResources.getIdentifier(mContext.getResources().getResourceEntryName(attrValueRefId),
                                             "mipmap",
                                             mPackageName);
        }
        if (drawableid == 0) {
            drawable = ContextCompat.getDrawable(mContext, attrValueRefId);
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
     * @param attrValueRefId
     * @param dir            drawable/mipmap
     *
     * @return
     */
    public Drawable getDrawable(int attrValueRefId, String dir) {
        Drawable drawable = null;
        if (mResources == null || isDefaultSkin) {
            drawable = ContextCompat.getDrawable(mContext, attrValueRefId);
            return drawable;
        }
        int drawableid =
                mResources.getIdentifier(mContext.getResources().getResourceEntryName(attrValueRefId),
                                         dir,
                                         mPackageName);
        if (drawableid == 0) {
            drawable = ContextCompat.getDrawable(mContext, attrValueRefId);
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
    public boolean setSkinRootPath(String newSkinRootPath) {
        File file = new File(newSkinRootPath);
        file.mkdirs();
        if (!file.exists()) return false;
        DBUtils.setSkinRootPath(mContext, newSkinRootPath);
        return true;
    }


    /**
     * @return
     */
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

    /**
     * @param fileDir
     */
    private void pasFileIndex(File fileDir) {
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                pasFileIndex(file);
            } else {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
                    LinkedHashMap<String, String> strings = mSkinResDataIndex.get(fileName);
                    if (strings != null) {
                        strings.put(file.getParentFile().getName(), file.getName());
                    } else {
                        LinkedHashMap<String, String> strings1 = new LinkedHashMap<>();
                        strings1.put(file.getParentFile().getName(), file.getName());
                        mSkinResDataIndex.put(fileName, strings1);
                    }
                }
            }
        }
    }

    /**
     * @param imageName
     * @param isRN
     *
     * @return
     */
    private String getPath(String imageName, boolean isRN) {
        String path = sImageNameMap.get(imageName);
        if (path != null) return path;
        if (SkinManager.getInstance().mSkinPathRes == null || SkinManager.getInstance().isDefaultSkin) {
            return imageName;
        }
        LinkedHashMap<String, String> skinImgRes = mSkinResDataIndex.get(imageName);
        if (skinImgRes == null) {
            return imageName;
        } else {
            String indexFirst = getBestIndex(skinImgRes);
            String s = skinImgRes.get(indexFirst);
            if (TextUtils.isEmpty(s)) {
                return imageName;
            }
            if (isRN) {
                path = "file://" +
                       SkinManager.getInstance().mSkinPathRes + indexFirst + "/" + s;
            } else {
                path = SkinManager.getInstance().mSkinPathRes + indexFirst + "/" + s;
            }
        }
        sImageNameMap.put(imageName, path);
        return path;
    }

    /**
     * @param densityMap
     *
     * @return
     */
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

    /**
     * @param index
     * @param firstIndex
     *
     * @return
     */
    private String getIndexForLow(String[] index, int firstIndex) {
        if (firstIndex < 0) return null;
        String str0 = index[firstIndex];
        if (!TextUtils.isEmpty(str0)) {
            return str0;
        } else {
            return getIndexForLow(index, --firstIndex);
        }
    }

    /**
     * @param index
     * @param firstIndex
     *
     * @return
     */
    private String getIndexForHight(String[] index, int firstIndex) {
        if (firstIndex >= 7) return null;
        String str0 = index[firstIndex];
        if (!TextUtils.isEmpty(str0)) {
            return str0;
        } else {
            return getIndexForHight(index, ++firstIndex);
        }
    }
}
