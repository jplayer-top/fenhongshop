package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.event.DutyAddShowEvent;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyAddrAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午9:04.
 * 功能描述: 急速免税店 地址列表
 */
public class DutyAddrListActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener, DutyAddress.DutyAddrRequestCallBack {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    DutyAddrAdapter adapter;

    DutyAddress addressReq;
    private String cartID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_duty_addresslist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        cartID = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        tvHead.setText("收货地址管理");
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        enableTvMore(R.string.submit, false);

        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DutyAddrAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        addressReq = new DutyAddress();
        addressReq.setDutyAddrReqCallBack(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (member == null) return;
        addressReq.getData(member);
    }

    @Override
    public void onDutyAddrList(List<DutyAddress> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        adapter.setList(list);
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        //Obl-change
//        int totalNum = CartCheckCache.getNumOfGoods(cartID);
//        int canBindNum /*= CartCheckCache.getLeftNumOfCartId(cartID, totalNum);*/
//         = totalNum;
//        if (canBindNum == 0) {
//            BaseFunc.showMsg(mContext, "无可继续分配的商品数量!");
//            return;
//        }
//        int selectedNum = adapter.getSelectedNum();
//        if (selectedNum > canBindNum) {
//            BaseFunc.showMsg(mContext, "最多能分配" + canBindNum + "个地址");
//            return;
//        }
        boolean selected = adapter.onBindAddress(cartID);
        if (selected) {
            EventBus.getDefault().postSticky(new DutyAddShowEvent(true));
            finish();
        } else {
            BaseFunc.showMsg(mContext, "请选择一个地址");
        }

    }

    @OnClick(value = {R.id.tvNew})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvNew:
                BaseFunc.gotoActivity(mContext, DutyNewAddrsActivity.class, null);
                break;
        }
    }
}
