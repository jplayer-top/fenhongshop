package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.FansList;
import com.fanglin.fenhong.microbuyer.microshop.TalentActivity;
import com.fanglin.fhui.CircleImageView;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/21-下午5:04.
 * 功能描述:
 */
public class FansListAdapter extends RecyclerView.Adapter<FansListAdapter.ItemViewHolder> {

    private Context mContext;
    private List<FansList> list;

    public FansListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<FansList> getList() {
        return list;
    }

    public void setList(List<FansList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<FansList> tlist) {
        if (tlist != null && tlist.size() > 0) {
            if (list != null && list.size() > 0) {
                list.addAll(tlist);
                notifyDataSetChanged();
            }
        }
    }

    private FansList getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_fanslist, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final FansList fansList = getItem(position);
        if (fansList != null) {
            new FHImageViewUtil(holder.circleImageView).setImageURI(fansList.getFollower_avatar(), FHImageViewUtil.SHOWTYPE.AVATAR);
            holder.tvName.setText(fansList.getFollower_name());
            if (fansList.getTalent_id() > 0) {
                holder.tvExplorer.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoActivity(mContext, TalentActivity.class, String.valueOf(fansList.getTalent_id()));
                    }
                });
            } else {
                holder.tvExplorer.setVisibility(View.GONE);
            }

            String talentType = fansList.getTalent_type();
            if (!TextUtils.isEmpty(talentType)&&fansList.getTalent_id() > 0) {
                holder.tvSubTitle.setText(talentType);
                holder.tvSubTitle.setVisibility(View.VISIBLE);
            } else {
                holder.tvSubTitle.setVisibility(View.GONE);
            }
        }

        if (position == getItemCount() - 1) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvName, tvSubTitle, tvExplorer;
        View vLine;

        public ItemViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tvSubTitle);
            tvExplorer = (TextView) itemView.findViewById(R.id.tvExplorer);
            vLine = itemView.findViewById(R.id.vLine);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            BaseFunc.setFont(tvExplorer);
        }
    }
}
