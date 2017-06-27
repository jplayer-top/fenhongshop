package com.fanglin.fenhong.microbuyer.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.PaySuccessBonus;
import com.fanglin.fenhong.microbuyer.base.model.PopupInfo;
import com.fanglin.fenhong.microbuyer.common.adapter.GoodsRecommendRCVAdapter;
import com.fanglin.fenhong.microbuyer.microshop.LayoutBefansPopup;
import com.fanglin.fenhong.microbuyer.microshop.LayoutOrderSuccess;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-11-22.
 * 支付成功
 */
public class PaySuccessActivity extends BaseFragmentActivityUI implements View.OnClickListener, GoodsScheme.GoodsSchemeModelCallBack, PaySuccessBonus.PSBModelCallBack, PullToRefreshBase.OnRefreshListener2, PopupInfo.PopupInfoModelCallBack {

    RecyclerViewHeader rcvheader;
    TextView tv_icon_success;
    TextView tv_pay_amount;

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.btn_bonus)
    Button btn_bonus;

    int curpage_recommend = 1;
    GoodsScheme goodsScheme;
    GoodsRecommendRCVAdapter adapter;

    LayoutOrderSuccess layoutOrderSuccess;
    LayoutBefansPopup layoutBefansPopup;


    private double pay_amount;
    private double use_amount;//使用站内余额
    private String pay_sn;

    PopupInfo popupInfoReq;
    PaySuccessBonus paySuccessBonusReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_pay_success, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            String val = getIntent().getStringExtra("VAL");
            PayEntity entity = new Gson().fromJson(val, PayEntity.class);

            pay_amount = entity.pay_amount;
            use_amount = entity.youhui;
            pay_sn = entity.pay_sn;
        } catch (Exception e) {
            pay_amount = 0;
        }
        initView();
    }

    @Override
    public void onPSBData(PaySuccessBonus PSBData) {
        if (PSBData != null) {
            btn_bonus.setVisibility(View.VISIBLE);
            btn_bonus.setTag(PSBData);
        }
    }

    @Override
    public void onPSBError(String errcode) {
        btn_bonus.setVisibility(View.INVISIBLE);
        btn_bonus.setTag(null);
    }

    @Override
    public void onPopupInfo(PopupInfo data) {
        if (data != null) {
            //有则先弹成为达人的弹框 且弹完之后再弹红包弹窗
            layoutBefansPopup.refreshData(data);
            layoutBefansPopup.show();
        } else {
            //没有则弹红包
            onBonusClick();
        }
    }

    private void initView() {
        paySuccessBonusReq = new PaySuccessBonus();
        layoutOrderSuccess = new LayoutOrderSuccess(btn_bonus);
        paySuccessBonusReq.setModelCallBack(this);
        paySuccessBonusReq.setIfShowMsg(false);
        btn_bonus.setVisibility(View.INVISIBLE);
        btn_bonus.setOnClickListener(this);

        layoutBefansPopup = new LayoutBefansPopup(this);
        layoutBefansPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //成为达人的弹窗弹完之后再弹红包的弹窗
                onBonusClick();
            }
        });

        setHeadTitle(R.string.lbl_pay_success);
        DecimalFormat df = new DecimalFormat("#0.00");

        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);
        adapter = new GoodsRecommendRCVAdapter(mContext);

        pullToRefreshRecycleView.getRefreshableView().addItemDecoration(new MutiItemDecoration(MutiItemDecoration.Type.ALL, 2));
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        rcvheader = RecyclerViewHeader.fromXml(mContext, R.layout.layout_paysuccess_header);
        tv_icon_success = (TextView) rcvheader.findViewById(R.id.tv_icon_success);
        tv_pay_amount = (TextView) rcvheader.findViewById(R.id.tv_pay_amount);
        TextView tv_order = (TextView) rcvheader.findViewById(R.id.tv_order);
        TextView tv_close = (TextView) rcvheader.findViewById(R.id.tv_close);
        tv_order.setOnClickListener(this);
        tv_close.setOnClickListener(this);

        BaseFunc.setFont(tv_icon_success);
        if (use_amount > 0) {
            String fmt = getString(R.string.fmt_pay_amountby_deposit);
            fmt = String.format(fmt, df.format(pay_amount), df.format(use_amount));
            tv_pay_amount.setText(Html.fromHtml(fmt));
        } else {
            String fmt = getString(R.string.fmt_pay_amount);
            fmt = String.format(fmt, df.format(pay_amount));
            tv_pay_amount.setText(Html.fromHtml(fmt));
        }
        rcvheader.attachTo(pullToRefreshRecycleView.getRefreshableView());


        goodsScheme = new GoodsScheme("cainixihuan");
        goodsScheme.setModelCallBack(this);
        paySuccessBonusReq.getData(member, pay_sn);

        popupInfoReq = new PopupInfo();
        popupInfoReq.setModelCallBack(this);
        popupInfoReq.getData(member, 1);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage_recommend = 1;
        goodsScheme.getList(-1, BaseVar.REQUESTNUM, curpage_recommend);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage_recommend++;
        goodsScheme.getList(-1, BaseVar.REQUESTNUM, curpage_recommend);
    }

    @Override
    public void RError(String curType, String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (curpage_recommend > 1) {
            pullToRefreshRecycleView.showNoMore();
        } else {
            BaseFunc.showMsg(mContext, getString(R.string.no_data));
        }
    }

    @Override
    public void RgetList(String curType, List<GoodsScheme> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (curpage_recommend > 1) {
            adapter.addList(list);
            adapter.notifyDataSetChanged();
            pullToRefreshRecycleView.onAppendData();
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
            pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order:
                BaseFunc.gotoActivity(this, OrderActivity.class, "20");
                break;
            case R.id.tv_close:
                finish();
                break;
            case R.id.btn_bonus:
                onBonusClick();
                break;
        }
    }

    private void onBonusClick() {
        if (layoutOrderSuccess != null) {
            if (btn_bonus.getTag() != null) {
                PaySuccessBonus psb = (PaySuccessBonus) btn_bonus.getTag();
                layoutOrderSuccess.show(psb);
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        setResult(RESULT_OK);
    }
}
