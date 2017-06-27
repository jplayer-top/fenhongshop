package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午3:13.
 * 功能描述: 极速免税店 商品搜索页请求
 */
public class DutyGoodsListRequest extends BaseProduct {
    public DutyGoodsListRequest() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (goodsListReqCallback != null) {
                    if (isSuccess) {
                        List<BaseProduct> list;
                        try {
                            list = new Gson().fromJson(data, new TypeToken<List<BaseProduct>>() {
                            }.getType());
                        } catch (Exception e) {
                            list = null;
                            FHLog.d("Plucky", e.getMessage());
                        }
                        goodsListReqCallback.onDutyGoodsList(list);
                    } else {
                        goodsListReqCallback.onDutyGoodsList(null);
                    }
                }
            }
        });
    }

    public void getList(Member member, int curpage, int type, int sort, String name) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("type", String.valueOf(type));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("sort", String.valueOf(sort));
        if (!TextUtils.isEmpty(name)) {
            params.addBodyParameter("name", name);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_GOODSLIST, params);
    }

    private DutyGoodsListReqCallback goodsListReqCallback;

    public void setGoodsListReqCallback(DutyGoodsListReqCallback goodsListReqCallback) {
        this.goodsListReqCallback = goodsListReqCallback;
    }

    public interface DutyGoodsListReqCallback {
        void onDutyGoodsList(List<BaseProduct> list);
    }
}
