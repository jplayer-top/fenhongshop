package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.common.adapter.GoodsFancyAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 * Created by Plucky on 2015/10/12.
 * 精选推荐列表
 */
public class FancyListActivity extends BaseFragmentActivityUI implements GoodsScheme.GoodsSchemeModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    int curpage = 1;
    GoodsScheme goodsFancy;
    GoodsFancyAdapter adapter;
    int area = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_actlist, null);//共用一套界面
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            area = Integer.valueOf(getIntent().getStringExtra("VAL"));
        } catch (Exception e) {
            area = 0;
        }
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.fancy);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setOnRefreshListener(this);

        goodsFancy = new GoodsScheme("jingxuantuijian");
        goodsFancy.setModelCallBack(this);


        adapter = new GoodsFancyAdapter(mContext);
        adapter.setMember(member);
        pullToRefreshListView.setAdapter(adapter);
        onPullDownToRefresh(pullToRefreshListView);
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        if (goodsFancy != null) {
            goodsFancy.getList(area, BaseVar.REQUESTNUM, curpage);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        if (goodsFancy != null) {
            goodsFancy.getList(area, BaseVar.REQUESTNUM, curpage);
        }
    }

    /** 精选推荐代理方法*/
    @Override
    public void RError(String curType, String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            pullToRefreshListView.showNoMore();
        } else {
            BaseFunc.showMsg(mContext,getString(R.string.no_data));
        }
    }

    @Override
    public void RgetList(String curType, List<GoodsScheme> list) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            adapter.addList(list);
            adapter.notifyDataSetChanged();
        } else {
            pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }
}
