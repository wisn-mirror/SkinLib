package com.wisn.skin;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinFragmentActivity;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.interfaces.SkinPathChangeLister;
import com.wisn.skinlib.loader.SkinResourceCompat;
import com.wisn.skinlib.utils.LogUtils;

import java.io.File;

public class MainActivity extends SkinFragmentActivity implements View.OnClickListener, SkinLoaderListener {
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
//        startActivity(new Intent(this,TestAppCompatActivity.class));
        final float scale = this.getResources().getDisplayMetrics().density;
        LogUtils.e(TAG,"aaa:"+scale);
//        return (int) (dpValue * scale + 0.5f);
        new Thread(new Runnable() {
            @Override
            public void run() {
             /*   SkinManager.getInstance().updateSkinPath(Environment.getExternalStorageDirectory() +
                                                         File.separator +
                                                         "dd",
                                                         new SkinPathChangeLister() {
                                                             @Override
                                                             public void start() {
                                                                 LogUtils.e(TAG,"SkinPathChangeLister:start:");
                                                             }

                                                             @Override
                                                             public void progress(int current, int progress) {
                                                                 LogUtils.e(TAG,"SkinPathChangeLister:progress:current:"+current+" progress"+progress);
                                                             }

                                                             @Override
                                                             public void finish() {
                                                                 LogUtils.e(TAG,"SkinPathChangeLister:finish:");
                                                             }
                                                         });*/
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        if(v==mChangeSkin){
            SkinManager.getInstance().loadSkin("theme-com.wisn.skin1--28-1.0-2017-09-16-10-24-16.skin",
                                               this);
            SkinResourceCompat.print();
        }else if(v==resetSkin){
//            SkinResourceCompat.print();
            long start=System.currentTimeMillis();
            String path = SkinResourceCompat.getPath("aaaaa");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_half_black_36dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            SkinResourceCompat.getPath("abc_ic_star_black_16dp");
            SkinResourceCompat.getPath("abc_ic_menu_copy_mtrl_am_alpha");
            Log.e(TAG,(System.currentTimeMillis()-start)+":"+path);
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
