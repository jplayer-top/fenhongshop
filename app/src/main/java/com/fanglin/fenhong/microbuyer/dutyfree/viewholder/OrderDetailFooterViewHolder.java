package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyOrderGoods;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyWaybill;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-上午11:51.
 * 功能描述: 极速免税店 订单详情页 Footer
 */
public class OrderDetailFooterViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout LMore;
    TextView tvMore;

    public OrderDetailFooterViewHolder(View itemView) {
        super(itemView);
        LMore = (LinearLayout) itemView.findViewById(R.id.LMore);
        tvMore = (TextView) itemView.findViewById(R.id.tvMore);
    }

    public static OrderDetailFooterViewHolder getHolder(Context mContext) {
        View view = View.inflate(mContext, R.layout.item_orderdtl_footer, null);
        return new OrderDetailFooterViewHolder(view);
    }

    public void refreshView(DutyWaybill bill) {
        if (bill != null) {
            tvMore.setSelected(bill.isExpanded());
            tvMore.setText(bill.getExpandDesc());
        }
    }
}
