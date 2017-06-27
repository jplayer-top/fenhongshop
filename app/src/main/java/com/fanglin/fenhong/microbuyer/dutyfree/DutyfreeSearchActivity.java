package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/14-下午6:33.
 * 功能描述: 极速免税店 搜索页面
 */
public class DutyfreeSearchActivity extends BaseFragmentActivity implements TextView.OnEditorActionListener {

    @ViewInject(R.id.etSearch)
    EditText etSearch;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutyfree_search);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etSearch.setOnEditorActionListener(this);
    }

    @OnClick(value = {R.id.tvGoods, R.id.tvBrand, R.id.tvCancel})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvGoods:
                etSearch.setBackgroundResource(R.drawable.editbg_duty_search);
                etSearch.setHint("搜索商品 分类");
                index = 0;
                break;
            case R.id.tvBrand:
                etSearch.setBackgroundResource(R.drawable.editbg_duty_search_brand);
                etSearch.setHint("搜索品牌");
                index = 1;
                break;
            case R.id.tvCancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String key = etSearch.getText().toString();
        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            BaseFunc.toggleSoftInput(mContext);
            if (index == 0) {
                BaseFunc.gotoActivity(mContext, DutyfreeGoodsListActivity.class, key);
            } else {
                BaseFunc.gotoActivity(mContext, DutyfreeBrandlistActivity.class, key);
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        InputMethodManager im = BaseFunc.getInputMethodManager(this);
        if (im != null && im.isActive()) {
            im.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }
}
