package com.fanglin.fenhong.microbuyer.common;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.StoreNewGoods;
import com.fanglin.fenhong.microbuyer.common.adapter.StoreNewGoodsAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/13-下午3:05.
 * 功能描述: 店铺上新商品
 */
public class StoreNewGoodsActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener, StoreNewGoods.SNGModelCallBack {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    private LayoutMoreVertical layoutMoreVertical;

    private String storeId, contactUrl;
    private ShareData shareData;

    StoreNewGoodsAdapter goodsAdapter;
    //上新商品
    StoreNewGoods newGoodsReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_storenewgoods, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        storeId = getIntent().getStringExtra("STOREID");
        contactUrl = getIntent().getStringExtra("CONTACT");
        try {
            String share = getIntent().getStringExtra("SHARE");
            shareData = new Gson().fromJson(share, ShareData.class);
        } catch (Exception e) {
            shareData = null;
        }

        initView();
    }

    private void initView() {
        skipChk = true;
        setHeadTitle(R.string.title_newgoods);
        enableIvMore(0);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        goodsAdapter = new StoreNewGoodsAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(goodsAdapter);

        layoutMoreVertical = new LayoutMoreVertical(vBottomLine);
        layoutMoreVertical.setIsSearchShow(false);
        layoutMoreVertical.setShareData(shareData);
        layoutMoreVertical.setContactUrl(contactUrl);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        GridLayoutManager.SpanSizeLookup lookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return goodsAdapter.getSpanSize(position);
            }
        };
        manager.setSpanSizeLookup(lookup);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);


        MutiItemDecoration decoration = new MutiItemDecoration(MutiItemDecoration.Type.ALL, 2);
        decoration.setSectionListener(new MutiItemDecoration.DecorationSectionListener() {
            @Override
            public void onGetSectionOffsets(Rect outRect, int position, int index) {
                int[] px = goodsAdapter.getOffsets(position, index);
                outRect.set(px[0], px[1], px[2], px[3]);
            }
        });
        pullToRefreshRecycleView.getRefreshableView().addItemDecoration(decoration);
        pullToRefreshRecycleView.setBackUpView(btnBackTop);

        newGoodsReq = new StoreNewGoods();
        newGoodsReq.setModelCallBack(this);

        onRefresh(pullToRefreshRecycleView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);

        onEmptyView(R.string.hint_nonewgoods, R.drawable.store_nonewgoods, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh(pullToRefreshRecycleView);
                refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        newGoodsReq.getList(storeId);
    }

    @Override
    public void onSNGError(String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public void onSNGList(List<StoreNewGoods> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (list != null && list.size() > 0) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            goodsAdapter.setList(list);
            goodsAdapter.notifyDataSetChanged();
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
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
