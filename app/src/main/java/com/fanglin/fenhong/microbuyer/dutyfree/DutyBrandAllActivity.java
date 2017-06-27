package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyBrandAll;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyBrandRequest;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyBrandAllAdapter;
import com.fanglin.fhlib.other.FHLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午1:25.
 * 功能描述: 极速免税店 按字母索引对品牌进行分类
 */
public class DutyBrandAllActivity extends BaseFragmentActivityUI implements DutyBrandRequest.DutyBrandRequestCallBack {


    @ViewInject(R.id.pinnedListView)
    PullToRefreshPinnedHeaderListView pinnedListView;
    @ViewInject(R.id.tvIndex)
    TextView tvIndex;
    @ViewInject(R.id.LIndex)
    LinearLayout LIndex;
    DutyBrandRequest brandRequest;

    DutyBrandAllAdapter adapter;
    public static final String[] indexs = {"#", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        skipChk = true;
        View view = View.inflate(mContext, R.layout.activity_dutybrand_all, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        pinnedListView.setMode(PullToRefreshBase.Mode.DISABLED);
        adapter = new DutyBrandAllAdapter(mContext);
        pinnedListView.getRefreshableView().setAdapter(adapter);
        tvHead.setText("品牌");

        LIndex.removeAllViews();
        for (int i = 0; i < indexs.length; i++) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            textView.setLayoutParams(params);
            textView.setTextColor(getResources().getColor(R.color.color_33));
            textView.setGravity(Gravity.CENTER);
            textView.setText(indexs[i]);
            textView.setTextSize(12);
            textView.setTag(i);
            LIndex.addView(textView);
        }

        tvIndex.setVisibility(View.GONE);
        LIndex.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float eventY = event.getY();
                float height = LIndex.getHeight();
                float itemH = height / (indexs.length * 1.0f);

                int index = Math.round(eventY / itemH);
                int maxIndex = indexs.length - 1;
                index = index < 0 ? 0 : (index > maxIndex ? maxIndex : index);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        tvIndex.setVisibility(View.GONE);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        tvIndex.setVisibility(View.VISIBLE);
                        handleSliding(eventY-50, index);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        handleSliding(eventY-50, index);
                        break;
                }
                return true;
            }
        });

        brandRequest = new DutyBrandRequest();
        brandRequest.setBrandRequestCallBack(this);
        brandRequest.getAllList(member);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }


    private void handleSliding(float eventY, int index) {
        tvIndex.setY(eventY);
        String title = indexs[index];
        tvIndex.setText(title);
        int selection = adapter.getPositionByTitle(title);
        FHLog.d("Plucky", "sel:" + selection);
        if (selection >= 0) {
            pinnedListView.getRefreshableView().setSelection(selection);
        }
    }

    @Override
    public void onDutyBrandList(List<BrandMessage> brandList) {
        if (brandList != null && brandList.size() > 0) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            List<DutyBrandAll> allList = DutyBrandAll.getListByAll(brandList);
            adapter.setBrandAll(allList);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }
}
