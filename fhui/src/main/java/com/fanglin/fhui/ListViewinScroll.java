package com.fanglin.fhui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewinScroll extends ListView {
    // TODO 即将被删除
    public ListViewinScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewinScroll(Context context) {
        super(context);
    }

    public ListViewinScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
