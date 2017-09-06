package com.wisn.skinlib;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinManager {
    private SkinManager(){}
    public static SkinManager manager;
    public SkinManager getInstance(){
        if(manager==null){
            synchronized (SkinManager.class){
                if(manager==null){
                    manager=new SkinManager();
                }
            }
        }
        return manager;
    }
}
