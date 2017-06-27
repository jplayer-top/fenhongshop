package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/6-下午1:42.
 * 功能描述;//微店信息
 */
public class MicroshopInfo extends APIUtil {
    private String shop_id;//微店ID
    private String shop_name;//微店名称
    private String shop_scope;//微店标语
    private String shop_logo;//微店logo


    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_scope() {
        return shop_scope;
    }

    public void setShop_scope(String shop_scope) {
        this.shop_scope = shop_scope;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    /**
     * 网络请求部分
     */

    public MicroshopInfo() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                MicroshopInfo microshopInfo;
                if (isSuccess) {
                    try {
                        microshopInfo = new Gson().fromJson(data, MicroshopInfo.class);
                    } catch (Exception e) {
                        microshopInfo = null;
                    }
                } else {
                    microshopInfo = null;
                }
                if (callBack != null) {
                    callBack.onMSIData(microshopInfo);
                }
            }
        });
    }

    public interface MSIModelCallBack {
        /**
         * 微店信息回调
         */
        void onMSIData(MicroshopInfo microshopInfo);
    }

    private MSIModelCallBack callBack;

    public void setModelCallBack(MSIModelCallBack callBack) {
        this.callBack = callBack;
    }

    public void getData(String mid) {
        String url = BaseVar.API_GET_SHOP_INFO + "&mid=" + mid;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }
}
