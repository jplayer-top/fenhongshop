package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/13-上午11:53.
 * 功能描述;//达人时光评论
 */
public class TalentImageComment {
    private String comment_id;//2,
    private String comment_message;//很好哦很好哦,
    private String comment_member_id;//123,
    private String comment_member_name;//评论人名称
    private String comment_member_avatar;//评论人员头像
    private String up_count;//0, 点赞数
    private String comment_time;//1465191536评论时间
    private String comment_time_fmt;// 06-06 13:38
    private String is_owner;//是否为楼主
    private TalentImageComment parent;
    private String is_upped;//是否赞过

    public String getIs_upped() {
        return is_upped;
    }

    public void setIs_upped(String is_upped) {
        this.is_upped = is_upped;
    }

    //点赞加一次
    public void opZan(boolean isAdd) {
        if (BaseFunc.isInteger(up_count)) {
            int upC = Integer.valueOf(up_count) + (isAdd ? 1 : -1);
            up_count = String.valueOf(upC);
        }
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_message() {
        return comment_message;
    }

    public void setComment_message(String comment_message) {
        this.comment_message = comment_message;
    }

    public String getComment_member_id() {
        return comment_member_id;
    }

    public void setComment_member_id(String comment_member_id) {
        this.comment_member_id = comment_member_id;
    }

    public String getComment_member_name() {
        return comment_member_name;
    }

    public void setComment_member_name(String comment_member_name) {
        this.comment_member_name = comment_member_name;
    }

    public String getComment_member_avatar() {
        return comment_member_avatar;
    }

    public void setComment_member_avatar(String comment_member_avatar) {
        this.comment_member_avatar = comment_member_avatar;
    }

    public String getUp_count() {
        return up_count;
    }

    public void setUp_count(String up_count) {
        this.up_count = up_count;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getIs_owner() {
        return is_owner;
    }

    public boolean isLouzhu() {
        return TextUtils.equals("1", getIs_owner());
    }

    public void setIs_owner(String is_owner) {
        this.is_owner = is_owner;
    }

    public TalentImageComment getParent() {
        return parent;
    }

    public void setParent(TalentImageComment parent) {
        this.parent = parent;
    }

    public String getComment_time_fmt() {
        return comment_time_fmt;
    }

    public void setComment_time_fmt(String comment_time_fmt) {
        this.comment_time_fmt = comment_time_fmt;
    }

    public boolean isUpped() {
        return TextUtils.equals("1", getIs_upped());
    }
}
