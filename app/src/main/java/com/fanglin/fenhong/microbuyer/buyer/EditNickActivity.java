package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fhui.FHEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/15-上午11:18.
 * 功能描述: 修改昵称界面
 */
public class EditNickActivity extends BaseFragmentActivityUI implements TextWatcher {
    @ViewInject(R.id.etNick)
    FHEditText etNick;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;
    @ViewInject(R.id.tvTips)
    TextView tvTips;

    private String nickName;
    private int editStatus = 1;//（1可修改  2不可修改）
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_editnick, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        nickName = getIntent().getStringExtra("NICK");
        day = getIntent().getIntExtra("TIME", 180);
        editStatus = getIntent().getIntExtra("EDITSTATES", 1);

        ivClear.setVisibility(View.GONE);
        etNick.addTextChangedListener(this);
        String fmt = getString(R.string.fmt_hint_editnick);
        if (editStatus == 1) {
            tvTips.setText(String.format(fmt, "修改昵称后，180天内将不能再次修改"));
            etNick.setInputType(InputType.TYPE_CLASS_TEXT);
            ivClear.setVisibility(View.VISIBLE);
        } else {
            tvTips.setText(String.format(fmt, day + "天后可修改昵称"));
            etNick.setInputType(InputType.TYPE_NULL);
            ivClear.setVisibility(View.GONE);
        }

        initView();
    }

    private void initView() {
        setHeadTitle(R.string.title_editnick);
        enableTvMore(R.string.sure, false);
        etNick.setText(nickName);
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        if (editStatus == 2) {
            BaseFunc.showMsg(mContext, day + "天后可修改昵称");
            return;
        }

        if (etNick.length() < 4) {
            YoYo.with(Techniques.Shake).duration(700).playOn(etNick);
            BaseFunc.showMsg(mContext, getString(R.string.hint_inputnickname));
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("VAL", etNick.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etNick.length() > 0 && editStatus == 1) {
            ivClear.setVisibility(View.VISIBLE);
        } else {
            ivClear.setVisibility(View.GONE);
        }
    }

    @OnClick(value = {R.id.ivClear})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivClear:
                etNick.getText().clear();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        BaseFunc.hideSoftInput(mContext, etNick);
    }
}
