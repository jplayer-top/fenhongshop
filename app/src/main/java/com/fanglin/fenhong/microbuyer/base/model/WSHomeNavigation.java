package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 首页导航按钮列表请求类
 * Created by lizhixin on 2015/12/23.
 */
public class WSHomeNavigation extends APIUtil {

    public WSHomeNavigation() {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<HomeNavigation> lst = new Gson ().fromJson (data, new TypeToken<List<HomeNavigation>> () {
                        }.getType ());
                        if (mcb != null) mcb.onNavigationSuccess(lst);
                    } catch (Exception e) {
                        FHLog.d("Plucky",e.getMessage());
                        if (mcb != null) mcb.onNavigationError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onNavigationError (data);
                }
            }
        });
    }


    /*
     * location 位置 （0: 头部 | 1: 中部 (8个导航按钮属于中部) | 2: 底部）
     * limit 限制数量 （可选）
     */
    public void getList (int location , int limit) {
        String url = BaseVar.API_GET_HOME_NAVIGATION;
        url += "&location=" + location;
        if (limit > 0) {
            url += "&limit=" + limit;
        }

        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private HomeNavigationModelCallBack mcb;

    public void setNavigationModelCallBack (HomeNavigationModelCallBack cb) {
        this.mcb = cb;
    }

    public interface HomeNavigationModelCallBack {
        void onNavigationSuccess(List<HomeNavigation> list);

        void onNavigationError(String errcode);
    }
}
