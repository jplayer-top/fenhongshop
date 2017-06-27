package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedSectionListView;
import com.fanglin.fenhong.microbuyer.base.model.Commission;
import com.fanglin.fenhong.microbuyer.base.model.WSCommission;
import com.fanglin.fenhong.microbuyer.microshop.adapter.CommissionAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 我的奖金 Fragment
 * 新版微店首页嵌套
 * Created by Plucky on 2016/06/06.
 */
public class CommissionFragment extends BaseFragment implements WSCommission.WSCommissionModelCallBack, PullToRefreshBase.OnRefreshListener2, CommissionAdapter.TitleChangeCallBack {

    private static final String KEY_CONTENT = "CommissionFragment:Content";
    private String mContent = "???";

    @ViewInject(R.id.pullToRefreshPinnedSectionListView)
    PullToRefreshPinnedSectionListView pullToRefreshPinnedSectionListView;

    private int curTradeMenu = 0;//交易明细菜单：0-全部，1-进行中，2-完成
    private int curPage = 1;
    private CommissionAdapter adapter;
    private WSCommission wsCommissionHandler;
    private int from;//推广来源  0普通 1达人


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_commission, container, false);
        ViewUtils.inject(this, view);
        initData();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    public static CommissionFragment newInstance(String content) {
        CommissionFragment fragment = new CommissionFragment();
        fragment.mContent = content;
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * -----------分割线--------------
     */

    private void initData() {
        wsCommissionHandler = new WSCommission();
        wsCommissionHandler.setWSCommissionModelCallBack(this);

        ArrayList<Commission> list = new ArrayList<>();
        adapter = new CommissionAdapter(act, list);

        pullToRefreshPinnedSectionListView.getRefreshableView().setAdapter(adapter);
        pullToRefreshPinnedSectionListView.setOnRefreshListener(this);
        pullToRefreshPinnedSectionListView.setMode(PullToRefreshBase.Mode.BOTH);

        adapter.setCallBack(this);

        onPullDownToRefresh(pullToRefreshPinnedSectionListView);
    }

    @Override
    public void onTitleChange(int index) {
        curPage = 1;//每次切换菜单时页码从头开始
        curTradeMenu = index;
        adapter.getList().clear();//切换菜单时先清空列表
        adapter.setCurIndex(index);
        adapter.notifyDataSetChanged();
        wsCommissionHandler.getList(member, curPage, curTradeMenu, from);//发送请求获取list数据
    }

    /**
     * 请求失败
     */
    @Override
    public void onWSTeamError(String errcode) {
        pullToRefreshPinnedSectionListView.onRefreshComplete();
        if (curPage > 1) {
            pullToRefreshPinnedSectionListView.showNoMore();
        } else {
            adapter.getList().clear();
            adapter.notifyDataSetChanged();
            BaseFunc.showMsg(act, getString(R.string.no_data));
            adapter.setMenuDatas(null);//團隊成員人數 與 奖金收入 清零
        }
    }

    /**
     * 请求成功，处理数据
     */
    @Override
    public void onWSTeamSuccess(WSCommission data) {
        pullToRefreshPinnedSectionListView.onRefreshComplete();
        if (curPage > 1) {
            if (data != null && data.deduct_list != null && data.deduct_list.size() > 0) {
                adapter.addList(data.deduct_list);
                adapter.notifyDataSetChanged();
                pullToRefreshPinnedSectionListView.onAppendData(200);
                pullToRefreshPinnedSectionListView.resetPull(PullToRefreshBase.Mode.BOTH);
            } else {
                pullToRefreshPinnedSectionListView.showNoMore();
            }
        } else {
            pullToRefreshPinnedSectionListView.resetPull(PullToRefreshBase.Mode.BOTH);
            if (data != null) {
                adapter.setMenuDatas(data);
                adapter.setList(data.deduct_list);
                adapter.notifyDataSetChanged();
            } else {
                adapter.getList().clear();
                adapter.notifyDataSetChanged();
                BaseFunc.showMsg(act, getString(R.string.no_data));
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curPage = 1;
        wsCommissionHandler.getList(member, curPage, curTradeMenu, from);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curPage++;
        wsCommissionHandler.getList(member, curPage, curTradeMenu, from);
    }

}
