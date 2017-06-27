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
import com.fanglin.fenhong.microbuyer.base.model.Favorites;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/16.
 */
public class FavGoodsImgTxtAdapter extends RecyclerView.Adapter<FavGoodsImgTxtAdapter.FGITViewHolder> {

    private Context mContext;
    private List<Favorites.FavGoods> list = new ArrayList<>();

    public FavGoodsImgTxtAdapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<Favorites.FavGoods> lst) {
        this.list = lst;
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

    public Favorites.FavGoods getItem(int position) {
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(FGITViewHolder holder, final int position) {
        new FHImageViewUtil(holder.sdv).setImageURI(getItem(position).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

        holder.tv.setText( getItem(position).getPriceDesc());
        holder.sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoGoodsDetail(mContext, getItem(position).goods_id, null,null);
            }
        });
    }

    @Override
    public FGITViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fav_goods_imgtxt, null);
        return new FGITViewHolder(view);
    }

    class FGITViewHolder extends RecyclerView.ViewHolder {
        public ImageView sdv;
        public TextView tv;

        public FGITViewHolder(View itemView) {
            super(itemView);
            sdv = (ImageView) itemView.findViewById(R.id.sdv);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
