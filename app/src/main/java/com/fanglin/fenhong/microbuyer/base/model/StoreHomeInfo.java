package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-15.
 */
public class StoreHomeInfo extends APIUtil {
    public String store_id;//店铺id
    public String store_name;//店铺名称
    public String store_banner;//店铺横幅
    public String store_label;//店铺logo（宽尺寸）
    public String store_avatar;//店铺头像（方尺寸）
    public String store_slide;//
    public String store_slide_url;//
    public String store_qrcode;//店铺二维码
    public List<StoreSlide> store_slide_all;//幻灯片
    public int is_collected;//0是否为当前用户收藏 0:否|1:是

    public String store_baidusales;//百度商桥链接

    public String store_desccredit;//     "0"
    public String store_servicecredit;//  "0"
    public String store_deliverycredit;// "0"
    public String store_notice;
    public String store_address;
    public String store_time;
    public String store_es_responer;

    public String share_name;
    public String share_content;
    public String share_images;
    public String share_url;

    public int new_goods_status;//商品上新状态（1隐藏，2显示）
    public int new_goods_num;//7天内上新商品数量

    /**
     * 通过 List<StoreSlide> 转换为 List<Banner>
     */
    public List<Banner> getBanners() {
        if (store_slide_all == null || store_slide_all.size() == 0) {
            return null;
        }

        List<Banner> banners = new ArrayList<>();
        for (int i = 0; i < store_slide_all.size(); i++) {
            Banner b = new Banner();
            b.link_url = store_slide_all.get(i).url;
            b.image_url = store_slide_all.get(i).img;
            banners.add(b);
        }
        return banners;
    }

    public StoreHomeInfo() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        StoreHomeInfo info = new Gson().fromJson(data, StoreHomeInfo.class);
                        if (mcb != null) mcb.onSHIData(0, info);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onSHIData(-1, null);
                    }
                } else {
                    if (mcb != null) mcb.onSHIData(-1, null);
                }
            }
        });
    }

    public void getInfo(Member m, String store_id) {
        if (store_id == null) {
            if (mcb != null) mcb.onSHIData(-1, null);
            return;
        }

        String url = BaseVar.API_GET_STORE_HOME_INFO;
        if (m != null) url += "&mid=" + m.member_id;
        url += "&store_id=" + store_id;

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private SHICallBack mcb;

    public void setModelCallBack(SHICallBack cb) {
        this.mcb = cb;
    }

    public interface SHICallBack {
        void onSHIData(int errcode, StoreHomeInfo info);
    }


    public String getQrStoreName() {
        return "店铺：" + store_name;
    }

    /**
     * 内
     * 部
     * 类
     */
    public class StoreSlide {
        public String img;//幻灯片图片
        public String url;// 幻灯片链接
    }

    public boolean isShowRedDot() {
        return new_goods_status == 2;
    }
}
