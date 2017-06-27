package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/8.
 */
public class GroupBuyGoods extends APIUtil {
    public String groupbuy_id;// 团购id
    public String groupbuy_name;// 团购名称
    public String goods_id;// 商品id
    public String goods_name;// 商品名称
    public double goods_price;// 商品原价
    public double groupbuy_price;// 团购价格
    public String groupbuy_image;// 团购图片
    public String groupbuy_image1;//
    public String remark;// 团购副标题

    public GroupBuyGoods( ) {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<GroupBuyGoods> goods = new Gson().fromJson(data, new TypeToken<List<GroupBuyGoods>>() {
                        }.getType());
                        if (mcb != null) mcb.onGBGoodsList(goods);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onGBGError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onGBGError(data);
                }
            }
        });
    }

    /*
     *  获取团购商品列表(get)
     *  class_id 一级分类id
     *  s_class_id 二级分类id
     *  num 分页数量/每次请求数量  (可选, 默认20)
     *  curpage 当前页/第几次请求 (可选, 默认1)
     */
    public void getList(String class_id, String s_class_id, int curpage) {
        String url = BaseVar.API_GET_GRP_BUY_GOODS_LIST;
        if (!TextUtils.isEmpty(class_id)) {
            url += "&class_id=" + class_id;
        }
        if (!TextUtils.isEmpty(s_class_id)) {
            url += "&s_class_id=" + s_class_id;
        }
        url += "&num=" + 20;
        url += "&curpage=" + curpage;

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private GroupBuyGoodsModelCallBack mcb;

    public void setModelCallBack(GroupBuyGoodsModelCallBack cb) {
        this.mcb = cb;
    }

    public interface GroupBuyGoodsModelCallBack {
        void onGBGoodsList(List<GroupBuyGoods> goods);

        void onGBGError(String errcode);
    }

    public static List<GroupBuyGoods> getTestData() {
        List<GroupBuyGoods> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            GroupBuyGoods x = new GroupBuyGoods();
            x.goods_id = "" + i;
            x.goods_name = "xxxxxxxxxxxxxxxxxxxx" + i;
            list.add(x);
        }
        return list;
    }
}
