package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/9-下午12:04.
 * 功能描述:
 */
public class SeqGoodsModel extends BaseGoods {
    public String id;//活动商品编号
    public String killing_title;//秒杀商品显示的标题
    public String killing_pic;//秒杀图片
    public String goods_pic;//秒杀首页图片
    public double killing_price;//秒杀价格
    public long start_time;
    public long end_time;
    public long countdown;//距离开始的时间 s
    public long countdown_end;//距离结束的时间 s
    public int is_end;//是否结束 1:已结束 | 0:未结束
    public int killing_stock;//秒杀库存
    public int killed_pct;//已售百分比 (int)
    public String store_id;
    public String cover_title;
    public String cover_pic;

    public SeqGoodsModel() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<SeqGoodsModel> goods = new Gson().fromJson(data, new TypeToken<List<SeqGoodsModel>>() {
                        }.getType());
                        if (mcb != null) mcb.onSeqGoodsList(goods);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onSeqGoodsList(null);
                    }
                } else {
                    if (mcb != null) mcb.onSeqGoodsList(null);
                }
            }
        });
    }

    public void getGoods(String seqid) {
        String url = BaseVar.API_GET_SEQUENCE_GOODS + "&sequence_id=" + seqid;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private SeqGoodsModelCallBack mcb;

    public void setModelCallBack(SeqGoodsModelCallBack cb) {
        this.mcb = cb;
    }

    public interface SeqGoodsModelCallBack {
        void onSeqGoodsList(List<SeqGoodsModel> list);
    }

    public String getKillPctDesc() {
        if (!isSaleOut()) {
            return "已售" + killed_pct + "%";
        } else {
            return "已抢光";
        }
    }

    //判断商品是否已经抢光
    public boolean isSaleOut() {
        return !(killed_pct >= 0 && killed_pct < 100);
    }
}
