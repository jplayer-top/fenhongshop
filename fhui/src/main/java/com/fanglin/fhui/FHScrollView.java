package com.fanglin.fhui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ScrollView;

/**
 * 分红通用UI库
 * Created by Plucky on 2015/8/22.
 */
public class FHScrollView extends ScrollView {
    private int downY;
    private int mTouchSlop;//禁止ScrollView内的RecycleView滑动事件
    private OnScrollListener onScrollListener;

    private View scroll2TopBtn;//回到顶部的按钮
    private int screenHeight;

    public FHScrollView(Context context) {
        this(context, null);
        initView(context);
    }

    public FHScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }

    public FHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        screenHeight = getDisplayMetrics(context).heightPixels;

        //禁用手机回弹效果  如锤子、魅蓝
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    public void setScrollBtn(View ScrollBtn) {
        this.scroll2TopBtn = ScrollBtn;
        if (scroll2TopBtn != null) {
            scroll2TopBtn.setVisibility(GONE);
            scroll2TopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    smoothScrollTo(0, 0);
                }
            });
        }
    }

    /**
     * 设置滚动接口
     *
     * @param onScrollListener onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(l, t, oldl, oldt);
        }
        if (scroll2TopBtn != null) {
            if (t > screenHeight) {
                scroll2TopBtn.setVisibility(VISIBLE);
            } else {
                scroll2TopBtn.setVisibility(GONE);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (onScrollListener != null) {
            onScrollListener.onTop(t);
        }
    }

    /**
     * 滚动的回调接口
     *
     * @author Plucky
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回FHScrollView滑动的Y方向距离
         */
        void onScroll(int l, int t, int oldl, int oldt);

        void onTop(int top);
    }

    public static DisplayMetrics getDisplayMetrics(Context c) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
