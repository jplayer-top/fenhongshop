package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.CalculateData;

import java.text.DecimalFormat;

/**
 * 作者： Created by Plucky on 2015/9/18.
 */
public class CalculateLayout implements View.OnClickListener {
    private Context mContext;
    private View view;
    private ImageView imgCheck;
    public TextView tv_money, tv_calculate;
    private DecimalFormat df;

    public CalculateLayout(Context c) {
        mContext = c;
        view = View.inflate(c, R.layout.layout_calculate, null);
        tv_money = (TextView) view.findViewById(R.id.tv_money);
        tv_calculate = (TextView) view.findViewById(R.id.tv_calculate);

        imgCheck = (ImageView) view.findViewById(R.id.imgCheck);
        LinearLayout LCheck = (LinearLayout) view.findViewById(R.id.LCheck);

        LCheck.setOnClickListener(this);

        tv_calculate.setOnClickListener(this);
        df = new DecimalFormat("#0.00");
    }

    private CalculateData calculateData;

    public void Calculate(CalculateData cd) {
        if (cd == null) {
            cd = new CalculateData();
            cd.count_all_id = -1;//制造不相等
        }
        calculateData = cd;
        /**
         * 编辑状态下结算按钮不应该显示
         */
        tv_calculate.setVisibility(cd.isEditMode ? View.GONE : View.VISIBLE);

        String money_cal = String.format(mContext.getString(R.string.fmt_cart_footer_money), df.format(cd.money));

        imgCheck.setSelected(cd.getAllChecked());//当全部选中时

        tv_money.setText(BaseFunc.fromHtml(money_cal));

        if (cd.num > 99) {
            tv_calculate.setText(String.format(mContext.getString(R.string.fmt_pay_cart), "99+"));
        } else {
            tv_calculate.setText(String.format(mContext.getString(R.string.fmt_pay_cart), cd.num));
        }

        if (cd.num > 0) {
            tv_calculate.setEnabled(true);
        } else {
            tv_calculate.setEnabled(false);
        }
    }

    public View getView() {
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LCheck:
                boolean flag = calculateData.hasOneChecked();

                imgCheck.setSelected(!flag);
                if (mcb != null) {
                    mcb.onCheckBoxClick(!flag);
                }
                break;
            case R.id.tv_calculate:
                if (mcb != null) {
                    mcb.onCalculateClick();
                }
                break;
        }
    }

    public CalculateCallBack mcb;

    public void setCallBack(CalculateCallBack cb) {
        this.mcb = cb;
    }

    public interface CalculateCallBack {
        void onCheckBoxClick(boolean b);

        void onCalculateClick();
    }
}
