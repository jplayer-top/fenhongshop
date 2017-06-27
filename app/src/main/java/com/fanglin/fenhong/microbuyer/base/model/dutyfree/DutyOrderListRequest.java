package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/21-下午2:32.
 * 功能描述: 极速免税店 订单列表 请求
 */
public class DutyOrderListRequest extends DutyOrder {

    public DutyOrderListRequest() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (requestCallBack != null) {
                    if (isSuccess) {
                        List<DutyOrder> list;
                        try {
                            list = new Gson().fromJson(data, new TypeToken<List<DutyOrder>>() {
                            }.getType());
                        } catch (Exception e) {
                            list = null;
                        }
                        requestCallBack.onDutyOrderList(list);
                    } else {
                        requestCallBack.onDutyOrderList(null);
                    }
                }
            }
        });
    }

    /**
     * @param m       Member
     * @param state   1：待发货 2：未付款 3：待收货 4：已收货 5：所有订单
     * @param curpage int
     */
    public void getList(Member m, int state, int curpage) {
        RequestParams params = new RequestParams();
        if (m != null) {
            params.addBodyParameter("mid", m.member_id);
            params.addBodyParameter("token", m.token);
        }
        params.addBodyParameter("state", String.valueOf(state));
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_ORDERLIST, params);
    }

    private DutyOrderListRequestCallBack requestCallBack;

    public void setRequestCallBack(DutyOrderListRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
    }

    public interface DutyOrderListRequestCallBack {
        void onDutyOrderList(List<DutyOrder> list);
    }
}
