package com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.fanglin.fhui.FHScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/31-上午11:18.
 * 功能描述:Android-PullToRefresh FHScrollView
 */
public class PullToRefreshFHScrollView extends PullToRefreshBase<FHScrollView> {
    public PullToRefreshFHScrollView(Context context) {
        super(context);
    }

    public PullToRefreshFHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshFHScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshFHScrollView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected FHScrollView createRefreshableView(Context context, AttributeSet attrs) {
        FHScrollView fhScrollView = new FHScrollView(context, attrs);
        return fhScrollView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return getRefreshableView().getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View scrollViewChild = getRefreshableView().getChildAt(0);
        if (null != scrollViewChild) {
            return getRefreshableView().getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

    /**
     * 往上滑动一点点
     *
     * @param millonSecond 毫秒数
     */
    public void onAppendDateWithDelay(long millonSecond) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onAppendData();
            }
        }, millonSecond);
    }

    public void onAppendData() {
        getRefreshableView().smoothScrollBy(0, 100);
    }

}
