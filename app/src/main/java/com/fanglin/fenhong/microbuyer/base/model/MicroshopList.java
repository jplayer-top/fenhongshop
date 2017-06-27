package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/5.
 * 我的微店列表
 */
public class MicroshopList extends APIUtil {

    public String shop_id;// 微店id,
    public String member_id;// 会员id,
    public String shop_name;// 微店名,
    public String shop_banner;// 微店店招,
    public String shop_scope;// 微店推荐语,
    public int share_count;// 分享数,
    public int collect_count;// 收藏数,
    public String fork_id;// 精品微店的代理id (自主微店为null),
    public String store_id;// 精品微店的相关店铺id（自主微店为null）,
    public List<GoodsList> goods;//

    /* 是否是精选微店 */
    public boolean isFancy () {
        return !(TextUtils.isEmpty (fork_id) || TextUtils.isEmpty (store_id));
    }

    public String getShop_name () {
        if (TextUtils.isEmpty (shop_name)) {
            return "分红全球购";
        }
        return shop_name;
    }

    public String getShop_scope () {
        if (TextUtils.isEmpty (shop_scope)) {
            return "购全球，享分红";
        }
        return shop_scope;
    }

    public MicroshopList () {
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<MicroshopList> alist = new Gson ().fromJson (data, new TypeToken<List<MicroshopList>> () {
                        }.getType ());
                        if (mcb != null) mcb.onMslList (alist);
                    } catch (Exception e) {
                        if (mcb != null) mcb.onMslError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.onMslError (data);
                }
            }
        });
    }

    public void getList (Member m, String type) {
        if (m == null) {
            if (mcb != null) mcb.onMslError ("-1");
            return;
        }

        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        if (!TextUtils.isEmpty (type)) {
            params.addBodyParameter ("type", type);
        }
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_MICROSHOP_LIST, params);
    }


    private MslModelCallBack mcb;

    public void setModelCallBack (MslModelCallBack cb) {
        this.mcb = cb;
    }

    public interface MslModelCallBack {
        void onMslError (String errcode);

        void onMslList (List<MicroshopList> list);
    }

    public static MicroshopList getTestData () {
        MicroshopList mshop = new MicroshopList ();
        mshop.shop_id = "1070023";// 微店id,
        mshop.member_id = "0";// 会员id,
        mshop.shop_name = "好想你枣旗舰店";// 微店名,
        mshop.shop_banner = BaseVar.APP_LOGO;// 微店店招,
        mshop.shop_scope = "来自世界最顶级红枣家族——好想你，系出名门，品质卓群。";// 微店推荐语,
        mshop.fork_id = "323963";
        mshop.store_id = "323963";

        List<GoodsList> goods = new ArrayList<> ();
        GoodsList g1 = new GoodsList ();
        g1.goods_id = "222";
        g1.goods_image = "http://img.fenhongshop.com/goods/323963/20141023/5448c271a10b8.jpg!small";
        goods.add (g1);

        g1 = new GoodsList ();
        g1.goods_id = "223";
        g1.goods_image = "http://img.fenhongshop.com/goods/323963/20141023/5448c26b331b0.jpg!big";
        goods.add (g1);

        g1 = new GoodsList ();
        g1.goods_id = "174";
        g1.goods_image = "http://img.fenhongshop.com/goods/323963/20141023/5448c2cb20ccf.jpg!big";
        goods.add (g1);

        mshop.goods = goods;

        return mshop;
    }
}
