package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/18-下午2:37.
 * 功能描述:品牌信息
 */
public class BrandMessage extends APIUtil {

    private String brand_id;//品牌id
    private String brand_name;//品牌名称
    private String brand_initial;//品牌首字母
    private String brand_class;//类别名称
    private String brand_pic;//品牌logo
    private String brand_url;//
    private String brand_intro;//品牌简介(描述)
    private String brand_page_pic;//品牌聚合页首图
    private String country_pic;//国旗
    private String country_name;//品牌国家
    private String share_img;//分享图片
    private String share_title;//分享标题
    private String share_desc;//分享文案

    private int goods_storage;//该品牌下所有商品(非库存) 在商品详情页叫 brand_goods_storage

    private String brand_mainpage_pic;//极速免税首页品牌

    private int is_favorites;//该会员针对此品牌是否关注过

    private List<BaseProduct> goods_list;//品牌向下的商品列表  极速免税

    //多余字段
    public boolean isExpanded;
    public int maxLines;


    public BrandMessage() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        BrandMessage brandMsg = new Gson().fromJson(data, BrandMessage.class);
                        if (callBack != null) {
                            callBack.onBrandMsgData(0, brandMsg);
                        }
                    } catch (Exception e) {
                        if (callBack != null) {
                            callBack.onBrandMsgData(-1, null);
                        }
                    }
                } else {
                    if (callBack != null) {
                        callBack.onBrandMsgData(-1, null);
                    }
                }
            }
        });
    }

    public void getBrandMessage(String brandId, Member member) {
        if (TextUtils.isEmpty(brandId)) {
            if (callBack != null) {
                callBack.onBrandMsgData(-1, null);
            }
            return;
        }
        String url = BaseVar.API_GET_BRANDMESSGE;
        url += "&brand_id=" + brandId;
        if (member != null) {
            url += "&mid=" + member.member_id;
        }
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private BrandMsgModelCallBack callBack;

    public void setModelCallBack(BrandMsgModelCallBack callBack) {
        this.callBack = callBack;
    }

    public interface BrandMsgModelCallBack {
        void onBrandMsgData(int errorCode, BrandMessage brandMessage);
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getBrand_initial() {
        return brand_initial;
    }

    public String getBrand_class() {
        return brand_class;
    }

    public String getBrand_pic() {
        return brand_pic;
    }

    public String getBrand_intro() {
        return brand_intro;
    }

    public String getBrand_page_pic() {
        return brand_page_pic;
    }

    public String getCountry_pic() {
        return country_pic;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getShare_img() {
        return share_img;
    }

    public String getShare_title() {
        return share_title;
    }

    public String getShare_desc() {
        return share_desc;
    }

    public String getGoods_storage() {
        return String.valueOf(goods_storage);
    }

    public String getShareUrl() {
        return String.format(BaseVar.URL_GET_GROUPSHAREURL, brand_id);
    }

    public boolean hasCollected() {
        return is_favorites == 1;
    }

    public void setIs_favorites(int is_favorites) {
        this.is_favorites = is_favorites;
    }

    public List<BaseProduct> getGoodslist() {
        return goods_list;
    }

    public Spanned getGoodsStorage(Context context) {
        String brandGoodsNum = String.format(context.getString(R.string.fmt_brand_goodsnum), goods_storage);
        return BaseFunc.fromHtml(brandGoodsNum);
    }

    public String getBrand_mainpage_pic() {
        return brand_mainpage_pic;
    }
}
