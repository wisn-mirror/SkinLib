package com.wisn.skin;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinFragmentActivity;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.interfaces.SkinPathChangeLister;
import com.wisn.skinlib.loader.SkinResourceCompat;
import com.wisn.skinlib.utils.ColorUtils;
import com.wisn.skinlib.utils.LogUtils;

import java.io.File;

public class MainActivity extends SkinFragmentActivity implements View.OnClickListener, SkinLoaderListener {
    private static final String TAG="MainActivity";
    private Button mChangeSkin,resetSkin,getcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChangeSkin = (Button) findViewById(R.id.changeSkin);
        getcolor = (Button) findViewById(R.id.getcolor);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setPressed(true);
        resetSkin = (Button) findViewById(R.id.resetSkin);
        mChangeSkin.setOnClickListener(this);
        resetSkin.setOnClickListener(this);
        getcolor.setOnClickListener(this);
//        startActivity(new Intent(this,TestAppCompatActivity.class));
        final float scale = this.getResources().getDisplayMetrics().density;
        LogUtils.e(TAG,"aaa:"+scale);
//        return (int) (dpValue * scale + 0.5f);


    }

    @Override
    public void onClick(View v) {
        if(v==mChangeSkin){
            SkinManager.getInstance().loadSkin("theme-com.wisn.skin1--30-1.0-2017-09-18-09-16-10.skin",
                                               this);
            Log.e(TAG,"printprintprint--------------------------------------------------");
            Log.e(TAG," "+ColorUtils.colorToARGB(ContextCompat.getColor(this, R.color.colorPrimary)));
            Log.e(TAG," "+ColorUtils.colorToARGB(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            Log.e(TAG," "+ColorUtils.colorToARGB(ContextCompat.getColor(this, R.color.colorAccent)));
            Log.e(TAG," "+ColorUtils.colorToARGB(ContextCompat.getColor(this, R.color.primary)));
            SkinResourceCompat.print();
        }else if(v==resetSkin){
//            SkinResourceCompat.print();
            long start=System.currentTimeMillis();
            String path = SkinResourceCompat.getPath("aaaaa");
            for(int i=0;i<10;i++){
                Log.e(TAG,  SkinResourceCompat.getPath("gift_0"));
                Log.e(TAG,SkinResourceCompat.getPath("gift_1"));
                Log.e(TAG,SkinResourceCompat.getPath("ic_launcher_round"));
            }
            Log.e(TAG,(System.currentTimeMillis()-start)+":"+path);
            SkinManager.getInstance().resetDefaultThem();
        }else if(v==getcolor){
            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorPrimary"));
            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorPrimaryDark"));
            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorAccent"));
            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("primary"));

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

    public void updateSkinPath(){
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
    public void onFailed(String error) {
        LogUtils.e(TAG,"onFailed"+error);
    }

    @Override
    public void onProgress(int progress, int sum) {

    }
}
