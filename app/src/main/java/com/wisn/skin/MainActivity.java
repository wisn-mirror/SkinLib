package com.wisn.skin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wisn.skin.view.DrawableTopAttr;
import com.wisn.skin.view.MyRadioButton;
import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinFragmentActivity;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.utils.LogUtils;


public class MainActivity extends SkinFragmentActivity implements View.OnClickListener, SkinLoaderListener {
    private static final String TAG = "MainActivity";
    private Button mChangeSkin, resetSkin, getcolor,getSkinPath,changeFont;

    private MyRadioButton mRadiobutton_bg_home;
    private MyRadioButton mRadiobutton_bg_gift;
    private MyRadioButton mRadiobutton_bg_start;
    private MyRadioButton mRadiobutton_bg_watch;
    private LinearLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChangeSkin = (Button) findViewById(R.id.changeSkin);
        getcolor = (Button) findViewById(R.id.getcolor);
        resetSkin = (Button) findViewById(R.id.resetSkin);
        getSkinPath = (Button) findViewById(R.id.getSkinPath);
        changeFont = (Button) findViewById(R.id.changeFont);
        mContentView = (LinearLayout) findViewById(R.id.contentView);
        mRadiobutton_bg_home = (MyRadioButton) findViewById(R.id.radiobutton_bg_home);
        mRadiobutton_bg_gift = (MyRadioButton) findViewById(R.id.radiobutton_bg_gift);
        mRadiobutton_bg_start = (MyRadioButton) findViewById(R.id.radiobutton_bg_start);
        mRadiobutton_bg_watch = (MyRadioButton) findViewById(R.id.radiobutton_bg_watch);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setPressed(true);
        mChangeSkin.setOnClickListener(this);
        resetSkin.setOnClickListener(this);
        getcolor.setOnClickListener(this);
        getSkinPath.setOnClickListener(this);
        changeFont.setOnClickListener(this);
//        startActivity(new Intent(this,TestAppCompatActivity.class));
        final float scale = this.getResources().getDisplayMetrics().density;
        Button button=new Button(this);
        CheckBox checkBox=new CheckBox(this);
        LogUtils.e(TAG, "aaa:" + scale+(button instanceof TextView));
        LogUtils.e(TAG, "checkBox:" + scale+(checkBox instanceof TextView));
//        return (int) (dpValue * scale + 0.5f);@drawable/radiobutton_bg_home
        addCustomeView(mRadiobutton_bg_home,R.drawable.radiobutton_bg_home);
        addCustomeView(mRadiobutton_bg_gift,R.drawable.radiobutton_bg_gift);
        addCustomeView(mRadiobutton_bg_start,R.drawable.radiobutton_bg_start);
        addCustomeView(mRadiobutton_bg_watch,R.drawable.radiobutton_bg_watch);
        addNewObjectView();
    }

    long sum = 0;
    int count = 0;

    @Override
    public void onClick(View v) {
        if (v == mChangeSkin) {
            SkinManager.getInstance().loadSkin("theme-com.wisn.skin2--56-1.0-2017-09-25--09-26-17.skin"
                                               ,true,this);
            Log.e(TAG, "printprintprint--------------------------------------------------");
        } else if (v == resetSkin) {
            long start = System.currentTimeMillis();
            String path = SkinManager.getInstance().getPath("aaaaa");
          /*  for (int i = 0; i < 800; i++) {
                Log.e(TAG," "+SkinManager.getInstance().getPath("gift_0"));
                Log.e(TAG," "+SkinManager.getInstance().getPath("gift_1"));
                Log.e(TAG," "+SkinManager.getInstance().getPath("ic_launcher_round"));
                Log.e(TAG," "+SkinManager.getInstance().getPath("gift_1"));
                SkinManager.getInstance().getColorForRN("colorPrimaryDark");
                SkinManager.getInstance().getColorForRN("colorAccent");
                SkinManager.getInstance().getColorForRN("colorAccent");
                SkinManager.getInstance().getColorForRN("primary");
            }
            */
            long end = System.currentTimeMillis() - start;
            count = count + 800 * 4;
            sum = sum + end;
            Log.e(TAG, end + ":" + path);
            SkinManager.getInstance().resetDefaultSkin();
        } else if (v == getcolor) {
            Log.e(TAG, " " + SkinManager.getInstance().getColorForRN("colorPrimary"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorPrimaryDark"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("colorAccent"));
//            Log.e(TAG," "+SkinManager.getInstance().getColorForRN("primary"));
            Log.e(TAG, "count:" + count + "sum:" + sum + " v:" + ((double) sum) / ((double) count));
            startActivity(new Intent(this, TestActivity.class));
        }else if(v==getSkinPath){
            startActivity(new Intent(this, TestAppCompatActivity.class));
        }else if(v==changeFont){
            LogUtils.e(TAG,"changeFont:"+SkinManager.getInstance().saveFont(Environment.getExternalStorageDirectory()+"/dd/font.ttf","aaaa.ttf"));
            SkinManager.getInstance().loadFont("aaaa.ttf",this);
            SkinManager.getInstance().nightMode();
        }
    }
    public void addNewObjectView(){
        for(int i=0;i<5;i++){
            TextView textView=new TextView(this);
            textView.setText("test"+i);
            mContentView.addView(textView);
            dynamicAddView(textView,"textColor",R.color.colorAccent);
        }
    }

    /**
     *
     * @param view  控件ID
     * @param attrValueresId  控件资源ID
     */
    public void addCustomeView(View view,int attrValueresId){
        DrawableTopAttr  drawableTopAttr=new DrawableTopAttr();
        drawableTopAttr.setRes("drawableTop",attrValueresId);
        dynamicAddView(view,drawableTopAttr);
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
