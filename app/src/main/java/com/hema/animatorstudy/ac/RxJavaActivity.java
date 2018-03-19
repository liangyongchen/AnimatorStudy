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
 * 小游戏
 * Created by asus on 2017/12/5.
 */

public class RxJavaActivity extends BaseActivity {

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
        setTitle("RxJava1总结");
        mTextview.setText("RxJava学习");

        addData();

    }

    private void addData() {
        // RxJava下载App更新安装
        ActivityModel m = new ActivityModel();
        m.setName("RxJava下载App更新安装");
        m.setTypeActivity("com.hema.animatorstudy.appdownload.AppDownloadActivity");
        data.add(m);

        // 学习 RxJava 的 基本 原理
        ActivityModel m1 = new ActivityModel();
        m1.setName("学习RxJava的基本原理");
        m1.setTypeActivity("com.hema.animatorstudy.rxjava_study.OneRxJavaActivity");
        data.add(m1);

        // 学习 RxJava 的 基本 原理:线程加载、数据变换
        ActivityModel m2 = new ActivityModel();
        m2.setName("学习RxJava的基本原理:线程加载、数据变换");
        m2.setTypeActivity("com.hema.animatorstudy.rxjava_study.TwoRxJavaActivity");
        data.add(m2);

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
                            IntentUtil.startActivity(RxJavaActivity.this, t, CommonUtil.enumActionType.ACTION_FORWARD);
                        } catch (Exception e) {
                            String a = e.getMessage();
                        }
                    }
                });
            }
        });

    }


}
