package com.wisn.skinlib.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.wisn.skinlib.config.SkinConfig;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisn on 2017/9/6.
 */

public class SkinUtils {
    private static final String TAG = "SkinUtils";
    private static LinkedHashMap<String, LinkedHashMap<String, String>>
            skinData =
            new LinkedHashMap<String, LinkedHashMap<String, String>>();

    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) return true;
        return false;
    }

    public static void getAllFileIndex(String skinName) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/dd/the/res");
        if (file.isDirectory()) {
            pasFileIndex(file);
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
            Log.e(TAG, next.getKey() + value);
        }
    }

    public static String getPath(String imageName) {
        String basePath = Environment.getExternalStorageDirectory().getPath() + "/dd/the/res";
        LinkedHashMap<String, String> stringStringLinkedHashMap = skinData.get(imageName);
        if (stringStringLinkedHashMap == null) {
            return imageName;
        } else {
            String indexFirst = getBestPath(stringStringLinkedHashMap);
            String s = stringStringLinkedHashMap.get(indexFirst);
            return basePath + File.separator + indexFirst + File.separator + s;
        }
    }

    private static String getBestPath(LinkedHashMap<String, String> densityMap) {
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
            } else if (next.contains("-xdpi")) {
                index[4] = next;
            } else if (next.contains("-xxdpi")) {
                index[5] = next;
            } else if (next.contains("-xxxdpi")) {
                index[6] = next;
            }
        }
        int firstIndex = getFirstIndex();
        String keyForindex = getKeyForHight(index, firstIndex);
        if (TextUtils.isEmpty(keyForindex)) {
            keyForindex = getKeyForLow(index, firstIndex);
        }
        return keyForindex;
    }

    private static String getKeyForLow(String[] index, int firstIndex) {
        if (firstIndex < 0) return null;
        String str0 = index[firstIndex];
        if (!TextUtils.isEmpty(str0)) {
            return str0;
        } else {
            return getKeyForHight(index, firstIndex--);
        }
    }

    private static String getKeyForHight(String[] index, int firstIndex) {
        if (firstIndex >= index.length) return null;
        String str0 = index[firstIndex];
        if (!TextUtils.isEmpty(str0)) {
            return str0;
        } else {
            return getKeyForHight(index, firstIndex++);
        }
    }

    private static int getFirstIndex() {
        int Firstindex = 1;
        if (SkinConfig.Density < 1) {
            //ldpi
            Firstindex = 0;
        } else if (SkinConfig.Density < 1.5) {
            //mdpi
            Firstindex = 1;
        } else if (SkinConfig.Density < 2) {
            //hdpi
            Firstindex = 3;
        } else if (SkinConfig.Density < 3) {
            //xhdpi
            Firstindex = 4;
        } else if (SkinConfig.Density < 4) {
            //xxhdpi
            Firstindex = 5;
        }
        return Firstindex;
    }

}
