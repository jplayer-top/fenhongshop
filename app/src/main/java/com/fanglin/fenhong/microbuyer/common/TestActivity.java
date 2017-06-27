package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.fanglin.fenhong.microbuyer.R;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.CartGoodsComparator;
import com.fanglin.fenhong.microbuyer.base.model.GoodsinCart;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.ResultModel;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-10.
 */
public class TestActivity extends AppCompatActivity {
    Context mContext;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;

    @ViewInject(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ViewUtils.inject(this);
        initView();
    }


    private void initView() {
        mContext = this;
        View view = multiStateView.getView(MultiStateView.VIEW_STATE_ERROR);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }

        webView.setWebViewClient(new AClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.loadUrl("http://jdpaydemo.jd.com/payStart.htm");


        List<GoodsinCart> list = new ArrayList<>();
        GoodsinCart goods = new GoodsinCart();
        goods.goods_name = "C";
        goods.add_time = 123;
        list.add(goods);

        goods = new GoodsinCart();
        goods.goods_name = "B1";
        goods.add_time = 12;
        list.add(goods);

        goods = new GoodsinCart();
        goods.goods_name = "B33";
        goods.add_time = 3342;
        list.add(goods);

        goods = new GoodsinCart();
        goods.goods_name = "BA";
        goods.add_time = 321;
        list.add(goods);

        goods = new GoodsinCart();
        goods.goods_name = "B333";
        goods.add_time = 3456;
        list.add(goods);

        goods = new GoodsinCart();
        goods.goods_name = "A";
        goods.add_time = 666;
        list.add(goods);

        FHLog.d("Plucky", "before:" + new Gson().toJson(list));

        Object[] arr = list.toArray();
        Arrays.sort(arr, new CartGoodsComparator(true));
        FHLog.d("Plucky", "after:" + new Gson().toJson(arr));
    }

    class AClient extends WebViewClient {
        /**
         * @param view v
         * @param url  u
         * @return true 则拦截点击事件 false 正常跳转
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            /**
             * 所以可以根据这个原理进行一些操作  比如监听到url中包含!login
             * 跳转至登录界面 等等 这是wap页面与原生页面的交互方式之一
             */
            return super.shouldOverrideUrlLoading(view, url);
        }
    }


    public static void JDPay(final Context context) {
        Member member = new Member();
        member.member_id = "328386";
        member.token = "0e527e9d56e13550bcdbe2b0b9bf38c2";

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    ResultModel resultModel = getFHReuslt(data);
                    if (resultModel != null && !resultModel.errorEmpty()) {
                        //如果返回了
                        if (TextUtils.isEmpty(resultModel.getMsg())) {
                            BaseFunc.showMsg(context, "支付失败!");
                        } else {
                            BaseFunc.showMsg(context, resultModel.getMsg());
                        }
                    } else {
                        Intent intent = new Intent(context, FHBrowserActivity.class);
                        intent.putExtra("VAL", data);
                        intent.putExtra("TYPE", true);
                        context.startActivity(intent);
                    }
                } else {
                    BaseFunc.showMsg(context, "支付失败!");
                }
            }
        }).setNormalRequest(true).jdPay(member, "700524329115490386");
    }

    private static ResultModel getFHReuslt(String data) {
        try {
            return new Gson().fromJson(data, ResultModel.class);
        } catch (Exception e) {
            return null;
        }
    }

}
