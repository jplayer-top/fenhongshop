package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/9.
 */
public class ActivityList extends APIUtil {
    public String activity_id;//
    public String activity_title;//
    public String activity_banner;//
    public String activity_url;
    public String activity_pic;//
    public String resource_tags;//统计用 -- lizhixin

    private String share_title;//
    private String share_desc;//
    private String share_img;//

    public String getShareDesc() {
        if (!TextUtils.isEmpty(share_desc)) {
            return share_desc;
        }
        return activity_title;
    }

    public String getShareImg() {
        if (!TextUtils.isEmpty(share_img)) {
            return share_img;
        }
        return activity_pic;
    }

    public String getShareTitle() {
        if (!TextUtils.isEmpty(share_title)) {
            return share_title;
        }

        return activity_title;
    }

    public ActivityList() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<ActivityList> lst = new Gson().fromJson(data, new TypeToken<List<ActivityList>>() {
                        }.getType());
                        if (mcb != null) mcb.onAlList(lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onAlError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onAlError(data);
                }
            }
        });
    }


    /**
     * type    活动类型 （中文全拼 如：主题馆 : zhutiguan)
     * channel 按频道获取 （可选，1 商城首页  2 全球购首页）
     * num 分页数量/每次请求数量
     * curpage 当前页/第几次请求 （第一页对应数字1）
     */
    public void getList(int area, int chanel, String type, int num, int curpage) {
        String url = BaseVar.API_GET_ACTIVITY_LIST;
        url += "&area=" + area;
        if (type != null) {
            url += "&type=" + type;
        }
        url += "&num=" + num;
        url += "&channel=" + chanel;
        url += "&curpage=" + curpage;

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private ActivityListModelCallBack mcb;

    public void setModelCallBack(ActivityListModelCallBack cb) {
        this.mcb = cb;
    }

    public interface ActivityListModelCallBack {
        void onAlError(String errcode);

        void onAlList(List<ActivityList> list);
    }

    public static List<ActivityList> getTest() {
        List<ActivityList> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActivityList alist = new ActivityList();
            alist.activity_banner = "http://pic.fenhongshop.com/slides/20160323/56f25e8aba751.jpg";
            list.add(alist);
        }
        return list;
    }
}
