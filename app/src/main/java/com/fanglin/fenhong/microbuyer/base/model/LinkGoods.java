package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.event.LinkGoodsEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/16-下午1:14.
 * 功能描述;//时光关联的商品Model
 */
public class LinkGoods extends APIUtil {
    @Id
    private String goods_id;// 商品id
    private String goods_image;// 商品主图
    private String goods_name;//商品全称
    private double goods_price;//99.90, 商品价格
    private double goods_commission;//11.12, 商品奖金
    private String goods_origin;//国内, 产地
    private int bound;//0 已关联 （0：否 | 1：是）
    private int bound_count;//0 已关联数量 （所有商品都一样，随便取一个）
    private int time_image_id;//0, 绑定时光图片id (大于0说明是图片关联商品)

    private String time_image_id_local;//绑定图片本地的（暂未提交服务器）
    private String shortName;//商品简称

    public LinkGoods() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<LinkGoods> goodsList;
                if (isSuccess) {
                    try {
                        goodsList = new Gson().fromJson(data, new TypeToken<List<LinkGoods>>() {
                        }.getType());
                    } catch (Exception e) {
                        goodsList = null;
                    }
                } else {
                    goodsList = null;
                }

                if (callBack != null) {
                    callBack.onLinkGoodsList(goodsList);
                }
            }
        });
    }

    /**
     * 获取关联商品列表 (post)
     *
     * @param member  mid  token
     * @param key     搜索关键词 （可选，和 type是互斥的）
     * @param type    （key为空时 必传）[bought：已购买 | cart：购物车 | collected：已收藏 | browsed：浏览记录 | bound：已关联]
     * @param time_id （可选，修改时光时必传）
     *                <p>num 分页数量/每次请求数量</p>
     * @param curpage 当前页/第几次请求
     */
    public void getList(Member member, String key, String type, String time_id, int curpage) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));
        params.addBodyParameter("curpage", String.valueOf(curpage));

        if (!TextUtils.isEmpty(key)) {
            params.addBodyParameter("key", key);
        }
        if (!TextUtils.isEmpty(time_id)) {
            params.addBodyParameter("time_id", time_id);
        }
        if (!TextUtils.isEmpty(type)) {
            params.addBodyParameter("type", type);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_LINK_GOODS, params);
    }


    private LinkGoodsModelCallBack callBack;

    public void setModelCall(LinkGoodsModelCallBack callBack) {
        this.callBack = callBack;
    }

    public interface LinkGoodsModelCallBack {
        void onLinkGoodsList(List<LinkGoods> goodsList);
    }


    /**
     * getter & setter
     */
    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public double getGoods_price() {
        return goods_price;
    }

    public String getGoodsPrice() {
        return getGoods_price() + "";
    }

    public String getGoodsPriceDesc() {
        return "¥" + getGoods_price();
    }

    public void setGoods_price(double goods_price) {
        this.goods_price = goods_price;
    }

    public double getGoods_commission() {
        return goods_commission;
    }

    public void setGoods_commission(double goods_commission) {
        this.goods_commission = goods_commission;
    }

    public String getGoods_origin() {
        return goods_origin;
    }

    public void setGoods_origin(String goods_origin) {
        this.goods_origin = goods_origin;
    }

    public int getBound() {
        return bound;
    }

    public void setBound(int bound) {
        this.bound = bound;
    }

    public int getBound_count() {
        return bound_count;
    }

    public void setBound_count(int bound_count) {
        this.bound_count = bound_count;
    }

    public int getTime_image_id() {
        return time_image_id;
    }

    public void setTime_image_id(int time_image_id) {
        this.time_image_id = time_image_id;
    }

    public String getTime_image_id_local() {
        return time_image_id_local;
    }

    public void setTime_image_id_local(String time_image_id_local) {
        this.time_image_id_local = time_image_id_local;
    }

    /**
     * 是否已经关联了,如果是本地的需要读取数据库
     *
     * @return boolean
     */
    public boolean isBounded() {
        if (time_image_id > 0) {
            //如果是编辑绑定--从服务器读取的
            return bound == 1;
        } else {
            if (!TextUtils.isEmpty(time_image_id_local)) {
                return true;
            } else {
                //从数据库中读取
                LinkGoods aGoods = LinkGoodsEvent.getGoodsById(goods_id);
                return aGoods != null;
            }
        }
    }

    /**
     * 是否绑定本地图片
     *
     * @return boolean
     */
    public boolean isBindedImageOfLocal() {
        //从数据库中读取
        LinkGoods aGoods = LinkGoodsEvent.getGoodsById(goods_id);
        return aGoods != null && !TextUtils.isEmpty(aGoods.time_image_id_local);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
