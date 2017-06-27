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
public class DrFavAdapter extends FavoritesAdapter {

    public DrFavAdapter(Context c) {
        super(c);
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fav_dr, null);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, final int position) {
        TimeViewHolder sholder = (TimeViewHolder) holder;

        new FHImageViewUtil(sholder.circleIV).setImageURI(getItem(position).talent_avatar, FHImageViewUtil.SHOWTYPE.DEFAULT);

        sholder.tvName.setText(getItem(position).talent_name);
        sholder.tvTime.setText(mContext.getString(R.string.dr_time_) + getItem(position).time_count);

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
                    mcb.onItemClick(5, position);
                }
            }
        });
    }

    class TimeViewHolder extends FavViewHolder {
        public CheckBox cb;
        public TextView tvName;
        public CircleImageView circleIV;
        public TextView tvTime;

        public TimeViewHolder(View itemView) {
            super(itemView);
            cb = (CheckBox) itemView.findViewById(R.id.cb);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            circleIV = (CircleImageView) itemView.findViewById(R.id.circleIV);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
