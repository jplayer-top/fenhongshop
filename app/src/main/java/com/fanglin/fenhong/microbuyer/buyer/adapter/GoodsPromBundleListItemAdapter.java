package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BundlingItem;

import java.util.List;

/**
 * 促销商品 优惠套装列表 Item 里的 Item
 * Created by lizhixin on 2015/12/31.
 */
public class GoodsPromBundleListItemAdapter extends RecyclerView.Adapter<GoodsPromBundleListItemAdapter.BundleItemViewHolder> {

    private Context mContext;
    private List<BundlingItem> list;//优惠套装
    private OnItemClickListener onItemClickListener;

    public GoodsPromBundleListItemAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<BundlingItem> list) {
        this.list = list;
    }

    @Override
    public BundleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_goods_prom_bundle_list_item, null);
        return new BundleItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BundleItemViewHolder viewHolder, final int position) {
        if (position == 0) {
            viewHolder.tvPlus.setVisibility(View.GONE);
        } else {
            viewHolder.tvPlus.setVisibility(View.VISIBLE);
        }
        new FHImageViewUtil(viewHolder.image).setImageURI(list.get(position).image, FHImageViewUtil.SHOWTYPE.DEFAULT);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(viewHolder.itemView, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    class BundleItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvPlus;
        private ImageView image;
        private View itemView;

        public BundleItemViewHolder(View itemView) {
            super(itemView);
            this.tvPlus = (TextView) itemView.findViewById(R.id.tv_plus);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.itemView = itemView;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
