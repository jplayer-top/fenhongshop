package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.event.HomeBtnDoubleClickEvent;
import com.fanglin.fenhong.microbuyer.base.event.WifiUnconnectHintAfter;
import com.fanglin.fenhong.microbuyer.base.model.HomeNavigation;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.WSDefaultSearchText;
import com.fanglin.fenhong.microbuyer.base.model.WSHomeNavigation;
import com.fanglin.fenhong.microbuyer.buyer.SearchActivity;
import com.fanglin.fenhong.microbuyer.common.adapter.HomeTopNavigationAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import java.util.ArrayList;
import java.util.List;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 作者： Created by Plucky on 2015/8/17.
 * ModifyBy Plucky 2016-3-23 10:00
 * modify by lizhixin on 2016/04/22
 * 添加EventBus接收事件
 */
public class HomeFragment extends BaseFragment implements WSHomeNavigation.HomeNavigationModelCallBack, WSDefaultSearchText.WSDefaultSearchTextModelCallBack {

    View view;

    @ViewInject(R.id.tvTitleSearchIcon)
    TextView tvTitleSearchIcon;

    @ViewInject(R.id.tvIconShare)
    TextView tvIconShare;

    @ViewInject(R.id.tvTitleText)
    TextView tvTitleText;

    @ViewInject(R.id.tvMsgNum)
    TextView tvMsgNum;

    @ViewInject(R.id.indicator)
    TabPageIndicator indicator;
    @ViewInject(R.id.pager)
    ViewPager pager;

    WSHomeNavigation wsHomeNavigation;
    private WSDefaultSearchText wsDefaultSearchText;
    HomeTopNavigationAdapter adapter;

    @ViewInject(R.id.LDoing)
    LinearLayout LDoing;
    @ViewInject(R.id.progressBar)
    ProgressBar progressBar;
    @ViewInject(R.id.ivRefresh)
    ImageView ivRefresh;
    @ViewInject(R.id.tvRefresh)
    TextView tvRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            tvMsgNum.setText(String.valueOf(msgnum.getTotalNum()));
            tvMsgNum.setVisibility(View.VISIBLE);
        } else {
            tvMsgNum.setText("0");
            tvMsgNum.setVisibility(View.INVISIBLE);
        }
    }


    private void initView() {
        BaseFunc.setFont(tvTitleSearchIcon);
        BaseFunc.setFont(tvIconShare);

        adapter = new HomeTopNavigationAdapter(act, act.getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        pager.setOffscreenPageLimit(6);

        wsHomeNavigation = new WSHomeNavigation();
        wsHomeNavigation.setNavigationModelCallBack(this);

        //首页头部搜索文字提示
        wsDefaultSearchText = new WSDefaultSearchText();
        wsDefaultSearchText.setWSDefaultSearchText(this);

        beginRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ViewUtils.inject(this, view);
        initView();
        return view;
    }


    @Override
    public void onNavigationSuccess(List<HomeNavigation> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();
        endRefresh(true);
    }

    @Override
    public void onNavigationError(String errcode) {
        endRefresh(false);
    }

    @Override
    public void onDefaultSearchText(String str) {
        tvTitleText.setText(str);
    }

    private void beginRefresh() {
        LDoing.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        tvRefresh.setVisibility(View.GONE);
        ivRefresh.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);

        wsHomeNavigation.getList(0, 0);
        wsDefaultSearchText.getDefaultSearchText();
    }

    private void endRefresh(boolean isSuccess) {
        progressBar.setVisibility(View.GONE);
        if (isSuccess) {
            pager.setVisibility(View.VISIBLE);
            LDoing.setVisibility(View.GONE);
            ivRefresh.setVisibility(View.GONE);
            tvRefresh.setVisibility(View.GONE);
        } else {
            pager.setVisibility(View.GONE);
            LDoing.setVisibility(View.VISIBLE);
            ivRefresh.setVisibility(View.VISIBLE);
            tvRefresh.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(value = {R.id.ivRefresh, R.id.tvRefresh, R.id.LShare, R.id.LMsg, R.id.LSearch})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivRefresh:
            case R.id.tvRefresh:
                beginRefresh();
                break;
            case R.id.LShare:
                ShareData.fhShare(act, getShareData(), null);
                break;
            case R.id.LMsg:
                BaseFunc.gotoActivity(act, MsgCenterActivity.class, null);
                break;
            case R.id.LSearch:
                BaseFunc.gotoActivity(act, SearchActivity.class, tvTitleText.getText().toString());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onHomeBtnDoubleClick(HomeBtnDoubleClickEvent event) {
        if (pager != null) {
            pager.setCurrentItem(0);//回到第一个频道 -- lizhixin
        }
    }

    private ShareData getShareData() {
        ShareData shareData = new ShareData();
        shareData.title = act.getString(R.string.share_home_title);
        shareData.content = act.getString(R.string.share_home_content);
        shareData.imgs = BaseVar.APP_SHARE_HOME_LOGO;
        List<String> list_image = new ArrayList<>();
        list_image.add(BaseVar.APP_SHARE_HOME_LOGO);
        shareData.mul_img = list_image;
        shareData.url = BaseVar.APP_SHARE_HOME_URL;
        return shareData;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleNoWfi(WifiUnconnectHintAfter wifiUnconnectHintEntity) {
        if (adapter.list == null || adapter.list.size() == 0) {
            beginRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
