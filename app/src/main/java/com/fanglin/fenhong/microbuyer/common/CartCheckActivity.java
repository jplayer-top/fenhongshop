package com.fanglin.fenhong.microbuyer.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.Address;
import com.fanglin.fenhong.microbuyer.base.model.BaseGoods;
import com.fanglin.fenhong.microbuyer.base.model.Bonus;
import com.fanglin.fenhong.microbuyer.base.model.CartCheck;
import com.fanglin.fenhong.microbuyer.base.model.CartCheckData;
import com.fanglin.fenhong.microbuyer.base.model.OrderGen;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.buyer.AddressActivity;
import com.fanglin.fenhong.microbuyer.buyer.EditAddressActivity;
import com.fanglin.fenhong.microbuyer.buyer.LayoutGrpBuyAddrEdit;
import com.fanglin.fenhong.microbuyer.common.adapter.CartCheckAdapter;
import com.fanglin.fenhong.microbuyer.microshop.InputPaypwdActivity;
import com.fanglin.fenhong.microbuyer.microshop.SelectBonusActivity;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.google.gson.Gson;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/23.
 */
public class CartCheckActivity extends BaseFragmentActivityUI implements View.OnClickListener, CartCheckAdapter.AgreementCheckboxCallBack, LayoutNotSupportGoods.NotSupportGoodsListener {


    private static final int REQCODE = 123;//地址请求码
    private static final int REQPAYCODE = 124;//订单支付请求吗
    private static final int REQBONUS = 125;
    private static final int REQPWD = 126;
    @ViewInject(R.id.rcv)
    RecyclerView rcv;
    @ViewInject(R.id.tv_num)
    TextView tv_num;
    @ViewInject(R.id.tv_money)
    TextView tv_money;
    @ViewInject(R.id.tv_pay)
    TextView tv_pay;
    String cart_info;
    String addressTips;
    String custom_claim;//如果等于一需要验证身份证信息
    int if_cart = 0;
    int gc_area = 0;//0表示国内订单 1 国外
    String vat_hash;//    加密字段1（来自核对购物车信息接口）
    String offpay_hash;//     加密字段2（来自核对购物车信息接口）
    String offpay_hash_batch;//     加密字段3（来自核对购物车信息接口）
    String pay_pwd = null;//支付密码
    String coupon_id = null;//购物券编码

    /**
     * 选择地址相关的控件
     */
    LinearLayout LAddr, LAdd;
    TextView tv_addr, tv_phone, tv_name, tv_add;
    Address address;
    RecyclerViewHeader header;
    boolean isActive = true;
    private String resource_tags;//统计用 --lizhixin
    private CartCheckAdapter adapter;

    LayoutNotSupportGoods layoutNotSupportGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = View.inflate(mContext, R.layout.activity_cartcheck, null);
        LHold.addView(v);
        ViewUtils.inject(this, v);

        cart_info = getIntent().getStringExtra("goodsId_num");
        gc_area = getIntent().getIntExtra("goods_source", 0);
        if_cart = getIntent().getIntExtra("if_cart", 0);
        resource_tags = getIntent().getStringExtra("resource_tags");

        initView();
    }

    private void initView() {
        setHeadTitle(R.string.order_confirm);

        tv_num.setText("0");
        rcv.setLayoutManager(new LinearLayoutManager(mContext));
        header = RecyclerViewHeader.fromXml(mContext, R.layout.layout_addr);
        header.attachTo(rcv);
        LAddr = (LinearLayout) header.findViewById(R.id.LAddr);
        LAdd = (LinearLayout) header.findViewById(R.id.LAdd);
        tv_addr = (TextView) header.findViewById(R.id.tv_addr);
        tv_phone = (TextView) header.findViewById(R.id.tv_phone);
        tv_name = (TextView) header.findViewById(R.id.tv_name);
        tv_add = (TextView) header.findViewById(R.id.tv_add);
        TextView tv_addr_icon = (TextView) header.findViewById(R.id.tv_addr_icon);
        TextView tv_certname_icon = (TextView) header.findViewById(R.id.tv_certname_icon);
        TextView tv_phone_icon = (TextView) header.findViewById(R.id.tv_phone_icon);

        layoutNotSupportGoods = new LayoutNotSupportGoods(mContext);
        layoutNotSupportGoods.setListener(this);

        BaseFunc.setFont(tv_phone_icon);
        BaseFunc.setFont(tv_addr_icon);
        BaseFunc.setFont(tv_certname_icon);

        LAddr.setOnClickListener(this);
        tv_add.setOnClickListener(this);

        adapter = new CartCheckAdapter(mContext);
        adapter.setCallBack(new CartCheckAdapter.CartCheckCallBack() {
            @Override
            public void onSelectPay() {
                adapter.isAccountSelected = !adapter.isAccountSelected;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSelectBonus(CartCheckData.COUPON_LIST coupon_list) {
                BaseFunc.gotoActivity4Result(CartCheckActivity.this, SelectBonusActivity.class, new Gson().toJson(coupon_list), REQBONUS);
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                String fmt = getString(R.string.count_num);
                DecimalFormat df = new DecimalFormat("¥#0.00");
                double[] pays = adapter.getPayAndNum();
                tv_money.setText(df.format(pays[0]));
                tv_num.setText(String.format(fmt, (int) pays[1]));
            }
        });
        adapter.setAgreementCheckboxCallBack(this);
        rcv.setAdapter(adapter);

        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @OnClick(value = {R.id.tv_pay})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pay:
                doSubmit();
                break;
        }
    }

    private void doSubmit() {
        if (address == null) {
            YoYo.with(Techniques.Shake).duration(700).playOn(header);
            BaseFunc.showMsg(mContext, getString(R.string.hint_area_info));
            return;
        }
        if (TextUtils.equals(custom_claim, "1")) {
            if (TextUtils.isEmpty(address.cert_num) || TextUtils.isEmpty(address.cert_name)) {
                confirmEditAddr(address);
                return;
            }
        }

        //不支持送货提示
        if (!TextUtils.isEmpty(addressTips)) {
            notSupportAddressTips(addressTips);
            return;
        }

        if (layoutNotSupportGoods.getGoodsNum() > 0) {
            layoutNotSupportGoods.show();
            return;
        }


        if (cart_info == null) return;
        if (member == null) return;
        int area;//生成订单时需要区分国内和国外购物车
        if (gc_area == 0) {
            area = 0;
        } else {
            area = 1;
        }


        if (adapter.SelectedBonus != null) {
            coupon_id = adapter.SelectedBonus.coupon_id;
        }

        if (adapter.isAccountSelected) {
            //如果是使用余额 则需要进入下一个页面
            BaseFunc.gotoActivity4Result(this, InputPaypwdActivity.class, null, REQPWD);
        } else {
            sendOrderSubmitReq(member.member_id, member.token, address.address_id, if_cart, cart_info, vat_hash, offpay_hash, offpay_hash_batch, area, coupon_id, pay_pwd);
        }
    }

    private void sendOrderSubmitReq(String mid, String token, String address_id, int if_cart, String cart_info, String vat_hash, String offpay_hash, String offpay_hash_batch, int area, String coupon_id, String pay_pwd) {
        final SpotsDialog sad = BaseFunc.getLoadingDlg(this, getString(R.string.order_gening));
        String seckilling_goods = adapter.getSeckillingGoodsIDs();
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {

            @Override
            public void onStart(String data) {
                tv_pay.setEnabled(false);
                if (isActive) {
                    sad.show();
                }

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isActive) {
                    sad.dismiss();
                }
                if (isSuccess) {
                    try {
                        OrderGen orderGen = new Gson().fromJson(data, OrderGen.class);
                        if (orderGen.pay_amount > 0) {
                            /** 需要支付*/
                            PayEntity entity = new PayEntity();
                            entity.pay_sn = orderGen.pay_sn;//交易单编号
                            entity.lastClassName = CartCheckActivity.class.getName();
                            entity.gc_area = gc_area;
                            entity.order_custom = orderGen.order_custom;//海关编码
                            entity.pay_amount = orderGen.pay_amount;//交易金额
                            entity.payment_list = orderGen.payment_list;//支付方式
                            BaseFunc.gotoPayActivity(CartCheckActivity.this, entity, REQPAYCODE);

                            tv_pay.setEnabled(false);
                        } else {
                            double[] pays = adapter.getPayAndNum();
                            /** 通过余额支付成功*/

                            PayEntity entity = new PayEntity();
                            entity.pay_sn = orderGen.pay_sn;
                            entity.gc_area = gc_area;
                            entity.pay_amount = orderGen.pay_amount;
                            entity.youhui = pays[2];
                            BaseFunc.gotoPaySuccessActivity(CartCheckActivity.this, entity, 0);
                            finish();
                        }
                    } catch (Exception e) {
                        tv_pay.setEnabled(true);
                    }
                } else {
                    tv_pay.setEnabled(true);
                }
            }
        }).submit(mid, token, address_id, if_cart, cart_info, vat_hash, offpay_hash, offpay_hash_batch, area, coupon_id, pay_pwd, seckilling_goods, resource_tags);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LAddr:
                BaseFunc.gotoActivity4Result(this, AddressActivity.class, CartCheckActivity.class.getName(), REQCODE);
                break;
            case R.id.tv_add:
                BaseFunc.gotoActivity4Result(this, EditAddressActivity.class, CartCheckActivity.class.getName(), REQCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQCODE:
                if (resultCode == RESULT_OK && data != null) {
                    try {
                        String val = data.getStringExtra("VAL");
                        address = new Gson().fromJson(val, Address.class);
                        tv_addr.setText(address.getAddressDesc());
                        tv_phone.setText(BaseFunc.getMaskMobile(address.mob_phone));
                        tv_name.setText(address.name);
                        LAddr.setVisibility(View.VISIBLE);
                        LAdd.setVisibility(View.GONE);
                    } catch (Exception e) {
                        //
                    }
                }
                break;
            case REQPAYCODE:
                //如果支付成功了--获取取消支付，退出订单确认页
                finish();
                break;
            case REQBONUS:
                try {
                    String val = data.getStringExtra("VAL");
                    adapter.SelectedBonus = new Gson().fromJson(val, Bonus.class);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    //
                }
                break;
            case REQPWD:
                try {
                    String val = data.getStringExtra("VAL");
                    if (!TextUtils.isEmpty(val)) {
                        pay_pwd = val;
                        if (member == null) return;
                        sendOrderSubmitReq(member.member_id, member.token, address.address_id, if_cart, cart_info, vat_hash, offpay_hash, offpay_hash_batch, gc_area == 0 ? 0 : 1, coupon_id, pay_pwd);
                    }
                } catch (Exception e) {
                    //
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCart();
        isActive = true;
    }

    private void checkCart() {
        if (member == null) return;
        if (cart_info == null) return;
        final int area;//核对购物车的时候需要区分国内和国外购物车
        if (gc_area == 0) {
            area = 0;
        } else {
            area = 1;
        }
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
                    List<CartCheck> list;
                    List<BaseGoods> unSupportGoods;
                    CartCheckData checkData = null;
                    try {
                        checkData = new Gson().fromJson(data, CartCheckData.class);
                        list = checkData.store_cart_list;
                        address = checkData.address;
                        vat_hash = checkData.vat_hash;
                        offpay_hash = checkData.offpay_hash;
                        offpay_hash_batch = checkData.offpay_hash_batch;
                        addressTips = checkData.address_tips;
                        custom_claim = checkData.custom_claim;
                        unSupportGoods = checkData.not_deliver_areas_goods_list;
                    } catch (Exception e) {
                        list = null;
                        address = null;
                        addressTips = null;
                        custom_claim = null;
                        unSupportGoods = null;
                    }
                    if (list != null && list.size() > 0) {
                        boolean isChina = (area == 0);
                        adapter.setList(checkData, isChina);
                        rcv.getAdapter().notifyDataSetChanged();
                    }

                    layoutNotSupportGoods.setList(unSupportGoods);
                    if (unSupportGoods != null && unSupportGoods.size() > 0) {
                        layoutNotSupportGoods.show();
                    }

                    if (address != null) {
                        /** 改为不由地址来决定按钮是否可点击*/
                        tv_addr.setText(address.getAddressDesc());
                        tv_phone.setText(BaseFunc.getMaskMobile(address.mob_phone));
                        tv_name.setText(address.name);
                        LAddr.setVisibility(View.VISIBLE);
                        LAdd.setVisibility(View.GONE);
                    } else {
                        LAddr.setVisibility(View.GONE);
                        LAdd.setVisibility(View.VISIBLE);
                    }
                } else {
                    refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        }).check_cart(member.member_id, member.token, cart_info, member.store_id, if_cart, address != null ? address.address_id : null, area);
    }

    private void confirmEditAddr(final Address address) {
        LayoutGrpBuyAddrEdit layoutGrpBuyAddrEdit = new LayoutGrpBuyAddrEdit(mContext);
        layoutGrpBuyAddrEdit.REQCODE = REQCODE;
        layoutGrpBuyAddrEdit.address = address;
        layoutGrpBuyAddrEdit.show();
    }

    /**
     * 不支持配送的提示
     * Added By Plucky
     *
     * @param tips 提示信息
     */
    private void notSupportAddressTips(String tips) {
        FHHintDialog hintDialog = new FHHintDialog(this);
        hintDialog.setTvTitle("提示");
        hintDialog.setTvContent(tips);
        hintDialog.setTvLeft("更改收货地址");
        hintDialog.setTvRight("返回");
        hintDialog.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
                BaseFunc.gotoActivity4Result(CartCheckActivity.this, AddressActivity.class, CartCheckActivity.class.getName(), REQCODE);
            }

            @Override
            public void onRightClick() {

            }
        });
        hintDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    /**
     * 是否同意商家服务政策与个人申报协议回调
     */
    @Override
    public void onAgreementChanged(boolean isChecked) {
        if (isChecked) {
            tv_pay.setEnabled(true);
        } else {
            tv_pay.setEnabled(false);
        }
    }

    @Override
    public void onCart() {
        finish();
    }

    @Override
    public void onModify() {
        BaseFunc.gotoActivity4Result(this, AddressActivity.class, CartCheckActivity.class.getName(), REQCODE);
    }
}
