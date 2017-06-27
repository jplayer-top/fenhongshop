package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.ExpressEntity;
import com.fanglin.fenhong.microbuyer.base.model.WSGetExpressList;
import com.fanglin.fenhong.microbuyer.base.model.WSReturnGoodsGetRefundAddr;
import com.fanglin.fenhong.microbuyer.base.model.WSReturnGoodsRefundShip;
import com.fanglin.fenhong.microbuyer.microshop.LayoutSelectaBank;
import com.fanglin.fenhong.microbuyer.microshop.LayoutSelectaBank.onBankSelected;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.ArrayList;

/**
 * 卖家同意申请，等待买家发货 Activity
 * Created by lizhixin on 2015/11/11.
 */
public class ReturnGoodsApplySuccessActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.tv_icon_success)
    private TextView tvIconSuccess;
    @ViewInject(R.id.tv_ship_name)
    private TextView tvShipName;//图片
    @ViewInject(R.id.tv_ship_telephone)
    private TextView tvShipTel;//图片
    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.tv_phone)
    private TextView tvPhone;
    @ViewInject(R.id.tv_addr)
    private TextView tvAddr;
    @ViewInject(R.id.rl_logistics_select)
    private RelativeLayout rlLogisticsSelect;
    @ViewInject(R.id.tv_apply_service)
    private TextView tvConfirm;//确认发货按钮
    @ViewInject(R.id.tv_look_logistic)
    private TextView tvLookLogistic;//查看物流按钮

    @ViewInject(R.id.tv_logistics_company)
    private TextView tvLogisticsCompany;//物流公司
    @ViewInject(R.id.et_logistics_num)
    private EditText etLogisticsNum;

    private String expressCode;//物流公司编号，用去请求快递100
    private String refundId;//请求参数
    private ArrayList<ExpressEntity> list;//物流公司实体类
    private FHApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadTitle(R.string.title_return_goods_1);

        View view = View.inflate(this, R.layout.activity_return_goods_apply_success, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        if (getIntent().hasExtra("ID")) {
            refundId = getIntent().getStringExtra("ID");
        } else {
            refundId = "-1";
        }

        sendRequestToGetRefundAddr();//初始化地址信息

        initView();
    }

    private void initView() {
        BaseFunc.setFont(tvIconSuccess);
        BaseFunc.setFont(tvShipName);
        BaseFunc.setFont(tvShipTel);

        /**
         * 物流公司信息 先从缓存中去取，没取到的话再去请求webservice
         */
        app = ((FHApp) getApplication());
        try {
            //初始化物流信息
            list = (ArrayList<ExpressEntity>) app.fhdb.findAll(ExpressEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (list == null || list.size() == 0) {
            sendRequestToGetExpress();//请求物流信息
        }
    }

    @OnClick({R.id.tv_apply_service, R.id.rl_logistics_select, R.id.tv_look_logistic})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply_service:

                if (etLogisticsNum.isEnabled()) {
                    /**
                     * 如果物流信息两栏是可编辑状态，表明是允许提交发货
                     */
                    if (BaseFunc.getInputMethodManager(mContext).isActive(etLogisticsNum)) {
                        // 关闭软键盘
                        BaseFunc.hideSoftInput(mContext, etLogisticsNum);
                    }
                    if (checkOverParams()) {
                        // 确认发货
                        sendRequestToSubmitShip();
                    }
                } else {
                    /**
                     * 否则表示之前提交过一次，物流信息不可编辑，此时要
                     * 1.把物流信息两栏置为可编辑状态
                     * 2.提交按钮的文字变为确定
                     */
                    rlLogisticsSelect.setEnabled(true);
                    tvLogisticsCompany.setTextColor(getResources().getColor(R.color.color_33));
                    etLogisticsNum.setEnabled(true);
                    etLogisticsNum.setTextColor(getResources().getColor(R.color.color_33));
                    tvConfirm.setText(getString(R.string.confirm));

                }

                break;
            case R.id.rl_logistics_select:
                if (list == null || list.size() == 0) {
                    BaseFunc.showMsg(mContext, getString(R.string.no_data));
                } else {
                    showDialog();
                }
                break;
            case R.id.tv_look_logistic:
                Bundle bundle = new Bundle();
                bundle.putString("e_code", expressCode);
                bundle.putString("e_name", tvLogisticsCompany.getText().toString().trim());
                bundle.putString("shipping_code", etLogisticsNum.getText().toString().trim());

                BaseFunc.gotoActivityBundle(mContext, Express100Activity.class, bundle);
                break;
            default:
                break;
        }
    }

    /**
     * 请求退款退货地址
     */
    private void sendRequestToGetRefundAddr() {
        WSReturnGoodsGetRefundAddr getRefundAddrHandler = new WSReturnGoodsGetRefundAddr();
        getRefundAddrHandler.setWSReturnGoodsRefundAddrCallBack(new WSReturnGoodsGetRefundAddr.WSReturnGoodsRefundAddrCallBack() {
            @Override
            public void onWSReturnGoodsRefundAddrSuccess(WSReturnGoodsGetRefundAddr data) {
                if (data != null) {
                    initAddressWithWS(data);
                }
            }

            @Override
            public void onWSReturnGoodsRefundAddrError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }
        });
        getRefundAddrHandler.getRefundAddress(member, refundId);
    }

    private void initAddressWithWS(WSReturnGoodsGetRefundAddr data) {
        tvName.setText(data.consignee);
        tvPhone.setText(data.tel);
        tvAddr.setText(data.address);
        /**
         * 根据是否有物流信息返回来判断 物流选项、按钮的显示与隐藏
         */
        if (!TextUtils.isEmpty(data.express_code)) {
            tvLookLogistic.setVisibility(View.VISIBLE);
            //填充物流信息
            tvLogisticsCompany.setTag(data.express_id);
            tvLogisticsCompany.setText(data.express_name);
            etLogisticsNum.setText(data.express_no);
            expressCode = data.express_code;

            /**
             * 同时要修改
             * 1.确定按钮的提示文字
             * 2.选择物流和填写物流单号两个框变为不可编辑状态
             */
            rlLogisticsSelect.setEnabled(false);
            tvLogisticsCompany.setTextColor(getResources().getColor(R.color.color_99));
            etLogisticsNum.setEnabled(false);
            etLogisticsNum.setTextColor(getResources().getColor(R.color.color_99));
            tvConfirm.setText(getString(R.string.modify_logistics));
        }
    }

    /**
     * 提交退货发货
     */
    private void sendRequestToSubmitShip() {
        WSReturnGoodsRefundShip submitShipHandler = new WSReturnGoodsRefundShip();
        submitShipHandler.setWSReturnGoodsShipCallBack(new WSReturnGoodsRefundShip.WSReturnGoodsShipCallBack() {
            @Override
            public void onWSReturnGoodsShipSuccess(String data) {
                BaseFunc.showMsg(mContext, getString(R.string.op_success));
                finish();
            }

            @Override
            public void onWSReturnGoodsShipError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }
        });
        submitShipHandler.submitShip(member, refundId, tvLogisticsCompany.getTag().toString(), etLogisticsNum.getText().toString().trim());// 快递单号
    }

    /**
     * 请求物流公司信息
     */
    private void sendRequestToGetExpress() {
        WSGetExpressList getExpressListHandler = new WSGetExpressList();
        getExpressListHandler.setWSExpressCallBack(new WSGetExpressList.WSExpressCallBack() {
            @Override
            public void onWSExpressSuccess(String data) {
                /**
                 * 处理物流公司信息
                 */
                list = new Gson().fromJson(data, new TypeToken<ArrayList<ExpressEntity>>() {
                }.getType());
                try {
                    app.fhdb.saveAll(list);//存入数据库
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWSExpressError(String errcode) {

            }
        });
        getExpressListHandler.getExpressList();
    }

    /**
     * 选择物流公司
     */
    private void showDialog() {
        ArrayList<String> expresses = new ArrayList<>();
        for (ExpressEntity express : list) {
            expresses.add(express.getExpress_name());
        }

        LayoutSelectaBank selectExpressDialog = new LayoutSelectaBank(mContext, expresses.toArray(new String[]{}));
        selectExpressDialog.setTv_title(getResources().getString(R.string.select_express));

        selectExpressDialog.setCallBack(new onBankSelected() {
            @Override
            public void onSelected(String bankname, int index) {
                tvLogisticsCompany.setText(bankname);
                tvLogisticsCompany.setTag(list.get(index).getExpress_id());
                expressCode = list.get(index).getExpress_code();
            }
        });
        selectExpressDialog.show();
    }

    /**
     * 检查参数是否合法
     */
    private boolean checkOverParams() {
        //物流单号应是9-15位数字
        String logisticNum = etLogisticsNum.getText().toString().trim();
        if (TextUtils.isEmpty(logisticNum)) {
            BaseFunc.showMsg(mContext, getString(R.string.return_goods_logistics));
            return false;
        } else if (logisticNum.length() < 9 || logisticNum.length() > 30) {
            BaseFunc.showMsg(mContext, getString(R.string.return_goods_logistics_num));
            return false;
        }

        if (TextUtils.isEmpty(tvLogisticsCompany.getText().toString().trim())) {
            BaseFunc.showMsg(mContext, getString(R.string.logistics_select_hint));
            return false;
        }
        return true;
    }

}
