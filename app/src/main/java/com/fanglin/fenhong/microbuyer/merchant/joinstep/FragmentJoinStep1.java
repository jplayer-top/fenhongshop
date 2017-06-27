package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.VarInstance;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.model.JoinStep1;
import com.fanglin.fenhong.microbuyer.merchant.adapter.JoinStep1Adapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 15-9-30.
 * 供应商审核第一步--检查当前到达的步骤
 */
public class FragmentJoinStep1 extends BaseFragment implements JoinStep1.JoinStep1CallBack {
    View view;
    @ViewInject(R.id.tv_account)
    TextView tv_account;
    @ViewInject(R.id.tv_shopname)
    TextView tv_shopname;
    @ViewInject(R.id.tv_feelv)
    TextView tv_feelv;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_shopcls)
    TextView tv_shopcls;
    @ViewInject(R.id.tv_bond)
    TextView tv_bond;
    @ViewInject(R.id.tv_topay)
    TextView tv_topay;
    @ViewInject(R.id.rcv)
    RecyclerView rcv;
    @ViewInject(R.id.tv_tips)
    TextView tv_tips;
    @ViewInject(R.id.btn_submit)
    TextView btn_submit;

    String step;
    JoinStep1 step1;
    JoinStep1Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(act, R.layout.fragment_joinstep1, null);
        ViewUtils.inject(this, view);
        step1 = new JoinStep1();
        step1.setModelCallBack(this);
        rcv.setLayoutManager(new AutoGridLayoutManager(act, 1));
        adapter = new JoinStep1Adapter(act);
        rcv.setAdapter(adapter);
        btn_submit.setVisibility(View.GONE);
    }

    public void getData() {
        if (step1 != null) step1.getData(member, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getData();
        return view;
    }

    @OnClick(value = {R.id.btn_submit})
    public void onViewClick(View v) {
        if (TextUtils.equals("6", step) || TextUtils.equals("7", step)) {
            step1.getData(member, step);
            return;
        }

        //确认并付款
        BaseFunc.gotoActivity4Result(this, JoinstepPayActivity.class, null, 0x001);
    }

    @Override
    public void onSuccess(JoinStep1 joinStep1) {
        tv_account.setText(joinStep1.seller_name);
        tv_shopname.setText(joinStep1.store_name);
        tv_feelv.setText(joinStep1.charge_std);
        tv_time.setText(joinStep1.joinin_year + "年");
        tv_shopcls.setText(joinStep1.store_class);
        tv_bond.setText(joinStep1.deposit + "元");
        tv_topay.setText(joinStep1.pay_amount + "元");
        tv_tips.setText(joinStep1.tip);

        adapter.setList(joinStep1.store_class_names, joinStep1.store_class_deduct_rates);
        rcv.getAdapter().notifyDataSetChanged();

        step = joinStep1.step;
        parseStep(step);
    }

    private void parseStep(String step) {
        int s;
        if (step == null || !TextUtils.isDigitsOnly(step)) {
            s = 0;
        } else {
            s = Integer.valueOf(step);
        }
        switch (s) {
            case 1://在填写公司信息的步骤
                ((JoinStepActivity) act).changePage(1);
                break;
            case 2://在完善店铺信息的步骤
                ((JoinStepActivity) act).changePage(2);
                break;
            case 0://默认
            case 3://入驻审核中
            case 5://付款信息审核中
            case 8://开店成功
                btn_submit.setVisibility(View.GONE);
                break;
            case 4://入驻申请审核通过--待付款
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setText(getString(R.string.merchantin3_submit));
                break;
            case 6:// 入驻申请审核失败--重新提交
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setText(getString(R.string.merchantin_submit_btn1));
                break;
            case 7://付款审核失败--重新付款
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setText(getString(R.string.merchantin_submit_btn2));
                break;


        }
    }

    @Override
    public void onError(String errcode) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 0x001:
                step1.getData(member, null);
                break;
        }
    }
}
