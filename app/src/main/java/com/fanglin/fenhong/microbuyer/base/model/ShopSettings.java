package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/28-下午2:02.
 * 功能描述: 获取微店设置
 * 发现页面广告位
 */
public class ShopSettings extends APIUtil {
    private String shop_banner_pic;//    微店顶部横幅图片
    private String shop_banner_url;//    微店顶部横幅链接

    public ShopSettings() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                ShopSettings shopSettings;
                if (isSuccess) {
                    try {
                        shopSettings = new Gson().fromJson(data, ShopSettings.class);
                    } catch (Exception e) {
                        shopSettings = null;
                    }
                } else {
                    shopSettings = null;
                }

                if (callBack != null) {
                    callBack.onShopSettingData(shopSettings);
                }
            }
        });
    }

    public void getData(Member member) {
        String aurl = BaseVar.API_GET_SHOP_SETTINGS;
        if (member != null) {
            aurl += "&mid=" + member.member_id;
            aurl += "&token=" + member.token;
        }
        execute(HttpRequest.HttpMethod.GET, aurl, null);
    }

    /**
     * getter
     */
    public String getShop_banner_pic() {
        return shop_banner_pic;
    }

    public String getShop_banner_url() {
        return shop_banner_url;
    }

    private ShopSettingCallBack callBack;

    public void setModelCallBack(ShopSettingCallBack callBack) {
        this.callBack = callBack;
    }

    public interface ShopSettingCallBack {
        void onShopSettingData(ShopSettings settings);
    }
}
