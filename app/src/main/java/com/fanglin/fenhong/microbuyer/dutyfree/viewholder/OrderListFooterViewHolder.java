package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrder;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.DutyOrderActionListener;
import com.fanglin.fhui.FHHintDialog;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/10-下午3:49.
 * 功能描述: 极速免税订单列表 Footer
 */
public class OrderListFooterViewHolder extends RecyclerView.ViewHolder {
    TextView tvNum, tvPayPrefix, tvAmount, tvFreight;
    Button btnOne, btnTwo;
    private static FHHintDialog dialog;

    public OrderListFooterViewHolder(View itemView) {
        super(itemView);
        tvNum = (TextView) itemView.findViewById(R.id.tvNum);
        tvPayPrefix = (TextView) itemView.findViewById(R.id.tvPayPrefix);
        tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
        tvFreight = (TextView) itemView.findViewById(R.id.tvFreight);
        btnOne = (Button) itemView.findViewById(R.id.btnOne);
        btnTwo = (Button) itemView.findViewById(R.id.btnTwo);
    }

    public static OrderListFooterViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_orderlist_footer, null);
        dialog = new FHHintDialog(mContext);
        return new OrderListFooterViewHolder(view);

    }

    private DutyOrderActionListener listener;

    public void setListener(DutyOrderActionListener listener) {
        this.listener = listener;
    }

    public void refreshView(final DutyOrder order, final int section) {
        if (order != null) {
            tvNum.setText(order.getGoodsNumDesc());
            tvAmount.setText(order.getPayPriceDesc());
            tvFreight.setText(order.getFreightDesc());
            String[] str = order.getButtonDesc(true);
            if (TextUtils.isEmpty(str[0])) {
                btnOne.setVisibility(View.GONE);
            } else {
                btnOne.setVisibility(View.VISIBLE);
                btnOne.setText(str[0]);
            }

            if (TextUtils.isEmpty(str[1])) {
                btnTwo.setVisibility(View.GONE);
            } else {
                btnTwo.setVisibility(View.VISIBLE);
                btnTwo.setText(str[1]);
            }

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof Button) {
                        String text = ((Button) v).getText().toString();
                        if (TextUtils.equals(text, DutyOrderActionListener.DELETE)) {
                            //删除订单
                            // listener.onDelete(order, section);
                            dialog.setTvContent("您确认删除订单么？");
                            dialog.show();
                            dialog.setHintListener(new FHHintDialog.FHHintListener() {
                                @Override
                                public void onLeftClick() {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onRightClick() {
                                    listener.onDelete(order, section);
                                    dialog.dismiss();
                                }
                            });
                        }
                        if (TextUtils.equals(text, DutyOrderActionListener.CANCEL)) {
                            //取消订单
                            //   listener.onCancel(order, section);
                            dialog.setTvContent("您确认取消订单么？");
                            dialog.show();
                            dialog.setHintListener(new FHHintDialog.FHHintListener() {
                                @Override
                                public void onLeftClick() {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onRightClick() {
                                    listener.onCancel(order, section);
                                    dialog.dismiss();
                                }
                            });
                        }
                        if (TextUtils.equals(text, DutyOrderActionListener.PAY)) {
                            listener.onPay(order, section);
                        }
                        if (TextUtils.equals(text, DutyOrderActionListener.RECEIVE)) {
                            listener.onReceive(order, section);
                        }
                        if (TextUtils.equals(text, DutyOrderActionListener.DETAIL)) {
                            listener.onDetail(order, section);
                        }
                    }
                }
            };
            btnOne.setOnClickListener(clickListener);
            btnTwo.setOnClickListener(clickListener);
        }
    }
}
