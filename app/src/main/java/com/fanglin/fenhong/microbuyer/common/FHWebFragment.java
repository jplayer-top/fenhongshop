package com.fanglin.fenhong.microbuyer.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.model.NativeUrlEntity;
import com.fanglin.fhlib.other.FHLog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public final class FHWebFragment extends BaseFragment {
    private static final String KEY_CONTENT = "FHWebFragment:Content";

    @ViewInject(R.id.progressBar)
    ProgressBar progressBar;
    @ViewInject(R.id.LDoing)
    LinearLayout LDoing;
    @ViewInject(R.id.webView)
    WebView webView;

    public static FHWebFragment newInstance(String url) {
        FHWebFragment fragment = new FHWebFragment();
        fragment.murl = url;

        return fragment;
    }

    private String murl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            murl = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_fhweb, container, false);
        ViewUtils.inject(this, layout);
        initView();
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, murl);
    }

    private void initView() {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setUserAgentString(ws.getUserAgentString() + "FHMall_Android");
        webView.setWebChromeClient(new FHChromeClient());
        webView.setWebViewClient(new FHClient());

        webView.loadUrl(murl);
        FHLog.d("Plucky", "Url of Web:" + murl);
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
        }
    }

    private class FHClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //如果是原生路由 则URL将不进行跳转 要进行原生跳转
            NativeUrlEntity urlEntity = BaseFunc.isNative(url);
            if (urlEntity != null) {
                BaseFunc.urlRedirect(getActivity(), urlEntity);
                return true;
            } else {
                return false;
            }
        }

        @OnClick(value = {R.id.LDoing})
        public void onViewClick(View view) {
            switch (view.getId()) {
                case R.id.LDoing:
                    webView.reload();
                    break;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            LDoing.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LDoing.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}
