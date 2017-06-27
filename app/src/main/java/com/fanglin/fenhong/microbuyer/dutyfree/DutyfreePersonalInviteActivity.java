package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreePersonalMessageBean;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyfreePersonalInviteMoneyAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/6.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */

public class DutyfreePersonalInviteActivity extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener2, DutyfreePersonalMessageBean.RequDataCallBack, View.OnClickListener {
    @ViewInject(R.id.ivBack)
    ImageView ivBack;
    @ViewInject(R.id.llImage)
    LinearLayout llImage;
    @ViewInject(R.id.ivMenu)
    ImageView ivMenu;
    @ViewInject(R.id.tvMoney)
    TextView tvMoney;
    @ViewInject(R.id.tvAllMoney)
    TextView tvAllMoney;
    @ViewInject(R.id.recyclerView)
    PullToRefreshRecycleView recyclerView;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    @ViewInject(R.id.tvPersonalCounts)
    TextView tvPersonalCounts;
    @ViewInject(R.id.llAllPersonal)
    LinearLayout llAllPersonal;
    private DutyfreePersonalMessageBean dutyfreePersonalMessageBean;
    private DutyfreePersonalInviteMoneyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutyfree_personal_message);
        ViewUtils.inject(this);
        initView();
    }
    //初始化界面

    private void initView() {
        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.setOnRefreshListener(this);
        recyclerView.setScrollingWhileRefreshingEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.getRefreshableView().setLayoutManager(manager);
        recyclerView.getRefreshableView().setHasFixedSize(true);
        adapter = new DutyfreePersonalInviteMoneyAdapter(mContext);
        recyclerView.getRefreshableView().setAdapter(adapter);

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        View emptyView = multiStateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        if (emptyView != null) {
            emptyView.setOnClickListener(this);
        }

        dutyfreePersonalMessageBean = new DutyfreePersonalMessageBean();
        dutyfreePersonalMessageBean.setRequDataCallBack(this);
        onPullDownToRefresh(recyclerView);
    }

    @OnClick(value = {R.id.ivBack, R.id.ivMenu, R.id.tvMoney, R.id.tvAllMoney, R.id.recyclerView, R.id.llImage})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.llImage://修复点击位置范围过小的BUG
                finish();
                break;
            case R.id.ivMenu:
                BaseFunc.gotoActivity(this, DutyfreeConsumeListActivity.class, null);
                break;
        }
    }

    private int curpage;

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        dutyfreePersonalMessageBean.requData(curpage, member);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        dutyfreePersonalMessageBean.requData(curpage, member);
    }

    @Override
    public void onRequData(DutyfreePersonalMessageBean messageBean) {
        recyclerView.onRefreshComplete();
        recyclerView.resetPull(PullToRefreshBase.Mode.BOTH);
        if (messageBean != null) {
            tvAllMoney.setText(messageBean.total_money);
            tvMoney.setText(messageBean.my_award);
            llAllPersonal.setVisibility(View.VISIBLE);
            tvPersonalCounts.setText(messageBean.total_person);
            if (messageBean.award_list.size() > 0) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                if (curpage == 1) {
                    adapter.setVIPMessage(messageBean);
                } else {
                    adapter.addVIPMessage(messageBean);
                    recyclerView.onAppendData();
                }
            } else if (messageBean.award_list.size() == 0) {
                llAllPersonal.setVisibility(View.INVISIBLE);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        } else {
            if (curpage == 1) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                recyclerView.showNoMore();
            }
        }
    }

    @Override
    public void onClick(View v) {
        onPullDownToRefresh(recyclerView);
    }
}
