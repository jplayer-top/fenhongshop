package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.BankCard;
import com.fanglin.fenhong.microbuyer.microshop.adapter.SwipeBankCardAdapter;
import com.fanglin.fhui.FHHintDialog;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/4.
 * 银行卡列表页 带侧滑按钮
 */
public class BankCardListActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener, BankCard.BCModelCallBack {
    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;
    SwipeBankCardAdapter adapter;
    BankCard bankCardReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_bankcardlist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }


    private void initView() {
        setHeadTitle(R.string.bankcard_list);
        enableTvMore(R.string.if_add,true);

        adapter = new SwipeBankCardAdapter(mContext);
        bankCardReq = new BankCard();
        bankCardReq.setModelCallBack(this);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);

        pullToRefreshListView.setAdapter(adapter);
        adapter.setCallBack(new SwipeBankCardAdapter.SwipeBankCardCallBack() {
            @Override
            public void onEdit(BankCard bankCard) {
                BaseFunc.gotoActivity(mContext, EditBankCardActivity.class, new Gson().toJson(bankCard));
            }

            @Override
            public void onDelete(BankCard bankCard) {
                confirm(bankCard);
            }
        });
        pullToRefreshListView.getRefreshableView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh(pullToRefreshListView);
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        BaseFunc.gotoActivity(this, EditBankCardActivity.class, null);
    }

    @Override
    public void onError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (bankCardReq.actionNum == 1) {//删除
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            if (TextUtils.equals("0", errcode)) {
                onRefresh(pullToRefreshListView);
                BaseFunc.showMsg(mContext, getString(R.string.op_success));
            }
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onList(List<BankCard> list) {
        pullToRefreshListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        adapter.setList(null);
        adapter.notifyDataSetChanged();
        bankCardReq.getList(member);
    }

    private void confirm(final BankCard adata) {
        if (member == null || adata == null) return;

        final FHHintDialog fhd = new FHHintDialog(BankCardListActivity.this);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                bankCardReq.delete(member, adata.card_id);
            }
        });
        fhd.setTvContent(getString(R.string.tips_del_bankcard));
        fhd.show();
    }
}
