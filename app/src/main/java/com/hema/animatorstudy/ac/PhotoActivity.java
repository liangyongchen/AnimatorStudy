package com.hema.animatorstudy.ac;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hema.animatorstudy.R;
import com.hema.animatorstudy.photostudy.PhotoSystemActivity;
import com.hema.baselibrary.base.BaseActivity;
import com.hema.baselibrary.util.CommonUtil;
import com.hema.baselibrary.util.IntentUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 2017-10-20
 * 调用系统相册和系统相机
 */
public class PhotoActivity extends BaseActivity {

    @BindView(R.id.Photo1)
    Button mPhoto1;
    @BindView(R.id.Photo2)
    Button mPhoto2;

    @Override
    public int getContentView() {
        return R.layout.activity_photo;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("相机/相册");
    }

    @Override
    public void initEvent() {

    }


    @OnClick({R.id.Photo1, R.id.Photo2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Photo1:  // 自定义相机
                Toast.makeText(this, "正在开发中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Photo2:  // 相机相册
                IntentUtil.startActivity(this, PhotoSystemActivity.class, CommonUtil.enumActionType.ACTION_FORWARD);
                break;
        }
    }
}
