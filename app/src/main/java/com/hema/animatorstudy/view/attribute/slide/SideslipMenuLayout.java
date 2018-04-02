package com.hema.animatorstudy.view.attribute.slide;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by liangyongchen on 2018/3/27.
 * 继承ViewGroup必须要实现onMeasure方法和onLayout方法
 */

public class SideslipMenuLayout extends ViewGroup {


    private PointF mFirstPointF = new PointF(); // 记录按下的时候记录的点（x，y）

    private PointF mLastPointF = new PointF();  // 记录滑动最后记录的点（x，y）

    private int rightMoveMaxLength = 0; // 除开第一个子视图后的宽度添加后面所有子视图的宽度

    private static SideslipMenuLayout mSideslipMenuLayout;

    // region // 初始化

    public SideslipMenuLayout(Context context) {
        super(context);
    }

    public SideslipMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideslipMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // endregion


    // 测量子控件的方法，用来整理设置父空间的长宽
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d("onMeasure", "onMeasure====000");

        setClickable(true); // 令自己可点击，从而获取触摸事件,不设置只能进行点击事件

        int childCount = getChildCount(); // 获取容器的所有子类

        int parentWidth = 0, parentHeight = 0; // 默认父容器的宽度为0，高度为0

        int childMaxHeight = 0;

        rightMoveMaxLength = 0;

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            childView.setClickable(true); // 设置每个子类都可以触发点击事件

            if (childView.getVisibility() != GONE) {

                measureChild(childView, widthMeasureSpec, heightMeasureSpec);

                childMaxHeight = Math.max(childMaxHeight, childView.getMeasuredHeight()); //  获取子类最大的高度

                // 以第二个视图开始计算能向右边滑动的最大距离
                if (i > 0) {
                    rightMoveMaxLength += childView.getMeasuredWidth();
                }
            }
        }

        parentWidth = getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
        parentHeight = childMaxHeight + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(parentWidth, parentHeight);

    }


    // 子控件位置摆放设置管理
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();

        int left = getPaddingLeft(); // 获取父控件的 padding 确定子控件的位置

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);

            if (childView.getVisibility() != GONE) {

                // 第一个控件在xml布局默认为 match_parent
                childView.layout(
                        left + 0,
                        getPaddingTop() + 0,
                        left + childView.getMeasuredWidth(),
                        getPaddingTop() + childView.getMeasuredHeight());
                left += childView.getMeasuredWidth();

            }
        }

    }

    /**
     * android View/ViewGroup的生命周期-自定义
     * view:onAttachToWindow —>onMeasure —>onSizeChanged —>onLayout —>(onDraw)dispatchDraw —>
     * onMeasure —>onLayout —>(onDraw)dispatchDraw —>onDetachedFromWindow(脱离窗户)
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimator();
    }

    // region // 触摸事件

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                // 设置按下的的 x y 位置，记录起来
                mFirstPointF.set(ev.getRawX(), ev.getRawY());
                mLastPointF.set(ev.getRawX(), ev.getRawY());
                if (mSideslipMenuLayout != null) {
                    mSideslipMenuLayout.closeAnimator();
                }
                break;

            case MotionEvent.ACTION_MOVE:

                float RelativeMove = mLastPointF.x - ev.getRawX(); // 相对移动的距离

                scrollBy((int) RelativeMove, 0); // 平滑的移动

                mLastPointF.set(ev.getRawX(), ev.getRawY());

                // 判断移动的距离是否越界，以 left = 0 ,right = 屏幕宽为临界值
                if (getScrollX() < 0) {
                    scrollTo(0, 0);
                }

                if (getScrollX() > rightMoveMaxLength) {
                    scrollTo(rightMoveMaxLength, 0); // 设置 getScrollX 最大值是 rightMoveMaxLength
                }

                break;

            // 操作控件结束就开始执行动画
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (getScrollX() < rightMoveMaxLength * 4 / 10) {
                    closeAnimator();
                } else {
                    expandAnimator();
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d("onInterceptTouchEvent", "按下");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d("onInterceptTouchEvent", "移动");
                break;

            case MotionEvent.ACTION_UP:
                Log.d("onInterceptTouchEvent", "抬起");
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d("onInterceptTouchEvent", "取消");
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d("onTouchEvent", "按下");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d("onTouchEvent", "移动");

                break;

            case MotionEvent.ACTION_UP:
                Log.d("onTouchEvent", "抬起");
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d("onTouchEvent", "取消");
                break;
        }

        return super.onTouchEvent(ev);
    }

    // endregion


    // region  // 动画

    // 属性动画
    private ValueAnimator mCloseAnimator;  //  操作结束的时候执行的动画
    private ValueAnimator mExpandAnimator; //  滑动展开的时候执行的动画

    // 动画操作结束的时候移动小于 rightMoveMaxLength * 4 / 10 就关闭
    private void closeAnimator() {

        // 先取消动画在设置
        cancelAnimator();
        // 从当前位置返回到0： -n -->  0  <-- n  正负数都返回0位置
        mCloseAnimator = ValueAnimator.ofInt(getScrollX(), 0);
        // 监听动画更新状态，设置 chiildView 返回的位置
        mCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((int) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnimator.setInterpolator(new AccelerateInterpolator()); // 设置加速插值器
        mCloseAnimator.setDuration(250).start(); // 设置时常开始执行动画

    }

    // 动画操作结束的时候移动大于 rightMoveMaxLength * 4 / 10 就展开
    private void expandAnimator() {

        mSideslipMenuLayout = SideslipMenuLayout.this; // 记录当前view，当执行下一个view的时候就关闭当前的view做准备

        // 先取消动画在设置
        cancelAnimator();
        // 从当前位置返回到0： -n -->  0  <-- n  正负数都返回0位置
        mExpandAnimator = ValueAnimator.ofInt(getScrollX(), rightMoveMaxLength);
        // 监听动画更新状态，设置 chiildView 返回的位置
        mExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((int) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnimator.setInterpolator(new AccelerateInterpolator()); // 设置加速插值器
        mExpandAnimator.setDuration(250).start(); // 设置时常开始执行动画

    }

    // 取消动画
    private void cancelAnimator() {

        // 判断如果不为null和动画在运行的时候就取消
        if (mCloseAnimator != null && mCloseAnimator.isRunning()) {
            mCloseAnimator.cancel();
        }

        if (mExpandAnimator != null && mExpandAnimator.isRunning()) {
            mExpandAnimator.cancel();
        }

    }

    // endregion

}
