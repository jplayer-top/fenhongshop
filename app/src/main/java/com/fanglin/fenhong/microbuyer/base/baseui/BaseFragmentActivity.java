package com.fanglin.fenhong.microbuyer.base.baseui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baselib.MobCalculate;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.MessageNum;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.xingepush.XGPushContext;
import com.tencent.xingepush.XgPushMessage;

/**
 * 作者： Created by Plucky on 2015/7/6.
 * <p/>
 * 基类FragmentActivity
 */
public class BaseFragmentActivity extends FragmentActivity {
    public Context mContext;
    public Typeface iconfont;//iconfont 字体
    public Member member;
    public String member_id;
    public MessageNum msgnum;
    public FHApp fhApp;

    @Override
    protected void onResume() {
        super.onResume();

        /** 每次从后台返回前台,刷新一遍*/
        refreshBaseData();

        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            /** 处理信鸽的点击事件*/
            XgPushMessage msg = XGPushContext.getClickEntity(click);
            if (msg != null) {
                FHCache.onMsgClick(mContext, msg.activity);
                BaseFunc.urlClick(mContext, msg.url);
            }
        }

        MobCalculate.resumePage(this, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        iconfont = BaseFunc.geticonFontType(mContext);
        fhApp = ((FHApp) getApplication());

        refreshBaseData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
        MobCalculate.pausePage(this, null);
    }

    private void refreshBaseData() {
        member = FHCache.getMember(this);
        msgnum = FHCache.getNum(this);
        member_id = member != null ? member.member_id : null;
    }
}
