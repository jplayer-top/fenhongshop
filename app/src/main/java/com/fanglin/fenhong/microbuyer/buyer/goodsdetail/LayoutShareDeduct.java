package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;

import java.text.DecimalFormat;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/4-下午1:00.
 * 功能描述:
 */
public class LayoutShareDeduct implements View.OnClickListener {
    private View view;
    private String url;

    public LayoutShareDeduct(Context c, double price, double deduct_price) {
        view = View.inflate(c, R.layout.layout_share_deduct, null);
        DecimalFormat df = new DecimalFormat("¥#0.00");
        TextView tvMoney = (TextView) view.findViewById(R.id.tvMoney);
        TextView tvIcon = (TextView) view.findViewById(R.id.tvIcon);

        url = String.format(BaseVar.GOODS_COMMISSION, price, deduct_price);

        view.findViewById(R.id.tvDesc).setOnClickListener(this);
        tvMoney.setText(df.format(deduct_price));
        tvIcon.setOnClickListener(this);
        BaseFunc.setFont(tvIcon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDesc:
            case R.id.tvIcon:
                BaseFunc.gotoActivity(view.getContext(), FHBrowserActivity.class, url);
                break;
        }
    }

    public View getView() {
        return view;
    }
}
