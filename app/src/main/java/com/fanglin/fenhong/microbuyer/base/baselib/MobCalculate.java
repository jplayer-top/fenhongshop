package com.fanglin.fenhong.microbuyer.base.baselib;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * 作者： Created by Plucky on 2015/11/17.
 * 友盟统计类
 */
public class MobCalculate {

    private static final String UMENG_APPKEY = "564a9b6ae0f55a3e7b0003ff";
    /*
     *  "分红商城"
     *  "小米应用商店"
     *  "应用宝"
     *  "豌豆荚"
     *  "百度"
     *  "360市场"
     *  "华为应用市场"
     *  "木蚂蚁"
     *  "OPPO软件商店"
     */

    //  <meta-data android:value="Channel ID" android:name="UMENG_CHANNEL"/>
    public static final String UMENG_CHANNEL = "分红商城";

    public static void init (Context c) {
        AnalyticsConfig.setAppkey (c, UMENG_APPKEY);
        //AnalyticsConfig.setChannel (UMENG_CHANNEL); 渠道包在AndroidManifext.xml文件中配置
    }

    public static void resumePage (Context c, String pkg) {
        if (TextUtils.isEmpty (pkg)) {
            MobclickAgent.onPageStart (pkg);
        }
        MobclickAgent.onResume (c);
    }


    public static void pausePage (Context c, String pkg) {
        if (!TextUtils.isEmpty (pkg)) {
            MobclickAgent.onPageEnd (pkg);
        }
        MobclickAgent.onPause (c);
    }

    public static void end (Context c) {
        MobclickAgent.onKillProcess (c);
    }

}
