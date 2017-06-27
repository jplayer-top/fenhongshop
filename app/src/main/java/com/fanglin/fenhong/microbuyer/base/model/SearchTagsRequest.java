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
 * author:Created by Plucky on 16/6/15-下午4:04.
 * 功能描述: 搜索标签
 */
public class SearchTagsRequest extends APIUtil {
    private String exists;//是否存在 0:否 | 1:是
    private List<String> tags;

    public SearchTagsRequest() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                SearchTagsRequest requestData;
                if (isSuccess) {
                    try {
                        requestData = new Gson().fromJson(data, SearchTagsRequest.class);
                    } catch (Exception e) {
                        requestData = null;
                    }
                } else {
                    requestData = null;
                }

                if (callBack != null) {
                    callBack.onSTReqData(requestData);
                }
            }
        });
    }

    /**
     * 搜索时光标签 (post)
     *
     * @param member mid 会员id token 会员登录token
     * @param key    搜索关键词
     *               <p/>
     *               num 标签显示数量 （可选，默认：10）
     */
    public void getData(Member member, String key) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("key", key);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_SEARCH_TAGS, params);
    }

    private STReqModelCallBack callBack;

    public void setModelCallBack(STReqModelCallBack callBack) {
        this.callBack = callBack;
    }

    public interface STReqModelCallBack {
        void onSTReqData(SearchTagsRequest reqData);
    }

    /**
     * getter & setter
     */

    public boolean isExists() {
        return TextUtils.equals("1", getExists());
    }

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
