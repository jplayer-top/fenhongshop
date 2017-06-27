package com.fanglin.fenhong.microbuyer.microshop;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fhui.FHDialog;

/**
 * 作者： Created by Plucky on 2015/11/27.
 * 新人红包弹窗
 */
public class LayoutBonus implements View.OnClickListener {

    private FHDialog dlg;
    private TextView tc_desc;
    private String storeId;

    public LayoutBonus(Context c) {
        View view = View.inflate(c, R.layout.layout_bonus, null);
        int w = BaseFunc.getDisplayMetrics(c).widthPixels;
        int h = BaseFunc.getDisplayMetrics(c).heightPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        view.setLayoutParams(params);
        dlg = new FHDialog(c);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setBotView(view, 2);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        view.findViewById(R.id.iv_use).setOnClickListener(this);
        view.findViewById(R.id.iv_look).setOnClickListener(this);
        tc_desc = (TextView) view.findViewById(R.id.tc_desc);
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void show(String bag_name) {
        if (!TextUtils.isEmpty(bag_name)) {
            tc_desc.setText(Html.fromHtml(bag_name));
            dlg.show();
        }
    }

    public void dismiss() {
        dlg.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_use:
                String couponType = (v.getContext() instanceof BonusActivity) ? "null" : "person";
                BaseFunc.gotoCouponGoodsPage(v.getContext(), storeId, couponType);
                dismiss();
                break;
            case R.id.iv_look:
                if (!(v.getContext() instanceof BonusActivity)) {
                    BaseFunc.gotoActivity(v.getContext(), BonusActivity.class, null);
                }
                dismiss();
                break;
        }
    }
}
