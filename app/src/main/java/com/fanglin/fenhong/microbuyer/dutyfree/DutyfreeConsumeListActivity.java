package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeConsume;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeConsumeList;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyConsumeListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/8-上午9:59.
 * 功能描述: 极速免税消费列表
 */
public class DutyfreeConsumeListActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, DutyfreeConsumeList.DutyfreeConsumeListRequestCallBack {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    DutyConsumeListAdapter adapter;

    int curpage = 1;
    DutyfreeConsumeList consumeListReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dutyfree_consumelist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        tvHead.setText("消费明细");
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);
        adapter = new DutyConsumeListAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        consumeListReq = new DutyfreeConsumeList();
        consumeListReq.setRequestCallBack(this);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onDutyfreeConsumeList(List<DutyfreeConsume> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (list != null && list.size() > 0) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            if (curpage == 1) {
                adapter.setList(list);
            } else {
                adapter.addList(list);
            }
        } else {
            if (curpage == 1) {
                refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        consumeListReq.getList(member, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        consumeListReq.getList(member, curpage);
    }
}
