package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * authorCreated by Plucky on 16/3/7-下午215.
 * 功能描述
 */
public class SequenceModel extends APIUtil {

    public String sequence_id;// 25,
    public String sequence_time;//0800,
    public String sequence_title;// "今日场/星期二/星期三...", 场次标题（只有单场次模式才返回）
    public int goods_num;//场次商品款数
    public int sequence_state = 1;//场次状态 1 未开始 2 进行中  3 已结束  4 管理员关闭
    public String sequence_theme;//default,
    public Banner sequence_banner;
    public long start_time;//1457395200,
    public long end_time;//1457452799,
    public long countdown;//开场倒计时秒数
    public int if_current;// 0, 是否为当前场 1 是 0 否
    public int if_cover;//   1, 是否做特殊处理（是才返回，否则不返回）
    public int sequence_type;//场次类型 （1 今日场  2 明日预告场）
    public boolean isLast;


    public SequenceModel() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<SequenceModel> list = new Gson().fromJson(data, new TypeToken<List<SequenceModel>>() {
                        }.getType());
                        if (mcb != null) mcb.onSeqsList(list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onSeqsList(null);
                    }
                } else {
                    if (mcb != null) mcb.onSeqsList(null);
                }
            }
        });
    }

    public void getList() {
        execute(HttpRequest.HttpMethod.GET, BaseVar.API_GET_SEQUENCE_LIST, null);
    }

    private SeqsCallBack mcb;

    public void setModelCallBack(SeqsCallBack cb) {
        this.mcb = cb;
    }

    public interface SeqsCallBack {
        void onSeqsList(List<SequenceModel> list);
    }

    public String getSeqStatus() {
        return sequence_state == 2 ? (if_current == 1 ? "抢购中" : "已开抢") : (sequence_state == 3 ? "已结束" : "即将开抢");
    }

    public class Banner {
        public String pic;
        public String url;
    }

    public String getClockTips() {
        StringBuilder sb = new StringBuilder();
        if (sequence_state == 2) {//进行中
            if (if_current == 1) {
                sb.append("限时限量 入场疯抢");
            } else {
                sb.append("本场次还有商品可以继续抢购");
            }
        } else {
            if (sequence_state == 1)//未开始
            {
                sb.append("即将开始");
                sb.append(sequence_time);
                sb.append("开抢");
            } else {
                sb.append("本场活动已结束");//3已结束4管理员关闭 均视为活动结束
            }
        }
        return sb.toString();
    }

    public String getPicUrl() {
        return sequence_banner != null ? sequence_banner.pic : null;
    }

    public String getClickUrl() {
        return sequence_banner != null ? sequence_banner.url : null;
    }

}
