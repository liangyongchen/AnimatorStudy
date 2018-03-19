package com.hema.animatorstudy.ac;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hema.animatorstudy.R;
import com.hema.baselibrary.base.BaseActivity;

import butterknife.BindView;

/**
 * 属性动画
 * Created by asus on 2017/12/5.
 */

public class AttributeActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    @BindView(R.id.textview)
    TextView mTextview;

    @Override
    public int getContentView() {
        return R.layout.public_recycer_view;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("自定义视图");
        mTextview.setText("学习和收集各种属性动画");
    }

    @Override
    public void initEvent() {

    }

}