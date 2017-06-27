package com.fanglin.fenhong.microbuyer.base.baselib;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/19-下午2:55.
 * 功能描述: 极速免税店 接口业务
 */
public class DutyfreeBO extends APIUtil {

    /**
     * 复写父类设置回调的方法,让其返回当前实例
     */
    @Override
    public DutyfreeBO setCallBack(FHAPICallBack cb) {
        super.setCallBack(cb);
        return this;
    }

    /**
     * 极速免税店 加入购物车
     *
     * @param member     会员
     * @param product_id 商品id
     * @param num        个数
     */
    public void addCart(Member member, String product_id, int num) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("product_id", product_id);
        params.addBodyParameter("num", String.valueOf(num));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_ADDCART, params);
    }

    public void delCart(Member member, String cart_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("cart_id", cart_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_DELCART, params);
    }

    public void editCart(Member member, String cart_id, int num, int select) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("cart_id", cart_id);
        params.addBodyParameter("num", String.valueOf(num));
        params.addBodyParameter("select", String.valueOf(select));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_EDITCART, params);
    }


    public void addAddr(Member m, DutyAddress address) {
        if (m == null || address == null) return;
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", m.member_id);
        params.addBodyParameter("token", m.token);

        if (!TextUtils.isEmpty(address.getTrue_name())) {
            params.addBodyParameter("true_name", address.getTrue_name());
        }

        if (!TextUtils.isEmpty(address.getMobile())) {
            params.addBodyParameter("mobile", address.getMobile());
        }

        if (!TextUtils.isEmpty(address.getArea_info())) {
            params.addBodyParameter("area_info", address.getArea_info());
        }

        if (!TextUtils.isEmpty(address.getTrue_name())) {
            params.addBodyParameter("true_name", address.getTrue_name());
        }

        if (!TextUtils.isEmpty(address.getAddress())) {
            params.addBodyParameter("address", address.getAddress());
        }

        if (!TextUtils.isEmpty(address.getIs_default())) {
            params.addBodyParameter("is_default", address.getIs_default());
        }

        if (!TextUtils.isEmpty(address.getProvince_id())) {
            params.addBodyParameter("province_id", address.getProvince_id());
        }

        if (!TextUtils.isEmpty(address.getCity_id())) {
            params.addBodyParameter("city_id", address.getCity_id());
        }

        if (!TextUtils.isEmpty(address.getArea_id())) {
            params.addBodyParameter("area_id", address.getArea_id());
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_ADDADDR, params);
    }

    public void delAddr(Member m, String address_id) {
        RequestParams params = new RequestParams();
        if (m != null) {
            params.addBodyParameter("mid", m.member_id);
            params.addBodyParameter("token", m.token);
        }
        params.addBodyParameter("address_id", address_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_DELADDR, params);
    }

    public void editAddr(Member m, DutyAddress address) {
        if (m == null || address == null) return;
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", m.member_id);
        params.addBodyParameter("token", m.token);

        if (!TextUtils.isEmpty(address.getAddress_id())) {
            params.addBodyParameter("address_id", address.getAddress_id());
        }

        if (!TextUtils.isEmpty(address.getTrue_name())) {
            params.addBodyParameter("true_name", address.getTrue_name());
        }

        if (!TextUtils.isEmpty(address.getMobile())) {
            params.addBodyParameter("mobile", address.getMobile());
        }

        if (!TextUtils.isEmpty(address.getArea_info())) {
            params.addBodyParameter("area_info", address.getArea_info());
        }

        if (!TextUtils.isEmpty(address.getTrue_name())) {
            params.addBodyParameter("true_name", address.getTrue_name());
        }

        if (!TextUtils.isEmpty(address.getAddress())) {
            params.addBodyParameter("address", address.getAddress());
        }

        if (!TextUtils.isEmpty(address.getIs_default())) {
            params.addBodyParameter("is_default", address.getIs_default());
        }

        if (!TextUtils.isEmpty(address.getProvince_id())) {
            params.addBodyParameter("province_id", address.getProvince_id());
        }

        if (!TextUtils.isEmpty(address.getCity_id())) {
            params.addBodyParameter("city_id", address.getCity_id());
        }

        if (!TextUtils.isEmpty(address.getArea_id())) {
            params.addBodyParameter("area_id", address.getArea_id());
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_EDITADDR, params);
    }

    public void selectAllCart(Member member, boolean select) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("select", select ? "1" : "0");
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_SELECTCART, params);
    }

    /**
     * @param member      Member
     * @param from        1 来自购物车 ,2 来自立即购买
     * @param cartInfo    String
     * @param is_selected int 0：未使用优惠券  1：使用优惠券
     */
    public void genOrder(Member member, String from, String cartInfo, int is_selected) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("from", from);
        params.addBodyParameter("cart_info", cartInfo);
        params.addBodyParameter("is_selected", String.valueOf(is_selected));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_GENORDER, params);
    }

    /**
     * @param member   Member
     * @param order_id String
     */
    public void cancelOrder(Member member, String order_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("order_id", order_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_CANCELORDER, params);
    }

    /**
     * @param member   Member
     * @param order_id String
     */
    public void delOrder(Member member, String order_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("order_id", order_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_DELORDER, params);
    }

    /**
     * 一键收货
     *
     * @param member   Member
     * @param order_id String
     */
    public void receiveAllOrder(Member member, String order_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("order_id", order_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_ALLRECEIVE, params);
    }

    /**
     * 运单确认收货
     *
     * @param member     Member
     * @param waybill_id String
     */
    public void receiveBill(Member member, String waybill_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("waybill_id", waybill_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_RECEIVEONE, params);
    }
}
