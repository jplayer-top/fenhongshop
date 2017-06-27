package it.carlom.stikkyheader.core;


import android.content.Context;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

public class StikkyHeaderListView extends StikkyHeader {

    private final ListView mListView;
    private AbsListView.OnScrollListener mDelegateOnScrollListener;

    StikkyHeaderListView(final Context context, final ListView listView, final View header, final int minHeightHeader, final HeaderAnimator headerAnimator) {
        super(context, listView, header, minHeightHeader, headerAnimator);
        this.mListView = listView;
    }

    protected void init() {
        createFakeHeader();
        measureHeaderHeight();
        setupAnimator();
        setStickyOnScrollListener();
    }


    protected void createFakeHeader() {

        mFakeHeader = new Space(mContext);

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mFakeHeader.setLayoutParams(lp);

        mListView.addHeaderView(mFakeHeader);
    }

    private void setStickyOnScrollListener() {

        StickyOnScrollListener mStickyOnScrollListener = new StickyOnScrollListener();
        mListView.setOnScrollListener(mStickyOnScrollListener);

        //通过设定Tag来获取OnScrollListener --fix By Plucky
        mListView.setTag(mStickyOnScrollListener);
    }

    private class StickyOnScrollListener implements AbsListView.OnScrollListener {

        private int mScrolledYList = 0;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mDelegateOnScrollListener != null) {
                mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            mScrolledYList = -calculateScrollYList();

            //notify the animator
            mHeaderAnimator.onScroll(mScrolledYList);

            if (mDelegateOnScrollListener != null) {
                mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }

        private int calculateScrollYList() {
            /*
             * 判断是否为XListView added by Plucky
             * 因为XListView 带有刷新头 所以在计算高度的时候出错了
             * 特此修改此BUG
             */

            View c = mListView.getChildAt(0);
            if (c == null) {
                return 0;
            }


            /*下拉刷新出现刷新头的处理方式*/
            String headrcls = c.getClass().getSimpleName().toLowerCase();
            Log.d("StikkyHeaderListView", headrcls);
            if (TextUtils.equals("xlistviewheader", headrcls)) {
                //如果是XListView
                View h = mListView.getChildAt(1);
                if (h != null) {
                    return -h.getTop();
                }
            }



            int firstVisiblePosition = mListView.getFirstVisiblePosition();

            /*
             * 在存在刷新头的情况下 firstVisiblePosition==1 时，其实还属于头部
             * 而在没有刷新头的情况下 firstVisiblePosition==1 时，已经进入内容区域了
             * */
            if (TextUtils.equals("space", headrcls) && firstVisiblePosition == 1) {
                return -c.getTop();
            }


            int headerHeight = 0;
            if (firstVisiblePosition >= 1) {
                headerHeight = mHeightHeader;
            }

            return -c.getTop() + firstVisiblePosition * c.getMeasuredHeight() + headerHeight;
        }


    }

    public void setOnScrollListener(final AbsListView.OnScrollListener onScrollListener) {
        mDelegateOnScrollListener = onScrollListener;
    }


}
