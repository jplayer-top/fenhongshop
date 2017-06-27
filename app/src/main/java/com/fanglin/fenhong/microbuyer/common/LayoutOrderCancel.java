package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fhui.FHDialog;

/**
 * 作者： Created by Plucky on 15-9-27.
 */
public class LayoutOrderCancel implements View.OnClickListener {

    public View view;
    TextView tv_quit, tv_msgerr, tv_payerr, tv_duppay, tv_other;
    LinearLayout LOutSide;
    FHDialog dlg;
    Member member;
    String order_id;
    OrderCancelCallBack mcb;
    private Context mContext;

    public LayoutOrderCancel(Context c) {
        mContext = c;
        view = View.inflate(mContext, R.layout.layout_cancel_order_reason, null);
        tv_quit = (TextView) view.findViewById(R.id.tv_quit);
        tv_msgerr = (TextView) view.findViewById(R.id.tv_msgerr);
        tv_payerr = (TextView) view.findViewById(R.id.tv_payerr);
        tv_duppay = (TextView) view.findViewById(R.id.tv_duppay);
        tv_other = (TextView) view.findViewById(R.id.tv_other);

        LOutSide = (LinearLayout) view.findViewById(R.id.LOutSide);
        LOutSide.setOnClickListener(this);

        tv_quit.setOnClickListener(this);
        tv_msgerr.setOnClickListener(this);
        tv_payerr.setOnClickListener(this);
        tv_duppay.setOnClickListener(this);
        tv_other.setOnClickListener(this);

        dlg = new FHDialog(mContext);
        dlg.setBotView(view, 2);
    }

    public void show(Member member, String order_id) {
        this.member = member;
        this.order_id = order_id;
        dlg.show();
    }

    @Override
    public void onClick(View v) {
        dlg.dismiss();
        if (v.getId() == R.id.LOutSide) return;
        if (member == null || order_id == null) return;
        String msg = ((TextView) v).getText().toString();
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                BaseFunc.showMsg(mContext, mContext.getString(R.string.cancelOrder_requesting));
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    if (mcb != null) mcb.onSuccess();
                } else {
                    if (mcb != null) mcb.onError();
                }
            }
        }).cancle_order(member.member_id, member.token, order_id, msg);
    }

    public void setCallBack(OrderCancelCallBack cb) {
        mcb = cb;
    }

    public interface OrderCancelCallBack {
        void onSuccess();

        void onError();
    }
}
