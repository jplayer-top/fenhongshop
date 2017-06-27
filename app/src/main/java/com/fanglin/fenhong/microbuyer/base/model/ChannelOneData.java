package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/30-下午4:20.
 * 功能描述:
 */
public class ChannelOneData extends APIUtil {
    public List<Banner> channel_slides;//轮播图
    public List<ChannelClass> channel_classes;//频道分类
    public HomeNavData.NavZhutiguan hot_activities;//主题商品
    public HomeNavData.NavTuijian hot_goods;//热门好货

    public class ChannelClass {
        public String class_name;
        public String class_url;
        public String class_pic;
    }

    public ChannelOneData() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        ChannelOneData channelOneData = new Gson().fromJson(data, ChannelOneData.class);
                        if (mcb != null) mcb.onChannelOneData(channelOneData);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onChannelOneData(null);
                    }
                } else {
                    if (mcb != null) mcb.onChannelOneData(null);
                }
            }
        });
    }

    private ChannelClassModelCallBack mcb;

    public void setModelCallBack(ChannelClassModelCallBack callBack) {
        this.mcb = callBack;
    }

    public interface ChannelClassModelCallBack {
        void onChannelOneData(ChannelOneData channelOneData);
    }

    public void getData(int channel_id) {
        String url = BaseVar.API_GET_CHANNEL_DATA;
        url += "&channel_id=" + channel_id;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }


    public int getClassSize() {
        if (channel_classes == null) return 0;
        return channel_classes.size();
    }

    public int getZhutiCount() {
        if (hot_activities != null && hot_activities.list != null)
            return hot_activities.list.size();
        return 0;
    }

    public int getHotGoodsSize() {
        if (hot_goods != null && hot_goods.list != null)
            return hot_goods.list.size();
        return 0;
    }

    public int getJustBannerCount() {
        int hotGoodsCount = getHotGoodsSize();
        if (hotGoodsCount >= 3) {
            return 3;
        } else {
            return hotGoodsCount;
        }
    }

    private int getLastGoodsCount() {
        int hotGoodsCount = getHotGoodsSize();
        if (hotGoodsCount >= 3) {
            return hotGoodsCount - 3;
        } else {
            return 0;
        }
    }

    public int getLastGoodsRows() {
        return BaseFunc.getRowCountOfList(getLastGoodsCount(), 2);
    }

    public String getHotActivityTitle() {
        if (hot_activities != null)
            return hot_activities.title;
        return "";
    }

    public String getHotGoodsTitle() {
        if (hot_goods != null) return hot_goods.title;
        return "";
    }

    public ChannelClass getChannerlClass(int row, int col) {
        int index = BaseFunc.getIndexOfList(row, col, 2);
        if (index < getClassSize()) {
            return channel_classes.get(index);
        }
        return null;
    }

    public HomeNavData.ActivityListWithGoods getZhutiWithGoods(int position) {
        if (position < getZhutiCount() && position >= 0) {
            return hot_activities.list.get(position);
        }
        return null;
    }

    public GoodsScheme getBannerGoods(int position) {
        if (position < getJustBannerCount()) {
            return hot_goods.list.get(position);
        }
        return null;
    }

    public GoodsScheme getLastGoods(int row, int col) {
        int index = BaseFunc.getIndexOfList(row, col, 2);
        if (index < getLastGoodsCount()) {
            return hot_goods.list.get(3 + index);
        }
        return null;
    }
}


