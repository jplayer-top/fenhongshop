package com.tencent.xingepush;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/8/3.
 * 信鸽服务
 */
public class XGPushContext {

    public static void init(Context c, String userid) {
        if (userid != null) {
            onRegister(c, userid);
        } else {
            onRegister(c);
        }

        Intent service = new Intent(c, XGPushService.class);
        c.startService(service);
    }

    /**
     * 通过信鸽注册
     */
    public static void onRegister(Context mContext, String userid) {
        XGPushManager.registerPush(mContext, userid, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.d("XGToken", String.valueOf(o));
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.d("XGToken", s);
            }
        });
    }

    public static void onRegister(Context mContext) {
        XGPushManager.registerPush(mContext, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.d("XGToken", String.valueOf(o));
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.d("XGToken", s);
            }
        });
    }

    public static XgPushMessage handleNotification(XGNotifaction xgNotifaction) {
        XgPushMessage msg = new XgPushMessage();
        try {
            String cust = xgNotifaction.getCustomContent();
            JSONObject json = new JSONObject(cust == null ? "{}" : cust);
            msg.title = xgNotifaction.getTitle();
            msg.content = xgNotifaction.getContent();
            msg.timestamp = xgNotifaction.getNotifaction().when / 1000;
            msg.activity = json.has("activity") ? json.getInt("activity") : -1;
            msg.img = json.has("img") ? json.getString("img") : null;
            msg.url = json.has("url") ? json.getString("url") : null;
        } catch (Exception e) {
            msg = null;
        }
        return msg;
    }

    public static XgPushMessage getClickEntity(XGPushClickedResult click) {
        if (click == null) return null;
        XgPushMessage msg = new XgPushMessage();
        try {
            msg.title = click.getTitle();
            msg.content = click.getContent();
            msg.timestamp = click.getMsgId();
            JSONObject json = new JSONObject(click.getCustomContent());
            msg.activity = json.has("activity") ? json.getInt("activity") : -1;
            msg.img = json.has("img") ? json.getString("img") : null;
            msg.url = json.has("url") ? json.getString("url") : null;
        } catch (Exception e) {
            //
        }
        return msg;
    }

}
