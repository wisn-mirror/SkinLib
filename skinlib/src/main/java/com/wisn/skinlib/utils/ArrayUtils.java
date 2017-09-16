package com.wisn.skinlib.utils;

import java.util.List;

/**
 * Created by wisn on 2017/9/16.
 */

public class ArrayUtils {

    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) return true;
        return false;
    }
}
