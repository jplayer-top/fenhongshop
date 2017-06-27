package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.AppMsgList;
import com.fanglin.fenhong.microbuyer.buyer.adapter.AppMsgAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 * 消息中心页面
 * 作者： Created by Plucky on 2015/10/27.
 * modify by lizhixin on 2015/12/25.
 */
public class MsgListActivity extends BaseFragmentActivityUI implements AppMsgList.AMLModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    AppMsgList appMsgList;

    String title;
    int type, curpage = 1;
    AppMsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_msglist, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        title = getIntent().getStringExtra("TITLE");
        type = getIntent().getIntExtra("TYPE", -1);

        if (TextUtils.isEmpty(title) || type == -1) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        setHeadTitle(title);
        enableIvMore(0);

        appMsgList = new AppMsgList();
        appMsgList.setModelCallBack(this);
        adapter = new AppMsgAdapter(mContext);
        pullToRefreshListView.setAdapter(adapter);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);

        onPullDownToRefresh(pullToRefreshListView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        appMsgList.getList(member, type, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        appMsgList.getList(member, type, curpage);
    }

    @Override
    public void onivMoreClick() {
        LayoutMoreVertical layoutMoreVertical = new LayoutMoreVertical(vBottomLine);
        layoutMoreVertical.setShareData(null);
        layoutMoreVertical.setIsMsgShow(false);
        layoutMoreVertical.show();
    }

    /**
     * APP消息接口代理
     */
    @Override
    public void onError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (curpage > 1) {
            pullToRefreshListView.showNoMore();
        } else {
            BaseFunc.showMsg(mContext, getString(R.string.no_data));
        }
    }

    @Override
    public void onList(final List<AppMsgList> list) {
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
