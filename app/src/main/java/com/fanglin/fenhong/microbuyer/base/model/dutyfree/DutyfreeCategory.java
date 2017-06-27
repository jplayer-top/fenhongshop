package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/13-上午11:49.
 * 功能描述: 极速免税 分类
 */
public class DutyfreeCategory extends APIUtil {

    public int position;

    private String id;
    private String msg;//
    private String type_name;
    private int type;// type=0 表示分类 type=1 表示品牌
    private String url;//点击url
    private String img;//显示图片
    private List<BaseProduct> goods_list;


    public List<BaseProduct> getGoodsList() {
        return goods_list;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTypeName(String type_name) {
        this.type_name = type_name;
    }

    public String getId() {
        return id;
    }

    public String getTypeName() {
        return type_name;
    }

    public String getMsg() {
        return msg;
    }

    public int getType() {
        return type;
    }

    public DutyfreeCategory() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (callBack != null) {
                    if (isSuccess) {
                        List<DutyfreeCategory> list = new Gson().fromJson(data, new TypeToken<List<DutyfreeCategory>>() {
                        }.getType());
                        callBack.onCategoryReqList(list);
                    } else {
                        callBack.onCategoryReqList(null);
                    }
                }
            }
        });
    }

    /**
     * 获取极速免税店一级分类
     */
    public void getCategory1() {
        String url = BaseVar.API_DUTYFREE_CATEGORYONE;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private CategoryRequestCallBack callBack;

    public void setCallBack(CategoryRequestCallBack callBack) {
        this.callBack = callBack;
    }

    public interface CategoryRequestCallBack {
        void onCategoryReqList(List<DutyfreeCategory> list);
    }

    public static List<DutyfreeCategory> getData() {
        List<DutyfreeCategory> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            DutyfreeCategory category = new DutyfreeCategory();
            category.setImg("http://pic.fenhongshop.com/microshop/shop_class/20160712/5784b34c04275.jpg");
            category.setTypeName("小小" + i);
            list.add(category);
        }
        return list;
    }
}
