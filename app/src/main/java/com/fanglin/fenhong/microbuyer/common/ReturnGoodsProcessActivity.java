package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.RefundProcess;
import com.fanglin.fenhong.microbuyer.base.model.RefundProgress;
import com.fanglin.fenhong.microbuyer.common.adapter.RefundProcessAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 退款进度 Activity
 * Added By lizhixin
 * fixed BY Plucky
 */
public class ReturnGoodsProcessActivity extends BaseFragmentActivityUI implements RefundProgress.RPModelCallBack,PullToRefreshBase.OnRefreshListener {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;
    RefundProcessAdapter adapter;
    String refund_id;//上级页面传入的退货、退款单号
    int refund_state;// 1-7 退货单当前状态
    int refund_type;//  退货-2? 退款-1?
    String rec_id, order_id;

    RefundProgress RPREQ;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        View view = View.inflate (this, R.layout.activity_return_goods_process, null);
        LHold.addView (view);
        ViewUtils.inject (this, view);

        try {
            refund_id = getIntent ().getStringExtra ("ID");
            refund_state = Integer.valueOf (getIntent ().getStringExtra ("STATE"));
            refund_type = getIntent ().getIntExtra ("TYPE", 1);
            rec_id = getIntent ().getStringExtra ("REC");
            order_id = getIntent ().getStringExtra ("ORDER");
        } catch (Exception e) {
            refund_state = 0;
            refund_type = 0;
            rec_id = "-1";
            order_id = "-1";
        }

        initView ();
    }

    private void initView () {
        if (refund_type == 1) {
            setHeadTitle(R.string.return_goods_process_money);
        } else {
            setHeadTitle(R.string.return_goods_process_goods);
        }

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);

        adapter = new RefundProcessAdapter (mContext, refund_id, order_id, rec_id);
        if (refund_type > 0 && refund_type > 0) {
            adapter.setList (RefundProcess.getList (refund_type, refund_state));
        }
        pullToRefreshListView.setAdapter (adapter);

        RPREQ = new RefundProgress ();
        RPREQ.setModelCallBack (this);
    }

    @Override
    protected void onResume () {
        super.onResume ();
        onRefresh(pullToRefreshListView);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        RPREQ.getData (member, refund_id);
    }


    @Override
    public void onRPData (final RefundProgress data) {
        pullToRefreshListView.onRefreshComplete();
        if (data != null) {
            adapter.setList (RefundProcess.getList (data.refund_type, data.refund_state));
            adapter.notifyDataSetChanged ();
        }

    }

    @Override
    public void onRPError (String errcode) {
        pullToRefreshListView.onRefreshComplete();
        BaseFunc.showMsg (mContext, getString (R.string.op_error));
    }
}
