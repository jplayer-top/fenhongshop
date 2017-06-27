package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fhlib.other.FHLib;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-下午3:13.
 * 功能描述: 极速免税店 店家二维码
 */
public class SellerQrcodeActivity extends BaseFragmentActivityUI {
    @ViewInject(R.id.ivQrcode)
    ImageView ivQrcode;

    private String qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_seller_qrcode, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        qrcode = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        tvHead.setText("联系卖家");
        new FHImageViewUtil(ivQrcode).setImageURI(qrcode, FHImageViewUtil.SHOWTYPE.DEFAULT);
        ivQrcode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String path = BaseFunc.getImagePathByTime(null);
                FHLib.saveView(ivQrcode, path);
                return false;
            }
        });
    }
}
