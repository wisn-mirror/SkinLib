package com.wisn.skin;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinFragmentActivity;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.utils.ColorUtils;
import com.wisn.skinlib.utils.LogUtils;

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

    long sum=0;
    int count =0;
    @Override
    public void onClick(View v) {
        if(v==mChangeSkin){
            SkinManager.getInstance().loadSkin("theme-com.wisn.skin1--30-1.0-2017-09-18-09-16-10.skin",
                                               this);
            Log.e(TAG,"printprintprint--------------------------------------------------");
//            Log.e(TAG," "+ColorUtils.colorToARGB(ContextCompat.getColor(this, R.color.colorPrimary)));
//            Log.e(TAG," "+ColorUtils.colorToARGB(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
//            Log.e(TAG," "+ColorUtils.colorToARGB(ContextCompat.getColor(this, R.color.colorAccent)));
//            Log.e(TAG," "+ColorUtils.colorToARGB(ContextCompat.getColor(this, R.color.primary)));
//            SkinResourceCompat.print();
        }else if(v==resetSkin){
//            SkinResourceCompat.print();
            long start=System.currentTimeMillis();
            String path = SkinManager.getInstance().getPath("aaaaa");
            for(int i=0;i<200;i++){
                SkinManager.getInstance().getPath("gift_0");
                SkinManager.getInstance().getPath("gift_1");
                SkinManager.getInstance().getPath("ic_launcher_round");
            }
           long end= System.currentTimeMillis()-start;
            count=count+200*3;
            sum=sum+end;
            Log.e(TAG,end+":"+path);

            SkinManager.getInstance().resetDefaultThem();
        }else if(v==getcolor){
            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorPrimary"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorPrimaryDark"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorAccent"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("primary"));
            Log.e(TAG,"count:"+count +"sum:"+sum+" v:"+((double)sum)/((double)count));
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
