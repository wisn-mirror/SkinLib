package com.wisn.skinlib.loader;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.wisn.skinlib.font.TypeFaceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisn on 2017/9/7.
 */

public class FontRepository {
    private static Map<Activity, List<View>> viewMap = new HashMap<>();

    public static void add(Activity activity, View view) {
        if (view instanceof TextView) {
            if (viewMap.containsKey(activity)) {
                viewMap.get(activity).add(view);
            } else {
                List<View> viewlist = new ArrayList<>();
                viewlist.add(view);
                viewMap.put(activity, viewlist);
            }
            // TODO: 2017/9/7   optimization
            ((TextView) view).setTypeface(TypeFaceUtils.Current_typeFace);
        }
    }

    public static void remove(Activity activity) {
        if (viewMap != null) {
            viewMap.remove(activity);
        }
    }

    public static boolean remove(Activity activity, View view) {
        if (viewMap != null) {
            return viewMap.containsKey(activity) && viewMap.get(activity).remove(view);
        } else {
            return false;
        }
    }

    public static void applyTypeFace(Typeface typeface){
        for (Activity activity:viewMap.keySet()){
            for(View view:viewMap.get(activity)){
                //// TODO: 2017/9/7 optimization 
                if(view instanceof TextView){
                    ((TextView) view).setTypeface(TypeFaceUtils.Current_typeFace);
                }
            }
        }
    }

}
