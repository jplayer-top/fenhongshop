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
 * author:Created by Plucky on 2016/12/8-下午1:35.
 * 功能描述: 极速免税 消费列表
 */
public class DutyfreeConsumeList extends DutyfreeConsume {

    public DutyfreeConsumeList() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (requestCallBack != null) {
                    if (isSuccess) {
                        List<DutyfreeConsume> list;
                        try {
                            list = new Gson().fromJson(data, new TypeToken<List<DutyfreeConsume>>() {
                            }.getType());
                        } catch (Exception e) {
                            list = null;
                        }
                        requestCallBack.onDutyfreeConsumeList(list);
                    } else {
                        requestCallBack.onDutyfreeConsumeList(null);
                    }
                }
            }
        });
    }


    public void getList(Member member, int curpage) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_CONSUME_LIST, params);
    }

    private DutyfreeConsumeListRequestCallBack requestCallBack;

    public void setRequestCallBack(DutyfreeConsumeListRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
    }

    public interface DutyfreeConsumeListRequestCallBack {
        void onDutyfreeConsumeList(List<DutyfreeConsume> list);
    }
}
