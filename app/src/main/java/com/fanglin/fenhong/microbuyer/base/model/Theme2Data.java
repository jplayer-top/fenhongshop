package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/5-下午5:39.
 * 功能描述:首页频道样式二数据模型
 */
public class Theme2Data extends APIUtil {
    private int type;
    private String title;
    private List<Object> list;

    private boolean[] collections;

    //附加字段
    private boolean expanded;

    public Theme2Data() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<Theme2Data> list;
                if (isSuccess) {
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<Theme2Data>>() {
                        }.getType());
                    } catch (Exception e) {
                        FHLog.d("Plucky", e.getMessage());
                        list = null;
                    }
                } else {
                    list = null;
                }
                if (callBack != null) {
                    callBack.onTheme2List(list);
                }
            }
        });
    }

    public void getList(int curpage) {
        String url = BaseVar.API_GET_THEME2_LIST;
        url += "curpage" + curpage;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public int getOriginListSize() {
        if (list == null) return 0;
        return list.size();
    }

    public List<Object> getList() {
        if (expanded) {
            return list;
        } else {
            if (list != null && list.size() > 8) {
                return list.subList(0, 8);
            } else {
                return list;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private Theme2APICallBack callBack;

    public void setModelCallBack(Theme2APICallBack callBack) {
        this.callBack = callBack;
    }

    public interface Theme2APICallBack {
        void onTheme2List(List<Theme2Data> list);
    }

    //Banner
    public List<Banner> getBanner() {
        List<Banner> banners;
        try {
            String jsonstr = new Gson().toJson(list);
            banners = new Gson().fromJson(jsonstr, new TypeToken<List<Banner>>() {
            }.getType());
        } catch (Exception e) {
            banners = null;
        }
        return banners;
    }

    //广告
    public List<Adv> getAdv() {
        List<Adv> advs;
        try {
            String jsonstr = new Gson().toJson(list);
            advs = new Gson().fromJson(jsonstr, new TypeToken<List<Adv>>() {
            }.getType());
        } catch (Exception e) {
            advs = null;
        }
        return advs;
    }

    //分类
    public List<ChannelOneData.ChannelClass> getChanelClass() {
        List<ChannelOneData.ChannelClass> classes;
        try {
            String jsonstr = new Gson().toJson(list);
            classes = new Gson().fromJson(jsonstr, new TypeToken<List<ChannelOneData.ChannelClass>>() {
            }.getType());
        } catch (Exception e) {
            classes = null;
        }
        return classes;
    }

    //主题商品
    public List<HomeNavData.ActivityListWithGoods> getActivityGoods() {
        List<HomeNavData.ActivityListWithGoods> activityListWithGoodses;
        try {
            String jsonstr = new Gson().toJson(list);
            activityListWithGoodses = new Gson().fromJson(jsonstr, new TypeToken<List<HomeNavData.ActivityListWithGoods>>() {
            }.getType());
        } catch (Exception e) {
            activityListWithGoodses = null;
        }
        return activityListWithGoodses;
    }

    //商品列表
    public List<GoodsScheme> getGoods() {
        List<GoodsScheme> goods;
        try {
            String jsonstr = new Gson().toJson(list);
            goods = new Gson().fromJson(jsonstr, new TypeToken<List<GoodsScheme>>() {
            }.getType());
        } catch (Exception e) {
            goods = null;
        }
        return goods;
    }

    public boolean getPlaceBoolean(int positon) {
        if (collections == null || collections.length == 0) {
            collections = new boolean[list.size()];
        }
        return collections[positon];
    }

    public void setPlaceBoolean(int position, boolean aBool) {
        if (collections != null && collections.length > position) {
            collections[position] = aBool;
        }
    }
}
