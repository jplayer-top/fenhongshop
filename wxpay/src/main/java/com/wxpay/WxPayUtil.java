package com.wxpay;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

public class WxPayUtil {
    private WxOrderEntity entity;
    private Activity activity;

    private Map<String, String> step1Result;
    private PayReq step2Result;
    private WXConstants wxc;

    public WxPayUtil (Activity act, WxOrderEntity ent) {
        this.activity = act;
        this.entity = ent;
        step2Result = new PayReq ();
    }

    /** 第一步 生成预支付订单 */
    private void genPrepayId () {
        new GetPrepayIdTask ().execute ();
    }

	/** 第二步 生成APP微信支付参数 并唤起支付 */

    private void genPayReq () {
        if (step1Result == null) {
            return;
        }

        if (step2Result == null) {
            return;
        }

        step2Result.appId = wxc.APP_ID;
        step2Result.partnerId = wxc.MCH_ID;
        step2Result.prepayId = step1Result.get ("prepay_id");
        step2Result.packageValue = "Sign=WXPay";
        step2Result.nonceStr = genNonceStr ();
        step2Result.timeStamp = String.valueOf (genTimeStamp ());

        List<NameValuePair> signParams = new LinkedList<> ();
        signParams.add (new BasicNameValuePair ("appid", step2Result.appId));
        signParams.add (new BasicNameValuePair ("noncestr", step2Result.nonceStr));
        signParams.add (new BasicNameValuePair ("package", step2Result.packageValue));
        signParams.add (new BasicNameValuePair ("partnerid", step2Result.partnerId));
        signParams.add (new BasicNameValuePair ("prepayid", step2Result.prepayId));
        signParams.add (new BasicNameValuePair ("timestamp", step2Result.timeStamp));

        step2Result.sign = genAppSign (signParams);

        IWXAPI msgApi = WXAPIFactory.createWXAPI (activity, null);
        msgApi.registerApp (wxc.APP_ID);
        msgApi.sendReq (step2Result);
    }

    public void pay () {
        genPrepayId ();
    }

    /* 异步生成预付款订单 */
    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute () {
            if (mcb != null) {
                mcb.onStart ();
            }
        }

        @Override
        protected void onPostExecute (Map<String, String> result) {
            try {
                step1Result = result;
                if (TextUtils.equals ("FAIL", result.get ("return_code"))) {
                    //出现了重复提交 PrePayId 的情况
                    if (mcb != null) {
                        mcb.onPrePayErr (step1Result);
                    }
                    return;
                }
                if (TextUtils.equals ("FAIL", result.get ("result_code"))) {
                    //出现了重复提交 PrePayId 的情况
                    if (mcb != null) {
                        mcb.onPrePayErr (step1Result);
                    }
                    return;
                }

                //每次保存获取完 PrePayId 应该保存下来
                if (mcb != null) {
                    mcb.onPrePayOK (step1Result);
                }

                genPayReq ();

            } catch (Exception e) {
                Log.d ("GetPrepayId", e.getMessage ());
            }

        }

        @Override
        protected void onCancelled () {
            super.onCancelled ();
        }

        @Override
        protected Map<String, String> doInBackground (Void... params) {
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            String entity = genProductArgs ();
            byte[] buf = Util.httpPost (url, entity);
            String content = null;
            if (buf != null) {
                content = new String (buf);
            }
            return decodeXml (content);
        }
    }

    private WxPayCallBack mcb;

    public WxPayUtil setConstants (WXConstants constants) {
        this.wxc = constants;
        return this;
    }

    public WxPayUtil setCallBack (WxPayCallBack cb) {
        this.mcb = cb;
        return this;
    }

    public interface WxPayCallBack {
        void onStart ();

        void onPrePayErr (Map<String, String> result);

        void onPrePayOK (Map<String, String> result);
    }

    private String genProductArgs () {
        String xmlstring;
        try {
            String nonceStr = genNonceStr ();
            String body = new String (entity.getBody ().getBytes ("UTF-8"), DEFAULT_CHARSET);
            String price = formatPrice (entity.price);
            Log.e ("wxpay", "price:" + price);
            List<NameValuePair> packageParams = new LinkedList<> ();
            packageParams.add (new BasicNameValuePair ("appid", wxc.APP_ID));
            packageParams.add (new BasicNameValuePair ("body", body));
            packageParams.add (new BasicNameValuePair ("input_charset", "UTF-8"));
            packageParams.add (new BasicNameValuePair ("mch_id", wxc.MCH_ID));
            packageParams.add (new BasicNameValuePair ("nonce_str", nonceStr));
            packageParams.add (new BasicNameValuePair ("notify_url", wxc.getNOTIFY_URL ()));
            packageParams.add (new BasicNameValuePair ("out_trade_no", entity.tradeno));
            packageParams.add (new BasicNameValuePair ("spbill_create_ip", "127.0.0.1"));
            packageParams.add (new BasicNameValuePair ("total_fee", price));
            packageParams.add (new BasicNameValuePair ("trade_type", "APP"));
            String sign = genPackageSign (packageParams);
            packageParams.add (new BasicNameValuePair ("sign", sign));
            xmlstring = toXml (packageParams);
        } catch (Exception e) {
            xmlstring = null;
        }

        return xmlstring;
    }

    /** 其他私有变量及函数 */
    private static final String DEFAULT_CHARSET = "ISO8859-1";// "UTF-8";

    //微信支付 单位是分
    private String formatPrice (String price) {
        try {
            int i = (int) (Double.valueOf (price) * 1000) / 10;
            return String.valueOf (i);
        } catch (Exception e) {
            return price;
        }
    }

    private Map<String, String> decodeXml (String content) {
        try {
            Map<String, String> xml = new HashMap<> ();
            XmlPullParser parser = Xml.newPullParser ();
            parser.setInput (new StringReader (content));
            int event = parser.getEventType ();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName ();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        if (!"xml".equals (nodeName)) {
                            xml.put (nodeName, parser.nextText ());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next ();
            }
            return xml;
        } catch (Exception e) {
            Log.e ("orion", e.toString ());
        }
        return null;

    }

    private String toXml (List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder ();
        sb.append ("<xml>");
        for (int i = 0; i < params.size (); i++) {
            sb.append ("<" + params.get (i).getName () + ">");

            sb.append (params.get (i).getValue ());
            sb.append ("</" + params.get (i).getName () + ">");
        }
        sb.append ("</xml>");

        String result = sb.toString ();
        return result;
    }

    private String genNonceStr () {
        Random random = new Random ();
        return MD5.getMessageDigest (String.valueOf (random.nextInt (10000)).getBytes ()).toUpperCase ();
    }

    private long genTimeStamp () {
        return System.currentTimeMillis () / 1000;
    }

    private String genPackageSign (List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder ();
        for (int i = 0; i < params.size (); i++) {
            sb.append (params.get (i).getName ());
            sb.append ('=');
            sb.append (params.get (i).getValue ());
            sb.append ('&');
        }
        sb.append ("key=");
        sb.append (wxc.API_KEY);

		/** fixed By Plucky */
        return MD5Util.MD5Encode (sb.toString (), DEFAULT_CHARSET).toUpperCase ();
    }

    private String genAppSign (List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder ();

        for (int i = 0; i < params.size (); i++) {
            sb.append (params.get (i).getName ());
            sb.append ('=');
            sb.append (params.get (i).getValue ());
            sb.append ('&');
        }
        sb.append ("key=");
        sb.append (wxc.API_KEY);

		/** fixed By Plucky */
        return MD5Util.MD5Encode (sb.toString (), DEFAULT_CHARSET).toUpperCase ();
    }

}
