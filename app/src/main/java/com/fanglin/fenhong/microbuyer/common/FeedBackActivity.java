package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.AppInfo;
import com.fanglin.fenhong.microbuyer.base.model.FeedBack;
import com.fanglin.fenhong.microbuyer.base.model.WSFeedBack;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 意见反馈 Activity
 * Created by lizhixin on 2015/11/17.
 */
public class FeedBackActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.tv_icon_quto)
    private TextView tvIconQuto;
    @ViewInject(R.id.tv_count)
    private TextView tvCount;
    @ViewInject(R.id.et_content)
    private EditText etContent;
    @ViewInject(R.id.et_contact)
    private EditText etContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadTitle(R.string.title_feed_back);

        View view = View.inflate(this, R.layout.activity_feed_back, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {

        BaseFunc.setFont(tvIconQuto);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = etContent.length();
                String format = String.format(getString(R.string.evaluate_limit), len);
                tvCount.setText(format);
            }
        });

    }

    @OnClick({R.id.tv_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    BaseFunc.showMsg(mContext, getString(R.string.feed_back_check_content));
                    YoYo.with(Techniques.Shake).duration(700).playOn(etContent);
                } else {
                    sendRequest();
                }
                break;
            default:
                break;
        }
    }

    private void sendRequest() {

        WSFeedBack feedBackHandler = new WSFeedBack();
        feedBackHandler.setWSFeedBackCallBack(new WSFeedBack.WSFeedBackCallBack() {
            @Override
            public void onWSFeedBackSuccess(String data) {
                BaseFunc.showMsg(mContext, getString(R.string.feed_back_success));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void onWSFeedBackError(String errcode) {
                BaseFunc.showMsg(mContext, getString(R.string.feed_back_failed));
            }
        });

        FeedBack feedBackEntity = new FeedBack();
        feedBackEntity.setContent(etContent.getText().toString().trim());
        feedBackEntity.setContact(etContact.getText().toString().trim());
        feedBackEntity.setAppInfo(new AppInfo(mContext));

        feedBackHandler.submit(member, new Gson().toJson(feedBackEntity));
    }

}
