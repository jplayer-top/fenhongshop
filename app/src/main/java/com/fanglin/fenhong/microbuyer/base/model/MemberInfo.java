package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhui.datapicker.GenderPickDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 2015/9/7.
 */
public class MemberInfo extends APIUtil {

    public String member_id;// 会员id

    public String member_name;// 会员名
    public String member_email;// 会员邮箱
    public String member_truename;// 会员真实姓名
    public String member_nickname;// 会员昵称
    public String member_sex;// 会员性别
    public String member_birthday;// 会员生日
    public String member_mobile;// 会员手机号
    public String member_qq;// 会员qq
    public String member_wx;// 会员微信
    public String member_exppoints;// 会员经验
    public String member_points;// 会员积分
    private String member_avatar;// 会员头像


    public String token;// 登录令牌
    public String wap_key;// app里wap免登录令牌（是否需要待定）
    public String store_id;//店铺id (如果存在，则说明该会员是商家身份，前端务必禁止其购买自己的商品，并给予提示）
    public MemberAreainfo areainfo;

    public int nickname_status;//  修改昵称状态（1可修改  2不可修改）
    public int surplus_time;//        剩余修改昵称天数

    private int updateFiled;

    public String getMember_avatar() {
        return String.valueOf(BaseFunc.getMember_avatar(member_avatar));
    }

    public String getMember_avatar_S() {
        return member_avatar;
    }

    public void setMember_avatar(String member_avatar) {
        this.member_avatar = member_avatar;
    }

    public MemberInfo() {
        super();
        updateFiled = -1;
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    if (updateFiled == -1) {
                        MemberInfo info;
                        try {
                            info = new Gson().fromJson(data, MemberInfo.class);
                        } catch (Exception e) {
                            info = null;
                        }
                        if (info != null) {
                            member_avatar = info.member_avatar;
                            member_nickname = info.member_nickname;
                            if (mcb != null) mcb.onData(info);
                        }
                    } else {
                        if (mcb != null) mcb.onEnd(true, updateFiled);
                    }
                }
            }
        });
    }

    public String getGender() {
        if (TextUtils.equals("1", member_sex)) {
            return GenderPickDialog.getNameById(1);
        } else if (TextUtils.equals("2", member_sex)) {
            return GenderPickDialog.getNameById(2);
        } else {
            return GenderPickDialog.getNameById(0);
        }
    }

    public int getGenderInt() {
        if (BaseFunc.isInteger(member_sex)) {
            return Integer.valueOf(member_sex);
        }
        return 0;
    }

    /**
     * 获取会员资料(post)
     * mid 会员id
     * token 登录令牌
     */
    public void getMemberInfo() {
        updateFiled = -1;
        if (member_id == null || token == null) return;
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", member_id);
        params.addBodyParameter("token", token);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_MEMBERINFO, params);
    }

    /**
     * -1 表示不更新（获取数据）updateFiled
     * 0 nick
     * 1 avatar
     * 2 sex
     * 3 birthday
     */
    public void editMemberInfo(int updateFiled) {
        this.updateFiled = updateFiled;
        if (member_id == null || token == null) return;

        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", member_id);
        params.addBodyParameter("token", token);

        if (!TextUtils.isEmpty(member_nickname) && updateFiled == 0) {
            params.addBodyParameter("nickname", member_nickname);
        }

        if (!TextUtils.isEmpty(member_qq)) {
            params.addBodyParameter("qq", member_qq);
        }

        if (!TextUtils.isEmpty(member_wx)) {
            params.addBodyParameter("wx", member_wx);
        }

        if (!TextUtils.isEmpty(member_avatar)) {
            params.addBodyParameter("avatar", member_avatar);
        }

        if (!TextUtils.isEmpty(member_sex)) {
            params.addBodyParameter("sex", member_sex);
        }

        if (!TextUtils.isEmpty(member_birthday)) {
            params.addBodyParameter("birth", member_birthday);
        }

        if (areainfo != null) {
            params.addBodyParameter("provinceid", areainfo.provinceid);
            params.addBodyParameter("cityid", areainfo.cityid);
            params.addBodyParameter("areaid", areainfo.areaid);
            params.addBodyParameter("area_info", areainfo.area_info);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_EDIT_MEMBERINFO, params);
    }

    private MemberInfoCallBack mcb;

    public void setModelCallBack(MemberInfoCallBack cb) {
        this.mcb = cb;
    }

    public interface MemberInfoCallBack {
        void onData(MemberInfo m);

        void onEnd(boolean isSuccess, int updateFiled);
    }
}
