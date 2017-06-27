package com.fanglin.fenhong.microbuyer.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.event.WifiConnectDealEvent;
import com.fanglin.fenhong.microbuyer.base.event.WifiUnconnectHintAfter;
import com.fanglin.fenhong.microbuyer.base.event.WifiUnconnectHintEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by lizhixin on 2016/05/31
 * 网络状态变化的接收器
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo infoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (infoMobile.isConnected() || infoWifi.isConnected()) {
            //网络可用
            EventBus.getDefault().post(new WifiConnectDealEvent());
            //從无网络到有网络环境时要通知去刷新数据
            EventBus.getDefault().post(new WifiUnconnectHintAfter());
        } else {
            //网络不可用
            EventBus.getDefault().post(new WifiUnconnectHintEvent());
        }
    }

}
