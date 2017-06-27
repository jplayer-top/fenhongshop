package com.fanglin.fenhong.microbuyer.base.baseui;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Plucky on 2015/9/8.
 * 用于计算在滚动视图中RecycleView的高度
 */
public class AutoGridLayoutManager extends GridLayoutManager {

    private int dividerHeight = 0;

    public AutoGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
    }


    private int itemHeight;

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getItemHeightByView(View view) {
        if (view != null && itemHeight == 0) {
            return view.getMeasuredHeight();
        } else {
            return itemHeight;
        }
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        if (getItemCount() == 0) return;
        View view = recycler.getViewForPosition(0);
        if (view != null) {
            boolean flag = getItemCount() % getSpanCount() == 0;
            int lines = flag ? (getItemCount() / getSpanCount()) : getItemCount() / getSpanCount() + 1;
            measureChild(view, widthSpec, heightSpec);
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
            int aHeight = getItemHeightByView(view) + dividerHeight;

            int measuredHeight = aHeight * lines;
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

}