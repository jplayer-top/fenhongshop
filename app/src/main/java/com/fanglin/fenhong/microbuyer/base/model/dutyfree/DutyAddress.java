package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/20-下午1:21.
 * 功能描述:急速免税店 收货地址
 */
public class DutyAddress extends APIUtil {

    private String address_id;//地址id,
    private String true_name;//收货姓名
    private String tel_phone;//手机号显示,
    private String mobile;//手机号,
    private String area_info;//城市信息,
    private String address;//街道信息,
    private String province_id;//省份id,
    private String city_id;//城市id,
    private String area_id;//区县id,
    private String is_default;//是否默认 0非默认 10默认

    //前端自组字段
    @Id
    private String indexID;//主键、用作缓存用
    private String cartID;//关联商品的ID
    private int selectNum;//关联商品选中的数量
    private boolean isSelected;//多选时是否选中


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_info() {
        return area_info;
    }

    public String getWholeAddrDesc() {
        return area_info + "  " + address;
    }

    public String getNameAndPhone() {
        return getTrue_name() + "　　　" + getTel_phone();
    }

    public void setArea_info(String area_info) {
        this.area_info = area_info;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getTel_phone() {
        return tel_phone;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        if (!TextUtils.isEmpty(mobile) && mobile.length() >= 11) {
            String pre = mobile.substring(0, 3);
            String afx = mobile.substring(mobile.length() - 4);
            tel_phone = pre + "****" + afx;
        }
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getIndexID() {
        return indexID;
    }

    public void setIndexID(String indexID) {
        this.indexID = indexID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSelectNumDesc() {
        return String.valueOf(selectNum);
    }

    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

    public DutyAddress() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (dutyAddrReqCallBack != null) {
                    if (isSuccess) {
                        List<DutyAddress> addresses;
                        try {
                            addresses = new Gson().fromJson(data, new TypeToken<List<DutyAddress>>() {
                            }.getType());
                        } catch (Exception e) {
                            addresses = null;
                        }
                        dutyAddrReqCallBack.onDutyAddrList(addresses);
                    } else {
                        dutyAddrReqCallBack.onDutyAddrList(null);
                    }
                }
            }
        });
    }

    private DutyAddrRequestCallBack dutyAddrReqCallBack;

    public void setDutyAddrReqCallBack(DutyAddrRequestCallBack dutyAddrReqCallBack) {
        this.dutyAddrReqCallBack = dutyAddrReqCallBack;
    }

    public interface DutyAddrRequestCallBack {
        void onDutyAddrList(List<DutyAddress> list);
    }

    public void getData(Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DUTYFREE_ADDRLIST, params);
    }
}
