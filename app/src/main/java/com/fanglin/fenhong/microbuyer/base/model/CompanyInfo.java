package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 作者： Created by Plucky on 15-9-30.
 */
public class CompanyInfo extends APIUtil {
    public String company_name;// 公司名称
    public String company_address;// 公司所在地
    public String company_address_detail;// 公司详细地址
    public String contacts_name;// 联系人
    public String contacts_phone;// 联系电话
    public String contacts_email;// 联系邮箱
    public String business_licence_number_electronic;// 营业执照电子版
    public String identity_code;// 身份证号
    public String identity_code_electronic;// 身份证电子版
    public String step;

    private boolean isUpdate = false;

    public CompanyInfo (Context c) {
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
                        CompanyInfo info;
                        try {
                            info = new Gson ().fromJson (data, CompanyInfo.class);
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

    public void getData (Member m) {
        if (m == null) return;
        String url = BaseVar.API_COMPANY_INFO + "&mid=" + m.member_id + "&token=" + m.token;
        isUpdate = false;
        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    public void update (Member m, String province_id) {
        if (m == null) return;
        isUpdate = true;
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);

        if (province_id != null) params.addBodyParameter ("province_id", province_id);
        if (company_name != null) params.addBodyParameter ("company_name", company_name);
        if (company_address != null) params.addBodyParameter ("company_address", company_address);
        if (company_address_detail != null)
            params.addBodyParameter ("company_address_detail", company_address_detail);
        if (contacts_name != null) params.addBodyParameter ("contacts_name", contacts_name);
        if (contacts_phone != null) params.addBodyParameter ("contacts_phone", contacts_phone);
        if (contacts_email != null) params.addBodyParameter ("contacts_email", contacts_email);
        if (business_licence_number_electronic != null)
            params.addBodyParameter ("business_licence_number_electronic", business_licence_number_electronic);
        if (identity_code != null) params.addBodyParameter ("identity_code", identity_code);
        if (identity_code_electronic != null)
            params.addBodyParameter ("identity_code_electronic", identity_code_electronic);

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_COMPANY_INFO_SAVE, params);
    }

    public void submit (Member m) {
        if (m == null) return;
        isUpdate = true;
        RequestParams params = new RequestParams ();
        params.addBodyParameter ("mid", m.member_id);
        params.addBodyParameter ("token", m.token);

        execute (HttpRequest.HttpMethod.POST, BaseVar.API_COMPANY_INFO_SUBMIT, params);
    }

    private CompanyInfoCallBack mcb;

    public void setModelCallBack (CompanyInfoCallBack cb) {
        this.mcb = cb;
    }

    public interface CompanyInfoCallBack {
        void onSuccess (CompanyInfo info);

        void onError (String errcode);
    }


}
