package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * Created by lizhixin on 2015/10/9.
 */
public class WSHomeNationalPavList extends APIUtil {
    public String activity_id;//
    public String activity_title;//
    public String activity_banner;//
    public String activity_pic;

    public WSHomeNationalPavList () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<WSHomeNationalPavList> lst = new Gson ().fromJson (data, new TypeToken<List<WSHomeNationalPavList>> () {
                        }.getType ());
                        if (mcb != null) mcb.onNationalPavListSuccess (lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onNationalPavListError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onNationalPavListError (data);
                }
            }
        });
    }


    /*
     * type    活动类型 （中文全拼 如：国家馆 : guojiaguan)
     * channel 按频道获取 （可选，1 商城首页  2 全球购首页）
     * num 分页数量/每次请求数量
     * curpage 当前页/第几次请求 （第一页对应数字1）
     */
    public void getList (int area, int chanel, String type, int num, int curpage) {
        String url = BaseVar.API_GET_ACTIVITY_LIST;
        url += "&area=" + area;
        if (type != null) {
            url += "&type=" + type;
        }
        url += "&num=" + num;
        url += "&channel=" + chanel;
        url += "&curpage=" + curpage;

        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private NationalPavListModelCallBack mcb;

    public void setModelCallBack (NationalPavListModelCallBack cb) {
        this.mcb = cb;
    }

    public interface NationalPavListModelCallBack {
        void onNationalPavListSuccess (List<WSHomeNationalPavList> list);

        void onNationalPavListError (String errcode);
    }
}
