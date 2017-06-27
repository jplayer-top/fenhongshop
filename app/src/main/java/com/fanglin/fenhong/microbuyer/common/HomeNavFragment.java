package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BannerView;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.event.FHLoginEvent;
import com.fanglin.fenhong.microbuyer.base.event.HomeBtnDoubleClickEvent;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.base.model.HomeNavData;
import com.fanglin.fenhong.microbuyer.buyer.adapter.HomeSectionAdapter;
import com.fanglin.fhlib.other.FHLib;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/23-上午11:43.
 * 功能描述:新首页
 * modify by lizhixin on 2016/04/22
 * 添加EventBus接收事件
 */
public class HomeNavFragment extends BaseFragment implements HomeNavData.HomeNavDataModelCallBack, GoodsScheme.GoodsSchemeModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.pinnedListView)
    PullToRefreshPinnedHeaderListView pullToRefreshPinnedHeaderListView;

    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    @ViewInject(R.id.LDoing)
    LinearLayout LDoing;
    @ViewInject(R.id.progressBar)
    ProgressBar progressBar;
    @ViewInject(R.id.ivRefresh)
    ImageView ivRefresh;
    @ViewInject(R.id.tvRefresh)
    TextView tvRefresh;

    private View headerView;


    private static final String KEY_CONTENT = "HomeNavFragment:Content";
    private String mContent = "???";

    HomeNavData homeNavDataReq;
    HomeSectionAdapter adapter;
    /**
     * 常见方法
     */

    int curpage = 2;
    public static final int REQNUM = 10;
    GoodsScheme goodsFancy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homenav, container, false);
        ViewUtils.inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onHomeNavData(HomeNavData data) {
        EventBus.getDefault().post(new MessageEvent(false, data));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    public static HomeNavFragment newInstance(String content) {
        HomeNavFragment fragment = new HomeNavFragment();
        fragment.mContent = content;
        return fragment;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 2;
        pullToRefreshPinnedHeaderListView.resetPull(PullToRefreshBase.Mode.BOTH);
        homeNavDataReq.getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        goodsFancy.getList(-1, REQNUM, curpage);
    }

    @Override
    public void RgetList(String curType, List<GoodsScheme> list) {
        pullToRefreshPinnedHeaderListView.onRefreshComplete();
        curpage++;
        if (list != null && list.size() > 0) {
            adapter.addRecommentGoods(list);
            pullToRefreshPinnedHeaderListView.onAppendData(200);
        }
    }

    @Override
    public void RError(String curType, String errcode) {
        pullToRefreshPinnedHeaderListView.onRefreshComplete();
        pullToRefreshPinnedHeaderListView.showNoMore();
    }

    private void initView() {
        pullToRefreshPinnedHeaderListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshPinnedHeaderListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshPinnedHeaderListView.setOnRefreshListener(this);

        pullToRefreshPinnedHeaderListView.getRefreshableView().setPinHeaders(false);
        pullToRefreshPinnedHeaderListView.getRefreshableView().setBackUpView(btnBackTop, 14);

        homeNavDataReq = new HomeNavData();
        homeNavDataReq.setModelCallBack(this);

        goodsFancy = new GoodsScheme("jingxuantuijian");
        goodsFancy.setModelCallBack(this);

        adapter = new HomeSectionAdapter(act);

        onPullDownToRefresh(pullToRefreshPinnedHeaderListView);
        EventBus.getDefault().post(new MessageEvent(true, null));
    }

    /**
     * 处理刷新状态
     *
     * @param isBegin 开始状态
     * @param data    刷新过后的数据
     */
    private void handleRefreshStatus(boolean isBegin, HomeNavData data) {
        pullToRefreshPinnedHeaderListView.resetPull(PullToRefreshBase.Mode.BOTH);
        if (isBegin) {
            LDoing.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            pullToRefreshPinnedHeaderListView.setVisibility(View.GONE);
            /**
             * 只在第一次加载的时候去判断是否断网，来显示这两个控件
             */
            if (FHLib.isNetworkConnected(getActivity()) == 0) {
                ivRefresh.setVisibility(View.VISIBLE);
                tvRefresh.setVisibility(View.VISIBLE);
            } else {
                ivRefresh.setVisibility(View.GONE);
                tvRefresh.setVisibility(View.GONE);
            }

        } else {
            LDoing.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            pullToRefreshPinnedHeaderListView.setVisibility(View.VISIBLE);

            ivRefresh.setVisibility(View.GONE);
            tvRefresh.setVisibility(View.GONE);
            if (data != null) {
                // 处理界面
                if (headerView != null) {
                    pullToRefreshPinnedHeaderListView.getRefreshableView().removeHeaderView(headerView);
                }
                if (data.slides != null && data.slides.size() > 0) {
                    BannerView bannerView = new BannerView(act);
                    bannerView.setHeightPxNew(460);
                    bannerView.setShowType(FHImageViewUtil.SHOWTYPE.NEWHOME_BANNER);
                    headerView = bannerView.getMainBannerView(data.slides);
                    pullToRefreshPinnedHeaderListView.getRefreshableView().addHeaderView(headerView);
                }

                pullToRefreshPinnedHeaderListView.getRefreshableView().setAdapter(adapter);
                adapter.setData(data);
                adapter.notifyDataSetChanged();
                pullToRefreshPinnedHeaderListView.onRefreshComplete();
            } else {
                LDoing.setVisibility(View.VISIBLE);
                pullToRefreshPinnedHeaderListView.onRefreshComplete();
            }
        }
    }

    @OnClick(value = {R.id.ivRefresh, R.id.tvRefresh})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivRefresh:
            case R.id.tvRefresh:
                onPullDownToRefresh(pullToRefreshPinnedHeaderListView);
                EventBus.getDefault().post(new MessageEvent(true, null));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void HomeNavEvent(MessageEvent event) {
        if (event != null) {
            handleRefreshStatus(event.isBegin, event.data);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onHomeBtnDoubleClick(HomeBtnDoubleClickEvent event) {
        if (pullToRefreshPinnedHeaderListView != null) {
            pullToRefreshPinnedHeaderListView.getRefreshableView().setSelection(0);//回顶部 -- lizhixin
        }
    }

    //网络故障后 点击图标重新刷新事件
    class MessageEvent {
        public boolean isBegin;//是否是第一次进入界面
        public HomeNavData data;

        public MessageEvent(boolean isBegin, HomeNavData data) {
            this.isBegin = isBegin;
            this.data = data;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleLogin(FHLoginEvent loginEvent) {
        if (loginEvent != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
