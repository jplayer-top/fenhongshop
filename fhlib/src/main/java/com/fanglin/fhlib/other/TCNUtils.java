package com.fanglin.fhlib.other;

import android.os.AsyncTask;
import android.util.Patterns;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Plucky on 2015/7/27.
 * Desc: 新浪短网址生成器
 */
public class TCNUtils {
    public static int SUCCESS = 0;
    public static int ERROR = 1;
    public static int CANCEL = 2;

    public static final String KEY = "1927845752";
    public static final String APIURL = "http://api.t.sina.com.cn/short_url/shorten.json?source="
            + KEY;

    public TCNUtils() {

    }

    public void getShortUrl(String url) {
        /** 先进行格式检查*/
        if (url == null || !Patterns.WEB_URL.matcher(url).matches()) {
            if (mcb != null) {
                mcb.onEnd(ERROR, null);
            }
            return;
        }

        String orignalurl;
        try {
            orignalurl = URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
            orignalurl = url;
        }

        new task().execute(APIURL + "&url_long=" + orignalurl);
    }

    private class task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String res;
            try {
                res = ((JSONObject) new JSONArray(s).opt(0)).getString("url_short");
            } catch (Exception e) {
                res = null;
            }
            if (res != null) {
                if (mcb != null) {
                    mcb.onEnd(SUCCESS, res);
                }
            } else {
                if (mcb != null) {
                    mcb.onEnd(ERROR, null);
                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient hclient = new DefaultHttpClient();
                HttpGet hget = new HttpGet(params[0]);
                HttpResponse response = hclient.execute(hget);
                StatusLine statusLine = response.getStatusLine();
                //当且仅当200时才算成功!
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    byte[] b = EntityUtils.toByteArray(response.getEntity());
                    return new String(b, "UTF-8");
                } else {
                    return null;
                }

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (mcb != null) {
                mcb.onEnd(CANCEL, null);
            }
        }
    }

    public tcnCallBack mcb;

    public TCNUtils setCallBack(tcnCallBack cb) {
        this.mcb = cb;
        return this;
    }

    public interface tcnCallBack {
        void onEnd(int status, String data);
    }
}
