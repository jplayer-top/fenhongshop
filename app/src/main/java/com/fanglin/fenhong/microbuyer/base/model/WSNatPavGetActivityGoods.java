package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 国家馆 商品列表 请求webservice
 * Created by lizhixin on 2015/11/30.
 */
public class WSNatPavGetActivityGoods extends APIUtil {

    private WSGetActivityGoodsCallBack mcb;

    public WSNatPavGetActivityGoods() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        Type type = new TypeToken<ArrayList<NationalPavGoodsEntity>>(){}.getType();
                        ArrayList<NationalPavGoodsEntity> activity_goods = new Gson().fromJson(data, type);
                        if (mcb != null) mcb.onWSGetActivityGoodsSuccess(activity_goods);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSGetActivityGoodsError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSGetActivityGoodsError(data);
                }
            }
        });
    }

    public void getActivityGoods (String activity_id, String class_id, int num, int curpage, int sort, int order) {

        StringBuilder builder = new StringBuilder();
        builder.append(BaseVar.API_GET_ACTIVITY_GOODS);
        builder.append("&activity_id=" + activity_id);
        builder.append("&class_id=" + class_id);//活动商品分类id （传0或不传 取全部商品）
        builder.append("&num=" + String.valueOf(num));
        builder.append("&curpage=" + String.valueOf(curpage));
        builder.append("&sort=" + String.valueOf(sort));//排序类型 1:综合 | 2:销量 | 3:价格 | 4:人气
        builder.append("&order=" + String.valueOf(order));//排序方式 1:升序 | 2:降序

        execute (HttpRequest.HttpMethod.GET, builder.toString(), null);
    }

    public void setWSGetActivityGoodsCallBack(WSGetActivityGoodsCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSGetActivityGoodsCallBack {
        void onWSGetActivityGoodsError(String errcode);

        void onWSGetActivityGoodsSuccess (ArrayList<NationalPavGoodsEntity> activity_goods);
    }

}
