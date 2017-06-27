package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.MemberInfo;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.CircleImageView;
import com.fanglin.fhui.datapicker.GenderPickDialog;
import com.fanglin.fhui.datapicker.TimePickDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 编辑个人资料
 * Created by Plucky on 2015/9/6.
 */
public class ProfileActivity extends BaseFragmentActivityUI implements MemberInfo.MemberInfoCallBack {
    @ViewInject(R.id.tvHeadIcon)
    TextView tvHeadIcon;
    @ViewInject(R.id.tvPwdIcon)
    TextView tvPwdIcon;
    @ViewInject(R.id.sdv)
    CircleImageView sdv;
    @ViewInject(R.id.tv_membername)
    TextView tv_membername;
    MemberInfo memberInfo;

    @ViewInject(R.id.tvGenderIcon)
    TextView tvGenderIcon;
    @ViewInject(R.id.tvBirthIcon)
    TextView tvBirthIcon;
    @ViewInject(R.id.tvBirth)
    TextView tvBirth;
    @ViewInject(R.id.tvGender)
    TextView tvGender;
    @ViewInject(R.id.tvNick)
    TextView tvNick;
    @ViewInject(R.id.tvNickIcon)
    TextView tvNickIcon;

    public final static int REQPHOTO = 0x001;
    public final static int REQTIME = 0x002;
    public final static int REQNICK = 0x003;

    private long timeBirth = 0;
    private int gender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_profile, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        tvHeadIcon.setTypeface(iconfont);
        tvPwdIcon.setTypeface(iconfont);
        tvGenderIcon.setTypeface(iconfont);
        tvBirthIcon.setTypeface(iconfont);
        tvNickIcon.setTypeface(iconfont);

        enableTvMore(R.string.submit, false);
        setVisibleOfTvMore(false);
        setHeadTitle(R.string.profile);

        if (member != null) {
            memberInfo = new MemberInfo();
            memberInfo.member_id = member.member_id;
            memberInfo.token = member.token;
            memberInfo.setModelCallBack(this);
            memberInfo.getMemberInfo();
            if (!TextUtils.isEmpty(member.member_nickname)) {
                tvNick.setText(member.member_nickname);
            }
        }

        RefreshView();
    }


    private void RefreshView() {
        if (member != null) {
            tv_membername.setText(member.member_name);
            tvNick.setText(member.member_nickname);
            new FHImageViewUtil(sdv).setImageURI(member.getMember_avatar(), FHImageViewUtil.SHOWTYPE.GROUP);
            sdv.setTag(member.getMember_avatar());
            tvGender.setText(member.getGender());
            tvBirth.setText(member.member_birthday);
        }
    }

    @OnClick(value = {R.id.sdv, R.id.LAvatar, R.id.LPwd,
            R.id.LBirth, R.id.LGender, R.id.LNick})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.sdv:
                if (sdv.getTag() == null) {
                    doCrop();
                } else {
                    FileUtils.BrowserOpenS(this, null, sdv.getTag().toString());
                }
                break;
            case R.id.LAvatar:
                doCrop();
                break;
            case R.id.LPwd:
                if (member == null) return;
                BaseFunc.gotoRetrievePwdActivity(this,member.member_mobile,0);
                break;
            case R.id.LBirth:
                TimePickDialog.pickeDate(this, timeBirth, REQTIME);
                break;
            case R.id.LGender:
                new GenderPickDialog(mContext).setListener(new GenderPickDialog.GenderDialogListener() {
                    @Override
                    public void onSelected(int id, String name) {
                        gender = id;
                        tvGender.setText(name);
                        memberInfo.member_sex = String.valueOf(gender);
                        memberInfo.setModelCallBack(ProfileActivity.this);
                        memberInfo.editMemberInfo(2);
                    }
                }).show(gender);
                break;
            case R.id.LNick:
                Intent intent = new Intent(this, EditNickActivity.class);
                intent.putExtra("NICK", tvNick.getText().toString());
                intent.putExtra("EDITSTATES", memberInfo.nickname_status);
                intent.putExtra("TIME", memberInfo.surplus_time);
                startActivityForResult(intent, REQNICK);
                break;
        }
    }

    private void doCrop() {
        if (member == null) return;
        BaseFunc.selectPictures(this, REQPHOTO, true, 600, 600, 1, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQPHOTO:
                try {
                    Uri uri = data.getParcelableExtra("VAL");
                    upload(uri);
                } catch (Exception e) {
                    //
                }
                break;
            case REQTIME:
                timeBirth = data.getLongExtra("timestamp", 0);
                String str = data.getStringExtra("time");
                memberInfo.member_birthday = str;
                memberInfo.setModelCallBack(ProfileActivity.this);
                memberInfo.editMemberInfo(3);
                tvBirth.setText(str);
                break;
            case REQNICK:
                String nick = data.getStringExtra("VAL");
                tvNick.setText(nick);
                memberInfo.member_nickname = nick;
                memberInfo.setModelCallBack(ProfileActivity.this);
                memberInfo.editMemberInfo(0);
                break;
        }
    }

    private void upload(Uri uri) {
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
                    memberInfo.setMember_avatar(data);
                    memberInfo.setModelCallBack(ProfileActivity.this);
                    memberInfo.editMemberInfo(1);
                }
            }
        }).upload();
    }

    @Override
    public void onData(MemberInfo m) {
        if (m != null) {
            memberInfo = m;
            memberInfo.member_id = member.member_id;
            memberInfo.token = member.token;

            tv_membername.setText(m.member_name);
            tvNick.setText(m.member_nickname);
            new FHImageViewUtil(sdv).setImageURI(m.getMember_avatar(), FHImageViewUtil.SHOWTYPE.AVATAR);
            sdv.setTag(m.getMember_avatar());
            tvGender.setText(m.getGender());

            gender = m.getGenderInt();
            timeBirth = BaseFunc.dateStr2TimeInMillis(m.member_birthday, "yyyy-MM-dd");
        }
    }

    @Override
    public void onEnd(boolean isSuccess, int updateFiled) {
        if (isSuccess) {
            switch (updateFiled) {
                case 0:
                    tvNick.setText(memberInfo.member_nickname);
                    memberInfo.getMemberInfo();//重新刷新一下
                    break;
                case 1:
                    new FHImageViewUtil(sdv).setImageURI(memberInfo.getMember_avatar(), FHImageViewUtil.SHOWTYPE.AVATAR);
                    sdv.setTag(memberInfo.getMember_avatar());
                    break;
                case 2:
                    tvGender.setText(GenderPickDialog.getNameById(gender));
                    break;
                case 3:
                    tvBirth.setText(memberInfo.member_birthday);
                    break;
            }
            BaseFunc.showMsg(mContext, getString(R.string.update_success));
        }
    }

    @Override
    public void finish() {
        super.finish();
        /** 更新缓存*/
        if (member != null && memberInfo != null) {
            member.setMember_avatar(memberInfo.getMember_avatar_S());
            member.member_nickname = memberInfo.member_nickname;
            member.member_sex = memberInfo.member_sex;
            member.member_birthday = memberInfo.member_birthday;
            FHCache.setMember(this, member);
        }
    }
}
