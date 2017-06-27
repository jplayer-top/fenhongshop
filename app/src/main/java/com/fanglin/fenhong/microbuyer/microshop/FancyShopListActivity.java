package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.FancyMShop;
import com.fanglin.fenhong.microbuyer.base.model.ShopClass;
import com.fanglin.fenhong.microbuyer.microshop.adapter.FancyShopAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/13.
 * 精选微店列表
 */
public class FancyShopListActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, FancyMShop.FMSModelCallBack {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    FancyMShop fancyMShop;
    FancyShopAdapter adapter;

    String cid;
    ShopClass shopClass;
    int curpage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_actlist, null);//共用一套界面
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            shopClass = new Gson().fromJson(getIntent().getStringExtra("VAL"), ShopClass.class);
            cid = shopClass.sc_id;
            setHeadTitle(shopClass.sc_name);
        } catch (Exception e) {
            cid = null;
        }
        initView();
    }

    private void initView() {
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setOnRefreshListener(this);

        fancyMShop = new FancyMShop();
        fancyMShop.setModelCallBack(this);
        adapter = new FancyShopAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);

        onPullDownToRefresh(pullToRefreshListView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        fancyMShop.getList(cid, BaseVar.REQUESTNUM, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        fancyMShop.getList(cid, BaseVar.REQUESTNUM, curpage);
    }

    @Override
    public void onFMSError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            pullToRefreshListView.showNoMore();
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onFMSList(List<FancyMShop> list) {
        pullToRefreshListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (curpage > 1) {
            adapter.addList(list);
            adapter.notifyDataSetChanged();
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
            pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
        }
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }
}
