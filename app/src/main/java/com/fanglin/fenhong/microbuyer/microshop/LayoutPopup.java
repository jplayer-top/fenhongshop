package com.fanglin.fenhong.microbuyer.microshop;

import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.PopupInfo;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/1-上午10:02.
 * 普通弹窗 针对的需求有点多，有点混乱
 */
public abstract class LayoutPopup implements View.OnClickListener {
    private Dialog fhDialog;
    private TextView tvTitle, tvContent, tvCancel, tvConfirm, tvLink;
    private CheckBox checkBox;
    private LinearLayout LCheckBox;

    private String linkUrl, cancelUrl, confirmUrl;
    public BaseFragmentActivity activity;

    public LayoutPopup(BaseFragmentActivity activity) {
        this.activity = activity;
        View view = View.inflate(activity, R.layout.layout_normal_popup, null);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvConfirm = (TextView) view.findViewById(R.id.tvConfirm);

        LCheckBox = (LinearLayout) view.findViewById(R.id.LCheckBox);
        tvLink = (TextView) view.findViewById(R.id.tvLink);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvLink.setOnClickListener(this);

        fhDialog = new Dialog(activity, R.style.InnerDialog);
        fhDialog.setContentView(view);
    }

    public void refreshData(PopupInfo popupInfo) {
        if (popupInfo != null) {
            tvTitle.setText(popupInfo.getTitle());
            tvContent.setText(popupInfo.getDescription());
            if (!TextUtils.isEmpty(popupInfo.getLinkTitle())) {
                tvLink.setText(popupInfo.getLinkTitleDesc());
                LCheckBox.setVisibility(View.VISIBLE);
            } else {
                LCheckBox.setVisibility(View.GONE);
            }
            linkUrl = popupInfo.getLink_url();


            List<PopupInfo.PopUpButton> buttons = popupInfo.getButton();
            if (buttons != null && buttons.size() > 1) {
                cancelUrl = buttons.get(0).getButton_url();
                tvCancel.setText(buttons.get(0).getButton_name());
                confirmUrl = buttons.get(1).getButton_url();
                tvConfirm.setText(buttons.get(1).getButton_name());
            }
        }
    }

    public void show() {
        fhDialog.show();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        fhDialog.setOnDismissListener(listener);
    }

    public void onCancel(View v) {
        fhDialog.dismiss();
        if (BaseFunc.isValidUrl(cancelUrl)) {
            BaseFunc.urlClick(v.getContext(), cancelUrl);
        }
    }

    public void dismiss() {
        fhDialog.dismiss();
    }

    //当返回URL时，除处理URL点击事件的其他事件
    public void onSubmitWithUrl() {

    }

    public void onConfirm(View v, String confirmUrl) {
        if (checkBox.isChecked()) {
            if (BaseFunc.isValidUrl(confirmUrl)) {
                fhDialog.dismiss();
                BaseFunc.urlClick(v.getContext(), confirmUrl);
                onSubmitWithUrl();
            } else {
                onSubmit(v);
            }
        } else {
            YoYo.with(Techniques.Shake).duration(700).playOn(LCheckBox);
        }
    }

    //不返回URL时执行的操作
    public abstract void onSubmit(View v);

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvCancel:
                onCancel(v);
                break;
            case R.id.tvConfirm:
                onConfirm(v, confirmUrl);
                break;
            case R.id.tvLink:
                if (BaseFunc.isValidUrl(linkUrl)) {
                    BaseFunc.urlClick(v.getContext(), linkUrl);
                }
                break;
        }

    }
}
