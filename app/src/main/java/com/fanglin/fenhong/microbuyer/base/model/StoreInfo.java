package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-1.
 * 店铺经营信息
 */
public class StoreInfo extends APIUtil {

    public String seller_name;// 商家账号
    public String store_name;// 店铺名称
    public String sg_name;// 店铺等级（字符串）
    public String sc_name;// 店铺分类（字符串）
    public List<StoreCategory> store_classes;// 经营类目（数组）
    public List<StoreGrade> grade_list;// 店铺等级（json数组）
    public List<StoreCls> store_class;// 店铺分类（json数组）
    public String step;

    /*用于更新*/
    public String sg_id;// 店铺等级id
    public String sc_id;// 店铺分类id
    public String gc_ids;// 经营类目id (逗号隔开)
    public String gc_names;// 经营类目名称 (逗号隔开)


    private boolean isUpdate = false;

    public StoreInfo () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {
            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    if (isUpdate) {
                        if (mcb != null) mcb.onSuccess (null);
                    } else {
                        StoreInfo info;
                        try {
                            info = new Gson ().fromJson (data, StoreInfo.class);
                        } catch (Exception e) {
                            info = null;
                        }
                        if (info != null) {
                            if (mcb != null) mcb.onSuccess (info);
                        } else {
                            if (mcb != null) mcb.onError ("-1");
                        }
                    }
                } else {
                    if (mcb != null) mcb.onError (data);
                }
            }
        });
    }

    /*获取店铺经营信息(get)*/
    public void getData (Member m) {
        if (m == null) return;
        isUpdate = false;
        String url = BaseVar.API_STORE_INFO + "&mid=" + m.member_id + "&token=" + m.token;
        execute (HttpRequest.HttpMethod.GET, url, null);

    }

    /*保存店铺经营信息(post)*/
    public void update (Member m) {
        if (m == null) return;
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        if (seller_name != null) params.addBodyParameter ("seller_name", seller_name);
        if (store_name != null) params.addBodyParameter ("store_name", store_name);
        if (sg_id != null) params.addBodyParameter ("sg_id", sg_id);
        if (sc_id != null) params.addBodyParameter ("sc_id", sc_id);
        if (gc_ids != null) params.addBodyParameter ("gc_ids", gc_ids);
        if (gc_names != null) params.addBodyParameter ("gc_names", gc_names);
        isUpdate = true;
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_STORE_INFO_SAVE, params);
    }

    /*删除经营类目(post)*/
    public void delete_cls (Member m) {
        if (m == null) return;
        if (gc_ids == null || gc_names == null) return;
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("gc_ids", gc_ids);
        params.addBodyParameter ("gc_names", gc_names);

        isUpdate = true;

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_STORE_CLS_DELETE, params);
    }

    /*提交店铺经营信息(post)*/
    public void submit (Member m) {
        if (m == null) return;
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        isUpdate = true;
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_STORE_INFO_SUBMIT, params);
    }


    private StoreInfoModeCallBack mcb;

    public void setModelCallBack (StoreInfoModeCallBack cb) {
        this.mcb = cb;
    }

    public interface StoreInfoModeCallBack {
        void onSuccess (StoreInfo info);

        void onError (String errcode);
    }

}
