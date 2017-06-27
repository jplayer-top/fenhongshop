package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/5.
 */
public class GoodsScheme extends BaseGoods {

    public String goods_pic;// 商品配图（如果不为null，请使用该图作为商品展示图片）
    private String atype;
    public String brand_name;//品牌名称
    public String scheme_name;//
    public String scheme_desc;//
    public String goods_jingle;//

    public boolean isCollected;//是否已经收藏 用作桃心处理
    public String resource_tags;//统计用 -- lizhixin

    public GoodsScheme(String type) {
        super();
        atype = type;
        setCallBack(new FHAPICallBack() {

            @Override
            public void onStart(String data) {
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    List<GoodsScheme> list;
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<GoodsScheme>>() {
                        }.getType());
                    } catch (Exception e) {
                        list = null;
                    }
                    if (list != null && list.size() > 0) {
                        if (mcb != null) mcb.RgetList(atype, list);
                    } else {
                        if (mcb != null) mcb.RError(atype, "-1");
                    }
                } else {
                    if (mcb != null) mcb.RError(atype, data);
                }
            }
        });
    }

    public void getList(int area, int num, int curpage) {
        String url = BaseVar.API_GET_GOODS_SCHEME + "&num=" + num + "&type=" + atype + "&curpage=" + curpage;
        if (area > -1) {
            url += "&area=" + area;
        }
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private GoodsSchemeModelCallBack mcb;

    public void setModelCallBack(GoodsSchemeModelCallBack cb) {
        this.mcb = cb;
    }

    public interface GoodsSchemeModelCallBack {
        void RgetList(String curType, List<GoodsScheme> list);

        void RError(String curType, String errcode);
    }
}
