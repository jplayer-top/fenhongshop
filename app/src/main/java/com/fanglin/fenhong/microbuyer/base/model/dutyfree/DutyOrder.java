package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyOrderActionListener;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author;//Created by Plucky on 2016/11/21-下午1;//30.
 * 功能描述;//极速免税店 订单
 */
public class DutyOrder extends APIUtil {


    private String order_id;// "452",
    private String order_name;//极速免税

    private String taxes;
    private String order_sn;// "8000000000041401",
    private int order_state;// "10",
    private String payment_time;// "0000-00-00 00;//00;//00",
    private String pay_sn;// "980532994117436182",
    private int goods_amount;// "2",
    private double original_price;// "431.36",
    private double reality_price;// "388.22",
    private String state_desc;// "未付款",
    private String kefu_qq;// null,
    private String store_baidusales;// null,
    private String store_qrcode;//店铺客服二维码
    private List payment_list;//支持的付款方式
    private DutyPayOffline pay_offline;
    private List<DutyOrderGoods> order_goods;//
    private List<DutyWaybill> waybill;//运单

    private double goods_promotion_amount;//0.01
    private String goods_promotion_desc;//0~4万（9折）

    //进口商品的提示
    private String import_label;//进口商品
    private String import_desc;//9折优惠
    private String import_money;//-¥5.2.1

    private String balance_label;//余额抵扣
    private String balance_desc;//(可用余额¥5000.00)
    private String balance_money;//-¥5000.00

    //运费相关
    private String freight;//"¥0.00",
    private String goods_freight_title;//包邮提示
    private String freight_desc;// 进口商品$0.00<br/>VIP商品$9.00
    private String freight_weight;//（900.00）
    private String freight_intro;//  快递运费的提示

    private String item_label;//优惠方式
    private String item_desc;// 限时优惠
    private String item_money;// -$0.07

    public List<DutyWaybill> getWaybill() {
        return waybill;
    }

    public String getOrderName() {
        return order_name;
    }

    public String getFreightDesc() {
        return TextUtils.isEmpty(freight) ? "" : "（运费" + freight + "）";
    }

    public boolean showPromotion() {
        return !TextUtils.isEmpty(goods_promotion_desc) && goods_promotion_amount > 0;
    }

    public String getFreightSimpleDesc() {
        return TextUtils.isEmpty(freight) ? "" : freight;
    }

    public String getTaxes() {
        return TextUtils.isEmpty(taxes) ? "" : taxes;
    }

    public String getGoodsNumDesc() {
        return "共" + goods_amount + "件";
    }

    public String getKefu_qq() {
        return kefu_qq;
    }

    public DutyPayOffline getPay_offline() {
        return pay_offline;
    }

    public List<DutyOrderGoods> getOrder_goods() {
        return order_goods;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public int getOrder_state() {
        return order_state;
    }

    public String getOriginalPriceDesc() {
        DecimalFormat decimalFormat = new DecimalFormat("¥#0.00");
        return decimalFormat.format(original_price);
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public List getPayment_list() {
        return payment_list;
    }

    public Spanned getPaymentTimeDesc(Context context) {
        String fmt = String.format(context.getString(R.string.fmt_black_red), "订单时间", payment_time);
        return BaseFunc.fromHtml(fmt);
    }

    public String getPayPriceDesc() {
        DecimalFormat decimalFormat = new DecimalFormat("¥#0.00");
        return decimalFormat.format(reality_price);
    }

    public String getState_desc() {
        return state_desc;
    }

    public String getStore_baidusales() {
        return store_baidusales;
    }

    public String getStore_qrcode() {
        return store_qrcode;
    }

    public double getReality_price() {
        return reality_price;
    }

    public String getMoney4Dtl() {
        DecimalFormat decimalFormat = new DecimalFormat("合计：¥#0.00");
        return decimalFormat.format(reality_price);
    }

    public String getNumAndFreight() {
        return "共 " + goods_amount + " 件　运费:" + freight;
    }

    /**
     * 两个按钮的文本状态
     *
     * @return String[]
     */
    public String[] getButtonDesc(boolean isList) {
        String[] str = new String[]{"", ""};
        switch (order_state) {
            case 0:
                //已取消
                str[0] = DutyOrderActionListener.DELETE;
                str[1] = "";
                break;
            case 10:
                //待付款
                str[0] = DutyOrderActionListener.CANCEL;
                str[1] = DutyOrderActionListener.PAY;
                break;
            case 20:
            case 30:
                //待发货  列表显示查看详情  详情页不展示按钮
                str[0] = isList ? DutyOrderActionListener.DETAIL : "";
                str[1] = "";
                break;
            case 40:
                //待收货  列表均不展示物流跟踪 只有运单才能跟踪物流
                str[0] = isList ? DutyOrderActionListener.DETAIL : "";
                str[1] = DutyOrderActionListener.RECEIVE;
                break;
            case 50:
                //已收货 交易完成
                str[0] = isList ? DutyOrderActionListener.DETAIL : "";
                str[1] = DutyOrderActionListener.DELETE;//应该还有评价完成的状态  只有评价完成才能删除 这里先可以删除（TODO 后续设计会加上待评价状态）
                break;
            case 60:
                //订单异常
                str[0] = DutyOrderActionListener.DELETE;
                str[1] = "";
                break;
            default:
                break;
        }
        return str;
    }

    public String getGoodsProAmount() {
        DecimalFormat decimalFormat = new DecimalFormat("-¥#0.00");
        return decimalFormat.format(goods_promotion_amount);
    }

    public String getGoodsPromotionDesc() {
        return goods_promotion_desc;
    }

    public String getImportDesc() {
        return import_desc;
    }

    public String getImportLabel() {
        return import_label;
    }

    public String getImportMoney() {
        return import_money;
    }

    public boolean showImport() {
        return !TextUtils.isEmpty(import_label) && !TextUtils.isEmpty(import_desc) && !TextUtils.isEmpty(import_money);
    }

    public String getBalanceDesc() {
        return balance_desc;
    }

    public String getBalanceLabel() {
        return balance_label;
    }

    public String getBalanceMoney() {
        return balance_money;
    }

    public boolean showBalance() {
        return !TextUtils.isEmpty(balance_label) && !TextUtils.isEmpty(balance_desc) && !TextUtils.isEmpty(balance_money);
    }

    public boolean showFreightIntro() {
        return !TextUtils.isEmpty(freight_intro);
    }

    public Spanned getFreightIntroHtml() {
        return BaseFunc.fromHtml(freight_intro);
    }

    public Spanned getFreightWeightHtml() {
        return BaseFunc.fromHtml(freight_weight);
    }

    public Spanned getFreightDescHtml() {
        return BaseFunc.fromHtml(freight_desc);
    }

    public String getGoodsFreightTitle() {
        return goods_freight_title;
    }

    public String getItemLabel() {
        return item_label;
    }

    public String getItemDesc() {
        return item_desc;
    }

    public String getItemMoney() {
        return item_money;
    }

    /**
     * 是否显示ItemPrice
     *
     * @return true, false
     */
    public boolean isItemVisible() {
        return !TextUtils.isEmpty(item_label) && !TextUtils.isEmpty(item_desc) && !TextUtils.isEmpty(item_money);
    }
}
