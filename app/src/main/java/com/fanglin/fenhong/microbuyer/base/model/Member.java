package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fhui.datapicker.GenderPickDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.db.annotation.Id;

/**
 * 作者： Created by Plucky on 2015/9/7.
 */
public class Member {
    @Id
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

    public int talent_id;// 达人id

    public String token;// 登录令牌
    public String wap_key;// app里wap免登录令牌（是否需要待定）
    public String store_id;//店铺id (如果存在，则说明该会员是商家身份，前端务必禁止其购买自己的商品，并给予提示）
    public String shop_id;// 微店的id，如果是微店主则会返回
    public int if_shoper = 0;//是否是微店主（0 否 1是）

    public String getMember_avatar() {
        return String.valueOf(BaseFunc.getMember_avatar(member_avatar));
    }

    public void setMember_avatar(String member_avatar) {
        this.member_avatar = member_avatar;
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

    public String getString(){
        return new Gson().toJson(this);
    }
}
