package com.fanglin.fenhong.microbuyer.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.echo.QrCodeUtils;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.NativeUrlEntity;
import com.fanglin.fenhong.microbuyer.base.model.PayResult;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.UploadImgInfo;
import com.fanglin.fenhong.microbuyer.common.listener.FHBrowserListener;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyFreeMainActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyPayDialog;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.sharesdk.fhshare.model.FHShareItem;

/**
 * Created by Plucky on 15-9-1.
 * 分红浏览器
 */
public class FHBrowserActivity extends BaseFragmentActivity implements FHBrowserListener, DutyPayDialog.DutyPayDialogListener, DutyPayDialog.DutyPayDialogCloseListener {

    @ViewInject(R.id.tvHead)
    TextView tvHead;
    @ViewInject(R.id.tvMore)
    TextView tvMore;
    @ViewInject(R.id.msgDot)
    View msgDot;
    @ViewInject(R.id.progressBar)
    ProgressBar progressBar;
    @ViewInject(R.id.LWEB)
    LinearLayout LWEB;

    WebView mweb;
    ShareData shareData;
    String murl;
    boolean isData;
    boolean qrLoadUrl = false;
    FHBrowserInterfaceNew interfaceNew;
    public static final int REQ_PAY = 110;
    DutyPayDialog payDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fhbrowser);
        ViewUtils.inject(this);

        //通过Application创建Webview也许可以提高性能
        mweb = new WebView(getApplicationContext());
        mweb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LWEB.removeAllViews();
        LWEB.addView(mweb);

        interfaceNew = new FHBrowserInterfaceNew(this, this);
        isData = getIntent().getBooleanExtra("TYPE", false);
        murl = getIntent().getStringExtra("VAL");
        FHLog.d("FHBrowserActivity", "isUrl?" + isData + " = " + murl);

        if (!isData) {
            murl = BaseFunc.formatHtml(murl);
            if (!BaseFunc.isValidUrl(murl)) {
                murl = BaseVar.APPDECLARE;
            }

            NativeUrlEntity urlEntity = BaseFunc.isNative(murl);
            if (urlEntity != null) {
                BaseFunc.urlRedirect(mContext, urlEntity);
                finish();
            }
        }

        initView();
    }

    private void initView() {
        msgDot.setVisibility(View.INVISIBLE);
        if (mweb == null) return;

        payDialog = new DutyPayDialog(this);
        payDialog.setDutyPayListener(this);
        payDialog.setDutyPayCloseListener(this);

        WebSettings ws = mweb.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setSaveFormData(false);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setDomStorageEnabled(true);
        ws.setUserAgentString(ws.getUserAgentString() + "FHMall_Android");
        mweb.setWebChromeClient(new FHChromeClient());
        mweb.setWebViewClient(new FHClient());


        //HTTP与HTTPS无法共存的问题
        if (Build.VERSION.SDK_INT >= 21) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //不事先加载图片 提高加载速度
        if (Build.VERSION.SDK_INT >= 19) {
            mweb.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mweb.getSettings().setLoadsImagesAutomatically(false);
        }

        if (isData) {
            mweb.loadData(murl, "text/html", "utf-8");
        } else {
            mweb.loadUrl(murl);
        }
    }

    @OnClick(value = {R.id.ivBack, R.id.tvClose,
            R.id.tvMore, R.id.ivMore})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvClose:
                finish();
                break;
            case R.id.tvMore:
                if (shareData != null) {
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
                        ShareData.fhShareByItems(this, shareData, null, item);
                    } else {
                        ShareData.fhShare(this, shareData, null);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSubmitClick(boolean isSuccess) {
        if (isSuccess) {
            BaseFunc.gotoActivity(mContext, DutyFreeMainActivity.class, null);
            finish();
        } else {
            BaseFunc.topUp(this, REQ_PAY);
        }
    }

    @Override
    public void onCloseClick(boolean isSuccess) {
        if (isSuccess) {
            //如果在浏览器页面购买成功了 点关闭的时候则关闭当前页面
            finish();
        }
    }


    private class FHChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if (newProgress == 100)
                progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (isData) {
                if (!TextUtils.isEmpty(title) && title.matches(getString(R.string.valid_char_reg))) {
                    tvHead.setText(title);
                }
            } else {
                tvHead.setText(title);
            }
        }

    }


    private class FHClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (handler != null) {
                handler.proceed();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            /**
             * 如果是原生路由 则URL将不进行跳转 要进行原生跳转 !goodsdetail
             */
            NativeUrlEntity urlEntity = BaseFunc.isNative(url);
            if (urlEntity != null) {
                boolean isFirst = TextUtils.equals(url, murl);
                BaseFunc.urlRedirect(mContext, urlEntity);
                if (isFirst) finish();
                return true;
            } else {
                /**
                 * fhmall 形式
                 */
                return jsCallNative(url);
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            FHLog.d("Plucky", "onLoadResource:" + url);
            /**
             *  在以下手机： Redmi-3S(Android-6.0.1 MIUI-8.1)
             *
             *  当页面跳转再点击返回键（执行webview.goback）的时候，shouldOverrideUrlLoading并不会执行，但这个方法会执行
             *  因而不会触发原生功能  Added By Plucky 2016-12-30 16:52
             */
            jsCallNative(url);

        }

        /**
         * js调用原生功能
         *
         * @param url String
         * @return true 调起原生功能 false 普通链接
         */
        private boolean jsCallNative(String url) {
            Uri aURI = BaseFunc.getURIByString(url);

            /** 如果是原生交互事件的话*/
            if (aURI != null && aURI.getScheme().toLowerCase().equals("fhmall")) {
                interfaceNew.callNative(aURI);
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mweb.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);

            /**dom渲染完成再自动加载图片*/
            if (!mweb.getSettings().getLoadsImagesAutomatically()) {
                mweb.getSettings().setLoadsImagesAutomatically(true);
            }
            //返回上个页面标题不变的问题
            if (view != null) {
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title) && title.matches(getString(R.string.valid_char_reg))) {
                    tvHead.setText(view.getTitle());
                }
            }
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mweb.loadUrl(BaseVar.FAILEDWEB);
            tvHead.setText("无法加载");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mweb.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mweb.pauseTimers();
    }

    /**
     * 这样就不会 WebView.destroy() called while still attached!
     */
    @Override
    protected void onDestroy() {
        if (mweb != null) {
            ViewGroup parent = (ViewGroup) mweb.getParent();
            if (parent != null) {
                parent.removeView(mweb);
            }
            mweb.setVisibility(View.GONE);
            mweb.removeAllViews();
            mweb.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            qrLoadUrl = true;//彩蛋的目的是爲了方便測試頁面
            QrCodeUtils.qrScan4Result(this, FHBrowserInterfaceNew.REQZLIB);

        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mweb.canGoBack()) {
            mweb.goBack();
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FHBrowserInterfaceNew.REQLOGIN) {
            //登录回调
            Member m = FHCache.getMember(this);
            if (m != null) {
                onEECrossResult("gotoLogin4Result", m.getString());
            }

        }

        if (requestCode == FHBrowserInterfaceNew.REQPHOTO) {
            //裁剪完图片需要上传至又拍云 上传成功之后回调给WAP
            if (data != null && data.hasExtra("VAL")) {
                Uri uri = data.getParcelableExtra("VAL");
                upload(uri);
            }
        }

        if (data == null || resultCode != RESULT_OK) return;
        switch (requestCode) {
            case FHBrowserInterfaceNew.REQZLIB:
                //扫描二维码
                String code = data.getStringExtra(QrCodeUtils.QRTEXT);
                if (BaseFunc.isValidUrl(code) && qrLoadUrl) {
                    NativeUrlEntity urlEntity = BaseFunc.isNative(code);
                    if (urlEntity != null) {
                        BaseFunc.urlRedirect(mContext, urlEntity);
                    } else {
                        mweb.loadUrl(code);
                    }
                    qrLoadUrl = false;
                } else {
                    BaseFunc.showMsg(mContext, code);
                }
                //扫描二维码回调给WAP
                onEECrossResult("qrScan", code);
                break;
            case FHBrowserInterfaceNew.REQPAYCODE:
                //支付信息回调给WAP
                PayResult payResult = new PayResult();
                payResult.payment = data.getIntExtra("payment", 0);
                payResult.result = data.getIntExtra("result", -2);
                onEECrossResult("pay", payResult.getString());
                break;
            case REQ_PAY:
                int result = data.getIntExtra("result", -1);
                payDialog.refreshView(result == 0);
                payDialog.show();
                break;
        }
    }


    /**
     * 又拍云上传图片
     *
     * @param uri Uri
     */
    private void upload(Uri uri) {
        String path = uri != null ? uri.getPath() : "";
        if (member == null || TextUtils.isEmpty(path)) return;
        final SpotsDialog dlg = BaseFunc.getLoadingDlg(this, getString(R.string.picture_uploading));
        new UpyunUploader(member.member_id).setUploadFile(path).setUpYunCallBack(new UpyunUploader.UpYunCallBack() {
            @Override
            public void startLoading() {
                dlg.show();
            }

            @Override
            public void endLoading(boolean isSuccess, String data) {
                dlg.dismiss();
                //图片上传成功之后需要回调给WAP
                onEECrossResult("uploadImg", data);
            }
        }).upload();
    }


    /**
     * APP原生功能返回值回调给WAP
     *
     * @param func 方法名
     * @param data 方法回传值 （暂时只支持一个值：可使用json字符串回传多个值）
     */
    @Override
    public void onEECrossResult(String func, String data) {
        String executeStr = String.format(FHBrowserInterfaceNew.EXECUTE_JS_FMT, func, data);
        mweb.loadUrl(executeStr);
    }

    /**
     * WAP调用当前Activity中的方法
     */
    @Override
    public void doEnableShareButtonAction(ShareData shareData) {
        if (shareData != null) {
            tvMore.setVisibility(View.VISIBLE);
            this.shareData = shareData;
            tvMore.setTypeface(iconfont);
            tvMore.setText(R.string.if_share);
        } else {
            tvMore.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void doGoHomeAction(int index) {
        BaseFunc.gotoHome(this, index);
        finish();
    }

    @Override
    public void doReloadAction() {
        mweb.reload();
    }

    @Override
    public void doFinishAction() {
        finish();
    }

    @Override
    public void doJDPayAction(int error, String msg) {
        Intent intent = new Intent();
        intent.putExtra("JD_ERROR", error);
        intent.putExtra("JD_MSG", msg);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void doUploadAction(UploadImgInfo info) {
        if (info != null && member != null) {
            BaseFunc.selectPictures(this, FHBrowserInterfaceNew.REQPHOTO, info.isCrop(), info.getOutputX(), info.getOutputY(), info.getAspectX(), info.getAspectY());
        }
    }

}
