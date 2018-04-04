package com.hema.animatorstudy.ac;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hema.animatorstudy.R;
import com.hema.animatorstudy.model.ActivityModel;
import com.hema.baselibrary.base.BaseActivity;
import com.hema.baselibrary.util.CommonUtil;
import com.hema.baselibrary.util.IntentUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

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

    private LinearLayoutManager mLinearLayoutManager;
    List<ActivityModel> data = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.public_recycer_view;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("自定义视图");
        mTextview.setText("学习和收集各种属性动画");

        addData();
    }

    private void addData() {

        // 侧滑属性控件
        ActivityModel m = new ActivityModel();
        m.setName("属性动画侧滑删除/添加/点赞");
        m.setTypeActivity("com.hema.animatorstudy.ac.SideslipMenuActivity");
        data.add(m);

        // 侧滑属性控件
        ActivityModel m1 = new ActivityModel();
        m1.setName("ObjectAnimator动画讲解");
        m1.setTypeActivity("com.hema.animatorstudy.ac.ObjectAnimatorActivity");
        data.add(m1);

    }


    @Override
    public void initEvent() {

        mMRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));
        mMRecyclerView.setAdapter(new CommonAdapter<ActivityModel>(this, R.layout.public_recycer_list_adapter, data) {
            @Override
            protected void convert(ViewHolder holder, ActivityModel o, int position) {

                holder.setVisible(R.id.textview, true);
                holder.setText(R.id.textview, o.getName());
                holder.setOnClickListener(R.id.textview, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Class t = Class.forName(o.getTypeActivity());
                            IntentUtil.startActivity(AttributeActivity.this, t, CommonUtil.enumActionType.ACTION_FORWARD);
                        } catch (Exception e) {
                            String a = e.getMessage();
                        }
                    }
                });
            }
        });

    }

}