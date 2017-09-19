package com.wisn.skinlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;

import com.wisn.skinlib.base.SkinApplication;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.font.TypeFaceUtils;
import com.wisn.skinlib.interfaces.ISkinUpdateObserver;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.interfaces.SkinPathChangeLister;
import com.wisn.skinlib.interfaces.SubObserver;
import com.wisn.skinlib.loader.ResourceCompat;
import com.wisn.skinlib.loader.SkinResourceCompat;
import com.wisn.skinlib.utils.LogUtils;
import com.wisn.skinlib.utils.SkinFileUitls;
import com.wisn.skinlib.utils.SpUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinManager implements SubObserver {
    private static final String TAG = "SkinManager";
    private Context context;
    private List<ISkinUpdateObserver> mSkinObservers;
    private boolean mNightMode = false;
    public boolean isDefaultSkin = true;
    private Resources mResources;
    private String mPackageName;
    public String skinPath;
    public String skinPathRes;

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
    }

    /**
     * updateSkinPath
     *
     * @param newSkinRootPath
     * @param skinPathChangeLister
     */
    public void updateSkinPath(String newSkinRootPath, SkinPathChangeLister skinPathChangeLister) {
        SkinFileUitls.updateSkinPath(context, newSkinRootPath, skinPathChangeLister);
    }
    public void setSkinRootPath(String newSkinRootPath){
        File file=new File(newSkinRootPath);
        file.mkdirs();
        SpUtils.setSkinRootPath(context, newSkinRootPath);
    }

    /**
     *
     * @param listener
     */
    public void loadSkin(SkinLoaderListener listener) {
        if (SpUtils.isDefaultSkin(context)) {
            skinPath = null;
            skinPathRes = null;
            return;
        }
        if(!SpUtils.isDefaultSkin(context)){
            String customSkinName = SpUtils.getCustomSkinName(context);
            loadSkin(customSkinName, listener);
        }
    }


    /**
     * @param skinFilePath
     * @param skinName
     * @param listener
     * @param isLoadImmediately
     */
    public void saveSkin(String skinFilePath,
                         String skinName,
                         SkinLoaderListener listener,
                         boolean isLoadImmediately) {
        SkinFileUitls.saveSkinFile(context, skinFilePath, skinName);
        SkinFileUitls.upZipSkin(context, skinFilePath, skinName);
        if (isLoadImmediately) {
            loadSkin(skinName, listener);
        }
    }

    /**
     * @param skinName
     * @param listener
     */
    public void loadSkin(String skinName, final SkinLoaderListener listener) {
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
                        String
                                skinPath =
                                SkinFileUitls.getSkinPath(context, false) +
                                File.separator +
                                strings[0];
                        String
                                skinPathRes =
                                SkinFileUitls.getSkinPath(context, true) +
                                File.separator +
                                strings[0];
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
                                ResourceCompat.getResource(assetManager,
                                                           superRes.getDisplayMetrics(),
                                                           superRes.getConfiguration());
                        SkinResourceCompat.loadSkinFile(context,strings[0]);
                        SpUtils.setCustomSkinName(context, strings[0]);
                        SkinManager.this.skinPath = skinPath;
                        SkinManager.this.skinPathRes = skinPathRes;
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
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    SpUtils.setNightMode(context, false);
                                    if (listener != null) listener.onSuccess();
                                    notifyUpdate();
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
        notifyUpdate();
    }

    public void resetDefaultThem() {
        isDefaultSkin = true;
        mNightMode = false;
        skinPath = null;
        skinPathRes = null;
        SpUtils.setNightMode(context, false);
        SpUtils.setDefaultSkin(context);
        notifyUpdate();
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

    @Override
    public void notifyUpdate() {
        if (mSkinObservers == null) return;
        for (ISkinUpdateObserver iSkinUpdate : mSkinObservers) {
            iSkinUpdate.onThemUpdate();
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

}
