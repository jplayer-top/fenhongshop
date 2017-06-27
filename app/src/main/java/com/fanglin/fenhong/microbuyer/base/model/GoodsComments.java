package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/14.
 */
public class GoodsComments {
    public String id;//   评论id
    public float stars;//  评论星级
    public String content;// 评论内容
    public String is_anonymous;//  是否匿名 （0  不匿名  1 匿名）
    public String date;//   评论日期   "2015-02-03"
    public String member_id;//  会员id
    public String member_name;//  会员名
    public String member_avatar;
    public String member_nickname;// 会员昵称
    public String images;// 评论图片 （多张返回用逗号隔开的字符串）
    public String seller_explain;//   商家解释
    public String is_append;//    是否为追加评价
    public int evaluate_days;//    收货后第几天评价 1代表当天 2代表第二天 (一般在追加评价里有)
    public List<GoodsComments> append_evaluate;// 追加评价

    private String getMaskName(String membername) {
        if (!TextUtils.isEmpty(membername)) {
            char pre = membername.charAt(0);
            char after = membername.charAt(membername.length() - 1);
            return pre + "***" + after;
        } else {
            return "m***";
        }
    }

    public String getCommentUser() {
        if (TextUtils.equals(is_anonymous, "1")) {
            return getMaskName(member_name);
        } else {
            if (!TextUtils.isEmpty(member_nickname)) {
                return member_nickname;
            } else {
                return member_name;
            }
        }
    }
}
