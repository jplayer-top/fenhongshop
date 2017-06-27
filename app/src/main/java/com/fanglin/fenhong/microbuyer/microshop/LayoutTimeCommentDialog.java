package com.fanglin.fenhong.microbuyer.microshop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/21-下午3:45.
 * 功能描述: 时光详情评论 弹框
 */
public class LayoutTimeCommentDialog implements View.OnClickListener {

    private Dialog dialog;

    public LayoutTimeCommentDialog(Context context) {
        View view = View.inflate(context, R.layout.layout_talentdetail_comment_dialog, null);
        view.findViewById(R.id.LContainer).setOnClickListener(this);
        view.findViewById(R.id.LSubContainer).setOnClickListener(this);
        view.findViewById(R.id.tvReply).setOnClickListener(this);
        view.findViewById(R.id.tvCopy).setOnClickListener(this);
        view.findViewById(R.id.tvJuBao).setOnClickListener(this);
        dialog = new Dialog(context, R.style.FHDialog);
        dialog.setContentView(view);
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LContainer:
                dialog.dismiss();
                break;
            case R.id.LSubContainer:
                break;
            case R.id.tvReply:
                dialog.dismiss();
                if (listener != null) {
                    listener.onReply();
                }
                break;
            case R.id.tvCopy:
                dialog.dismiss();
                if (listener != null) {
                    listener.onCopy();
                }
                break;
            case R.id.tvJuBao:
                dialog.dismiss();
                if (listener != null) {
                    listener.onjuBao();
                }
                break;
        }
    }

    private LayoutTimeCommentDialogListener listener;

    public void setListener(LayoutTimeCommentDialogListener listener) {
        this.listener = listener;
    }

    public interface LayoutTimeCommentDialogListener {
        void onReply();

        void onCopy();

        void onjuBao();
    }
}
