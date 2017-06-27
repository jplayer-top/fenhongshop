package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/2-上午11:58.
 * 功能描述: 极速免税店 购物车数量 + 其他
 */
public class DutyFreeCount extends APIUtil {
    private int cart_num;//购物车数量

    public DutyFreeCount() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (requestCallBack != null) {
                    if (isSuccess) {
                        DutyFreeCount dutyFreeCount;
                        try {
                            dutyFreeCount = new Gson().fromJson(data, DutyFreeCount.class);
                        } catch (Exception e) {
                            dutyFreeCount = null;
                        }
                        requestCallBack.onDutyFreeCountRequest(dutyFreeCount);
                    } else {
                        requestCallBack.onDutyFreeCountRequest(null);
                    }
                }
            }
        });
    }


    public void getCount(Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_COUNT, params);
    }

    private DutyFreeCountRequestCallBack requestCallBack;

    public void setRequestCallBack(DutyFreeCountRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
    }

    public interface DutyFreeCountRequestCallBack {
        void onDutyFreeCountRequest(DutyFreeCount data);
    }

    public String getCartNumDesc() {
        if (cart_num > 99) {
            return "99+";
        } else {
            return String.valueOf(cart_num);
        }
    }

    public boolean showCartNum() {
        return cart_num > 0;
    }
}
