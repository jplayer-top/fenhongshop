package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/9-下午9:17.
 * 功能描述:
 */
public class SequenceModelData extends APIUtil {
    public String seckilling_title;//    秒杀活动顶部标题
    public String share_title;//    分享标题
    public String share_desc;//    分享描述
    public String share_img;//    分享配图
    public String share_url = BaseVar.HOST + BaseVar.FLAG;//   分享链接

    public SequenceModelData() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        SequenceModelData seqData = new Gson().fromJson(data, SequenceModelData.class);
                        if (mcb != null) mcb.onSeqData(seqData);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onSeqData(null);
                    }
                } else {
                    if (mcb != null) mcb.onSeqData(null);
                }
            }
        });
    }

    public void getData() {
        execute(HttpRequest.HttpMethod.GET, BaseVar.API_GET_SEQUENCE_DATA, null);
    }

    private SequenceDataCallBack mcb;

    public void setModelCallBack(SequenceDataCallBack cb) {
        this.mcb = cb;
    }

    public interface SequenceDataCallBack {
        void onSeqData(SequenceModelData data);
    }
}
