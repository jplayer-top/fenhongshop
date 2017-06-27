package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/23-上午10:11.
 * 功能描述: 达人发现首页 数据请求接口
 */
public class TalentFindReq extends APIUtil {

    public TalentFindReq() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<TalentImagesDetail> times;
                if (isSuccess) {
                    try {
                        times = new Gson().fromJson(data, new TypeToken<List<TalentImagesDetail>>() {
                        }.getType());
                    } catch (Exception e) {
                        times = null;
                    }
                } else {
                    times = null;
                }
                if (callBack != null) {
                    callBack.onTalentFindList(times);
                }
            }
        });
    }

    /**
     * 获取发现首页信息 (get)
     *
     * @param member  mid 会员id token（可选）
     * @param curpage 当前页/第几次请求  num 分页数量/每次请求数量
     */
    public void getList(Member member, int curpage) {
        String url = BaseVar.API_GET_FOUND_HOME;
        if (member != null) {
            url += "&mid=" + member.member_id;
            url += "&token=" + member.token;
        }
        url += "&num=" + BaseVar.REQUESTNUM_10;
        url += "&curpage=" + curpage;

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    /**
     * callBack
     */
    public interface TalentFindReqCallBack {
        void onTalentFindList(List<TalentImagesDetail> times);
    }

    private TalentFindReqCallBack callBack;

    public void setModelCallBack(TalentFindReqCallBack callBack) {
        this.callBack = callBack;
    }
}
