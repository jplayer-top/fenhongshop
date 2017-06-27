package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.CalculateData;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromMansongRules;
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
 * 店铺满送的界面
 */
public class MansongActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, GoodsList.GoodsListModelCallBack {

    @ViewInject(R.id.tv_msg)
    TextView tv_msg;
    @ViewInject(R.id.tv_tips_more)
    TextView tv_tips_more;
    @ViewInject(R.id.tv_money)
    TextView tv_money;
    @ViewInject(R.id.tv_num)
    TextView tv_num;

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    DecimalFormat df;
    String count_money;
    FreeFreightAdapter adapter;

    //店铺商品
    GoodsList goodsList;
    GoodsFilter gfilter;

    CalculateData calculateData;
    double original_total;

    int tips_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_mansong, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        try {
            calculateData = new Gson().fromJson(getIntent().getStringExtra("VAL"), CalculateData.class);
        } catch (Exception e) {
            calculateData = null;
        }
        if (calculateData == null || (calculateData.manlist == null || calculateData.manlist.size() == 0)) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
        }
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.goods_detail_prom_title_mansong);

        BaseFunc.setFont(tv_tips_more);

        df = new DecimalFormat("#0.00");

        count_money = getString(R.string.yuan_);

        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);

        adapter = new FreeFreightAdapter(mContext);
        adapter.setCallBack(new FreeFreightAdapter.FreeFreightAdapterCallBack() {
            @Override
            public void onChange(int position) {
                BaseFunc.showMsg(mContext, "购物车添加成功!");
                double smoney = adapter.getItem(position).goods_price;
                original_total = original_total + smoney;
                refreshNum(GoodsDtlPromMansongRules.getLeft(mContext, calculateData.manlist, original_total), original_total);
            }
        });
        pullToRefreshListView.setAdapter(adapter);

        /**
         * 处理crash log 上的bug，先做判断 -- lizhixin
         *
         * 判断没处理好 应该改成 calculateData == null
         * 则finish Added By Plucky 2016-06-13 08:58
         * fuck....
         */
        if (calculateData == null) {
            finish();
            return;
        }

        original_total = calculateData.money;
        refreshNum(GoodsDtlPromMansongRules.getLeft(mContext, calculateData.manlist, original_total), original_total);

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
        String adesc = GoodsDtlPromMansongRules.getRules(mContext, calculateData.manlist);
        tv_msg.setText(adesc);
        tv_msg.setMaxLines(1);

        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    private void refreshNum(String left_desc, double total) {
        tv_money.setText(String.format(count_money, df.format(total)));
        tv_num.setText(left_desc);
    }

    @OnClick(value = {R.id.tv_calculate, R.id.LTips})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_calculate:
                finish();
                break;
            case R.id.LTips:
                boolean f = !tv_msg.isSelected();
                tips_line = tv_msg.getLineCount();
                tv_msg.setSelected(f);
                if (f) {
                    tv_msg.setMaxLines(tips_line);
                    tv_tips_more.setTextSize(8);
                    tv_tips_more.setText(getString(R.string.if_2down));
                } else {
                    tv_msg.setMaxLines(1);
                    tv_tips_more.setTextSize(12);
                    tv_tips_more.setText(getString(R.string.if_2right));
                }
                break;
        }
    }

    @Override
    public void onGLMCError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (gfilter.curpage > 1) {
            pullToRefreshListView.showNoMore();
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
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
