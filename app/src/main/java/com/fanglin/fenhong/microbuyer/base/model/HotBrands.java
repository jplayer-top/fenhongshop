package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/12.
 * 今日最大牌  品牌馆
 * modify by lizhixin on 2016/02/04
 */
public class HotBrands extends APIUtil {
    public String brand_id;//     品牌id
    public String brand_name;//   品牌名称
    public String brand_pic;//    品牌图片
    public String brand_banner;// 品牌横幅（用于品牌下的商品列表页面）
    public String share_title;//  分享标题
    public String share_desc;//   分享描述
    public String share_img;//    分享图片
    public String brand_url;//预计API返回点击链接

    public static String getShareUrl(String brand_id, String brand_banner, String brand_name, String share_desc, String share_img, String share_title) {
        brand_banner = BaseFunc.UrlEncode(brand_banner);
        brand_name = BaseFunc.UrlEncode(brand_name);
        return String.format(BaseVar.SHARE_HOTBRAND_DTL, brand_id, brand_banner, brand_name, share_desc, share_img, share_title);//添加后三个参数，配合wap
    }

    public HotBrands() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<HotBrands> lst = new Gson().fromJson(data, new TypeToken<List<HotBrands>>() {
                        }.getType());
                        if (mcb != null) mcb.onHBList(lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onHBError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onHBError(data);
                }
            }
        });
    }

    public void getList(int area, int num, int curpage) {
        String url = BaseVar.API_GET_HOT_BRANDS;
        if (area != -1) {
            url += "&area=" + area;
        }
        url += "&num=" + num;
        url += "&curpage=" + curpage;

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private HBModelCallBack mcb;

    public void setModelCallBack(HBModelCallBack cb) {
        this.mcb = cb;
    }

    public interface HBModelCallBack {
        void onHBList(List<HotBrands> list);

        void onHBError(String errcode);
    }
}
