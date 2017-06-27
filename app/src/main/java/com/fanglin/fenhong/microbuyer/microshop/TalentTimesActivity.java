package com.fanglin.fenhong.microbuyer.microshop;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.TalentImagesDetail;
import com.fanglin.fenhong.microbuyer.base.model.TalentShare;
import com.fanglin.fenhong.microbuyer.base.model.TalentShareFlag;
import com.fanglin.fenhong.microbuyer.base.model.TalentTimesGroup;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TalentTimesAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/22-下午1:24.
 * 功能描述: 达人的时光 按月日分组
 */
public class TalentTimesActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, TalentTimesGroup.TalentTimesGroupCallBack, TalentTimesAdapter.TalentTimesAdapterListener {

    @ViewInject(R.id.pinnedListView)
    PullToRefreshPinnedHeaderListView pinnedListView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    TalentTimesAdapter adapter;
    TalentTimesGroup groupReq;
    private int curpage = 1;
    private String talentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skipChk=true;
        View view = View.inflate(mContext, R.layout.activity_talenttimes, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        talentId = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        pinnedListView.setMode(PullToRefreshBase.Mode.BOTH);
        pinnedListView.setScrollingWhileRefreshingEnabled(true);
        pinnedListView.setOnRefreshListener(this);
        pinnedListView.getRefreshableView().setPinHeaders(false);
        pinnedListView.getRefreshableView().setBackUpView(btnBackTop, 7);

        adapter = new TalentTimesAdapter(mContext);
        adapter.setListener(this);
        pinnedListView.getRefreshableView().setAdapter(adapter);

        groupReq = new TalentTimesGroup();
        groupReq.setModelCallBack(this);

        onPullDownToRefresh(pinnedListView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        groupReq.getList(member, talentId, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        groupReq.getList(member, talentId, curpage);
    }

    @Override
    public void onTalentTimesList(List<TalentImagesDetail> list) {
        pinnedListView.onRefreshComplete();
        if (curpage == 1) {
            pinnedListView.resetPull(PullToRefreshBase.Mode.BOTH);
            if (list != null && list.size() > 0) {
                String atitle = list.get(0).getTalentTimesTitle();
                setHeadTitle(atitle);

                List<TalentTimesGroup> groups = TalentTimesGroup.parseTimesArrayByDay(list);
                adapter.setList(groups);
                refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            }
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            if (list != null && list.size() > 0) {
                List<TalentTimesGroup> groups = TalentTimesGroup.parseTimesArrayByDay(list);
                adapter.addList(groups);
                pinnedListView.onAppendData(200);
            } else {
                pinnedListView.showNoMore();
            }
        }
    }

    @Override
    public void onMore(View view, final int section, final int position) {
        TalentImagesDetail detail = adapter.getItem(section, position);
        new LayoutTalentMore(this, view).setCallBack(new LayoutTalentMore.LayoutTalentMoreCallBack() {
            @Override
            public void onDeleteResult(boolean isSuccess) {
                if (isSuccess) {
                    adapter.removeItem(section, position);
                }
            }
        }).show(detail);
    }

    @Override
    public void onItemClick(String talentId) {
        BaseFunc.gotoTalentImageDetail(mContext, talentId, false);
    }

    @Override
    public void onShare(final View view, int section, int position) {
        TalentImagesDetail detail = adapter.getItem(section, position);
        if (detail != null) {
            TalentShare share = detail.getShare();
            if (share != null) {
                TalentShareFlag talentShareFlag = new TalentShareFlag();
                talentShareFlag.type = "time";
                talentShareFlag.share_id = detail.getTime_id();

                ShareData shareData = new ShareData();
                shareData.title = share.getTitle();
                shareData.content = share.getContent();
                shareData.imgs = share.getImage();
                shareData.url = share.getUrl();
                shareData.talentShareFlag = talentShareFlag;
                view.setEnabled(false);
                ShareData.fhShare(this, shareData, null).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        view.setEnabled(true);
                    }
                });
            }
        }
    }

    @Override
    public void onComment(int section, int position) {
        TalentImagesDetail detail = adapter.getItem(section, position);
        if (detail != null) {
            BaseFunc.gotoTalentImageDetail(this, detail.getTime_id(), true);
        }
    }

    @Override
    public void onCollect(int section, int position) {
        if (member == null) {
            BaseFunc.gotoLogin(this);
            return;
        }

        final TalentImagesDetail detail = adapter.getItem(section, position);
        if (detail == null) return;
        final boolean hasCollect = detail.isCollected();
        APIUtil.FHAPICallBack fhapiCallBack = new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    if (!hasCollect) {
                        BaseFunc.showMsg(mContext, "已收藏");
                        detail.setIs_collected("1");
                        detail.addCollect();
                        adapter.notifyDataSetChanged();
                    } else {
                        detail.setIs_collected("0");
                        detail.minusCollect();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };
        if (hasCollect) {
            new BaseBO().setCallBack(fhapiCallBack).deleteFavorites(member, detail.getTime_id(), "time");
        } else {
            new BaseBO().setCallBack(fhapiCallBack).addFavorites(member, detail.getTime_id(), "time");
        }
    }

    @Override
    public void onImageClick(ViewGroup view, int section, int position) {
        if (view != null) {
            if (view.getTag() != null) {
                view.setTag(null);
            } else {
                view.setTag(1);
            }
            BaseFunc.setVisibleOfViewGroup(view, view.getTag() == null);
        }
    }
}
