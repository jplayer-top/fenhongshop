package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fhui.DisableViewPager;
import com.fanglin.fhui.VPAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-24.
 */
public class ConceptActivity extends BaseFragmentActivity {
    @ViewInject(R.id.pager)
    DisableViewPager pager;
    @ViewInject(R.id.LIndicator)
    LinearLayout LIndicator;
    @ViewInject(R.id.tv_close)
    TextView tv_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concept);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        tv_close.setTypeface(iconfont);
        List<View> list = new ArrayList<>();
        View v1 = View.inflate(mContext, R.layout.layout_concept_one, null);
        View v2 = View.inflate(mContext, R.layout.layout_concept_two, null);
        View v3 = View.inflate(mContext, R.layout.layout_concept_three, null);
        View v4 = View.inflate(mContext, R.layout.layout_concept_four, null);
        v4.findViewById(R.id.iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FHCache.setNotFirst(mContext);
                finish();
            }
        });

        list.add(v1);
        list.add(v2);
        list.add(v3);
        list.add(v4);

        VPAdapter adapter = new VPAdapter();
        adapter.setList(list);
        pager.setPagingEnabled(true);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicatorStatus(0);
    }

    @OnClick(value = {R.id.tv_close})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                FHCache.setNotFirst(mContext);
                finish();
                break;
        }
    }


    private void indicatorStatus(int index) {
        for (int i = 0; i < 4; i++) {
            LIndicator.getChildAt(i).setSelected(false);
        }
        LIndicator.getChildAt(index).setSelected(true);
    }
}
