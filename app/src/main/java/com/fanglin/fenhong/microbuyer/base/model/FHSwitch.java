package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/7.
 * 功能开关
 */
public class FHSwitch extends APIUtil {
    public String name;//hongbao,             //功能名称
    public String type;//ios,wap,   //全部：all ，部分：andriod, ios, wap
    public String message;//正在升级......  //提示信息，如果为url，为跳转链接
    public String url;

    public FHSwitch() {
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<FHSwitch> fhSwitches;
                if (isSuccess) {
                    try {
                        fhSwitches = new Gson().fromJson(data, new TypeToken<List<FHSwitch>>() {
                        }.getType());
                    } catch (Exception e) {
                        fhSwitches = null;
                    }
                } else {
                    fhSwitches = null;
                }

                FHApp.getInstance().fhSwitches = fhSwitches;
                if (callBack != null) {
                    callBack.onSwitchList(fhSwitches);
                }
            }
        });
    }

    public boolean isForbiddened() {
        if (!TextUtils.isEmpty(type)) {
            return TextUtils.equals(type, "all") || type.contains("andriod");
        }
        return false;
    }

    public boolean isHongbao() {
        return TextUtils.equals("hongbao", name);
    }

    public boolean isDiscover() {
        return TextUtils.equals("discover", name);
    }

    public static FHSwitch getDiscover(List<FHSwitch> fhSwitches) {
        if (fhSwitches != null && fhSwitches.size() > 0) {
            for (FHSwitch item : fhSwitches) {
                if (item.isDiscover() && item.isForbiddened()) {
                    /** 返回禁用的开关*/
                    return item;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static FHSwitch getHongbao(List<FHSwitch> fhSwitches) {
        if (fhSwitches != null && fhSwitches.size() > 0) {
            for (int i = 0; i < fhSwitches.size(); i++) {
                FHSwitch item = fhSwitches.get(i);
                if (item.isHongbao() && item.isForbiddened()) {
                    /** 返回禁用的开关*/
                    return item;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 后端控制APP开关
     */
    public void getList() {
        execute(HttpRequest.HttpMethod.GET, BaseVar.API_SWITCH, null);
    }

    private FHSwitchModelCallBack callBack;

    public void setModelCallback(FHSwitchModelCallBack callback) {
        this.callBack = callback;
    }

    public interface FHSwitchModelCallBack {
        void onSwitchList(List<FHSwitch> list);
    }
}
