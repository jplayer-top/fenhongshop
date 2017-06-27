package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.BrandFragment;
import com.fanglin.fenhong.microbuyer.dutyfree.CategoryOneFragment;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-下午5:41.
 * 功能描述: 极速免税店顶部导航适配器
 */
public class DutyfreeNavigationAdapter extends FragmentPagerAdapter {
    List<DutyfreeCategory> list;

    public DutyfreeNavigationAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(List<DutyfreeCategory> list) {
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        if (list.get(position).getType() == 1) {
            return BrandFragment.newInstance(list.get(position).getId());
        } else {
            return CategoryOneFragment.newInstance(list.get(position).getId());
        }
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getTypeName();
    }

    public String getMsg(int postion) {
        return list.get(postion).getMsg();
    }
}
