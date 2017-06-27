package com.fanglin.fenhong.microbuyer.common;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.StoreHomeInfo;
import com.fanglin.fhlib.other.FHLib;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/10/14-上午11:34.
 * 功能描述: 店铺分享二维码
 */
public class LayoutShopQrcode implements View.OnClickListener {
    private Dialog dialog;
    private LinearLayout LCanvas;
    private TextView tvStoreName;
    private ImageView ivStoreBg, ivStoreLogo, ivQrcode;
    FrameLayout.LayoutParams params;

    public LayoutShopQrcode(Context context) {
        View view = View.inflate(context, R.layout.layout_shop_qrcode, null);
        LCanvas = (LinearLayout) view.findViewById(R.id.LCanvas);
        view.findViewById(R.id.tvSave).setOnClickListener(this);
        view.findViewById(R.id.ivClose).setOnClickListener(this);

        tvStoreName = (TextView) view.findViewById(R.id.tvStoreName);
        ivStoreBg = (ImageView) view.findViewById(R.id.ivStoreBg);
        ivStoreLogo = (ImageView) view.findViewById(R.id.ivStoreLogo);
        ivQrcode = (ImageView) view.findViewById(R.id.ivQrcode);

        int w = BaseFunc.getDisplayMetrics(context).widthPixels;
        int h = 400 * w / 790;
        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        ivStoreBg.setLayoutParams(params);

        dialog = new Dialog(context, R.style.InnerDialog);
        dialog.setContentView(view);
    }

    public void refreshView(StoreHomeInfo storeInfo) {
        if (storeInfo != null) {
            tvStoreName.setText(storeInfo.getQrStoreName());
            new FHImageViewUtil(ivStoreBg).setImageURI(storeInfo.store_banner, FHImageViewUtil.SHOWTYPE.STORE_BANNER);
            new FHImageViewUtil(ivStoreLogo).setImageURI(storeInfo.store_avatar, FHImageViewUtil.SHOWTYPE.DEFAULT);
            new FHImageViewUtil(ivQrcode).setImageURI(storeInfo.store_qrcode, FHImageViewUtil.SHOWTYPE.DEFAULT);
        }
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSave:
                save();
                break;
            case R.id.ivClose:
                dialog.dismiss();
                break;
        }
    }

    private void save() {
        String path = BaseFunc.getImagePathByTime(null);
        FHLib.saveView(LCanvas, path);
        BaseFunc.showMsg(LCanvas.getContext(), "保存成功!");
    }
}
