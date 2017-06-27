package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fhui.FHDialog;
import com.fanglin.fhui.FHHintDialog;

/**
 * 作者： Created by Plucky on 2015/11/1.
 * modify by lizhixin on 2016/03/23
 * 通用对话框提示
 */
public class LayoutEvaluateTips implements FHHintDialog.FHHintListener {
    FHHintDialog dlg;
    private String order_id;

    public LayoutEvaluateTips(Context mContext) {
        dlg = new FHHintDialog(mContext);
        dlg.setTvTitle(mContext.getString(R.string.title_receive));
        dlg.setTvContent(mContext.getString(R.string.tips_receive));
        dlg.setTvLeft(mContext.getString(android.R.string.cancel));
        dlg.setTvRight(mContext.getString(android.R.string.ok));
        dlg.setHintListener(this);

    }

    public void show(String order_id) {
        this.order_id = order_id;
        dlg.show();
    }

    @Override
    public void onLeftClick() {
        if (mcb != null) mcb.onLETCancel();
    }

    @Override
    public void onRightClick() {
        if (mcb != null) mcb.onLETEnd(order_id);
    }

    private EvaluateTipsCallBack mcb;

    public void setCallBack(EvaluateTipsCallBack cb) {
        this.mcb = cb;
    }

    public interface EvaluateTipsCallBack {
        void onLETEnd(String order_id);//此参数在订单中是订单ID，在购物车中是cartId，在我的提醒中是goodsId

        void onLETCancel();
    }

    public static void confirm2Evaluate(final Context c, final String orderid) {
        final FHHintDialog fhd = new FHHintDialog(c);
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                BaseFunc.gotoActivity(c, EvaluateBeforeActivity.class, orderid);
            }
        });
        fhd.setTvTitle(c.getString(R.string.tips_2_evaluate_title));
        fhd.setTvContent(c.getString(R.string.tips_2_evaluate_content));
        fhd.show();
    }
}
