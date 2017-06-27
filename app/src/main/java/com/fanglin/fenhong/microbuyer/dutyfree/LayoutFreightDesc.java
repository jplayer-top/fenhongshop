package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Context;
import android.view.View;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/6-下午6:45.
 * 功能描述: 极速免税 税费说明弹窗
 */
public class LayoutFreightDesc extends LayoutTaxfee {

    public LayoutFreightDesc(Context mContext) {
        super(mContext);
        vTaxfee.setVisibility(View.GONE);
        tvTaxfee.setVisibility(View.GONE);
        int h = BaseFunc.getDisplayMetrics(mContext).heightPixels / 2;
        setHeight(h);
    }
}
