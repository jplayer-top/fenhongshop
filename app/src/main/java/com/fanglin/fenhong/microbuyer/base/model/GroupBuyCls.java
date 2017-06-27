package com.fanglin.fenhong.microbuyer.base.model;


import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/8.
 * 团购分类
 */
public class GroupBuyCls extends APIUtil {
    public String class_id;// 分类id
    public String class_name;// 分类名称
    public String class_image;// 分类图片
    public String class_parent_id;// 父级分类id
    public String sort;// 排序
    public String deep;//
    public String recommended;// 是否推荐 (0:否 | 1:是)
    public boolean isSelected;//用于记录选中状态

    public String share_title;// 分享标题
    public String share_desc;// 分享描述
    public String share_img;// 分享图片

    public GroupBuyCls () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<GroupBuyCls> list = new Gson ().fromJson (data, new TypeToken<List<GroupBuyCls>> () {
                        }.getType ());
                        if (mcb != null) mcb.GBCList (list);
                    } catch (Exception e) {
                        if (mcb != null) mcb.GBCError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.GBCError (data);
                }
            }
        });
    }


    /*
     *  获取团购分类(get)
     *  recommend 是否推荐 ( 可选， 0：否（默认） | 1：是 )
     *  class_parent_id 父级分类id ( 可选， 0：返回一级分类 (默认) )
     *  num 数量 ( 可选， 0：返回所有 (默认) )
     */
    public void get_groupbuy_class (int recommend, String class_parent_id, int num, int area) {
        String url = BaseVar.API_GET_GRP_BUY_CLASS;
        if (recommend != -1) {
            url += "&recommend=" + recommend;
        }
        url += "&class_parent_id=" + class_parent_id;
        url += "&num=" + num;
        if (area > -1) {
            url += "&area=" + area;
        }
        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private GroupBuyClsModelCallBack mcb;

    public void setModelCallBack (GroupBuyClsModelCallBack cb) {
        this.mcb = cb;
    }

    public interface GroupBuyClsModelCallBack {
        void GBCList (List<GroupBuyCls> lst);

        void GBCError (String errcode);
    }

}
