package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/29.
 * 分享记录
 */
public class Share extends APIUtil {
    public String trace_membername;//分享者姓名
    public String trace_title;//分享标题
    public String trace_content;//分享内容
    public String trace_addtime;//分享时间
    public String trace_imgurl;//分享单品或者店铺的logo
    public String trace_from;//来源 1=shop 3=microshop
    public String trace_shareurl;//分享的链接
    public String trace_name;//分享的店铺或者商品的名称
    public String trace_sharetype;// 分享渠道的id的字符串串联.分享类型 1：内部动态 2:qq 3 新浪微博 4：微信朋友圈 5：微信好友 6：qq空间 7:人人网 8=短信  9=其它
    public int vistor_num;//累计访问次数

    public Share () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<Share> list = new Gson ().fromJson (data, new TypeToken<List<Share>> () {
                        }.getType ());
                        if (mcb != null) mcb.onTraceList (list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onTraceError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onTraceError (data);
                }
            }
        });
    }

    /**
     * mid:用户ID
     * token：token值
     * start:开始的记录索引
     * num:每页显示的数量
     * startdate:开始时间
     * enddate:结束时间
     * sort:排序 1:分享时间(trace_addtime)
     * order:降序 升序 1为升，2为降
     */
    public void getList (Member m, int start) {
        if (m == null) {
            if (mcb != null) mcb.onTraceError ("-1");
            return;
        }
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("start", String.valueOf (start));
        params.addBodyParameter ("num", String.valueOf (BaseVar.REQUESTNUM));
        params.addBodyParameter ("sort", String.valueOf (1));
        params.addBodyParameter ("order", String.valueOf (2));
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_TRACES, params);
    }

    public interface TraceModelCallBack {
        void onTraceError (String errcode);

        void onTraceList (List<Share> list);
    }

    private TraceModelCallBack mcb;

    public void setModelCallBack (TraceModelCallBack cb) {
        this.mcb = cb;
    }


    /* 获取图标的状态 */
    public boolean[] getIconStatus () {
        /*                               4      5      6       2      3 */
        /*                               朋友圈  微信   QQ空间  QQ     微博 */
        boolean[] status = new boolean[]{false, false, false, false, false};
        if (TextUtils.isEmpty (trace_sharetype)) {
            return status;
        }
        if (trace_sharetype.contains ("4")) {
            status[0] = true;
        }
        if (trace_sharetype.contains ("5")) {
            status[1] = true;
        }
        if (trace_sharetype.contains ("6")) {
            status[2] = true;
        }
        if (trace_sharetype.contains ("2")) {
            status[3] = true;
        }
        if (trace_sharetype.contains ("3")) {
            status[4] = true;
        }
        return status;
    }
}
