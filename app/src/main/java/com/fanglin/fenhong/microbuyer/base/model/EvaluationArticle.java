package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;


/**
 * Author: Created by lizhixin on 2016/2/29 20:30.
 */
public class EvaluationArticle extends APIUtil {

    public String article_content;

    private ArticleModelCallBack callBack;

    public void setCallBack(ArticleModelCallBack callBack) {
        this.callBack = callBack;
    }

    public EvaluationArticle() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                try {
                    if (isSuccess) {
                        EvaluationArticle entity = new Gson().fromJson(data, new TypeToken<EvaluationArticle>(){}.getType());
                        if (callBack != null) {
                            callBack.onDataSuccess(entity);
                        }
                    } else {
                        callBack.onDataError("暂无积分说明");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onDataError("解析积分说明数据失败");
                }
            }
        });
    }

    /*获取评价 积分说明*/
    public void getEvaluateExplain () {
        execute (HttpRequest.HttpMethod.GET, BaseVar.WAP_EVALUATE_POINT_RULES, null);
    }

    public interface ArticleModelCallBack {
        void onDataSuccess (EvaluationArticle entity);

        void onDataError (String errcode);
    }

}
