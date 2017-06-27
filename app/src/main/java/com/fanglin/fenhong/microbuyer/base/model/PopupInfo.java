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
 * author:Created by Plucky on 16/9/23-上午10:26.
 * 功能描述: API返回弹窗内容
 */
public class PopupInfo extends APIUtil {
    private String title;//弹窗title， 比如“恭喜您有机会成为分享达人”
    private String desc;//ios需要返回（description为关键值） 值=description的值
    private String description;// 弹窗描述，  “成为分享达人，分享商品给好友，好友购买后就能获得奖励！”
    private String link_title;//弹窗链接文本 比如“我已阅读并同意《推广合作协议》”
    private String link_url;//点击跳转url
    private List<PopUpButton> button;

    public List<PopUpButton> getButton() {
        return button;
    }

    public String getDescription() {
        if (!TextUtils.isEmpty(desc)) {
            return desc;
        } else {
            return description;
        }
    }

    public String getLinkTitle() {
        return link_title;
    }

    public String getLinkTitleDesc() {
        return " " + link_title;
    }

    public String getLink_url() {
        return link_url;
    }

    public String getTitle() {
        return title;
    }

    public PopupInfo() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                PopupInfo info;
                if (isSuccess) {
                    try {
                        info = new Gson().fromJson(data, PopupInfo.class);
                    } catch (Exception e) {
                        info = null;
                    }
                } else {
                    info = null;
                }

                if (mCallBack != null) {
                    mCallBack.onPopupInfo(info);
                }
            }
        });
    }

    public void getData(Member member, int type) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("type", String.valueOf(type));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_POPUP_INFO, params);
    }


    public interface PopupInfoModelCallBack {
        void onPopupInfo(PopupInfo data);
    }

    private PopupInfoModelCallBack mCallBack;

    public void setModelCallBack(PopupInfoModelCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public class PopUpButton {
        private String button_name;
        private String button_url;

        public String getButton_name() {
            return button_name;
        }

        public String getButton_url() {
            return button_url;
        }
    }
}
