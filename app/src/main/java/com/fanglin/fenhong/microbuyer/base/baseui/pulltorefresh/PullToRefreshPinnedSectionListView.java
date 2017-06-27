package com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hb.views.PinnedSectionListView;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/31-上午11:18.
 * 功能描述:Android-PullToRefresh PullToRefreshPinnedSectionListView
 */
public class PullToRefreshPinnedSectionListView extends PullToRefreshBase<PinnedSectionListView> {
    public PullToRefreshPinnedSectionListView(Context context) {
        super(context);
    }

    public PullToRefreshPinnedSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshPinnedSectionListView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshPinnedSectionListView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected PinnedSectionListView createRefreshableView(Context context, AttributeSet attrs) {
        return new PinnedSectionListView(context, attrs);
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullStart() {
        return isFirstItemVisible();
    }

    private boolean isFirstItemVisible() {
        final Adapter adapter = getRefreshableView().getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;

        } else {
            if (getRefreshableView().getFirstVisiblePosition() <= 1) {
                final View firstVisibleChild = getRefreshableView().getChildAt(0);
                if (firstVisibleChild != null) {
                    return firstVisibleChild.getTop() >= getRefreshableView().getTop();
                }
            }
        }

        return false;
    }

    private boolean isLastItemVisible() {
        final Adapter adapter = getRefreshableView().getAdapter();
        if (null == adapter || adapter.isEmpty()) {
            return true;
        } else {
            final int lastItemPosition = getRefreshableView().getCount() - 1;
            final int lastVisiblePosition = getRefreshableView().getLastVisiblePosition();
            if (lastVisiblePosition >= lastItemPosition) {
                final int childIndex = lastVisiblePosition - getRefreshableView().getFirstVisiblePosition();
                final View lastVisibleChild = getRefreshableView().getChildAt(childIndex);
                if (lastVisibleChild != null) {
                    return lastVisibleChild.getBottom() <= getRefreshableView().getBottom();
                }
            }
        }

        return false;
    }

    public void onAppendData(int distance) {
        getRefreshableView().smoothScrollBy(distance, 200);
    }
}
