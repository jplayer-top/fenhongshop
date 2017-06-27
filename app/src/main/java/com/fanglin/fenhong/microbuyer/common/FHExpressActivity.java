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
import com.fanglin.fenhong.microbuyer.base.model.FHExpress;
import com.fanglin.fenhong.microbuyer.common.adapter.ExpressAdapter;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购 物流信息
 * Created by lizhixin on 2015/12/2.
 */
public class FHExpressActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    View headerView;
    LinearLayout LIcon;
    TextView tvExpressState;
    TextView tvExpressNum;
    TextView tvExpressName;

    String orderId;

    ExpressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(mContext, R.layout.activity_express_fh, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        if (getIntent().hasExtra("VAL")) {
            orderId = getIntent().getStringExtra("VAL");
        }

        FHLog.d("FHExpressActivity", orderId);

        initView();
    }

    private void initView() {
        setHeadTitle(R.string.express_detail);
        headerView=View.inflate(mContext,R.layout.layout_fhexpress_header,null);
        LIcon=(LinearLayout) headerView.findViewById(R.id.LIcon);
        tvExpressState=(TextView)headerView.findViewById(R.id.tvExpressState);
        tvExpressNum=(TextView)headerView.findViewById(R.id.tvExpressNum);
        tvExpressName=(TextView)headerView.findViewById(R.id.tvExpressName);


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
        if (member == null) return;

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
                pullToRefreshListView.onRefreshComplete();
                try {
                    FHExpress fhExpress = null;
                    if (isSuccess) {//没有返回错误信息
                        fhExpress = new Gson().fromJson(data, FHExpress.class);
                    } else {
                        //返回错误信息，无法解析数据，给予提示
                        tvExpressState.setText(getString(R.string.no_express_hint));
                    }

                    if (fhExpress != null) {

                        if (!TextUtils.isEmpty(fhExpress.state_info)) {
                            tvExpressState.setText(fhExpress.state_info);
                        }

                        //单号 有国内单号先去用国内单号
                        if (fhExpress.home_express_info != null) {
                            tvExpressNum.setText(fhExpress.home_express_info.nu);

                            if (fhExpress.home_express_info.com != null) {
                                tvExpressName.setText(fhExpress.home_express_info.com);
                            } else {
                                tvExpressName.setText("");
                            }
                        } else if (fhExpress.foreign_express_info != null) {
                            //没有国内单号的情况下如果有国外单号则表示还未到国内，此时用国外单号
                            if (!TextUtils.isEmpty(fhExpress.foreign_express_info.nu))
                                tvExpressNum.setText(fhExpress.foreign_express_info.nu);
                            if (fhExpress.home_express_info != null && fhExpress.home_express_info.com != null) {
                                tvExpressName.setText(fhExpress.home_express_info.com);
                            } else {
                                tvExpressName.setText("");
                            }
                        }

                        List<Express100.ExpData> list = new ArrayList<>();
                        /**
                         * 加载 物流信息 列表
                         * 如果有国内信息，则优先加载国内数据，国外信息添加在国内之后
                         */
                        if (fhExpress.home_express_info != null && fhExpress.home_express_info.data != null) {
                            list.addAll(fhExpress.home_express_info.data);
                        }
                        if (fhExpress.foreign_express_info != null && fhExpress.foreign_express_info.data != null) {
                            list.addAll(fhExpress.foreign_express_info.data);
                        }
                        if (list.size() > 0) {
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    BaseFunc.showMsg(mContext, getString(R.string.op_error));
                }
            }
        }).get_fh_express(orderId, member.member_id, member.token);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        getdata();
    }
}
