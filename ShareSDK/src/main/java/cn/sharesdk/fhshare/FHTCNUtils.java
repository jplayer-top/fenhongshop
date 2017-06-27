package cn.sharesdk.fhshare;

import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
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
public class FHTCNUtils {
    public static int SUCCESS = 0;
    public static int ERROR = 1;
    public static int CANCEL = 2;

    public static final String KEY = "1927845752";
    public static final String APIURL = "http://api.t.sina.com.cn/short_url/shorten.json?source=" + KEY;
    private String orignalurl;

    public FHTCNUtils() {

    }

    public void getShortUrl(String url) {
        /**先进行格式检查*/
        if (url == null || !Patterns.WEB_URL.matcher(url).matches()) {
            if (mcb != null) {
                mcb.onEnd(ERROR, null);
            }
            return;
        }

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
            if (mcb != null) {
                mcb.onStart();
            }
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
                    mcb.onEnd(ERROR, orignalurl);//如果生成失败则返回原链接
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

    /**
     * 获取剪切板管理器 API 11之后
     */
    public static android.content.ClipboardManager getClipBoardManagerOfContent(Context context) {
        return (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 获取剪切板管理器 API 11之前
     */
    public static android.text.ClipboardManager getClipBoardManagerOfText(Context context) {
        return (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 复制内容
     */
    public static void copy(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            if (Build.VERSION.SDK_INT > 11) {
                getClipBoardManagerOfContent(context).setPrimaryClip(ClipData.newPlainText(null, str.trim()));
            } else {
                getClipBoardManagerOfText(context).setText(str.trim());
            }
        }
    }

    /**
     * 粘贴接剪切板中的内容
     */
    public static String paste(Context context) {
        if (Build.VERSION.SDK_INT > 11) {
            return getClipBoardManagerOfContent(context).getPrimaryClip().toString();
        } else {
            return getClipBoardManagerOfText(context).getText().toString();
        }
    }

    public tcnCallBack mcb;

    public FHTCNUtils setCallBack(tcnCallBack cb) {
        this.mcb = cb;
        return this;
    }

    public interface tcnCallBack {
        /**
         * 调用开始
         */
        void onStart();

        /**
         * 调用结束
         */
        void onEnd(int status, String data);
    }
}
