package com.fanglin.fenhong.microbuyer.microshop;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.PaySuccessBonus;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fhui.FHDialog;

/**
 * 作者： Created by Plucky on 2015/11/27.
 * 订单购买成功之后弹窗
 */
public class LayoutOrderSuccess implements View.OnClickListener {

    private FHDialog dlg;
    ShareData shareData;
    private TextView tv_desc;
    private View clickView;

    public LayoutOrderSuccess (View clickView) {
        Context c = clickView.getContext ();
        this.clickView = clickView;
        View view = View.inflate (c, R.layout.layout_order_success_bonus, null);
        int w = BaseFunc.getDisplayMetrics (c).widthPixels;
        int h = BaseFunc.getDisplayMetrics (c).heightPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (w, h);
        view.setLayoutParams (params);
        dlg = new FHDialog (c);
        dlg.setCanceledOnTouchOutside (true);
        dlg.setBotView (view, 2);
        view.findViewById (R.id.iv_close).setOnClickListener (this);
        view.findViewById (R.id.iv_share).setOnClickListener (this);
        tv_desc = (TextView) view.findViewById (R.id.tv_desc);
    }

    public void show (PaySuccessBonus PSBData) {
        if (PSBData != null) {
            shareData = new ShareData ();
            shareData.title = PSBData.share_title;
            shareData.content = PSBData.share_desc;
            shareData.imgs = PSBData.share_img;
            shareData.url = PSBData.share_url;
            shareData.justWechat = true;
            String fmt = dlg.getContext ().getString (R.string.def_order_bonus);
            tv_desc.setText (String.format (fmt, PSBData.bag_num));
            clickView.setVisibility (View.INVISIBLE);
            dlg.show ();
        }
    }

    public void dismiss () {
        clickView.setVisibility (View.VISIBLE);
        dlg.dismiss ();
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.iv_close:
                dismiss ();
                break;
            case R.id.iv_share:
                dismiss ();
                if (shareData != null) {
                    //ShareData.share (v, shareData);
                    ShareData.fhShare ((BaseFragmentActivity) v.getContext (), shareData, null);
                }
                break;
        }
    }
}
