package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.event.MicroshopInfoEvent;
import com.fanglin.fenhong.microbuyer.base.model.MicroshopInfo;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.MsgCenterActivity;
import com.fanglin.fenhong.microbuyer.microshop.adapter.MicroshopPagerAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;

/**
 * 作者： Created by Plucky on 2015/11/5.
 * 我的微店 第一次重构 By Plucky 2016-06-03 11:30
 */
public class MicroshopListActivity extends BaseFragmentActivityUI implements ViewPager.OnPageChangeListener, MicroshopInfo.MSIModelCallBack {


    MicroshopPagerAdapter adapter;
    @ViewInject(R.id.viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.tvShop)
    TextView tvShop;
    @ViewInject(R.id.tvCommission)
    TextView tvCommission;
    @ViewInject(R.id.tvTeam)
    TextView tvTeam;

    ShareData shareData;
    MicroshopInfo microshopInfoRequset;
    int curIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_micrshop_list, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        String val = getIntent().getStringExtra("VAL");
        if (BaseFunc.isInteger(val)) {
            curIndex = Integer.valueOf(val);
        }
        initView();
    }

    private void initView() {
        adapter = new MicroshopPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);

        viewPager.setCurrentItem(curIndex);
        onIndexChange(curIndex);

        microshopInfoRequset = new MicroshopInfo();
        microshopInfoRequset.setModelCallBack(this);

        /**
         * 加上这句 EventBus 失效？？？
         */
        //refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
        /**
         * 获取微店信息
         */
        microshopInfoRequset.getData(member_id);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onIndexChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(value = {R.id.tvShop, R.id.tvCommission, R.id.tvTeam})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvShop:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tvCommission:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tvTeam:
                viewPager.setCurrentItem(2
                );
                break;
        }
    }

    private void onIndexChange(int index) {
        String titleStr;
        if (index == 2) {
            titleStr = getString(R.string.my_group);
            setVisibleOfTvExtra(false);
            enableTvMore(R.string.invite, false);
        } else if (index == 1) {
            titleStr = getString(R.string.my_commission);
            setVisibleOfTvExtra(false);
            setVisibleOfTvMore(false);
        } else {
            titleStr = getString(R.string.microshop);
            enableTvExtra(R.string.if_share, true);
            enableTvMore(R.string.if_msg, true);
        }

        setHeadTitle(titleStr);


        tvShop.setSelected(index == 0);
        tvCommission.setSelected(index == 1);
        tvTeam.setSelected(index == 2);
        refreshMsgDot();
    }


    @Override
    public void onTvExtraClick() {
        super.onTvExtraClick();
        if (shareData != null) {
            ShareData.fhShare(this, shareData, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMsgDot();
    }

    private void refreshMsgDot() {
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            if (viewPager.getCurrentItem() == 0) {
                enableMsgDot(true);
            } else {
                enableMsgDot(false);
            }

        } else {
            enableMsgDot(false);
        }
    }

    /**
     * 店铺信息
     */
    @Override
    public void onMSIData(MicroshopInfo microshopInfo) {
        if (microshopInfo != null) {
            /** 设定分享格式*/
            shareData = new ShareData();
            shareData.content = getString(R.string.share_content_microshop);
            shareData.url = String.format(BaseVar.SHARE_FANCYSHOP, microshopInfo.getShop_id());
            shareData.title = microshopInfo.getShop_name();

            if (BaseFunc.isValidUrl(microshopInfo.getShop_logo())) {
                shareData.imgs = microshopInfo.getShop_logo();
            }
        }

        //通知Fragment微店信息获取到了
        EventBus.getDefault().post(new MicroshopInfoEvent(microshopInfo));
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        if (viewPager.getCurrentItem() == 0) {
            //消息中心
            BaseFunc.gotoActivity(this, MsgCenterActivity.class, null);
        }

        if (viewPager.getCurrentItem() == 2) {
            //邀请好友
            if (member == null) {
                BaseFunc.gotoLogin(this);
                return;
            }
            if (member.if_shoper == 1) {
                BaseFunc.gotoActivity(this, FHBrowserActivity.class, BaseVar.INVITE_FRIEND);
            } else {
                BaseFunc.gotoActivity(this, FHBrowserActivity.class, BaseVar.MICROSHOP_GUIDE);
            }
        }
    }

    @Override
    public void onBackClick() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackClick();
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(0);
        }
    }
}
