package com.fanglin.fenhong.microbuyer.base.baseui;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.fanglin.fhlib.other.FHLog;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/14-上午9:05.
 * 功能描述:分割线
 */
public class MutiItemDecoration extends RecyclerView.ItemDecoration {

    public enum Type {
        VERTICAL, HORIZONTAL, ALL
    }

    private Type type;//分割线类型
    private int dividerSize = 10;//分割线尺寸


    public MutiItemDecoration(MutiItemDecoration.Type type, int dividerSize) {
        this.type = type;
        this.dividerSize = dividerSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);

        switch (type) {
            case ALL:
                if (listener != null) {
                    listener.onGetOffsets(outRect, itemPosition);
                } else if (sectionListener != null) {
                    Object aTag = view.getTag();//给itemView 设置Tag 来区分分区的概念
                    if (aTag != null && TextUtils.isDigitsOnly(aTag.toString())) {
                        sectionListener.onGetSectionOffsets(outRect, itemPosition, Integer.valueOf(aTag.toString()));
                    }
                } else {
                    /**
                     * 源代码在spansize不一样的情况下存在BUG
                     * 所以简单粗暴地设置成这样
                     * Added By Plucky
                     */
                    outRect.set(dividerSize / 2, dividerSize / 2, dividerSize / 2, dividerSize / 2);
                }
                break;
            case VERTICAL:
                if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0, 0, dividerSize);
                }
                break;
            case HORIZONTAL:
                if (isLastColum(parent, itemPosition, spanCount, childCount)) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0, dividerSize, 0);
                }
                break;
        }
    }

    // 是否是最后一列
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)
                return true;
        } else {
            if (pos == childCount - 1)
                return true;
        }
        return false;
    }

    // 是否是最后一行
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)
                return true;
        } else {
            if (pos == childCount - 1)
                return true;
        }
        return false;
    }


    //返回列数
    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return -1;
    }

    private DecorationListener listener;
    private DecorationSectionListener sectionListener;

    public void setListener(DecorationListener listener) {
        this.listener = listener;
    }

    public void setSectionListener(DecorationSectionListener sectionListener) {
        this.sectionListener = sectionListener;
    }

    public interface DecorationListener {
        void onGetOffsets(Rect outRect, int position);
    }

    /**
     * 这个接口设计的目的是为了获取分区中商品的间隔处理
     */
    public interface DecorationSectionListener {
        /**
         * 获取带分区的Offsets
         *
         * @param outRect  Rect
         * @param position int
         * @param index    int
         */
        void onGetSectionOffsets(Rect outRect, int position, int index);
    }
}
