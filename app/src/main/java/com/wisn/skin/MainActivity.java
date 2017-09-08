package com.wisn.skin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinActivity;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.utils.LogUtils;

public class MainActivity extends SkinActivity implements View.OnClickListener,SkinLoaderListener {
    private static final String TAG="MainActivity";
    private Button mChangeSkin,resetSkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChangeSkin = (Button) findViewById(R.id.changeSkin);
        resetSkin = (Button) findViewById(R.id.resetSkin);
        mChangeSkin.setOnClickListener(this);
        resetSkin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mChangeSkin){
            SkinManager.getInstance().loadSkin("theme-com.wisn.skin1--15-1.0-2017-09-07-10-06-20.skin",
                                               this);
        }else if(v==resetSkin){
            SkinManager.getInstance().resetDefaultThem();
        }
    }

    @Override
    public void start() {
        LogUtils.e(TAG,"start");
    }

    @Override
    public void onSuccess() {
        LogUtils.e(TAG,"onSuccess");

    }

    @Override
    public void onFailed(String error) {
        LogUtils.e(TAG,"onFailed"+error);
    }

    @Override
    public void onProgress(int progress, int sum) {

    }
}
