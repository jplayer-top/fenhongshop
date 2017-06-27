package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.content.Context;
import android.text.Spanned;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Banner;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.FHSpanned;
import com.fanglin.fenhong.microbuyer.base.model.GoodsIntro;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.PopupInfo;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/10-上午9:23.
 * 功能描述:急速免税商品详情模型
 */
public class ProductDetail extends BaseProduct {
    private List<String> banner;
    private List<GoodsIntro> goods_intro_info;//商品说明信息
    private String goods_notice;// "商品出现任何问题，质量、破损平台等值赔",
    private String goods_discount_desc;// "40000元以下 打9折 / 40000元以上打8.5折",

    private String goods_freight_title;//包邮提示
    private String goods_freight_desc;// "包邮",
    private String goods_freight_intro;//税费说明

    private BrandMessage brand;//所属品牌

    private String nation_flag;//国旗
    private String goods_promise;//极速免税 正品保证

    private String tax_fee;//$0.00
    private String tax_desc;//本商品包税，无需再额外缴纳

    private String goods_dtl_url;//图文详情页URL
    private String store_baidusales;//百度商桥URL
    // 参数相关
    private LinkedTreeMap goods_attr;//    商品参数  {参数名 : 参数值 ,...}

    private Favorable favorable;
    private PopupInfo popup_info;//加入购物车弹窗提示

    public ProductDetail() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (productDtlReqCallback != null) {
                    if (isSuccess) {
                        ProductDetail detail;
                        try {
                            detail = new Gson().fromJson(data, ProductDetail.class);
                        } catch (Exception e) {
                            FHLog.d("Plucky", e.getMessage());
                            detail = null;
                        }
                        productDtlReqCallback.onProductDtlReqCallback(detail);
                    } else {
                        productDtlReqCallback.onProductDtlReqCallback(null);
                    }
                }
            }
        });
    }

    public void getData(Member member, String product_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("product_id", product_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_GOODSDTL, params);
    }

    private ProductDetailRequestCallBack productDtlReqCallback;

    public void setProductDtlReqCallback(ProductDetailRequestCallBack productDtlReqCallback) {
        this.productDtlReqCallback = productDtlReqCallback;
    }

    public interface ProductDetailRequestCallBack {
        void onProductDtlReqCallback(ProductDetail detail);
    }

    public String getTaxFee() {
        return tax_fee;
    }

    public BrandMessage getBrand() {
        return brand;
    }

    public String getGoodsDiscountDesc() {
        return goods_discount_desc;
    }

    public String getGoodsDtlUrl() {
        return goods_dtl_url;
    }

    public Spanned getGoodsFreightDesc() {
        return BaseFunc.fromHtml(goods_freight_desc);
    }

    public List<GoodsIntro> getGoodsIntroInfo() {
        return goods_intro_info;
    }

    public String getGoodsNotice() {
        return goods_notice;
    }

    public String getGoodsPromise() {
        return goods_promise;
    }

    public String getNationFlag() {
        return nation_flag;
    }

    public String getStoreBaidusales() {
        return store_baidusales;
    }

    public String getTaxDesc() {
        return tax_desc;
    }

    public CharSequence getRefundTips(Context c) {
        if (goods_intro_info != null && goods_intro_info.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (GoodsIntro intro : goods_intro_info) {
                sb.append("[fwarn]");
                sb.append("  ");
                sb.append(intro.getTitle());
                sb.append("    ");
            }
            return FHSpanned.convertNormalStringToSpannableString(c, sb.toString());
        } else {
            return "";
        }
    }

    public LinkedTreeMap getGoods_attr() {
        return goods_attr;
    }

    public List<Banner> getImageBanner() {
        if (banner != null && banner.size() > 0) {
            List<Banner> banners = new ArrayList<>();
            for (int i = 0; i < banner.size(); i++) {
                Banner b = new Banner();
                b.index = i;
                if (BaseFunc.isValidUrl(banner.get(i))) {
                    b.image_url = banner.get(i);
                } else {
                    b.image_url = BaseVar.DEFAULT_BANNER;
                }
                banners.add(b);
            }
            return banners;
        }
        return null;
    }

    //税费说明
    public Spanned getGoodsFreightIntro() {
        return BaseFunc.fromHtml(goods_freight_intro);
    }

    public String getGoodsFreightTitle() {
        return goods_freight_title;
    }

    public Favorable getFavorable() {
        return favorable;
    }

    public PopupInfo getPopupInfo() {
        return popup_info;
    }
}
