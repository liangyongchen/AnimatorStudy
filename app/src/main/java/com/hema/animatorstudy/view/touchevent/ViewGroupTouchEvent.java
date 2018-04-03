package com.hema.animatorstudy.view.touchevent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by 河马安卓 on 2018/4/3.
 */

public class ViewGroupTouchEvent extends LinearLayout {


    private String TAG = "";

    public void setTAG(String str) {
        TAG = str;
    }

    private TouchEventString mTouchEventString;

    public ViewGroupTouchEvent(Context context) {
        super(context);
    }

    public ViewGroupTouchEvent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupTouchEvent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTouchEventString(TouchEventString touchEventString) {
        mTouchEventString = touchEventString;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTouchEventString != null)
            mTouchEventString.toString(String.format("%s == %s ", TAG, "dispatchTouchEvent " + super.dispatchTouchEvent(ev)));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mTouchEventString != null)
            mTouchEventString.toString(String.format("%s == %s ", TAG, "onInterceptTouchEvent " + super.onInterceptTouchEvent(ev)));
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchEventString != null)
            mTouchEventString.toString(String.format("%s == %s ", TAG, "onTouchEvent " + super.onTouchEvent(event)));
        return super.onTouchEvent(event);
    }


}
