package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.TalentImageComment;
import com.fanglin.fenhong.microbuyer.base.model.TalentImagesDetail;
import com.fanglin.fenhong.microbuyer.base.model.TalentInfo;
import com.fanglin.fenhong.microbuyer.base.model.TalentShare;
import com.fanglin.fenhong.microbuyer.base.model.TalentShareFlag;
import com.fanglin.fenhong.microbuyer.common.LayoutMoreVertical;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TalentImagesDetailPinnedSectionAdapter;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhui.FHHintDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/12-下午4:49.
 * 功能描述: 达人详情页
 */
public class TalentImagesDetailActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2, ViewTreeObserver.OnGlobalLayoutListener, TalentImagesDetail.TIDModelCallBack, TalentImagesDetailPinnedSectionAdapter.TalentImagesDetailAdapterCallBack, LayoutMoreVertical.LayoutMoreVerticalListener {

    @ViewInject(R.id.btnSubmit)
    Button btnSubmit;
    @ViewInject(R.id.btnSend)
    Button btnSend;

    @ViewInject(R.id.imagePinnedListView)
    PullToRefreshPinnedHeaderListView imagePinnedListView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;
    @ViewInject(R.id.etMessage)
    EditText etMessage;
    TalentImagesDetailPinnedSectionAdapter adapter;

    int curpage = 1;
    boolean isEvaluate;
    String imageId;
    TalentImagesDetail detailReq;
    ShareData shareData;
    LayoutMoreVertical layoutMoreVertical;
    LayoutTimeCommentDialog timeCommentDialog;

    TalentShareFlag talentShareFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skipChk = true;
        View view = View.inflate(mContext, R.layout.activity_talent_detail, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);

        imageId = getIntent().getStringExtra("TIMEID");
        isEvaluate = getIntent().getBooleanExtra("ISEVALUATE", false);
        initView();
    }

    private void initView() {
        btnSubmit.setTypeface(iconfont);
        setHeadTitle(R.string.talent_timesdtl);
        enableIvMore(0);
        enableTvExtra(R.string.if_share, true);
        onEmptyView(R.string.tips_talentimage_deleted, 0, null);

        talentShareFlag = new TalentShareFlag();
        talentShareFlag.type = "time";
        talentShareFlag.share_id = imageId;

        layoutMoreVertical = new LayoutMoreVertical(vBottomLine);
        layoutMoreVertical.setShareData(null);
        layoutMoreVertical.setIsSearchShow(false);
        layoutMoreVertical.setListener(this);

        timeCommentDialog = new LayoutTimeCommentDialog(mContext);

        LHold.getViewTreeObserver().addOnGlobalLayoutListener(this);

        //刷新控件
        imagePinnedListView.setMode(PullToRefreshBase.Mode.BOTH);
        imagePinnedListView.setScrollingWhileRefreshingEnabled(true);
        imagePinnedListView.setOnRefreshListener(this);
        imagePinnedListView.getRefreshableView().setBackUpView(btnBackTop, 7);

        adapter = new TalentImagesDetailPinnedSectionAdapter(mContext);
        adapter.setCallBack(this);
        imagePinnedListView.getRefreshableView().setAdapter(adapter);
        imagePinnedListView.getRefreshableView().setPinHeaders(false);

        detailReq = new TalentImagesDetail();
        detailReq.setModelCallBack(this);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @OnClick(value = {R.id.btnSubmit, R.id.btnSend})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                doCollect();
                break;
            case R.id.btnSend:
                if (comment != null) {
                    addComment(comment.getComment_id());
                } else {
                    addComment("0");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            layoutMoreVertical.setMsgNum(msgnum.getTotalNum());
            enableMsgDot(true);
        } else {
            enableMsgDot(false);
            layoutMoreVertical.setMsgNum(0);
        }
        adapter.refreshMember(member);
        onPullDownToRefresh(imagePinnedListView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        detailReq.getData(imageId, curpage, member);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        detailReq.getData(imageId, curpage, member);
    }

    @Override
    public void onTIDModelData(TalentImagesDetail detail) {
        imagePinnedListView.onRefreshComplete();
        if (curpage > 1) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            //加载更多
            if (detail != null) {
                if (detail.getComment() != null && detail.getComment().size() > 0) {
                    adapter.addList(detail.getComment());
                    adapter.notifyDataSetChanged();
                    imagePinnedListView.onAppendData(200);
                } else {
                    imagePinnedListView.showNoMore();
                }
            } else {
                imagePinnedListView.showNoMore();
            }
        } else {
            //刷新
            if (detail != null) {
                adapter.setData(detail);
                hasCollect = detail.hasCollect();
                if (hasCollect) {
                    btnSubmit.setText(getString(R.string.talent_collected));
                } else {
                    btnSubmit.setText(getString(R.string.talent_collect));
                }

                boolean isOwn = detail.getTalent() != null && detail.getTalent().getIs_own() == 1;
                if (isOwn) {
                    layoutMoreVertical.setDeleted(true);
                    layoutMoreVertical.setJubao(false);
                } else {
                    layoutMoreVertical.setDeleted(false);
                    layoutMoreVertical.setJubao(true);
                }

                shareData = new ShareData();
                TalentShare talentShare = detail.getShare();
                if (talentShare != null) {
                    shareData.title = talentShare.getTitle();
                    shareData.content = talentShare.getContent();
                    shareData.imgs = talentShare.getImage();
                    shareData.url = talentShare.getUrl();
                    shareData.talentShareFlag = talentShareFlag;
                }

                adapter.notifyDataSetChanged();
                imagePinnedListView.resetPull(PullToRefreshBase.Mode.BOTH);
                refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);

                /**
                 * 定位到评论
                 */
                if (isEvaluate) {
                    imagePinnedListView.getRefreshableView().setSelection(imagePinnedListView.getRefreshableView().getCount() - 1);
                    openSoftKeyBoard();
                    isEvaluate = false;
                }
            } else {
                refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
            }
        }
    }

    @Override
    public void onTvExtraClick() {
        super.onTvExtraClick();
        if (shareData == null) return;
        ShareData.fhShare(this, shareData, null);
    }

    @Override
    public void onivMoreClick() {
        super.onivMoreClick();
        layoutMoreVertical.show();
    }


    /**
     * 通过监听界面布局的变化来判断输入法是否弹出
     */
    @Override
    public void onGlobalLayout() {
        int heightDiff = LHold.getRootView().getHeight() - LHold.getHeight();
        if (heightDiff > 300) {
            btnSubmit.setVisibility(View.GONE);
            btnSend.setVisibility(View.VISIBLE);
            if (comment != null) {
                String fmt = String.format(getString(R.string.hint_reply_comment), comment.getComment_member_name());
                etMessage.setHint(fmt);
            } else {
                etMessage.setHint(getString(R.string.hint_publish_comment));
            }
        } else {
            btnSubmit.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
            comment = null;
            etMessage.setHint(getString(R.string.hint_publish_comment));
        }
    }


    @Override
    public void onFocus(final boolean isFollowed) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }
        String talentId = null;
        if (adapter.getTalentInfo() != null) {
            talentId = adapter.getTalentInfo().getTalent_id();
        }
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    adapter.refreshFocus(!isFollowed);
                }
            }
        }).followTalent(member, talentId, isFollowed ? "1" : "0");
    }

    private boolean hasCollect = false;

    private void doCollect() {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }
        APIUtil.FHAPICallBack fhapiCallBack = new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    hasCollect = !hasCollect;
                    if (hasCollect) {
                        btnSubmit.setText(getString(R.string.talent_collected));
                    } else {
                        btnSubmit.setText(getString(R.string.talent_collect));
                    }
                }
            }
        };
        if (hasCollect) {
            new BaseBO().setCallBack(fhapiCallBack).deleteFavorites(member, imageId, "time");
        } else {
            new BaseBO().setCallBack(fhapiCallBack).addFavorites(member, imageId, "time");
        }
    }

    private TalentImageComment comment;

    private void openSoftKeyBoard() {
        etMessage.setFocusable(true);
        etMessage.requestFocus();
        InputMethodManager im = BaseFunc.getInputMethodManager(mContext);
        im.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void onComment(final TalentImageComment comment) {
        this.comment = comment;
        if (comment == null) return;
        timeCommentDialog.setListener(new LayoutTimeCommentDialog.LayoutTimeCommentDialogListener() {
            @Override
            public void onReply() {
                openSoftKeyBoard();
            }

            @Override
            public void onCopy() {
                FHLib.copy(mContext, comment.getComment_message());
                BaseFunc.showMsg(mContext, getString(R.string.already_copy));
            }

            @Override
            public void onjuBao() {
                BaseFunc.juBao(TalentImagesDetailActivity.this,
                        comment.getComment_member_name(),
                        "time_comment",
                        comment.getComment_id());
            }
        });
        timeCommentDialog.show();
    }

    private void hideSoft() {
        InputMethodManager im = BaseFunc.getInputMethodManager(this);
        if (im.isActive()) {
            im.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
        }
    }

    private void addComment(String commentId) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }
        if (etMessage.length() == 0) return;
        String comment = String.valueOf(etMessage.getText());

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    onPullDownToRefresh(imagePinnedListView);
                    etMessage.getText().clear();
                    hideSoft();
                }
            }
        }).addComment(member, imageId, comment, commentId);
    }

    @Override
    public void onDelete() {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }
        FHHintDialog dialog = new FHHintDialog(mContext);
        dialog.setTvContent(getString(R.string.confirm_deletetimes));
        dialog.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
                    @Override
                    public void onStart(String data) {

                    }

                    @Override
                    public void onEnd(boolean isSuccess, String data) {
                        if (isSuccess) {
                            finish();
                        }
                    }
                }).deleteTime(member, imageId);
            }
        });
        dialog.show();
    }

    @Override
    public void onJubao() {
        TalentInfo talentInfo = adapter.getTalentInfo();
        if (talentInfo != null) {
            BaseFunc.juBao(this, talentInfo.getTalent_name(), "time", imageId);
        }

    }

    @Override
    public void onTalent(String talentId) {
        BaseFunc.gotoActivity(mContext, TalentActivity.class, talentId);
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
