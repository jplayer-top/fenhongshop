package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.baseui.Theme2MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.Theme2Data;
import com.fanglin.fenhong.microbuyer.common.adapter.ThemeTwoAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/1-下午3:54.
 * 功能描述: 首页主题二
 */
public class HomeThemeTwoFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, Theme2Data.Theme2APICallBack {

    private static final String KEY_CONTENT = "HomeThemeTwoFragment:Content";

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    ThemeTwoAdapter adapter;
    int curpage = 1;
    Theme2Data theme2DataReq;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_themetwo, container, false);
        ViewUtils.inject(this, view);
        initView();
        return view;
    }

    public static HomeThemeTwoFragment newInstance(String url) {
        HomeThemeTwoFragment fragment = new HomeThemeTwoFragment();
        fragment.murl = url;
        return fragment;
    }

    private String murl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            murl = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, murl);
    }

    private void initView() {
        adapter = new ThemeTwoAdapter(act);
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setBackUpView(btnBackTop);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(act, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanSize(position);
            }
        });

        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(gridLayoutManager);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        Theme2MutiItemDecoration decoration = new Theme2MutiItemDecoration(MutiItemDecoration.Type.ALL, 2);
        decoration.setNotDrawViewType(ThemeTwoAdapter.TYPE_CLASS_4COLUMN, ThemeTwoAdapter.TYPE_CLASS_2COLUMN, ThemeTwoAdapter.TYPE_GOODS_1COLUMN, ThemeTwoAdapter.TYPE_GOODS_HUGE);
        pullToRefreshRecycleView.getRefreshableView().addItemDecoration(decoration);

        theme2DataReq = new Theme2Data();
        theme2DataReq.setModelCallBack(this);

        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onTheme2List(List<Theme2Data> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (curpage == 1) {
            adapter.setList(list);
        } else {
            adapter.addList(list);
            pullToRefreshRecycleView.onAppendData();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        theme2DataReq.getList(curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        theme2DataReq.getList(curpage);
    }
}
