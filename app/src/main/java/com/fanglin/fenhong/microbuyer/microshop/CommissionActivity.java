package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedSectionListView;
import com.fanglin.fenhong.microbuyer.base.model.Commission;
import com.fanglin.fenhong.microbuyer.base.model.WSCommission;
import com.fanglin.fenhong.microbuyer.microshop.adapter.CommissionAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 我的奖金 activity
 * Created by lizhixin on 2015/11/6.
 * Rebuilt on 2016/06/03 By Plucky
 */
public class CommissionActivity extends BaseFragmentActivityUI implements WSCommission.WSCommissionModelCallBack, PullToRefreshBase.OnRefreshListener2, CommissionAdapter.TitleChangeCallBack {

    @ViewInject(R.id.pullToRefreshPinnedSectionListView)
    PullToRefreshPinnedSectionListView pullToRefreshPinnedSectionListView;

    private int curTradeMenu = 0;//交易明细菜单：0-全部，1-进行中，2-完成
    private int curPage = 1;
    private CommissionAdapter adapter;
    private WSCommission wsCommissionHandler;
    private int from;//推广来源 0普通 1达人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadTitle(R.string.my_commission);

        View view = View.inflate(this, R.layout.activity_my_commission, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        String val = getIntent().getStringExtra("VAL");
        if (BaseFunc.isInteger(val)) {
            from = Integer.valueOf(val);
        }

        initData();
    }

    private void initData() {
        wsCommissionHandler = new WSCommission();
        wsCommissionHandler.setWSCommissionModelCallBack(this);

        ArrayList<Commission> list = new ArrayList<>();
        adapter = new CommissionAdapter(this, list);

        pullToRefreshPinnedSectionListView.getRefreshableView().setAdapter(adapter);
        pullToRefreshPinnedSectionListView.setOnRefreshListener(this);
        pullToRefreshPinnedSectionListView.setMode(PullToRefreshBase.Mode.BOTH);

        adapter.setCallBack(this);

        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
        onPullDownToRefresh(pullToRefreshPinnedSectionListView);
    }

    @Override
    public void onTitleChange(int index) {
        curPage = 1;//每次切换菜单时页码从头开始
        curTradeMenu = index;
        adapter.getList().clear();//切换菜单时先清空列表
        adapter.setCurIndex(index);
        adapter.notifyDataSetChanged();
        wsCommissionHandler.getList(member, curPage, curTradeMenu, from);//发送请求获取list数据
    }

    /**
     * 请求失败
     */
    @Override
    public void onWSTeamError(String errcode) {
        pullToRefreshPinnedSectionListView.onRefreshComplete();
        if (curPage > 1) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            pullToRefreshPinnedSectionListView.showNoMore();
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            adapter.getList().clear();
            adapter.notifyDataSetChanged();
            BaseFunc.showMsg(mContext, getString(R.string.no_data));
            adapter.setMenuDatas(null);//團隊成員人數 與 奖金收入 清零
        }
    }

    /**
     * 请求成功，处理数据
     */
    @Override
    public void onWSTeamSuccess(WSCommission data) {
        pullToRefreshPinnedSectionListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (curPage > 1) {
            if (data != null && data.deduct_list != null && data.deduct_list.size() > 0) {
                adapter.addList(data.deduct_list);
                adapter.notifyDataSetChanged();
                pullToRefreshPinnedSectionListView.onAppendData(200);
                pullToRefreshPinnedSectionListView.resetPull(PullToRefreshBase.Mode.BOTH);
            } else {
                pullToRefreshPinnedSectionListView.showNoMore();
            }
        } else {
            pullToRefreshPinnedSectionListView.resetPull(PullToRefreshBase.Mode.BOTH);
            if (data != null) {
                adapter.setMenuDatas(data);
                adapter.setList(data.deduct_list);
                adapter.notifyDataSetChanged();
            } else {
                adapter.getList().clear();
                adapter.notifyDataSetChanged();
                BaseFunc.showMsg(mContext, getString(R.string.no_data));
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curPage = 1;
        wsCommissionHandler.getList(member, curPage, curTradeMenu, from);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curPage++;
        wsCommissionHandler.getList(member, curPage, curTradeMenu, from);
    }

}
