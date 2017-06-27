package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BannerView;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.model.ChannelOneData;
import com.fanglin.fenhong.microbuyer.common.adapter.ThemeOneAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public final class HomeThemeOneFragment extends BaseFragment implements ChannelOneData.ChannelClassModelCallBack, PullToRefreshBase.OnRefreshListener2 {


    @ViewInject(R.id.pinnedListView)
    PullToRefreshPinnedHeaderListView pullToRefreshPinnedHeaderListView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    private View headerView;

    ThemeOneAdapter adapter;

    private static final String KEY_CONTENT = "HomeThemeOneFragment:Content";
    ChannelOneData channelOneDataReq;

    public static HomeThemeOneFragment newInstance(String url) {
        HomeThemeOneFragment fragment = new HomeThemeOneFragment();
        fragment.murl = url;
        return fragment;
    }

    private String murl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            murl = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_homenav, container, false);
        ViewUtils.inject(this, layout);
        initView();
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, murl);
    }

    private void initView() {
        pullToRefreshPinnedHeaderListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshPinnedHeaderListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshPinnedHeaderListView.setOnRefreshListener(this);

        pullToRefreshPinnedHeaderListView.getRefreshableView().setPinHeaders(false);
        pullToRefreshPinnedHeaderListView.getRefreshableView().setBackUpView(btnBackTop, 10);

        channelOneDataReq = new ChannelOneData();
        channelOneDataReq.setModelCallBack(this);

        adapter = new ThemeOneAdapter(act);
        onPullDownToRefresh(pullToRefreshPinnedHeaderListView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        int channel_id = getChannelId(murl);
        channelOneDataReq.getData(channel_id);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        refreshView.getLoadingLayoutProxy().setPullLabel(getString(R.string.pullup_error_tips));
        refreshView.onRefreshComplete();
    }

    @Override
    public void onChannelOneData(ChannelOneData channelOneData) {
        if (channelOneData != null) {
            if (headerView != null) {
                pullToRefreshPinnedHeaderListView.getRefreshableView().removeHeaderView(headerView);
            }

            if (channelOneData.channel_slides != null && channelOneData.channel_slides.size() > 0) {
                BannerView bannerView = new BannerView(act);
                bannerView.setHeightPxNew(400);
                bannerView.setShowType(FHImageViewUtil.SHOWTYPE.THEMEONE_BANNER);
                headerView = bannerView.getMainBannerView(channelOneData.channel_slides);
                pullToRefreshPinnedHeaderListView.getRefreshableView().addHeaderView(headerView);
            }

            adapter.setData(channelOneData);
            pullToRefreshPinnedHeaderListView.getRefreshableView().setAdapter(adapter);
            pullToRefreshPinnedHeaderListView.onRefreshComplete();
        } else {
            pullToRefreshPinnedHeaderListView.onRefreshComplete();
        }
    }


}
