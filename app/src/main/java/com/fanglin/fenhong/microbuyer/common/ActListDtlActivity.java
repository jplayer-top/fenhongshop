package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BannerView;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.ActivityDtl;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.adapter.ActDtlGoodsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

/**
 * 作者： Created by Plucky on 2015/10/8.
 * 主题馆
 */
public class ActListDtlActivity extends BaseFragmentActivityUI implements ActivityDtl.ActivityDtlModelCallBack, PullToRefreshBase.OnRefreshListener {

    String activity_id;//父级分类id

    ActivityDtl activityDtl;

    //显示楼层与商品
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;


    @ViewInject(R.id.btn)
    Button btn;
    @ViewInject(R.id.LStickky)
    LinearLayout LStickky;

    ActDtlGoodsAdapter goodsAdapter;
    GridLayoutManager gridLayoutManager;

    ShareData shareData;
    BannerView bv;
    LayoutActDtlSectionContent recycleSectionContent, recycleSectionContentStickky;
    int offset = 0;
    int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_actlist_dtl, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        activity_id = getIntent().getStringExtra("VAL");
        offset = getResources().getDimensionPixelOffset(R.dimen.dp_of_50);

        initView();
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        ShareData.fhShare(this, shareData, null);
    }

    private void initView() {
        setHeadTitle(R.string.actlist);
        enableTvMore(R.string.if_share, true);

        goodsAdapter = new ActDtlGoodsAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(goodsAdapter);

        gridLayoutManager = new GridLayoutManager(mContext, 2);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(goodsAdapter, gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(lookup);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(gridLayoutManager);

        pullToRefreshRecycleView.getRefreshableView().addItemDecoration(new MutiItemDecoration(MutiItemDecoration.Type.ALL, 2));


        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setBackUpView(btn);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);


        pullToRefreshRecycleView.getRefreshableView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] loc = new int[]{0, 0};//分割线的位置
                int[] pos = new int[]{0, 0};//分割线的位置

                View vLine = recycleSectionContent.getvLine();
                View vLineStickky = recycleSectionContentStickky.getvLine();
                if (vLine != null) {
                    vLine.getLocationOnScreen(loc);
                }

                if (vLineStickky != null) {
                    vLineStickky.getLocationOnScreen(pos);
                }

                if (loc[1] >= pos[1]) {
                    LStickky.setVisibility(View.GONE);
                } else {
                    LStickky.setVisibility(View.VISIBLE);
                }
            }
        });

        /** 幻灯片*/
        bv = new BannerView(mContext);
        bv.setHeightPxOld(350);

        LayoutActDtlSectionContent.SctDtlSectionCallBack sctDtlSectionCallBack = new LayoutActDtlSectionContent.SctDtlSectionCallBack() {
            @Override
            public void onItemClick(int position, int goods_position) {
                recycleSectionContent.doItemClick(position);
                recycleSectionContentStickky.doItemClick(position);
                try {
                    gridLayoutManager.scrollToPositionWithOffset(goods_position, offset);
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onMoreClick() {
                recycleSectionContent.doMoreClick();
                recycleSectionContentStickky.doMoreClick();
            }
        };

        /** 横向楼层导航条*/
        recycleSectionContent = new LayoutActDtlSectionContent(mContext);
        recycleSectionContent.setCallBack(sctDtlSectionCallBack);

        recycleSectionContentStickky = new LayoutActDtlSectionContent(mContext);
        recycleSectionContentStickky.setCallBack(sctDtlSectionCallBack);

        activityDtl = new ActivityDtl();
        activityDtl.setModelCallBack(this);

        onRefresh(pullToRefreshRecycleView);
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        activityDtl.get_activity_detail(activity_id);
    }

    @Override
    public void onData(ActivityDtl data) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (data != null) {
            /** 设置分享格式*/
            shareData = new ShareData();
            shareData.title = !TextUtils.isEmpty(data.share_title) ? data.share_title : data.activity_title;
            String fmt = getString(R.string.share_content_actdtl);
            shareData.content = !TextUtils.isEmpty(data.share_desc) ? data.share_desc : String.format(fmt, data.activity_title);
            shareData.imgs = data.share_img;
            shareData.url = String.format(BaseVar.SHARE_ACTIVITY_DTL, "android", data.activity_id);


            LinearLayout recycleViewHeader = new LinearLayout(mContext);
            recycleViewHeader.setOrientation(LinearLayout.VERTICAL);
            if (data.getBanners() != null && data.getBanners().size() > 0) {
                recycleViewHeader.addView(bv.getMainBannerView(data.getBanners()));
                height = 350 * BaseFunc.getDisplayMetrics(mContext).widthPixels / 720;
            }


            if (data.activity_content != null && data.activity_content.size() > 0) {

                recycleViewHeader.addView(recycleSectionContent.getView(data.activity_content));

                LStickky.setVisibility(View.VISIBLE);
                LStickky.removeAllViews();
                LStickky.addView(recycleSectionContentStickky.getView(data.activity_content));
            }

            goodsAdapter.setHeaderViewAboveSectionone(recycleViewHeader);

            if (!TextUtils.isEmpty(data.activity_title)) {
                setHeadTitle(data.activity_title);
            }

            /** 竖向楼层商品*/
            goodsAdapter.setList(data.activity_content);
            pullToRefreshRecycleView.getRefreshableView().getAdapter().notifyDataSetChanged();
            if (data.activity_content == null || data.activity_content.size() == 0) {
                BaseFunc.showMsg(mContext, getString(R.string.no_data));
            }
        }
    }

    @Override
    public void onError(String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
    }
}
