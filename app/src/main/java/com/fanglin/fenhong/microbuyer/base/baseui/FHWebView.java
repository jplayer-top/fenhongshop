package com.fanglin.fenhong.microbuyer.base.baseui;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/18-下午3:13.
 * 功能描述:
 */
public class FHWebView extends WebView {

    public FHWebView(Context context) {
        super(context);
        initView(context);
    }

    public FHWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FHWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

    }

    float y1, y2;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                y2 = ev.getY();
                break;
        }

        if (getScrollY() == 0) {
            requestScroll = y2 <= y1;
        }

        requestDisallowInterceptTouchEvent(requestScroll);
        return super.onTouchEvent(ev);
    }


    private boolean requestScroll = true;

    public void setRequestScroll(boolean requestScroll) {
        this.requestScroll = requestScroll;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t == 0) {
            setRequestScroll(false);
        } else {
            setRequestScroll(true);
        }
    }
}
