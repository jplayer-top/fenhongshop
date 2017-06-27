package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.PointsData;
import com.fanglin.fenhong.microbuyer.common.adapter.PointsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 作者： Created by Plucky on 2015/10/29.
 */
public class PointsActivity extends BaseFragmentActivityUI implements PointsData.PointsDataModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    private int curpage = 1;
    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;


    TextView tvAllPoints;
    TextView tvList;

    PointsAdapter adapter;
    PointsData pointsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_points, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        initView();
    }

    private void initView() {
        setHeadTitle(R.string.points);

        View headerView = View.inflate(mContext, R.layout.layout_points_header, null);
        tvAllPoints=(TextView)headerView.findViewById(R.id.tvAllPoints);
        tvList=(TextView)headerView.findViewById(R.id.tvList);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.getRefreshableView().addHeaderView(headerView);

        adapter = new PointsAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);
        pointsData = new PointsData();
        pointsData.setModelCallBack(this);

        if (member != null) {
            /** 刷新数据*/
            tvAllPoints.setText("");
            onPullDownToRefresh(pullToRefreshListView);
            refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        if (pointsData != null) pointsData.getList(member, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        if (pointsData != null) pointsData.getList(member, curpage);
    }

    /**
     * 数据回调
     */
    @Override
    public void onPDData(PointsData data) {
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        pullToRefreshListView.onRefreshComplete();
        tvList.setText(getString(R.string.points_dtl));
        if (curpage > 1) {
            if (data != null) {
                tvAllPoints.setText(data.allpoints);
                if (data.points_list != null && data.points_list.size() > 0) {
                    adapter.addList(data.points_list);
                    adapter.notifyDataSetChanged();
                } else {
                    pullToRefreshListView.showNoMore();
                }
            } else {
                pullToRefreshListView.showNoMore();
            }
        } else {
            pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
            if (data != null) {
                tvAllPoints.setText(data.allpoints);
                adapter.setList(data.points_list);
                adapter.notifyDataSetChanged();
            } else {
                adapter.setList(null);
                adapter.notifyDataSetChanged();
                BaseFunc.showMsg(mContext, getString(R.string.no_data));
            }
        }
    }

    @Override
    public void onPDError(String errcode) {
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        tvList.setText(getString(R.string.points_dtl));
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            pullToRefreshListView.showNoMore();
        } else {
            if (!TextUtils.equals("-4", errcode))
                BaseFunc.showMsg(mContext, getString(R.string.no_data));
        }
    }

}
