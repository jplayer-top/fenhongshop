package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyBindAddrAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午7:30.
 * 功能描述: 极速免税店 关联地址
 */
public class DutyfreeBindAddrActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    DutyBindAddrAdapter adapter;
    private String cartID;
    private DutyCartProduct product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_dutyfree_bind_addr, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        cartID = getIntent().getStringExtra("VAL");
        product = CartCheckCache.findGoodsById(cartID);
        initView();
    }

    private void initView() {
        if (product == null) {
            finish();
            BaseFunc.showMsg(mContext, "异常数据");
            return;
        }
        tvHead.setText("关联地址");
        enableTvMore(R.string.commit, false);
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.DISABLED);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        adapter = new DutyBindAddrAdapter(mContext);
        adapter.setTotalNum(product.getProduct_num());
        adapter.setProduct(product);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(new LinearLayoutManager(mContext));
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
    }

    @OnClick(value = {R.id.tvList, R.id.tvNew})
    public void onViewClick(View view) {
        if (product == null) return;
        boolean canBinded = adapter.canBind();
        if (!canBinded) {
            BaseFunc.showMsg(mContext, "商品数量不可再分配!");
            return;
        }
        switch (view.getId()) {
            case R.id.tvList:
                BaseFunc.gotoActivity(mContext, DutyAddrListActivity.class, cartID);
                break;
            case R.id.tvNew:
                BaseFunc.gotoDutyNewAddress(mContext, cartID, null);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<DutyAddress> addrList = CartCheckCache.getAddrList(cartID);
        adapter.setAddrList(addrList);
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        if (adapter.hasBinded()) {
            finish();
        }
    }
}
