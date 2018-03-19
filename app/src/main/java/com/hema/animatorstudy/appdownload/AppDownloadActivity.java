package com.hema.animatorstudy.appdownload;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.hema.animatorstudy.R;
import com.hema.baselibrary.base.BaseActivity;
import com.hema.baselibrary.base.EquipmentAuthorization;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * //
 * http://blog.csdn.net/cfy137000/article/details/70257912
 * Created by asus on 2017/11/14.
 */
public class AppDownloadActivity extends BaseActivity {

    private static final String APK_URL = "http://101.28.249.94/apk.r1.market.hiapk.com/data/upload/apkres/2017/4_11/15/com.baidu.searchbox_034250.apk";

    @BindView(R.id.down_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.down_btn)
    Button mDownBtn;
    @BindView(R.id.install_mode_switch)
    Switch mInstallModeSwitch;

    private DownloadService.DownloadBinder mDownloadBinder;
    private Subscription mSubscription; // 可以取消观察者

    // binder 通讯
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadBinder = null;
        }

    };

    @Override
    public int getContentView() {
        return R.layout.activity_app_download;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("app下载");
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE); // 绑定服务
    }

    @Override
    public void initEvent() {

        mInstallModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonView.setText("root模式");
            } else {
                buttonView.setText("普通模式");
            }
            if (mDownloadBinder != null) {
                mDownloadBinder.setInstallMode(isChecked);
            }
        });

        mDownBtn.setOnClickListener(v -> {
            requestPermission(EquipmentAuthorization.ANDROID_PERMISSION_GROUP_STORAGE, EquipmentAuthorization.ANDROID_PERMISSION_GROUP_STORAGE_KEY);
        });

    }

    @Override
    protected void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);

        if (mDownloadBinder != null) {
            long downloadId = mDownloadBinder.startDownload(APK_URL);
            startCheckProgress(downloadId);
        }

    }

    @Override
    protected void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
    }

    @Override
    protected void onDestroy() {
        if (mSubscription != null) {
            // 取消监听,释放内存
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    // 开始监听进度
    private void startCheckProgress(long downloadId) {
        Observable
                .interval(100, 200, TimeUnit.MILLISECONDS, Schedulers.io()) // 无限轮询,准备查询进度,在io线程执行
                .filter(times -> mDownloadBinder != null)
                .map(i -> mDownloadBinder.getProgress(downloadId)) // 获得下载进度
                .takeUntil(progress -> progress >= 100) // 返回true就停止了,当进度>=100就是下载完成了
                .distinct() // 去重复
                .subscribeOn(Schedulers.io()) // 改变调用它之前代码的线程
                .observeOn(AndroidSchedulers.mainThread()) // 改变调用它之后代码的线程
                .subscribe(new ProgressObserver()); // 事件
    }

    // 观察者
    private class ProgressObserver implements Observer<Integer> {

        //@Override
        public void onSubscribe(Subscription s) {
            mSubscription = s;
        }

        @Override
        public void onNext(Integer progress) {
            mProgressBar.setProgress(progress);//设置进度
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            Toast.makeText(AppDownloadActivity.this, "出错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCompleted() {
            mProgressBar.setProgress(100);
            Toast.makeText(AppDownloadActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
        }
    }

}


