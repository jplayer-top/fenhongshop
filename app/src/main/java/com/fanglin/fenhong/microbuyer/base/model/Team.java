package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 我的团队 list item
 */
public class Team {
    private String member_id;
    private String member_name;
    private String member_mobile;
    private String deep;// B 级团队  或  C 级团队
    private int level;// deep对应的级别 B=1 C=2
    private double deduct_money;// 用户的奖金金额
    private String member_nickname;//昵称
    private String member_avatar;//头像

    private int verify_status;//0未认证 1已认证

    public String getMember_nickname() {
        return member_nickname;
    }

    public void setMember_nickname(String member_nickname) {
        this.member_nickname = member_nickname;
    }

    public String getMember_avatar() {
        return member_avatar;
    }

    public void setMember_avatar(String member_avatar) {
        this.member_avatar = member_avatar;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_mobile() {
        return member_mobile;
    }

    public void setMember_mobile(String member_mobile) {
        this.member_mobile = member_mobile;
    }

    public double getDeduct_money() {
        return deduct_money;
    }

    public void setDeduct_money(double deduct_money) {
        this.deduct_money = deduct_money;
    }

    public String getDeep() {
        return deep;
    }

    public int getLevel() {
        return level;
    }

    public void setDeep(String deep) {
        this.deep = deep;
    }

    public int getVerify_status() {
        return verify_status;
    }

    //粉丝是否已经经过认证
    public boolean isVerified() {
        return verify_status == 1;
    }

    public String getVerifyDesc() {
        return verify_status == 1 ? "已认证" : "未认证";
    }
}
