package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 作者： Created by Plucky on 2015/10/29.
 * 分享成功之后回写数据
 */
public class ShareRecord extends APIUtil {
    public String title;//分享的标题
    public String content;//分享的内容
    public String img;//分享的图片（非必填，如果是商品的可以不填，团购等需要填写）
    public String rurl;//分享的url
    public Member member;

    public TalentShareFlag talentShareFlag;

    public ShareRecord() {
        super();
    }


    public void share() {
        /**
         * 通过判断达人或详情的分享标识来走不同的逻辑
         */
        if (talentShareFlag != null) {
            recordShare();
        } else {
            if (member == null) return;
            if (TextUtils.isEmpty(title)) return;
            if (TextUtils.isEmpty(content)) return;
            if (!BaseFunc.isValidUrl(rurl)) return;
            /** 图片可传可不传 */

            RequestParams params = new RequestParams();
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);

            params.addBodyParameter("title", title);
            params.addBodyParameter("content", content);
            params.addBodyParameter("rurl", rurl);
            if (!BaseFunc.isValidImgUrl(img)) {
                params.addBodyParameter("img", img);
            }

            execute(HttpRequest.HttpMethod.POST, BaseVar.API_SHARE_RECORD, params);
        }
    }


    /**
     * 在分享达人或者时光详情的时候，不管有没有登录均需要记录一次
     * 所以需要调用该接口 调用该接口时可以不用调用原始的share接口
     * Added By Plucky 2016-06-15 11:22
     * 与后台商量后如是说
     */
    private void recordShare() {
        if (talentShareFlag == null) return;
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", talentShareFlag.type);
        params.addBodyParameter("share_id", talentShareFlag.share_id);
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_TALENT_SHARE_RECORD, params);
    }


    /**
     * 生成vid
     */
    public static String calculateVid(Member m) {
        if (m == null) return null;
        return m.member_id + "@" + System.currentTimeMillis() / 1000;
    }


    /**
     * 获取分享类型
     */
    public static int calculateShareType(String platname) {
        int sharetype = 9;//其他渠道
        if (TextUtils.equals(QQ.NAME, platname)) {
            sharetype = 2;
        }

        if (TextUtils.equals(SinaWeibo.NAME, platname)) {
            sharetype = 3;
        }

        if (TextUtils.equals(WechatMoments.NAME, platname)) {
            sharetype = 4;
        }

        if (TextUtils.equals(Wechat.NAME, platname)) {
            sharetype = 5;
        }

        if (TextUtils.equals(WechatFavorite.NAME, platname)) {
            sharetype = 5;
        }

        if (TextUtils.equals(QZone.NAME, platname)) {
            sharetype = 6;
        }

        if (TextUtils.equals(Email.NAME, platname)) {
            sharetype = 8;
        }

        return sharetype;
    }
}
