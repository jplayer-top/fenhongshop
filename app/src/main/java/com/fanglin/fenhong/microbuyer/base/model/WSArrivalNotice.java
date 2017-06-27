package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 我的提醒 商品列表请求类
 * Created by lizhixin on 2016/04/06.
 */
public class WSArrivalNotice extends APIUtil {

    private WSArrivalNoticeCallBack mcb;

    public WSArrivalNotice() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        ArrayList<GoodsArrivalNotice> adata = new Gson().fromJson(data, new TypeToken<ArrayList<GoodsArrivalNotice>>(){}.getType());
                        if (mcb != null) mcb.onArrivalNoticeSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onArrivalNoticeError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onArrivalNoticeError(data);
                }
            }
        });
    }

    /**
     * @param m
     * @param curpage
     * @param type 可选，不传返回全部 1: 到货通知 | 2: 预售提醒
     */
    public void getList (Member m, int curpage, String type) {
        if (m == null) {
            if (mcb != null) mcb.onArrivalNoticeError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("num", String.valueOf (BaseVar.REQUESTNUM));
        params.addBodyParameter ("curpage", String.valueOf (curpage));//当前页
        params.addBodyParameter ("type", type);//可选，不传返回全部 1: 到货通知 | 2: 预售提醒

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_ARRIVAL_NOTICE_GOODS, params);
    }

    public void setWSArrivalNoticeCallBack(WSArrivalNoticeCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSArrivalNoticeCallBack {
        void onArrivalNoticeError(String errcode);

        void onArrivalNoticeSuccess (ArrayList<GoodsArrivalNotice> data);
    }

}
