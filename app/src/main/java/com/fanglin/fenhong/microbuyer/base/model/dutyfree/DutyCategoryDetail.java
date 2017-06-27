package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Banner;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午6:42.
 * 功能描述: 极速免税店 详情
 */
public class DutyCategoryDetail extends APIUtil {
    private List<Banner> banners;
    private List<DutyfreeCategory> category;

    public List<Banner> getBanners() {
        return banners;
    }

    public List<DutyfreeCategory> getCategory() {
        return category;
    }

    public DutyCategoryDetail() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (categoryDetailRequestCallBack != null) {
                    if (isSuccess) {
                        DutyCategoryDetail detail;
                        try {
                            detail = new Gson().fromJson(data, DutyCategoryDetail.class);
                        } catch (Exception e) {
                            detail = null;
                            FHLog.d("Plucky", e.getMessage());
                        }
                        categoryDetailRequestCallBack.onDutyCategoryDetailList(detail);
                    } else {
                        categoryDetailRequestCallBack.onDutyCategoryDetailList(null);
                    }
                }
            }
        });
    }

    private DutyCategoryDetailRequestCallBack categoryDetailRequestCallBack;

    public void setCategoryDetailRequestCallBack(DutyCategoryDetailRequestCallBack categoryDetailRequestCallBack) {
        this.categoryDetailRequestCallBack = categoryDetailRequestCallBack;
    }

    public interface DutyCategoryDetailRequestCallBack {
        void onDutyCategoryDetailList(DutyCategoryDetail detail);
    }

    public void getList(Member member, String id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("id", id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_CATEGORY_DETAIL, params);
    }
}
