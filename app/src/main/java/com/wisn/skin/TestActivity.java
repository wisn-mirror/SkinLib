package com.wisn.skin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.base.SkinActivity;
import com.wisn.skinlib.base.SkinAppCompatActivity;
import com.wisn.skinlib.interfaces.SkinLoaderListener;
import com.wisn.skinlib.utils.LogUtils;
import com.wisn.skinlib.utils.SkinFileUitls;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends SkinActivity implements View.OnClickListener, SkinLoaderListener ,AdapterView.OnItemClickListener{
    private static final String TAG="TestActivity";
    private Button mChangeSkin,resetSkin,loadResPathList,loadFontList;
    private ListView mListView;
    private List<String> mSkinListName=new ArrayList<>();
    private BaseAdapter mAdapter;
    private boolean isFont=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mChangeSkin = (Button) findViewById(R.id.changeSkin);
        loadFontList = (Button) findViewById(R.id.loadFontList);
        resetSkin = (Button) findViewById(R.id.resetSkin);
        loadResPathList = (Button) findViewById(R.id.loadResPathList);
        mListView = (ListView) findViewById(R.id.listview);
        loadFontList.setOnClickListener(this);
        loadResPathList.setOnClickListener(this);
        mChangeSkin.setOnClickListener(this);
        resetSkin.setOnClickListener(this);
        if(mSkinListName!=null){
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
                    TextView textView = new TextView(TestActivity.this);
                    textView.setText(mSkinListName.get(position));
                    return textView;
                }
            };
            mListView.setAdapter(mAdapter);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(isFont){
            SkinManager.getInstance().loadFont(mSkinListName.get(position), this);
        }else{
            SkinManager.getInstance().loadSkin(mSkinListName.get(position), this);

        }
    }

    @Override
    public void onClick(View v) {
        isFont=false;
        if (v == mChangeSkin) {
            List<String> mSkinListNameaa= SkinManager.getInstance().getSkinListName(false,true);
            if(mSkinListNameaa==null)return ;
            mSkinListName = mSkinListNameaa;
            mAdapter.notifyDataSetChanged();
            mListView.setOnItemClickListener(null);
        } else if (v == resetSkin) {
            List<String> mSkinListNameaa= SkinManager.getInstance().getSkinListName(false,false);
            if(mSkinListNameaa==null)return ;
            mSkinListName = mSkinListNameaa;
            mAdapter.notifyDataSetChanged();
            mListView.setOnItemClickListener(this);
        }else if(v==loadResPathList){
            List<String> mSkinListNameaa= SkinManager.getInstance().getSkinListName(true,true);
            if(mSkinListNameaa==null)return ;
            mSkinListName = mSkinListNameaa;
            mAdapter.notifyDataSetChanged();
            mListView.setOnItemClickListener(null);
        } else if(v==loadFontList){
            List<String> fontListName= SkinManager.getInstance().getFontListName(false);
            if(fontListName==null)return ;
            isFont=true;
            mSkinListName = fontListName;
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
        LogUtils.e(TAG, "progress:" + progress+" sum:"+sum);
    }

}