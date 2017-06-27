package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.HomeNavigation;
import com.fanglin.fenhong.microbuyer.common.FHWebFragment;
import com.fanglin.fenhong.microbuyer.common.HomeNavFragment;
import com.fanglin.fenhong.microbuyer.common.HomeThemeOneFragment;
import com.fanglin.fenhong.microbuyer.common.HomeThemeTwoFragment;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/27-下午7:28.
 * 功能描述:首页顶部导航适配器
 */
public class HomeTopNavigationAdapter extends FragmentPagerAdapter {
    public List<HomeNavigation> list;
    public static final int TYPE_WAP = 0;
    public static final int TYPE_HOME = 1;
    public static final int TYPE_THEME_1 = 2;
    public static final int TYPE_THEME_2 = 3;

    private Context mContext;


    public HomeTopNavigationAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        int type = getThemeType(position);
        switch (type) {
            case TYPE_HOME:
                return HomeNavFragment.newInstance(getItemData(position).nav_url);
            case TYPE_WAP:
                return FHWebFragment.newInstance(getItemData(position).nav_url);
            case TYPE_THEME_1:
                return HomeThemeOneFragment.newInstance(getItemData(position).nav_url);
            case TYPE_THEME_2:
                return HomeThemeTwoFragment.newInstance(getItemData(position).nav_url);
            default:
                return FHWebFragment.newInstance(getItemData(position).nav_url);
        }
    }

    public void setList(List<HomeNavigation> list) {
        this.list = list;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItemData(position).getNavTitle(mContext);
    }

    private HomeNavigation getItemData(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    private int getThemeType(int position) {
        HomeNavigation data = getItemData(position);
        int themeType = 0;
        if (data != null) {
            if (BaseFunc.isValidUrl(data.nav_url)) {
                Uri uri = Uri.parse(data.nav_url);
                if (uri != null) {
                    String themeTypeStr = uri.getQueryParameter("themeType");
                    if (BaseFunc.isInteger(themeTypeStr))
                        themeType = Integer.valueOf(themeTypeStr);
                }
            }
        }
        return themeType;
    }
}
