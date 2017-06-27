package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-15.
 */
public class StoreHomeCls extends APIUtil {

    public String stc_id;//    分类id
    public String stc_name;//    分类名称
    public String stc_parent_id;//    父级分类id
    public int stc_state;//:1
    public String store_id;//
    public int stc_sort;//0    分类排序
    public List<StoreHomeCls> children;//    子分类

    public StoreHomeCls() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<StoreHomeCls> lst = new Gson().fromJson(data, new TypeToken<List<StoreHomeCls>>() {
                        }.getType());
                        if (mcb != null) mcb.onSHCList(lst);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onSHCError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onSHCError(data);
                }
            }
        });
    }

    public void getList(String store_id) {
        if (store_id == null) {
            if (mcb != null) mcb.onSHCError("-1");
            return;
        }
        String url = BaseVar.API_GET_STORE_HOME_CLS + "&store_id=" + store_id;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private SHCCallBack mcb;

    public void setModelCallBack(SHCCallBack cb) {
        this.mcb = cb;
    }

    public interface SHCCallBack {
        void onSHCList(List<StoreHomeCls> list);

        void onSHCError(String errcode);
    }

    public static StoreHomeCls getDefault() {
        StoreHomeCls shm = new StoreHomeCls();
        shm.stc_name = "全部分类";
        shm.stc_id = null;
        return shm;
    }
}
