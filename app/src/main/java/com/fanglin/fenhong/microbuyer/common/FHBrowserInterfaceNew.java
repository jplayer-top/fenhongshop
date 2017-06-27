package com.fanglin.fenhong.microbuyer.common;

import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alipay.Base64;
import com.echo.QrCodeUtils;
import com.fanglin.fenhong.mapandlocate.baiduloc.BaiduLocateUtil;
import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.mapandlocate.baiduloc.LocMsg;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baselib.FHDownloadUtils;
import com.fanglin.fenhong.microbuyer.base.baselib.VarInstance;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.model.AppInfo;
import com.fanglin.fenhong.microbuyer.base.model.DownloadInfo;
import com.fanglin.fenhong.microbuyer.base.model.FHAjaxData;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.PayEntity;
import com.fanglin.fenhong.microbuyer.base.model.PayInfo;
import com.fanglin.fenhong.microbuyer.base.model.PayResult;
import com.fanglin.fenhong.microbuyer.base.model.QrcodeInfo;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.ShareRecord;
import com.fanglin.fenhong.microbuyer.base.model.ShareRespon;
import com.fanglin.fenhong.microbuyer.base.model.UpdateVersion;
import com.fanglin.fenhong.microbuyer.base.model.UploadImgInfo;
import com.fanglin.fenhong.microbuyer.common.listener.FHBrowserListener;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhlib.ypyun.api.Base64Coder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.sharesdk.fhshare.FHShareDialog;
import cn.sharesdk.fhshare.model.FHShareData;
import cn.sharesdk.fhshare.model.FHShareItem;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/16.
 * 功能描述: 新交互方式代理方法
 */
class FHBrowserInterfaceNew {

    static final String EXECUTE_JS_FMT = "javascript:onEECrossResult('%1$s','%2$s')";
    static final int REQZLIB = 0x001;
    static final int REQPAYCODE = 0x002;
    static final int REQLOGIN = 0x003;
    static final int REQPHOTO = 0x004;

    private BaseFragmentActivity mContext;
    private FHBrowserListener listener;


    FHBrowserInterfaceNew(BaseFragmentActivity mContext, FHBrowserListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    void callNative(Uri aURI) {
        if (aURI == null) return;
        String func = aURI.getHost();
        FHLog.d("Plucky", func + ":" + aURI.getQuery());
        if (TextUtils.equals("doLocate", func)) {
            doLocate();
        }
        if (TextUtils.equals("showMap", func)) {
            showMap(aURI);
        }
        if (TextUtils.equals("gotoActivity", func)) {
            gotoActivity(aURI);
        }
        if (TextUtils.equals("shareByApp", func)) {
            shareByApp(aURI);
        }
        if (TextUtils.equals("getMemberInfo", func)) {
            getMemberInfo();
        }
        if (TextUtils.equals("toast", func)) {
            toast(aURI);
        }
        if (TextUtils.equals("gotoLogin4Result", func)) {
            gotoLogin4Result();
        }
        if (TextUtils.equals("getAppInfo", func)) {
            getAppInfo();
        }
        if (TextUtils.equals("pay", func)) {
            pay(aURI);
        }
        if (TextUtils.equals("logOff", func)) {
            logOff();
        }
        if (TextUtils.equals("enableShareButton", func)) {
            enableShareButton(aURI);
        }
        if (TextUtils.equals("copy", func)) {
            copy(aURI);
        }
        if (TextUtils.equals("download", func)) {
            download(aURI);
        }
        if (TextUtils.equals("gotoHome", func)) {
            gotoHome(aURI);
        }
        if (TextUtils.equals("reload", func)) {
            reload();
        }
        if (TextUtils.equals("finish", func)) {
            finish();
        }
        if (TextUtils.equals("ajax", func)) {
            ajax(aURI);
        }
        if (TextUtils.equals("genQrcode", func)) {
            genQrcode(aURI);
        }
        if (TextUtils.equals("qrScan", func)) {
            qrScan();
        }
        if (TextUtils.equals("openBrower", func)) {
            openBrower(aURI);
        }
        if (TextUtils.equals("uploadImg", func)) {
            uploadImg(aURI);
        }
        if (TextUtils.equals("sendJDPayResult", func)) {
            sendJDPayResult(aURI);
        }
        if (TextUtils.equals("showUpdate", func)) {
            showUpdate(aURI);
        }
        if (TextUtils.equals("openPictures", func)) {
            openPictures(aURI);
        }
        if (TextUtils.equals("updateMemberInfo", func)) {
            updateMemberInfo(aURI);
        }
        if (TextUtils.equals("shareAppType", func)) {
            shareAppType(aURI);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.obj != null ? msg.obj.toString() : "";
            if (listener != null) {
                listener.onEECrossResult("shareAppType", data);
            }

            switch (msg.what) {
                case 0://成功
                    VarInstance.getInstance().showMsg(R.string.share_completed);
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        ShareRecord shareRecord = new ShareRecord();
                        shareRecord.member = mContext.member;
                        /** 所有信息生成完毕时 */
                        shareRecord.title = bundle.getString("title");
                        shareRecord.content = bundle.getString("content");
                        shareRecord.img = bundle.getString("img");
                        shareRecord.rurl = bundle.getString("rurl");
                        if (bundle.getBoolean("record")) {
                            shareRecord.share();
                        }
                    }
                    break;
                case 1://取消
                    VarInstance.getInstance().showMsg(R.string.share_canceled);
                    break;
                case 2://失败
                    VarInstance.getInstance().showMsg(R.string.share_failed);
                    break;
            }
        }
    };


    /**
     * 方法 零：单个分享,静态可点击图片的分享
     *
     * @param aURI Uri
     */
    private void shareAppType(Uri aURI) {
        final ShareRespon shareRes = new ShareRespon();
        final String shareJson = getUriQueryByKey(aURI, "shareJson");
        final FHShareData fhShareData = ShareData.shareByItems(mContext.member, shareJson);
        if (fhShareData == null) {
            shareRes.error = 4;
            shareRes.result = "数据解析失败";
            if (listener != null) {
                listener.onEECrossResult("shareAppType", shareRes.getString());
            }
            return;
        }
        PlatformActionListener actionListener = new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                shareRes.error = 0;
                shareRes.result = fhShareData.platType;
                shareRes.data = platform.getName();

                //分享成功 除了回调给WAP页结果还需要 往后台提交数据（API）
                Message message = new Message();
                message.what = 0;
                message.obj = shareRes.getString();
                Bundle bundle = new Bundle();
                bundle.putString("title", fhShareData.title);
                bundle.putString("content", fhShareData.content);
                bundle.putString("img", fhShareData.imgs);
                bundle.putString("rurl", fhShareData.url);
                bundle.putBoolean("record", fhShareData.record);
                message.setData(bundle);
                //发送数据
                handler.sendMessage(message);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                shareRes.error = 2;
                shareRes.result = fhShareData.platType;//分享渠道标识
                shareRes.data = platform.getName();//渠道名称
                //分享失败
                Message message = new Message();
                message.what = 2;
                message.obj = shareRes.getString();
                handler.sendMessage(message);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                shareRes.error = 1;
                shareRes.result = fhShareData.platType;
                shareRes.data = platform.getName();
                //取消分享 必须在子线程中执行
                Message message = new Message();
                message.what = 1;
                message.obj = shareRes.getString();
                handler.sendMessage(message);
            }
        };
        FHShareItem.shareAppType(mContext, fhShareData, actionListener, null, fhShareData.platType);
    }

    /**
     * 方法一：定位
     */
    private void doLocate() {
        if (mContext == null) return;
        BaiduLocateUtil.getinstance(mContext).start();
        BaiduLocateUtil.getinstance(mContext).setCallBack(new BaiduLocateUtil.LocationCallBack() {
            @Override
            public void onChange(FHLocation location) {
                String result = new Gson().toJson(location);
                if (listener != null) {
                    listener.onEECrossResult("doLocate", result);
                }
                BaiduLocateUtil.getinstance(mContext).stop();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 方法二：在地图上显示经纬度
     *
     * @param aURI Uri
     */
    private void showMap(Uri aURI) {
        if (mContext == null) return;
        LocMsg lmsg = new LocMsg();
        lmsg.setmLatByString(getUriQueryByKey(aURI, "mLat"));
        lmsg.setmLngByString(getUriQueryByKey(aURI, "mLng"));
        lmsg.mPoi = getUriQueryByKey(aURI, "mPoi");
        BaiduLocateUtil.getinstance(mContext).ShowMapLocation(lmsg);
    }

    /**
     * 方法三：原生页面跳转
     *
     * @param aURI Uri
     */
    private void gotoActivity(Uri aURI) {
        if (mContext == null) return;
        try {
            Class cls = Class.forName(getUriQueryByKey(aURI, "controller"));
            String json = getUriQueryByKey(aURI, "pageData");
            //pageData为前端经过Base64加密的json字符串
            json = Base64Coder.safeDecodeString(json);
            if (FHLib.isJson(json)) {
                Bundle bundle = BaseFunc.getBundleByJson(json);
                BaseFunc.gotoActivityBundle(mContext, cls, bundle);
            } else {
                BaseFunc.gotoActivity(mContext, cls, json);
            }
        } catch (Exception e) {
            FHLog.d("Plucky", e.getMessage());
        }
    }

    /**
     * 方法四：调取APP的分享功能
     *
     * @param aURI Uri
     */
    private void shareByApp(Uri aURI) {
        final ShareRespon shareRes = new ShareRespon();
        String shareJson = getUriQueryByKey(aURI, "shareJson");
        if (!TextUtils.isEmpty(shareJson)) {
            shareJson = Base64Coder.safeDecodeString(shareJson);
            if (!TextUtils.isEmpty(shareJson)) {
                final ShareData shareData = new Gson().fromJson(shareJson, ShareData.class);
                if (shareData != null) {
                    ShareData.FHBrowserShareListener shareListener = new ShareData.FHBrowserShareListener() {
                        @Override
                        public void onBrowserShare(int error, int shareType, String platform) {
                            shareRes.error = error;
                            shareRes.result = shareType;
                            shareRes.data = platform;
                            if (listener != null) {
                                listener.onEECrossResult("shareByApp", shareRes.getString());
                            }
                        }
                    };
                    shareData.show = false;
                    final FHShareDialog shareDialog;
                    if (shareData.storeInfo != null) {
                        FHShareItem item = new FHShareItem();
                        item.setName("二维码");
                        item.setIcon(R.drawable.fhshare_qrcode);
                        item.setListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LayoutShopQrcode layoutShopQrcode = new LayoutShopQrcode(mContext);
                                layoutShopQrcode.refreshView(shareData.storeInfo);
                                layoutShopQrcode.show();
                            }
                        });
                        shareDialog = ShareData.shareByItems(mContext, shareData, null, shareListener, item);
                    } else {
                        shareDialog = ShareData.shareByItems(mContext, shareData, null, shareListener);
                    }
                    // 启动分享GUI
                    if (shareDialog != null) {
                        shareDialog.show();
                    }
                } else {
                    shareRes.error = 4;
                    shareRes.result = "数据解析失败";
                    if (listener != null) {
                        listener.onEECrossResult("shareByApp", shareRes.getString());
                    }
                }
            } else {
                shareRes.error = 4;
                shareRes.result = "数据异常: Base64解析失败";
                if (listener != null) {
                    listener.onEECrossResult("shareByApp", shareRes.getString());
                }
            }
        } else {
            shareRes.error = 4;
            shareRes.result = "数据异常：shareJson为空";
            if (listener != null) {
                listener.onEECrossResult("shareByApp", shareRes.getString());
            }
        }
    }

    /**
     * 方法五：获取会员信息
     */
    private void getMemberInfo() {
        if (mContext == null) return;
        if (listener != null) {
            String info = FHCache.getMemberStringFromCache(mContext);
            listener.onEECrossResult("getMemberInfo", info);
        }
    }

    /**
     * 方法六：轻提示
     *
     * @param aURI Uri
     */
    private void toast(Uri aURI) {
        if (mContext == null) return;
        String content = getUriQueryByKey(aURI, "content");
        BaseFunc.showMsg(mContext, content);
    }

    /**
     * 方法七：带回调信息登录
     */
    private void gotoLogin4Result() {
        if (mContext == null) return;
        BaseFunc.gotoLogin(mContext, REQLOGIN);
    }

    /**
     * 方法八：获取APP信息
     */
    private void getAppInfo() {
        if (mContext == null) return;
        AppInfo appInfo = new AppInfo(mContext);
        appInfo.tFlag = null;
        if (listener != null) {
            listener.onEECrossResult("getAppInfo", appInfo.getString());
        }
    }

    /**
     * 方法九：支付一波
     *
     * @param aURI Uri
     */
    private void pay(Uri aURI) {
        if (mContext == null) return;
        PayResult payResult = new PayResult();
        String payInfo = getUriQueryByKey(aURI, "payInfo");
        payInfo = Base64Coder.safeDecodeString(payInfo);
        PayInfo mPay;
        try {
            mPay = new Gson().fromJson(payInfo, PayInfo.class);
        } catch (Exception e) {
            mPay = null;
        }
        if (mPay != null) {
            PayEntity entity = new PayEntity();
            entity.pay_sn = mPay.getPaySn();//交易单编号
            entity.lastClassName = mContext.getClass().getName();
            entity.gc_area = mPay.getCountrySource();
            entity.pay_amount = mPay.getPayAmount();
            entity.payment_list = mPay.getPaymentList();
            BaseFunc.gotoPayActivity(mContext, entity, REQPAYCODE);
        } else {
            payResult.msg = "支付信息异常";
            if (listener != null) {
                listener.onEECrossResult("pay", payResult.getString());
            }
        }
    }

    /**
     * 方法十：退出登录
     */
    private void logOff() {
        if (mContext == null) return;
        BaseFunc.gotoLogin(mContext);
    }

    /**
     * 方法十一：解锁分享按钮
     *
     * @param aURI Uri
     */
    private void enableShareButton(Uri aURI) {
        if (mContext == null) return;
        ShareData shareData = null;
        String shareJson = getUriQueryByKey(aURI, "shareJson");
        if (!TextUtils.isEmpty(shareJson)) {
            shareJson = Base64Coder.safeDecodeString(shareJson);
            if (!TextUtils.isEmpty(shareJson)) {
                shareData = new Gson().fromJson(shareJson, ShareData.class);
            }
        }
        if (listener != null) {
            listener.doEnableShareButtonAction(shareData);
        }
    }

    /**
     * 方法十二：复制文本
     *
     * @param aURI Uri
     */
    private void copy(Uri aURI) {
        if (mContext == null) return;
        String content = getUriQueryByKey(aURI, "content");
        if (!TextUtils.isEmpty(content)) {
            FHLib.copy(mContext, content);
            BaseFunc.showMsg(mContext, "复制成功");
        } else {
            BaseFunc.showMsg(mContext, "复制失败");
        }
    }

    /**
     * 方法十三：下载文件（主要下载图片）
     *
     * @param aURI Uri
     */
    private void download(Uri aURI) {
        if (mContext == null) return;
        String urls = getUriQueryByKey(aURI, "urls");
        if (!TextUtils.isEmpty(urls)) {
            urls = Base64Coder.safeDecodeString(urls);
            if (!TextUtils.isEmpty(urls)) {
                DownloadInfo downloadInfo;
                try {
                    downloadInfo = new Gson().fromJson(urls, DownloadInfo.class);
                } catch (Exception e) {
                    downloadInfo = null;
                }
                if (downloadInfo != null) {
                    String[] list = downloadInfo.getArrayList();
                    if (list != null && list.length > 0) {
                        new FHDownloadUtils(list).download();
                    }
                }
            }
        }
    }

    /**
     * 方法十四：回到首页
     *
     * @param aURI Uri index：0-首页 1-分类 2-发现 3-购物车 4-我的
     */
    private void gotoHome(Uri aURI) {
        if (mContext == null) return;
        int index = 0;
        String indexStr = getUriQueryByKey(aURI, "index");
        if (BaseFunc.isInteger(indexStr)) {
            index = Integer.valueOf(indexStr);
        }
        if (listener != null) {
            listener.doGoHomeAction(index);
        }
    }

    /**
     * 方法十五：重新加载
     */
    private void reload() {
        if (listener != null) {
            listener.doReloadAction();
        }
    }

    /**
     * 方法十六：关闭当前页面
     */
    private void finish() {
        if (listener != null) {
            listener.doFinishAction();
        }
    }

    /**
     * 方法十七：内置浏览器API请求功能（ajax）
     *
     * @param aURI Uri
     */
    private void ajax(Uri aURI) {
        if (mContext == null) return;
        String ajaxData = getUriQueryByKey(aURI, "ajaxData");
        if (!TextUtils.isEmpty(ajaxData)) {
            ajaxData = Base64Coder.safeDecodeString(ajaxData);
            if (!TextUtils.isEmpty(ajaxData)) {
                FHAjaxData fhAjaxData;
                try {
                    fhAjaxData = new Gson().fromJson(ajaxData, FHAjaxData.class);
                } catch (Exception e) {
                    fhAjaxData = null;
                }

                if (fhAjaxData == null) {
                    if (listener != null) {
                        listener.onEECrossResult("ajax", "数据解析异常");
                    }
                    return;
                }
                APIUtil apiUtil = new APIUtil();
                apiUtil.normalRequest = true;
                apiUtil.setCallBack(new APIUtil.FHAPICallBack() {
                    @Override
                    public void onStart(String data) {

                    }

                    @Override
                    public void onEnd(boolean isSuccess, String data) {
                        if (listener != null) {
                            listener.onEECrossResult("ajax", data);
                        }
                    }
                });
                String method = fhAjaxData.getMethod();
                if (TextUtils.isEmpty(method)) {
                    if (listener != null) {
                        listener.onEECrossResult("ajax", "数据异常:非法method");
                    }
                    return;
                }

                String url = fhAjaxData.getUrl();
                if (TextUtils.isEmpty(url)) {
                    if (listener != null) {
                        listener.onEECrossResult("ajax", "数据异常:非法url");
                    }
                    return;
                }

                HttpRequest.HttpMethod httpMethod;
                if (TextUtils.equals("POST", method.toUpperCase())) {
                    httpMethod = HttpRequest.HttpMethod.POST;
                } else {
                    httpMethod = HttpRequest.HttpMethod.GET;
                }
                RequestParams reqParams = new RequestParams();
                JSONObject jsonData = fhAjaxData.getData();
                if (jsonData != null && jsonData.length() > 0) {
                    Iterator<String> it = jsonData.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        String obj;
                        try {
                            obj = jsonData.getString(key);
                        } catch (Exception e) {
                            obj = "";
                        }
                        reqParams.addBodyParameter(key, obj);
                    }
                }
                apiUtil.execute(httpMethod, url, reqParams);
            }
        } else {
            if (listener != null) {
                listener.onEECrossResult("ajax", "数据异常：ajaxData不能为空!");
            }
        }
    }

    /**
     * 方法十八：生成二维码
     *
     * @param aURI Uri
     */
    private void genQrcode(Uri aURI) {
        if (mContext == null) return;
        String qrResult = "";
        String qrParams = getUriQueryByKey(aURI, "params");
        if (!TextUtils.isEmpty(qrParams)) {
            qrParams = Base64Coder.safeDecodeString(qrParams);
        }
        if (!TextUtils.isEmpty(qrParams)) {
            QrcodeInfo info;
            try {
                info = new Gson().fromJson(qrParams, QrcodeInfo.class);
            } catch (Exception e) {
                info = null;
            }
            if (info != null) {
                int width = info.getWidth();
                int height = info.getHeight();
                if (width <= 0) width = 200;
                if (height <= 0) height = 200;
                String preFix = info.getPreFix();
                Bitmap bitmap = QrCodeUtils.createQrImage(info.getQrtext(), width, height);
                if (bitmap != null) {
                    byte[] bytes = FHLib.bitmap2ByteArray(bitmap);
                    String base64 = Base64.encode(bytes);
                    if (TextUtils.isEmpty(preFix)) {
                        preFix = "data:image/png;base64,";
                    }
                    qrResult = preFix + base64;
                }
            }
        }

        if (listener != null) {
            listener.onEECrossResult("genQrcode", qrResult);
        }
    }

    /*********** Android端额外功能 *************/

    /**
     * 方法十九：二维码扫描功能
     */
    private void qrScan() {
        if (mContext == null) return;
        QrCodeUtils.qrScan4Result(mContext, REQZLIB);
    }

    /**
     * 方法二十：打开系统浏览器
     *
     * @param aURI Uri
     */
    private void openBrower(Uri aURI) {
        if (mContext == null) return;
        String url = getUriQueryByKey(aURI, "url");
        if (!TextUtils.isEmpty(url)) {
            FHLib.openBrowser(mContext, url);
        }
    }

    /**
     * 方法二十一：上传图片（可按比例裁剪图片）
     *
     * @param aURI Uri
     */
    private void uploadImg(Uri aURI) {
        UploadImgInfo imgInfo = null;
        String uploadInfoStr = getUriQueryByKey(aURI, "uploadInfo");
        if (!TextUtils.isEmpty(uploadInfoStr)) {
            uploadInfoStr = Base64Coder.safeDecodeString(uploadInfoStr);
            try {
                imgInfo = new Gson().fromJson(uploadInfoStr, UploadImgInfo.class);
            } catch (Exception e) {
                imgInfo = null;
            }
        }
        if (listener != null) {
            listener.doUploadAction(imgInfo);
        }
    }

    /**
     * 方法二十二：京东支付WAP页 支付完成之后需要给浏览器回调的信息
     *
     * @param aURI Uri
     */
    private void sendJDPayResult(Uri aURI) {
        int error = 0;
        String msg = getUriQueryByKey(aURI, "msg");
        String err = getUriQueryByKey(aURI, "error");
        if (BaseFunc.isInteger(err)) {
            error = Integer.valueOf(err);
        }
        if (listener != null) {
            listener.doJDPayAction(error, msg);
        }
    }

    /**
     * 方法二十三：弹出更新提示框
     *
     * @param aURI Uri
     */
    private void showUpdate(Uri aURI) {
        if (mContext == null) return;
        String updateInfo = getUriQueryByKey(aURI, "updateInfo");
        if (TextUtils.isEmpty(updateInfo)) return;
        updateInfo = Base64Coder.safeDecodeString(updateInfo);
        if (TextUtils.isEmpty(updateInfo)) return;

        UpdateVersion updateVersion;
        try {
            updateVersion = new Gson().fromJson(updateInfo, UpdateVersion.class);
        } catch (Exception e) {
            updateVersion = null;
        }
        if (updateVersion != null) {
            PackageInfo pkgInfo = FHLib.getPkgInfo(mContext);
            int vercode = pkgInfo != null ? pkgInfo.versionCode : 0;
            if (!TextUtils.isEmpty(updateVersion.update_log) && !TextUtils.isEmpty(updateVersion.version_name) && BaseFunc.isValidUrl(updateVersion.app_url) && updateVersion.version_code > vercode) {
                LayoutUpdate layoutUpdate = new LayoutUpdate(mContext, updateVersion);
                layoutUpdate.show();
            }
        }
    }

    /**
     * 方法二十四：打开图片浏览器
     *
     * @param aURI Uri
     */
    private void openPictures(Uri aURI) {
        if (mContext == null) return;
        String imgStr = getUriQueryByKey(aURI, "imgs");
        if (TextUtils.isEmpty(imgStr)) return;
        imgStr = Base64Coder.safeDecodeString(imgStr);
        if (TextUtils.isEmpty(imgStr)) return;
        List<String> imgs;
        try {
            imgs = new Gson().fromJson(imgStr, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            imgs = null;
        }
        if (imgs != null && imgs.size() > 0) {
            FileUtils.BrowserOpenL(mContext, imgs, imgs.get(0));
        }
    }

    /**
     * 方法二十五：更新个人信息
     *
     * @param aURI Uri
     */
    private void updateMemberInfo(Uri aURI) {
        if (mContext == null) return;
        String memberInfo = getUriQueryByKey(aURI, "memberInfo");
        if (TextUtils.isEmpty(memberInfo)) return;
        memberInfo = Base64Coder.safeDecodeString(memberInfo);
        if (TextUtils.isEmpty(memberInfo)) return;
        Member member;
        try {
            member = new Gson().fromJson(memberInfo, Member.class);
        } catch (Exception e) {
            member = null;
        }
        if (member == null) return;
        FHCache.setMember(mContext, member);
    }

    /**
     * 解决base64字符串中+号变空格的BUG，APP做一层异常处理
     *
     * @param aURI Uri
     * @param key  String
     * @return String
     */
    private String getUriQueryByKey(Uri aURI, String key) {
        //getQueryParameter会存在BUG（base64特殊字符）
        //String val = aURI.getQueryParameter(key);
        HashMap<String, String> keyVal = getQuery(aURI.getQuery());
        if (keyVal != null) {
            return keyVal.get(key);
        } else {
            return null;
        }

    }


    /**
     * 获取URL中的key对应的只
     *
     * @param params m=1&n=2&a=111
     * @return HashMap
     */
    private HashMap<String, String> getQuery(String params) {
        if (TextUtils.isEmpty(params)) return null;
        HashMap<String, String> hash = new HashMap<>();
        String[] keyValArr = params.split("&");
        for (String keyVal : keyValArr) {
            String[] arr = keyVal.split("=");
            if (arr.length == 2) {
                //为了防止Base64中的 = 号的干扰
                String key = arr[0];
                String val = keyVal.substring(key.length() + 1);
                hash.put(key, val);
            }
        }
        return hash;
    }
}
