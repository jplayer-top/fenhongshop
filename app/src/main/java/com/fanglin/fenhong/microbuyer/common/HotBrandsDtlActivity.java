package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedSectionListView;
import com.fanglin.fenhong.microbuyer.base.model.HotBrandDtl;
import com.fanglin.fenhong.microbuyer.base.model.HotBrands;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.adapter.HotBrandDtlAdapter;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;


/**
 * 作者： Created by Plucky on 2015/10/8.
 * 品牌馆
 */
public class HotBrandsDtlActivity extends BaseFragmentActivityUI implements HotBrandDtl.HBDModelCallBack, PullToRefreshBase.OnRefreshListener2, HotBrandDtlAdapter.PinnedItemCallBack {

    @ViewInject(R.id.pullToRefreshPinnedSectionListView)
    PullToRefreshPinnedSectionListView pullToRefreshPinnedSectionListView;
    @ViewInject(R.id.btn)
    Button btn;

    HotBrands aBrand;
    HotBrandDtl brandDtl;

    int curpage = 1, sort = 3, order = 1;
    HotBrandDtlAdapter adapter;

    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_hotbrands_dtl, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        try {
            aBrand = new Gson().fromJson(getIntent().getStringExtra("VAL"), HotBrands.class);
        } catch (Exception e) {
            aBrand = null;
            FHLog.d("Plucky", e.getMessage());
        }

        if (aBrand == null) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
            return;
        }
        initView();
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        ShareData.fhShare(this, shareData, null);
    }

    private void initView() {
        btn.setVisibility(View.GONE);

        enableTvMore(R.string.if_share, true);
        setHeadTitle(R.string.hotbrands);

        pullToRefreshPinnedSectionListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshPinnedSectionListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshPinnedSectionListView.setOnRefreshListener(this);
        pullToRefreshPinnedSectionListView.getRefreshableView().setShadowVisible(false);

        brandDtl = new HotBrandDtl();
        brandDtl.setModelCallBack(this);
        adapter = new HotBrandDtlAdapter(mContext);
        pullToRefreshPinnedSectionListView.getRefreshableView().setAdapter(adapter);

        shareData = new ShareData();
        shareData.title = aBrand.brand_name;
        String fmt = getString(R.string.share_content_hotbrands);
        shareData.content = String.format(fmt, aBrand.brand_name);
        shareData.url = HotBrands.getShareUrl(aBrand.brand_id, aBrand.brand_banner, aBrand.brand_name, aBrand.share_desc, aBrand.share_img, aBrand.share_title);

        setHeadTitle(aBrand.brand_name);

        if (BaseFunc.isValidUrl(aBrand.brand_banner)) {
            shareData.imgs = aBrand.brand_banner;
        }

        adapter.setData(aBrand);
        adapter.notifyDataSetChanged();

        if (!TextUtils.isEmpty(aBrand.share_title)) {
            shareData.title = aBrand.share_title;
        }
        if (!TextUtils.isEmpty(aBrand.share_desc)) {
            shareData.content = aBrand.share_desc;
        }
        if (!TextUtils.isEmpty(aBrand.share_img)) {
            shareData.imgs = aBrand.share_img;
        }

        adapter.setItemCallBack(this);
        onPullDownToRefresh(pullToRefreshPinnedSectionListView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onItemClick(int index, LinearLayout LGrp, View viewUp, View viewDown) {
        chgStatus(index, LGrp, viewUp, viewDown);
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    private void chgStatus(int index, LinearLayout LGrp, View up, View down) {
        down.setSelected(false);
        up.setSelected(false);
        switch (index) {
            case 0:
                sort = 3;//3为综合推荐
                break;
            case 1:
                sort = 1;//1为销量
                break;
            case 2:
                sort = 2;//2为商品价格
                break;
            case 3:
                sort = 4;//4为人气
                break;
        }

        //order (1为升序，2为降序）
        order = order == 2 ? 1 : 2;

        for (int i = 0; i < 4; i++) {
            LGrp.getChildAt(i).setSelected(false);
        }
        LGrp.getChildAt(index).setSelected(true);

        if (index == 2) {
            if (order == 1) {
                up.setSelected(true);
                down.setSelected(false);
            } else {
                down.setSelected(true);
                up.setSelected(false);
            }
        }
        adapter.refreshIndex(index, order);
        onPullDownToRefresh(pullToRefreshPinnedSectionListView);
    }


    @Override
    public void onHBDError(String errcode) {
        pullToRefreshPinnedSectionListView.onRefreshComplete();
        if (curpage > 1) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            pullToRefreshPinnedSectionListView.showNoMore();
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onHBDList(List<HotBrandDtl> list) {
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        pullToRefreshPinnedSectionListView.onRefreshComplete();
        if (curpage > 1) {
            adapter.addList(list);
            adapter.notifyDataSetChanged();
            pullToRefreshPinnedSectionListView.onAppendData(200);
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
            pullToRefreshPinnedSectionListView.resetPull(PullToRefreshBase.Mode.BOTH);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        brandDtl.getList(aBrand.brand_id, sort, order, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        brandDtl.getList(aBrand.brand_id, sort, order, curpage);
    }
}
