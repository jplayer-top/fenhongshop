package com.fanglin.fenhong.microbuyer.base.baseui;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/2-下午5:08.
 * 功能描述: 文字展示Dialog
 */
public class TextShowDialog extends Dialog implements View.OnClickListener {

    TextView tvMsg;
    ScrollView scrollView;

    public TextShowDialog(Context context) {
        super(context, R.style.InnerDialog);
        View view = View.inflate(context, R.layout.layout_text_show, null);
        tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        scrollView=(ScrollView)view.findViewById(R.id.scrollView);

        DisplayMetrics metrics = BaseFunc.getDisplayMetrics(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels);
        scrollView.setLayoutParams(params);

        setContentView(view);

        tvMsg.setOnClickListener(this);
    }

    public void setText(String msg) {
        tvMsg.setText(msg);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
