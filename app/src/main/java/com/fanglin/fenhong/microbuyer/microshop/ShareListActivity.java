package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.Share;
import com.fanglin.fenhong.microbuyer.microshop.adapter.ShareAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 *  Created by Plucky on 2015/10/29.
 *  我的推广
 */
public class ShareListActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, Share.TraceModelCallBack {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;
    Share share;
    int curpage = 1;
    ShareAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_actlist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.shares);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);

        share = new Share();
        share.setModelCallBack(this);
        adapter = new ShareAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);
        onPullDownToRefresh(pullToRefreshListView);

        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        share.getList(member, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        share.getList(member, curpage);
    }

    /** 数据代理 */
    @Override
    public void onTraceError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            pullToRefreshListView.showNoMore();
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onTraceList(List<Share> list) {
        pullToRefreshListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (curpage > 1) {
            if (list != null && list.size() > 0) {
                adapter.addList(list);
                adapter.notifyDataSetChanged();
            } else {
                pullToRefreshListView.showNoMore();
            }
        } else {
            pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }
}
