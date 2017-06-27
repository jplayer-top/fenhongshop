package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.CalculateData;
import com.fanglin.fenhong.microbuyer.base.model.GoodsFilter;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.common.adapter.FreeFreightAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/31.
 * 店铺满多少包邮的界面
 */
public class FreeFreightActivity extends BaseFragmentActivityUI implements GoodsList.GoodsListModelCallBack,PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.tv_msg)
    TextView tv_msg;
    @ViewInject(R.id.tv_money)
    TextView tv_money;
    @ViewInject(R.id.tv_num)
    TextView tv_num;
    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    DecimalFormat df;
    String hint_free_freight, count_money, count_num_free_freight;
    FreeFreightAdapter adapter;

    //店铺商品
    GoodsList goodsList;
    GoodsFilter gfilter;

    CalculateData calculateData;
    double original_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_free_freight, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        try {
            calculateData = new Gson().fromJson(getIntent().getStringExtra("VAL"), CalculateData.class);
        } catch (Exception e) {
            calculateData = null;
        }
        if (calculateData == null) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
        }
        initView();
    }

    private void initView() {
        if (calculateData == null) return;

        setHeadTitle(R.string.title_free_freight);

        df = new DecimalFormat("#0.00");
        hint_free_freight = getString(R.string.hint_free_freight);
        count_money = getString(R.string.yuan_);
        count_num_free_freight = getString(R.string.count_num_free_freight);


        pullToRefreshListView.setOnRefreshListener(this);
        adapter = new FreeFreightAdapter(mContext);
        adapter.setCallBack(new FreeFreightAdapter.FreeFreightAdapterCallBack() {
            @Override
            public void onChange(int position) {
                BaseFunc.showMsg(mContext, "购物车添加成功!");
                double smoney = adapter.getItem(position).goods_price;
                original_total = original_total + smoney;
                refreshNum(calculateData.free_freight_limit, original_total);
            }
        });
        pullToRefreshListView.setAdapter(adapter);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);

        original_total = calculateData.money;
        refreshNum(calculateData.free_freight_limit, original_total);

        /** 筛选条件类*/
        gfilter = new GoodsFilter();
        gfilter.sid = calculateData.store_id;
        gfilter.curpage = 1;
        gfilter.sort = 3;
        gfilter.order = 1;
        gfilter.is_sale = "1";

        goodsList = new GoodsList();
        goodsList.setModelCallBack(this);

        onPullDownToRefresh(pullToRefreshListView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    private void refreshNum(double freight_limit, double total) {
        double freight_left = freight_limit - total;
        freight_left = freight_left < 0 ? 0 : freight_left;
        tv_msg.setText(String.format(hint_free_freight, df.format(freight_limit)));
        tv_money.setText(String.format(count_money, df.format(total)));
        if (freight_left > 0) {
            tv_num.setText(String.format(count_num_free_freight, df.format(freight_left)));
        } else {
            tv_num.setText("已包邮");
        }

    }

    @OnClick(value = {R.id.tv_calculate})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_calculate:
                finish();
                break;
        }
    }

    @Override
    public void onGLMCError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (gfilter.curpage > 1) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            pullToRefreshListView.showNoMore();
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            adapter.setList(null);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGLMCList(List<GoodsList> list) {
        pullToRefreshListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (gfilter.curpage > 1) {
            adapter.addList(list);
            adapter.notifyDataSetChanged();
        } else {
            pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        gfilter.curpage = 1;
        goodsList.get_goods(gfilter);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        gfilter.curpage++;
        goodsList.get_goods(gfilter);
    }
}
