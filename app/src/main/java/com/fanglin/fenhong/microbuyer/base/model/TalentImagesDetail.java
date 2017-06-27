package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhlib.other.FHLib;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/13-上午11:11.
 * 功能描述: 时光详情
 */
public class TalentImagesDetail extends APIUtil {
    private TalentInfo talent;
    private int is_own;// 0 是不是本人 0:否 | 1:是
    private String time_id;//时光id
    private long add_time;
    private String add_time_fmt;
    private String content;// 时光描述内容

    private List<TalentGoods> goods;// 关联的商品
    private List<TalentTagImage> images;// 达人发表的图片--带标签
    private List<TalentImageComment> comment;// 评论列表

    private List<String> tags;// 标签
    private String share_count;// "0", 分享数
    private String comment_count;// "6",评论数
    private String collect_count;// "1",收藏数
    private String count_text;//收藏数量
    private String count_text2;//预留字段 购买数量(后台暂时不做)

    private String is_collected;//0, 是否已收藏 0:否 | 1:是
    private TalentShare share;//分享信息

    public TalentImagesDetail() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                TalentImagesDetail detail;
                if (isSuccess) {
                    try {
                        detail = new Gson().fromJson(data, TalentImagesDetail.class);
                    } catch (Exception e) {
                        detail = null;
                    }
                } else {
                    detail = null;
                }

                if (callBack != null) {
                    callBack.onTIDModelData(detail);
                }
            }
        });
    }

    public void getData(String time_id, int curpage, Member member) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("time_id", time_id);
        params.addBodyParameter("curpage", String.valueOf(curpage));
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM_10));
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_TIME_DETAIL, params);
    }

    private TIDModelCallBack callBack;

    public void setModelCallBack(TIDModelCallBack callBack) {
        this.callBack = callBack;
    }

    public interface TIDModelCallBack {
        void onTIDModelData(TalentImagesDetail detail);
    }

    /**
     * getter
     */


    public TalentInfo getTalent() {
        return talent;
    }

    public String getTalentMemberId() {
        TalentInfo info = getTalent();
        //info.getTalent_id() 应该是member_id
        if (info != null) return info.getMember_id();
        return null;
    }

    public String getTalentId(){
        TalentInfo info = getTalent();
        if (info != null) return info.getTalent_id();
        return null;
    }

    public long getAdd_time() {
        return add_time;
    }

    public String getAdd_time_fmt() {
        return add_time_fmt;
    }

    public String getContent() {
        return content;
    }

    public List<TalentGoods> getGoods() {
        return goods;
    }

    public List<TalentTagImage> getImages() {
        return images;
    }

    public List<TalentImageComment> getComment() {
        return comment;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getShare_count() {
        return share_count;
    }

    public String getShareCountDesc() {
        if (TextUtils.equals("0", share_count)) {
            return SHARE;
        }
        return share_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public String getCommentCountDesc() {
        if (TextUtils.equals("0", comment_count)) {
            return COMMENT;
        }
        return comment_count;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public String getCollectCountDesc() {
        if (TextUtils.equals("0", collect_count)) {
            return COLLECT;
        }
        return collect_count;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public boolean isCollected() {
        return TextUtils.equals("1", is_collected);
    }

    public static final String COLLECT = "收藏";
    public static final String COMMENT = "评论";
    public static final String SHARE = "分享";

    public void addCollect() {
        if (TextUtils.equals(COLLECT, collect_count)) {
            this.collect_count = "1";
        } else {
            if (BaseFunc.isInteger(collect_count)) {
                int c = Integer.valueOf(collect_count);
                this.collect_count = (c + 1) + "";
            }
        }
    }

    public void minusCollect() {
        if (BaseFunc.isInteger(collect_count)) {
            int c = Integer.valueOf(collect_count);
            c = c - 1;
            this.collect_count = c <= 0 ? COLLECT : c + "";
        }
    }

    public boolean hasCollect() {
        return TextUtils.equals("1", getIs_collected());
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }

    public TalentShare getShare() {
        return share;
    }

    public void setShare(TalentShare share) {
        this.share = share;
    }

    public String gettMonth() {
        return FHLib.formatTimestamp(add_time, "MM");
    }

    public String gettYear() {
        return FHLib.formatTimestamp(add_time, "yyyy");
    }

    public String gettDay() {
        return FHLib.formatTimestamp(add_time, "dd");
    }

    public class TalentGoods {
        private String goods_id;// 商品id
        private String goods_name;//xxx 商品名称
        private String goods_price;//¥298.00 价格
        private String goods_origin;//韩国 产地
        private String goods_image;//...

        public String getGoods_id() {
            return goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public String getGoods_origin() {
            return goods_origin;
        }

        public String getGoods_image() {
            return goods_image;
        }
    }

    public String getCount_text() {
        return count_text;
    }

    public String getTalentTimesTitle() {
        if (talent != null) {
            return talent.getTalent_name() + "·时光";
        }
        return "达人·时光";
    }

    public String getTime_id() {
        return time_id;
    }

    public int getIs_own() {
        return is_own;
    }

    public String getCount_text2() {
        return count_text2;
    }
}
