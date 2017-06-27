package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 退货列表 list item
 */
public class ReturnGoodsList extends APIUtil implements APIUtil.FHAPICallBack {
    public String refund_id;//   退单id
    public String refund_sn;//   退单单号
    public String order_id;//    订单id
    public String store_id;//   店铺id
    public String store_name;//    店铺名称
    public String goods_id;//   商品ID 0表示全部退款
    public String order_goods_id;//   订单商品ID
    public String goods_name;//    商品名称 (全部退款时显示：订单商品全部退款)
    public int goods_num;//    商品数量
    public double refund_amount;//    退款金额
    public String goods_image;//
    public int refund_type;//  申请类型:1:退款 | 2:退货
    public String return_type;//    退货类型:1为不用退货,2为需要退货
    public LinkedTreeMap goods_spec;//    商品规格
    public String is_all;// 是否全部退款 0:否 | 1:是
    public String progress;// 退款/退货进度


    public ReturnGoodsList () {
        setCallBack (this);
    }

    public interface RGLModelCallBack {
        void onRGLError (String errcode);

        void onRGLList (List<ReturnGoodsList> list);
    }

    private RGLModelCallBack mcb;

    public void setModelCallBack (RGLModelCallBack cb) {
        this.mcb = cb;
    }

    @Override
    public void onEnd (boolean isSuccess, String data) {
        if (isSuccess) {
            try {
                List<ReturnGoodsList> list = new Gson ().fromJson (data, new TypeToken<List<ReturnGoodsList>> () {
                }.getType ());
                if (mcb != null) mcb.onRGLList (list);
            } catch (Exception e) {
                if (mcb != null) mcb.onRGLError ("-1");
            }
        } else {
            if (mcb != null) mcb.onRGLError (data);
        }
    }

    @Override
    public void onStart (String data) {

    }


    public void getList (Member m, int curpage) {
        if (m == null) {
            if (mcb != null) mcb.onRGLError ("-1");
            return;
        }
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);
        params.addBodyParameter ("num", String.valueOf (BaseVar.REQUESTNUM));
        params.addBodyParameter ("curpage", String.valueOf (curpage));

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GET_REFUND_LIST, params);
    }

    /**
     *  1.提交成功
     *  2.卖家拒绝
     *  3.卖家同意
     *  4.买家己发货
     *  5.收货期己到
     *  6.平台审核中
     *  7.审核成功
     */
    public static String[] getProgressDescAndColor (String progress, int refund_type) {
        String[] res = new String[]{"", "#aaa"};
        int pro = TextUtils.isDigitsOnly (progress) ? Integer.valueOf (progress) : -1;
        switch (pro) {
            case 1:
                res[0] = "卖家审核中";
                res[1] = "#faa429";
                break;
            case 2:
                res[0] = "卖家拒绝";
                res[1] = "#fa2855";
                break;
            case 3:
                if (refund_type == 2) {
                    res[0] = "卖家同意";
                    res[1] = "#63dc43";
                } else {
                    res[0] = "平台审核中";
                    res[1] = "#faa429";
                }
                break;
            case 4:
                res[0] = "卖家收货中";
                res[1] = "#faa429";
                break;
            case 5:
                res[0] = "收货期己到";
                res[1] = "#fa2855";
                break;
            case 6:
                res[0] = "平台审核中";
                res[1] = "#faa429";
                break;
            case 7:
                res[0] = refund_type == 1 ? "退款成功" : "退货成功";
                res[1] = "#63dc43";
                break;
        }
        return res;
    }
}
