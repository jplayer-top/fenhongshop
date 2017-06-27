package com.fanglin.fenhong.microbuyer.microshop;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fhui.FHDialog;
import com.fanglin.fhui.datapicker.NumberPicker;

/**
 * 作者： Created by Plucky on 2015/11/2.
 * 选择一个开户行 或 物流公司
 */
public class LayoutSelectaBank implements View.OnClickListener {
    private View view;
    private NumberPicker picker;
    private TextView tv_title;
    FHDialog dlg;
    private String[] displays;

    public LayoutSelectaBank(Context c, String[] datas) {
        view = View.inflate(c, R.layout.layout_select_abank, null);
        int w = BaseFunc.getDisplayMetrics(c).widthPixels;
        int h = BaseFunc.getDisplayMetrics(c).heightPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        view.setLayoutParams(params);

        picker = (NumberPicker) view.findViewById(R.id.picker);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        dlg = new FHDialog(c);
        dlg.setBotView(view, 2);

        if (datas == null || datas.length == 0) {

            displays = BaseFunc.getAvaliableBanks();
            picker.setMinValue(0);
            picker.setMaxValue(displays.length - 1);
            picker.setDisplayedValues(displays);

        } else {

            this.displays = datas;
            picker.setMinValue(0);
            picker.setMaxValue(datas.length - 1);
            picker.setDisplayedValues(displays);
        }

    }

    public void show() {
        if (displays == null || displays.length == 0) {
            BaseFunc.showMsg(view.getContext(), view.getContext().getString(R.string.no_data));
            return;
        }
        dlg.show();
    }

    public void dismiss() {
        dlg.dismiss();
    }

    /**
     * 动态设置标题
     *
     * @param title title
     */
    public void setTv_title(String title) {
        this.tv_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.tv_ok:
                if (mcb != null) {
                    int index = picker.getValue();
                    mcb.onSelected(displays[index], index);
                }
                break;
            case R.id.tv_cancel:

                break;
        }
    }

    private onBankSelected mcb;

    public void setCallBack(onBankSelected cb) {
        this.mcb = cb;
    }

    public interface onBankSelected {
        void onSelected(String bankname, int index);
    }
}
