package com.fanglin.fenhong.microbuyer.microshop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/20-下午2:32.
 * 功能描述: 输入框
 */
public class LayoutInputDialog implements View.OnClickListener {
    private Dialog dialog;
    public EditText etContent;
    public TextView tvTitle;

    public LayoutInputDialog(Context mContext) {
        View view = View.inflate(mContext, R.layout.layout_input_dialog, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        etContent = (EditText) view.findViewById(R.id.etContent);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        view.findViewById(R.id.LContainer).setOnClickListener(this);
        view.findViewById(R.id.LSubContainer).setOnClickListener(this);
        view.findViewById(R.id.btnOK).setOnClickListener(this);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);
        dialog = new Dialog(mContext, R.style.FHDialog);
        dialog.setContentView(view);
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                if (etContent.length() == 0) {
                    YoYo.with(Techniques.Shake).duration(700).playOn(etContent);
                    return;
                }
                if (listener != null) {
                    listener.onData(etContent.getText().toString());
                }
                if (listener != null) {
                    listener.onBeforeDismiss(etContent);
                }
                dialog.dismiss();
                break;
            case R.id.btnCancel:
                if (listener != null) {
                    listener.onBeforeDismiss(etContent);
                }
                dialog.dismiss();
                break;
            case R.id.LSubContainer:
                break;
            case R.id.LContainer:
                if (listener != null) {
                    listener.onBeforeDismiss(etContent);
                }
                dialog.dismiss();
                break;
        }
    }

    private LayoutInputDialogListener listener;

    public void setListener(LayoutInputDialogListener listener) {
        this.listener = listener;
    }

    public interface LayoutInputDialogListener {
        void onData(String content);

        void onBeforeDismiss(EditText editText);
    }
}
