package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-16.
 * modify by lizhixin on 2015/12/22
 */
public class Favorites extends BaseGoods {
    //type = goods

    //type = shop
    public String store_id;// 店铺id
    public String store_name;// 店铺名称
    public String store_avatar;// 店铺头像
    //public String store_domain;// 店铺二级域名
    public float store_credit;// 店铺信用
    public List<FavGoods> commend_goods;// 推荐商品（object）与 type = goods 结构一致

    //type = micoshop
    public String shop_id;// 微店id
    public String shop_name;// 微店名称
    public String shop_logo;// 微店logo
    public int collect_count;// 微店收藏数量

    //    type = time
    public String time_id;//时光id
    public String time_talent_id;//达人id
    public String member_id;
    public String time_image;// 时光主图
    public String time_content;// 时光文字
    public String time_talent_name;// 达人昵称
    public String time_talent_avatar;// 达人头像

    //    type = talent
    public String talent_id;// 达人id
    public String talent_avatar;// 达人头像
    public String talent_name;// 达人昵称
    public String time_count;// 达人时光数量

    //    type = brand
    public String brand_id;// "745",
    public String brand_name;// "城野医生",
    public String brand_pic;// "05150814051456966_small.jpg",
    public String brand_collect;// "2",
    public String brand_temp_describe;// "3万 关注"

    public class FavGoods {
        public String goods_id;    // 商品id
        public String goods_name;  // 商品名称
        public String goods_image; // 商品图片
        public String goods_price; // 商品价格

        public String getPriceDesc() {
            return "¥" + goods_price;
        }
    }

    /**
     * 0 添加收藏 1 取消收藏 2 获取列表
     */
    public int actionNum = 0;

    public boolean isSelected = false;

    public float getStar() {
        return (float) (store_credit * 0.01 * 5);
    }

    public Favorites() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {
                if (mcb != null) mcb.onFavStart();
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    switch (actionNum) {
                        case 0:
                            if (mcb != null) mcb.onFavError("0");
                            break;
                        case 1:
                            if (mcb != null) mcb.onFavError("0");
                            break;
                        case 2:
                            try {
                                List<Favorites> list = new Gson().fromJson(data, new TypeToken<List<Favorites>>() {
                                }.getType());
                                if (mcb != null) mcb.onFavList(list);
                            } catch (Exception e) {
                                if (mcb != null) mcb.onFavError("-1");
                            }
                            break;
                    }
                } else {
                    if (mcb != null) mcb.onFavError(data);
                }
            }
        });
    }


    /**
     * 添加收藏(post)
     * token 登录令牌
     * mid 会员id
     * fid 收藏对象id（商品：goods_id | 店铺：store_id | 微店：shop_id|时光：time_id）
     * type 收藏类型（商品：goods | 店铺：shop | 微店：microshop| 时光：time）
     */
    public void add_favorites(Context c, Member member, String fid, String type) {
        if (member == null) {
            if (mcb != null) mcb.onFavError("-1");
            BaseFunc.gotoLogin(c);
            return;
        }
        if (fid == null || type == null) {
            if (mcb != null) mcb.onFavError("-1");
            return;
        }

        actionNum = 0;

        RequestParams params = new RequestParams();

        params.addBodyParameter("token", member.token);
        params.addBodyParameter("mid", member.member_id);
        params.addBodyParameter("fid", fid);
        params.addBodyParameter("type", type);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_FAVORITES, params);
    }

    /**
     * 删除收藏(post)
     * token 登录令牌
     * mid 会员id
     * fid 收藏对象id（商品：goods_id | 店铺：store_id | 微店：shop_id  删除单条记录 "id" 删除多条记录 "id1,id2,id3"）
     * type 收藏类型（商品：goods | 店铺：shop | 微店：microshop）
     */
    public void delete_favorites(Context c, Member member, String fid, String type) {
        if (member == null) {
            if (mcb != null) mcb.onFavError("-1");
            BaseFunc.gotoLogin(c);
            return;
        }
        if (fid == null || type == null) {
            if (mcb != null) mcb.onFavError("-1");
            return;
        }

        actionNum = 1;

        RequestParams params = new RequestParams();

        params.addBodyParameter("token", member.token);
        params.addBodyParameter("mid", member.member_id);
        params.addBodyParameter("fid", fid);
        params.addBodyParameter("type", type);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DELETE_FAVORITES, params);
    }


    /**
     * 获取收藏列表(post)
     * token 登录令牌
     * mid 会员id
     * type 收藏类型（商品：goods | 店铺：shop | 微店：microshop）
     * num 分页数量/每次请求数量
     * curpage 当前页/第几次请求
     */
    public void get_favorites_list(Context c, Member member, String type, int curpage) {
        if (member == null) {
            if (mcb != null) mcb.onFavError("-1");
            BaseFunc.gotoLogin(c);
            return;
        }

        if (type == null) {
            if (mcb != null) mcb.onFavError("-1");
            return;
        }

        actionNum = 2;

        RequestParams params = new RequestParams();

        params.addBodyParameter("token", member.token);
        params.addBodyParameter("mid", member.member_id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_FAVORITES, params);
    }

    /**
     * 获取浏览历史(post)
     * token 登录令牌
     * mid 会员id
     * num 分页数量/每次请求数量
     * curpage 当前页/第几次请求
     */
    public void get_browse_list(Context c, Member member, int curpage) {
        if (member == null) {
            if (mcb != null) mcb.onFavError("-1");
            BaseFunc.gotoLogin(c);
            return;
        }

        actionNum = 2;

        RequestParams params = new RequestParams();

        params.addBodyParameter("token", member.token);
        params.addBodyParameter("mid", member.member_id);
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_BROWSELIST, params);
    }


    private FavModelCallBack mcb;

    public void setModelCallBack(FavModelCallBack cb) {
        this.mcb = cb;
    }

    public interface FavModelCallBack {
        void onFavError(String errcode);//error =0  表示成功

        void onFavList(List<Favorites> list);

        void onFavStart();
    }

}
