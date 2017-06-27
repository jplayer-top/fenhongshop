package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreePayment;

import java.util.List;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/29.
 * 功能描述: VIP买手支付方式适配器
 */

public class VipBuyerPayAdapter extends RecyclerView.Adapter<VipBuyerPayAdapter.PayViewHolder> {

    private Context mContext;
    private List<DutyfreePayment> payments;

    public VipBuyerPayAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setPayments(List<DutyfreePayment> payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }

    public DutyfreePayment getItem(int position) {
        return payments.get(position);
    }

    @Override
    public PayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_vipbuyer_payment, null);
        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PayViewHolder holder, int position) {
        DutyfreePayment item = getItem(position);
        String payment = item.getPayment();
        holder.refreshData(item);
        if (TextUtils.equals("alipay", payment)) {
            holder.ivPay.setImageResource(R.drawable.icon_dutyfree_alipay);
        } else if (TextUtils.equals("wxpay", payment)) {
            holder.ivPay.setImageResource(R.drawable.icon_dutyfree_wxpay);
        } else if (TextUtils.equals("jdpay", payment)) {
            holder.ivPay.setImageResource(R.drawable.icon_dutyfree_jdpay);
        } else {
            holder.ivPay.setImageResource(0);
        }
    }

    @Override
    public int getItemCount() {
        if (payments == null)
            return 0;
        return payments.size();
    }

    class PayViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPay;
        DutyfreePayment payment;

        PayViewHolder(View itemView) {
            super(itemView);
            ivPay = (ImageView) itemView.findViewById(R.id.ivPay);
            ivPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (payListener != null && payment != null) {
                        if (TextUtils.isEmpty(payment.getMsg())) {
                            payListener.onVipPay(payment.getPayment());
                        } else {
                            BaseFunc.showMsg(mContext, payment.getMsg());
                        }
                    }
                }
            });
        }

        void refreshData(DutyfreePayment payment) {
            this.payment = payment;
        }
    }

    private VipPayListener payListener;

    public void setPayListener(VipPayListener payListener) {
        this.payListener = payListener;
    }

    public interface VipPayListener {
        void onVipPay(String payment);
    }
}
