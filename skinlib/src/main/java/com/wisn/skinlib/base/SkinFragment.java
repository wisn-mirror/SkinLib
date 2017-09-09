package com.wisn.skinlib.base;

import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wisn.skinlib.attr.base.DynamicAttr;
import com.wisn.skinlib.interfaces.DynamicView;
import com.wisn.skinlib.loader.SkinAppCompatInflaterFactory;

import java.util.List;

/**
 * Created by wisn on 2017/9/5.
 */

public class SkinFragment extends Fragment implements DynamicView {
    private SkinAppCompatInflaterFactory mSkinInflaterFactory;

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

    public final SkinAppCompatInflaterFactory getSkinInflaterFactory() {
        if (mSkinInflaterFactory == null) {
            synchronized (SkinFragment.class) {
                if (getActivity() instanceof SkinAppCompatActivity) {
                    mSkinInflaterFactory = ((SkinAppCompatActivity) getActivity()).getSkinInflaterFactory();
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

    }

    @Override
    public void dynamicAddView(View view, String attrName, int attrValueresId) {

    }

    @Override
    public void dynamicAddFontView(TextView textView) {

    }
}
