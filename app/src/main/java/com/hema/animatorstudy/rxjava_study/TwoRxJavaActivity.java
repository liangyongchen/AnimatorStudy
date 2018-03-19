package com.hema.animatorstudy.rxjava_study;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hema.animatorstudy.R;
import com.hema.baselibrary.base.BaseActivity;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2017/12/25.
 */

public class TwoRxJavaActivity extends BaseActivity {


    // region // 初始化

    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.img)
    ImageView mImg;
    @BindView(R.id.btnImg)
    Button mBtnImg;
    @BindView(R.id.created)
    Button mCreated;
    @BindView(R.id.from)
    Button mFrom;
    @BindView(R.id.just)
    Button mJust;
    @BindView(R.id.map)
    Button mMap;
    @BindView(R.id.flatMap)
    Button mFlatMap;
    @BindView(R.id.filter)
    Button mFilter;
    @BindView(R.id.lift)
    Button mLift;
    @BindView(R.id.compose)
    Button mCompose;

    public TwoRxJavaActivity() throws URISyntaxException {
    }

    // endregion

    // region // 父类方法 + 生命周期

    @Override
    public int getContentView() {
        return R.layout.activity_rxjava_two;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("RxJava进阶");
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // endregion


    @OnClick({R.id.btnImg, R.id.created, R.id.from, R.id.just, R.id.map, R.id.flatMap, R.id.filter, R.id.lift, R.id.compose})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnImg:
                setImg();
                break;
            case R.id.created:
                create();
                break;
            case R.id.from:
                from();
                break;
            case R.id.just:
                just();
                break;
            case R.id.map:
                map();
                break;
            case R.id.flatMap:
                flatMap();
                break;
            case R.id.filter:
                filter();
                break;
            case R.id.lift:
                lift();
                break;
            case R.id.compose:
                compose();
                break;
        }
    }

    // region// 加载图片

    private void setImg() {
        Drawable[] b = {getResources().getDrawable(R.mipmap.icon1), getResources().getDrawable(R.mipmap.icon1)};
        Observable.from(b)
                // 筛选 方法 从上往下 执行
                .flatMap(new Func1<Drawable, Observable<Drawable>>() {
                    @Override
                    public Observable<Drawable> call(Drawable bitmap) {
                        // Toast.makeText(TwoRxJavaActivity.this, "哈哈哈哈哈", Toast.LENGTH_SHORT).show(); // 添加了这句话.subscribeOn(Schedulers.io())已经是主线程之外的方法了
                        return Observable.from(new Drawable[]{bitmap});
                    }
                })
                .filter(i -> i != null)
                .map(new Func1<Drawable, Bitmap>() {
                    @Override
                    public Bitmap call(Drawable drawable) {
                        BitmapDrawable bd = (BitmapDrawable) drawable;
                        Bitmap bm = bd.getBitmap();
                        return bm;
                    }
                })
                // 把加载图片的处理放在主线程之外，可以防止短时间的卡顿，让用户使用app更加的随心应手
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程:所以 flatMap、filter、map都会发生在主线程之外，不能更新界面信息
                .observeOn(AndroidSchedulers.mainThread()) // // 指定 Subscriber 的回调发生在主线程:所以 new 出来的 观察则 都会发生在主线程中
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        Toast.makeText(TwoRxJavaActivity.this, "哈哈哈哈哈", Toast.LENGTH_SHORT).show();
                        mImg.setImageBitmap(bitmap);
                    }
                });
    }

// endregion

    // region // created 方法

    private void create() {

        Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("Observable observable = Observable.create(new Observable.OnSubscribe<String>() {");
                        subscriber.onNext("    @Override");
                        subscriber.onNext("    public void call(Subscriber<? super String> subscriber) {");
                        subscriber.onNext("        subscriber.onNext(\"Hello\");");
                        subscriber.onNext("        subscriber.onNext(\"Hi\");");
                        subscriber.onNext("        subscriber.onNext(\"Aloha\");");
                        subscriber.onNext("        subscriber.onCompleted();");
                        subscriber.onNext("    }");
                        subscriber.onNext("});");
                        subscriber.onNext("observable.subscribeOn(Schedulers.io();");
                        subscriber.onNext("observable.observeOn(AndroidSchedulers.mainThread());");
                        subscriber.onNext("observable.subscribe(new Action1<String>() {");
                        subscriber.onNext("    @Override");
                        subscriber.onNext("    public void call(String s) {");
                        subscriber.onNext("    }");
                        subscriber.onNext("    });");
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    String text = "";

                    @Override
                    public void onCompleted() {
                        mText.setText(text);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        text += s + "\n";
                    }
                });
//                 // 有多少个 onNext 就会执行多少次
//                .subscribe(new Action1<String>() {
//                    String text = "";
//                    @Override
//                    public void call(String s) {
//                        text += s + "\n";
//                        mText.setText(text);
//                    }
//                });
    }

    // endregion

    // region // from 方法

    private void from() {

        // region // data 资料

        String[] words = {
                "String[] words = {\"Hello\", \"Hi\", \"Aloha\", \"\", null, \"最后\"};",
                "// from : 数据可以切换，不一定说传什么就返回什么\n",
                "Observable.from(words)",
                "     // 判断是否存在",
                "     // .exists(i -> StringUtils.isNotEmpty(i)) ",
                "     // 判断是否存在",
                "     // .map((Func1<String, Boolean>) s -> StringUtils.isNotEmpty(s)); ",
                "     .map(new Func1<String, Integer>() {",
                "         @Override",
                "         public Integer call(String s) {",
                "             return StringUtils.isNotEmpty(s) ? 0 : 1;",
                "         }",
                "     })",
                "     // lambda 表达式 ",
                "     //.lift((Observable.Operator<String, Integer>) subscriber -> new Subscriber<Integer>() {  ",
                "     // 替换/转换对象 ",
                "     .lift(new Observable.Operator<String, Integer>() {",
                "         @Override",
                "         public Subscriber<? super Integer> call(Subscriber<? super String> subscriber) {",
                "            return new Subscriber<Integer>() {",
                "",
                "               @Override",
                "               public void onCompleted() { }",
                "",
                "               @Override",
                "               public void onError(Throwable e) { }",
                "",
                "               @Override",
                "               public void onNext(Integer integer) {",
                "                  subscriber.onNext(\"\" + integer);",
                "               }",
                "            };",
                "         }",
                "     })",
                "     .subscribe(new Action1<String>() {",
                "         String text = \"\";",
                "         @Override",
                "         public void call(String s) {",
                "             text += s;",
                "             mText.setText(text);",
                "         }",
                "     });",
                "// 打印出来 是 0,0,0,1,1,0 （null 就为 1，不为空就是0）",
                "",
                "",
                null,
                "最后完成"};

        // endregion

        // from : 数据可以切换，不一定说传什么就返回什么
        Observable.from(words)
                // lift 为替换功能
                // .lift((Observable.Operator<String, String>) subscriber -> new Subscriber<String>() {
                .lift(new Observable.Operator<String, String>() {
                    @Override
                    public Subscriber<? super String> call(Subscriber<? super String> subscriber) {
                        return new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {
                                subscriber.onNext(s);
                            }
                        };
                    }
                })
                .subscribe(new Action1<String>() {
                    String text = "";

                    @Override
                    public void call(String s) {
                        text += "\n" + s;
                        mText.setText(text);
                    }
                });

    }

    // endregion

    // region // just 方法

    private void just() {

        // region // data 资料

        List<String> data = new ArrayList<>();
        data.add("int[] data1 = {11, 12, 13, 14, 15, 16, 17, 18, 19};");
        data.add("int[] data2 = {21, 22, 23, 24, 25, 26, 27, 28, 29};");
        data.add("int[] data3 = {31, 32, 33, 34, 35, 36, 37, 38, 39};");
        data.add("// just: 传什么返回什么，需要用 替换/转换的方法转类型");
        data.add("Observable.just(data1, data2, data3)");
        data.add("   .filter(new Func1<int[], Boolean>() {");
        data.add("       @Override");
        data.add("       public Boolean call(int[] ints) {");
        data.add("           return ints.length > 0;");
        data.add("       }");
        data.add("   })");
        data.add("   .flatMap(new Func1<int[], Observable<String>>() {");
        data.add("       @Override");
        data.add("       public Observable<String> call(int[] ints) {");
        data.add("          // 不能在用just:否则我不能拆分数组");
        data.add("           return Observable.from(getStringCom(ints)); // 转成 String[] 类型");
        data.add("       }");
        data.add("   })");
        data.add("   // 可要可无");
        data.add("   .lift(new Observable.Operator<String, String>() {");
        data.add("       @Override");
        data.add("       public Subscriber<? super String> call(Subscriber<? super String> subscriber) {");
        data.add("           return new Subscriber<String>() {");
        data.add("               @Override");
        data.add("               public void onCompleted() { }");
        data.add("               @Override");
        data.add("               public void onError(Throwable e) { }");
        data.add("               @Override");
        data.add("               public void onNext(String s) {");
        data.add("                  subscriber.onNext(s);");
        data.add("               }");
        data.add("           };");
        data.add("       }");
        data.add("   })");
        data.add("   // 上加了线程切换会和上面的lift的观察者发生交集错误");
        data.add("   // .subscribeOn(Schedulers.io())");
        data.add("   // .observeOn(AndroidSchedulers.mainThread())");
        data.add("   .subscribe(new Action1<String>() {");
        data.add("       String text = \"\";");
        data.add("       @Override");
        data.add("       public void call(String str) {");
        data.add("           text += \"\\n\" + str;");
        data.add("           mText.setText(text);");
        data.add("       }");
        data.add("   });");
        data.add("");
        data.add("");
        data.add(" 输出结果(都是换行的)：11, 12, 13, 14, 15, 16, 17, 18, 19,21, 22, 23, 24, 25, 26, 27, 28, 29,31, 32, 33, 34, 35, 36, 37, 38, 39");
        data.add("");
        data.add("");
        data.add("");
        data.add(" // lambda 表达式写法");
        data.add(" Observable.just(data1, data2, data3)");
        data.add("   .filter(i -> i.length > 0)");
        data.add("   .flatMap(ints -> Observable.from(getStringCom(ints)))");
        data.add("   .lift((Observable.Operator<String, String>) subscriber -> new Subscriber<String>() {");
        data.add("       @Override");
        data.add("       public void onCompleted() { }");
        data.add("       @Override");
        data.add("       public void onError(Throwable e) { }");
        data.add("       @Override");
        data.add("       public void onNext(String s) {");
        data.add("           subscriber.onNext(s);");
        data.add("       }");
        data.add("   })");
        data.add("   // 上加了线程切换会和上面的lift的观察者发生交集错误");
        data.add("   // .subscribeOn(Schedulers.io())");
        data.add("   // .observeOn(AndroidSchedulers.mainThread())");
        data.add("  .subscribe(s -> mText.setText(s));");
        data.add("");

        // endregion

        Observable.just(data)
                .filter(i -> i.size() > 0)
                // 需要转换一下，要不然直接返回List<String> 的类型
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    String text = "";

                    @Override
                    public void onNext(String s) {
                        text += "\n" + s;
                        mText.setText(text);
                    }
                });


        // region // 常规写法

        // int[] data1 = {11, 12, 13, 14, 15, 16, 17, 18, 19};
        // int[] data2 = {21, 22, 23, 24, 25, 26, 27, 28, 29};
        // int[] data3 = {31, 32, 33, 34, 35, 36, 37, 38, 39};
        // // just: 传什么返回什么
        // Observable.just(data1, data2, data3)
        //         .filter(new Func1<int[], Boolean>() {
        //             @Override
        //             public Boolean call(int[] ints) {
        //                 return ints.length > 0;
        //             }
        //         })
        //         .flatMap(new Func1<int[], Observable<String>>() {
        //             @Override
        //             public Observable<String> call(int[] ints) {
        //                 // 不能在用just:否则我不能拆分数组
        //                 return Observable.from(getStringCom(ints)); // 转成 String[] 类型
        //             }
        //         })
        //          .lift(new Observable.Operator<String, String>() {
        //             @Override
        //              public Subscriber<? super String> call(Subscriber<? super String> subscriber) {
        //                  return new Subscriber<String>() {
        //                      @Override
        //                     public void onCompleted() {
        //
        //                     }
        //                     @Override
        //                     public void onError(Throwable e) {
        //
        //                     }
        //                     @Override
        //                     public void onNext(String s) {
        //                         subscriber.onNext(s);
        //                     }
        //                 };
        //             }
        //          })
        //         // 上加了线程切换会和上面的lift的观察者发生交集错误
        //         // .subscribeOn(Schedulers.io())
        //         // .observeOn(AndroidSchedulers.mainThread())
        //         .subscribe(new Action1<String>() {
        //             String text = "";
        //             @Override
        //             public void call(String str) {
        //                 text += "\n" + str;
        //                 mText.setText(text);
        //             }
        //         });

        // endregion

        // region // lambda 表达式写法

        // lambda 表达式写法
        // Observable.just(data1, data2, data3)
        //   .filter(i -> i.length > 0)
        //   .flatMap(ints -> Observable.from(getStringCom(ints)))
        //   .lift((Observable.Operator<String, String>) subscriber -> new Subscriber<String>() {
        //       @Override
        //       public void onCompleted() { }
        //       @Override
        //       public void onError(Throwable e) { }
        //       @Override
        //       public void onNext(String s) {
        //           subscriber.onNext(s);
        //       }
        //   })
        //   // 上加了线程切换会和上面的lift的观察者发生交集错误
        //   // .subscribeOn(Schedulers.io())
        //   // .observeOn(AndroidSchedulers.mainThread())
        //  .subscribe(s -> mText.setText(s));

        // endregion

    }

    private String[] getStringCom(int[] i) {
        String[] ss = new String[i.length];
        for (int j = 0; j < i.length; j++) {
            ss[j] = String.valueOf(i[j]);
        }
        return ss;
    }

    // endregion

    // region // map 方法

    private void map() {

        // region // 资料

        List<String> list = new ArrayList<>();
        list.add("int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};");
        list.add("// 类型替换");
        list.add("Observable.just(data)");
        list.add("  .filter(i -> i.length > 0)");
        list.add("  // int[] 转 String[] ；map 是 一对一 关系");
        list.add("  .map(new Func1<int[], String[]>() {");
        list.add("      @Override");
        list.add("      public String[] call(int[] ints) {");
        list.add("          return getStringCom(ints);");
        list.add("      }");
        list.add("  })");
        list.add("  .subscribe(new Action1<String[]>() {");
        list.add("      @Override");
        list.add("      public void call(String[] strings) {");
        list.add("          Gson gson = new Gson();");
        list.add("          mText.setText(gson.toJson(strings));");
        list.add("      }");
        list.add("  });");
        list.add("");
        list.add("");
        list.add("输出结果：[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"0\"]");
        list.add("");
        list.add("");
        list.add("// Lambda 表达式");
        list.add("int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};");
        list.add("Observable.just(data)");
        list.add("        .filter(i -> i.length > 0)");
        list.add("        // int[] 转 String[]");
        list.add("        .map(ints -> getStringCom(ints))");
        list.add("        .subscribe(strings -> {");
        list.add("            Gson gson = new Gson();");
        list.add("            String a = gson.toJson(strings);");
        list.add("            mText.setText(gson.toJson(strings));");
        list.add("        });");
        list.add("");
        list.add("");
        list.add("输出结果：[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"0\"]");
        list.add("");
        list.add("");

        // endregion

        Observable.from(list)
                .filter(i -> i != null)
                .subscribe(new Action1<String>() {
                    String text = "";

                    @Override
                    public void call(String s) {
                        text += "\n" + s;
                        mText.setText(text);
                    }
                });


        // int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        // // 类型替换
        // Observable.just(data)
        //         .filter(i -> i.length > 0)
        //         // int[] 转 String[]
        //         .map(ints -> getStringCom(ints))
        //         .subscribe(strings -> {
        //             Gson gson = new Gson();
        //             String a = gson.toJson(strings);
        //             mText.setText(gson.toJson(strings));
        //         });


    }

    // endregion

    // region // flatMap 方法

    private void flatMap() {

        // region // 资料

        List<String> data = new ArrayList<>();

        data.add("Student[] students = ...;");
        data.add("Subscriber<Course> subscriber = new Subscriber<Course>() {");
        data.add("        @Override");
        data.add("        public void onNext(Course course) {");
        data.add("            Log.d(tag, course.getName());");
        data.add("        }");
        data.add("");
        data.add("        ...");
        data.add("");
        data.add("};");
        data.add("");
        data.add("Observable.from(students)");
        data.add("        // 替换，一对多 的关系");
        data.add("        .flatMap(new Func1<Student, Observable<Course>>() { ");
        data.add("            @Override");
        data.add("            public Observable<Course> call(Student student) {");
        data.add("                return Observable.from(student.getCourses());");
        data.add("            }");
        data.add("        })");
        data.add("        .subscribe(subscriber);");
        data.add("");
        data.add("");
        data.add("输出结果 ： 张三、李四、王五、...");
        data.add("");

        // endregion

        Observable.from(data)
                .filter(i -> i != null)
                .subscribe(new Action1<String>() {
                    String text = "";

                    @Override
                    public void call(String s) {
                        text += "\n" + s;
                        mText.setText(text);
                    }
                });

    }

    // endregion

    // region // filter 方法

    private void filter() {

        // region // 资料

        List<String> data = new ArrayList<>();
        data.add("int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};");
        data.add("// filter 返回 true/false");
        data.add("Observable.just(data)");
        data.add("        // 判断 数组 data 的长度大于0");
        data.add("        .filter(i -> i.length > 0)");
        data.add("        // .flatMap((Func1<int[], Observable<String>>) ints -> Observable.from(getStringCom(ints))) // lambda 表达式");
        data.add("        // 转换成 String[] 类型，然后用from 一个一个的读取出来");
        data.add("        .flatMap(new Func1<int[], Observable<String>>() {");
        data.add("            @Override");
        data.add("            public Observable<String> call(int[] ints) {");
        data.add("                return Observable.from(getStringCom(ints));");
        data.add("            }");
        data.add("        })");
        data.add("        // 判断");
        data.add("        .filter(i -> Integer.valueOf(i) % 2 == 0)");
        data.add("        .subscribe(new Action1<String>() {");
        data.add("            String text = \"\";");
        data.add("            @Override");
        data.add("            public void call(String s) {");
        data.add("                text += \"\\n\" + s;");
        data.add("                mText.setText(text);");
        data.add("            }");
        data.add("        });");
        data.add("");
        data.add("");
        data.add("结果：2，4，6，8，0");
        data.add("");
        data.add("");

        // endregion

        Observable.from(data)
                .filter(i -> i != null)
                .subscribe(new Action1<String>() {
                    String text = "";

                    @Override
                    public void call(String s) {
                        text += "\n" + s;
                        mText.setText(text);
                    }
                });

        // region // 方法

        // int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        // // filter 返回 true/false
        // Observable.just(data)
        //         // 判断 数组 data 的长度大于0
        //         .filter(i -> i.length > 0)
        //         // .flatMap((Func1<int[], Observable<String>>) ints -> Observable.from(getStringCom(ints))) // lambda 表达式
        //         // 转换成 String[] 类型，然后用from 一个一个的读取出来
        //         .flatMap(new Func1<int[], Observable<String>>() {
        //             @Override
        //             public Observable<String> call(int[] ints) {
        //                 return Observable.from(getStringCom(ints));
        //             }
        //         })
        //         // 判断
        //         .filter(i -> Integer.valueOf(i) % 2 == 0)
        //         .subscribe(new Action1<String>() {
        //             String text = "";

        //             @Override
        //             public void call(String s) {
        //                 text += "\n" + s;
        //                 mText.setText(text);
        //             }
        //         });

        // endregion


    }

    // endregion

    // region // lift 方法

    private void lift() {
        from();
    }

    // endregion

    // region // compose 方法

    private void compose() {

    }

    // endregion


}



