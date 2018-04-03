package com.hema.animatorstudy.ac;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hema.animatorstudy.R;
import com.hema.animatorstudy.view.touchevent.TouchEventString;
import com.hema.animatorstudy.view.touchevent.ViewGroupTouchEvent;
import com.hema.animatorstudy.view.touchevent.ViewTouchEvent;
import com.hema.baselibrary.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TouchEventActivity extends BaseActivity implements TouchEventString {


    @BindView(R.id.view1)
    ViewTouchEvent view1;
    @BindView(R.id.view2)
    ViewTouchEvent view2;
    @BindView(R.id.viewGroup2)
    ViewGroupTouchEvent viewGroup2;
    @BindView(R.id.viewGroup1)
    ViewGroupTouchEvent viewGroup1;

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.button)
    Button button;


    private StringBuffer mString = new StringBuffer();

    @Override
    public int getContentView() {
        return R.layout.activity_touch_event;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("TouchEvent处理");
    }

    @Override
    public void initEvent() {

        viewGroup1.setTouchEventString(this);
        viewGroup1.setTAG("viewGroup A");
        view1.setTouchEventString(this);
        view1.setTAG("view B");
        viewGroup2.setTouchEventString(this);
        viewGroup2.setTAG("viewGroup C");
        view2.setTouchEventString(this);
        view2.setTAG("view D");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
            }
        });

    }


    @Override
    public void toString(String str) {
        mString.append(str + "\n");
        text.setText(mString.toString());
        Log.d("TAG", str);
    }

}
