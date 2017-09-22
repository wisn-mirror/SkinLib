package com.wisn.skinlib.base;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wisn.skinlib.attr.base.DynamicAttr;
import com.wisn.skinlib.attr.base.SkinAttr;
import com.wisn.skinlib.attr.base.SkinItem;
import com.wisn.skinlib.interfaces.DynamicView;
import com.wisn.skinlib.interfaces.LayoutInflaterIns;
import com.wisn.skinlib.loader.SkinInflater;

import java.util.List;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinFragment extends Fragment implements DynamicView {
    private SkinInflater mSkinInflaterFactory;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        removeAllView(getView());
        super.onDestroyView();
    }

    public final SkinInflater getSkinInflaterFactory() {
        if (mSkinInflaterFactory == null) {
            synchronized (SkinFragment.class) {
                Object obj = getActivity();
                if (obj instanceof SkinInflater) {
                    mSkinInflaterFactory = ((LayoutInflaterIns) getActivity()).getSkinInflaterFactory();
                    return mSkinInflaterFactory;
                } else {
                    throw new RuntimeException(
                            " your  activity should extends SkinAppCompatActivity and  create SkinAppCompatInflaterFactory");
                }
            }
        }
        return mSkinInflaterFactory;
    }

    private void removeAllView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                removeAllView(viewGroup.getChildAt(i));
            }
        }
        getSkinInflaterFactory().removeSkinView(view);
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> attr) {
        if(mSkinInflaterFactory==null)return ;
        mSkinInflaterFactory.addSkinView(view,attr);
    }

    @Override
    public void dynamicAddView(View view, String attrName, int attrValueresId) {
        if(mSkinInflaterFactory==null)return ;
        mSkinInflaterFactory.addSkinView(view,attrName,attrValueresId);
    }

    @Override
    public void dynamicAddFontView(TextView textView) {

    }

    @Override
    public void dynamicAddView(View view, SkinAttr skinAttr) {
        if(mSkinInflaterFactory==null)return ;
        mSkinInflaterFactory.addSkinView(view,skinAttr);
    }
    @Override
    public void dynamicAddView(SkinItem skinItem) {
        if (mSkinInflaterFactory == null) return;
        mSkinInflaterFactory.addSkinView(skinItem);
    }
}
