package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyBrandRequest;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyBrandListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-上午10:09.
 * 功能描述: 极速免税店 品牌搜索页
 */
public class DutyfreeBrandlistActivity extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener2, DutyBrandRequest.DutyBrandRequestCallBack, TextView.OnEditorActionListener {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.etSearch)
    EditText etSearch;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    DutyBrandListAdapter adapter;

    DutyBrandRequest brandRequest;
    int curpage = 1;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutyfree_brandlist);
        ViewUtils.inject(this);
        key = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DutyBrandListAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        etSearch.setText(key);
        etSearch.setOnEditorActionListener(this);

        brandRequest = new DutyBrandRequest();
        brandRequest.setBrandRequestCallBack(this);
        onPullDownToRefresh(pullToRefreshRecycleView);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        brandRequest.getList(member, curpage, key);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        brandRequest.getList(member, curpage, key);
    }

    @Override
    public void onDutyBrandList(List<BrandMessage> brandList) {
        pullToRefreshRecycleView.onRefreshComplete();
        pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
        if (brandList != null && brandList.size() > 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            if (curpage == 1) {
                adapter.setList(brandList);
            } else {
                adapter.addList(brandList);
                pullToRefreshRecycleView.onAppendData();
            }
        } else {
            if (curpage == 1) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                pullToRefreshRecycleView.showNoMore();
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        key = etSearch.getText().toString();
        onPullDownToRefresh(pullToRefreshRecycleView);
        return false;
    }

    @OnClick(value = {R.id.ivBack})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
