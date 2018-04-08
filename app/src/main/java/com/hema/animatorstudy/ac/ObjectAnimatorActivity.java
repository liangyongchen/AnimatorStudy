package com.hema.animatorstudy.ac;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hema.animatorstudy.R;
import com.hema.baselibrary.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ObjectAnimatorActivity extends BaseActivity implements View.OnClickListener {


    // region //  初始化状态

    @BindView(R.id.alpha)
    TextView mAlpha;
    @BindView(R.id.rotation)
    TextView mRotation;
    @BindView(R.id.translationX)
    TextView mTranslationX;
    @BindView(R.id.translationY)
    TextView mTranslationY;
    @BindView(R.id.scale)
    TextView mScale;
    @BindView(R.id.scaleX)
    TextView mScaleX;
    @BindView(R.id.scaleY)
    TextView mScaleY;
    @BindView(R.id.setGroupAnimator)
    TextView mSetGroupAnimator;
    @BindView(R.id.setGroupAnimator2)
    TextView mSetGroupAnimator2;

    @BindView(R.id.test)
    TextView mTest;
    @BindView(R.id.instruction)
    TextView mInstruction;
    @BindView(R.id.ofFloat)
    TextView mOfFloat;
    @BindView(R.id.ofInt)
    TextView mOfInt;
    @BindView(R.id.outTest)
    TextView outTest;

    // endregion

    // 打印说明动态变化数据
    private StringBuffer uotString = new StringBuffer();
    private StringBuffer instructionString = new StringBuffer();

    // region // 父类初始化

    @Override
    public int getContentView() {
        return R.layout.activity_object_animator;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("ObjectAnimator动画讲解");
    }

    @Override
    public void initEvent() {

        mAlpha.setOnClickListener(this);
        mRotation.setOnClickListener(this);
        mTranslationX.setOnClickListener(this);
        mTranslationY.setOnClickListener(this);
        mScale.setOnClickListener(this);
        mScaleX.setOnClickListener(this);
        mScaleY.setOnClickListener(this);
        mSetGroupAnimator.setOnClickListener(this);
        mSetGroupAnimator2.setOnClickListener(this);
        mOfFloat.setOnClickListener(this);
        mOfInt.setOnClickListener(this);

    }

    // endregion

    // region // 动画方法

    private void alpha() {

        ObjectAnimator anim = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "alpha", 1f, 0f, 1f, 0.5f, 0f, 1f);
        anim.setDuration(3000);
        setObjectAnimatorListener(anim, "淡出淡出状态： alpha，取值范围在 [0,1] \n" + "ofFloat(mTest, \"alpha\", 1f, 0f, 1f, 0.5f, 0f,1f)");
        anim.start();

    }

    private void rotation() {
        ObjectAnimator anim = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "rotation", 0f, 360f, -180f, 270f, 0f);
        anim.setDuration(3000);
        setObjectAnimatorListener(anim, "旋转状态： rotation ，取值范围在 [-n,n] \n" + "ofFloat(mTest, \"rotation\", 0f, 360f, -180f, 270f, 0f)");
        anim.start();
    }

    private void translationX() {
        ObjectAnimator anim = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "translationX", 0f, 400f, -50f, 270f, -150f, 100f, 0f);
        anim.setDuration(3000);
        setObjectAnimatorListener(anim, "X 方向走直线运动： translationX ，取值范围在 [-n,n] \n" + "ofFloat(mTest, \"translationX\", 0f, 400f, -50f, 270f, -150f, 100f, 0f)");
        anim.start();
    }

    private void translationY() {
        ObjectAnimator anim = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "translationY", 0f, 400f, -50f, 270f, -150f, 100f, 0f);
        anim.setDuration(3000);
        setObjectAnimatorListener(anim, "Y 方向走直线运动： translationY ，取值范围在 [-n,n] \n" + "ofFloat(mTest, \"translationX\", 0f, 400f, -50f, 270f, -150f, 100f, 0f)");
        anim.start();

    }

    private void scale() {
        InstructionString("把 X 和 Y 方向的动画一起播放，没有用到组合动画状态\n\n");
        scaleX();
        scaleY();
    }

    private void scaleX() {
        ObjectAnimator anim = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "scaleX", 1f, 3f, 2f, 1f);
        anim.setDuration(3000);
        setObjectAnimatorListener(anim, " X 方向缩放比例： scale ，取值范围在 [-n,n] \n" + "ofFloat(mTest, \"scale\", 1f, 3f, 2f, 1f)");
        anim.start();
    }

    private void scaleY() {
        ObjectAnimator anim = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "scaleY", 1f, 3f, 2f, 1f);
        anim.setDuration(3000);
        setObjectAnimatorListener(anim, "Y 方向缩放比例： scale ，取值范围在 [-n,n] \n" + "ofFloat(mTest, \"scale\", 1f, 3f, 2f, 1f)");
        anim.start();
    }

    private void setGroupAnimator() {

        // 组合动画说明：
        InstructionString("实现组合动画功能主要需要借助AnimatorSet这个类，这个类提供了一个play()方法，如果我们向这个方法中传入一个Animator对象(ValueAnimator或ObjectAnimator)将会返回一个AnimatorSet.Builder的实例，AnimatorSet.Builder中包括以下四个方法：\n" +
                "\n" +
                "after(Animator anim)   将调用该方法的所有动画执行完在执行其他动画\n\n" +
                "after(long delay)   将现有动画延迟指定毫秒后执行\n\n" +
                "before(Animator anim)  将不是调用该方法的所有动画执行完再执行该方法的所有动画\n\n" +
                "with(Animator anim)   将该方法的所有动画和play()同步执行 \n\n");

        // 组合动画

        ObjectAnimator anim1 = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "translationX", 0f, 400f, -50f, 270f, -150f, 100f, 0f);
        setObjectAnimatorListener(anim1, "X 方向走直线运动： translationX ，调用 AnimatorSet的play()方法");

        ObjectAnimator anim2 = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "translationY", 0f, 400f, -50f, 270f, -150f, 100f, 0f);
        setObjectAnimatorListener(anim2, "Y 方向走直线运动： translationY ，调用 AnimatorSet的 with()方法");

        ObjectAnimator anim3 = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "rotation", 0f, 360f, -180f, 270f, 0f);
        setObjectAnimatorListener(anim3, "旋转状态： rotation ，调用 AnimatorSet的 after()方法");

        ObjectAnimator anim4 = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "scaleX", 1f, 3f, 2f, 1f);
        setObjectAnimatorListener(anim4, " X 方向缩放比例： scale ，调用 AnimatorSet的 after()方法");

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2).after(anim3).before(anim4);
        animSet.setDuration(5000);
        animSet.start();

    }

    private void setGroupAnimator2() {

        // 组合动画说明：
        InstructionString("实现组合动画功能主要需要借助AnimatorSet这个类，这个类提供了一个play()方法，如果我们向这个方法中传入一个Animator对象(ValueAnimator或ObjectAnimator)将会返回一个AnimatorSet.Builder的实例，AnimatorSet.Builder中包括以下四个方法：\n" +
                "\n" +
                "after(Animator anim)   将现有动画插入到传入的动画之后执行\n\n" +
                "after(long delay)   将现有动画延迟指定毫秒后执行\n\n" +
                "before(Animator anim)   将现有动画插入到传入的动画之前执行\n\n" +
                "with(Animator anim)   将现有动画和传入的动画同时执行 \n\n");

        // 组合动画

        ObjectAnimator anim1 = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "translationX", 0f, 400f, -50f, 270f, -150f, 100f, 0f);
        setObjectAnimatorListener(anim1, "X 方向走直线运动： translationX ，调用 AnimatorSet的play()方法");

        ObjectAnimator anim2 = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "translationY", 0f, 400f, -50f, 270f, -150f, 100f, 0f);
        setObjectAnimatorListener(anim2, "Y 方向走直线运动： translationY ，调用 AnimatorSet的 with()方法");

        ObjectAnimator anim3 = ObjectAnimator
                // ofFloat(Object target, String propertyName, float... values)
                .ofFloat(mTest, "rotation", 0f, 360f, -180f, 270f, 0f);
        setObjectAnimatorListener(anim3, "旋转状态： rotation ，调用 AnimatorSet的 with()方法");

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2).with(anim3);
        animSet.setDuration(5000);
        animSet.start();

    }

    private void ofFloat() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        // 监听变化状态
        setValueAnimatorListener(anim, "没有添加控件状态下测试ofInt");
        anim.start();
    }

    private void ofInt() {
        ValueAnimator anim = ValueAnimator.ofInt(0, 100);
        anim.setDuration(300);
        setValueAnimatorListener(anim, "没有添加控件状态下测试 ofInt 方法");
        anim.start();
    }

    // endregion

    // region // 公用动画监听器
    private void setValueAnimatorListener(ValueAnimator animator, String instruction) {

        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                InstructionString(instruction + "\n\n 默认是加减速插值器 \n\n");
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                TestString(String.valueOf(animation.getAnimatedValue()));
            }
        });


    }

    private void setObjectAnimatorListener(ObjectAnimator animator, String instruction) {

        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                InstructionString(instruction + "\n\n 默认是加减速插值器 \n\n");
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                TestString(String.valueOf(animation.getAnimatedValue()));
            }
        });
    }

    // region // 输出方法

    public void TestString(String str) {

        uotString.append(str + "\n");
        outTest.setText(uotString.toString());

    }

    public void InstructionString(String str) {

        instructionString.append(str + "\n");
        mInstruction.setText(instructionString.toString());

    }


    // endregion


    // region // 点击事件

    @Override
    public void onClick(View v) {

        // 清空
        uotString = new StringBuffer("");
        outTest.setText("");
        instructionString = new StringBuffer("");
        mInstruction.setText("");

        switch (v.getId()) {

            case R.id.alpha:
                alpha();
                break;

            case R.id.rotation:
                rotation();
                break;

            case R.id.translationX:
                translationX();
                break;

            case R.id.translationY:
                translationY();
                break;

            case R.id.scale:
                scale();
                break;

            case R.id.scaleX:
                scaleX();
                break;

            case R.id.scaleY:
                scaleY();
                break;

            case R.id.setGroupAnimator:
                setGroupAnimator();
                break;

            case R.id.setGroupAnimator2:
                setGroupAnimator2();
                break;

            case R.id.ofFloat:
                ofFloat();
                break;

            case R.id.ofInt:
                ofInt();
                break;

        }

    }


    // endregion

}
