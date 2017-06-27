package com.fanglin.fenhong.microbuyer.base.baselib;

import android.app.Activity;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.Member;

/**
 * 作者： Created by Plucky on 2015/10/12.
 */
public class UpdateAction {

    /**
     * 添加购物车的操作
     */
    public static void add2Cart(final Activity act, String goodsid, final int num, final int gc_area) {
        if (goodsid == null || num == 0) return;
        final Member member = FHCache.getMember(act);
        if (member == null) {
            BaseFunc.gotoLogin(act);
            return;
        }

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    BaseFunc.showMsg(act, act.getString(R.string.add_success));
                    //请求数据
                    BaseFunc.getCartNum(member);
                }
            }
        }).add_cart(member.member_id, member.token, goodsid, member.store_id, num, gc_area, null, null);
    }

    /**
     * 添加购物车的操作
     */
    public static void add2Cart(final Activity act, String goodsid, final int num, int gc_area, APIUtil.FHAPICallBack cb) {
        if (goodsid == null || num == 0) return;
        Member member = FHCache.getMember(act);
        if (member == null) {
            BaseFunc.gotoLogin(act);
            return;
        }

        new BaseBO().setCallBack(cb).add_cart(member.member_id, member.token, goodsid, member.store_id, num, gc_area, null, null);
    }
}
