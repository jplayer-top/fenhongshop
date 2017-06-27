package com.fanglin.fhui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plucky on 2015/9/11.
 * Frament结合ViewPager
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list = new ArrayList<Fragment> ();

    public FragmentViewPagerAdapter (FragmentManager fm) {
        super (fm);
    }

    public void setList (List<Fragment> list) {
        this.list = list;
    }

    @Override
    public Fragment getItem (int position) {
        return list.get (position);
    }

    @Override
    public int getCount () {
        return list.size ();
    }


    @Override
    public void startUpdate (ViewGroup container) {
        super.startUpdate (container);
    }

    @Override
    public void finishUpdate (ViewGroup container) {
        super.finishUpdate (container);
    }
}
