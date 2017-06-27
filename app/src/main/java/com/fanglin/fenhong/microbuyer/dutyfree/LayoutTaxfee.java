package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Context;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fhui.FHDialog;

/**
 * 极速免税的关税弹窗
 * Created by Plucky on 2016/11/10.
 */
public class LayoutTaxfee implements View.OnClickListener {

    View itemView;
    FHDialog dlg;
    TextView tvTitle, tvTaxdesc;
    public View vTaxfee;
    public TextView tvTaxfee;

    public LayoutTaxfee(Context mContext) {
        itemView = View.inflate(mContext, R.layout.layout_dutyfree_taxfee, null);

        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        vTaxfee = itemView.findViewById(R.id.vTaxfee);
        tvTaxfee = (TextView) itemView.findViewById(R.id.tvTaxfee);
        tvTaxdesc = (TextView) itemView.findViewById(R.id.tvTaxdesc);

        itemView.findViewById(R.id.ivClose).setOnClickListener(this);
        int h = BaseFunc.getDisplayMetrics(mContext).heightPixels * 2 / 5;
        setHeight(h);

        tvTaxdesc.setMovementMethod(new ScrollingMovementMethod());
        tvTaxdesc.setLineSpacing(1.1f,1.1f);

        dlg = new FHDialog(mContext);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setBotView(itemView, 0);
    }

    public void setHeight(int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        itemView.setLayoutParams(params);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void refreshView(String taxFee, String taxDesc) {
        tvTaxfee.setText(taxFee);
        tvTaxdesc.setText(taxDesc);
    }

    public void refreshView(Spanned taxFee, Spanned taxDesc) {
        tvTaxfee.setText(taxFee);
        tvTaxdesc.setText(taxDesc);
    }


    public void show() {
        dlg.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
                dlg.dismiss();
                break;
        }
    }
}
