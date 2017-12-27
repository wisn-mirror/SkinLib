package com.wisn.skinlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.interfaces.ISkinUpdateObserver;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.interfaces.SubObserver;
import com.wisn.skinlib.task.SkinThreadPool;
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
    public static final String  TAG="SkinManager";
    public static SkinManager skinManager;
    private LinkedHashMap<String, LinkedHashMap<String, String>> mSkinResDataIndex;
    private HashMap<String, String> sColorNameMap;
    private HashMap<String, String> sImageNameMap;
    private List<ISkinUpdateObserver> mSkinObservers;
    private boolean isNightMode = false;
    public boolean isDefaultSkin = true;
    private Resources mResources;
    private Typeface mTypeface;
    private String mPackageName;
    public Context mContext;
    public String mSkinPath;
    public String mSkinPathRes;
    public boolean isSupplyRN;
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
     * 设置皮肤的根目录
     * @param ctx
     * @param rootPath 皮肤根路径 如果指定的路径不存在，会使用默认的路径
     * @param isSupplyRN  是否支持RN换肤
     */
    public void init(Context ctx, String rootPath,boolean isSupplyRN) {
        if(isSupplyRN){
           mSkinResDataIndex = new LinkedHashMap<>();
           sColorNameMap = new HashMap<>();
           sImageNameMap = new HashMap<>();
        }
        this.isSupplyRN=isSupplyRN;
        mContext = ctx.getApplicationContext();
        SkinConfig.Density = mContext.getResources().getDisplayMetrics().density;
        SkinConfig.FirstIndex = getFirstIndex();
        if (rootPath != null) setSkinRootPath(rootPath);
        String fontName = DBUtils.getCustomFontName(mContext);
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
     * 删除皮肤文件
     * @param isClearFont 是否删除字体文件
     */
    public void clearSkin(final boolean isClearFont){
        resetDefaultSkin();
        if(isClearFont)resetDefaultFont();
        SkinThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                SkinFileUitls.deleteSkinFile(mContext,isClearFont);
            }
        });
    }

    /**
     * 切换皮肤根路径
     * @param newSkinRootPath 新的皮肤根路径
     * @param skinLoaderListener  切换监听
     */
    public void updateSkinPath(String newSkinRootPath, SkinLoaderListener skinLoaderListener) {
        SkinFileUitls.updateSkinPath(mContext, newSkinRootPath, skinLoaderListener);
    }

    /**
     * 获取当前字体
     * @return
     */
    public Typeface getTypeFace() {
        return mTypeface;
    }


    /**
     * 保存皮肤
     * @param skinFilePath
     * @param skinName 指定皮肤的名称
     *
     * @return
     */
    public boolean saveSkin(String skinFilePath,
                            String skinName) {
        List<String> skinListName = getSkinListName(false, false);
        if(!isSupplyRN){
            if (skinListName != null &&
                skinListName.contains(skinName)) return true;
            return SkinFileUitls.saveSkinFile(mContext, skinFilePath, skinName);
        }else{
            List<String> skinResListName = getSkinListName(true, false);
            if (skinListName != null &&
                skinListName.contains(skinName) &&
                skinResListName != null &&
                skinResListName.contains(skinName)) return true;
            return (SkinFileUitls.saveSkinFile(mContext, skinFilePath, skinName) &&
                    SkinFileUitls.upZipSkin(mContext, skinFilePath, skinName));
        }
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
     *  指定切换字体
     * @param fontName
     * @param listener
     */
    @SuppressLint("StaticFieldLeak")
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
     * 程序加载时切换皮肤
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
     * 加载指定的皮肤
     * @param skinName
     * @param listener
     */
    public void loadSkin(String skinName, final SkinLoaderListener listener) {
        loadSkin(skinName, false, listener);
    }

    /**
     * 加载指定的皮肤 并指定是否时夜间模式
     * @param skinName
     * @param isNight  是否是夜间模式
     * @param listener
     */
    @SuppressLint("StaticFieldLeak")
    public void loadSkin(final String skinName, final boolean isNight, final SkinLoaderListener listener) {
        if (skinName == null) {
            LogUtils.e("loadSkin", "skinName is null");
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
                Resources resource = null;
                try {
                    if (strings != null && strings.length == 1 && strings[0] != null) {
                        String skinPath =
                                SkinFileUitls.getSkinPath(mContext, false) +
                                File.separator +
                                strings[0];
                        File skinFile = new File(skinPath);
                        if (!skinFile.exists()) {
                            LogUtils.e("loadSkin", skinPath+"skinFile not exists");
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
                        resource = new Resources(assetManager,
                                                 superRes.getDisplayMetrics(),
                                                 superRes.getConfiguration());
                        SkinManager.this.mSkinPath = skinPath;
                        if(isSupplyRN){
                            String skinPathRes =
                                    SkinFileUitls.getSkinPath(mContext, true) +
                                    File.separator +
                                    strings[0] + "/res/";
                            SkinManager.this.mSkinPathRes = skinPathRes;
                            loadSkinFileForRN(skinPathRes);
                        }
                        DBUtils.setCustomSkinName(mContext, strings[0]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    return resource;
                }
            }

            @Override
            protected void onPostExecute(Resources resources) {
                if (resources != null) {
                    mResources = resources;
                    if(sColorNameMap!=null&&sImageNameMap!=null){
                        sColorNameMap.clear();
                        sImageNameMap.clear();
                    }
                    //在成功后这个值才生效
                    isNightMode = isNight;
                    isDefaultSkin = false;
                    DBUtils.setNightMode(mContext, isNightMode);
                    if (isNight)DBUtils.setNightName(mContext, skinName);
                    if (listener != null) listener.onSuccess();
                    notifySkinUpdate();
                } else {
                    isDefaultSkin = true;
                    if (listener != null) listener.onFailed(" Resource is null ");
                }
            }
        }.execute(skinName);
    }

    /**
     * 加载皮肤文件
     * @param skinPathRes
     */
    public void loadSkinFileForRN(String skinPathRes) {
        if(!isSupplyRN)return ;
        File file = new File(skinPathRes);
        if (file.exists() && file.isDirectory()) {
            if (mSkinResDataIndex != null) {
                mSkinResDataIndex.clear();
                pasFileIndex(file);
            }
        }
    }

    /**
     * 是否加载过皮肤  true 正加载了皮肤，false 没有加载皮肤
     * @return
     */
    public boolean isExternalSkin() {
        return mResources != null && !isDefaultSkin;
    }

    /**
     * 是否是夜间模式
     * @return
     */
    public boolean isNightMode() {
        return isNightMode;
    }

    /**
     * 启动夜间模式
     */
    public void nightMode() {
        loadSkin(DBUtils.getNightName(mContext), true, null);
    }

    /**
     * 获取当前皮肤的名称
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
     * 获取皮肤列表
     * @param isRes 是否是res（RN）目录
     * @param isPath 是否返回绝对路径
     *
     * @return
     */
    public List<String> getSkinListName(boolean isRes, boolean isPath) {
        return SkinFileUitls.getSkinListName(mContext, isRes, isPath);
    }

    /**
     * 获取字体列表
     * @param isPath 是否返回绝对路径
     *
     * @return
     */
    public List<String> getFontListName(boolean isPath) {
        return SkinFileUitls.getFontListName(mContext, isPath);
    }


    /**
     * 重置默认皮肤
     */
    public void resetDefaultSkin() {
        isDefaultSkin = true;
        isNightMode = false;
        mSkinPath = null;
        mSkinPathRes = null;
        if(sColorNameMap!=null&&sImageNameMap!=null){
            sColorNameMap.clear();
            sImageNameMap.clear();
        }
        DBUtils.setNightMode(mContext, false);
        notifySkinUpdate();
    }


    /**
     * 添加页面皮肤的被观察者
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
     * 取消页面皮肤的被观察者
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
     * 通知皮肤切换
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
     * 通知字体切换
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
     * 获取颜色值
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
        if(!this.isSupplyRN)return colorName;
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
     * 获取drawable
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
     * 指定drawable/mipmap 目录获取drawable
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
     * 设置皮肤目录的根路径
     * @param newSkinRootPath
     */
    public boolean setSkinRootPath(String newSkinRootPath) {
        File file = new File(newSkinRootPath);
        file.mkdirs();
        if (!file.exists()){
            LogUtils.e(TAG,newSkinRootPath +" is not exist");
            return false;
        }
        DBUtils.setSkinRootPath(mContext, newSkinRootPath);
        return true;
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
     *
     * @param imageName
     *
     * @return
     */
    public String getPath(String imageName) {
        return getPath(imageName, false);
    }

    /**
     * @param imageName
     * @param isRN
     * @hide
     * @return
     */
    private String getPath(String imageName, boolean isRN) {
        if(!isSupplyRN)return imageName;
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
     *
     * @return
     * @hide
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
     * @hide
     */
    private void pasFileIndex(File fileDir) {
        if(!isSupplyRN)return ;
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
     * @param densityMap
     * @hide
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
     * @hide
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
     * @hide
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
