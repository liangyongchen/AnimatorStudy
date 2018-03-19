package com.hema.baselibrary.base;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;

import com.hema.baselibrary.exception.CrashHandler;
import com.hema.baselibrary.util.ImageDisplayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by asus on 2017/9/22.
 */

public class app extends Application {

    private static String TAG = "";

    private static app mContext;

    public static synchronized app getContext() {
        return (app) mContext;
    }

    // region  // 阿里巴巴字体图标

    private Typeface iconTypeFace;

    public Typeface getIconTypeFace() {
        return iconTypeFace;
    }

    // endregion

    // region // Activity 的添加与删除

    private List<Activity> acList; // 用于存放所有启动的Activity的集合

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!acList.contains(activity)) {
            acList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
        // 判断当前集合中存在该Activity
        if (acList.contains(activity)) {
            acList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : acList) {
            activity.finish();
        }
    }

    // endregion

    // region // 网络请求（OK3）

    private volatile static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (OkHttpClient.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder().connectTimeout(5 * 60, TimeUnit.SECONDS).readTimeout(5 * 60, TimeUnit.SECONDS).build();
                }
            }
        }
        return client;
    }

    // endregion

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        // Activity 的添加与删除
        acList = new ArrayList<Activity>();

        // 全程捕获异常信息
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        getClient(); // 初始化OK3

        // 设置阿里巴巴矢量字体初始化，防止在Activity加载造成卡顿现象
        this.iconTypeFace = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");

        // 图片加载初始化
        ImageDisplayUtil.initImageLoaderConfiguration(this);

    }

}
