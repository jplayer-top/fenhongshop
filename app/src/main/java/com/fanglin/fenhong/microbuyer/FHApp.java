package com.fanglin.fenhong.microbuyer;

import android.app.Application;
import android.app.Notification;
import android.net.Uri;
import android.text.TextUtils;

import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baselib.MobCalculate;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.event.CartActionEvent;
import com.fanglin.fenhong.microbuyer.base.event.UpdateActionEvent;
import com.fanglin.fenhong.microbuyer.base.model.CartAction;
import com.fanglin.fenhong.microbuyer.base.model.FHSwitch;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.Settings;
import com.fanglin.fenhong.microbuyer.base.model.TabEntity;
import com.fanglin.fenhong.microbuyer.base.model.UpdateVersion;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;
import com.fanglin.fenhong.microbuyer.wxapi.CrashActivity;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.tencent.xingepush.XGPushContext;
import com.tencent.xingepush.XgPushMessage;

import java.util.List;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;

/**
 * 作者： Created by Plucky on 2015/8/7.
 */
public class FHApp extends Application {
    public DbUtils fhdb;
    private Settings settings;//默认设置
    private FHLocation fhLocation;//默认地址
    public Member member;//作为一个全局变量 如果存在则取
    private long cachesize;

    public List<FHSwitch> fhSwitches;

    private static FHApp instance;
    private CartAction cartAction;//购物车数据变化监听--为了达到购物中中加减不重新请求数据
    public boolean hasPopUp;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        try {
            ShareSDK.initSDK(this);//初始化ShareSDK
        } catch (Exception e) {
            FHLog.w("Plucky", "ShareSDK init Error");
        }


        /** 处理程序崩溃恢复程序*/
        CustomActivityOnCrash.install(this);
        CustomActivityOnCrash.setErrorActivityClass(CrashActivity.class);

        //初始化UniversalImageLoader
        FHImageViewUtil.initImageLoader(this);

        MobCalculate.init(this);//友盟初始化

        /** 初始化信鸽*/
        registerXinge();

        /** 初始化数据库*/
        fhdb = DbUtils.create(this, BaseVar.DBNAME, BaseVar.DBVER, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
                try {
                    dbUtils.dropTable(DutyCartProduct.class);
                    dbUtils.dropTable(DutyAddress.class);
                } catch (DbException e) {
                    e.printStackTrace();
                    FHLog.d("Obl", e.getMessage());
                }
            }
        });
    }

    public void setCachesize(long cachesize) {
        this.cachesize = cachesize;
    }

    public long getCachesize() {
        return cachesize;
    }

    public void resetSettings() {
        this.settings = null;
    }

    public Settings getSettings() {
        if (settings == null) {
            settings = Settings.getSettings(this);
        }
        return settings;
    }

    public void resetLocation() {
        this.fhLocation = null;
    }

    public FHLocation getDefLocation() {
        if (fhLocation == null) {
            fhLocation = FHCache.getLocationFromSandBox(this);
        }
        return fhLocation;
    }

    public void registerXinge() {

        if (FHLib.isMainProcess(this)) {

            XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {
                @Override
                public void handleNotify(final XGNotifaction xgNotifaction) {
                    XgPushMessage msg = XGPushContext.handleNotification(xgNotifaction);
                    if (msg == null) return;
                    FHLog.d("XgPushMessage", String.valueOf(msg.activity));
                    if (msg.activity == 101) {
                        parseTagData(msg);
                    } else if (msg.activity == 7) {
                        parseUpdate(msg);
                    } else if (isAllowNotifiy(msg.activity)) {
                        parseNotify(msg);
                        Settings mset = Settings.getSettings(getApplicationContext());
                        if (mset.recept) {
                            if (!mset.sound) {
                                xgNotifaction.getNotifaction().defaults = Notification.DEFAULT_VIBRATE;
                            } else {
                                xgNotifaction.getNotifaction().defaults = Notification.DEFAULT_SOUND;
                            }
                        }
                        xgNotifaction.getNotifaction().icon = R.drawable.fh_push;
                        xgNotifaction.doNotify();
                    }
                }
            });
        }
    }

    private void parseTagData(XgPushMessage msg) {
        try {
            fhdb.deleteAll(TabEntity.class);
            List<TabEntity> tabs = new Gson().fromJson(msg.url, new TypeToken<List<TabEntity>>() {
            }.getType());
            if (tabs != null && tabs.size() > 0) {
                fhdb.saveOrUpdateAll(tabs);
            }
        } catch (Exception e) {
            //
        }
    }

    private boolean isAllowNotifiy(int type) {
        return type == 12 || type == 13 || type == 14 || type == 15 || type == 16 || type == 9 || type == 17 || type == 18 || type == 19;
    }

    private void parseNotify(XgPushMessage msg) {
        if (msg == null) return;
        FHCache.addAMsg(this, msg.activity, msg.content);
    }

    /**
     * 解析推送的版本更新信息
     * title 包含版本相关信息：http://a.com/?vercode=336&vername=2.0
     * content 更新日志
     * url app下载链接
     */
    private void parseUpdate(XgPushMessage msg) {
        if (msg == null) return;
        UpdateVersion updateVersion = new UpdateVersion();
        updateVersion.version_name = "最新版";
        if (BaseFunc.isValidUrl(msg.title)) {
            String vailidUrl = BaseFunc.formatHtml(msg.title);
            FHLog.d("Plucky", vailidUrl);
            Uri uri = Uri.parse(vailidUrl);
            String vercode = uri.getQueryParameter("vercode");
            String vername = uri.getQueryParameter("vername");
            String force = uri.getQueryParameter("force");
            if (!TextUtils.isEmpty(vercode)) {
                if (TextUtils.isDigitsOnly(vercode)) {
                    updateVersion.version_code = Integer.valueOf(vercode);
                }
            }
            if (!TextUtils.isEmpty(vername)) {
                updateVersion.version_name = vername;
            }
            if (!TextUtils.isEmpty(force)) {
                updateVersion.if_compulsory = force;
            }

        }
        updateVersion.update_log = msg.content;
        updateVersion.app_url = msg.url;
        FHLog.d("Plucky", updateVersion.if_compulsory);
        EventBus.getDefault().post(new UpdateActionEvent(updateVersion));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        /** 如果程序被结束--保存统计数据*/
        MobCalculate.end(this);
    }

    public static FHApp getInstance() {
        return instance;
    }

    public void setCartAction(CartAction cartAction) {
        this.cartAction = cartAction;
    }

    /**
     * 如果只是购物车中数量的变化就不请求接口
     *
     * @param numOffset >0表示添加 <0 表示减
     */
    public void sendCartActionByLocal(int numOffset) {
        if (cartAction != null) {
            int num = cartAction.getNum();
            num += numOffset;
            num = num < 0 ? 0 : num;
            cartAction.setNum(num);
            cartAction.setJustChangeNum(true);
        }
        EventBus.getDefault().post(new CartActionEvent(cartAction));
    }

    /**
     * 获取上次添加至购物车的商品 是否为国外
     *
     * @return 0, 1
     */
    public int getIsGlobal() {
        if (cartAction != null) return cartAction.getIs_global();
        return 1;
    }

    public boolean showDiscover() {
        //如果请求超时或者数据解析出错的话 则默认显示原来的发现首页
        if (fhSwitches == null) {
            return false;
        } else {
            //如果不为空则表示关闭该功能，为null则表示启用
            return FHSwitch.getDiscover(fhSwitches) == null;
        }

    }

    public FHSwitch getHongBao() {
        return FHSwitch.getHongbao(fhSwitches);
    }

}
