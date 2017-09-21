package com.wisn.skin;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinAppCompatActivity;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.utils.LogUtils;
import com.wisn.skinlib.utils.SpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestAppCompatActivity extends SkinAppCompatActivity implements View.OnClickListener,
                                                                            SkinLoaderListener,
                                                                            AdapterView.OnItemClickListener {
    private static final String TAG = "TestAppCompatActivity";
    private Button mChangeSkin;
    private ListView mListView;
    private List<String> mSkinListName=new ArrayList<>();
    private BaseAdapter mAdapter;
    private EditText mSdcardpath;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testappcomp);
        mChangeSkin = (Button) findViewById(R.id.changeSkin);
        mSdcardpath = (EditText) findViewById(R.id.sdcardpath);
        mSdcardpath.setText(Environment.getExternalStorageDirectory().getAbsolutePath()+"/skinPath");
        mListView = (ListView) findViewById(R.id.listview);
        mChangeSkin.setOnClickListener(this);
        mSkinListName = SkinManager.getInstance().getSkinListName(true, false);
        if (mSkinListName != null) {
            mAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return mSkinListName.size();
                }

                @Override
                public Object getItem(int position) {
                    return mSkinListName.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = new TextView(TestAppCompatActivity.this);
                    textView.setText(mSkinListName.get(position));
                    return textView;
                }
            };
            mListView.setAdapter(mAdapter);
        }
    }

    public  List<String>  getSkinListFile(String sdCardPath) {
        File skinDir = new File(sdCardPath);
        if (skinDir == null || !skinDir.exists()) {
            return null;
        }
        List<String> skinListName = new ArrayList<>();
        for (File file : skinDir.listFiles()) {
            skinListName.add(file.getName());
        }
        return skinListName;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SkinManager.getInstance().saveSkin(mPath + File.separator + mSkinListName.get(position),
                                           mSkinListName.get(position),this,true);
    }

    @Override
    public void onClick(View v) {
        if (v == mChangeSkin) {
            mPath = mSdcardpath.getText().toString();
            List<String> skinListFile = getSkinListFile(mPath);
            if(skinListFile==null){
                LogUtils.e(TAG,"skinListFile is null");
                return ;
            }
            mSkinListName=skinListFile;
            mAdapter.notifyDataSetChanged();
            mListView.setOnItemClickListener(this);
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
        LogUtils.e(TAG, "progress:" + progress + " sum:" + sum);
    }

}