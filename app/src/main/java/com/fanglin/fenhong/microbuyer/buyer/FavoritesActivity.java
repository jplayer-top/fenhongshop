package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.event.FavoritesEditEvent;
import com.fanglin.fenhong.microbuyer.common.MsgCenterActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 作者： Created by Plucky on 15-9-16.
 * modify by lizhixin on 2016-06-03
 */
public class FavoritesActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.tvHead)
    TextView tvHead;
    @ViewInject(R.id.tvMsg)
    TextView tvMsg;
    @ViewInject(R.id.tvEdit)
    TextView tvEdit;
    @ViewInject(R.id.tvMsgNum)
    TextView tvMsgNum;

    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.indicator)
    private TabPageIndicator indicator;
    private static final String[] TITLES = {"收藏商品", "收藏店铺", "关注品牌", "我的足迹"};//"收藏微店", "收藏达人", "收藏时光",
    private static final String[] TYPES = {"goods", "shop", "brand", "footprint"};//"microshop", "dr", "time",
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ViewUtils.inject(this);
        initFragment();
    }

    private void initFragment() {
        int index;
        try {
            index = Integer.valueOf(getIntent().getStringExtra("VAL"));
        } catch (Exception e) {
            index = 0;
        }
        fragments = new ArrayList<>();

        FavoritesGoodsFragment collectGoods = new FavoritesGoodsFragment();
        fragments.add(collectGoods);

        FavoritesShopFragment collectShop = new FavoritesShopFragment();
        fragments.add(collectShop);

        /** 因业务需求--暂时屏蔽 微店收藏、达人及时光功能
         FavoritesMicroshopFragment collectMicroShop = new FavoritesMicroshopFragment();
         fragments.add(collectMicroShop);

         FavoritesDrFragment collectDr = new FavoritesDrFragment();
         fragments.add(collectDr);

         FavoritesTimeFragment collectTime = new FavoritesTimeFragment();
         fragments.add(collectTime);
         */

        FavoritesBrandFragment brandFragment = new FavoritesBrandFragment();
        fragments.add(brandFragment);

        FavoritesFootprintFragment footprint = new FavoritesFootprintFragment();
        fragments.add(footprint);

        viewpager.setAdapter(new CollectionsFragmentAdapter(getSupportFragmentManager()));
        indicator.setViewPager(viewpager);

        viewpager.addOnPageChangeListener(this);

        tvHead.setText(getString(R.string.favorites));
        tvEdit.setTypeface(iconfont);
        tvMsg.setTypeface(iconfont);

        viewpager.setOffscreenPageLimit(5);
        viewpager.setCurrentItem(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            tvMsgNum.setText(String.valueOf(msgnum.getTotalNum()));
            tvMsgNum.setVisibility(View.VISIBLE);
        } else {
            tvMsgNum.setText("0");
            tvMsgNum.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(value = {R.id.tvEdit, R.id.tvMsg, R.id.ivBack})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tvMsg:
                BaseFunc.gotoActivity(this, MsgCenterActivity.class, null);
                break;
            case R.id.tvEdit:
                EventBus.getDefault().post(new FavoritesEditEvent(getCurItemByIndex(viewpager.getCurrentItem())));
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private String getCurItemByIndex(int currentItem) {
        return TYPES[currentItem];
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 2) {
            tvEdit.setVisibility(View.GONE);
        } else {
            tvEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class CollectionsFragmentAdapter extends FragmentPagerAdapter {

        public CollectionsFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

}
