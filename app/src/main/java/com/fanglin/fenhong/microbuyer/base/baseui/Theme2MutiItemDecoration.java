package com.fanglin.fenhong.microbuyer.base.baseui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/7-下午4:16.
 * 功能描述: 指定不画切割线的viewType
 */
public class Theme2MutiItemDecoration extends MutiItemDecoration {

    private int[] notDrawViewType;

    public Theme2MutiItemDecoration(Type type, int dividerSize) {
        super(type, dividerSize);
    }

    public void setNotDrawViewType(int... viewType) {
        this.notDrawViewType = viewType;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        int viewType = parent.getAdapter().getItemViewType(itemPosition);
        if (containsViewType(viewType)) {
            outRect.set(0, 0, 0, 0);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    private boolean containsViewType(int viewType) {
        if (notDrawViewType != null && notDrawViewType.length > 0) {
            for (int atype : notDrawViewType) {
                if (atype == viewType) {
                    return true;
                }
            }
        }

        return false;
    }
}
