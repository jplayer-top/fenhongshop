package com.fanglin.dutyfree;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhlib.other.FHLog;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.tvDesc)
    TextView tvDesc;
    FHApp app;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        app = FHApp.getApp();
        mContext = this;

        String str = "abc=wsj";
        String str1 = "{'A':'a','b':0,'c':[1,2]}";
        FHLog.d("Plucky", "str is json:" + FHLib.isJson(str));
        FHLog.d("Plucky", "str1 is json:" + FHLib.isJson(str1));

        tvDesc.setText(str);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.build();
    }

    @Event(value = {R.id.btnAPI1, R.id.btnActivity, R.id.btnRead, R.id.btnSave, R.id.btnClick, R.id.btnAPI})
    private void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btnClick:
                tvDesc.setText("clicked +" + 1);
                break;
            case R.id.btnAPI:
                FHRequestParams params = new FHRequestParams("https://test.fenhongshop.com/api/index.php?act=wholesaler&op=gooddetails&flag=android");
                params.addBodyParameter("product_id", "1");
                new APIUtil().setCallBack(new APIUtil.FHAPICallBack() {
                    @Override
                    public void onEnd(boolean isSuccess, String data) {
                        FHLog.d("Plucky", "API:" + data);
                    }
                }).execute(APIUtil.APIGET, params);
                break;
            case R.id.btnAPI1:
                get("https://test.fenhongshop.com/api/index.php?act=wholesaler&op=gooddetails&flag=android");
                break;
            case R.id.btnActivity:

                break;
            case R.id.btnRead:
                try {
                    People people = app.getDb().findById(People.class, "b");
                    tvDesc.setText("read" + people.getId());
                    FHLog.d("Plucky", "desc:" + people.getDesc());
                } catch (Exception e) {
                    FHLog.d("Plucky", "read error:" + e.getMessage());
                }
                break;
            case R.id.btnSave:
                tvDesc.setText("saved +" + 1);
                People people = new People();
                people.setId("b");
                people.setName("0000");
                people.setDesc("222");

                try {
                    app.getDb().save(people);
                } catch (Exception e) {
                    FHLog.d("Plucky", "save error:" + e.getMessage());
                }
                break;
        }
    }


    private void get(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    message.obj = execute(url);
                    message.what = 0;
                } catch (Exception e) {
                    FHLog.d("Plucky", e.getMessage());
                    message.obj = null;
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    FHHandler handler = new FHHandler();

    static class FHHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null)
                FHLog.d("Plucky", "error:" + msg.what + " data:" + msg.what);
        }
    }


    OkHttpClient client;

    private String execute(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
