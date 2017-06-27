package com.fanglin.fenhong.microbuyer.microshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.SearchTagsRequest;
import com.fanglin.fenhong.microbuyer.base.model.TalentTagRequest;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TalentTagSearchAdapter;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/15-下午1:21.
 * 功能描述: 搜索标签页面
 */
public class TalentTagSearchActivity extends BaseFragmentActivity implements TalentTagRequest.TTReqModelCallBack, FHHintDialog.FHHintListener, TextView.OnEditorActionListener, SearchTagsRequest.STReqModelCallBack, TextWatcher, TalentTagSearchAdapter.TalentTagSearchAdapterListener {
    @ViewInject(R.id.scrollView)
    ScrollView scrollView;
    @ViewInject(R.id.listView)
    ListView listView;

    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    @ViewInject(R.id.tflRecentSearch)
    TagFlowLayout tflRecentSearch;
    @ViewInject(R.id.tflHotSearch)
    TagFlowLayout tflHotSearch;
    @ViewInject(R.id.etSearch)
    EditText etSearch;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;

    FHHintDialog fhHintDialog;

    TalentTagRequest tagRequest;
    List<String> historys;

    SearchTagsRequest searchTagsRequest;
    TalentTagSearchAdapter tagSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talenttag_search);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        tagRequest = new TalentTagRequest();
        tagRequest.setModelCallBack(this);
        tagRequest.getTags(member);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        fhHintDialog = new FHHintDialog(mContext);
        fhHintDialog.setTvContent(getString(R.string.tips_clear_recent));
        fhHintDialog.setTvLeft(getString(R.string.cancel));
        fhHintDialog.setTvRight(getString(R.string.lbl_confirm_clear));
        fhHintDialog.setHintListener(this);

        searchTagsRequest = new SearchTagsRequest();
        searchTagsRequest.setModelCallBack(this);

        tagSearchAdapter = new TalentTagSearchAdapter(mContext);
        tagSearchAdapter.setListener(this);
        listView.setAdapter(tagSearchAdapter);

        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    @OnClick(value = {R.id.ivBack, R.id.tvClear, R.id.ivClear})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.tvClear:
                if (member == null) {
                    BaseFunc.gotoLogin(mContext);
                    return;
                }
                if (historys == null || historys.size() == 0) return;
                fhHintDialog.show();
                break;
            case R.id.ivClear:
                etSearch.getText().clear();
                break;
        }
    }

    @Override
    public void onTTReqData(TalentTagRequest tagRequest) {
        if (tagRequest != null) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            List<String> hots = tagRequest.getHot_tags();
            historys = tagRequest.getHistory_tags();
            bindAdapter(tflHotSearch, hots);
            bindAdapter(tflRecentSearch, historys);
        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }


    private void bindAdapter(final TagFlowLayout flowLayout, List<String> tags) {
        if (flowLayout == null) return;
        //如果为null 则表示清空
        if (tags == null) tags = new ArrayList<>();
        flowLayout.setAdapter(new TagAdapter(tags) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_talent_tags, flowLayout, false);
                TextView tvName = (TextView) view.findViewById(R.id.tvName);
                View vBottom = view.findViewById(R.id.vBottom);
                vBottom.setVisibility(View.VISIBLE);
                final String val = String.valueOf(o);
                tvName.setText(val);
                tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("TAG", val);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                return view;
            }
        });

    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {
        if (member == null) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    historys = null;
                    bindAdapter(tflRecentSearch, null);
                }
            }
        }).deleteRecentTags(member, "1", null);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        searchTags();
        return false;
    }

    private void searchTags() {
        if (etSearch.length() == 0) {
            return;
        }
        listView.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        searchTagsRequest.getData(member, etSearch.getText().toString());
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideSoft();
    }

    @Override
    public void onSTReqData(SearchTagsRequest reqData) {
        if (reqData != null) {
            String key = reqData.isExists() ? null : etSearch.getText().toString();
            tagSearchAdapter.setListAndTag(reqData.getTags(), key);
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etSearch.length() == 0) {
            scrollView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            ivClear.setVisibility(View.GONE);
        } else {
            ivClear.setVisibility(View.VISIBLE);
        }
    }

    private void hideSoft() {
        InputMethodManager im = BaseFunc.getInputMethodManager(this);
        if (im.isActive()) {
            im.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemClick(boolean isAdd, String key) {
        if (isAdd) {
            addTag(key);
        } else {
            Intent intent = new Intent();
            intent.putExtra("TAG", key);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void addTag(final String key) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    Intent intent = new Intent();
                    intent.putExtra("TAG", key);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }).addTag(member, key);
    }

}
