package com.fanglin.fenhong.microbuyer.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.BuildConfig;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.AppInfo;
import com.fanglin.fhlib.other.FHLog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * 作者： Created by Plucky on 15-11-19.
 */
public class CrashActivity extends BaseFragmentActivity {
    private Class<? extends Activity> restartActivityClass;
    private String crashdata;
    @ViewInject(R.id.tv_close)
    TextView tv_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        BaseFunc.setFont(tv_close);
        crashdata = CustomActivityOnCrash.getStackTraceFromIntent(getIntent());
        restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());
        autoFeedBack();
    }

    @OnClick(value = {R.id.tv_close})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                if (restartActivityClass != null) {
                    Intent intent = new Intent(mContext, restartActivityClass);
                    startActivity(intent);
                }
                finish();
                break;
        }
    }

    /**
     * 程序崩溃之后自动提交崩溃信息至服务器
     */
    private void autoFeedBack() {
        if (!BuildConfig.DEBUG) {
            new BaseBO().crashReport(member, new AppInfo(mContext).getString(), crashdata);
        }
    }
}
