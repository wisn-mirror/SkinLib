package com.wisn.skinlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.wisn.skinlib.font.TypeFaceUtils;
import com.wisn.skinlib.interfaces.ISkinUpdate;
import com.wisn.skinlib.interfaces.IWatchObserver;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.loader.ResourceCompat;
import com.wisn.skinlib.utils.FileUitls;
import com.wisn.skinlib.utils.SpUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinManager implements IWatchObserver {

    private Context context;
    private List<ISkinUpdate> mSkinObservers;
    private boolean mNightMode = false;
    private boolean isDefaultSkin = true;
    private String skinPath;
    private Resources mResources;
    private String mPackageName;

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

    public boolean isNightMode() {
        return mNightMode;
    }

    public void init(Context ctx) {
        context = ctx.getApplicationContext();
        TypeFaceUtils.getTypeFace(context);
    }


    public void nightMode() {
        reSetDefaultThem();
        mNightMode = true;
        SpUtils.setNightMode(context, true);
        notifyUpdate();
    }

    private void reSetDefaultThem() {}

    @Override
    public void attach(ISkinUpdate observer) {
        if (mSkinObservers == null) {
            mSkinObservers = new ArrayList<>();
        }
        mSkinObservers.add(observer);
    }

    @Override
    public void detach(ISkinUpdate observer) {
        if (mSkinObservers == null) return;
        if (mSkinObservers.contains(observer)) {
            mSkinObservers.remove(observer);
        }
    }

    @Override
    public void notifyUpdate() {
        if (mSkinObservers == null) return;
        for (ISkinUpdate iSkinUpdate : mSkinObservers) {
            iSkinUpdate.onThemUpdate();
        }
    }

    public void loadSkin(SkinLoaderListener listener) {
        if (SpUtils.isDefaultSkin(context)) {
            return;
        }
        String customSkinName = SpUtils.getCustomSkinName(context);
        loadSkin(customSkinName, listener);
    }

    public void loadSkin() {
        loadSkin(null);
    }

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
                        String skinPath = FileUitls.getCacherDir(context) + File.separator + strings[0];
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

                        addAssetPath.invoke(assetManager, strings[0]);

                        Resources superRes = context.getResources();
                        Resources
                                resource =
                                ResourceCompat.getResource(assetManager,
                                                           superRes.getDisplayMetrics(),
                                                           superRes.getConfiguration());
                        SpUtils.setCustomSkinName(context, strings[0]);
                        SkinManager.this.skinPath = skinPath;
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
                    if (listener != null) listener.onSuccess();
                    notifyUpdate();
                } else {
                    isDefaultSkin = true;
                    if (listener != null) listener.onFailed(" Resource is null ");
                }
            }
        }.execute(skinName);
    }

    public int getColor(int resId) {
        return 0;
    }

    public Drawable getDrawable(int attrValueRefId) {
        return null;
    }
    public Drawable getDrawable(int attrValueRefId,String dir) {
        return null;
    }


}
