package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Banner;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/18-下午2:57.
 * 功能描述: 首页一级分类向下
 */
public class HomeCategory extends APIUtil {
    private List<Banner> banners;//轮播图
    private List<DutyfreeCategory> category;//分类
    private List<BaseProduct> goods_list;//商品列表

    public HomeCategory() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (reqCallback != null) {
                    if (isSuccess) {
                        HomeCategory category;
                        try {
                            category = new Gson().fromJson(data, HomeCategory.class);
                        } catch (Exception e) {
                            category = null;
                        }
                        reqCallback.onHomeCategoryReqData(category);
                    } else {
                        reqCallback.onHomeCategoryReqData(null);
                    }
                }
            }
        });
    }

    public void getData(Member member, String id, int curpage) {
        String url = BaseVar.API_DUTYFREE_HOMECATEGORY;
        if (member != null) {
            url += "&mid=" + member.member_id;
            url += "&token=" + member.token;
        }
        url += "&id=" + id;
        url += "&curpage=" + curpage;
        url += "&num=" + BaseVar.REQUESTNUM_10;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private HomeCategoryRequestCallBack reqCallback;

    public void setReqCallback(HomeCategoryRequestCallBack reqCallback) {
        this.reqCallback = reqCallback;
    }

    public interface HomeCategoryRequestCallBack {
        void onHomeCategoryReqData(HomeCategory data);
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public List<DutyfreeCategory> getCategory() {
        return category;
    }

    public List<BaseProduct> getGoodsList() {
        return goods_list;
    }
}
