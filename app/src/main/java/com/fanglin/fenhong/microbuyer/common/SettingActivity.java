package com.fanglin.fenhong.microbuyer.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.Settings;
import com.fanglin.fenhong.microbuyer.buyer.LoginActivity;
import com.fanglin.fenhong.microbuyer.buyer.ProfileActivity;
import com.fanglin.fhlib.other.DataCleanManager;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 作者： Created by Plucky on 15-10-2.
 * modify by lizhixin on 2016/04/08 清除缓存后设置0B
 */
public class SettingActivity extends BaseFragmentActivityUI {

    Settings settings;
    @ViewInject(R.id.tv_cb_auto)
    TextView tv_cb_auto;
    @ViewInject(R.id.tv_cb_high)
    TextView tv_cb_high;
    @ViewInject(R.id.tv_cb_normal)
    TextView tv_cb_normal;
    @ViewInject(R.id.tv_cb_recept)
    TextView tv_cb_recept;
    @ViewInject(R.id.tv_cb_sound)
    TextView tv_cb_sound;
    @ViewInject(R.id.tv_cache)
    TextView tv_cache;

    @ViewInject(R.id.btn_exit)
    Button btn_exit;
    FHApp fhApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_setting, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        fhApp = (FHApp) getApplication();
        settings = Settings.getSettings(this);

        setHeadTitle(R.string.action_settings);

        BaseFunc.setFont(tv_cb_auto);
        BaseFunc.setFont(tv_cb_high);
        BaseFunc.setFont(tv_cb_normal);
        BaseFunc.setFont(tv_cb_recept);
        BaseFunc.setFont(tv_cb_sound);
        CalculateCahce();
    }

    @OnClick(value = {R.id.LAccount, R.id.LAboutUs, R.id.btn_exit, R.id.LAutoUpdate, R.id.LHigh, R.id.LNormal, R.id.LRecept, R.id.LSound, R.id.LClear, R.id.LConcept, R.id.Ladvice})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.LAccount:
                if (member == null) {
                    BaseFunc.gotoLogin(mContext);
                    return;
                }
                BaseFunc.gotoActivity(this, ProfileActivity.class, null);
                break;
            case R.id.LAboutUs:
                BaseFunc.gotoActivity(this, FHBrowserActivity.class, BaseVar.ABOUTUS);
                break;
            case R.id.btn_exit:
                confirmExit();
                break;
            case R.id.LAutoUpdate:
                settings.autoUpdate = !settings.autoUpdate;
                RefreshView();
                break;
            case R.id.LHigh:
                settings.highQuality = true;
                RefreshView();
                break;
            case R.id.LNormal:
                settings.highQuality = false;
                RefreshView();
                break;
            case R.id.LRecept:
                settings.recept = !settings.recept;
                RefreshView();
                break;
            case R.id.LSound:
                settings.sound = !settings.sound;
                RefreshView();
                break;
            case R.id.LClear:
                confirmClear();
                break;
            case R.id.LConcept:
                BaseFunc.gotoActivity(this, ConceptActivity.class, null);
                break;
            case R.id.Ladvice://意见反馈
                BaseFunc.gotoActivity(this, FeedBackActivity.class, null);
                break;
            default:
                break;
        }
    }

    private void RefreshView() {
        if (member == null) {
            btn_exit.setVisibility(View.GONE);
        } else {
            btn_exit.setVisibility(View.VISIBLE);
        }
        if (settings == null) return;
        tv_cb_auto.setVisibility(settings.autoUpdate ? View.VISIBLE : View.GONE);
        if (settings.highQuality) {
            tv_cb_high.setVisibility(View.VISIBLE);
            tv_cb_normal.setVisibility(View.GONE);
        } else {
            tv_cb_high.setVisibility(View.GONE);
            tv_cb_normal.setVisibility(View.VISIBLE);
        }
        tv_cb_recept.setVisibility(settings.recept ? View.VISIBLE : View.GONE);
        tv_cb_sound.setVisibility(settings.recept ? (settings.sound ? View.VISIBLE : View.GONE) : View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
        if (settings == null) return;
        Settings.setSettings(this, settings);
        FHApp app = (FHApp) getApplication();
        app.resetSettings();
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
        RefreshView();
    }

    /**
     * 计算缓存
     */
    private void CalculateCahce() {
        try {
            tv_cache.setText(BaseVar.ZEROBYTE);
            long size2 = DataCleanManager.getTotalCacheSize(mContext);
            // 欺骗的更加彻底一点 Added By Plucky
            long sizedx = size2 - fhApp.getCachesize();
            sizedx = sizedx < 0 ? 0 : sizedx;
            if (sizedx >= 0 && sizedx <= 2 * Math.pow(1024, 2)) {
                tv_cache.setText(BaseVar.ZEROBYTE);
            } else {
                fhApp.setCachesize(size2);
                tv_cache.setText(FHLib.getFriendlySize(size2));
            }
        } catch (Exception e) {
            tv_cache.setText(BaseVar.ZEROBYTE);
        }
    }

    private void ClearCache() {
        try {
            ImageLoader.getInstance().clearDiskCache();
            BitmapUtils b = new BitmapUtils(mContext);
            b.clearDiskCache();
            if (fhApp.fhdb != null) {
                fhApp.fhdb.deleteAll(Member.class);
            }
            DataCleanManager.cleanInternalCache(mContext);
            DataCleanManager.cleanExternalCache(mContext);
            mContext.deleteDatabase("webview.db");
            mContext.deleteDatabase("webviewCache.db");//浏览器缓存
            CalculateCahce();
        } catch (Exception e) {
            //
        }
    }

    private void confirmClear() {
        final FHHintDialog fhd = new FHHintDialog(SettingActivity.this);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                ClearCache();
            }
        });
        fhd.setTvContent(getString(R.string.tips_of_clear_cache));
        fhd.show();

    }

    private void confirmExit() {
        final FHHintDialog fhd = new FHHintDialog(SettingActivity.this);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                BaseFunc.gotoLogin(mContext);
            }
        });
        fhd.setTvContent(getString(R.string.tips_of_exit));
        fhd.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.LOGIN_REQ && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
