package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/24-下午1:37.
 * 功能描述: 品牌聚合页 筛选头
 */
public class DutyBrandDtlPindedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvDefault, tvSales, tvPrice, tvUp, tvDown, tvPopular;
    LinearLayout LPrice;

    private int index, order;

    public DutyBrandDtlPindedViewHolder(View itemView) {
        super(itemView);
        tvDefault = (TextView) itemView.findViewById(R.id.tvDefault);
        tvSales = (TextView) itemView.findViewById(R.id.tvSales);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        tvUp = (TextView) itemView.findViewById(R.id.tvUp);
        tvDown = (TextView) itemView.findViewById(R.id.tvDown);
        tvPopular = (TextView) itemView.findViewById(R.id.tvPopular);

        LPrice = (LinearLayout) itemView.findViewById(R.id.LPrice);

        tvDefault.setOnClickListener(this);
        tvSales.setOnClickListener(this);
        LPrice.setOnClickListener(this);
        tvPopular.setOnClickListener(this);

        BaseFunc.setFont(tvUp);
        BaseFunc.setFont(tvDown);

        itemView.setTag(this);
    }

    public static View getView(Context mContext) {
        return View.inflate(mContext, R.layout.item_group_header_pinned, null);
    }

    public static DutyBrandDtlPindedViewHolder getHolderByView(View view) {
        return new DutyBrandDtlPindedViewHolder(view);
    }

    public static DutyBrandDtlPindedViewHolder getHolder(Context mContext) {
        View view = getView(mContext);
        return new DutyBrandDtlPindedViewHolder(view);
    }

    public void refreshView(int index, int order) {
        this.index = index;
        this.order = order;
        tvDefault.setSelected(index == 0);
        tvSales.setSelected(index == 1);
        tvPrice.setSelected(index == 2);
        tvUp.setSelected(index == 2 && order == 1);
        tvDown.setSelected(index == 2 && order == 2);

        tvPopular.setSelected(index == 3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDefault:
                index = 0;
                break;
            case R.id.tvSales:
                index = 1;
                break;
            case R.id.LPrice:
                index = 2;
                order = order == 2 ? 1 : 2;
                break;
            case R.id.tvPopular:
                index = 3;
                break;
        }

        if (headerListener != null) {
            headerListener.onFilter(index, order);
        }
    }

    private PindedHeaderListener headerListener;

    public void setHeaderListener(PindedHeaderListener headerListener) {
        this.headerListener = headerListener;
    }

    public interface PindedHeaderListener {
        void onFilter(int index, int order);
    }
}
