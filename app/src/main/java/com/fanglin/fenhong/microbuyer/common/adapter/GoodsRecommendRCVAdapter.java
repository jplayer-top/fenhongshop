package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/23.
 * 猜你喜欢 RecycleView 适配器
 */
public class GoodsRecommendRCVAdapter extends RecyclerView.Adapter<GoodsRecommendRCVAdapter.ViewHolder> {


    private Context mContext;
    private List<GoodsScheme> list;

    public GoodsRecommendRCVAdapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<GoodsScheme> list) {
        this.list = list;
    }

    public void addList(List<GoodsScheme> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }


    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_actdtl_goods, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //商品标题 + 标签
        holder.tv_title.setText(list.get(position).getGoodsNameForDetail(mContext));
        holder.LTag.setVisibility(View.GONE);

        DecimalFormat df = new DecimalFormat("¥#0.00");
        holder.tv_price.setText(df.format(list.get(position).goods_price));
        holder.tv_price_market.setText(df.format(list.get(position).goods_marketprice));
        holder.tv_price_market.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        new FHImageViewUtil(holder.sdv).setImageURI(list.get(position).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoGoodsDetail(mContext, list.get(position).goods_id, null, null);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public ImageView sdv;
        public TextView tv_price, tv_price_market;
        public LinearLayout LTag;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            LTag = (LinearLayout) itemView.findViewById(R.id.LTag);
            sdv = (ImageView) itemView.findViewById(R.id.sdv);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_price_market = (TextView) itemView.findViewById(R.id.tv_price_market);
        }
    }
}
