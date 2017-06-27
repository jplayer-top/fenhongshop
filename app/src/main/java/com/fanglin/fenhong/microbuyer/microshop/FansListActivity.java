package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.FansList;
import com.fanglin.fenhong.microbuyer.microshop.adapter.FansListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/21-下午4:57.
 * 功能描述: 达人粉丝列表
 */
public class FansListActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, FansList.FansListCallBack {
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    private String talentId;
    private int curpage = 1;
    FansList fansListReq;
    FansListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skipChk=true;
        View view = View.inflate(mContext, R.layout.activity_fanslist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        talentId = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.title_fans);
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);
        adapter = new FansListAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        fansListReq = new FansList();
        fansListReq.setModelCallBack(this);

        onPullDownToRefresh(pullToRefreshRecycleView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        fansListReq.getList(talentId, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        fansListReq.getList(talentId, curpage);
    }

    @Override
    public void onFansList(List<FansList> theList) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (curpage > 1) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            if (theList != null && theList.size() > 0) {
                adapter.addList(theList);
                pullToRefreshRecycleView.onAppendData();
            } else {
                pullToRefreshRecycleView.showNoMore();
            }
        } else {
            pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
            if (theList != null && theList.size() > 0) {
                adapter.setList(theList);
                refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            }
        }
    }
}
