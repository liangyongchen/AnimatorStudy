package com.hema.baselibrary.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hema.baselibrary.R;
import com.hema.baselibrary.util.CommonUtil;
import com.hema.baselibrary.util.FragmentUtil;
import com.hema.baselibrary.util.IntentUtil;
import com.hema.baselibrary.view.IconfontTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by asus on 2017/9/22.
 */

public abstract class BaseActivity extends FragmentActivity implements BackHandledInterface {


    // region  // 初始化

    // 返回
    private IconfontTextView btnBack;
    // 标题
    private TextView txtTitle;
    private FrameLayout viewContent;

    private void initView() {
        btnBack = (IconfontTextView) findViewById(R.id.btnBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        viewContent = (FrameLayout) findViewById(R.id.viewContent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //  去标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  竖屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();

        try {

            LayoutInflater.from(BaseActivity.this).inflate(getContentView(), viewContent); // 添加子布局
            ButterKnife.bind(BaseActivity.this, viewContent);

            init(savedInstanceState);

            initEvent();

            setBackClick();

        } catch (Exception e) {
            String a = e.getMessage();
        }

    }

    // 布局
    public abstract int getContentView();

    public abstract void init(Bundle savedInstanceState);

    public abstract void initEvent();

    protected void setTitle(String title) {
        txtTitle.setText(title);
    }

    private void setBackClick() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

    }

    protected void setBackButtonVisibility(int visibility) {
        btnBack.setVisibility(visibility);
    }

    protected void onBackClick() {
        IntentUtil.destroyActivity(this, CommonUtil.enumActionType.ACTION_SIGN_OUT);
    }

    // endregion


    // region // 设置 Edittext 之外点击随意地方，隐藏键盘

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View v = getCurrentFocus();
            if (isShouHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            return super.dispatchTouchEvent(ev);
        }

        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    //     计算 Edittext 之外的位置
    private boolean isShouHideInput(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的 location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();

            // 点击的是输入框的区域，保存点击EditText事件
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // endregion


    // region  // android 授权的方法

    private OnPermission onPermission;

    public void setOnPermission(OnPermission mOnPermission) {
        onPermission = mOnPermission;
    }


    private int mRequestCode;

    /**
     * 请求权限
     *
     * @param permissions 需要的权限列表
     * @param requestCode 请求码
     */
    protected void requestPermission(String[] permissions, int requestCode) {
        mRequestCode = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(requestCode);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), requestCode);
        }
    }

    /**
     * 检查所需的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        // 手机版本 SDK 低于23 ，在Manifest 上注册有效，大于 23 的（android6.0以后的），读取手机的隐私需要在代码动态申请
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取所需权限列表中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            // 检查权限,如果没有授权就添加
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 权限请求成功
     *
     * @param requestCode
     */
    protected void permissionSuccess(int requestCode) {
        Log.d("permissionSuccess====== ", "获取权限成功：" + requestCode);
        if (onPermission != null) {
            onPermission.permissionSuccess(requestCode);
        }
    }

    /**
     * 权限请求失败
     *
     * @param requestCode
     */
    protected void permissionFail(int requestCode) {
        Log.d("permissionFail====== ", "获取权限失败：" + requestCode);
        if (onPermission != null) {
            onPermission.permissionFail(requestCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //系统请求权限回调
        if (requestCode == mRequestCode) {
            if (EquipmentAuthorization.verifyPermissions(grantResults)) {
                permissionSuccess(mRequestCode);
            } else {
                permissionFail(mRequestCode);
            }
        }
    }

    // endregion


    // region // Fragment 同意监听返回键

    private BaseFragment mBackHandedFragment;
    private boolean hadIntercept;

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {

        if (mBackHandedFragment != null) {
            Log.d(String.format("onBackPressed ==========%s", mBackHandedFragment.toString()), "onBackPressed");
        }
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                // super.onBackPressed();
                IntentUtil.destroyActivity(this, CommonUtil.enumActionType.ACTION_SIGN_OUT);
            } else {
                //getSupportFragmentManager().popBackStack();
                FragmentUtil.removeFragment(this, mBackHandedFragment, CommonUtil.enumActionType.ACTION_SIGN_OUT2); // 动画销毁
            }
        }

    }
    // endregion


}
