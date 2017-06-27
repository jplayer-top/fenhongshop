package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyGoodsListRequest;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyfreeGoodsListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/15-上午9:22.
 * 功能描述: 极速免税店 商品列表页
 */
public class DutyfreeGoodsListActivity extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener2, DutyGoodsListRequest.DutyGoodsListReqCallback, TextView.OnEditorActionListener {

    @ViewInject(R.id.etSearch)
    EditText etSearch;
    @ViewInject(R.id.ivMore)
    ImageView ivMore;
    @ViewInject(R.id.tvDefault)
    TextView tvDefault;
    @ViewInject(R.id.vDefault)
    View vDefault;
    @ViewInject(R.id.tvSales)
    TextView tvSales;
    @ViewInject(R.id.vSales)
    View vSales;
    @ViewInject(R.id.tvPrice)
    TextView tvPrice;
    @ViewInject(R.id.ivPrice)
    ImageView ivPrice;
    @ViewInject(R.id.vPrice)
    View vPrice;
    @ViewInject(R.id.tvPopular)
    TextView tvPopular;
    @ViewInject(R.id.vPopular)
    View vPopular;
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;

    DutyfreeGoodsListAdapter adapter;
    int sort = 1, type = 1, curpage = 1;
    String key;
    DutyGoodsListRequest goodsListRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutyfree_goodslist);
        ViewUtils.inject(this);
        key = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);

        adapter = new DutyfreeGoodsListAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
        changeView(0);
        goodsListRequest = new DutyGoodsListRequest();
        goodsListRequest.setGoodsListReqCallback(this);

        etSearch.setOnEditorActionListener(this);
        etSearch.setText(key);

        onPullDownToRefresh(pullToRefreshRecycleView);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @OnClick(value = {R.id.ivBack, R.id.tvDefault, R.id.tvSales, R.id.tvPrice, R.id.tvPopular})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvDefault:
                changeView(0);
                break;
            case R.id.tvSales:
                changeView(1);
                break;
            case R.id.tvPrice:
                sort = sort == 1 ? 2 : 1;
                changeView(2);
                break;
            case R.id.tvPopular:
                changeView(3);
                break;
        }
        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    private void changeView(int index) {
        type = index + 1;// 1:综合2：销量3：价格4：人气
        tvDefault.setSelected(index == 0);
        vDefault.setSelected(index == 0);
        tvSales.setSelected(index == 1);
        vSales.setSelected(index == 1);
        tvPrice.setSelected(index == 2);
        vPrice.setSelected(index == 2);
        ivPrice.setSelected(index == 2 && sort == 1);
        tvPopular.setSelected(index == 3);
        vPopular.setSelected(index == 3);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        goodsListRequest.getList(member, curpage, type, sort, key);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        goodsListRequest.getList(member, curpage, type, sort, key);
    }

    @Override
    public void onDutyGoodsList(List<BaseProduct> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (list != null && list.size() > 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            if (curpage == 1) {
                adapter.setProducts(list);
            } else {
                adapter.addProducts(list);
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
}
