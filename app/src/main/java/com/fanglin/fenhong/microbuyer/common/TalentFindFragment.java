package com.fanglin.fenhong.microbuyer.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.event.WifiUnconnectHintAfter;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.TalentFindReq;
import com.fanglin.fenhong.microbuyer.base.model.TalentImagesDetail;
import com.fanglin.fenhong.microbuyer.base.model.TalentInfo;
import com.fanglin.fenhong.microbuyer.base.model.TalentShare;
import com.fanglin.fenhong.microbuyer.base.model.TalentShareFlag;
import com.fanglin.fenhong.microbuyer.common.adapter.TalentFindAdapter;
import com.fanglin.fenhong.microbuyer.microshop.EditTalentImageActivity;
import com.fanglin.fenhong.microbuyer.microshop.TalentActivity;
import com.fanglin.fhlib.other.FHLib;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/22-下午8:37.
 * 功能描述: 达人发现页面  新版
 */
public class TalentFindFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, TalentFindReq.TalentFindReqCallBack, TalentFindAdapter.TalentFindAdapterListener {

    public static final int REFRESHREQ = 0x001;

    private View view;
    @ViewInject(R.id.ivCamera)
    ImageView ivCamera;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    @ViewInject(R.id.pinnedTalentFindListView)
    PullToRefreshPinnedHeaderListView pinnedTalentFindListView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    TalentFindAdapter adapter;
    TalentFindReq talentFindReq;

    private int curpage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(act, R.layout.fragment_talentfind, null);
        ViewUtils.inject(this, view);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        /**
         * 只在第一次加载的时候去判断是否断网，来显示这两个控件
         */
        if (FHLib.isNetworkConnected(getActivity()) == 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }

        adapter = new TalentFindAdapter(act);
        adapter.setListener(this);

        pinnedTalentFindListView.setMode(PullToRefreshBase.Mode.BOTH);
        pinnedTalentFindListView.setScrollingWhileRefreshingEnabled(true);
        pinnedTalentFindListView.setOnRefreshListener(this);
        pinnedTalentFindListView.getRefreshableView().setAdapter(adapter);
        pinnedTalentFindListView.getRefreshableView().setBackUpView(btnBackTop, 7);
        pinnedTalentFindListView.getRefreshableView().setPinHeaders(false);

        talentFindReq = new TalentFindReq();
        talentFindReq.setModelCallBack(this);

        //添加点击事件
        View emptyView = multiStateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        if (emptyView != null) {
            TextView tvEmpty = (TextView) emptyView.findViewById(R.id.tvEmpty);
            if (tvEmpty != null) {
                tvEmpty.setText(getString(R.string.lbl_empty_click));
            }
            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    onPullDownToRefresh(pinnedTalentFindListView);
                }
            });
        }

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        onPullDownToRefresh(pinnedTalentFindListView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleNoWfi(WifiUnconnectHintAfter wifiUnconnectHintEntity) {
        if (adapter.getSectionCount() == 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            onPullDownToRefresh(pinnedTalentFindListView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (member != null && member.talent_id > 0) {
            ivCamera.setVisibility(View.VISIBLE);
        } else {
            ivCamera.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(value = {R.id.ivCamera})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivCamera:
                BaseFunc.gotoActivity4Result(this, EditTalentImageActivity.class, null, REFRESHREQ);
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        talentFindReq.getList(member, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        talentFindReq.getList(member, curpage);
    }

    @Override
    public void onTalentFindList(List<TalentImagesDetail> times) {
        pinnedTalentFindListView.onRefreshComplete();
        if (curpage > 1) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            if (times != null && times.size() > 0) {
                adapter.addTimes(times);
                pinnedTalentFindListView.onAppendData(200);
            } else {
                pinnedTalentFindListView.showNoMore();
            }
        } else {
            pinnedTalentFindListView.resetPull(PullToRefreshBase.Mode.BOTH);
            adapter.setTimes(times);
            if (times != null && times.size() > 0) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }
    }

    @Override
    public void onItemClick(int section, int position) {
        TalentImagesDetail detail = adapter.getItem(section, position);
        if (detail != null) {
            BaseFunc.gotoTalentImageDetail(act, detail.getTime_id(), false);
        }
    }

    @Override
    public void onShareItem(final View view, int section, int position) {
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
                ShareData.fhShare(act, shareData, null).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        view.setEnabled(true);
                    }
                });
            }
        }
    }

    @Override
    public void onCommentItem(int section, int position) {
        TalentImagesDetail detail = adapter.getItem(section, position);
        if (detail != null) {
            BaseFunc.gotoTalentImageDetail(act, detail.getTime_id(), true);
        }
    }

    @Override
    public void onCollectItem(int section, int position) {
        if (member == null) {
            BaseFunc.gotoLogin(act);
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
                        BaseFunc.showMsg(act, "已收藏");
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
    public void onHeaderClick(int section, int position) {
        TalentImagesDetail detail = adapter.getItem(section, position);
        if (detail != null) {
            TalentInfo info = detail.getTalent();
            if (info != null) {
                BaseFunc.gotoActivity(act, TalentActivity.class, info.getTalent_id());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFRESHREQ && resultCode == Activity.RESULT_OK) {
            onPullDownToRefresh(pinnedTalentFindListView);
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
            BaseFunc.setVisibleOfViewGroup(view,view.getTag() == null);
        }
    }
}
