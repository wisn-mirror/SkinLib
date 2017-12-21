package com.wisn.skinlib.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wisn on 2017/12/20.
 */

public class SkinThreadPool {
    private static SkinThreadPool mThreadPool;
    private final ExecutorService mExecutorService;

    private SkinThreadPool() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    public static SkinThreadPool getInstance() {
        if (mThreadPool == null) {
            synchronized (SkinThreadPool.class) {
                if (mThreadPool == null) {
                    mThreadPool = new SkinThreadPool();
                }
            }
        }
        return mThreadPool;
    }

    public void execute(Runnable runnable) {
        mExecutorService.execute(runnable);
    }
}
