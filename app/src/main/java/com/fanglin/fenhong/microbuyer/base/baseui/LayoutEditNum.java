package com.fanglin.fenhong.microbuyer.base.baseui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/2-下午9:34.
 * 功能描述:极速免税店 数字编辑
 */
public abstract class LayoutEditNum implements View.OnClickListener {

    public EditText etNum;
    private Dialog dialog;

    public LayoutEditNum(Context mContext) {
        View view = View.inflate(mContext, R.layout.layout_editnum, null);
        etNum = (EditText) view.findViewById(R.id.etNum);
        view.findViewById(R.id.tvPlus).setOnClickListener(this);
        view.findViewById(R.id.tvMinus).setOnClickListener(this);
        view.findViewById(R.id.tvCancel).setOnClickListener(this);
        view.findViewById(R.id.tvSubmit).setOnClickListener(this);

        dialog = new Dialog(mContext, R.style.InnerDialog);
        dialog.setContentView(view);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMinus:
                onMinus();
                break;
            case R.id.tvPlus:
                onPlus();
                break;
            case R.id.tvSubmit:
                onSubmit();
                break;
            case R.id.tvCancel:
                onCancel();
                break;
        }
    }

    public void onMinus() {
        String numStr = etNum.getText().toString();
        int num = BaseFunc.isInteger(numStr) ? Integer.valueOf(numStr) : 0;
        num--;
        num = num < 1 ? 1 : num;
        etNum.setText(String.valueOf(num));
    }

    public void onPlus() {
        String numStr = etNum.getText().toString();
        int num = BaseFunc.isInteger(numStr) ? Integer.valueOf(numStr) : 0;
        num++;
        num = num > 999 ? 999 : num;//新需求 最大数目为999
        etNum.setText(String.valueOf(num));
    }

    public void onCancel() {
        dialog.dismiss();
    }

    public abstract void onSubmit();


    public LayoutEditNumListener numListener;

    public void setNumListener(LayoutEditNumListener numListener) {
        this.numListener = numListener;
    }

    public interface LayoutEditNumListener {
        void onEditNum(int num);
    }
}
