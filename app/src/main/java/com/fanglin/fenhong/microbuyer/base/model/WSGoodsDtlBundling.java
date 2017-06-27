package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 商品详情 促销单元 请求webservice 获取推荐组合与优惠套装 数据
 * Created by lizhixin on 2015/12/31
 */
public class WSGoodsDtlBundling extends APIUtil {

    public List<GoodsBundling> bundling;//优惠套装
    public double bundling_save_money;//最高节省的金额
    public List combo;//推荐组合

    private WSGoodsDtlBundlingCallBack mcb;

    public WSGoodsDtlBundling() {
        super ();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        WSGoodsDtlBundling adata = new Gson().fromJson(data, WSGoodsDtlBundling.class);
                        if (mcb != null) mcb.onWSGoodsDtlBundlingSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSGoodsDtlBundlingError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSGoodsDtlBundlingError(data);
                }
            }
        });
    }

    public void getBundlingList (String goodsId) {
        if (goodsId == null) {
            return;
        }

        String url = BaseVar.API_GET_GOODS_PROM_BUNDLING + "&goods_id=" + goodsId;

        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    public void setWSGoodsDtlBundlingCallBack(WSGoodsDtlBundlingCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSGoodsDtlBundlingCallBack {
        void onWSGoodsDtlBundlingError(String errcode);

        void onWSGoodsDtlBundlingSuccess (WSGoodsDtlBundling data);
    }

}
