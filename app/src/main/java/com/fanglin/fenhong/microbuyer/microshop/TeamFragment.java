package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.model.Team;
import com.fanglin.fenhong.microbuyer.base.model.WSTeam;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TeamAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 我的团队 Fragment
 * Added By Plucky 2016-06-06 21:49
 */
public class TeamFragment extends BaseFragment implements WSTeam.WSTeamModelCallBack, PullToRefreshBase.OnRefreshListener2, View.OnClickListener {

    @ViewInject(R.id.pullToRefreshListView)
    private PullToRefreshListView pullToRefreshListView;

    private View headerView;
    private TextView tvMenuAll, tvMenuB, tvMenuC;
    private View sliderAll, sliderB, sliderC;
    private TextView tvPeopleCount, tvCommIncome,tvTeamLbl;

    private LinearLayout LNums;

    private TeamAdapter adapter;
    private int curMenu = 0;//当前的活跃菜单项(0--全部队员，1--B级队员，2--C级队员)
    private WSTeam wsTeamHandler;
    private int curPage = 1;

    private static final String KEY_CONTENT = "TeamFragment:Content";
    private String mContent = "???";

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
        View view = inflater.inflate(R.layout.activity_my_team, container, false);
        headerView = View.inflate(act, R.layout.item_team_header, null);
        tvMenuAll = (TextView) headerView.findViewById(R.id.tv_all);
        tvMenuB = (TextView) headerView.findViewById(R.id.tv_B);
        tvMenuC = (TextView) headerView.findViewById(R.id.tv_C);

        sliderAll = headerView.findViewById(R.id.slider_all);
        sliderB = headerView.findViewById(R.id.slider_B);
        sliderC = headerView.findViewById(R.id.slider_C);

        headerView.findViewById(R.id.ll_all).setOnClickListener(this);
        headerView.findViewById(R.id.ll_B).setOnClickListener(this);
        headerView.findViewById(R.id.ll_C).setOnClickListener(this);

        tvPeopleCount = (TextView) headerView.findViewById(R.id.tv_people_count);
        tvCommIncome = (TextView) headerView.findViewById(R.id.tv_commission_income);
        tvTeamLbl = (TextView) headerView.findViewById(R.id.tvTeamLbl);

        LNums = (LinearLayout) headerView.findViewById(R.id.LNums);

        ViewUtils.inject(this, view);
        initData();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    public static TeamFragment newInstance(String content) {
        TeamFragment fragment = new TeamFragment();
        fragment.mContent = content;
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void initData() {
        ArrayList<Team> list = new ArrayList<>();
        adapter = new TeamAdapter(act, list);

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
        } else {
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
        double comm;
        if (data != null) {
            people = data.getPeopleCount(curMenu);
            comm = data.getCommissionCount(curMenu);
        } else {
            people = 0;
            comm = 0.0;
        }
        DecimalFormat df = new DecimalFormat("#0.00");

        String fmtPeo = getString(R.string.fmt_group_people);
        String resPeo = String.format(fmtPeo, people);
        String fmtComm = getString(R.string.fmt_group_comm);
        String resComm = String.format(fmtComm, df.format(comm));

        tvPeopleCount.setText(resPeo);
        tvCommIncome.setText(resComm);
        tvTeamLbl.setText(WSTeam.getLevelNumLbl(curMenu));
    }

    private void changeMenuBackground(int index) {
        if (index == 0) {//全部队员
            tvMenuAll.setTextColor(getResources().getColor(R.color.fh_red));
            sliderAll.setBackgroundColor(getResources().getColor(R.color.fh_red));

            tvMenuB.setTextColor(getResources().getColor(R.color.color_33));
            sliderB.setBackgroundColor(getResources().getColor(R.color.white));

            tvMenuC.setTextColor(getResources().getColor(R.color.color_33));
            sliderC.setBackgroundColor(getResources().getColor(R.color.white));

        } else if (index == 1) {//B级队员
            tvMenuAll.setTextColor(getResources().getColor(R.color.color_33));
            sliderAll.setBackgroundColor(getResources().getColor(R.color.white));

            tvMenuB.setTextColor(getResources().getColor(R.color.fh_red));
            sliderB.setBackgroundColor(getResources().getColor(R.color.fh_red));

            tvMenuC.setTextColor(getResources().getColor(R.color.color_33));
            sliderC.setBackgroundColor(getResources().getColor(R.color.white));

        } else {//index == 2 C级队员
            tvMenuAll.setTextColor(getResources().getColor(R.color.color_33));
            sliderAll.setBackgroundColor(getResources().getColor(R.color.white));

            tvMenuB.setTextColor(getResources().getColor(R.color.color_33));
            sliderB.setBackgroundColor(getResources().getColor(R.color.white));

            tvMenuC.setTextColor(getResources().getColor(R.color.fh_red));
            sliderC.setBackgroundColor(getResources().getColor(R.color.fh_red));
        }
    }
}
