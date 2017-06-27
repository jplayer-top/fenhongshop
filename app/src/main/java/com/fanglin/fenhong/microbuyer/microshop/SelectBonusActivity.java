package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.CartCheckData;
import com.fanglin.fenhong.microbuyer.microshop.adapter.BonusAdapter;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 2015/12/1.
 * 选择优惠券列表
 */
public class SelectBonusActivity extends BaseFragmentActivityUI {
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.ll_use)
    LinearLayout ll_use;
    @ViewInject(R.id.tv_use)
    TextView tv_use;
    @ViewInject(R.id.ll_not)
    LinearLayout ll_not;
    @ViewInject(R.id.tv_not)
    TextView tv_not;

    BonusAdapter adapter;
    CartCheckData.COUPON_LIST coupon_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_selected_bonus, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        try {
            coupon_list = new Gson().fromJson(getIntent().getStringExtra("VAL"), CartCheckData.COUPON_LIST.class);
        } catch (Exception e) {
            coupon_list = null;
        }

        initView();
    }

    @OnClick(value = {R.id.ll_use, R.id.ll_not})
    public void onViewClick(View v) {
        ll_use.setSelected(false);
        ll_not.setSelected(false);
        switch (v.getId()) {
            case R.id.ll_use:
                ll_use.setSelected(true);
                adapter.actionid = 0;
                adapter.setList(coupon_list != null ? coupon_list.available : null);
                adapter.notifyDataSetChanged();
                tv_use.setText(coupon_list != null ? coupon_list.getAvailableNum() : getString(R.string.tag_bonus_use));
                tv_not.setText(coupon_list != null ? coupon_list.getUnavailableNum() : getString(R.string.tag_bonus_not));
                break;
            case R.id.ll_not:
                ll_not.setSelected(true);
                adapter.actionid = 1;
                adapter.setList(coupon_list != null ? coupon_list.unavailable : null);
                adapter.notifyDataSetChanged();
                tv_use.setText(coupon_list != null ? coupon_list.getAvailableNum() : getString(R.string.tag_bonus_use));
                tv_not.setText(coupon_list != null ? coupon_list.getUnavailableNum() : getString(R.string.tag_bonus_not));
                break;
        }
    }

    private void initView() {
        setHeadTitle(R.string.title_select_bonus);
        adapter = new BonusAdapter(mContext);
        adapter.actionid = 0;
        adapter.setList(coupon_list != null ? coupon_list.available : null);
        listView.setAdapter(adapter);
        tv_use.setText(coupon_list != null ? coupon_list.getAvailableNum() : getString(R.string.tag_bonus_use));
        tv_not.setText(coupon_list != null ? coupon_list.getUnavailableNum() : getString(R.string.tag_bonus_not));
        ll_use.setSelected(true);
    }
}
