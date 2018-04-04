package com.hema.animatorstudy.ac;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

import static com.hema.animatorstudy.R.layout.sidesli_view;

public class SideslipMenuActivity extends BaseActivity {


    @BindView(R.id.listview)
    RecyclerView listview;


    @Override
    public int getContentView() {
        return R.layout.activity_sideslip_menu;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        setTitle("侧滑控件");

    }

    List<String> data = new ArrayList<>();

    @Override
    public void initEvent() {

        for (int i = 0; i < 30; i++) {
            data.add("s" + i);
        }

        listview.setLayoutManager(new LinearLayoutManager(this));
        listview.setAdapter(new CommonAdapter<String>(this, sidesli_view, data) {
            @Override
            protected void convert(ViewHolder holder, String o, int position) {

            }
        });
    }

}
