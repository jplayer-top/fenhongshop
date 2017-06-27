package com.fanglin.fhui.datapicker;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.fanglin.fhui.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/15-上午9:52.
 * 功能描述: 性别选择
 */
public class GenderPickDialog extends Dialog implements View.OnClickListener {
    public static String[] displays = new String[]{"保密", "男", "女", "保密", "男", "女"};

    private NumberPicker picker;

    public GenderPickDialog(Context context) {
        super(context, R.style.InnerDialog);
        initView(context);
    }

    public GenderPickDialog(Context context, int theme) {
        super(context, theme);
        initView(context);
    }

    private void initView(Context context) {
        int minWidth = context.getResources().getDisplayMetrics().widthPixels - 80;
        setContentView(R.layout.dialog_gender_pick);
        picker = (NumberPicker) findViewById(R.id.picker);
        LinearLayout LContainer = (LinearLayout) findViewById(R.id.LContainer);
        findViewById(R.id.btnOk).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);

        LContainer.setMinimumWidth(minWidth);
        picker.setMinValue(0);
        picker.setMaxValue(5);
        picker.setDisplayedValues(displays);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOk) {
            if (listener != null) {
                int value = picker.getValue();
                String name = displays[value];
                int id = value > 2 ? value - 3 : value;
                listener.onSelected(id, name);
            }
        }
        dismiss();
    }

    private GenderDialogListener listener;

    public GenderPickDialog setListener(GenderDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public interface GenderDialogListener {
        void onSelected(int id, String name);
    }

    public void show(int value) {
        super.show();
        if (value < 3) {
            picker.setValue(value);
        }
    }

    public static String getNameById(int id) {
        if (id < 6) {
            return displays[id];
        }
        return displays[0];
    }
}
