package com.fanglin.fhui;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 通用提示对话框
 * 支持功能：自定义主题、标题、提示内容、两个按钮的文字、颜色、背景，完全自定义两个按钮以及点击事件，
 * Created by lizhixin on 2016/4/19 14:53.
 */
public class FHHintDialog extends Dialog implements View.OnClickListener {

    private TextView tvTitle, tvContent, tvLeft, tvRight;
    private LinearLayout llLeft, llRight;
    private FHHintListener listener;
    private String confirmData;//弹框时传入的数据

    public FHHintDialog(Context context) {
        super(context, R.style.FHDialog);
        initView();
    }

    public FHHintDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_fh_hint);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvRight = (TextView) findViewById(R.id.tvRight);
        LinearLayout llContainer = (LinearLayout) findViewById(R.id.llContainer);
        LinearLayout llSubContainer = (LinearLayout) findViewById(R.id.llSubContainer);
        llLeft = (LinearLayout) findViewById(R.id.llLeft);
        llRight = (LinearLayout) findViewById(R.id.llRight);
        llLeft.setOnClickListener(this);
        llRight.setOnClickListener(this);
        llContainer.setOnClickListener(this);
        llSubContainer.setOnClickListener(this);
    }

    /**
     * @param view 自定义的左边按钮
     */
    public void setLeftBtn(View view) {
        if (view == null) {
            llLeft.setVisibility(View.GONE);
        } else {
            llLeft.removeAllViews();
            llLeft.addView(view);
        }
    }

    /**
     * @param view 自定义的右边按钮
     */
    public void setRightBtn(View view) {
        if (view == null) {
            llRight.setVisibility(View.GONE);
        } else {
            llRight.removeAllViews();
            llRight.addView(view);
        }
    }

    /**
     * @param str 标题
     */
    public void setTvTitle(String str) {
        tvTitle.setText(str);
        if (!TextUtils.isEmpty(str)) {
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param str 提示内容
     */
    public void setTvContent(String str) {

        tvContent.setText(str);
    }

    /**
     * @param str 左边按钮文字
     */
    public void setTvLeft(String str) {
        tvLeft.setText(str);
    }

    /**
     * @param resourceId 左边按钮文字颜色
     */
    public void setTvLeftColor(int resourceId) {
        this.tvLeft.setTextColor(resourceId);
    }

    /**
     * @param resourceId 左边按钮文字背景
     */
    public void setTvLeftBg(int resourceId) {
        this.tvLeft.setBackgroundResource(resourceId);
    }

    /**
     * @param str 右边按钮文字
     */
    public void setTvRight(String str) {
        tvRight.setText(str);
    }

    /**
     * @param resourceId 右边按钮文字颜色
     */
    public void setTvRightColor(int resourceId) {
        this.tvRight.setTextColor(resourceId);
    }

    /**
     * @param resourceId 右边按钮文字背景
     */
    public void setTvRightBg(int resourceId) {
        this.tvRight.setBackgroundResource(resourceId);
    }

    public void setHintListener(FHHintListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.llLeft) {
            if (listener != null)
                listener.onLeftClick();
        }
        if (i == R.id.llRight) {
            if (listener != null)
                listener.onRightClick();
        }
        if (i != R.id.llSubContainer) {
            /** 点击有效区域之外才关闭对话框 */
            dismiss();
        }
    }

    public interface FHHintListener {
        void onLeftClick();

        void onRightClick();
    }

    public String getConfirmData() {
        return confirmData;
    }

    public void setConfirmData(String confirmData) {
        this.confirmData = confirmData;
    }
}
