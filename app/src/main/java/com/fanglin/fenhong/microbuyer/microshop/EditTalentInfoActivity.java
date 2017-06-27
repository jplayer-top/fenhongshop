package com.fanglin.fenhong.microbuyer.microshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.TalentInfo;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.CircleImageView;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.google.gson.Gson;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/26-下午10:54.
 * 功能描述: 达人信息编辑
 */
public class EditTalentInfoActivity extends BaseFragmentActivityUI implements TextWatcher {
    @ViewInject(R.id.circleImageView)
    CircleImageView circleImageView;
    @ViewInject(R.id.tvHeadIcon)
    TextView tvHeadIcon;
    @ViewInject(R.id.ivBg)
    ImageView ivBg;
    @ViewInject(R.id.tvBgIcon)
    TextView tvBgIcon;
    @ViewInject(R.id.LNick)
    LinearLayout LNick;
    @ViewInject(R.id.etNick)
    EditText etNick;
    @ViewInject(R.id.LMotto)
    LinearLayout LMotto;
    @ViewInject(R.id.etMotto)
    EditText etMotto;

    TalentInfo talentInfo;
    public final static int CROPAVATAR = 0x001;
    public final static int CROPBG = 0x002;
    private String avatar, background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_edit_talent_info, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        String str = getIntent().getStringExtra("VAL");
        try {
            talentInfo = new Gson().fromJson(str, TalentInfo.class);
        } catch (Exception e) {
            talentInfo = null;
        }
        if (talentInfo == null) {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            avatar = talentInfo.getTalent_avatar();
            background = talentInfo.getTalent_background();
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
        }
        initView();
    }

    private void initView() {
        setHeadTitle(getString(R.string.edit));
        tvHeadIcon.setTypeface(iconfont);
        tvBgIcon.setTypeface(iconfont);
        if (talentInfo != null) {
            enableTvMore(R.string.save, false);
            new FHImageViewUtil(circleImageView).setImageURI(avatar, FHImageViewUtil.SHOWTYPE.AVATAR);
            new FHImageViewUtil(ivBg).setImageURI(background, FHImageViewUtil.SHOWTYPE.TALENTBG);
            ivBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            etNick.setText(talentInfo.getTalent_name());
            etMotto.setText(talentInfo.getTalent_intro());
        }

        etNick.addTextChangedListener(this);
        etMotto.addTextChangedListener(this);
    }

    @OnClick(value = {R.id.LAvatar, R.id.circleImageView, R.id.LBg, R.id.ivBg,})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.LAvatar:
                doCrop(CROPAVATAR);
                break;
            case R.id.circleImageView:
                if (BaseFunc.isValidUrl(avatar)) {
                    FileUtils.BrowserOpenS(this, null, avatar);
                } else {
                    doCrop(CROPAVATAR);
                }
                break;
            case R.id.LBg:
                doCrop(CROPBG);
                break;
            case R.id.ivBg:
                if (BaseFunc.isValidUrl(background)) {
                    FileUtils.BrowserOpenS(this, null, background);
                } else {
                    doCrop(CROPBG);
                }
                break;
        }
    }

    private void doCrop(int REQ) {
        if (REQ == CROPAVATAR) {
            //裁剪头像
            BaseFunc.selectPictures(this, REQ, true, 600, 600, 1, 1);
        } else {
            BaseFunc.selectPictures(this, REQ, true, 0, 0, 0, 0);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case CROPAVATAR:
                try {
                    Uri uri = data.getParcelableExtra("VAL");
                    upload(uri, CROPAVATAR);
                } catch (Exception e) {
                    //
                }
                break;
            case CROPBG:
                try {
                    Uri uri = data.getParcelableExtra("VAL");
                    upload(uri, CROPBG);
                } catch (Exception e) {
                    //
                }
                break;
        }
    }

    private void upload(Uri uri, final int type) {
        if (member == null) return;
        final SpotsDialog dlg = BaseFunc.getLoadingDlg(this, getString(R.string.picture_uploading));
        new UpyunUploader(member.member_id).setUploadFile(uri.getPath()).setUpYunCallBack(new UpyunUploader.UpYunCallBack() {
            @Override
            public void startLoading() {
                dlg.show();
            }

            @Override
            public void endLoading(boolean isSuccess, String data) {
                dlg.dismiss();
                if (isSuccess) {
                    hasEdit = true;
                    if (type == CROPAVATAR) {
                        avatar = data;
                        new FHImageViewUtil(circleImageView).setImageURI(avatar, FHImageViewUtil.SHOWTYPE.AVATAR);
                    } else {
                        background = data;
                        new FHImageViewUtil(ivBg).setImageURI(background, FHImageViewUtil.SHOWTYPE.TALENTBG);
                        ivBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }
            }
        }).upload();
    }

    @Override
    public void onBackClick() {
        if (hasEdit) {
            confirmTips();
        } else {
            super.onBackClick();
        }
    }

    @Override
    public void onBackPressed() {
        if (hasEdit) {
            confirmTips();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        hasEdit = true;
        if (etNick.length() < 2) {
            etNick.setTextColor(getResources().getColor(R.color.fh_red));
        } else {
            etNick.setTextColor(getResources().getColor(R.color.color_99));
        }

        if (etMotto.length() == 0) {
            etMotto.setTextColor(getResources().getColor(R.color.fh_red));
        } else {
            etMotto.setTextColor(getResources().getColor(R.color.color_33));
        }
    }

    private boolean hasEdit = false;

    private void confirmTips() {
        FHHintDialog fhHintDialog = new FHHintDialog(mContext);
        fhHintDialog.setTvContent(getString(R.string.microshop_info_edit_tips));
        fhHintDialog.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                finish();
            }
        });
        fhHintDialog.show();
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();

        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        if (etNick.length() < 2) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LNick);
            BaseFunc.showMsg(mContext, getString(R.string.nick_less_than_2));
            return;
        }

        if (etMotto.length() == 0) {
            YoYo.with(Techniques.Shake).duration(700).playOn(LMotto);
            BaseFunc.showMsg(mContext, getString(R.string.tips_talent_motto));
            return;
        }

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    member.setMember_avatar(avatar);
                    member.member_nickname = etNick.getText().toString();
                    FHCache.setMember(EditTalentInfoActivity.this, member);
                    finish();
                }
            }
        }).changeTalentInfo(member, avatar, background, etNick.getText().toString(), etMotto.getText().toString());

    }
}
