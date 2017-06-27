package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fhui.FragmentViewPagerAdapter;
import com.fanglin.fhui.DisableViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-30.
 */
public class JoinStepActivity extends BaseFragmentActivityUI {
    @ViewInject(R.id.vpager)
    DisableViewPager vpager;

    List<Fragment> list;
    String[] titles;
    FragmentViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_joinstep, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }


    public void initView() {
        list = new ArrayList<>();
        titles = new String[]{getString(R.string.joinstep_0), getString(R.string.joinstep_1), getString(R.string.joinstep_2)};
        setHeadTitle(titles[0]);

        final FragmentJoinStep1 step1 = new FragmentJoinStep1();
        list.add(step1);
        final FragmentJoinStep2 step2 = new FragmentJoinStep2();
        list.add(step2);
        final FragmentJoinStep3 step3 = new FragmentJoinStep3();
        list.add(step3);

        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        adapter.setList(list);
        vpager.setAdapter(adapter);
        vpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setHeadTitle(titles[position]);
                switch (position) {
                    case 0:
                        step1.getData();
                        break;
                    case 1:
                        step2.getData();
                        break;
                    case 2:
                        step3.getData();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpager.setCurrentItem(0);
        vpager.setOffscreenPageLimit(1);
    }

    public void changePage(int index) {
        vpager.setCurrentItem(index);
    }


}
