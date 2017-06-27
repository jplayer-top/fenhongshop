package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.TalentHomeRequest;
import com.fanglin.fenhong.microbuyer.base.model.TalentInfo;
import com.fanglin.fenhong.microbuyer.base.model.TalentShare;
import com.fanglin.fenhong.microbuyer.base.model.TalentShareFlag;
import com.fanglin.fenhong.microbuyer.base.model.TimeImagesGroup;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TalentPinnedSectionAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/7-下午2:17.
 * 功能描述: 达人首页
 */
public class TalentActivity extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener2, AbsListView.OnScrollListener, TalentHomeRequest.THReqModelCallBack, TalentPinnedSectionAdapter.TalentPinnedSectionAdapterListener {

    @ViewInject(R.id.talentPinnedListView)
    PullToRefreshPinnedHeaderListView talentPinnedListView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;
    TalentPinnedSectionAdapter adapter;
    @ViewInject(R.id.ivCamera)
    ImageView ivCamera;
    @ViewInject(R.id.ivBg)
    ImageView ivBg;

    TalentHomeRequest homeRequest;
    int curpage = 1;
    private String talentId;
    ShareData shareData;
    TalentShareFlag talentShareFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent);
        ViewUtils.inject(this);
        talentId = getIntent().getStringExtra("VAL");

        talentShareFlag = new TalentShareFlag();
        talentShareFlag.type = "talent";
        talentShareFlag.share_id = talentId;

        initView();
    }


    private void initView() {
        ivCamera.setVisibility(View.GONE);

        talentPinnedListView.setMode(PullToRefreshBase.Mode.BOTH);
        talentPinnedListView.setScrollingWhileRefreshingEnabled(true);
        talentPinnedListView.setOnRefreshListener(this);
        talentPinnedListView.getRefreshableView().setOnScrollListener(this);

        adapter = new TalentPinnedSectionAdapter(mContext);
        adapter.setListener(this);
        talentPinnedListView.getRefreshableView().setAdapter(adapter);

        talentPinnedListView.getRefreshableView().setPinHeaders(false);
        talentPinnedListView.getRefreshableView().setBackUpView(btnBackTop, 7);

        homeRequest = new TalentHomeRequest();
        homeRequest.setModelCallBack(this);

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPullDownToRefresh(talentPinnedListView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        homeRequest.getHomeData(curpage, member, talentId);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        homeRequest.getHomeData(curpage, member, talentId);
    }

    @Override
    public void onTHReqData(TalentHomeRequest data) {
        talentPinnedListView.onRefreshComplete();
        if (curpage == 1) {
            if (data != null) {

                TalentShare talentShare = data.getShare();
                if (talentShare != null) {
                    /** 设定分享格式*/
                    shareData = new ShareData();
                    shareData.content = talentShare.getContent();
                    shareData.url = talentShare.getUrl();
                    shareData.title = talentShare.getTitle();
                    shareData.imgs = talentShare.getImage();
                    shareData.talentShareFlag = talentShareFlag;
                }

                List<TimeImagesGroup> groups = TimeImagesGroup.parseImagesArrayByMonth(data.getTime_images());
                adapter.setList(groups);
                TalentInfo talentInfo = data.getTalent_info();
                if (talentInfo != null) {
                    new FHImageViewUtil(ivBg).setImageURI(talentInfo.getTalent_background(), FHImageViewUtil.SHOWTYPE.TALENTBG);
                    ivBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                if (talentInfo != null && talentInfo.getIs_own() == 1) {
                    ivCamera.setVisibility(View.VISIBLE);
                } else {
                    ivCamera.setVisibility(View.GONE);
                }
                adapter.setTalentInfo(talentInfo);
                adapter.notifyDataSetChanged();
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                talentPinnedListView.resetPull(PullToRefreshBase.Mode.BOTH);
            } else {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        } else {
            if (data != null && data.getTime_images() != null && data.getTime_images().size() > 0) {
                List<TimeImagesGroup> groups = TimeImagesGroup.parseImagesArrayByMonth(data.getTime_images());
                adapter.addList(groups);
                adapter.notifyDataSetChanged();
                talentPinnedListView.onAppendData(200);
            } else {
                talentPinnedListView.showNoMore();
            }
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }

    }

    /**
     * 滑动偏移计算
     * @param view        listview
     * @param scrollState 停止\滑动\等等
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        View c = view.getChildAt(0);
        if (c != null) {
            int origin = Math.abs(c.getTop());
            int dy = origin > 120 ? 120 : origin;

            float tAlpha = (100 * dy / 120) * 0.01F;
            float bAlpha = (100 * dy / 120) * 0.01F;
            adapter.setAlpha(tAlpha, bAlpha);
            adapter.notifyDataSetChanged();

        }
    }

    @OnClick(value = {R.id.ivBack, R.id.ivCamera, R.id.ivShare})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivCamera:
                if (member == null) {
                    BaseFunc.gotoLogin(mContext);
                    return;
                }
                BaseFunc.gotoActivity(mContext, EditTalentImageActivity.class, null);
                break;
            case R.id.ivShare:
                if (shareData != null) {
                    ShareData.fhShare(this, shareData, null);
                }
                break;
        }
    }

    @Override
    public void onEdit(TalentInfo talentInfo) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }
        if (talentInfo != null) {
            BaseFunc.gotoActivity(mContext, EditTalentInfoActivity.class, new Gson().toJson(talentInfo));
        }
    }

    @Override
    public void onCommission() {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }
        BaseFunc.gotoActivity(mContext, CommissionActivity.class, "1");
    }

    @Override
    public void onFocus(String talentId, final boolean hasFocus) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    adapter.refreshFocus(!hasFocus);
                }
            }
        }).followTalent(member, talentId, hasFocus ? "1" : "0");
    }

    @Override
    public void onTimes(String id) {
        BaseFunc.gotoActivity(mContext, TalentTimesActivity.class, talentId);
    }

    @Override
    public void onFans(String talentId) {
        BaseFunc.gotoActivity(mContext, FansListActivity.class, talentId);
    }

    @Override
    public void onGoods(String talentId) {
        BaseFunc.gotoActivity(mContext, TalentLoveGoodsActivity.class, talentId);
    }
}
