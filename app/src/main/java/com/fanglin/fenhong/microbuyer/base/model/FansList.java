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
 * author:Created by Plucky on 16/6/21-下午4:43.
 * 功能描述:
 */
public class FansList extends APIUtil {
    private String follower_mid;//328386,  粉丝会员id
    private String follower_name;//py, 用户名
    private String follower_avatar;//http://q.qlogo.cn/qqapp/1104541562/13565F0571F4A9250E19D9EA54A8F76F/100, 头像
    private String follow_time;//1464844282, 关注时间
    private int talent_id;//0 达人id （大于0 说明是达人）
    private String talent_type;//

    public FansList() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<FansList> theList;
                if (isSuccess) {
                    try {
                        theList = new Gson().fromJson(data, new TypeToken<List<FansList>>() {
                        }.getType());
                    } catch (Exception e) {
                        theList = null;
                    }
                } else {
                    theList = null;
                }

                if (callBack != null) {
                    callBack.onFansList(theList);
                }
            }
        });
    }


    /**
     * 获取粉丝列表 (get)
     *
     * @param talent_id talent_id 达人id
     * @param curpage   当前页/第几次请求 num 分页数量/每次请求数量
     */
    public void getList(String talent_id, int curpage) {
        String url = BaseVar.API_GET_FANS_LIST;
        url += "&talent_id=" + talent_id;
        url += "&num=" + String.valueOf(BaseVar.REQUESTNUM);
        url += "&curpage=" + curpage;

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private FansListCallBack callBack;

    public void setModelCallBack(FansListCallBack callBack) {
        this.callBack = callBack;
    }

    public interface FansListCallBack {
        void onFansList(List<FansList> theList);
    }

    /**
     * getter&setter
     **/
    public String getFollower_mid() {
        return follower_mid;
    }

    public void setFollower_mid(String follower_mid) {
        this.follower_mid = follower_mid;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    public String getFollower_avatar() {
        return follower_avatar;
    }

    public void setFollower_avatar(String follower_avatar) {
        this.follower_avatar = follower_avatar;
    }

    public String getFollow_time() {
        return follow_time;
    }

    public void setFollow_time(String follow_time) {
        this.follow_time = follow_time;
    }

    public int getTalent_id() {
        return talent_id;
    }

    public void setTalent_id(int talent_id) {
        this.talent_id = talent_id;
    }

    public String getTalent_type() {
        return talent_type;
    }
}
