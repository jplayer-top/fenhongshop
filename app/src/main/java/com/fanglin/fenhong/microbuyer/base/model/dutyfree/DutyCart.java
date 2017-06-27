package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/19-下午3:13.
 * 功能描述: 极速免税购物车
 */
public class DutyCart extends APIUtil {
    private List<DutyCartProduct> goods_list;
    private double goods_all_price;//商品总额
    private String taxes;//税费
    private String freight;//¥0.00
    private String goods_promotion_desc;//活动标签
    private double goods_promotion_amount;//活动价
    private int total_num;//总商品数
    private double total_price;//合计金额
    private String import_label;//进口商品
    private String import_desc;//9折优惠
    private String import_money;//-¥5.2.1

    private String balance_label;//余额抵扣
    private String balance_desc;//(可用余额¥5000.00)
    private String balance_money;//-¥5000.00

    //购物车凑单提示
    private DutyCartTips disparity;

    private String item_label;//优惠方式
    private String item_desc;// 限时优惠
    private String item_money;// -$0.07

    public String getGoodsAllPrice() {
        DecimalFormat decimalFormat = new DecimalFormat("¥#0.00");
        return decimalFormat.format(goods_all_price);
    }

    public List<DutyCartProduct> getGoodsList() {
        return goods_list;
    }

    public String getTotalNum() {
        return String.valueOf(total_num);
    }

    public String getTaxes() {
        return taxes;
    }

    public String getFreight() {
        return freight;
    }

    public String getGoodsPromotionAmount() {
        DecimalFormat decimalFormat = new DecimalFormat("-¥#0.00");
        return decimalFormat.format(goods_promotion_amount);
    }

    public String getGoodsPromotionDesc() {
        return goods_promotion_desc;
    }

    public boolean showPromotion() {
        return !TextUtils.isEmpty(goods_promotion_desc) && goods_promotion_amount > 0;
    }

    public String getTotalPrice() {
        DecimalFormat decimalFormat = new DecimalFormat("¥#0.00");
        return decimalFormat.format(total_price);
    }

    public String getImportDesc() {
        return import_desc;
    }

    public String getImportLabel() {
        return import_label;
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

    public String getImportMoney() {
        return import_money;
    }

    public DutyCartTips getDisparity() {
        return disparity;
    }

    public DutyCart() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (dutyCartRequestCallback != null) {
                    if (isSuccess) {
                        DutyCart cart;
                        try {
                            cart = new Gson().fromJson(data, DutyCart.class);
                        } catch (Exception e) {
                            cart = null;
                        }
                        dutyCartRequestCallback.onDutyCartData(cart);
                    } else {
                        dutyCartRequestCallback.onDutyCartData(null);
                    }
                }
            }
        });
    }

    private DutyCartRequestCallback dutyCartRequestCallback;

    public void setDutyCartRequestCallback(DutyCartRequestCallback dutyCartRequestCallback) {
        this.dutyCartRequestCallback = dutyCartRequestCallback;
    }

    public interface DutyCartRequestCallback {
        void onDutyCartData(DutyCart data);
    }

    public void getData(Member member) {
        String url = BaseVar.API_DUTYFREE_CARTLIST;
        if (member != null) {
            url += "&mid=" + member.member_id;
            url += "&token=" + member.token;
        }

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    public boolean showImport() {
        return !TextUtils.isEmpty(import_label) && !TextUtils.isEmpty(import_desc) && !TextUtils.isEmpty(import_money);
    }

    public boolean showBalance() {
        return !TextUtils.isEmpty(balance_label) && !TextUtils.isEmpty(balance_desc) && !TextUtils.isEmpty(balance_money);
    }

    public void refreshByEditResult(DutyCartEditResult result) {
        if (result != null) {
            goods_all_price = result.getGoods_all_price();
            goods_promotion_desc = result.getGoods_promotion_desc();
            goods_promotion_amount = result.getGoods_promotion_amount();
            total_num = result.getTotal_num();
            total_price = result.getTotal_price();
            import_label = result.getImportLabel();
            import_desc = result.getImportDesc();
            import_money = result.getImportMoney();

            balance_label = result.getBalanceLabel();
            balance_desc = result.getBalanceDesc();
            balance_money = result.getBalanceMoney();

            item_label = result.getItemLabel();
            item_desc = result.getItemDesc();
            item_money = result.getItemMoney();

            disparity = result.getDisparity();
            taxes = result.getTaxes();
            freight = result.getFreight();
        }
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
