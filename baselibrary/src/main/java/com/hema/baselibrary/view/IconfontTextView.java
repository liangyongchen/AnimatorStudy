package com.hema.baselibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hema.baselibrary.base.app;


/**
 * Created by asus on 2017/7/24.
 */

public class IconfontTextView extends TextView {

    public IconfontTextView(Context context) {
        super(context);
        init();
    }

    public IconfontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconfontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        设置字体图标
        this.setTypeface(app.getContext().getIconTypeFace());
    }

}
