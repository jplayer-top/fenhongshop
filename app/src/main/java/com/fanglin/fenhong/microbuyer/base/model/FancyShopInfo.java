package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 微店信息
 * Created by Plucky on 2015/10/13.
 */
public class FancyShopInfo extends APIUtil {
    public String shop_id;//微店id
    public String shop_name;//微店名称,
    public String shop_scope;// 微店介绍语,
    public String shop_banner;//微店店招,
    public String shop_logo;//微店logo,
    public String member_id;// 微店所属会员的会员id（精品微店为0，其它类型微店需根据此id来判断用户是否正在浏览自己的微店，如果是则隐藏掉收藏按钮）
    public int if_collected = 0;
    public List<FancyShopCls> goods_classes;// 微店商品分类

    public FancyShopInfo() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        FancyShopInfo info = new Gson().fromJson(data, FancyShopInfo.class);
                        if (mcb != null) mcb.onFSIData(info);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onFSIError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onFSIError(data);
                }
            }
        });
    }

    /**
     * shop_id 微店id
     * mid
     */
    public void getData(String shop_id, String mid) {
        if (shop_id == null) {
            if (mcb != null) mcb.onFSIError("-1");
            return;
        }
        String url = BaseVar.API_GET_MSHOP_INFO;
        url += "&shop_id=" + shop_id;
        if (mid != null) {
            url += "&mid=" + mid;
        }
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private FSIModelCallBack mcb;

    public void setModelCallBack(FSIModelCallBack cb) {
        this.mcb = cb;
    }

    public interface FSIModelCallBack {
        void onFSIError(String errcode);

        void onFSIData(FancyShopInfo info);
    }
}
