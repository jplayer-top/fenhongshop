package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.spotsdialog.SpotsDialog;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 15-9-30.
 */
public class JoinstepPayofflineActivity extends BaseFragmentActivityUI {
    @ViewInject(R.id.iv_license)
    ImageView iv_license;
    @ViewInject(R.id.Lupload)
    LinearLayout Lupload;
    @ViewInject(R.id.edscope)
    EditText edscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_joinstep_payoffline, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        enableTvMore(R.string.save,false);
        setHeadTitle(R.string.merchantinpay_offline);
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        submit();
    }

    @OnClick(value = {R.id.Lupload, R.id.iv_license})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.Lupload:
                BaseFunc.selectPictures(this, 0x001, true, 0, 0, 0, 0);
                break;
            case R.id.iv_license:
                if (iv_license.getTag() != null) {
                    FileUtils.BrowserOpenS(this, null, iv_license.getTag().toString());
                }
                break;
        }
    }

    private void submit() {
        if (iv_license.getTag() == null) {
            BaseFunc.showMsg(this, (getString(R.string.merchantinpay_uploadlicense)));
            YoYo.with(Techniques.Shake).duration(700).playOn(Lupload);
            YoYo.with(Techniques.Bounce).duration(700).playOn(iv_license);
            return;
        }
        if (member == null) return;
        final SpotsDialog dlg = BaseFunc.getLoadingDlg(this, getString(R.string.deposit_requesting));
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                dlg.show();
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                dlg.dismiss();
                if (isSuccess) {
                    Intent i = new Intent(mContext, JoinstepPayActivity.class);
                    setResult(RESULT_OK, i);
                    BaseFunc.showMsg(mContext, getString(R.string.op_success));
                    finish();
                }
            }
        }).pay_offline(member.member_id, member.token, iv_license.getTag().toString(), edscope.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 0x001:
                try {
                    Uri uri = data.getParcelableExtra("VAL");
                    upload(uri);
                } catch (Exception e) {
                    //
                }
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
                    new FHImageViewUtil(iv_license).setImageURI(data, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    iv_license.setTag(data);
                }
            }
        }).upload();
    }
}
