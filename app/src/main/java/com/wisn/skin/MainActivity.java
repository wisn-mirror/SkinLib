package com.wisn.skin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinFragmentActivity;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.utils.LogUtils;

public class MainActivity extends SkinFragmentActivity implements View.OnClickListener, SkinLoaderListener {
    private static final String TAG = "MainActivity";
    private Button mChangeSkin, resetSkin, getcolor,getSkinPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChangeSkin = (Button) findViewById(R.id.changeSkin);
        getcolor = (Button) findViewById(R.id.getcolor);
        resetSkin = (Button) findViewById(R.id.resetSkin);
        getSkinPath = (Button) findViewById(R.id.getSkinPath);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setPressed(true);
        mChangeSkin.setOnClickListener(this);
        resetSkin.setOnClickListener(this);
        getcolor.setOnClickListener(this);
        getSkinPath.setOnClickListener(this);
//        startActivity(new Intent(this,TestAppCompatActivity.class));
        final float scale = this.getResources().getDisplayMetrics().density;
        LogUtils.e(TAG, "aaa:" + scale);
//        return (int) (dpValue * scale + 0.5f);


    }

    long sum = 0;
    int count = 0;

    @Override
    public void onClick(View v) {
        if (v == mChangeSkin) {
            SkinManager.getInstance().loadSkin("theme-com.wisn.skin1--30-1.0-2017-09-18-09-16-10.skin",
                                               this);
            Log.e(TAG, "printprintprint--------------------------------------------------");
        } else if (v == resetSkin) {
            long start = System.currentTimeMillis();
            String path = SkinManager.getInstance().getPath("aaaaa");
            for (int i = 0; i < 200; i++) {
                SkinManager.getInstance().getPath("gift_0");
                SkinManager.getInstance().getPath("gift_1");
                SkinManager.getInstance().getPath("ic_launcher_round");
            }
            long end = System.currentTimeMillis() - start;
            count = count + 200 * 3;
            sum = sum + end;
            Log.e(TAG, end + ":" + path);
            SkinManager.getInstance().resetDefaultThem();
        } else if (v == getcolor) {
            Log.e(TAG, " " + SkinManager.getInstance().getColorForRN("colorPrimary"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorPrimaryDark"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorAccent"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("primary"));
            Log.e(TAG, "count:" + count + "sum:" + sum + " v:" + ((double) sum) / ((double) count));
            startActivity(new Intent(this, TestActivity.class));
        }else if(v==getSkinPath){
            startActivity(new Intent(this, TestAppCompatActivity.class));

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
