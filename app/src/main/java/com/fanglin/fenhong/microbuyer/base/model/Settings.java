package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhlib.other.PreferenceUtils;
import com.google.gson.Gson;

/**
 * 作者： Created by Plucky on 15-10-2.
 */
public class Settings {
    public boolean autoUpdate = true;//wifi环境下自动更新
    public boolean highQuality = false;//高画质与普通画质
    public boolean recept = true;//接收推送消息
    public boolean sound = true;//声音提醒

    public static void setSettings(Context c, Settings mset) {
        PreferenceUtils.setPrefString(c, BaseVar.SETTINGS, new Gson().toJson(mset));
    }

    /** 获取设置信息*/
    public static Settings getSettings(Context c) {
        Settings mset;
        try {
            String json = PreferenceUtils.getPrefString(c, BaseVar.SETTINGS, "");
            mset = new Gson().fromJson(json, Settings.class);
        } catch (Exception e) {
            mset = new Settings();
        }
        if (mset == null) mset = new Settings();
        return mset;
    }
}
