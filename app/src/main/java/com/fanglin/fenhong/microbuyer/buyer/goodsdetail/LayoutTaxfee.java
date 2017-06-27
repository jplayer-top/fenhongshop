package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDetail;
import com.fanglin.fhui.FHDialog;

import java.text.DecimalFormat;

/**
 * 极速免税关税说明
 * Created by Plucky on 2015/10/22.
 */
public class LayoutTaxfee implements View.OnClickListener {
    DecimalFormat df, taxdf;
    FHDialog dlg;
    private TextView tv_taxfee, tv_hsrate;

    public LayoutTaxfee(Context mContext) {
        df = new DecimalFormat("¥#0.00");
        taxdf = new DecimalFormat("#0.0");
        View v = View.inflate(mContext, R.layout.layout_taxfee, null);
        tv_taxfee = (TextView) v.findViewById(R.id.tv_taxfee);
        TextView tv_close = (TextView) v.findViewById(R.id.tv_close);
        tv_hsrate = (TextView) v.findViewById(R.id.tv_hsrate);
        FrameLayout FTop = (FrameLayout) v.findViewById(R.id.FTop);
        tv_close.setOnClickListener(this);

        int h = BaseFunc.getDisplayMetrics(mContext).heightPixels * 2 / 5;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        v.setLayoutParams(params);

        BaseFunc.setFont(FTop);

        dlg = new FHDialog(mContext);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setBotView(v, 0);
    }

    public void show(GoodsDetail goodsDetail, double freight, int buynum) {
        if (goodsDetail == null) return;
        double fee = goodsDetail.goods_price * 1 * goodsDetail.hs_rate;
        tv_taxfee.setText(df.format(fee));
        tv_hsrate.setText(goodsDetail.no_rate_tip);
        dlg.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                dlg.dismiss();
                break;
        }
    }
}
