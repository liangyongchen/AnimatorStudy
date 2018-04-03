自定义控件学习
======
## 高仿QQ侧滑item空间
#:2018.3.28
 ![](art/SideslipMenuLayout.gif)
## 描述

- SideslipMenuLayout类
- onMeasure 方法测量每个子控件的尺寸，从第二个item开始获取宽度，对比每个item的高度最大值来获取
>    @Override
>    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
>        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
>
>        setClickable(true); // 令自己可点击，从而获取触摸事件,不设置只能进行点击事件
>        int childCount = getChildCount(); // 获取容器的所有子类
>        int parentWidth = 0, parentHeight = 0; // 默认父容器的宽度为0，高度为0
>        int childMaxHeight = 0;
>        rightMoveMaxLength = 0;
>
>        for (int i = 0; i < childCount; i++) {
>
>            View childView = getChildAt(i);
>            childView.setClickable(true); // 设置每个子类都可以触发点击事件
>
>            if (childView.getVisibility() != GONE) {
>
>                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
>
>                childMaxHeight = Math.max(childMaxHeight, childView.getMeasuredHeight()); //  获取子类最大的高度
>
>                // 以第二个视图开始计算能向右边滑动的最大距离
>                if (i > 0) {
>                    rightMoveMaxLength += childView.getMeasuredWidth();
>                }
>            }
>        }
>        parentWidth = getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
>       parentHeight = childMaxHeight + getPaddingBottom() + getPaddingTop();
>        setMeasuredDimension(parentWidth, parentHeight);
>    }
- onLayout给每个item在父布局的布局位置
>    @Override
>    protected void onLayout(boolean changed, int l, int t, int r, int b) {
>
>        int childCount = getChildCount();
>        int left = getPaddingLeft(); // 获取父控件的 padding 确定子控件的位置
>
>        for (int i = 0; i < childCount; i++) {
>
>            View childView = getChildAt(i);
>
>            if (childView.getVisibility() != GONE) {
>
>                // 第一个控件在xml布局默认为 match_parent
>                childView.layout(
>                        left + 0,
>                        getPaddingTop() + 0,
>                        left + childView.getMeasuredWidth(),
>                        getPaddingTop() + childView.getMeasuredHeight());
>                left += childView.getMeasuredWidth();
>            }
>        }
>    }
- PointF使用记录每次按下的点，记录每次滑动的时候更换的点
> case MotionEvent.ACTION_DOWN:
>                // 设置按下的的 x y 位置，记录起来
>                mFirstPointF.set(ev.getRawX(), ev.getRawY());
>                mLastPointF.set(ev.getRawX(), ev.getRawY());
>                if (mSideslipMenuLayout != null) {
>                    mSideslipMenuLayout.closeAnimator();
>                }
>                break;
>
>            case MotionEvent.ACTION_MOVE:
>
>                float RelativeMove = mLastPointF.x - ev.getRawX(); // 相对移动的距离
>
>                scrollBy((int) RelativeMove, 0); // 平滑的移动
>
>                mLastPointF.set(ev.getRawX(), ev.getRawY());
>
>                // 判断移动的距离是否越界，以 left = 0 ,right = 屏幕宽为临界值
>                if (getScrollX() < 0) {
>                    scrollTo(0, 0);
>                }
>
>                if (getScrollX() > rightMoveMaxLength) {
>                    scrollTo(rightMoveMaxLength, 0); // 设置 getScrollX 最大值是 rightMoveMaxLength
>                }
>
>                break;

- 使用scrollBy（x,y）平滑移动item，scrollTo（x,y）返回初始状态
- 属性动画ValueAnimator，加速插值器AccelerateInterpolator
>     // 关闭展开item的动画
>     private void closeAnimator() {
>         ...
>        // 从当前位置返回到0： -n -->  0  <-- n  正负数都返回0位置
>        mCloseAnimator = ValueAnimator.ofInt(getScrollX(), 0);
>        // 监听动画更新状态，设置 chiildView 返回的位置
>        mCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
>            @Override
>            public void onAnimationUpdate(ValueAnimator animation) {
>                scrollTo((int) animation.getAnimatedValue(), 0);
>            }
>        });
>        mCloseAnimator.setInterpolator(new AccelerateInterpolator()); // 设置加速插值器
>        mCloseAnimator.setDuration(250).start(); // 设置时常开始执行动画
>     }

>     // 展开item的动画
>     private void expandAnimator() {
>         ...
>         // 从当前位置返回到0： -n -->  0  <-- n  正负数都返回0位置
>         mExpandAnimator = ValueAnimator.ofInt(getScrollX(), rightMoveMaxLength);
>         // 监听动画更新状态，设置 chiildView 返回的位置
>         mExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
>             @Override
>             public void onAnimationUpdate(ValueAnimator animation) {
>                 scrollTo((int) animation.getAnimatedValue(), 0);
>             }
>         });
>         mExpandAnimator.setInterpolator(new AccelerateInterpolator()); // 设置加速插值器
>         mExpandAnimator.setDuration(250).start(); // 设置时常开始执行动画
>     }


## dispatchTouchEvent、onInterceptTouchEvent、onTouchEvent分发拦截事件处理
#:2018.4.3
 ![](art/SideslipMenuLayout.gif)
 
 Activity:
 
 https://github.com/liangyongchen/AnimatorStudy/blob/master/app/src/main/java/com/hema/animatorstudy/ac/TouchEventActivity.java

dispatchTouchEvent、onInterceptTouchEvent、onTouchEvent分发拦截事件处理说明

 https://www.jianshu.com/p/fb84dceb1600










