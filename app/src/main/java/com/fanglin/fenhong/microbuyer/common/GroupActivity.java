package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedSectionListView;
import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;
import com.fanglin.fenhong.microbuyer.base.model.BrandGoods;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.Favorites;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.adapter.GroupAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/13-上午9:20.
 * 功能描述: 聚合页
 */
public class GroupActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, BrandMessage.BrandMsgModelCallBack, BrandGoods.BrandGoodsModelCallBack, GroupAdapter.GroupClickListener {

    @ViewInject(R.id.pullToRefreshPinnedSectionListView)
    PullToRefreshPinnedSectionListView pullToRefreshPinnedSectionListView;
    GroupAdapter groupAdapter;

    BrandMessage brandMessageReq;
    private String brandId;
    private ShareData shareData;

    BrandGoods brandGoodsReq;

    private int curpage = 1, sort = 3, order = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skipChk = true;
        View view = View.inflate(mContext, R.layout.activity_group, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        brandId = getIntent().getStringExtra("VAL");
        initView();
    }


    private void initView() {
        enableTvMore(R.string.if_share, true);

        pullToRefreshPinnedSectionListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshPinnedSectionListView.setOnRefreshListener(this);
        pullToRefreshPinnedSectionListView.setScrollingWhileRefreshingEnabled(true);

        groupAdapter = new GroupAdapter(mContext);
        groupAdapter.setListener(this);
        pullToRefreshPinnedSectionListView.getRefreshableView().setAdapter(groupAdapter);

        brandMessageReq = new BrandMessage();
        brandMessageReq.setModelCallBack(this);

        brandGoodsReq = new BrandGoods();
        brandGoodsReq.setModelCall(this);

        onPullDownToRefresh(pullToRefreshPinnedSectionListView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
        setVisibleOfTvMore(false);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        brandMessageReq.getBrandMessage(brandId, member);
        brandGoodsReq.getGoodsList(brandId, curpage, sort, order);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        brandGoodsReq.getGoodsList(brandId, curpage, sort, order);
    }

    @Override
    public void onBrandMsgData(int errorCode, BrandMessage brandMessage) {
        pullToRefreshPinnedSectionListView.onRefreshComplete();
        if (errorCode < 0) {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        }

        groupAdapter.setBrandMessage(brandMessage);
        if (brandMessage != null) {
            setHeadTitle(brandMessage.getBrand_name());
            if (!TextUtils.isEmpty(brandMessage.getShare_title()) && !TextUtils.isEmpty(brandMessage.getShare_desc())) {
                setVisibleOfTvMore(true);
                shareData = new ShareData();
                shareData.title = brandMessage.getShare_title();
                shareData.content = brandMessage.getShare_desc();
                shareData.imgs = brandMessage.getShare_img();
                shareData.url = brandMessage.getShareUrl();
            } else {
                setVisibleOfTvMore(false);
            }
        }
    }

    @Override
    public void onBrandGoodsList(List<ActivityGoods> list) {
        pullToRefreshPinnedSectionListView.onRefreshComplete();

        if (curpage > 1) {
            if (list != null && list.size() > 0) {
                groupAdapter.addList(list);
                pullToRefreshPinnedSectionListView.onAppendData(200);
            } else {
                pullToRefreshPinnedSectionListView.showNoMore();
            }
        } else {
            groupAdapter.setList(list);
            pullToRefreshPinnedSectionListView.resetPull(PullToRefreshBase.Mode.BOTH);
        }
    }

    @Override
    public void onFilterClick(int sort, int order) {
        curpage = 1;
        this.sort = sort;
        this.order = order;
        brandGoodsReq.getGoodsList(brandId, curpage, sort, order);
    }

    @Override
    public void onCollect(String brand_id, final boolean hasCollected) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        Favorites.FavModelCallBack callBack = new Favorites.FavModelCallBack() {
            @Override
            public void onFavError(String errcode) {
                if (TextUtils.equals("0", errcode)) {
                    groupAdapter.refreshCollectStatus(hasCollected ? 0 : 1);
                }
            }

            @Override
            public void onFavList(List<Favorites> list) {

            }

            @Override
            public void onFavStart() {

            }
        };
        Favorites favReq = new Favorites();
        favReq.setIfShowMsg(true);
        favReq.setModelCallBack(callBack);
        if (hasCollected) {
            favReq.delete_favorites(mContext, member, brand_id, "brand");
        } else {
            favReq.add_favorites(mContext, member, brand_id, "brand");
        }
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        ShareData.fhShare(this, shareData, null);
    }
}
