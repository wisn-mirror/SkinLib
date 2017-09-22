package com.wisn.skin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.wisn.skin.R;


/**
 * Created by wisn on 2017/9/12.
 */

public class MyRadioButton extends RadioButton {
    private int mDrawableSize;

    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ButtonView);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ButtonView_drawableSize:
                    mDrawableSize = a.getDimensionPixelSize(R.styleable.ButtonView_drawableSize, 50);
                    break;
                case R.styleable.ButtonView_drawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.ButtonView_drawableBottom:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.ButtonView_drawableRight:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.ButtonView_drawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public int getDrawableSize() {
        return mDrawableSize;
    }

    /**
     * RadioButton上、下、左、右设置图标
     */
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
                                                        Drawable top,
                                                        Drawable right,
                                                        Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

}
