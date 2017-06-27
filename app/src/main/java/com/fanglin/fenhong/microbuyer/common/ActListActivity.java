package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.ActivityList;
import com.fanglin.fenhong.microbuyer.common.adapter.ActListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 * Created by Plucky on 2015/10/8.
 * 主题馆列表
 */
public class ActListActivity extends BaseFragmentActivityUI implements ActivityList.ActivityListModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    ActListAdapter adapter;
    ActivityList activityList;
    private int curpage = 1;
    int area = 0, chanel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_actlist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            area = Integer.valueOf(getIntent().getStringExtra("VAL"));
        } catch (Exception e) {
            area = 0;
        }
        chanel = (area == 0 || area == 2) ? 1 : 2;//1 表示首页 2 表示全球购
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.actlist);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setOnRefreshListener(this);

        /** 设置数据源*/
        adapter = new ActListAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);

        /** 实例化Model 并设置代理*/
        activityList = new ActivityList();
        activityList.setModelCallBack(this);

        onPullDownToRefresh(pullToRefreshListView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        /** 请求数据*/
        activityList.getList(area, chanel, "zhutiguan", BaseVar.REQUESTNUM, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        /** 请求数据*/
        activityList.getList(area, chanel, "zhutiguan", BaseVar.REQUESTNUM, curpage);
    }

    @Override
    public void onAlError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            pullToRefreshListView.showNoMore();
        }
    }

    @Override
    public void onAlList(List<ActivityList> list) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            if (list != null && list.size() > 0) {
                adapter.addList(list);
                adapter.notifyDataSetChanged();
            } else {
                pullToRefreshListView.showNoMore();
            }
        } else {
            pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

}
