package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午4:17.
 * 功能描述: 急速免税店 品牌搜索
 */
public class DutyBrandRequest extends BrandMessage {

    public DutyBrandRequest() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (brandRequestCallBack != null) {
                    if (isSuccess) {
                        List<BrandMessage> brandList;
                        try {
                            brandList = new Gson().fromJson(data, new TypeToken<List<BrandMessage>>() {
                            }.getType());
                        } catch (Exception e) {
                            brandList = null;
                        }
                        brandRequestCallBack.onDutyBrandList(brandList);
                    } else {
                        brandRequestCallBack.onDutyBrandList(null);
                    }
                }
            }
        });
    }

    private DutyBrandRequestCallBack brandRequestCallBack;

    public void setBrandRequestCallBack(DutyBrandRequestCallBack brandRequestCallBack) {
        this.brandRequestCallBack = brandRequestCallBack;
    }

    public interface DutyBrandRequestCallBack {
        void onDutyBrandList(List<BrandMessage> brandList);
    }

    public void getList(Member member, int curpage, String name) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        if (!TextUtils.isEmpty(name)) {
            params.addBodyParameter("name", name);
        }
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_BRANDLIST, params);
    }

    /**
     * 获取所有品牌列表
     *
     * @param member Member
     */
    public void getAllList(Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("all", "1");

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_BRANDLIST, params);
    }
}
