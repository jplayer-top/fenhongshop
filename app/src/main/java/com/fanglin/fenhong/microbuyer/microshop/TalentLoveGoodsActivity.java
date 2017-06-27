package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.TalentInfo;
import com.fanglin.fenhong.microbuyer.base.model.TalentLoveGoods;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsListAdapter;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TalentGoodsClsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/22-上午9:12.
 * 功能描述: 达人推荐商品列表
 */
public class TalentLoveGoodsActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, TalentLoveGoods.TalentLoveGoodsModelCallBack, TalentGoodsClsAdapter.TalentGoodsClsAdapterListener, GoodsListAdapter.GoodsListCallBack {

    private String talentId;
    private String curGid;
    private int curpage = 1;

    TalentGoodsClsAdapter clsAdapter;
    GoodsListAdapter goodsListAdapter;
    TalentLoveGoods talentLoveGoodsReq;

    @ViewInject(R.id.recyclerView)
    RecyclerView recyclerView;
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skipChk=true;
        View view = View.inflate(mContext, R.layout.activity_talentlovegoods, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        talentId = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        //分类
        LinearLayoutManager clsManager = new LinearLayoutManager(mContext);
        clsManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(clsManager);

        clsAdapter = new TalentGoodsClsAdapter(mContext);
        clsAdapter.setListener(this);
        recyclerView.setAdapter(clsAdapter);

        //列表
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        LinearLayoutManager goodsManager = new LinearLayoutManager(mContext);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(goodsManager);

        goodsListAdapter = new GoodsListAdapter(mContext);
        goodsListAdapter.setListener(this);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(goodsListAdapter);

        talentLoveGoodsReq = new TalentLoveGoods();
        talentLoveGoodsReq.setModelCallBack(this);

        onPullDownToRefresh(pullToRefreshRecycleView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        talentLoveGoodsReq.getTalentLoveGoods(talentId, curGid, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        talentLoveGoodsReq.getTalentLoveGoods(talentId, curGid, curpage);
    }

    @Override
    public void onTalentLoveGoodsData(TalentLoveGoods talentLoveGoods) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (curpage > 1) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            if (talentLoveGoods != null) {
                List<GoodsList> goodsLists = talentLoveGoods.getGoods();
                if (goodsLists != null && goodsLists.size() > 0) {
                    goodsListAdapter.addList(goodsLists);
                    pullToRefreshRecycleView.onAppendData();
                } else {
                    pullToRefreshRecycleView.showNoMore();
                }
            } else {
                pullToRefreshRecycleView.showNoMore();
            }
        } else {
            pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
            if (talentLoveGoods != null) {
                refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
                clsAdapter.setList(talentLoveGoods.getGoods_classes());
                goodsListAdapter.setList(talentLoveGoods.getGoods());

                TalentInfo talentInfo = talentLoveGoods.getTalent();
                if (talentInfo != null) {
                    setHeadTitle(talentInfo.getTalentGoodsTitle());
                }
            } else {
                refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        clsAdapter.setCurPos(position);
        curGid = clsAdapter.getItem(position).gc_id;
        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onItemClick(GoodsList c1, int position) {
        //传入推广人员MemberId====talent_deductid
        BaseFunc.gotoGoodsDetail(this, c1.goods_id, null, c1.talent_deductid);
    }
}
