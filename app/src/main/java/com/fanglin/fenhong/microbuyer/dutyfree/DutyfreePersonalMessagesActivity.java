package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeMessagesBean;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyfreePersonalMessagesAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/9.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */
public class DutyfreePersonalMessagesActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, DutyfreeMessagesBean.RequDataCallBack {
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    @ViewInject(R.id.recyclerView)
    PullToRefreshRecycleView recyclerView;
    private DutyfreeMessagesBean dutyfreeMessagesBean;
    private DutyfreePersonalMessagesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        redTop = false;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dutyfree_messages, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        setHeadTitle("消息");
        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.setOnRefreshListener(this);
        recyclerView.setScrollingWhileRefreshingEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.getRefreshableView().setLayoutManager(manager);
        recyclerView.getRefreshableView().setHasFixedSize(true);
        adapter = new DutyfreePersonalMessagesAdapter(mContext);
        recyclerView.getRefreshableView().setAdapter(adapter);

        dutyfreeMessagesBean = new DutyfreeMessagesBean();
        dutyfreeMessagesBean.setRequDataCallBack(this);

        onPullDownToRefresh(recyclerView);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    private int curpage;

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        dutyfreeMessagesBean.requData(curpage, member);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        dutyfreeMessagesBean.requData(curpage, member);
    }

    @Override
    public void onRequDataCallBack(List<DutyfreeMessagesBean> data) {
        recyclerView.onRefreshComplete();
        recyclerView.resetPull(PullToRefreshBase.Mode.BOTH);
        if (data != null && data.size() > 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            if (curpage == 1) {
                adapter.setRequData(data);
            } else {
                adapter.addRequData(data);
                recyclerView.onAppendData();//上移
            }
        } else {
            if (curpage == 1) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                recyclerView.showNoMore();
            }
        }
    }
}
