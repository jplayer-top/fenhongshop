package com.fanglin.fenhong.microbuyer.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.WSReturnGoodGetState;
import com.fanglin.fhlib.other.FHLib;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 退款申请失败 Activity
 * Created by lizhixin on 2015/11/11.
 * 卖家不同意,退货单流程完结
 */
public class ReturnGoodsApplyFailActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.tv_icon_error)
    private TextView tvIconError;
    @ViewInject(R.id.tv_apply_fail_desc1)
    private TextView tvDesc;
    @ViewInject(R.id.ll_bottom)
    private LinearLayout llBottom;//底部的重新申请与申请客服介入按钮

    private String refundId;
    private String orderId;
    private String recId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.activity_return_goods_apply_fail, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        try {
            refundId = getIntent().getStringExtra("ID");
            orderId = getIntent().getStringExtra("ORDER");
            recId = getIntent().getStringExtra("REC");
        } catch (Exception e) {
            refundId = "-1";
            orderId = "-1";
            recId = null;
        }

        if (TextUtils.isEmpty(recId) || TextUtils.equals(recId, "0")) {
            recId = null;
            setHeadTitle(R.string.title_refund);
        } else {
            setHeadTitle(R.string.title_return_goods_1);
        }

        sendRequestToGetState();

        BaseFunc.setFont(tvIconError);
    }

    private void sendRequestToGetState() {
        WSReturnGoodGetState getStateHandler = new WSReturnGoodGetState();
        getStateHandler.setWSReturnGoodsGetStateCallBack(new WSReturnGoodGetState.WSReturnGoodsGetStateCallBack() {
            @Override
            public void onWSReturnGoodsGetStateSuccess(WSReturnGoodGetState data) {
                initDataWithWS(data.seller_message, data.seller_time, data.is_finish);
            }

            @Override
            public void onWSReturnGoodsGetStateError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }
        });
        getStateHandler.getRefundState(member, refundId, 2, orderId);//statue_id: 2 卖家不同意,退货单流程完结
    }

    private void initDataWithWS(String msg, long time, int is_finish) {
        Spanned html;

        if (is_finish == 1) {
            /**
             * 说明退款单流程完结，隐藏重新申请按钮，并显示拒绝时间
             */
            html = Html.fromHtml(String.format(getString(R.string.tips_of_refund_apply_failure_with_time), msg, FHLib.getTimeStrByTimestamp(time)));
        } else {

            llBottom.setVisibility(View.VISIBLE);
            html = Html.fromHtml(String.format(getString(R.string.tips_of_refund_apply_failure), msg));
        }
        tvDesc.setText(html);
    }

    @OnClick({R.id.tv_modify_refund_apply, R.id.tv_apply_service})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_modify_refund_apply:
                //携带必须的参数去申请售后页面
                Intent intent = new Intent(mContext, ReturnGoodsActivity.class);
                intent.putExtra("REC", recId);
                intent.putExtra("ORDER", orderId);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_apply_service:
                BaseFunc.startQQChat(mContext, BaseVar.DEFQQKF);
                break;
            default:
                break;
        }
    }

}
