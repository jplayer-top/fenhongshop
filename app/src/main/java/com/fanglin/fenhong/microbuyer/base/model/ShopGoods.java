package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/3-下午4:36.
 * 功能描述;//微店商品
 */
public class ShopGoods extends APIUtil {
    private String goods_id;//商品ID,
    private String goods_name;//商品名,
    private String goods_desc;//商品描述,
    private String goods_image;//商品主图,
    private double goods_price;//商品价格,
    private double goods_commission;//商品奖金,
    private String goods_storage;//库存数,
    private String goods_click;//浏览数,
    private String goods_collect;//收藏数,
    private String goods_salenum;//销量数,
    private String goods_shares;//3,4,5 已经分享过的渠道标识 （分享类型 1：内部动态 2：qq 3：新浪微博 4：微信朋友圈 5：微信好友 6：qq空间 7:人人网 8：短信  9：其它）,
    private String nation_flag;//国旗图片,
    private String nation_name;// 国家名称,
    private String goods_promise;//保证标语

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public double getGoods_price() {
        return goods_price;
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

    public String getGoods_storage() {
        return goods_storage;
    }

    public void setGoods_storage(String goods_storage) {
        this.goods_storage = goods_storage;
    }

    public String getGoods_click() {
        return goods_click;
    }

    public void setGoods_click(String goods_click) {
        this.goods_click = goods_click;
    }

    public String getGoods_collect() {
        return goods_collect;
    }

    public void setGoods_collect(String goods_collect) {
        this.goods_collect = goods_collect;
    }

    public String getGoods_salenum() {
        return goods_salenum;
    }

    public void setGoods_salenum(String goods_salenum) {
        this.goods_salenum = goods_salenum;
    }

    public String getGoods_shares() {
        return goods_shares;
    }

    public void setGoods_shares(String goods_shares) {
        this.goods_shares = goods_shares;
    }

    public String getNation_flag() {
        return nation_flag;
    }

    public void setNation_flag(String nation_flag) {
        this.nation_flag = nation_flag;
    }

    public String getNation_name() {
        return nation_name;
    }

    public void setNation_name(String nation_name) {
        this.nation_name = nation_name;
    }

    public String getGoods_promise() {
        return goods_promise;
    }

    public void setGoods_promise(String goods_promise) {
        this.goods_promise = goods_promise;
    }

    public enum GSHARE {
        WECHAT,
        MOMENTS,
        QZONE,
        QQ,
        WEIBO
    }

    private String getGShareCode(GSHARE gshare) {
        if (gshare == GSHARE.WECHAT) {
            return "5";
        } else if (gshare == GSHARE.MOMENTS) {
            return "4";
        } else if (gshare == GSHARE.QZONE) {
            return "6";
        } else if (gshare == GSHARE.QQ) {
            return "2";
        } else if (gshare == GSHARE.WEIBO) {
            return "3";
        } else {
            return "9";
        }
    }

    /**
     * 判断是否通过某渠道分享
     *
     * @param goodsShareType GSHARE
     * @return true false
     */
    public boolean isSelected(GSHARE goodsShareType) {
        if (TextUtils.isEmpty(getGoods_shares())) return false;
        return getGoods_shares().contains(getGShareCode(goodsShareType));
    }

    /**
     * 请求相关的功能
     */

    public ShopGoods() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<ShopGoods> list;
                if (isSuccess) {
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<ShopGoods>>() {
                        }.getType());
                    } catch (Exception e) {
                        list = null;
                    }
                } else {
                    list = null;
                }
                if (callBack != null) {
                    callBack.onSGList(list);
                }
            }
        });
    }


    public interface SGModelCallBack {
        /**
         * 分享过的商品列表
         *
         * @param goodsList 如果为null则无数据
         */
        void onSGList(List<ShopGoods> goodsList);
    }

    private SGModelCallBack callBack;

    public void setModelCallBack(SGModelCallBack callBack) {
        this.callBack = callBack;
    }

    public void getList(String mid, int curpage) {
        String url = BaseVar.API_GET_SHARED_GOODS;
        url += "&mid=" + mid;
        url += "&num=" + BaseVar.REQUESTNUM_10;
        url += "&curpage=" + curpage;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }
}
