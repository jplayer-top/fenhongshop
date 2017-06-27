package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.DutyfreeBO;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedSectionListView;
import com.fanglin.fenhong.microbuyer.base.event.DutyfreeVipEvent;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyFreeCount;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.ProductDetail;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutGoodsIntro;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DFGoodsDetailAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/8-下午5:30.
 * 功能描述: 极速免税商品详情
 */
public class DFGoodsDetailActivity extends BaseFragmentActivity implements DFGoodsDetailAdapter.DetailClickListener, ProductDetail.ProductDetailRequestCallBack, LayoutProductSpec.DutySpecCallback, DutyFreeCount.DutyFreeCountRequestCallBack, DutyPayDialog.DutyPayDialogListener, LayoutVipBuyerTips.LayoutVipBuyerListener {

    @ViewInject(R.id.pullToRefreshPinnedSectionListView)
    PullToRefreshPinnedSectionListView pullToRefreshPinnedSectionListView;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    @ViewInject(R.id.tvCartNum)
    TextView tvCartNum;
    @ViewInject(R.id.vTopLine)
    View vTopLine;

    DFGoodsDetailAdapter adapter;
    LayoutTaxfee layoutTaxfee;
    LayoutFreightDesc layoutFreightDesc;
    LayoutProductSpec layoutProductSpec;
    LayoutGoodsIntro layoutGoodsIntro;
    LayoutVipBuyerTips layoutVipBuyerTips;

    ProductDetail productDtlReq;
    DutyFreeCount countReq;
    String goodsId, kefuUrl;

    public static final int REQ_PAY = 110;
    DutyPayDialog payDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_goodsdetail);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        goodsId = getIntent().getStringExtra("VAL");
        initView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        adapter = new DFGoodsDetailAdapter(mContext);
        adapter.setDetailListener(this);
        vTopLine.setVisibility(View.GONE);

        View view = multiStateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        if (view != null) {
            TextView tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
            tvEmpty.setText("商品不存在或已下架");
        }
        tvCartNum.setVisibility(View.INVISIBLE);


        layoutTaxfee = new LayoutTaxfee(mContext);
        layoutFreightDesc = new LayoutFreightDesc(mContext);
        layoutProductSpec = new LayoutProductSpec(mContext);
        layoutProductSpec.setDutySpecCallback(this);
        layoutGoodsIntro = new LayoutGoodsIntro(mContext);
        layoutVipBuyerTips = new LayoutVipBuyerTips(this);
        layoutVipBuyerTips.setVipBuyerListener(this);

        pullToRefreshPinnedSectionListView.setMode(PullToRefreshBase.Mode.DISABLED);
        pullToRefreshPinnedSectionListView.getRefreshableView().setAdapter(adapter);

        productDtlReq = new ProductDetail();
        productDtlReq.setProductDtlReqCallback(this);
        countReq = new DutyFreeCount();
        countReq.setRequestCallBack(this);

        productDtlReq.getData(member, goodsId);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        payDialog = new DutyPayDialog(this);
        payDialog.setDutyPayListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        countReq.getCount(member);
    }

    @Override
    public void onReadMeClick() {
        layoutGoodsIntro.show();
    }

    @Override
    public void onTaxClick() {
        layoutTaxfee.show();
    }

    @Override
    public void onSpecClick() {
        layoutProductSpec.show(2);
    }

    @Override
    public void onFreightClick() {
        layoutFreightDesc.show();
    }

    @OnClick(value = {R.id.ivKefu, R.id.tvLeft, R.id.tvRight, R.id.btnBackTop, R.id.FCart, R.id.ivBack})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivKefu:
                if (!TextUtils.isEmpty(kefuUrl)) {
                    BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, kefuUrl);
                } else {
                    BaseFunc.showMsg(mContext, "客服不在线!");
                }
                break;
            case R.id.FCart:
                BaseFunc.gotoActivity(mContext, DutyfreeCartActivity.class, null);
                break;
            case R.id.tvLeft:
                onCart(goodsId, 1);
                break;
            case R.id.tvRight:
                layoutProductSpec.show(1);
                break;
            case R.id.btnBackTop:
                pullToRefreshPinnedSectionListView.getRefreshableView().scrollTo(0, 0);
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    @Override
    public void onProductDtlReqCallback(ProductDetail detail) {
        if (detail != null) {
            kefuUrl = detail.getStoreBaidusales();
            adapter.setDetail(detail);
            layoutProductSpec.refreshView(detail);
            layoutTaxfee.refreshView(detail.getTaxFee(), detail.getTaxDesc());
            layoutFreightDesc.setTitle(detail.getGoodsFreightTitle());
            layoutFreightDesc.refreshView(detail.getGoodsFreightDesc(), detail.getGoodsFreightIntro());
            layoutGoodsIntro.setList(detail.getGoodsIntroInfo());
            layoutVipBuyerTips.refreshData(detail.getPopupInfo());
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onBuy(String goodsId, int buyNum) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        //如果有弹窗信息且没有弹过窗的话则弹窗提示 只弹一次
        if (adapter.hasPopUpInfo() && !fhApp.hasPopUp) {
            layoutVipBuyerTips.show();
            fhApp.hasPopUp = true;
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put(goodsId, buyNum);
            BaseFunc.dutyCartCheck(DFGoodsDetailActivity.this, json.toString(), "2");
        } catch (Exception e) {
            //JSON FUCKER
        }
    }

    @Override
    public void onCart(String goodsId, int buyNum) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        //如果有弹窗信息且没有弹过窗的话则弹窗提示 只弹一次
        if (adapter.hasPopUpInfo() && !fhApp.hasPopUp) {
            layoutVipBuyerTips.show();
            fhApp.hasPopUp = true;
            return;
        }
        addCart(goodsId, buyNum);
    }

    /**
     * 添加购物车
     *
     * @param _goodsId String
     * @param _buyNum  int
     */
    private void addCart(String _goodsId, int _buyNum) {
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    countReq.getCount(member);
                }
            }
        }).addCart(member, _goodsId, _buyNum);
    }

    @Override
    public void onDutyFreeCountRequest(DutyFreeCount data) {
        if (data != null && data.showCartNum()) {
            tvCartNum.setVisibility(View.VISIBLE);
            tvCartNum.setText(data.getCartNumDesc());
        } else {
            tvCartNum.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBuyVipClick() {
        PayEntity entity = new PayEntity();
        entity.vip_pay_type = 1;//购买VIP买手
        entity.lastClassName = this.getClass().getName();
        BaseFunc.gotoPayActivity(this, entity, REQ_PAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_PAY:
                if (resultCode == RESULT_OK && data != null) {
                    int result = data.getIntExtra("result", -1);
                    payDialog.refreshView(result == 0);
                    payDialog.show();
                }
                break;
        }
    }

    @Override
    public void onSubmitClick(boolean isSuccess) {
        if (isSuccess) {
            BaseFunc.gotoActivity(mContext, DutyFreeMainActivity.class, null);
            finish();
        } else {
            onBuyVipClick();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleDutyfreeVipEvent(DutyfreeVipEvent event) {
        if (event != null && event.isVip()) {
            productDtlReq.getData(member, goodsId);
        }
    }

    //VIP购买提示框回调事件
    @Override
    public void onVipSubmit() {
        //开通VIP买手
        onBuyVipClick();
    }

    @Override
    public void onVipCancel() {
        //普通买手价格购买
        addCart(goodsId, 1);
    }
}
