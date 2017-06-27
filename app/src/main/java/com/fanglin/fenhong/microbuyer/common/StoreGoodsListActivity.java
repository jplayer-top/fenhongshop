package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.GoodsFilter;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsListAdapter;
import com.fanglin.fenhong.microbuyer.common.adapter.StoreGoodsListAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/14-下午2:42.
 * 功能描述: 店铺商品列表
 */
public class StoreGoodsListActivity extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener2, GoodsList.GoodsListModelCallBack, TextView.OnEditorActionListener, TextWatcher, GoodsListAdapter.GoodsListCallBack {

    @ViewInject(R.id.tvSearchIcon)
    TextView tvSearchIcon;
    @ViewInject(R.id.etSearch)
    EditText etSearch;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;
    @ViewInject(R.id.ivMore)
    ImageView ivMore;

    @ViewInject(R.id.tvDefault)
    TextView tvDefault;
    @ViewInject(R.id.tvSales)
    TextView tvSales;
    @ViewInject(R.id.LPrice)
    LinearLayout LPrice;
    @ViewInject(R.id.tvUp)
    TextView tvUp;
    @ViewInject(R.id.tvDown)
    TextView tvDown;
    @ViewInject(R.id.tvPopular)
    TextView tvPopular;

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    @ViewInject(R.id.vMsgDot)
    View vMsgDot;

    StoreGoodsListAdapter adapter;
    GoodsList goodsListReq;
    GoodsFilter gfilter;

    LayoutMoreVertical layoutMoreVertical;

    private String storeId, classId, contactUrl, search;
    private ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storegoodslist);
        ViewUtils.inject(this);
        storeId = getIntent().getStringExtra("STOREID");
        classId = getIntent().getStringExtra("CLASSID");
        contactUrl = getIntent().getStringExtra("CONTACT");
        search = getIntent().getStringExtra("SEARCH");

        try {
            String share = getIntent().getStringExtra("SHARE");
            shareData = new Gson().fromJson(share, ShareData.class);
        } catch (Exception e) {
            shareData = null;
        }
        initView();
    }

    private void initView() {
        tvSearchIcon.setTypeface(iconfont);
        tvUp.setTypeface(iconfont);
        tvDown.setTypeface(iconfont);
        etSearch.setText(search);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setBackUpView(btnBackTop);

        layoutMoreVertical = new LayoutMoreVertical(ivMore);
        layoutMoreVertical.setContactUrl(contactUrl);
        layoutMoreVertical.setShareData(shareData);
        layoutMoreVertical.setIsSearchShow(false);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);
        adapter = new StoreGoodsListAdapter(mContext);
        adapter.setListener(this);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        goodsListReq = new GoodsList();
        goodsListReq.setModelCallBack(this);

        /** 筛选条件类*/
        gfilter = new GoodsFilter();
        gfilter.sid = storeId;
        gfilter.key = search;
        gfilter.curpage = 1;
        gfilter.sort = 3;
        gfilter.order = 1;
        if (!TextUtils.isEmpty(classId)) {
            gfilter.scid = classId;
        }

        etSearch.setOnEditorActionListener(this);
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etSearch.addTextChangedListener(this);

        refreshClickView();
    }

    @OnClick(value = {R.id.ivBack, R.id.ivList, R.id.ivMore, R.id.ivClear,
            R.id.tvDefault, R.id.tvSales, R.id.LPrice, R.id.tvPopular})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivList:
                BaseFunc.gotoStoreClassActivity(this, storeId, new Gson().toJson(layoutMoreVertical.getShareData()), layoutMoreVertical.getContactUrl());
                break;
            case R.id.ivMore:
                layoutMoreVertical.show();
                break;
            case R.id.ivClear:
                etSearch.getText().clear();
                break;
            case R.id.tvDefault:
                currentIndex = 0;
                gfilter.sort = 3;
                gfilter.order = -1;
                refreshClickView();
                break;
            case R.id.tvSales:
                currentIndex = 1;
                gfilter.sort = 1;
                gfilter.order = -1;
                refreshClickView();
                break;
            case R.id.LPrice:
                currentIndex = 2;
                gfilter.sort = 2;
                currentOrder = currentOrder == 1 ? 2 : 1;
                gfilter.order = currentOrder;
                refreshClickView();
                break;
            case R.id.tvPopular:
                currentIndex = 3;
                gfilter.sort = 4;
                gfilter.order = -1;
                refreshClickView();
                break;
        }
    }

    private int currentIndex = 0;
    private int currentOrder = 1;

    private void refreshClickView() {
        tvDefault.setSelected(currentIndex == 0);
        tvSales.setSelected(currentIndex == 1);
        LPrice.setSelected(currentIndex == 2);
        tvUp.setSelected(currentIndex == 2 && currentOrder == 1);
        tvDown.setSelected(currentIndex == 2 && currentOrder == 2);
        tvPopular.setSelected(currentIndex == 3);

        onPullDownToRefresh(pullToRefreshRecycleView);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onItemClick(GoodsList c1, int position) {
        BaseFunc.gotoGoodsDetail(this, c1.goods_id, null, null);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        gfilter.curpage = 1;
        goodsListReq.get_goods(gfilter);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        gfilter.curpage++;
        goodsListReq.get_goods(gfilter);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        BaseFunc.hideSoftInput(mContext, etSearch);
        gfilter.curpage = 1;
        goodsListReq.get_goods(gfilter);

        /**
         * 记录搜索动作
         */
        BaseFunc.add_search(mContext, etSearch.getText().toString(), member);
        return false;
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
            gfilter.key = null;
            ivClear.setVisibility(View.GONE);
        } else {
            gfilter.key = etSearch.getText().toString();
            ivClear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGLMCList(List<GoodsList> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (gfilter.curpage > 1) {
            //加载更多
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            if (list != null && list.size() > 0) {
                adapter.addList(list);
                pullToRefreshRecycleView.onAppendData();
            } else {
                pullToRefreshRecycleView.showNoMore();
            }
        } else {
            adapter.setList(list);
            pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
            if (list != null && list.size() > 0) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }
    }

    @Override
    public void onGLMCError(String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (gfilter.curpage > 1) {
            pullToRefreshRecycleView.showNoMore();
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            vMsgDot.setVisibility(View.VISIBLE);
            layoutMoreVertical.setMsgNum(msgnum.getTotalNum());
        } else {
            vMsgDot.setVisibility(View.GONE);
            layoutMoreVertical.setMsgNum(0);
        }
    }
}
