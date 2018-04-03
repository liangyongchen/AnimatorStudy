package com.hema.animatorstudy;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hema.animatorstudy.ac.AttributeActivity;
import com.hema.animatorstudy.ac.CustomActivity;
import com.hema.animatorstudy.ac.GameActivity;
import com.hema.animatorstudy.ac.PhotoActivity;
import com.hema.animatorstudy.ac.RxJavaActivity;
import com.hema.animatorstudy.ac.TraditionActivity;
import com.hema.baselibrary.base.BaseActivity;
import com.hema.baselibrary.util.CommonUtil;
import com.hema.baselibrary.util.IntentUtil;
import com.hema.baselibrary.view._recycler_view.RefreshItemDecoration;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * 学习网址：
 * 1.上篇：http://blog.csdn.net/guolin_blog/article/details/43536355
 * 2.中篇：http://blog.csdn.net/guolin_blog/article/details/43816093
 * 3.下篇：http://blog.csdn.net/guolin_blog/article/details/44171115
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    RecyclerViewAdapter adapter;

    public static final String[] data = {
            "传统动画",
            "属性动画",
            "自定义视图",
            "相机/相册",
            "RxJava",
            "小游戏"};

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        setBackButtonVisibility(View.GONE);
        setTitle("功能界面");

        adapter = new RecyclerViewAdapter();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, VERTICAL, false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); // 动画
        // 分割线
        mRecyclerView.addItemDecoration(new RefreshItemDecoration(this, RefreshItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void initEvent() {

    }

    // 适配器
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public RecyclerViewAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.public_recycer_list_adapter, parent, false);

            return new cViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof cViewHolder) {
                cViewHolder cVH = (cViewHolder) holder;
                String aa = data[position];
                cVH.setData(aa);
                cVH.initEvent();
            }
        }

        @Override
        public int getItemCount() {
            return data.length;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        class cViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textview)
            TextView mTextview;

            public cViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void setData(String str) {
                mTextview.setText(str);
            }

            public void initEvent() {
                RxView.clicks(mTextview).throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                switch (mTextview.getText().toString()) {
                                    case "传统动画":
                                        IntentUtil.startActivity(MainActivity.this, TraditionActivity.class, CommonUtil.enumActionType.ACTION_FORWARD);
                                        break;
                                    case "属性动画":
                                        IntentUtil.startActivity(MainActivity.this, AttributeActivity.class, CommonUtil.enumActionType.ACTION_FORWARD);
                                        break;
                                    case "自定义视图":
                                        IntentUtil.startActivity(MainActivity.this, CustomActivity.class, CommonUtil.enumActionType.ACTION_FORWARD);
                                        break;
                                    case "相机/相册":
                                        IntentUtil.startActivity(MainActivity.this, PhotoActivity.class, CommonUtil.enumActionType.ACTION_FORWARD);
                                        break;
                                    case "RxJava":
                                        IntentUtil.startActivity(MainActivity.this, RxJavaActivity.class, CommonUtil.enumActionType.ACTION_FORWARD);
                                        break;
                                    case "小游戏":
                                        IntentUtil.startActivity(MainActivity.this, GameActivity.class, CommonUtil.enumActionType.ACTION_FORWARD);
                                        break;
                                }
                            }
                        });
            }
        }
    }

}
