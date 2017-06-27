package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.WSReturnGoodGetState;
import com.fanglin.fhlib.other.CountDown;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 退款申请提交成功 倒计时页面 Activity
 */
public class ReturnGoodsSubSuccessActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.tv_icon_success)
    private TextView tvIconSuccess;
    @ViewInject(R.id.tv_submit_shop_name)
    private TextView tvShopName;
    @ViewInject(R.id.tv_submit_QQ)
    private TextView tvQQ;
    @ViewInject(R.id.tv_submit_telephone)
    private TextView tvTelephone;
    @ViewInject(R.id.tv_submit_desc1)
    private TextView tvDesc;

    private String refundId;
    private int refundType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.activity_return_goods_sub_success, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        try {
            refundId = getIntent().getStringExtra("ID");
            refundType = getIntent().getIntExtra("TYPE", 1);
        } catch (Exception e) {
            refundId = "-1";
            refundType = 1;
        }

        if (refundType == 1) {
            setHeadTitle(R.string.title_refund);
        } else {
            setHeadTitle(R.string.title_return_goods_1);
        }

        sendRequestToInitData();

        BaseFunc.setFont(tvIconSuccess);
    }

    @OnClick({R.id.tv_go_back, R.id.tv_revocation})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go_back:
                onBackPressed();
                break;
            case R.id.tv_revocation:
                BaseFunc.showMsg(mContext, "撤销申请");
                break;
            default:
                break;
        }
    }

    /**
     * 发送请求初始化数据
     */
    private void sendRequestToInitData() {
        WSReturnGoodGetState getStateHandler = new WSReturnGoodGetState();
        getStateHandler.setWSReturnGoodsGetStateCallBack(new WSReturnGoodGetState.WSReturnGoodsGetStateCallBack() {
            @Override
            public void onWSReturnGoodsGetStateSuccess(WSReturnGoodGetState data) {
                if (data != null) initDataWithWS(data);
            }

            @Override
            public void onWSReturnGoodsGetStateError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }
        });
        //                      进度状态id : 1 - 退款提交成功       orderId不需要，传null
        getStateHandler.getRefundState(member, refundId, 1, null);
    }

    /**
     * 根据webservice返回值初始化数据
     */
    private void initDataWithWS(final WSReturnGoodGetState data) {

        final StringBuilder str = new StringBuilder();
        str.append(getResources().getString(R.string.refund_sub_success_desc1));
        str.append("<font color='red'>%s</font>");
        if (refundType == 1) {
            str.append(getResources().getString(R.string.refund_sub_success_desc5));
        } else {
            str.append(getResources().getString(R.string.refund_sub_success_desc2));
        }

        tvDesc.setText(Html.fromHtml(String.format(str.toString(), "0")));
        tvShopName.setText(data.store_name);
        if (!TextUtils.isEmpty(data.store_qq)) {
            /**
             * 打开指定qq
             */
            if (TextUtils.isDigitsOnly(data.store_qq)) {
                tvQQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.startQQChat(mContext, data.store_qq);
                    }
                });
            }
            tvQQ.setText(data.store_qq);
        } else {
            tvQQ.setText(getText(R.string.return_goods_no_qq));
        }

        if (!TextUtils.isEmpty(data.store_phone)) {
            /**
             * 拨打指定电话
             */
            if (TextUtils.isDigitsOnly(data.store_phone)) {
                tvTelephone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.Call(mContext, data.store_phone);
                    }
                });
            }
            tvTelephone.setText(data.store_phone);
        } else {
            tvTelephone.setText("暂未维护电话");
        }

        if (data.countdown > 0) {
            //计时器
            new CountDown(data.countdown).start(new CountDown.CountDownListener() {
                @Override
                public void onChange(long atime) {

                    tvDesc.setText(Html.fromHtml(String.format(str.toString(), BaseFunc.getCNTimeByTimeStamp(atime))));
                }

                @Override
                public void onStop() {
                    if (refundType == 1) {
                        tvDesc.setText(getString(R.string.refund_sub_success_desc4));
                    } else {
                        tvDesc.setText(getString(R.string.refund_sub_success_desc6));
                    }
                }
            });

        } else {
            if (refundType == 1) {
                tvDesc.setText(getString(R.string.refund_sub_success_desc4));
            } else {
                tvDesc.setText(getString(R.string.refund_sub_success_desc6));
            }
        }


    }

}
