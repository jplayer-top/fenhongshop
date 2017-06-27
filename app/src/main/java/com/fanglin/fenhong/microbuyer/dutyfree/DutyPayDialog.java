package com.fanglin.fenhong.microbuyer.dutyfree;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/29.
 * 功能描述: 极速免税支付成功失败弹窗
 */

public class DutyPayDialog extends Dialog implements View.OnClickListener {

    private BaseFragmentActivity mContext;
    private boolean isSuccess;

    private ImageView ivNiu;
    private TextView tvTitle, tvContent, tvSubmit;

    public DutyPayDialog(BaseFragmentActivity context) {
        super(context, R.style.InnerDialog);
        mContext = context;
        View view = View.inflate(mContext, R.layout.dialog_dutypay, null);
        ivNiu = (ImageView) view.findViewById(R.id.ivNiu);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvSubmit = (TextView) view.findViewById(R.id.tvSubmit);

        tvSubmit.setOnClickListener(this);
        view.findViewById(R.id.ivClose).setOnClickListener(this);

        setContentView(view);
    }

    public void refreshView(boolean isSuccess) {
        this.isSuccess = isSuccess;
        if (isSuccess) {
            ivNiu.setImageResource(R.drawable.img_niu_happy);
            tvTitle.setText("续费成功");
            tvContent.setText("快去购物吧 尊贵的VIP会员");
            tvSubmit.setText("去购物");
        } else {
            ivNiu.setImageResource(R.drawable.img_niu_sad);
            tvTitle.setText("放弃不是你的性格");
            tvContent.setText("VIP尊贵荣耀只差一步！放手干吧宝贝");
            tvSubmit.setText("继续支付");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismiss();
                if (dutyPayCloseListener != null) {
                    dutyPayCloseListener.onCloseClick(isSuccess);
                }
                break;
            case R.id.tvSubmit:
                dismiss();
                if (dutyPayListener != null) {
                    dutyPayListener.onSubmitClick(isSuccess);
                }
                break;
        }
    }

    private DutyPayDialogListener dutyPayListener;
    private DutyPayDialogCloseListener dutyPayCloseListener;

    public void setDutyPayCloseListener(DutyPayDialogCloseListener dutyPayCloseListener) {
        this.dutyPayCloseListener = dutyPayCloseListener;
    }

    public void setDutyPayListener(DutyPayDialogListener dutyPayListener) {
        this.dutyPayListener = dutyPayListener;
    }

    public interface DutyPayDialogListener {
        void onSubmitClick(boolean isSuccess);
    }

    public interface DutyPayDialogCloseListener {
        void onCloseClick(boolean isSuccess);
    }
}
