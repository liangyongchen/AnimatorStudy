package com.hema.animatorstudy.ac;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hema.animatorstudy.R;
import com.hema.animatorstudy.view.custom.AdImageViewVersion;
import com.hema.baselibrary.base.BaseActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 上下滑动的时候图片也跟着一起滑动
 * Created by asus on 2017/12/5.
 */

public class AdImageViewActivity extends BaseActivity {


    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    @BindView(R.id.textview)
    TextView mTextview;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public int getContentView() {
        return R.layout.public_recycer_view;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        setTitle("ImageView滑动");
        mTextview.setText("实现上下滚动消息的时候图片跟着一起滚动的效果：自定义视图：AdImageView和AdImageViewBersion");
        List<String> mockDatas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mockDatas.add(i + "");
        }

        mMRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));

        mMRecyclerView.setAdapter(new CommonAdapter<String>(this, R.layout.ad_image_view_list, mockDatas) {
            @Override
            protected void convert(ViewHolder holder, String o, int position) {
                if (position > 0 && position % 7 == 0) {
                    holder.setVisible(R.id.id_tv_title, false);
                    holder.setVisible(R.id.id_tv_desc, false);
                    holder.setVisible(R.id.id_iv_ad, true);
                } else {
                    holder.setVisible(R.id.id_tv_title, true);
                    holder.setVisible(R.id.id_tv_desc, true);
                    holder.setVisible(R.id.id_iv_ad, false);
                }
            }
        });

        mMRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int fPos = mLinearLayoutManager.findFirstVisibleItemPosition();
                int lPos = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                for (int i = fPos; i <= lPos; i++) {
                    View view = mLinearLayoutManager.findViewByPosition(i);
                    AdImageViewVersion adImageView = view.findViewById(R.id.id_iv_ad);
                    if (adImageView.getVisibility() == View.VISIBLE) {
                        adImageView.setDy(mLinearLayoutManager.getHeight() - view.getTop());
                    }
                }
            }
        });
    }

    @Override
    public void initEvent() {

    }

    @OnClick(R.id.mRecyclerView)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mRecyclerView:
                break;
        }
    }
}
