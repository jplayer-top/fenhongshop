package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Banner;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/18-下午5:21.
 * 功能描述: 极速免税店 首页品牌
 */
public class HomeBrand extends APIUtil {
    private List<Banner> banners;//轮播图
    private List<BrandMessage> goods_brand;//品牌
    private List<BaseProduct> goods_list;//商品列表


    public HomeBrand() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (brandRequestCallBack != null) {
                    if (isSuccess) {
                        HomeBrand brand;
                        try {
                            brand = new Gson().fromJson(data, HomeBrand.class);
                        } catch (Exception e) {
                            brand = null;
                            FHLog.d("Plucky", e.getMessage());
                        }
                        brandRequestCallBack.onHomeBrandReqData(brand);
                    } else {
                        brandRequestCallBack.onHomeBrandReqData(null);
                    }
                }
            }
        });
    }

    public void getBrand(Member member, int curpage) {
        String url = BaseVar.API_DUTYFREE_HOMEBRAND;
        if (member != null) {
            url += "&mid=" + member.member_id;
            url += "&token=" + member.token;
        }
        url += "&num=" + BaseVar.REQUESTNUM_10;
        url += "&curpage=" + curpage;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public List<BaseProduct> getGoodsList() {
        return goods_list;
    }

    public List<BrandMessage> getGoodsBrand() {
        return goods_brand;
    }

    private HomeBrandRequestCallBack brandRequestCallBack;

    public void setBrandRequestCallBack(HomeBrandRequestCallBack brandRequestCallBack) {
        this.brandRequestCallBack = brandRequestCallBack;
    }

    public interface HomeBrandRequestCallBack {
        void onHomeBrandReqData(HomeBrand data);
    }
}
