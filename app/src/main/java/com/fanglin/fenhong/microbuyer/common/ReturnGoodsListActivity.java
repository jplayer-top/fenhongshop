package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.MessageNum;
import com.fanglin.fenhong.microbuyer.base.model.ReturnGoodsList;
import com.fanglin.fenhong.microbuyer.common.adapter.ReturnGoodsListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 * 退货商品列表 Activity
 */
public class ReturnGoodsListActivity extends BaseFragmentActivityUI implements ReturnGoodsList.RGLModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    ReturnGoodsList req;
    ReturnGoodsListAdapter adapter;

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;
    int curpage = 1;

    LayoutMoreVertical layoutMoreVertical;//更多按钮弹框
    MessageNum msgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_return_goods_list, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        initView();
    }

    @Override
    public void onRGLError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            pullToRefreshListView.showNoMore();
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onRGLList(final List<ReturnGoodsList> list) {
        pullToRefreshListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (curpage > 1) {
            adapter.addList(list);
            adapter.notifyDataSetChanged();
        } else {
            pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        req.getList(member, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        req.getList(member, curpage);
    }

    private void initView() {
        enableIvMore(0);
        setHeadTitle(R.string.orders);

        if (msgNum != null && msgNum.getTotalNum() > 0) {
            enableMsgDot(true);
        } else {
            enableMsgDot(false);
        }

        //初始化更多按钮弹框
        layoutMoreVertical = new LayoutMoreVertical(vBottomLine);

        req = new ReturnGoodsList();
        req.setModelCallBack(this);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);

        adapter = new ReturnGoodsListAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPullDownToRefresh(pullToRefreshListView);
        msgNum = FHCache.getNum(this);
        if (msgNum != null && msgNum.getTotalNum() > 0) {
            enableMsgDot(true);
        } else {
            enableMsgDot(false);
        }
    }

    @Override
    public void onivMoreClick() {
        super.onivMoreClick();
        layoutMoreVertical.show();
    }
}
