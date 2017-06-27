package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fhui.CircleImageView;

/**
 * 作者： Created by Plucky on 2015/9/16.
 */
public class TimeFavAdapter extends FavoritesAdapter {

    public TimeFavAdapter(Context c) {
        super(c);
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fav_time, null);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, final int position) {
        TimeViewHolder sholder = (TimeViewHolder) holder;

        new FHImageViewUtil(sholder.sdv).setImageURI(getItem(position).time_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
        new FHImageViewUtil(sholder.circleIV).setImageURI(getItem(position).time_talent_avatar, FHImageViewUtil.SHOWTYPE.DEFAULT);

        sholder.tvTitle.setText(getItem(position).time_content);
        sholder.tvDrname.setText(getItem(position).time_talent_name);

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
                    mcb.onItemClick(4, position);
                }
            }
        });
    }

    class TimeViewHolder extends FavViewHolder {
        public CheckBox cb;
        public ImageView sdv;
        public TextView tvTitle;
        public CircleImageView circleIV;
        public TextView tvDrname;

        public TimeViewHolder(View itemView) {
            super(itemView);
            cb = (CheckBox) itemView.findViewById(R.id.cb);
            sdv = (ImageView) itemView.findViewById(R.id.sdv);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            circleIV = (CircleImageView) itemView.findViewById(R.id.circleIV);
            tvDrname = (TextView) itemView.findViewById(R.id.tvDrname);
        }
    }
}
