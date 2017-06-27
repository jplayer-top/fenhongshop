package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyFreeCount;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyHomePage;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyfreeNavigationAdapter;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-下午5:06.
 * 功能描述: 极速免税店 首页
 */
public class DutyFreeMainActivity extends BaseFragmentActivity implements DutyfreeCategory.CategoryRequestCallBack, DutyHomePage.DutyHomePageReqCallBack, DutyFreeCount.DutyFreeCountRequestCallBack {

    @ViewInject(R.id.indicator)
    TabPageIndicator indicator;
    @ViewInject(R.id.pager)
    ViewPager pager;
    @ViewInject(R.id.ivIntro)
    ImageView ivIntro;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    @ViewInject(R.id.tvSearch)
    TextView tvSearch;
    @ViewInject(R.id.tvCartNum)
    TextView tvCartNum;
    @ViewInject(R.id.LAction)
    LinearLayout LAction;
    @ViewInject(R.id.tvOrder)
    TextView tvOrder;
    @ViewInject(R.id.tvWallet)
    TextView tvWallet;

    DutyfreeNavigationAdapter adapter;
    DutyfreeCategory categoryReq;
    DutyFreeCount countReq;

    DutyHomePage pageReq;
    private String introUrl;
    private String ucenterMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutyfree_main);
        ViewUtils.inject(this);

        initView();
    }


    private void initView() {
        LAction.setVisibility(View.GONE);
        adapter = new DutyfreeNavigationAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        pager.setOffscreenPageLimit(3);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String msg = adapter.getMsg(position);
                if (!TextUtils.isEmpty(msg)) {
                    BaseFunc.showMsg(mContext, msg);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        categoryReq = new DutyfreeCategory();
        categoryReq.setCallBack(this);
        categoryReq.getCategory1();
        pageReq = new DutyHomePage();
        pageReq.setDutyHomePageReqCallBack(this);
        countReq = new DutyFreeCount();
        countReq.setRequestCallBack(this);

        pageReq.getData(member);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countReq.getCount(member);
    }

    @OnClick(value = {R.id.LSearch, R.id.LCart, R.id.tvOrder, R.id.ivBack, R.id.ivIntro, R.id.tvWallet})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.LSearch:
                BaseFunc.gotoActivity(mContext, DutyfreeSearchActivity.class, null);
                break;
            case R.id.LCart:
                BaseFunc.gotoActivity(mContext, DutyfreeCartActivity.class, null);
                break;
            case R.id.tvOrder:
                BaseFunc.gotoActivity(mContext, OrderListActivity.class, null);
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivIntro:
                BaseFunc.urlClick(mContext, introUrl);
                break;
            case R.id.tvWallet:
                BaseFunc.gotoActivity(mContext, DutyfreePersonalActivity.class, null);
                break;
        }
    }

    @Override
    public void onCategoryReqList(List<DutyfreeCategory> list) {
        if (list != null && list.size() > 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            adapter.setList(list);
            adapter.notifyDataSetChanged();
            indicator.notifyDataSetChanged();
        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onDutyHomeData(DutyHomePage data) {
        if (data != null) {
            if (!TextUtils.isEmpty(data.getGuide_url())) {
                ivIntro.setVisibility(View.VISIBLE);
                introUrl = data.getGuide_url();
                ucenterMsg = data.getUcenter_msg();
            } else {
                ivIntro.setVisibility(View.GONE);
            }
            LAction.setVisibility(View.VISIBLE);
            tvSearch.setText(data.getSearch_tips());
            if (data.isOrder_visible()) {
                tvOrder.setVisibility(View.VISIBLE);
            } else {
                tvOrder.setVisibility(View.GONE);
            }

            if (data.isWallet_visible()) {
                tvWallet.setVisibility(View.VISIBLE);
            } else {
                tvWallet.setVisibility(View.GONE);
            }
        } else {
            ivIntro.setVisibility(View.GONE);
            LAction.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDutyFreeCountRequest(DutyFreeCount data) {
        if (data != null && data.showCartNum()) {
            tvCartNum.setVisibility(View.VISIBLE);
            tvCartNum.setText(data.getCartNumDesc());
        } else {
            tvCartNum.setVisibility(View.INVISIBLE);
        }
    }
}
