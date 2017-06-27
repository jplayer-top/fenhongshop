package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanglin.fenhong.microbuyer.microshop.CommissionFragment;
import com.fanglin.fenhong.microbuyer.microshop.MicroShopFragment;
import com.fanglin.fenhong.microbuyer.microshop.TeamFragment;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/3-下午1:27.
 * 功能描述: 我的微店Activity的ViewPager Adapter
 */
public class MicroshopPagerAdapter extends FragmentPagerAdapter {
    public MicroshopPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return MicroShopFragment.newInstance("" + position);
        } else if (position == 1) {
            return CommissionFragment.newInstance("" + position);
        } else {
            return TeamFragment.newInstance("" + position);
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
