package com.wisn.skin;

import android.os.Environment;

import com.wisn.skinlib.base.SkinApplication;

import java.io.File;

/**
 * Created by wisn on 2017/9/5.
 */

public class BaseApplication extends SkinApplication {
    @Override
    public String setSkinRootPath() {
        return Environment.getExternalStorageDirectory()
               + File.separator+"skinRootPath";
    }
}
