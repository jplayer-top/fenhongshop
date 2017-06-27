package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-11-1.
 */
public class BankCard extends APIUtil {
    public String card_id;// 银行卡id
    public String bank_name;// 开户行名称
    public String bank_no;// 银行卡号
    public String bank_user;// 户主姓名
    public String id_card;// 身份证号 (可选)
    public String if_default;// 是否是默认银行卡 ( 1 是 0 否 )

    public int actionNum;//0 列表 1 删除 2 增改

    public BankCard () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    switch (actionNum) {
                        case 0:
                            try {
                                List<BankCard> list = new Gson ().fromJson (data, new TypeToken<List<BankCard>> () {
                                }.getType ());
                                if (mcb != null) mcb.onList (list);
                            } catch (Exception e) {
                                if (mcb != null) mcb.onError ("-1");
                            }
                            break;
                        case 1:
                        case 2:
                            if (mcb != null) mcb.onError ("0");
                            break;
                    }
                } else {
                    if (mcb != null) mcb.onError (data);
                }
            }
        });
    }

    public void getList (Member member) {
        actionNum = 0;
        if (member == null) {
            if (mcb != null) mcb.onError ("-1");
            return;
        }

        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", member.member_id);
        params.addBodyParameter ("token", member.token);
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_GETBANKCARD, params);
    }

    public void delete (Member member, String card_id) {
        actionNum = 1;
        if (member == null || TextUtils.isEmpty (card_id)) {
            if (mcb != null) mcb.onError ("-1");
            return;
        }

        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", member.member_id);
        params.addBodyParameter ("token", member.token);
        params.addBodyParameter ("card_id", card_id);
        execute (HttpRequest.HttpMethod.POST, BaseVar.API_DELBANKCARD, params);
    }

    public void update (Member member) {
        actionNum = 2;
        if (member == null) {
            if (mcb != null) mcb.onError ("-1");
            return;
        }

        if (TextUtils.isEmpty (bank_name) || TextUtils.isEmpty (bank_no) || TextUtils.isEmpty (bank_user)) {
            if (mcb != null) mcb.onError ("-1");
            return;
        }
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", member.member_id);
        params.addBodyParameter ("token", member.token);
        params.addBodyParameter ("bank_name", bank_name);
        params.addBodyParameter ("bank_no", bank_no);
        params.addBodyParameter ("bank_user", bank_user);
        if (card_id != null) params.addBodyParameter ("card_id", card_id);
        if (id_card != null) params.addBodyParameter ("id_card", id_card);
        if (if_default != null) params.addBodyParameter ("if_default", if_default);

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_SETBANKCARD, params);
    }

    private BCModelCallBack mcb;

    public void setModelCallBack (BCModelCallBack cb) {
        this.mcb = cb;
    }

    public interface BCModelCallBack {
        void onList (List<BankCard> list);

        void onError (String errcode);
    }


    public String getTitle () {
        String affix;
        if (!TextUtils.isEmpty (bank_no) && bank_no.length () > 4 && !TextUtils.equals (bank_no, "支付宝")) {
            affix = bank_no.substring (bank_no.length () - 4);
            affix = "(尾号:" + affix + ")";
        } else {
            affix = "(" + bank_no + ")";
        }
        return bank_user + affix;
    }

    public boolean getDefault () {
        return TextUtils.equals ("1", if_default);
    }

    public String getMaskIdCard () {
        String res;
        if (!TextUtils.isEmpty (id_card) && id_card.length () >= 15) {
            String pre = id_card.substring (0, 5);
            String aff = id_card.substring (id_card.length () - 5);
            res = "身份证:" + pre + "*****" + aff;
        } else {
            res = id_card;
        }
        return res;
    }

    public Spanned getDefaultDesc (Context c) {
        String fmt = c.getString (R.string.fmt_default_bankcard);
        if (getDefault ()) {
            fmt = String.format (fmt, "默认", getMaskIdCard ());
        } else {
            fmt = String.format (fmt, "", getMaskIdCard ());
        }

        return BaseFunc.fromHtml (fmt);
    }
}
