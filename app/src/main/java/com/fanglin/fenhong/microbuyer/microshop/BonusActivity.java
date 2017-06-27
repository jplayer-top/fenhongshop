package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.BonusData;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.microshop.adapter.BonusAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/27.
 * 我的优惠券
 */
public class BonusActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, BonusData.BDModelCallBack {
    @ViewInject(R.id.ll_not)
    LinearLayout ll_not;
    @ViewInject(R.id.tv_not)
    TextView tv_not;
    @ViewInject(R.id.ll_use)
    LinearLayout ll_use;
    @ViewInject(R.id.tv_use)
    TextView tv_use;
    @ViewInject(R.id.ll_out_of_date)
    LinearLayout ll_out_of_date;
    @ViewInject(R.id.tv_out_of_date)
    TextView tv_out_of_date;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    BonusAdapter adapter;

    BonusData bonusDataReq;
    LayoutBonus layoutBonus;

    int curpage = 1;
    int state = 0;
    boolean hasNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_bonus, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        hasNew = !TextUtils.isEmpty(getIntent().getStringExtra("VAL"));
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.coupon_list);
        layoutBonus = new LayoutBonus(this);

        ll_not.setSelected(true);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);

        refreshNum(null);
        adapter = new BonusAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);
        bonusDataReq = new BonusData();
        bonusDataReq.setModelCallBack(this);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);

        View emptyView = multiStateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        if (emptyView != null) {
            TextView tvEmpty = (TextView) emptyView.findViewById(R.id.tvEmpty);
            ImageView ivEmpty = (ImageView) emptyView.findViewById(R.id.ivEmpty);
            ivEmpty.setImageResource(R.drawable.img_no_coupons);
            tvEmpty.setText(getString(R.string.tips_no_counpons));
        }

        View errView = multiStateView.getView(MultiStateView.VIEW_STATE_ERROR);
        if (errView != null) {
            ImageView ivRefresh = (ImageView) errView.findViewById(R.id.ivRefresh);
            TextView tvMsg = (TextView) errView.findViewById(R.id.tvMsg);
            TextView tvRefresh = (TextView) errView.findViewById(R.id.tvRefresh);
            ivRefresh.setImageResource(R.drawable.img_no_coupons);
            tvMsg.setText(getString(R.string.tips_no_counpons));
            tvRefresh.setText(getString(R.string.lbl_get_coupons));

            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, BaseVar.URL_FRIEND_BAG);
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        onPullDownToRefresh(pullToRefreshListView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        adapter.setList(null);
        adapter.isred = (state == 0);
        adapter.notifyDataSetChanged();//刷新的时候先清数据
        bonusDataReq.getData(member, state, curpage);

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        bonusDataReq.getData(member, state, curpage);
    }

    @Override
    public void onBDData(BonusData data) {
        pullToRefreshListView.onRefreshComplete();
        if (data != null) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            refreshNum(data.coupon_counts);
            if (curpage > 1) {
                if (data.coupon_list != null && data.coupon_list.size() > 0) {
                    adapter.addList(data.coupon_list);
                } else {
                    pullToRefreshListView.showNoMore();
                }

            } else {
                pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
                adapter.setList(data.coupon_list);

                if (data.coupon_list != null && data.coupon_list.size() > 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    if (hasNew && state == 0) {
                        layoutBonus.setStoreId(data.coupon_list.get(0).store_id);
                        layoutBonus.show(data.coupon_list.get(0).getHongBao());
                        hasNew = false;
                    }
                } else {
                    if (state > 0) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    } else {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            refreshNum(null);
        }

    }

    @Override
    public void onBDError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            pullToRefreshListView.showNoMore();
        } else {
            refreshNum(null);
            BaseFunc.showMsg(mContext, getString(R.string.no_data));
        }
    }

    private void refreshNum(List<String> counts) {
        String not = "0", use = "0", out_of_date = "0";
        if (counts != null && counts.size() == 3) {
            not = counts.get(0);
            use = counts.get(1);
            out_of_date = counts.get(2);
        }
        tv_not.setText(String.format(getString(R.string.bonus_title_notuse), not));
        tv_use.setText(String.format(getString(R.string.bonus_title_used), use));
        tv_out_of_date.setText(String.format(getString(R.string.bonus_title_outofdate), out_of_date));
    }

    @OnClick(value = {R.id.ll_not, R.id.ll_use, R.id.ll_out_of_date})
    public void onViewClick(View v) {
        ll_not.setSelected(false);
        ll_use.setSelected(false);
        ll_out_of_date.setSelected(false);
        switch (v.getId()) {
            case R.id.ll_not:
                state = 0;
                ll_not.setSelected(true);
                break;
            case R.id.ll_use:
                state = 1;
                ll_use.setSelected(true);
                break;
            case R.id.ll_out_of_date:
                state = 2;
                ll_out_of_date.setSelected(true);
                break;
        }
        curpage = 1;
        onPullDownToRefresh(pullToRefreshListView);
    }
}
