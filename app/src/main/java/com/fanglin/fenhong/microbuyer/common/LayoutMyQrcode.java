package com.fanglin.fenhong.microbuyer.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.echo.QrCodeUtils;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.PersonalNum;
import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhlib.other.SaveViewUtil;
import com.fanglin.fhui.CircleImageView;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.fhshare.model.FHShareData;
import cn.sharesdk.fhshare.model.FHShareItem;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/10/27-上午10:09.
 * 功能描述: 我的二维码弹窗
 */
public class LayoutMyQrcode implements View.OnClickListener, PlatformActionListener {
    private Context mContext;
    private Dialog dialog;
    private boolean hasDraw;
    private LinearLayout LCanvas;
    ImageView ivQrcode, ivLogo, ivWechat, ivMoment, ivQQ, ivSina;
    CircleImageView circleImageView;
    TextView tvName;
    private String qrPath, qrCodeDesc;
    FHShareData shareData;


    public LayoutMyQrcode(Context context) {
        this.mContext = context;
        View view = View.inflate(mContext, R.layout.layout_myqrcode, null);

        LCanvas = (LinearLayout) view.findViewById(R.id.LCanvas);
        ivQrcode = (ImageView) view.findViewById(R.id.ivQrcode);
        ivLogo = (ImageView) view.findViewById(R.id.ivLogo);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
        tvName = (TextView) view.findViewById(R.id.tvName);

        ivWechat = (ImageView) view.findViewById(R.id.ivWechat);
        ivMoment = (ImageView) view.findViewById(R.id.ivMoment);
        ivQQ = (ImageView) view.findViewById(R.id.ivQQ);
        ivSina = (ImageView) view.findViewById(R.id.ivSina);

        ivWechat.setOnClickListener(this);
        ivMoment.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
        ivSina.setOnClickListener(this);

        view.findViewById(R.id.ivDownload).setOnClickListener(this);
        view.findViewById(R.id.ivClose).setOnClickListener(this);

        shareData = new FHShareData();
        shareData.shareImage = true;

        dialog = new Dialog(mContext, R.style.InnerDialog);
        dialog.setContentView(view);
    }

    public void refreshData(PersonalNum num, Member member) {
        if (num != null && member != null) {
            qrCodeDesc = num.qrcode_description;
            hasDraw = true;
            tvName.setText(num.member_nickname);
            new FHImageViewUtil(circleImageView).setImageURI(num.member_avatar, FHImageViewUtil.SHOWTYPE.AVATAR);
            if (BaseFunc.isValidUrl(num.qrcode)) {
                ivLogo.setVisibility(View.GONE);
                new FHImageViewUtil(ivQrcode).setImageURI(num.qrcode, FHImageViewUtil.SHOWTYPE.DEFAULT);
            } else {
                String url = String.format(BaseVar.CALL_URL, member.member_id);
                Bitmap bitmap = QrCodeUtils.createQrImage(url, 300, 300);
                ivQrcode.setImageBitmap(bitmap);
                ivLogo.setVisibility(View.VISIBLE);
            }

            if (FHLib.isAvilible(mContext, "com.tencent.mm")) {
                ivWechat.setVisibility(View.VISIBLE);
                ivMoment.setVisibility(View.VISIBLE);
            } else {
                ivWechat.setVisibility(View.GONE);
                ivMoment.setVisibility(View.GONE);
            }

            if (FHShareItem.isQQAvaliable(mContext)) {
                ivQQ.setVisibility(View.VISIBLE);
            } else {
                ivQQ.setVisibility(View.GONE);
            }

            qrPath = BaseFunc.getImagePathByTime(member.member_id + "qrcode.png");
        } else {
            hasDraw = false;
        }
    }

    @Override
    public void onClick(final View v) {
        shareData.content = null;
        switch (v.getId()) {
            case R.id.ivClose:
                dialog.dismiss();
                break;
            case R.id.ivWechat:
                refreshImageData();
                FHShareItem.shareByWeChat(mContext, shareData, LayoutMyQrcode.this, null);
                break;
            case R.id.ivMoment:
                refreshImageData();
                FHShareItem.shareByWeChatMoments(mContext, shareData, LayoutMyQrcode.this, null);
                break;
            case R.id.ivSina:
                refreshImageData();
                shareData.content = qrCodeDesc;
                FHShareItem.shareBySina(mContext, shareData, LayoutMyQrcode.this, null);
                break;
            case R.id.ivQQ:
            case R.id.ivDownload:
                shareByQQOrSaveOnly(v.getId());
                break;
        }


    }

    public boolean isHasDraw() {
        return hasDraw;
    }

    public void show() {
        dialog.show();
    }

    /**
     * 如果是微信、微信朋友圈、新浪微博的话 则通过imageData的形式分享
     */
    private void refreshImageData() {
        Bitmap bitmap = Bitmap.createBitmap(LCanvas.getWidth(), LCanvas.getHeight(), Bitmap.Config.ARGB_8888);
        LCanvas.draw(new Canvas(bitmap));
        shareData.imageData = bitmap;
    }

    /**
     * QQ 暂时只支持 imagePath、imageUrl
     *
     * @param id int
     */
    private void shareByQQOrSaveOnly(final int id) {
        new SaveViewUtil(LCanvas, qrPath).setListener(new SaveViewUtil.SaveViewListener() {
            @Override
            public void onSaveSuccess(File file) {
                if (file != null && file.exists()) {
                    if (id == R.id.ivDownload) {
                        BaseFunc.showMsg(mContext, "保存成功!");
                    } else {
                        shareData.imagePath = qrPath;
                        FHShareItem.shareByQQ(mContext, shareData, LayoutMyQrcode.this, null);
                    }
                }
            }

            @Override
            public void onSaveError() {
                BaseFunc.showMsg(mContext, "二维码生成失败,请重试!");
            }
        }).save();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        FHLog.d("Plucky", platform.getName() + ":" + i);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        FHLog.d("Plucky", platform.getName() + ":" + i);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        FHLog.d("Plucky", i + " " + throwable.getMessage());
    }
}
