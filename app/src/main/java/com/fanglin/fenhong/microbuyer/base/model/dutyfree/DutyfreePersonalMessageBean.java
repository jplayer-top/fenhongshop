package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/6.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */

public class DutyfreePersonalMessageBean extends APIUtil {
    public String my_award;
    public String total_money;
    public String total_person;
    public List<AwardListBean> award_list;

    public static class AwardListBean {
        public String pay_img;
        public String pay_money;
        public String curtime;
        public String phone_num;
    }

    public DutyfreePersonalMessageBean() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (dataCallBack != null) {
                    DutyfreePersonalMessageBean messageBean;
                    if (isSuccess) {
                        try {
                            messageBean = new Gson().fromJson(data, DutyfreePersonalMessageBean.class);
                            dataCallBack.onRequData(messageBean);
                        } catch (JsonSyntaxException e) {
                            messageBean = null;
                            e.printStackTrace();
                            FHLog.d("Oblivion", e.getLocalizedMessage());
                        }
                        dataCallBack.onRequData(messageBean);
                    } else {
                        dataCallBack.onRequData(null);
                    }
                }
            }
        });
    }


    //添加megre
    private RequDataCallBack dataCallBack;

    /**
     * 请求数据
     */
    public void requData(int curpage, Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            String member_id = member.member_id;
            params.addBodyParameter("mid", member_id);
            String token = member.token;
            params.addBodyParameter("token", token);
        }
        params.addBodyParameter("have_list", "1");
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_MYINVITE_LIST, params);
    }

    public void setRequDataCallBack(RequDataCallBack dataCallBack) {
        this.dataCallBack = dataCallBack;
    }

    public interface RequDataCallBack {
        void onRequData(DutyfreePersonalMessageBean messageBean);
    }


}
