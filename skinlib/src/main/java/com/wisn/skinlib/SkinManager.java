package com.wisn.skinlib;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.wisn.skinlib.font.TypeFaceUtils;
import com.wisn.skinlib.interfaces.ISkinUpdate;
import com.wisn.skinlib.interfaces.IWatchObserver;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinManager implements IWatchObserver {

    private Context context;
    private List<ISkinUpdate> mSkinObservers;
    private boolean mNightMode;

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
        if(SpUtils.isDefaultSkin(context)){
            return ;
        }
        String customSkinName = SpUtils.getCustomSkinName(context);
        loadSkin(customSkinName,listener);
    }

    public void loadSkin() {
        loadSkin(null);
    }

    public void loadSkin(String skinName, final SkinLoaderListener listener) {
        new AsyncTask<String,Void,Resources>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(listener!=null){
                    listener.start();
                }
            }

            @Override
            protected Resources doInBackground(String... strings) {
                if(strings!=null&&strings.length==1){

                }
                return null;
            }

            @Override
            protected void onPostExecute(Resources resources) {
                super.onPostExecute(resources);
            }
        }.execute(skinName);
    }
}
