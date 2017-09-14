package com.wisn.skin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinAppCompatActivity;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.utils.LogUtils;

public class TestAppCompatActivity extends SkinAppCompatActivity implements View.OnClickListener, SkinLoaderListener {
    private static final String TAG="TestActivity";
    private Button mChangeSkin,resetSkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testappcomp);
        mChangeSkin = (Button) findViewById(R.id.changeSkin);
        resetSkin = (Button) findViewById(R.id.resetSkin);
        mChangeSkin.setOnClickListener(this);
        resetSkin.setOnClickListener(this);
        startActivity(new Intent(this, TestActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v == mChangeSkin) {
            SkinManager.getInstance().loadSkin("theme-com.wisn.skin1--16-1.0-2017-09-08-09-55-06.skin",
                                               this);
        } else if (v == resetSkin) {
            SkinManager.getInstance().resetDefaultThem();
        }
    }

    @Override
    public void start() {
        LogUtils.e(TAG, "start");
    }

    @Override
    public void onSuccess() {
        LogUtils.e(TAG, "onSuccess");

    }

    @Override
    public void onFailed(String error) {
        LogUtils.e(TAG, "onFailed" + error);
    }

    @Override
    public void onProgress(int progress, int sum) {

    }
}