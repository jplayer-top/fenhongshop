package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fhui.FHDialog;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/12-下午4:40.
 * 功能描述: 不配送说明
 */
public class LayoutDeliverRemark implements View.OnClickListener {
    private FHDialog fhDialog;
    private TextView tvDesc;

    public LayoutDeliverRemark(Context context) {
        View view = View.inflate(context, R.layout.layout_deliver_remark, null);
        tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        view.findViewById(R.id.btnClose).setOnClickListener(this);

        fhDialog = new FHDialog(context);
        fhDialog.setBotView(view, 0);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                fhDialog.dismiss();
                break;
        }
    }

    public void show() {
        fhDialog.show();
    }
}
