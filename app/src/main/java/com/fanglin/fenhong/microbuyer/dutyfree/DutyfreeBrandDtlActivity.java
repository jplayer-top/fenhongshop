package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BrandMessageDetail;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyBrandDtlAdapter;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyBrandDtlPindedViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/23-下午3:55.
 * 功能描述: 极速免税店 品牌聚合页
 */
public class DutyfreeBrandDtlActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, DutyBrandDtlPindedViewHolder.PindedHeaderListener, BrandMessageDetail.BrandMessageDtlCallbackRequest {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.LContainer)
    LinearLayout LContainer;
    DutyBrandDtlPindedViewHolder holder;

    private String brandId;
    private int curpage = 1, sort = 1, order = 1;
    DutyBrandDtlAdapter adapter;
    BrandMessageDetail messageDtlReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        skipChk = true;
        View view = View.inflate(mContext, R.layout.activity_dutyfree_branddetail, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        brandId = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        tvHead.setText("品牌详情");
        vSpliter.setVisibility(View.GONE);
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        View view = DutyBrandDtlPindedViewHolder.getView(mContext);
        LContainer.removeAllViews();
        LContainer.addView(view);
        holder = DutyBrandDtlPindedViewHolder.getHolderByView(view);
        holder.setHeaderListener(this);
        holder.refreshView(0, 2);

        adapter = new DutyBrandDtlAdapter(mContext);
        adapter.setPindedHeaderListener(this);
        final GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanSize(position);
            }
        });

        pullToRefreshRecycleView.getRefreshableView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pos = manager.findFirstVisibleItemPosition();
                if (pos >= 5) {
                    LContainer.setVisibility(View.VISIBLE);
                } else {
                    LContainer.setVisibility(View.GONE);
                }
            }
        });
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        messageDtlReq = new BrandMessageDetail();
        messageDtlReq.setCallbackRequest(this);
        onPullDownToRefresh(pullToRefreshRecycleView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        messageDtlReq.getData(member, brandId, curpage, sort, order);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        messageDtlReq.getData(member, brandId, curpage, sort, order);
    }


    @Override
    public void onFilter(int index, int order) {
        if (holder != null) {
            holder.refreshView(index, order);
            adapter.setIndexAndSort(index, order);

            this.sort = index + 1;
            this.order = order;
            pullToRefreshRecycleView.getRefreshableView().scrollToPosition(3);

            onPullDownToRefresh(pullToRefreshRecycleView);
        }
    }

    @Override
    public void onBrandMessageDetail(BrandMessage data) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (data != null) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            if (curpage == 1) {
                pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
                tvHead.setText(data.getBrand_name());
                adapter.setBrandMessage(data);
            } else {
                List<BaseProduct> goodsList = data.getGoodslist();
                if (goodsList != null && goodsList.size() > 0) {
                    adapter.addGoodsList(goodsList);
                    pullToRefreshRecycleView.onAppendData();
                } else {
                    pullToRefreshRecycleView.showNoMore();
                }
            }
        } else {
            if (curpage == 1) {
                refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                pullToRefreshRecycleView.showNoMore();
            }
        }
    }
}
