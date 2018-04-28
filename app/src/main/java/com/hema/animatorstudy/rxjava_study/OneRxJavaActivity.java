package com.hema.animatorstudy.rxjava_study;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.hema.animatorstudy.R;
import com.hema.baselibrary.base.BaseActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by asus on 2017/12/25.
 */

public class OneRxJavaActivity extends BaseActivity {

    @BindView(R.id.b1)
    Button mB1;
    @BindView(R.id.b2)
    Button mB2;
    @BindView(R.id.b3)
    Button mB3;
    @BindView(R.id.b4)
    Button mB4;
    @BindView(R.id.text)
    TextView mText;


    @Override
    public int getContentView() {
        return R.layout.activity_rxjava_one;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    // 创建被观察者
    Integer[] numbers = {1, 2, 3, 4, 5, 6, 7};
    List<Integer> lists = Arrays.asList(numbers);
    Observable<Integer> integerObservable = Observable.from(lists);

    // 创建观察者 Subscriber
    // Subscriber是一种特殊类型的观察者，它可以取消订阅被观察者。
    Subscriber<Integer> mySubscriber = new Subscriber<Integer>() {
        @Override
        public void onNext(Integer data) {
            Log.d("Rx", "onNext:" + data);
            String text = mText.getText().toString();
            text += "\n结果 = \t" + data;
            mText.setText(text);
        }

        @Override
        public void onCompleted() {
            Log.d("Rx", "Complete!");
            mySubscriber.unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            // handle your error here
            String text = mText.getText().toString();
            text += "\n结果 = \t" + "失败";
            mText.setText(text);
        }
    };


    @Override
    public void initEvent() {

        // 注意： Func<T, R>表示一个单参数的函数，T是第一个参数的类型，R是返回结果的类型。

        /*
         subscribe(mObserver)和subscribe(mSubscriber)执行结果就会有区别：
         subscribe(mSubscriber)这种订阅方式在第二次请求数据时就不会执行了，原因就是第一次onNext后自动取消了订阅；
         subscribe(mObserver)则不出现此问题。

         注：正如前面所提到的，Observer 和 Subscriber 具有相同的角色，而且 Observer 在 subscribe() 过程中最终会
         被转换成 Subscriber 对象，因此，从这里开始，后面的描述我将用 Subscriber 来代替 Observer ，这样更加严谨。
         */

        // 默认情况从左到右依次读完
        RxView.clicks(mB1).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 第一种方法 subscribe(mSubscriber)
                        integerObservable
                                //.throttleFirst(1,TimeUnit.SECONDS)
                                .subscribe(mySubscriber);
                        // 第二种方法 subscribe(mObserver)则不出现此问题。
//                        integerObservable.subscribe(new Action1<Integer>() {
//                            @Override
//                            public void call(Integer integer) {
//                                Log.d("Rx", "onNext:" + integer);
//                                String text = mText.getText().toString();
//                                text += "\n结果 = \t" + integer;
//                                mText.setText(text);
//                            }
//                        });
                    }
                });


        // boolean判断
        RxView.clicks(mB2).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        integerObservable.filter(new Func1<Integer, Boolean>() {
                            @Override
                            public Boolean call(Integer o) {
                                return o % 2 == 0;
                            }
                        }).subscribe(mySubscriber);
                    }
                });

        // lambda 表达式
        RxView.clicks(mB3).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        integerObservable.filter(i -> i % 2 == 0).subscribe();
                    }
                });

        // int乘法
        RxView.clicks(mB4).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        integerObservable.map(new Func1<Integer, Integer>() {
                            @Override
                            public Integer call(Integer value) {
                                return value * value;
                            }
                        }).subscribe(mySubscriber);
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
