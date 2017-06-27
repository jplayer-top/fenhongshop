package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BundlingItem;
import java.util.List;

/**
 * 商品促销 套装详情 数据适配
 * Created by lizhixin on 2016/1/4.
 */
public class GoodsPromBundleSingleAdapter extends RecyclerView.Adapter<GoodsPromBundleSingleAdapter.BundleSingleViewHolder> {

    private Context mContext;
    private List<BundlingItem> list;

    public GoodsPromBundleSingleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<BundlingItem> list) {
        this.list = list;
    }

    @Override
    public BundleSingleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_goods_prom_bundle_single_item, null);
        return new BundleSingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BundleSingleViewHolder holder, int position) {
        final BundlingItem item = list.get(position);
        if (item != null) {
            holder.tvName.setText(item.name);
            holder.tvPrice.setText(BaseFunc.fromHtml(String.format(mContext.getString(R.string.goods_detail_prom_single_item_price), item.shop_price)));
            new FHImageViewUtil(holder.imageView).setImageURI(item.image, FHImageViewUtil.SHOWTYPE.DEFAULT);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoGoodsDetail(mContext,item.id,null,null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    class BundleSingleViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvPrice;
        private ImageView imageView;
        private View view;

        public BundleSingleViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            view = itemView;
        }
    }

}
