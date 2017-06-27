package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/8-下午1:24.
 * 功能描述;//极速免税 个人中心
 */
public class DutyfreeUCenter extends APIUtil {

    private String rank_img;//头衔图片 尺寸2：1,
    private String account;//m15105421856,
    private String account_money;//余额:50000元,
    private String invite_num;//10,
    private String invite_money;//500.00,
    private String extra_money;//300.00

    private String account_desc;//了解会员特权/2016-12-28到期,
    private String account_url;//普通买手? 了解特权的页面 ;//VIP会员详情页,
    private String button_label;//开通,
    private String account_days;//会员天数,

    private int unpaied_count;//待付款数量,
    private int unsent_count;//待发货数量,
    private int unreceived_count;//待收货数量
    private String read_count;//消息未读数
    private String invite_url;//邀请链接

    public DutyfreeUCenter() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (uCenterListener != null) {
                    if (isSuccess) {
                        DutyfreeUCenter uCenter;
                        try {
                            uCenter = new Gson().fromJson(data, DutyfreeUCenter.class);
                        } catch (Exception e) {
                            uCenter = null;
                        }
                        uCenterListener.onDutyfreeUCenterData(uCenter);
                    } else {
                        uCenterListener.onDutyfreeUCenterData(null);
                    }
                }
            }
        });
    }

    public void getData(Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_UCENTER, params);
    }

    private DutyfreeUCenterListener uCenterListener;

    public void setuCenterListener(DutyfreeUCenterListener uCenterListener) {
        this.uCenterListener = uCenterListener;
    }

    public interface DutyfreeUCenterListener {
        void onDutyfreeUCenterData(DutyfreeUCenter data);
    }

    public String getAccount() {
        return account;
    }

    public String getAccount_money() {
        return account_money;
    }

    public String getExtra_money() {
        return extra_money;
    }

    public String getInvite_money() {
        return invite_money;
    }

    public String getInvite_num() {
        return invite_num;
    }

    public String getRank_img() {
        return rank_img;
    }

    public String getAccount_desc() {
        return account_desc;
    }

    public String getAccount_url() {
        return account_url;
    }

    public String getButton_label() {
        return button_label;
    }

    public String getAccount_days() {
        return account_days;
    }

    public String getUnpaiedCountDesc() {
        if (unpaied_count > 99) {
            return "99+";
        } else {
            return String.valueOf(unpaied_count);
        }
    }

    public String getUnsentCountDesc() {
        if (unsent_count > 99) {
            return "99+";
        } else {
            return String.valueOf(unsent_count);
        }
    }

    public String getUnreceivedCountDesc() {
        if (unreceived_count > 99) {
            return "99+";
        } else {
            return String.valueOf(unreceived_count);
        }
    }

    public String getInvite_url() {
        return invite_url;
    }

    public void setInvite_url(String invite_url) {
        this.invite_url = invite_url;
    }

    public String getRead_count() {
        return read_count;
    }

    public void setRead_count(String read_count) {
        this.read_count = read_count;
    }
}
