package com.fanglin.fenhong.microbuyer.microshop;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.TalentImagesDetail;
import com.fanglin.fenhong.microbuyer.base.model.TalentShare;
import com.fanglin.fenhong.microbuyer.base.model.TalentShareFlag;
import com.fanglin.fhui.FHHintDialog;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/22-下午4:01.
 * 功能描述: 时光列表更多按钮下拉框 { 分享、删除}
 */
public class LayoutTalentMore implements View.OnClickListener {
    private PopupWindow pw;
    private View attachView;
    private TalentImagesDetail detail;
    private BaseFragmentActivity baseFragmentActivity;

    public LayoutTalentMore(BaseFragmentActivity activity, View attachView) {
        this.baseFragmentActivity = activity;
        this.attachView = attachView;

        View view = View.inflate(attachView.getContext(), R.layout.layout_talent_more, null);
        view.findViewById(R.id.tvShare).setOnClickListener(this);
        view.findViewById(R.id.tvDelete).setOnClickListener(this);
        pw = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new ColorDrawable());
        pw.update();
    }

    public void show(TalentImagesDetail detail) {
        this.detail = detail;
        pw.showAsDropDown(attachView);
    }

    @Override
    public void onClick(View v) {
        pw.dismiss();
        switch (v.getId()) {
            case R.id.tvShare:
                if (detail != null) {
                    TalentShare talentShare = detail.getShare();
                    if (talentShare != null) {
                        TalentShareFlag talentShareFlag = new TalentShareFlag();
                        talentShareFlag.type = "time";
                        talentShareFlag.share_id = detail.getTime_id();

                        ShareData shareData = new ShareData();
                        shareData.title = talentShare.getTitle();
                        shareData.content = talentShare.getContent();
                        shareData.imgs = talentShare.getImage();
                        shareData.url = talentShare.getUrl();
                        shareData.talentShareFlag = talentShareFlag;
                        ShareData.fhShare(baseFragmentActivity, shareData, null);
                    }
                }
                break;
            case R.id.tvDelete:
                onDelete();
                break;
        }
    }


    private void onDelete() {
        if (baseFragmentActivity == null || detail == null)
            return;
        if (baseFragmentActivity.member == null) {
            BaseFunc.gotoLogin(baseFragmentActivity);
            return;
        }
        FHHintDialog dialog = new FHHintDialog(baseFragmentActivity);
        dialog.setTvContent(baseFragmentActivity.getString(R.string.confirm_deletetimes));
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
                        if (callBack != null) {
                            callBack.onDeleteResult(isSuccess);
                        }
                    }
                }).deleteTime(baseFragmentActivity.member, detail.getTime_id());
            }
        });
        dialog.show();
    }

    private LayoutTalentMoreCallBack callBack;

    public LayoutTalentMore setCallBack(LayoutTalentMoreCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public interface LayoutTalentMoreCallBack {
        void onDeleteResult(boolean isSuccess);
    }
}
