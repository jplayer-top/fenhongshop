package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.HotBrands;
import com.fanglin.fenhong.microbuyer.common.adapter.HotBrandsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/12.
 * 品牌馆
 */
public class HotBrandsActivity extends BaseFragmentActivityUI implements HotBrands.HBModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    HotBrands brands;
    HotBrandsAdapter adapter;
    int curpage = 1;
    int area = -1;
    String atitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_hot_brands, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            area = Integer.valueOf(getIntent().getStringExtra("VAL"));
            atitle = getIntent().getStringExtra("TITLE");
        } catch (Exception e) {
            area = -1;
            atitle = "品牌馆";
        }
        initView();
    }

    private void initView() {
        setHeadTitle(atitle);

        GridLayoutManager gm = new GridLayoutManager(mContext, 2);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(gm);
        adapter = new HotBrandsAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
        brands = new HotBrands();
        brands.setModelCallBack(this);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        onPullDownToRefresh(pullToRefreshRecycleView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        if (brands != null) {
            brands.getList(area, BaseVar.REQUESTNUM, curpage);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        if (brands != null) {
            brands.getList(area, BaseVar.REQUESTNUM, curpage);
        }
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    @Override
    public void onHBError(String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (curpage > 1) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            pullToRefreshRecycleView.showNoMore();
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onHBList(final List<HotBrands> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (curpage > 1) {
            adapter.addList(list);
            adapter.notifyDataSetChanged();
            pullToRefreshRecycleView.onAppendData();
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
            pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
        }
    }
}
