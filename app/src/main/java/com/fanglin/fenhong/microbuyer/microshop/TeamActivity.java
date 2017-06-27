package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.Team;
import com.fanglin.fenhong.microbuyer.base.model.WSTeam;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TeamAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 我的团队 Activity
 * By lizhixin
 */
public class TeamActivity extends BaseFragmentActivityUI implements View.OnClickListener, WSTeam.WSTeamModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.pullToRefreshListView)
    private PullToRefreshListView pullToRefreshListView;

    private View headerView;
    private TextView tvMenuAll, tvMenuB, tvMenuC, tvMenuD;
    private View sliderAll, sliderB, sliderC, sliderD;
    private TextView tvPeopleCount, tvCommIncome, tvTeamLbl, tvLevel1, tvLevel2, tvLevel3, tvVerifyCount;
    private LinearLayout LNums;

    private TeamAdapter adapter;
    private int curMenu = 0;//当前的活跃菜单项(0--全部队员，1--B级队员，2--C级队员 3--D级队员)
    private WSTeam wsTeamHandler;
    private int curPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadTitle(R.string.my_group);

        View view = View.inflate(this, R.layout.activity_my_team, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        headerView = View.inflate(mContext, R.layout.item_team_header, null);
        tvMenuAll = (TextView) headerView.findViewById(R.id.tv_all);
        tvMenuB = (TextView) headerView.findViewById(R.id.tv_B);
        tvMenuC = (TextView) headerView.findViewById(R.id.tv_C);
        tvMenuD = (TextView) headerView.findViewById(R.id.tv_D);

        sliderAll = headerView.findViewById(R.id.slider_all);
        sliderB = headerView.findViewById(R.id.slider_B);
        sliderC = headerView.findViewById(R.id.slider_C);
        sliderD = headerView.findViewById(R.id.slider_D);

        tvPeopleCount = (TextView) headerView.findViewById(R.id.tv_people_count);
        tvCommIncome = (TextView) headerView.findViewById(R.id.tv_commission_income);
        tvTeamLbl = (TextView) headerView.findViewById(R.id.tvTeamLbl);
        tvLevel1 = (TextView) headerView.findViewById(R.id.tvLevel1);
        tvLevel2 = (TextView) headerView.findViewById(R.id.tvLevel2);
        tvLevel3 = (TextView) headerView.findViewById(R.id.tvLevel3);
        tvVerifyCount = (TextView) headerView.findViewById(R.id.tvVerifyCount);

        LNums = (LinearLayout) headerView.findViewById(R.id.LNums);

        headerView.findViewById(R.id.ll_all).setOnClickListener(this);
        headerView.findViewById(R.id.ll_B).setOnClickListener(this);
        headerView.findViewById(R.id.ll_C).setOnClickListener(this);
        headerView.findViewById(R.id.ll_D).setOnClickListener(this);

        initData();
    }

    private void initData() {
        ArrayList<Team> list = new ArrayList<>();
        adapter = new TeamAdapter(this, list);

        pullToRefreshListView.setAdapter(adapter);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setShowIndicator(false);

        pullToRefreshListView.getRefreshableView().addHeaderView(headerView);

        setMenuDatas(null);

        wsTeamHandler = new WSTeam();
        wsTeamHandler.setWSTeamModelCallBack(this);

        if (member != null) {
            /** 刷新数据*/
            onPullDownToRefresh(pullToRefreshListView);
            refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curPage = 1;
        wsTeamHandler.getList(member, curPage, curMenu);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curPage++;
        wsTeamHandler.getList(member, curPage, curMenu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_all:
                if (curMenu != 0) {
                    //切换菜单时先清空列表
                    adapter.getList().clear();
                    adapter.notifyDataSetChanged();

                    changeMenuBackground(0);
                    curMenu = 0;
                    onPullDownToRefresh(pullToRefreshListView);
                    LNums.setVisibility(View.VISIBLE);
                    tvVerifyCount.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_B:
                if (curMenu != 1) {
                    //切换菜单时先清空列表
                    adapter.getList().clear();
                    adapter.notifyDataSetChanged();

                    changeMenuBackground(1);
                    curMenu = 1;
                    onPullDownToRefresh(pullToRefreshListView);
                    LNums.setVisibility(View.GONE);
                    tvVerifyCount.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_C:
                if (curMenu != 2) {
                    //切换菜单时先清空列表
                    adapter.getList().clear();
                    adapter.notifyDataSetChanged();

                    changeMenuBackground(2);
                    curMenu = 2;
                    onPullDownToRefresh(pullToRefreshListView);
                    LNums.setVisibility(View.GONE);
                    tvVerifyCount.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_D:
                if (curMenu != 3) {
                    //切换菜单时先清空列表
                    adapter.getList().clear();
                    adapter.notifyDataSetChanged();

                    changeMenuBackground(3);
                    curMenu = 3;
                    onPullDownToRefresh(pullToRefreshListView);
                    LNums.setVisibility(View.GONE);
                    tvVerifyCount.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 请求失败
     *
     * @param errcode 错误码
     */
    @Override
    public void onWSTeamError(String errcode) {
        pullToRefreshListView.onRefreshComplete();
        if (curPage > 1) {
            pullToRefreshListView.showNoMore();
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            setMenuDatas(null);//團隊成員人數 與 奖金收入 清零
        }
    }

    /**
     * 请求成功，处理数据
     *
     * @param data data
     */
    @Override
    public void onWSTeamSuccess(WSTeam data) {
        pullToRefreshListView.onRefreshComplete();
        refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        if (curPage > 1) {
            if (data != null && data.team_list != null && data.team_list.size() > 0) {
                adapter.addList(data.team_list);
                adapter.notifyDataSetChanged();
            } else {
                pullToRefreshListView.showNoMore();
            }
        } else {
            if (data != null) {
                pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
                setMenuDatas(data);
                adapter.setList(data.team_list);
                adapter.notifyDataSetChanged();
            } else {
                adapter.getList().clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 请求返回数据后 设置菜单项各个值
     *
     * @param data data
     */
    private void setMenuDatas(WSTeam data) {
        int people;
        String level1Str, level2Str, level3Str, verifyStr;
        double comm;
        if (data != null) {
            people = data.getPeopleCount(curMenu);
            comm = data.getCommissionCount(curMenu);
            level1Str = data.first_count + "人";
            level2Str = data.second_count + "人";
            level3Str = data.third_count + "人";
            verifyStr = data.getVerifyCountDesc();
        } else {
            people = 0;
            comm = 0.0;
            level1Str = "0人";
            level2Str = "0人";
            level3Str = "0人";
            verifyStr = "已认证：0人";
        }
        DecimalFormat df = new DecimalFormat("#0.00");

        String fmtPeo = getString(R.string.fmt_group_people);
        String resPeo = String.format(fmtPeo, people);
        String fmtComm = getString(R.string.fmt_group_comm);
        String resComm = String.format(fmtComm, df.format(comm));

        tvPeopleCount.setText(resPeo);
        tvCommIncome.setText(resComm);
        tvTeamLbl.setText(WSTeam.getLevelNumLbl(curMenu));
        tvLevel1.setText(level1Str);
        tvLevel2.setText(level2Str);
        tvLevel3.setText(level3Str);

        tvVerifyCount.setText(verifyStr);
    }


    private void changeMenuBackground(int index) {
        tvMenuAll.setSelected(index == 0);
        sliderAll.setBackgroundColor(getSelectedColor(index == 0));

        tvMenuB.setSelected(index == 1);
        sliderB.setBackgroundColor(getSelectedColor(index == 1));

        tvMenuC.setSelected(index == 2);
        sliderC.setBackgroundColor(getSelectedColor(index == 2));

        tvMenuD.setSelected(index == 3);
        sliderD.setBackgroundColor(getSelectedColor(index == 3));
    }

    public int getSelectedColor(boolean flag) {
        return flag ? getResources().getColor(R.color.fh_red) : getResources().getColor(R.color.white);
    }
}
