package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.StoreHomeCls;
import com.fanglin.fenhong.microbuyer.common.adapter.StoreGoodsClassSectionedAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/14-上午11:25.
 * 功能描述: 店铺商品分类
 */
public class StoreGoodsClassActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener, StoreHomeCls.SHCCallBack, StoreGoodsClassSectionedAdapter.OnGoodsClassClickListener {

    @ViewInject(R.id.pinnedListView)
    PullToRefreshPinnedHeaderListView pinnedListView;

    StoreHomeCls homeClsReq;
    private String storeId, contactUrl, share;
    StoreGoodsClassSectionedAdapter sectionedAdapter;
    ShareData shareData;

    LayoutMoreVertical layoutMoreVertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_store_goodsclass, null);
        LHold.addView(view);
        skipChk = true;
        ViewUtils.inject(this, view);
        storeId = getIntent().getStringExtra("STOREID");
        contactUrl = getIntent().getStringExtra("CONTACT");
        try {
            share = getIntent().getStringExtra("SHARE");
            shareData = new Gson().fromJson(share, ShareData.class);
        } catch (Exception e) {
            shareData = null;
        }

        initView();
    }

    private void initView() {
        skipChk = true;
        setHeadTitle(R.string.title_store_goodsclass);
        enableIvMore(0);

        pinnedListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pinnedListView.setScrollingWhileRefreshingEnabled(true);
        pinnedListView.getRefreshableView().setPinHeaders(false);
        pinnedListView.setOnRefreshListener(this);

        layoutMoreVertical = new LayoutMoreVertical(vBottomLine);
        layoutMoreVertical.setIsSearchShow(false);
        layoutMoreVertical.setShareData(shareData);
        layoutMoreVertical.setContactUrl(contactUrl);

        homeClsReq = new StoreHomeCls();
        homeClsReq.setModelCallBack(this);

        sectionedAdapter = new StoreGoodsClassSectionedAdapter(mContext);
        sectionedAdapter.setListener(this);

        pinnedListView.getRefreshableView().setAdapter(sectionedAdapter);

        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
        onRefresh(pinnedListView);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        homeClsReq.getList(storeId);
    }

    @Override
    public void onSHCList(List<StoreHomeCls> list) {
        pinnedListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        sectionedAdapter.setList(list);
    }

    @Override
    public void onSHCError(String errcode) {
        pinnedListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        sectionedAdapter.setList(null);
    }

    @Override
    public void onSectionClick(String classId) {
        BaseFunc.gotoStoreGoodsListActivity(this, storeId, classId, contactUrl, share, null);
    }

    @Override
    public void onItemClick(String classId) {
        BaseFunc.gotoStoreGoodsListActivity(this, storeId, classId, contactUrl, share, null);
    }

    @Override
    public void onivMoreClick() {
        super.onivMoreClick();
        layoutMoreVertical.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            enableMsgDot(true);
            layoutMoreVertical.setMsgNum(msgnum.getTotalNum());
        } else {
            enableMsgDot(false);
            layoutMoreVertical.setMsgNum(0);
        }
    }
}
