package com.fanglin.fhui;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by plucky on 15-10-24.
 * ViewPager 的简单适配器
 */
public class VPAdapter extends PagerAdapter {
    private List<View> list;

    public VPAdapter() {
        super();
    }

    public void setList(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        (container).addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView(list.get(position));
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
