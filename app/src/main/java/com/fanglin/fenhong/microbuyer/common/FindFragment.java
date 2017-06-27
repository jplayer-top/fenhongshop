package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanglin.fenhong.mapandlocate.baiduloc.BaiduLocateUtil;
import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.event.WifiUnconnectHintAfter;
import com.fanglin.fenhong.microbuyer.base.model.GPSLocation;
import com.fanglin.fenhong.microbuyer.base.model.ShopClass;
import com.fanglin.fenhong.microbuyer.base.model.ShopSettings;
import com.fanglin.fenhong.microbuyer.common.adapter.SectionFindAdapter;
import com.fanglin.fhlib.other.FHLib;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 作者： Created by Plucky on 2015/8/17.
 */
public class FindFragment extends BaseFragment implements ShopClass.ShopClassModelCallBack, PullToRefreshBase.OnRefreshListener, ShopSettings.ShopSettingCallBack {

    View view;
    FHApp fhapp;

    ShopClass shopClass;

    @ViewInject(R.id.llRefresh)
    LinearLayout llRefresh;

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    SectionFindAdapter findAdapter;

    ShopSettings shopSettingsReq;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(act, R.layout.fragment_find, null);
        ViewUtils.inject(this, view);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        fhapp = ((FHApp) act.getApplication());

        shopClass = new ShopClass();
        shopClass.setModelCallBack(this);

        shopSettingsReq = new ShopSettings();
        shopSettingsReq.setModelCallBack(this);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        findAdapter = new SectionFindAdapter(act);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(findAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(act, 3);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(findAdapter, gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(lookup);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(gridLayoutManager);

        /**
         * 只在第一次加载的时候去判断是否断网，来显示这两个控件
         */
        if (FHLib.isNetworkConnected(getActivity()) == 0) {
            llRefresh.setVisibility(View.VISIBLE);
            pullToRefreshRecycleView.setVisibility(View.GONE);
        } else {
            llRefresh.setVisibility(View.GONE);
            pullToRefreshRecycleView.setVisibility(View.VISIBLE);
        }

        refreshData(1);
    }

    /**
     * 刷新数据
     *
     * @param action 0 刷新所有 1 刷新列表 2 刷新Banner
     */
    private void refreshData(int action) {

        if (action == 1 || action == 0) {
            getLoc();
            if (shopClass != null) {
                shopClass.getList(null);
            }
        }

        if (action == 2 || action == 0) {
            if (shopSettingsReq != null) {
                shopSettingsReq.getData(member);
            }
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        refreshData(0);
    }

    /**
     * 精品微店分类
     */
    @Override
    public void onSCError(String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
    }

    @Override
    public void onSCList(final List<ShopClass> lst) {
        pullToRefreshRecycleView.onRefreshComplete();
        findAdapter.setList(lst);
        findAdapter.notifyDataSetChanged();

        pullToRefreshRecycleView.setVisibility(View.VISIBLE);
        llRefresh.setVisibility(View.GONE);
    }


    /**
     * A
     * B
     * C
     * D
     * E
     * F
     */

    private void getLoc() {
        BaiduLocateUtil.getinstance(act.getApplicationContext()).start();
        BaiduLocateUtil.getinstance(act.getApplicationContext()).setCallBack(new BaiduLocateUtil.LocationCallBack() {
            @Override
            public void onChange(FHLocation location) {
                if (location != null && act != null) {
                    fhapp.resetLocation();
                    FHCache.setLocation2SandBox(act, location);
                    FHCache.recordGPS(fhapp, member, location);//记录GPS定位信息
                    findAdapter.notifyDataSetChanged();
                    BaiduLocateUtil.getinstance(act.getApplicationContext()).stop();

                    /** 提交定位信息
                     */
                    new GPSLocation().record(fhapp, member);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleNoWfi(WifiUnconnectHintAfter wifiUnconnectHintEntity) {
        if (findAdapter.getListCount() == 0) {
            onRefresh(pullToRefreshRecycleView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onShopSettingData(ShopSettings settings) {
        findAdapter.setShopSettings(settings);
    }
}
