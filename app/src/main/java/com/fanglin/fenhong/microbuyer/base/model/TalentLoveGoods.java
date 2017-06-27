package com.fanglin.fenhong.microbuyer.base.model;

import android.util.Log;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/22-上午8:52.
 * 功能描述: 达人推荐的商品
 */
public class TalentLoveGoods extends APIUtil {
    private TalentInfo talent;
    private List<GoodsClass> goods_classes;
    private List<GoodsList> goods;

    public TalentLoveGoods() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                TalentLoveGoods talentLoveGoods;
                if (isSuccess) {
                    try {
                        talentLoveGoods = new Gson().fromJson(data, TalentLoveGoods.class);
                    } catch (Exception e) {
                        Log.d("Plucky", e.getMessage());
                        talentLoveGoods = null;
                    }
                } else {
                    talentLoveGoods = null;
                }

                if (mCallBack != null) {
                    mCallBack.onTalentLoveGoodsData(talentLoveGoods);
                }
            }
        });
    }


    /**
     * 获取达人推荐商品列表 (get)
     *
     * @param talent_id 达人id
     * @param gid       分类id（可选，二级分类）
     * @param curpage   当前页/第几次请求 num 分页数量/每次请求数量
     */
    public void getTalentLoveGoods(String talent_id, String gid, int curpage) {
        String url = BaseVar.API_GET_RECOMMEND_GOODS;
        url += "&talent_id=" + talent_id;
        url += "&gid=" + gid;
        url += "&num=" + BaseVar.REQUESTNUM_10;
        url += "&curpage=" + curpage;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    /**
     * CallBack
     */
    public interface TalentLoveGoodsModelCallBack {
        void onTalentLoveGoodsData(TalentLoveGoods talentLoveGoods);
    }

    private TalentLoveGoodsModelCallBack mCallBack;

    public void setModelCallBack(TalentLoveGoodsModelCallBack callBack) {
        this.mCallBack = callBack;
    }

    /**
     * getter&setter
     */
    public TalentInfo getTalent() {
        return talent;
    }


    public List<GoodsClass> getGoods_classes() {
        return goods_classes;
    }

    public List<GoodsList> getGoods() {
        return goods;
    }
}
