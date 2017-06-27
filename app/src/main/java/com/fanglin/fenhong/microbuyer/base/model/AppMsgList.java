package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/27.
 */
public class AppMsgList extends APIUtil {
    public String message_id;//消息id
    public String message_title;//标题
    public String message;//内容
    public String url;//打开链接
    public String type;//消息类型  ( 9:系统消息 | 12:优惠促销 | 13:订单提醒 | 14:物流通知 | 15:我的钱包 | 16:我的团队 )
    public String images_url;//
    //public long add_time;//
    public long send_time;//
    public String timer;//
    public String user_list;//
    public String flag;//
    public String app;//

    public AppMsgList () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<AppMsgList> list = new Gson ().fromJson (data, new TypeToken<List<AppMsgList>> () {
                        }.getType ());
                        if (mcb != null) mcb.onList (list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onError (data);
                }
            }
        });
    }

    public void getList (Member m, int type, int curpage) {
        if (type < 0) {
            if (mcb != null) mcb.onError ("-1");
            return;
        }
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("num", BaseVar.REQUESTNUM + "");
        params.addBodyParameter ("curpage", curpage + "");
        params.addBodyParameter ("type", type + "");
        if (m != null) {
            params.addBodyParameter ("mid", m.member_id);
            params.addBodyParameter ("token", m.token);
        }

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_APP_MSGLST, params);
    }

    public AMLModelCallBack mcb;

    public void setModelCallBack (AMLModelCallBack cb) {
        this.mcb = cb;
    }

    public interface AMLModelCallBack {
        void onError (String errcode);

        void onList (List<AppMsgList> list);
    }
}
