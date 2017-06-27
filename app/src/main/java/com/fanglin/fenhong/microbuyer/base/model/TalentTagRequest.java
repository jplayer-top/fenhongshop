package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/15-下午1:55.
 * 功能描述: 请求达人标签
 */
public class TalentTagRequest extends APIUtil {
    private List<String> hot_tags;//热门标签
    private List<String> history_tags;//最近选择

    public TalentTagRequest() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                TalentTagRequest talentTagRequest;
                if (isSuccess) {
                    try {
                        talentTagRequest = new Gson().fromJson(data, TalentTagRequest.class);
                    } catch (Exception e) {
                        talentTagRequest = null;
                    }
                } else {
                    talentTagRequest = null;
                }

                if (callBack != null) {
                    callBack.onTTReqData(talentTagRequest);
                }
            }
        });
    }


    /**
     * 获取时光标签 (post)
     *
     * @param member mid 会员id token 登录token
     */
    public void getTags(Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_TAGS, params);
    }

    private TTReqModelCallBack callBack;

    public void setModelCallBack(TTReqModelCallBack callBack) {
        this.callBack = callBack;
    }

    public interface TTReqModelCallBack {
        void onTTReqData(TalentTagRequest tagRequest);
    }

    /**
     * getter and setter
     */
    public List<String> getHot_tags() {
        return hot_tags;
    }

    public void setHot_tags(List<String> hot_tags) {
        this.hot_tags = hot_tags;
    }

    public List<String> getHistory_tags() {
        return history_tags;
    }

    public void setHistory_tags(List<String> history_tags) {
        this.history_tags = history_tags;
    }
}
