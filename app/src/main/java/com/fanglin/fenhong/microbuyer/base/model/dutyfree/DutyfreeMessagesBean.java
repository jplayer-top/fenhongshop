package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/9.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */
public class DutyfreeMessagesBean extends APIUtil {
    public String msg_title;
    public String curtime;
    public String msg;
    public String is_read;
    public String msg_url;

    public DutyfreeMessagesBean() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (callBack != null) {
                    if (isSuccess) {
                        try {
                            List<DutyfreeMessagesBean> dutyfreeMessagesBeen = new Gson().fromJson(data, new TypeToken<List<DutyfreeMessagesBean>>() {
                            }.getType());
                            callBack.onRequDataCallBack(dutyfreeMessagesBeen);
                        } catch (JsonSyntaxException e) {
                            callBack.onRequDataCallBack(null);
                        }
                    } else {
                        callBack.onRequDataCallBack(null);
                    }
                }
            }

        });
    }

    public void requData(int curpage, Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_MESSAGE_LIST, params);
    }

    private RequDataCallBack callBack;

    public void setRequDataCallBack(RequDataCallBack callBack) {
        this.callBack = callBack;
    }

    public interface RequDataCallBack {
        void onRequDataCallBack(List<DutyfreeMessagesBean> dutyfreeMessagesBeen);
    }
}
