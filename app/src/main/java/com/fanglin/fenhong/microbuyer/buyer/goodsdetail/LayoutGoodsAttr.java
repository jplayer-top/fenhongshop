package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsAttrAdpter;
import com.fanglin.fhui.FHDialog;
import com.google.gson.internal.LinkedTreeMap;

/**
 * 作者： Created by Plucky on 15-10-21.
 */
public class LayoutGoodsAttr implements View.OnClickListener {

    private Context mContext;
    View vattr;
    FHDialog dlg;
    ListView LV_attr;
    GoodsAttrAdpter attrAdpter;

    public LayoutGoodsAttr(Context c) {
        mContext = c;
        vattr = View.inflate(mContext, R.layout.layout_goods_attr, null);
        int height = BaseFunc.getDisplayMetrics(mContext).heightPixels / 2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        vattr.setLayoutParams(params);
        vattr.findViewById(R.id.tv_close).setOnClickListener(this);
        vattr.findViewById(R.id.tv_close_bottom).setOnClickListener(this);

        FrameLayout FTop = (FrameLayout) vattr.findViewById(R.id.FTop);
        BaseFunc.setFont(FTop);

        LV_attr = (ListView) vattr.findViewById(R.id.LV_attr);
        attrAdpter = new GoodsAttrAdpter(mContext);
        LV_attr.setAdapter(attrAdpter);

        dlg = new FHDialog(mContext);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setBotView(vattr, 0);
    }


    public void show() {
        dlg.show();
    }

    public void setData(LinkedTreeMap goods_attr) {
        attrAdpter.setJson(goods_attr);
        attrAdpter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        dlg.dismiss();
        switch (v.getId()) {
            case R.id.tv_close:
                break;
            case R.id.tv_close_bottom:
                dlg.dismiss();
                break;
        }
    }
}
