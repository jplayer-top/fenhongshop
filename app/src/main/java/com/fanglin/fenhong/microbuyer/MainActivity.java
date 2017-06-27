package com.fanglin.fenhong.microbuyer;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baselib.VarInstance;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.FHwifiHintPopWindow;
import com.fanglin.fenhong.microbuyer.base.event.CartActionEvent;
import com.fanglin.fenhong.microbuyer.base.event.HomeBtnDoubleClickEvent;
import com.fanglin.fenhong.microbuyer.base.event.LoadingFinishEvent;
import com.fanglin.fenhong.microbuyer.base.event.UpdateActionEvent;
import com.fanglin.fenhong.microbuyer.base.event.WifiConnectDealEvent;
import com.fanglin.fenhong.microbuyer.base.event.WifiUnconnectHintEvent;
import com.fanglin.fenhong.microbuyer.base.model.HomeNavigation;
import com.fanglin.fenhong.microbuyer.base.model.UpdateVersion;
import com.fanglin.fenhong.microbuyer.base.model.WSHomeNavigation;
import com.fanglin.fenhong.microbuyer.buyer.FragmentCategory;
import com.fanglin.fenhong.microbuyer.common.CartFragment;
import com.fanglin.fenhong.microbuyer.common.ConceptActivity;
import com.fanglin.fenhong.microbuyer.common.FindFragment;
import com.fanglin.fenhong.microbuyer.common.HomeFragment;
import com.fanglin.fenhong.microbuyer.common.LayoutUpdate;
import com.fanglin.fenhong.microbuyer.common.PersonalFragment;
import com.fanglin.fenhong.microbuyer.common.TalentFindFragment;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.DisableViewPager;
import com.fanglin.fhui.FragmentViewPagerAdapter;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.xingepush.XGPushContext;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * modify by lizhixin on 2015/12/24.
 * 底部TabHost添加可由后台控制功能
 */
public class MainActivity extends BaseFragmentActivity implements WSHomeNavigation.HomeNavigationModelCallBack, LayoutUpdate.ExitAppListener, ViewPager.OnPageChangeListener {

    @ViewInject(R.id.mpager)
    DisableViewPager mpager;
    @ViewInject(R.id.ivHome)
    ImageView ivHome;
    @ViewInject(R.id.ivCategory)
    ImageView ivCategory;
    @ViewInject(R.id.ivFind)
    ImageView ivFind;
    @ViewInject(R.id.ivCart)
    ImageView ivCart;
    @ViewInject(R.id.ivPersonal)
    ImageView ivPersonal;
    @ViewInject(R.id.tvCartNum)
    TextView tvCartNum;
    @ViewInject(R.id.RHome)
    RelativeLayout RHome;

    FragmentViewPagerAdapter adapter;//viewpager适配器

    String advurl;
    FHApp fhApp;
    HomeNavigation navHome, navCategory, navFind, navCart, navPersonal;

    private GestureDetector mDetector;//手势监听 -- lizhixin
    private FHwifiHintPopWindow wifiHintPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        //更新事件EventBus
        EventBus.getDefault().register(this);

        initView();
    }

    private void initView() {

        fhApp = (FHApp) getApplication();

        XGPushContext.init(getApplicationContext(), member_id);

        String chanel = FHLib.getMetaData(this, "UMENG_CHANNEL");
        String deviceId = FHLib.getDeviceID(mContext);
        /** 初始化常量单例*/
        VarInstance.init(this, chanel, deviceId);

        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        List frags = getFragmentList();

        adapter.setList(frags);
        mpager.setAdapter(adapter);
        mpager.setOffscreenPageLimit(5);
        mpager.addOnPageChangeListener(this);

        changeStatus(0);

        /** 如果是第一次 则启动引导页*/
        if (FHCache.getFirst(mContext)) {
            BaseFunc.gotoActivity(this, ConceptActivity.class, null);
        } else {
            if (getIntent().hasExtra("ADV")) {
                advurl = getIntent().getStringExtra("ADV");

            }
            BaseFunc.urlClick(this, advurl);
        }

        checkUpdate();

        /**
         * 请求导航接口
         */
        WSHomeNavigation wsHomeNavigation = new WSHomeNavigation();
        wsHomeNavigation.setNavigationModelCallBack(this);
        wsHomeNavigation.getList(2, 0);

        /**
         * 添加监听手势，以实现双击首页效果 -- lizhixin
         */
        mDetector = new GestureDetector(this, new HomeBtnGestureListener());
        RHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return onTouchEvent(event);
            }
        });

        /**
         * 首次启动时要检查是否有网
         */
        if (FHLib.isNetworkConnected(mContext) == 0) {//网络未连接
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new WifiUnconnectHintEvent());
                }
            }, 500);
        }
    }

    private List getFragmentList() {
        List frags = new ArrayList();
        HomeFragment homeFragment = new HomeFragment();
        FragmentCategory fragmentCategory = new FragmentCategory();
        CartFragment cartFragment = new CartFragment();
        PersonalFragment personalFragment = new PersonalFragment();

        frags.add(homeFragment);
        frags.add(fragmentCategory);
        boolean showDiscover = FHApp.getInstance().showDiscover();
        if (showDiscover) {
            TalentFindFragment talentFindFragment = new TalentFindFragment();
            frags.add(talentFindFragment);
        } else {
            FindFragment findFragment = new FindFragment();
            frags.add(findFragment);
        }

        frags.add(cartFragment);
        frags.add(personalFragment);
        return frags;
    }

    @Override
    public void onBackPressed() {
        if (mpager.getCurrentItem() != 0) {
            resetPage(0);
        } else {
            doubleClick();
        }
    }

    public void resetPage(int index) {
        mpager.setCurrentItem(index);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        /** 解析上一个页面 */
        if (getIntent().hasExtra("TOHOME")) {
            int index = getIntent().getIntExtra("TOHOME", 0);
            resetPage(index);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        BaseFunc.getCartNum(member);
    }

    /**
     * 双击退出函数
     */
    private static boolean isExit = false;

    private void doubleClick() {
        if (!isExit) {
            isExit = true; // 准备退出
            BaseFunc.showMsg(mContext, getString(R.string.hint_of_exit));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000);
        } else {
            //其实并不关闭当前APP（仍留在后台）
            finish();
        }
    }

    /**
     * 导航请求接口回调
     */
    @Override
    public void onNavigationSuccess(List<HomeNavigation> list) {
        navHome = getNavigationOfIndex(list, 0);
        navCategory = getNavigationOfIndex(list, 1);
        navFind = getNavigationOfIndex(list, 2);
        navCart = getNavigationOfIndex(list, 3);
        navPersonal = getNavigationOfIndex(list, 4);
        changeStatus(mpager.getCurrentItem());
    }

    @Override
    public void onNavigationError(String errcode) {
        navHome = null;
        navCategory = null;
        navFind = null;
        navCart = null;
        navPersonal = null;
        changeStatus(mpager.getCurrentItem());
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleUpdateAction(UpdateActionEvent event) {
        if (event != null) {
            showUpdate(event.getUpdateVersion());
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleCartAction(CartActionEvent event) {
        if (event != null) {
            if (event.getCartAction() != null) {
                BaseFunc.setCartNumofTv(tvCartNum, event.getCartAction().getNum());
            } else {
                BaseFunc.setCartNumofTv(tvCartNum, 0);
            }
        }
    }

    /**
     * 無網絡提示
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleWifiConn(WifiUnconnectHintEvent event) {
        if (wifiHintPopWindow == null) {
            wifiHintPopWindow = BaseFunc.showWifiUnconnectHint(MainActivity.this, mpager, null);
        } else {
            BaseFunc.showWifiUnconnectHint(MainActivity.this, mpager, wifiHintPopWindow);
        }
    }

    /**
     * n
     * 有网络后的处理
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleWifiEnv(WifiConnectDealEvent event) {
        if (wifiHintPopWindow != null && wifiHintPopWindow.isShowing()) {
            wifiHintPopWindow.dismiss();
        }
    }

    /**
     * 更新提示弹框
     *
     * @param updateVersion 更新信息
     */
    private void showUpdate(UpdateVersion updateVersion) {
        if (updateVersion != null) {
            if (mContext == null) return;
            if (FHLib.isAppOnForeground(mContext)) {
                PackageInfo pkginfo = FHLib.getPkgInfo(mContext);
                if (pkginfo == null) return;
                int vercode = pkginfo.versionCode;
                if (updateVersion.version_code > vercode) {
                    LayoutUpdate layoutUpdate = new LayoutUpdate(mContext, updateVersion);
                    layoutUpdate.setExitAppListener(this);
                    layoutUpdate.show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //更新事件EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * 退出App
     */
    @Override
    public void onExitApp() {
        finish();
    }

    private void checkUpdate() {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        UpdateVersion uv = new Gson().fromJson(data, UpdateVersion.class);
                        //通过EventBus通知更新
                        EventBus.getDefault().post(new UpdateActionEvent(uv));
                    } catch (Exception e) {
                        /** ------ */
                    }
                }
            }
        }).getLastVersion();
    }

    private void changeStatus(int index) {
        bindNaviViewWithData(ivHome, navHome, false);
        bindNaviViewWithData(ivCategory, navCategory, false);
        bindNaviViewWithData(ivFind, navFind, false);
        bindNaviViewWithData(ivCart, navCart, false);
        bindNaviViewWithData(ivPersonal, navPersonal, false);
        switch (index) {
            case 0:
                bindNaviViewWithData(ivHome, navHome, true);
                break;
            case 1:
                bindNaviViewWithData(ivCategory, navCategory, true);
                break;
            case 2:
                bindNaviViewWithData(ivFind, navFind, true);
                break;
            case 3:
                bindNaviViewWithData(ivCart, navCart, true);
                break;
            case 4:
                bindNaviViewWithData(ivPersonal, navPersonal, true);
                break;
            default:
                bindNaviViewWithData(ivHome, navHome, true);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeStatus(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(value = {R.id.RHome, R.id.RCategory, R.id.RFind, R.id.RCart, R.id.RPersonal})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.RHome:
                mpager.setCurrentItem(0);
                break;
            case R.id.RCategory:
                mpager.setCurrentItem(1);
                break;
            case R.id.RFind:
                mpager.setCurrentItem(2);
                break;
            case R.id.RCart:
                mpager.setCurrentItem(3);
                break;
            case R.id.RPersonal:
                mpager.setCurrentItem(4);
                break;
        }
    }

    private void bindNaviViewWithData(ImageView imageView, HomeNavigation navData, boolean isSelected) {
        if (imageView == null) return;
        if (navData != null) {
            new FHImageViewUtil(imageView).setImageURI(isSelected ? navData.nav_icon_selected : navData.nav_icon, FHImageViewUtil.SHOWTYPE.DEFAULT);
        } else {
            imageView.setSelected(isSelected);
        }
    }

    private HomeNavigation getNavigationOfIndex(List<HomeNavigation> navList, int index) {
        if (navList == null || navList.size() == 0) return null;
        for (int i = 0; i < navList.size(); i++) {
            if (navList.get(i).nav_sort == (index + 1))
                return navList.get(i);
        }
        return null;
    }

    class HomeBtnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            EventBus.getDefault().post(new HomeBtnDoubleClickEvent());
            return super.onDoubleTap(e);
        }
    }

    @Override
    public void finish() {
        super.finish();
        fhApp.hasPopUp = false;
        EventBus.getDefault().post(new LoadingFinishEvent());
    }
}
