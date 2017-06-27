package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.ActivityGoodsComparatorByRatio;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/24-下午3:28.
 * 功能描述:
 */
public class HomeNavData extends APIUtil {
    public List<Banner> slides;
    public List<HomeNavigation> btn_navs;
    public List<NavActivity> activities;
    public NavGuojiaguan guojiaguan;
    public NavZhutiguan zhutiguan;
    public NavPinpaiguan pinpaiguan;
    public NavTuijian recommended_goods;


    public HomeNavData() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        HomeNavData homeNavData = new Gson().fromJson(data, HomeNavData.class);
                        if (mcb != null) mcb.onHomeNavData(homeNavData);
                    } catch (Exception e) {
                        FHLog.d("Plucky", e.getMessage());
                        if (mcb != null) mcb.onHomeNavData(null);
                    }
                } else {
                    if (mcb != null) mcb.onHomeNavData(null);
                }
            }
        });
    }

    public void getData() {
        execute(HttpRequest.HttpMethod.GET, BaseVar.API_GET_INDEX_DATA, null);
    }

    private HomeNavDataModelCallBack mcb;

    public void setModelCallBack(HomeNavDataModelCallBack cb) {
        this.mcb = cb;
    }

    public interface HomeNavDataModelCallBack {
        void onHomeNavData(HomeNavData data);
    }

    /**
     * 以下均为内部类
     */
    public class NavActivity {
        public NavSecKilling seckilling;
        public Adv seckilling_adv;
        public List<Adv> advs;
    }

    public class NavSecKilling {
        public String sequence_id;
        public String sequence_flag;
        public String sequence_url;
        public List<SeqGoodsModel> sequence_goods;
    }

    public class ActivityListWithGoods extends ActivityList {
        public List<ActivityGoods> goods_list;//

        public List<ActivityGoods> getReWordAscList() {
            if (goods_list != null && goods_list.size() > 0) {
                Object[] sortArr = goods_list.toArray();
                Arrays.sort(sortArr,new ActivityGoodsComparatorByRatio(false));

                List<ActivityGoods> result = new ArrayList<>();
                for (Object obj : sortArr) {
                    result.add((ActivityGoods) obj);
                }
                return result;
            } else {
                return null;
            }
        }
    }

    public class NavScheme {
        public String title;
        public String more_flag;
        public String more_title;
    }

    // 国家馆
    public class NavGuojiaguan extends NavScheme {
        public List<ActivityList> list;//
    }

    //主题馆
    public class NavZhutiguan extends NavScheme {
        public List<ActivityListWithGoods> list;
    }

    //品牌馆
    public class NavPinpaiguan extends NavScheme {
        public List<HotBrands> list;//
    }

    //今日推荐
    public class NavTuijian extends NavScheme {
        public List<GoodsScheme> list;
    }


    /* Model提供数据 */
    public String getHeaderString(int section) {
        switch (section) {
            case 3:
                return zhutiguan != null ? zhutiguan.title : "";
            case 4:
                return pinpaiguan != null ? pinpaiguan.title : "";
            case 5:
                return recommended_goods != null ? recommended_goods.title : "";
            default:
                return "";
        }
    }

    public int getZhutiCount() {
        if (zhutiguan != null) {
            if (zhutiguan.list != null) {
                return zhutiguan.list.size();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public ActivityListWithGoods getZhutiWithGoods(int position) {
        if (position < getZhutiCount() && position >= 0) {
            return zhutiguan.list.get(position);
        }
        return null;
    }

    public int getRecommendCount() {
        if (recommended_goods != null) {
            if (recommended_goods.list != null) {
                return recommended_goods.list.size();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取推荐商品
     *
     * @param position 索引
     * @return 商品
     */
    public GoodsScheme getRecommentGoodsAtPosition(int position) {
        if (position >= 0 && position < getRecommendCount()) {
            return recommended_goods.list.get(position);
        } else {
            return null;
        }
    }

    public Adv getMiaoshaAdv(int index) {
        if (activities != null && activities.size() > 0) {
            return activities.get(index).seckilling_adv;
        }
        return null;
    }

    public NavSecKilling getMiaosha(int index) {
        if (activities != null && activities.size() > 0) {
            return activities.get(index).seckilling;
        }
        return null;
    }

    public SeqGoodsModel getNacSecKillingGoods(int index) {
        NavSecKilling navSecKilling = getMiaosha(0);
        if (navSecKilling != null) {
            if (navSecKilling.sequence_goods != null && navSecKilling.sequence_goods.size() > 0 && index < navSecKilling.sequence_goods.size()) {
                return navSecKilling.sequence_goods.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Adv getAdv(int index) {
        if (activities != null && activities.size() > 0) {
            NavActivity navActivity = activities.get(0);
            if (navActivity != null) {
                List<Adv> advs = navActivity.advs;
                if (advs != null && advs.size() > 0) {
                    if (index >= 0 && index < advs.size()) {
                        return advs.get(index);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public int getBtnsSize() {
        if (btn_navs == null) return 0;
        return btn_navs.size();
    }

    public HomeNavigation getHomeNav(int row, int col) {
        int index = BaseFunc.getIndexOfList(row, col, 5);
        if (index < getBtnsSize()) {
            return btn_navs.get(index);
        }
        return null;
    }

    public int getCountryCount() {
        if (guojiaguan != null && guojiaguan.list != null)
            return guojiaguan.list.size();
        return 0;
    }

    public ActivityList getCountry(int row, int col) {
        int index = BaseFunc.getIndexOfList(row, col, 2);
        if (index < getCountryCount()) {
            return guojiaguan.list.get(index);
        }
        return null;
    }

    public int getBrandCount() {
        if (pinpaiguan != null && pinpaiguan.list != null)
            return pinpaiguan.list.size();
        return 0;
    }

    public HotBrands getBrand(int row, int col) {
        int index = BaseFunc.getIndexOfList(row, col, 2);
        if (index < getBrandCount()) {
            return pinpaiguan.list.get(index);
        }
        return null;
    }
}
