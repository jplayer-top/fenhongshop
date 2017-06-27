package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/8-下午5:26.
 * 功能描述:达人首页
 */
public class TalentHomeRequest extends APIUtil {
    private TalentShare share;//分享信息
    private TalentInfo talent_info;//达人信息， 只在第一次请求时返回
    private List<TalentTimeImages> time_images;//时光图片

    public TalentHomeRequest() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                TalentHomeRequest talentHomeData;
                if (isSuccess) {
                    try {
                        talentHomeData = new Gson().fromJson(data, TalentHomeRequest.class);
                    } catch (Exception e) {
                        talentHomeData = null;
                    }
                } else {
                    talentHomeData = null;
                }

                if (callBack != null) {
                    callBack.onTHReqData(talentHomeData);
                }
            }
        });
    }


    public void getHomeData(int curpage, Member member, String talent_id) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(talent_id))
            params.addBodyParameter("talent_id", talent_id);
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }

        params.addBodyParameter("curpage", String.valueOf(curpage));

        execute(HttpRequest.HttpMethod.POST, BaseVar.URL_GET_TALENT_HOME, params);
    }


    private THReqModelCallBack callBack;

    public void setModelCallBack(THReqModelCallBack callBack) {
        this.callBack = callBack;
    }

    public interface THReqModelCallBack {
        void onTHReqData(TalentHomeRequest data);
    }

    /**
     * getter and setter
     */
    public TalentInfo getTalent_info() {
        return talent_info;
    }

    public List<TalentTimeImages> getTime_images() {
        return time_images;
    }

    public TalentShare getShare() {
        return share;
    }
}
