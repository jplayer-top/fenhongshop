package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.DepositDtl;
import com.fanglin.fenhong.microbuyer.common.LayoutMoreVertical;
import com.fanglin.fenhong.microbuyer.microshop.adapter.DepositDtlAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 * 提现明细列表
 * 作者： Created by Plucky on 15-10-29.
 * modify by lizhixin on 2015/12/25.
 */
public class DepositeDtlActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener, DepositDtl.DDModelCallBack {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    DepositDtl depositDtl;
    DepositDtlAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_actlist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        enableIvMore(0);
        setHeadTitle(R.string.deposit_dtl);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);

        if (member == null) return;
        depositDtl = new DepositDtl();
        depositDtl.setModelCallBack(this);
        adapter = new DepositDtlAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);
        onRefresh(pullToRefreshListView);
    }

    @Override
    public void onivMoreClick() {
        super.onivMoreClick();
        LayoutMoreVertical layoutMoreVertical = new LayoutMoreVertical(vBottomLine);
        layoutMoreVertical.setShareData(null);
        layoutMoreVertical.show();
    }

    /** 奖金明细列表代理 */
    @Override
    public void onDDError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
    }

    @Override
    public void onDDList(List<DepositDtl> list) {
        pullToRefreshListView.onRefreshComplete();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        depositDtl.getList(member);
    }
}
