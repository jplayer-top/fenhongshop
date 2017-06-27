package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/13-上午11:24.
 * 功能描述: 搜索自动联想功能
 */
public class FHAutoComplete extends APIUtil {
    private String id;
    private String name;
    private String url;//点击事件
    private String key;//搜索词汇
    private long addtime;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public FHAutoComplete() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<FHAutoComplete> list;
                if (isSuccess) {
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<FHAutoComplete>>() {
                        }.getType());
                    } catch (Exception e) {
                        list = null;
                    }
                } else {
                    list = null;
                }

                if (callBack != null) {
                    callBack.onFHAutoList(list);
                }
            }
        });
    }

    public void getList() {
        String url = "http://42.96.171.11/fh_app/php/search.php?random=1";
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private FHAutoCompleteModelCallBack callBack;

    public void setCallBack(FHAutoCompleteModelCallBack callBack) {
        this.callBack = callBack;
    }

    public interface FHAutoCompleteModelCallBack {
        void onFHAutoList(List<FHAutoComplete> list);
    }
}
