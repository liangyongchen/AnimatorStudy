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
 * 自定义动画
 * Created by asus on 2017/12/5.
 */

public class CustomActivity extends BaseActivity {

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

    private void addData() {
        // AdImageViewActivity
        ActivityModel m = new ActivityModel();
        m.setName("AdImageViewActivity");
        m.setTypeActivity("com.hema.animatorstudy.ac.AdImageViewActivity");
        data.add(m);
        // TouchEventActivity
        ActivityModel m1 = new ActivityModel();
        m1.setName("TouchEventActivity");
        m1.setTypeActivity("com.hema.animatorstudy.ac.TouchEventActivity");
        data.add(m1);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("自定义视图");
        mTextview.setText("学习和收集各种自定义视图");

        addData();

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
                            IntentUtil.startActivity(CustomActivity.this, t, CommonUtil.enumActionType.ACTION_FORWARD);
                        } catch (Exception e) {
                            String a = e.getMessage();
                        }
                    }
                });
            }
        });

        mMRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

    }

    @Override
    public void initEvent() {

    }
}
