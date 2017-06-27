package com.fanglin.fenhong.microbuyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.event.LoadingFinishEvent;
import com.fanglin.fenhong.microbuyer.base.model.Adv;
import com.fanglin.fenhong.microbuyer.base.model.FHSwitch;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 作者： Created by Plucky on 15-10-24.
 * modify by lizhixin on 2016/03/31
 */
public class LoadingActivity extends Activity implements Adv.AdvModelCallBack {

    @ViewInject(R.id.iv_adv)
    ImageView iv_adv;
    @ViewInject(R.id.FADV)
    FrameLayout FADV;
    @ViewInject(R.id.tvCount)
    TextView tvCount;
    private CountDownTimer countDownTimer;
    //广告位
    Adv advreq;
    String murl;

    FHSwitch fhSwitchReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ViewUtils.inject(this);

        //注册EventBus
        EventBus.getDefault().register(this);

        initView();
    }

    private void initView() {
        FADV.setVisibility(View.GONE);
        fhSwitchReq = new FHSwitch();
        fhSwitchReq.setConTimeOut(5000);//超过5秒就过
        fhSwitchReq.setModelCallback(new FHSwitch.FHSwitchModelCallBack() {
            @Override
            public void onSwitchList(List<FHSwitch> list) {
                if (!FHCache.getFirst(LoadingActivity.this)) {
                    //广告位
                    advreq = new Adv();
                    advreq.setConTimeOut(5000);
                    advreq.setModelCallBack(LoadingActivity.this);
                    advreq.getList(2, "splash");
                } else {
                    BaseFunc.gotoActivity(LoadingActivity.this, MainActivity.class, null);
                    //finish();
                }
            }
        });
        fhSwitchReq.getList();
    }

    @OnClick(value = {R.id.tvCount, R.id.iv_adv})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tvCount:
                BaseFunc.gotoActivity(LoadingActivity.this, MainActivity.class, null);
                //finish();
                break;
            case R.id.iv_adv:
                if (!TextUtils.isEmpty(murl)) {
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    intent.putExtra("ADV", murl);
                    startActivity(intent);
                    //finish();
                }
                break;
        }
    }

    @Override
    public void onAdvError(String errcode) {
        EventBus.getDefault().post(new MessageEvent(null));
    }

    @Override
    public void onAdvList(List<Adv> list) {
        if (list != null && list.size() > 0) {
            Adv advData = list.get(0);
            murl = advData.adv_link;
            new FHImageViewUtil(iv_adv).setImageURI(advData.adv_pic, FHImageViewUtil.SHOWTYPE.LOADING);
            EventBus.getDefault().post(new MessageEvent(advData));
        } else {
            EventBus.getDefault().post(new MessageEvent(null));
        }
    }

    @Override
    public void finish() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.finish();
    }

    private void handleAdv(Adv advData) {
        if (advData != null) {
            FADV.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(3300, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long tmp = millisUntilFinished / 1000;
                    tvCount.setText(getTickTips(tmp));
                    if (tmp == 1) {
                        BaseFunc.gotoActivity(LoadingActivity.this, MainActivity.class, null);
                        //finish();
                    }
                }

                @Override
                public void onFinish() {

                }

            }.start();
        } else {
            BaseFunc.gotoActivity(LoadingActivity.this, MainActivity.class, null);
            //finish();
        }
    }

    private String getTickTips(long tmp) {
        return getString(R.string.jump) + " " + tmp + "s";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void loadingEvent(MessageEvent event) {
        if (event != null)
            handleAdv(event.advData);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void loadingFinishEvent(LoadingFinishEvent event) {
        if (event != null) {
            finish();
        }
    }

    class MessageEvent {
        Adv advData;

        public MessageEvent(Adv advData) {
            this.advData = advData;
        }
    }
}
