package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fhlib.other.FHLib;
import com.google.gson.Gson;

/**
 * 作者： Created by Plucky on 15-10-4.
 */
public class AppInfo {
    public String pkgname;//
    public String appname;//app名称
    public String vername;//app显示的版本名称
    public int vercode;//版本号
    public String deviceid;//当前运行设备的唯一编号
    public int mheight;//当前设备的高度--px
    public int mwidth;//当前设备的宽度--px
    public int networkType;//0 网络未连接 1 手机网络 2 wifi
    public String model;//手机型号
    public int versdk;//SDK 版本号
    public String versys;//android 系统版本号

    public String tFlag;//测试服务器表示字样
    public String flag;//
    public String api_key;//

    private String preView;

    public AppInfo(Context c) {
        PackageInfo info = FHLib.getPkgInfo(c);
        if (info != null) {
            pkgname = info.packageName;
            vername = info.versionName;
            vercode = info.versionCode;
            appname = c.getString(R.string.app_name);
        }
        tFlag = TextUtils.equals("http://test.fenhongshop.com", BaseVar.HOST) ? "测试服\n" : "";
        deviceid = FHLib.getDeviceID(c);
        DisplayMetrics dm = BaseFunc.getDisplayMetrics(c);
        mheight = dm.heightPixels;
        mwidth = dm.widthPixels;
        networkType = FHLib.isNetworkConnected(c);
        model = Build.MODEL;
        versdk = Build.VERSION.SDK_INT;
        versys = Build.VERSION.RELEASE;
        flag = "android";
        api_key = "6f89ef6522b0c98e88c234c379b54293";//猿族崛起
        preView = "";//"debug:" + BuildConfig.DEBUG;
    }

    public String getString() {
        return new Gson().toJson(this);
    }

    public String getAppinfo4Debug() {
        return tFlag + appname + "\n" + vername + " " + preView;
    }
}
