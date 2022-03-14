//package com.example.plana.view;
//
//import android.animation.Animator;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Color;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.view.ViewGroup;
//import android.view.ViewPropertyAnimator;
//import android.view.animation.DecelerateInterpolator;
//import android.view.animation.LinearInterpolator;
//import android.widget.LinearLayout;
//import android.widget.Scroller;
//
//import androidx.annotation.Nullable;
//import androidx.core.widget.NestedScrollView;
//
//import com.example.plana.R;
//
///**
// * @program: PlanA
// * @description: pull down to add a Thing
// */
//public class PullToAddView extends LinearLayout {
//
//    private Scroller mScroller;         // 滑动器
//    private NestedScrollView nestedScrollView;
//
//    private float mLastY = 0f;      // 手指按下的y轴坐标值
//    private float mLastX = 0f;      // 手指按下的y轴坐标值
//    private boolean mTouchEvent;        // 是否拦截点击事件
//    private float scrollYValue = 0f;    // 手指 Y 轴滑动的距离
//    private double decayRatio = 0.5;    // 阻尼系数
//    private TouchStateMove stateMove = TouchStateMove.NORMAL;   // 手势滑动状态
//
//    private View addView;               // 顶部view的add按钮
//    private int subViewHeight = 0;      // 顶部view的height
//    private int mPaddingBottom = 0;     // 顶部view的paddingBottom
//    private int mPaddingTop = 0;        // 顶部view的PaddingTop
//    private LinearLayout topLayout;     // 顶部view的父控件
//    private int topBackGroundColor;     // 顶部view的背景颜色
//    private ViewState stateView = ViewState.CLOSE;  // 顶部view的显示状态,默认是关闭状态
//
//    // TODO add PullFreshView
//    private FreshView freshView;        // 刷新列表的 View
//
//
//    /**
//     * 触摸状态
//     * DOWN_NO_OVER 向下滑动但是没有超出view的height值
//     * DOWN_OVER 向下滑动并且超出了height值
//     * UP 向上滑动
//     * NORMAL 无状态
//     */
//    enum TouchStateMove {
//        DOWN_NO_OVER, DOWN_OVER, UP, NORMAL
//    }
//
//
//    /**
//     * 顶部view的显示状态
//     * CLOSE 顶部为关闭
//     * OPEN 顶部为打开状态
//     */
//    enum ViewState {
//        CLOSE, OPEN
//    }
//
//
//    public PullToAddView(Context context) {
//        super(context);
//    }
//
//    public PullToAddView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs);
//    }
//
//    private void init(AttributeSet attrs) {
//        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
//
//        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PullToAddView);
//        topBackGroundColor = a.getColor(R.styleable.PullToAddView_top_background_color, Color.WHITE);
//        a.recycle();
//
//        setOrientation(VERTICAL);
//    }
//
//    /**
//     * 添加头部view中的Add按钮
//     */
//    public View addHeadView(int resLayout) {
//        addView = View.inflate(getContext(), resLayout, null);
//        addView.setVisibility(INVISIBLE);
//        buildView();
//        return addView;
//    }
//
//    /**
//     * 构建整个view布局
//     */
//    private void buildView() {
//        //子布局第一个必须为NestedScrollView
//        nestedScrollView = (NestedScrollView) getChildAt(0);
//
//        topLayout = new LinearLayout(getContext());
////        topLayout.setClickable(true);
//        topLayout.setBackgroundColor(topBackGroundColor);
//        topLayout.setOrientation(VERTICAL);
//        topLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                subViewHeight = topLayout.getHeight();
//                mPaddingTop = -subViewHeight;
//                int paddingLeft = topLayout.getPaddingLeft();
//                int paddingRight = topLayout.getPaddingRight();
//                mPaddingBottom = topLayout.getPaddingTop();
//                topLayout.setPadding(paddingLeft, mPaddingTop, paddingRight, mPaddingBottom);
//            }
//        });
//
//        topLayout.addView(addView);
//
//        freshView = new FreshView(getContext());
//        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 50);
//        topLayout.addView(freshView, layoutParams);
//        addView(topLayout, 0);
//    }
//
//
//    /**
//     * 处理触摸拦截事件
//     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            // 按下
//            case MotionEvent.ACTION_DOWN:
//                mLastX = ev.getX();
//                mLastY = ev.getY();
//                mTouchEvent = false;
//                break;
//
//            // 滑动
//            case MotionEvent.ACTION_MOVE:
//                float flX = ev.getX() - mLastX;
//                float flY = ev.getY() - mLastY;
//                float abs = Math.abs(flY);
//                // 手指滑动的阈值
//                int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledMaximumDrawingCacheSize();
//                if (abs > scaledTouchSlop) {
//                    mTouchEvent = (nestedScrollView.getScrollY() == 0);
//                    if (flY < 0 && stateView == ViewState.CLOSE) {
//                        mTouchEvent = false;
//                    }
//                    if (stateView == ViewState.OPEN) {
//                        if (Math.abs(flX) > abs) {
//                            mTouchEvent = false;
//                        }
//                        //顶部区域打开后不消费事件
//                        if (mLastY < subViewHeight) {
//                            mTouchEvent = false;
//                        }
//                    }
//                }
//                break;
//
//            // 抬起
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mTouchEvent = false;
//                break;
//        }
//        return mTouchEvent;
//    }
//
//
//    /**
//     * 触摸事件
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            // 按下
//            case MotionEvent.ACTION_DOWN:
//                mTouchEvent = true;
//                break;
//
//            // 滑动
//            case MotionEvent.ACTION_MOVE:
//                scrollYValue = (event.getY() - mLastY);
//                float abs = Math.abs(scrollYValue);
//                int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
//
//                // 移动的距离 大于 手指滑动阈值
//                if (abs > scaledTouchSlop) {
//                    mTouchEvent = true;
//                    if (scrollYValue > 0) {
//                        if (nestedScrollView.getScrollY() == 0) {
//                            if (stateView == ViewState.CLOSE) {
//                                // 向下滑动但是头部空间没完全显示
//                                if (mPaddingTop < 0) {
//                                    mPaddingTop = (int) (decayRatio * scrollYValue - subViewHeight);
//                                    topLayout.setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), mPaddingBottom);
//                                    stateMove = TouchStateMove.DOWN_NO_OVER;
//                                    dotView.setPercent(1 - ((float) mPaddingTop / (-subViewHeight)));
//                                    if (mPaddingTop > -subViewHeight / 2) {
//                                        showToDown(addView, (long) 200);
//                                    }
//                                    addView.setAlpha(1 - ((float) mPaddingTop / (-subViewHeight)));
//                                }
//                                // 头部空间没完全显示依然向下滑动
//                                else if (mPaddingTop >= 0) {
//                                    mPaddingTop = (int) (0.5 * decayRatio * scrollYValue + 0.5 * (-subViewHeight));
//                                    topLayout.setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), mPaddingTop);
//                                    stateMove = TouchStateMove.DOWN_OVER;
//                                    dotView.setPercent(1 - ((float) mPaddingTop / (-subViewHeight)));
//                                    addView.setAlpha(1 - ((float) mPaddingTop / (-subViewHeight)));
//                                }
//                            } else {
//                                mPaddingTop = (int) (0.5 * decayRatio * scrollYValue);
//                                topLayout.setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), mPaddingTop);
//                                stateMove = TouchStateMove.DOWN_OVER;
//                            }
//                        }
//                    } else {
//                        if (nestedScrollView.getScrollY() == 0) {
//                            if (stateView == ViewState.CLOSE) {
//                                mPaddingTop = -subViewHeight;
//                            } else {
//                                mPaddingTop = (int) (decayRatio * scrollYValue);
//                                if (mPaddingTop <= -subViewHeight) {
//                                    topLayout.setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), mPaddingBottom);
//                                    mPaddingTop = -subViewHeight;
//                                    stateView = ViewState.CLOSE;
//                                } else {
//                                    topLayout.setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), mPaddingBottom);
//                                    stateMove = TouchStateMove.UP;
//                                }
//                            }
//                        }
//                    }
//                }
//                break;
//
//            // 抬起 / 取消
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                if (mPaddingTop > -subViewHeight / 3 && mPaddingTop < 0 && stateMove == TouchStateMove.DOWN_NO_OVER) {
//                    moveAnimation(-mPaddingTop, mPaddingTop);
//                    stateView = ViewState.OPEN;
//                    dotHideAnim();
//                }
//                if (mPaddingTop <= -subViewHeight / 3 && mPaddingTop < 0 && stateMove == TouchStateMove.DOWN_NO_OVER) {
//                    moveAnimation(-mPaddingTop, subViewHeight);
//                    stateView = ViewState.CLOSE;
//                    addView.setVisibility(View.INVISIBLE);
//                    dotView.setAlpha(1.0f);
//                    dotView.setVisibility(View.VISIBLE);
//                }
//                if (stateMove == TouchStateMove.DOWN_OVER) {
//                    moveAnimation(-mPaddingTop, mPaddingTop);
//                    stateView = ViewState.OPEN;
//                    dotHideAnim();
//                }
//                if (stateMove == TouchStateMove.UP) {
//                    moveAnimation(-mPaddingTop, subViewHeight);
//                    addView.setVisibility(View.INVISIBLE);
//                    stateView = ViewState.CLOSE;
//                    dotView.setAlpha(1.0f);
//                    dotView.setVisibility(View.VISIBLE);
//                }
//                mTouchEvent = false;
//                scrollYValue = 0f;
//                mLastY = 0f;
//                // 返回监听回调
//                if (listener != null) {
//                    listener.onViewState(stateView);
//                }
//                break;
//        }
//        return mTouchEvent;
//    }
//
//    /**
//     * view滚动回弹动画
//     */
//    private void moveAnimation(int startY, int y) {
//        mScroller.startScroll(0, startY, 0, y, 400);
//        invalidate();
//    }
//
//    @Override
//    public void computeScroll() {
//        if (mScroller.computeScrollOffset()) {
//            int currY = mScroller.getCurrY();
//            topLayout.setPadding(getPaddingLeft(), -currY, getPaddingRight(), mPaddingBottom);
//        }
//        invalidate();//刷新view
//    }
//
//
//    /**
//     * 顶部view向下平移动画
//     *
//     * @param view
//     * @param time 动画时间
//     */
//    private void showToDown(final View view, Long time) {
//        if (view.getVisibility() != View.VISIBLE) {
//            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationY", -30f, 0f);
//            //渐变动画
//            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.playTogether(animator1, alphaAnimator);
//            animatorSet.setDuration(time);
//            animatorSet.setInterpolator(new LinearInterpolator());
//            animatorSet.start();
//            animatorSet.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    view.setVisibility(VISIBLE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    view.setVisibility(VISIBLE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//                }
//            });
//        }
//    }
//
//    /**
//     * 小圆点的隐藏动画
//     */
//    private void dotHideAnim() {
//        ViewPropertyAnimator alpha = dotView.animate().alpha(0f);
//        alpha.setDuration(400);
//        alpha.start();
//        dotView.setVisibility(View.GONE);
//        topLayout.setAlpha(1f);
//    }
//
//
//    private ViewStateListener listener;
//
//    public void setViewStateListener(ViewStateListener listener) {
//        this.listener = listener;
//    }
//
//    public interface ViewStateListener {
//        void onViewState(ViewState viewState);
//    }
//
//
//}
