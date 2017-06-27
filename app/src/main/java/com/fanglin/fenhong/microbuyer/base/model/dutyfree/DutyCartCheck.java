package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.text.Spanned;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.PopupInfo;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/20-上午9:12.
 * 功能描述: 急速免税订单确认
 */
public class DutyCartCheck extends APIUtil {

    private String goods_promotion_desc;// "0.90",
    private double goods_amount;// 509.2,
    private double goods_promotion_amount;// 50.92,
    private double total_price;// 458.28,

    //运费相关
    private String freight;//"¥0.00",
    private String goods_freight_title;//包邮提示
    private String freight_desc;// 进口商品$0.00<br/>VIP商品$9.00
    private String freight_weight;//（900.00）
    private String freight_intro;//  快递运费的提示

    private String taxes;// "¥0.00",
    private String all_product_num;// 2
    private List<DutyCartProduct> goods_list;
    private boolean hasChecked;
    private String agreement;//协议

    //进口商品的提示
    private String import_label;//进口商品
    private String import_desc;//9折优惠
    private String import_money;//-¥5.2.1

    private String balance_label;//余额抵扣
    private String balance_desc;//(可用余额¥5000.00)
    private String balance_money;//-¥5000.00
    private int is_selected;//是否使用了余额

    private PopupInfo popup_info;

    private String item_label;//优惠方式
    private String item_desc;// 限时优惠
    private String item_money;// -$0.07

    public DutyCartCheck() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (requestCallBack != null) {
                    if (isSuccess) {
                        DutyCartCheck check;
                        try {
                            check = new Gson().fromJson(data, DutyCartCheck.class);
                        } catch (Exception e) {
                            check = null;
                        }
                        requestCallBack.onRequestCallBack(check);
                    } else {
                        requestCallBack.onRequestCallBack(null);
                    }
                }
            }
        });
    }

    public boolean showPromotion() {
        return !TextUtils.isEmpty(goods_promotion_desc) && goods_promotion_amount > 0;
    }

    public void getData(Member m, String cart_info, String from, int is_selected) {
        RequestParams params = new RequestParams();
        if (m != null) {
            params.addBodyParameter("mid", m.member_id);
            params.addBodyParameter("token", m.token);
        }
        params.addBodyParameter("cart_info", cart_info);
        params.addBodyParameter("from", from);
        params.addBodyParameter("is_selected", String.valueOf(is_selected));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_ORDERCONFIRM, params);
    }

    private DutyCartCheckRequestCallBack requestCallBack;

    public void setRequestCallBack(DutyCartCheckRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
    }

    public interface DutyCartCheckRequestCallBack {
        void onRequestCallBack(DutyCartCheck data);
    }

    public String getAllNum() {
        return all_product_num;
    }

    public String getAllNumDesc() {
        return "共" + getAllNum() + "件";
    }

    public String getGoodsAmountDesc() {
        DecimalFormat decimalFormat = new DecimalFormat("¥#0.00");
        return decimalFormat.format(goods_amount);
    }

    public List<DutyCartProduct> getGoodsList() {
        return goods_list;
    }

    public String getGoodsProAmount() {
        DecimalFormat decimalFormat = new DecimalFormat("-¥#0.00");
        return decimalFormat.format(goods_promotion_amount);
    }

    public String getGoodsPromotionDesc() {
        return goods_promotion_desc;
    }

    public String getTaxes() {
        return taxes;
    }

    public String getTotalPriceDesc() {
        DecimalFormat decimalFormat = new DecimalFormat("¥#0.00");
        return decimalFormat.format(total_price);
    }

    public boolean isHasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }

    public String getAgreement() {
        return agreement;
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

    public String getBalanceDesc() {
        return balance_desc;
    }

    public String getBalanceLabel() {
        return balance_label;
    }

    public String getBalanceMoney() {
        return balance_money;
    }

    public int getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(int is_selected) {
        this.is_selected = is_selected;
    }

    public boolean showBalance() {
        return !TextUtils.isEmpty(balance_label) && !TextUtils.isEmpty(balance_desc) && !TextUtils.isEmpty(balance_money);
    }

    public boolean showImport() {
        return !TextUtils.isEmpty(import_label) && !TextUtils.isEmpty(import_desc) && !TextUtils.isEmpty(import_money);
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

    //弹窗提示
    public PopupInfo getPopupInfo() {
        return popup_info;
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
