package com.wisn.skinlib.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wisn.skinlib.SkinManager;
import com.wisn.skinlib.config.SkinConfig;
import com.wisn.skinlib.utils.LogUtils;
import com.wisn.skinlib.utils.SkinFileUitls;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinResourceCompat {
    private static final String TAG = "SkinResourceCompat";
    private static LinkedHashMap<String, LinkedHashMap<String, String>>
            skinData =
            new LinkedHashMap<>();

    public static void loadSkinFile(String skinPathRes) {
        File file =new File(skinPathRes);
        if (file.exists() && file.isDirectory()) {
            if (skinData != null) {
                skinData.clear();
                pasFileIndex(file);
            }
        }
    }

    private static void pasFileIndex(File fileDir) {
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                pasFileIndex(file);
            } else {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    Log.e(TAG,
                          "getName:" + file.getName().substring(0, file.getName().lastIndexOf(".")) +
                          " getName:" + file.getName() +
                          " getParent:" + file.getParent() +
                          "   getParentFile:" + file.getParentFile().getName());

                    String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
                    LinkedHashMap<String, String> strings = skinData.get(fileName);
                    if (strings != null) {
                        strings.put(file.getParentFile().getName(), file.getName());
                    } else {
                        LinkedHashMap<String, String> strings1 = new LinkedHashMap<>();
                        strings1.put(file.getParentFile().getName(), file.getName());
                        skinData.put(fileName, strings1);
                    }
                }

            }
        }
    }

    public static void print() {
        Iterator<Map.Entry<String, LinkedHashMap<String, String>>> iterator = skinData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LinkedHashMap<String, String>> next = iterator.next();
            LinkedHashMap<String, String> value1 = next.getValue();
            Iterator<Map.Entry<String, String>> iterator2 = value1.entrySet().iterator();
            String value = " value :";
            while (iterator2.hasNext()) {
                Map.Entry<String, String> next1 = iterator2.next();
                value = value + " getKey:" + next1.getKey() + " getValue:" + next1.getValue();
            }
            LogUtils.e(TAG, next.getKey() + value);
        }
    }

    public static String getPathForRN(String imageName) {
        return getPath(imageName, true);
    }

    public static String getPath(String imageName) {
        return getPath(imageName, false);
    }

    private  static String getPath(String imageName, boolean isRN) {
        if (SkinManager.getInstance().skinPathRes == null||SkinManager.getInstance().isDefaultSkin) {
            return imageName;
        }
        LinkedHashMap<String, String> skinImgRes = skinData.get(imageName);
        if (skinImgRes == null) {
            return imageName;
        } else {
            /*if (SkinConfig.isDebug) {
                Iterator<Map.Entry<String, String>> iterator2 = skinImgRes.entrySet().iterator();
                String value = " value :";
                while (iterator2.hasNext()) {
                    Map.Entry<String, String> next1 = iterator2.next();
                    value = value + " getKey:" + next1.getKey() + " getValue:" + next1.getValue();
                }
                LogUtils.e(TAG, "imageName:"+imageName+"  " + value);
            }*/
            String indexFirst = getBestIndex(skinImgRes);
            String s = skinImgRes.get(indexFirst);
//            LogUtils.e(TAG, "indexFirstddd:" + indexFirst + " " + s);
            if (TextUtils.isEmpty(s)) {
                return imageName;
            }
            if (isRN) {
                return "file://" +
                       SkinManager.getInstance().skinPathRes+ indexFirst +"／"+s;
            } else {
                return SkinManager.getInstance().skinPathRes + indexFirst +"／"+s;
            }
        }
    }

    private static String getBestIndex(LinkedHashMap<String, String> densityMap) {
        String[] index = new String[7];
        Iterator<String> iterator = densityMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.contains("-ldpi")) {
                index[0] = next;
            } else if (next.equals("drawable") || next.equals("mipmap")) {
                index[1] = next;
            } else if (next.contains("-mdpi")) {
                index[2] = next;
            } else if (next.contains("-hdpi")) {
                index[3] = next;
            } else if (next.contains("-xhdpi")) {
                index[4] = next;
            } else if (next.contains("-xxhdpi")) {
                index[5] = next;
            } else if (next.contains("-xxxhdpi")) {
                index[6] = next;
            }
        }
        String keyForindex = getIndexForHight(index, SkinConfig.FirstIndex);
        if (TextUtils.isEmpty(keyForindex)) {
            keyForindex = getIndexForLow(index, SkinConfig.FirstIndex-1);
        }
        return keyForindex;
    }

    private static String getIndexForLow(String[] index, int firstIndex) {
        if (firstIndex < 0) return null;
        String str0 = index[firstIndex];
//        LogUtils.e(TAG,"getIndexForLow: str0 :"+str0+ " firstIndex:"+firstIndex);
        if (!TextUtils.isEmpty(str0)) {
            return str0;
        } else {
            return getIndexForLow(index, --firstIndex);
        }
    }

    private static String getIndexForHight(String[] index, int firstIndex) {
        if (firstIndex >= 7) return null;
        String str0 = index[firstIndex];
//        LogUtils.e(TAG,"getIndexForHight :str0 :"+str0+ " firstIndex:"+firstIndex);
        if (!TextUtils.isEmpty(str0)) {
            return str0;
        } else {
            return getIndexForHight(index, ++firstIndex);
        }
    }

}
