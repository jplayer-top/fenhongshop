package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.WSReturnGoodsGetRefundDtl;
import com.fanglin.fenhong.microbuyer.common.adapter.RefundSuccessPicAdapter;
import com.fanglin.fenhong.microbuyer.microshop.BalanceActivity;
import com.fanglin.fhlib.other.FHLib;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 退款成功  Activity
 * Created by lizhixin on 2015/11/12.
 */
public class ReturnGoodsRefundSuccessActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.scv_top)
    private ScrollView scvTop;
    @ViewInject(R.id.tv_icon_success)
    private TextView tvIconSuccess;
    @ViewInject(R.id.tv_money)
    private TextView tvMoney;
    @ViewInject(R.id.tv_time)
    private TextView tvTime;
    @ViewInject(R.id.tv_shop_name)
    private TextView tvShopName;
    @ViewInject(R.id.tv_refund_type)
    private TextView tvRefundType;
    @ViewInject(R.id.tv_refund_money)
    private TextView tvRefundMoney;
    @ViewInject(R.id.tv_refund_reason)
    private TextView tvRefundReason;
    @ViewInject(R.id.tv_refund_desc)
    private TextView tvRefundDesc;
    @ViewInject(R.id.tv_refund_num)
    private TextView tvRefundNum;
    @ViewInject(R.id.tv_apply_time)
    private TextView tvApplyTime;
    @ViewInject(R.id.rcv_pic)
    private RecyclerView recyclerPic;//显示图片

    private RefundSuccessPicAdapter picAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadTitle(R.string.title_refund);

        View view = View.inflate(this, R.layout.activity_return_goods_refund_success, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        if (getIntent().hasExtra("ID")) {
            String refundId = getIntent().getStringExtra("ID");//请求参数
            sendRequestToGetRefundDetail(refundId);
        } else {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
        }

        initView();
    }

    private void initView() {
        BaseFunc.setFont(tvIconSuccess);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerPic.setLayoutManager(layoutManager);

        picAdapter = new RefundSuccessPicAdapter(this);
        recyclerPic.setAdapter(picAdapter);

    }

    @OnClick({R.id.tv_check_refund})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_refund:
                BaseFunc.gotoActivity(mContext, BalanceActivity.class, null);
                break;
            default:
                break;
        }
    }

    private void sendRequestToGetRefundDetail(String refundId) {
        WSReturnGoodsGetRefundDtl getRefundDtlHandler = new WSReturnGoodsGetRefundDtl();
        getRefundDtlHandler.setWSReturnGoodsInitCallBack(new WSReturnGoodsGetRefundDtl.WSReturnGoodsRefundDtlCallBack() {
            @Override
            public void onWSReturnGoodsRefundDtlSuccess(WSReturnGoodsGetRefundDtl data) {
                if (data != null) initDataWithWS(data);
            }

            @Override
            public void onWSReturnGoodsRefundDtlError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }
        });
        getRefundDtlHandler.getRefundDetail(member, refundId);
    }

    private void initDataWithWS(final WSReturnGoodsGetRefundDtl data) {

        scvTop.scrollTo(0, 0);

        tvMoney.setText(data.refund_amount + getString(R.string.money_unit));
        tvTime.setText(FHLib.getTimeStrByTimestamp(data.admin_time));//退款成功的时间
        tvShopName.setText(data.store_name);
        tvRefundType.setText((data.refund_type == 1) ? getString(R.string.title_refund) : getString(R.string.return_goods_and_refund));
        tvRefundMoney.setText(data.refund_amount);
        tvRefundReason.setText(data.reason_info);
        tvRefundDesc.setText(data.buyer_message);
        tvRefundNum.setText(data.refund_sn);
        tvApplyTime.setText(FHLib.getTimeStrByTimestamp(data.add_time));//申请退款的时间

        /**
         * 最后显示图片
         */
        if (data.pic_info_format != null && data.pic_info_format.size() > 0) {
            picAdapter.setList(data.pic_info_format);
            picAdapter.notifyDataSetChanged();
            picAdapter.setOnPicItemClickListener(new RefundSuccessPicAdapter.LookPicCallBackListener() {
                @Override
                public void onPicView(String picUrl) {
                    FileUtils.BrowserOpenL(mContext, data.pic_info_format, picUrl);
                }
            });
        }
    }

}
