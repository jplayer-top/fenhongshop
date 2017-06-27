package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fhlib.other.FHLog;

/**
 * 作者： Created by Plucky on 2015/9/16.
 */
public class ShopFavAdapter extends FavoritesAdapter {

    public ShopFavAdapter(Context c) {
        super(c);
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fav_shop, null);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, final int position) {
        ShopViewHolder sholder = (ShopViewHolder) holder;

        new FHImageViewUtil(sholder.store_img).setImageURI(getItem(position).store_avatar, FHImageViewUtil.SHOWTYPE.DEFAULT);

        sholder.tv_store_name.setText(getItem(position).store_name);
        float starts = getItem(position).getStar();
        sholder.rating.setRating(starts);
        FHLog.d("Plucky", "stars=" + starts);

        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        sholder.rcv.setLayoutManager(lm);
        FavGoodsImgTxtAdapter fgitAdapter = new FavGoodsImgTxtAdapter(mContext);
        fgitAdapter.setList(getItem(position).commend_goods);
        sholder.rcv.setAdapter(fgitAdapter);

        sholder.cb.setClickable(false);
        sholder.cb.setChecked(getItem(position).isSelected);
        sholder.cb.setVisibility(isShowChk ? View.VISIBLE : View.GONE);
        sholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowChk) {
                    getItem(position).isSelected = !getItem(position).isSelected;
                    notifyDataSetChanged();
                    return;
                }
                if (mcb != null) {
                    mcb.onItemClick(1, position);
                }
            }
        });
    }

    class ShopViewHolder extends FavViewHolder {
        public CheckBox cb;
        public ImageView store_img;
        public TextView tv_store_name;
        public RatingBar rating;
        public RecyclerView rcv;

        public ShopViewHolder(View itemView) {
            super(itemView);
            cb = (CheckBox) itemView.findViewById(R.id.cb);
            store_img = (ImageView) itemView.findViewById(R.id.store_img);
            tv_store_name = (TextView) itemView.findViewById(R.id.tv_store_name);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            rcv = (RecyclerView) itemView.findViewById(R.id.rcv);
        }
    }
}
