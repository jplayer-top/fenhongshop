package com.fanglin.fenhong.microbuyer.base.baseui;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午8:54.
 * 功能描述: 可控制是否能滚动的LayoutManager
 */
public class ScrollEnableGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public ScrollEnableGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ScrollEnableGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ScrollEnableGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
