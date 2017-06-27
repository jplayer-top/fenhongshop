package com.fanglin.fenhong.microbuyer.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.LastRefund;
import com.fanglin.fenhong.microbuyer.base.model.RefundReason;
import com.fanglin.fenhong.microbuyer.base.model.WSReturnGoods;
import com.fanglin.fenhong.microbuyer.base.model.WSReturnGoodsAll;
import com.fanglin.fenhong.microbuyer.base.model.WSReturnGoodsInit;
import com.fanglin.fenhong.microbuyer.common.adapter.ReturnGoodsPicAdapter;
import com.fanglin.fenhong.microbuyer.microshop.LayoutSelectaBank;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.spotsdialog.SpotsDialog;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

/**
 * 提交退货/退款申请 Activity
 * Created by lizhixin on 2015/11/10.
 */
public class ReturnGoodsActivity extends BaseFragmentActivityUI {

    public ArrayList<RefundReason> reasonsAll = new ArrayList<>();
    @ViewInject(R.id.tv_type_refund)
    private TextView tvRefund;
    @ViewInject(R.id.tv_type_return_goods)
    private TextView tvReturnGoods;
    @ViewInject(R.id.ll_type_return_goods)
    private RelativeLayout llReturnGoods;
    @ViewInject(R.id.tv_icon_refund)
    private TextView tvIconRefund;
    @ViewInject(R.id.tv_return_goods_money_most)
    private TextView tvMoneyMost;//最多退款金额
    @ViewInject(R.id.tv_return_goods_num_most)
    private TextView tvNumMost;//最多退货数量
    @ViewInject(R.id.tv_icon_return_goods)
    private TextView tvIconReturnGoods;
    @ViewInject(R.id.tv_return_reason)
    private TextView tvReason;
    @ViewInject(R.id.et_return_money)
    private EditText etMoney;
    @ViewInject(R.id.et_return_num)
    private EditText etNum;
    @ViewInject(R.id.ll_return_goods_num)
    private LinearLayout llGoodsNum;//退货数量两行，当仅退款时要隐藏
    @ViewInject(R.id.et_return_desc)
    private EditText etDesc;
    @ViewInject(R.id.rcv_pic_return_goods)
    private RecyclerView recyclerView;
    private ReturnGoodsPicAdapter adapter;
    private ArrayList<String> refundReasons = new ArrayList<>();
    private double goodsPrice;//webservice返回的最大退款金额
    private int goodsNum;//webservice返回的最大退货数量
    private int refundType;// 申请类型: 1为退款  2为退货

    private String orderId;
    private String rec_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeadTitle(R.string.title_return_goods);

        View view = View.inflate(this, R.layout.activity_return_goods, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        try {
            orderId = getIntent().getStringExtra("ORDER");
            rec_Id = getIntent().getStringExtra("REC");
        } catch (Exception e) {
            orderId = "-1";
            rec_Id = null;
        }

        initView();
        sendRequestToInitData();
    }

    private void initView() {
        tvIconRefund.setTypeface(iconfont);
        tvIconReturnGoods.setTypeface(iconfont);
        String moneyMost = getString(R.string.return_goods_money_most) + String.valueOf(goodsPrice);
        tvMoneyMost.setText(moneyMost);
        tvNumMost.setText(String.format(getString(R.string.return_goods_num_most), goodsNum));//最多退货数量

        if (TextUtils.isEmpty(rec_Id)) {

            /**
             * 如果rec_Id为空，表示我要退款
             * 此时的订单全部退款，不分单个商品
             */
            llGoodsNum.setVisibility(View.GONE);//隐藏退货数量栏
            llReturnGoods.setVisibility(View.GONE);//隐藏我要退货栏

            refundType = 1;
            changeRefundType(refundType);
        } else {
            /**
             * 单商品退货
             */
            llGoodsNum.setVisibility(View.VISIBLE);//显示退货数量栏
            llReturnGoods.setVisibility(View.VISIBLE);//显示我要退货栏
            refundType = 2;
            changeRefundType(refundType);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> list_pic = new ArrayList<>();//上传图片的集合，adapter的参数
        list_pic.add("ADD");//默认的添加按钮
        adapter = new ReturnGoodsPicAdapter(this, list_pic);
        adapter.setOnPictureItemClickListener(new ReturnGoodsPicAdapter.AddPicCallBackListener() {
            @Override
            public void onAddDefault() {
                if (adapter.getItemCount() < 4) {
                    //上传图片
                    BaseFunc.selectPictures(ReturnGoodsActivity.this, 0x001, true, 800, 800, 1, 1);
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.tips_max_pic_3));
                }
            }

            @Override
            public void onDelItem(int index) {
                adapter.list.remove(index);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPicView(String picUrl) {
                if (adapter.getItemCount() > 1)
                    FileUtils.BrowserOpenL(mContext, adapter.list.subList(0, adapter.getItemCount() - 1), picUrl);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    @OnClick({R.id.btn_subimt, R.id.ll_type_return_goods, R.id.ll_type_refund, R.id.rl_return_reason})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_subimt:
                /**
                 * 提交申请有两种情况，需要分别处理：
                 * 1。 提交单商品退货
                 * 2. 提交全部商品退货
                 */
                if (!TextUtils.isEmpty(rec_Id)) {
                    //单商品商品退货 也要区分两种情况， 退款0 与 退货1
                    int returnType = llGoodsNum.getVisibility() == View.VISIBLE ? 1 : 0;
                    if (checkParamsOfReturnGoodsSingle(returnType)) {
                        sendRequestToSubmit();
                    }
                } else {
                    //退款，即全部商品退货
                    if (checkParamsOfRefund()) {
                        sendRequestToSubmitAll();
                    }
                }
                break;
            case R.id.ll_type_return_goods://我要退货
                refundType = 2;
                changeRefundType(refundType);
                llGoodsNum.setVisibility(View.VISIBLE);//切换为退货选择时需要退货数量
                break;
            case R.id.ll_type_refund://我要退款
                refundType = 1;
                changeRefundType(refundType);
                llGoodsNum.setVisibility(View.GONE);//切换为退款选择时不需要退货数量
                break;
            case R.id.rl_return_reason:
                if (refundReasons.size() > 0) {
                    showDialog();
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.no_data));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化页面数据请求
     */
    private void sendRequestToInitData() {

        WSReturnGoodsInit initDataHandler = new WSReturnGoodsInit();
        initDataHandler.setWSReturnGoodsInitCallBack(new WSReturnGoodsInit.WSReturnGoodsInitCallBack() {
            @Override
            public void onWSReturnGoodsInitError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }

            @Override
            public void onWSReturnGoodsInitSuccess(WSReturnGoodsInit data) {
                if (data != null) initDataWithWS(data);
            }
        });
        //订单ID   订单商品表编号（可选，传0或不传返回订单所有，默认0）
        initDataHandler.getInitData(member, orderId, rec_Id);
    }

    /**
     * 提交退款申请请求
     * !!! 单商品 !!!
     */
    private void sendRequestToSubmit() {

        WSReturnGoods returnGoodsHandler = new WSReturnGoods();
        returnGoodsHandler.setWSReturnGoodsModelCallBack(new WSReturnGoods.WSReturnGoodsModelCallBack() {
            @Override
            public void onWSReturnGoodsError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }

            @Override
            public void onWSReturnGoodsSuccess(String refundId) {
                /**
                 * 提交退货申请成功后，携带参数跳转至成功页面，并关闭本页面
                 * refundId orderId rec_Id  fixed By Plucky
                 */
                BaseFunc.showMsg(mContext, getString(R.string.submit_success));
                Intent intent = new Intent(mContext, ReturnGoodsSubSuccessActivity.class);
                intent.putExtra("ID", refundId);
                intent.putExtra("TYPE", refundType);

                mContext.startActivity(intent);
                finish();
            }
        });
        returnGoodsHandler.submit(member, refundType, orderId, rec_Id, etMoney.getText().toString().trim(), etNum.getText().toString().trim(), tvReason.getTag().toString(), etDesc.getText().toString().trim(), adapter.list.subList(0, adapter.list.size() - 1).toString().replace("[", "").replace("]", "").replace(" ", ""));
    }

    /**
     * 提交退款申请请求
     * !!! 全部商品 !!!
     */
    private void sendRequestToSubmitAll() {

        WSReturnGoodsAll returnGoodsAllHandler = new WSReturnGoodsAll();
        returnGoodsAllHandler.setWSReturnGoodsAllCallBack(new WSReturnGoodsAll.WSReturnGoodsAllModelCallBack() {
            @Override
            public void onWSReturnGoodsAllSuccess(String refundId) {
                /**
                 * 提交退货申请成功后，携带参数跳转至成功页面，并关闭本页面
                 * refundId orderId rec_Id  fixed By Plucky
                 */
                BaseFunc.showMsg(mContext, getString(R.string.submit_success));
                Intent intent = new Intent(mContext, ReturnGoodsSubSuccessActivity.class);
                intent.putExtra("ID", refundId);
                intent.putExtra("TYPE", refundType);
                mContext.startActivity(intent);
                finish();
            }

            @Override
            public void onWSReturnGoodsAllError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.op_error));
            }
        });
        returnGoodsAllHandler.submit(member, orderId, etDesc.getText().toString(), adapter.list.subList(0, adapter.list.size() - 1).toString().replace("[", "").replace("]", ""), rec_Id);
    }

    /**
     * 初始化请求返回数据后 填充页面
     */
    private void initDataWithWS(WSReturnGoodsInit data) {

        reasonsAll = data.reason;//记录下来
        goodsPrice = data.goods_pay_price;
        goodsNum = data.goods_num;

        for (RefundReason refundReason : reasonsAll) {
            refundReasons.add(refundReason.getReason_info());
        }

        //最多退款金额
        String money_hint = getString(R.string.return_goods_money_most) + String.valueOf(goodsPrice);
        if (!TextUtils.isEmpty(data.coupon_deduct) && !TextUtils.equals("0.00", data.coupon_deduct)) {
            money_hint += String.format(getString(R.string.return_goods_coupon_deduct), data.coupon_deduct);
        }
        tvMoneyMost.setText(money_hint);

        //最多退货数量
        tvNumMost.setText(String.format(getString(R.string.return_goods_num_most), goodsNum));

        if (TextUtils.isEmpty(rec_Id)) {
            /**
             * 如果是全部退款
             * 退款金额直接写webservice返回的最多退款金额,同时不允许再次修改
             */
            etMoney.setText(String.valueOf(goodsPrice));
            etMoney.setEnabled(false);
            etMoney.setTextColor(getResources().getColor(R.color.color_99));
        }

        if (data.last_refund != null) {

            /**
             * 如果有上次的记录，则重现上次提交的申请记录
             */
            reappearLastRefund(data.last_refund);
        }
    }

    /**
     * 重现上次提交的申请记录
     */
    private void reappearLastRefund(LastRefund last_refund) {
        //退款类型
        if (TextUtils.equals(last_refund.getRefund_type(), "1")) {
            //退款
            refundType = 1;
            changeRefundType(refundType);
        } else {
            //退货
            refundType = 2;
            changeRefundType(refundType);
        }
        //退款原因
        tvReason.setText(last_refund.getReason_info());
        tvReason.setTag(last_refund.getReason_id());

        //退款金额
        if (Double.valueOf(last_refund.getRefund_amount()) > goodsPrice) {
            //如果上次填写的退款金额大于最大允许金额，则显示空值
            etMoney.setText("");
        } else {
            etMoney.setText(last_refund.getRefund_amount());
        }
        //退货数量
        etNum.setText(last_refund.getGoods_num());
        //退款说明
        etDesc.setText(last_refund.getBuyer_message());
        /**
         * 图片
         */
        if (last_refund.getPic_info_format() != null && last_refund.getPic_info_format().size() > 0) {
            adapter.list.addAll(0, last_refund.getPic_info_format());
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据选项切换 我要退款 / 我要退货
     */
    private void changeRefundType(int refundType) {
        if (refundType == 1) {//退款
            tvRefund.setSelected(true);
            tvReturnGoods.setSelected(false);
            tvIconRefund.setVisibility(View.VISIBLE);
            tvIconReturnGoods.setVisibility(View.GONE);

        } else { //退货
            tvRefund.setSelected(false);
            tvReturnGoods.setSelected(true);
            tvIconRefund.setVisibility(View.GONE);
            tvIconReturnGoods.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 0x001:
                try {
                    Uri uri = data.getParcelableExtra("VAL");

                    if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                        upload(uri);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void upload(Uri uri) {
        if (member == null) return;
        final SpotsDialog dlg = BaseFunc.getLoadingDlg(this, getString(R.string.picture_uploading));
        new UpyunUploader(member.member_id).setUploadFile(uri.getPath()).setUpYunCallBack(new UpyunUploader.UpYunCallBack() {
            @Override
            public void startLoading() {
                dlg.show();
            }

            @Override
            public void endLoading(boolean isSuccess, String data) {
                dlg.dismiss();
                if (isSuccess) {
                    if (BaseFunc.isValidImgUrl(data)) {

                        adapter.list.add(0, data);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).upload();
    }

    /**
     * 选择退款原因弹框
     */
    private void showDialog() {
        LayoutSelectaBank selectExpressDialog = new LayoutSelectaBank(mContext, refundReasons.toArray(new String[]{}));
        selectExpressDialog.setTv_title(getResources().getString(R.string.select_refund_reason));

        selectExpressDialog.setCallBack(new LayoutSelectaBank.onBankSelected() {
            @Override
            public void onSelected(String reason, int index) {
                tvReason.setText(reason);
                tvReason.setTag(reasonsAll.get(index).getReason_id());
            }
        });
        selectExpressDialog.show();
    }

    /**
     * 提交 单品退货 申请之前检查参数是否合法
     *
     * @param returnType 退款0 与 退货1 仅在本方法中有效
     */
    private boolean checkParamsOfReturnGoodsSingle(int returnType) {
        if (TextUtils.isEmpty(tvReason.getText().toString().trim())) {
            BaseFunc.showMsg(mContext, getString(R.string.return_goods_reason_select_));
            return false;
        }
        if (TextUtils.isEmpty(etMoney.getText().toString().trim())) {
            BaseFunc.showMsg(mContext, getString(R.string.return_goods_money_hint));
            return false;
        } else {
            double moneyInput = Double.parseDouble(etMoney.getText().toString().trim());
            if (moneyInput > goodsPrice) {
                BaseFunc.showMsg(mContext, getString(R.string.return_goods_money_check_hint) + String.valueOf(goodsPrice));
                return false;
            } else if (moneyInput == 0.0) {
                BaseFunc.showMsg(mContext, getString(R.string.return_goods_money_error_hint));
                return false;
            }
        }
        if (returnType == 1) {
            /**
             * 只有退货时才检查数量是否合法
             */
            if (TextUtils.isEmpty(etNum.getText().toString().trim())) {
                BaseFunc.showMsg(mContext, getString(R.string.return_goods_num_hint));
                return false;
            } else {
                int numInput = Integer.parseInt(etNum.getText().toString().trim());
                if (numInput > goodsNum) {
                    BaseFunc.showMsg(mContext, getString(R.string.return_goods_num_check_hint) + String.valueOf(goodsNum));
                    return false;
                } else if (numInput < 1) {
                    BaseFunc.showMsg(mContext, getString(R.string.return_goods_num_check_0));
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 提交 全部退款 申请之前检查参数是否合法
     */
    private boolean checkParamsOfRefund() {
        if (TextUtils.isEmpty(tvReason.getText().toString().trim())) {
            BaseFunc.showMsg(mContext, getString(R.string.return_goods_reason_select_));
            return false;
        }
        if (TextUtils.isEmpty(etMoney.getText().toString().trim())) {
            BaseFunc.showMsg(mContext, getString(R.string.return_goods_money_hint));
            return false;
        } else {
            double moneyInput = Double.parseDouble(etMoney.getText().toString().trim());
            if (moneyInput > goodsPrice) {
                BaseFunc.showMsg(mContext, getString(R.string.return_goods_money_check_hint) + String.valueOf(goodsPrice));
                return false;
            } else if (moneyInput == 0.0) {
                BaseFunc.showMsg(mContext, getString(R.string.return_goods_money_error_hint));
                return false;
            }
        }
        return true;
    }

    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("list_pic", adapter.getArrayList());
        super.onSaveInstanceState(outState);
    }

    /**
     * Activity被系统杀死后再重建时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        adapter.list = savedInstanceState.getStringArrayList("list_pic");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FHLog.d("lizx->", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FHLog.d("lizx->", "onDestroy");
    }

}
