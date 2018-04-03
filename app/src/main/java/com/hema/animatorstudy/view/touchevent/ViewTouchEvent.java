package com.hema.animatorstudy.view.touchevent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by 河马安卓 on 2018/4/3.
 */

public class ViewTouchEvent extends TextView {


    private String TAG = "";

    public void setTAG(String str) {
        TAG = str;
    }

    private TouchEventString mTouchEventString;

    public ViewTouchEvent(Context context) {
        super(context);
    }

    public ViewTouchEvent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTouchEvent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTouchEventString(TouchEventString touchEventString) {
        mTouchEventString = touchEventString;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mTouchEventString != null)
            mTouchEventString.toString(String.format("%s == %s ", TAG, "dispatchTouchEvent " + super.dispatchTouchEvent(event)));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchEventString != null)
            mTouchEventString.toString(String.format("%s == %s ", TAG, "onTouchEvent " + super.onTouchEvent(event)));
        return super.onTouchEvent(event);
    }

}
