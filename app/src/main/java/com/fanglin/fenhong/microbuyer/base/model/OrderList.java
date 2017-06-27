package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/25.
 * 订单列表页
 */
public class OrderList extends APIUtil {
    public String order_id;// 订单id
    public String order_sn;// 订单编号
    public String pay_sn;// 支付单号
    public String store_id;// 店铺id
    public String store_name;// 店铺名称
    public int order_state;// 订单状态码 （0：已取消 | 10：未付款 | 20：已付款 30：已发货 40：已收货 50：已失败）
    public long add_time;// 订单添加时间
    public double real_amount;// 实际支付金额

    public double shipping_fee;// 订单运费
    public String shipping_weight;//快递重量

    public int goods_num;// 商品数量

    public int country_source;//订单国别：0:国内 | 1:韩国
    public String evaluation_state;//订单评价状态 0:未评价 | 1:已评价 | 2:系统超期不允许评价 | 3:已追加评价
    public String state_desc;//订单状态描述

    public long validity_pay_time = 0;// 剩余支付时间秒数 (过期返回0)

    public List payment_list;//alipay：支付宝,  wxpay：微信,  chinapay：银联
    public List<OrderGoods> extend_order_goods;//订单商品信息

    public int order_custom; //海关id ( 0 青岛泛亚 1郑州捷龙 ...)

    //传pay_sn会返回以下字段
    public int all_goods_num;//
    public double all_goods_amount;//
    public double all_goods_shipping_fee;//总运费
    public double all_real_amount;//需要实际支付所有订单总额
    public ReciverInfo reciver_info;//收货信息

    public String store_baidusales;//百度商桥
    public String pd_amount;//站内余额金额
    public String coupon_amount;//优惠券金额
    public double free_freight;//满额包邮金额
    public GoodsDtlPromMansongRules mansong;//满额优惠

    public List<OrderList> merge_order_list;//待付款合并订单列表 (合并付款不会合并显示)

    public double micro_shoper_save_money;//微店主身份能节省的钱
    public String micro_shoper_save_desc;//红人店铺身份描述

    public String getPd_amountDesc() {
        double d = getPd_amount();
        DecimalFormat df = new DecimalFormat("-¥#0.00");
        return df.format(d);
    }

    public double getPd_amount() {
        try {
            return Double.valueOf(pd_amount);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getShipping_weight() {
        if (TextUtils.isEmpty(shipping_weight)) {
            return shipping_weight;
        } else {
            return "(" + shipping_weight + ")";
        }
    }

    public String getCoupon_amountDesc() {
        double d = getCoupon_amount();
        DecimalFormat df = new DecimalFormat("-¥#0.00");
        return df.format(d);
    }

    public double getCoupon_amount() {
        try {
            return Double.valueOf(coupon_amount);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getCalculateLable(Context mContext) {
        if (order_state > 10) {
            return mContext.getString(R.string.has_payed);
        } else {
            return mContext.getString(R.string.has_to_pay);
        }
    }

    /**
     * 获取订单内商品的状态 只有所有商品都有效时订单才有效且允许支付
     */
    public int getOrderGoodsState() {
        int result = 1;
        if (extend_order_goods != null && extend_order_goods.size() > 0) {
            for (OrderGoods goods : extend_order_goods) {
                if (!TextUtils.equals(goods.goods_state, "1")) {
                    //有无效商品
                    result = 0;
                    break;
                }
            }
        } else {
            result = 0;
        }
        return result;
    }

    public OrderList() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<OrderList> lst = new Gson().fromJson(data, new TypeToken<List<OrderList>>() {
                        }.getType());
                        if (mcb != null) mcb.onOrderList(lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onOrderError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onOrderError(data);
                }
            }
        });
    }

    public void get_order_list(String mid, String token, String pay_sn, int state, int curpage) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        if (state != -1) {
            params.addBodyParameter("state", String.valueOf(state));
        }
        if (pay_sn != null) {
            params.addBodyParameter("pay_sn", pay_sn);
        }

        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_ORDER_LIST, params);
    }

    private OrderModelCallBack mcb;

    public void setModelCallBack(OrderModelCallBack cb) {
        this.mcb = cb;
    }

    public interface OrderModelCallBack {

        void onOrderError(String errcode);

        void onOrderList(List<OrderList> list);

    }


    /**
     *   A
     *
     *   C
     *
     *   B
     *
     *
     *   D
     *
     *   F
     *
     *
     *   G
     */


    /**
     * 第0个表示订单的状态,1个表示第一个按钮的状态，2表示第二个按钮的状态
     */
    public static String[] getOrderStateDesc(int _order_state, long _validity_pay_time, String _evaluation_state) {
        String[] res = new String[]{"", "", ""};
        switch (_order_state) {
            case 0:
                res[0] = "交易取消";//已取消
                res[1] = "删除订单";
                break;
            case 10:
                if (_validity_pay_time > 0) {
                    res[0] = "未付款";
                } else {
                    res[0] = "过期订单";
                }
                res[1] = "取消订单";
                res[2] = "付款";
                break;
            case 20:
                res[0] = "分红全球购";
                res[1] = "分红全球购";
                res[2] = "订单跟踪";
                break;
            case 30:
                res[0] = "已发货";
                res[1] = "订单跟踪";
                res[2] = "确认收货";
                break;
            case 40:
                if (TextUtils.equals("0", _evaluation_state)) {
                    res[0] = "删除订单";
                    res[1] = "订单跟踪";
                    res[2] = "评价";
                } else if (TextUtils.equals("1", _evaluation_state)) {
                    res[0] = "删除订单";
                    res[1] = "订单跟踪";
                    res[2] = "追加评价";
                } else {
                    res[0] = "删除订单";
                    res[1] = "订单跟踪";
                    res[2] = "分红全球购";
                }
                break;
            case 50:
                res[0] = "交易失败";//已失败
                res[1] = "删除订单";
                res[2] = "商城客服";
                break;
        }

        return res;
    }


    /**
     * 是否为合并付款的订单
     *
     * @return true false
     */
    public boolean isMerge() {
        return merge_order_list != null && merge_order_list.size() > 0;
    }

    public OrderList getMergeOrder(int position) {
        return merge_order_list.get(position);
    }

    /**
     * 合并付款的订单数量
     *
     * @return int
     */
    public int getMergeCount() {
        if (isMerge()) {
            return merge_order_list.size();
        } else {
            return 0;
        }
    }

    /**
     * 获取商品数量
     *
     * @return int
     */
    public int getOrderGoodsCount() {
        if (extend_order_goods == null) return 0;
        return extend_order_goods.size();
    }

    /**
     * 商品是否为套装
     *
     * @param position int
     * @return true false
     */
    public boolean isOrderGoodsBundling(int position) {
        return position < getOrderGoodsCount() && extend_order_goods.get(position).isBundling();
    }

    public double getPayMoney() {
        if (isMerge()) {
            return all_real_amount;
        }
        return real_amount;
    }

    public Spanned getOrderStateDesc() {
        return Html.fromHtml(state_desc);
    }

    /**
     * 是否显示红人店铺的优惠
     *
     * @return boolean
     */
    public boolean showMicroShopSave() {
        return micro_shoper_save_money > 0 && !TextUtils.isEmpty(micro_shoper_save_desc);
    }

    public Spanned getMicroShoperSaveDesc() {
        return Html.fromHtml(micro_shoper_save_desc);
    }

    public String getMicroShoperSaveMoney() {
        return "-¥" + micro_shoper_save_money;
    }
}
