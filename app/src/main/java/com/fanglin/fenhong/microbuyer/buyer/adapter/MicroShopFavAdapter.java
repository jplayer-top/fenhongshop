package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fhui.CircleImageView;

/**
 * 作者： Created by Plucky on 2015/9/16.
 */
public class MicroShopFavAdapter extends FavoritesAdapter {

    public MicroShopFavAdapter (Context c) {
        super (c);
    }

    @Override
    public MicroShopViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = View.inflate (mContext, R.layout.item_fav_microshop, null);
        return new MicroShopViewHolder (view);
    }

    @Override
    public void onBindViewHolder (FavViewHolder holder, final int position) {
        MicroShopViewHolder mholder = (MicroShopViewHolder) holder;

        new FHImageViewUtil (mholder.sdv).setImageURI (getItem (position).shop_logo, FHImageViewUtil.SHOWTYPE.AVATAR);

        mholder.tv_title.setText (getItem (position).shop_name);
        String fmt = mContext.getString (R.string.fmt_microshop_collect);
        fmt = String.format (fmt, getItem (position).collect_count);
        mholder.tv_memo.setText (fmt);

        mholder.cb.setClickable (false);
        mholder.cb.setChecked (getItem (position).isSelected);
        mholder.cb.setVisibility (isShowChk ? View.VISIBLE : View.GONE);
        mholder.itemView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (isShowChk) {
                    getItem (position).isSelected = !getItem (position).isSelected;
                    notifyDataSetChanged ();
                    return;
                }
                if (mcb != null) {
                    mcb.onItemClick (2, position);
                }
            }
        });
    }

    class MicroShopViewHolder extends FavViewHolder {
        public CheckBox cb;
        public CircleImageView sdv;
        public TextView tv_title;
        public TextView tv_memo;

        public MicroShopViewHolder (View itemView) {
            super (itemView);
            cb = (CheckBox) itemView.findViewById (R.id.cb);
            sdv = (CircleImageView) itemView.findViewById (R.id.sdv);
            tv_title = (TextView) itemView.findViewById (R.id.tv_title);
            tv_memo = (TextView) itemView.findViewById (R.id.tv_memo);
        }
    }
}
