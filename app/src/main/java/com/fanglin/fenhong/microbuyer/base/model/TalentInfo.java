package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/8-下午5:21.
 * 功能描述: 达人信息
 */
public class TalentInfo {
    private String is_followed;// 0:否 | 1:是
    private int is_own;// 1, 是否为本人 "0":否 | "1":是
    private String member_id;// 1283330, 达人会员id
    private String talent_id;// 1, 达人id
    private String talent_name;// 一页书, 达人名称
    private String talent_avatar;//达人头像
    private String talent_intro;// 清风不识字，何故乱翻书, 简介
    private String talent_background;//背景图片
    private String fans_count;// 粉丝数量
    private String time_count;// 1490.0万, 时光数量
    private String goods_count;// 14.9亿 推荐商品数量
    private String talent_type;//达人身份

    //附加字段
    private String shop_id;//微店id

    public String getTalent_type() {
        return talent_type;
    }

    public int getIs_own() {
        return is_own;
    }

    public void setIs_own(int is_own) {
        this.is_own = is_own;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(String is_followed) {
        this.is_followed = is_followed;
    }

    public void setTalent_type(String talent_type) {
        this.talent_type = talent_type;
    }

    public boolean hasFollowed() {
        return TextUtils.equals("1", getIs_followed());
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTalent_id() {
        return talent_id;
    }

    public void setTalent_id(String talent_id) {
        this.talent_id = talent_id;
    }

    public String getTalent_name() {
        return talent_name;
    }

    public void setTalent_name(String talent_name) {
        this.talent_name = talent_name;
    }

    public String getTalent_avatar() {
        return talent_avatar;
    }

    public void setTalent_avatar(String talent_avatar) {
        this.talent_avatar = talent_avatar;
    }

    public String getTalent_intro() {
        return talent_intro;
    }

    public void setTalent_intro(String talent_intro) {
        this.talent_intro = talent_intro;
    }

    public String getTalent_background() {
        return talent_background;
    }

    public void setTalent_background(String talent_background) {
        this.talent_background = talent_background;
    }

    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public String getTime_count() {
        return time_count;
    }

    public void setTime_count(String time_count) {
        this.time_count = time_count;
    }

    public String getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(String goods_count) {
        this.goods_count = goods_count;
    }

    public String getTalentGoodsTitle(){
        return talent_name+"爱的·商品";
    }
}

