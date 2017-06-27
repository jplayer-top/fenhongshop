package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.Express100;
import com.fanglin.fenhong.microbuyer.common.adapter.ExpressAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 作者： Created by Plucky on 15-9-27.
 */
public class Express100Activity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    View headerView;
    LinearLayout LIcon;
    TextView tvExpressState;
    TextView tvExpressNum;
    TextView tvExpressName;

    String ecode;//快递公司编号
    String ename;//快递公司名称
    String freight_num;//运单号
    Express100 express100;
    ExpressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_express100, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        ecode =getIntent().getStringExtra("e_code");// ie.key1;
        freight_num =getIntent().getStringExtra("shipping_code");// ie.key2;
        ename =getIntent().getStringExtra("e_name");// ie.key2_0;
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.express_detail);

        headerView = View.inflate(mContext, R.layout.layout_fhexpress_header, null);
        LIcon = (LinearLayout) headerView.findViewById(R.id.LIcon);
        tvExpressState = (TextView) headerView.findViewById(R.id.tvExpressState);
        tvExpressNum = (TextView) headerView.findViewById(R.id.tvExpressNum);
        tvExpressName = (TextView) headerView.findViewById(R.id.tvExpressName);

        tvExpressNum.setText(freight_num);
        if (!TextUtils.isEmpty(ename)) {
            tvExpressName.setText(ename);
        } else {
            tvExpressName.setText(ecode);
        }

        tvExpressState.setText("");

        BaseFunc.setFont(LIcon);

        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setOnRefreshListener(this);
        adapter = new ExpressAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);

        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
        onRefresh(pullToRefreshListView);
    }

    private void getdata() {
        if (ecode == null || freight_num == null) {
            pullToRefreshListView.onRefreshComplete();
            return;
        }
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                pullToRefreshListView.onRefreshComplete();
                try {
                    express100 = new Gson().fromJson(data, Express100.class);
                    tvExpressState.setText(express100.getStateDesc());

                    if (!TextUtils.isEmpty(express100.nu)) {
                        tvExpressNum.setText(express100.nu);
                    }

                    if (!TextUtils.isEmpty(ename)) {
                        tvExpressName.setText(ename);
                    } else {
                        tvExpressName.setText(ecode);
                    }

                    if (express100.data != null && express100.data.size() > 0) {
                        adapter.setList(express100.data);
                        adapter.notifyDataSetChanged();
                        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
                    } else {
                        refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
                    }

                } catch (Exception e) {
                    refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
                    BaseFunc.showMsg(mContext, getString(R.string.op_error));
                }
            }
        }).get_express100(ecode, freight_num);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        getdata();
    }
}
