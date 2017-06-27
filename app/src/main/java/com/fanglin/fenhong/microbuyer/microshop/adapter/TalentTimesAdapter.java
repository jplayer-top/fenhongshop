package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.listener.TalentImageClickListener;
import com.fanglin.fenhong.microbuyer.base.model.TalentImagesDetail;
import com.fanglin.fenhong.microbuyer.base.model.TalentTimesGroup;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/22-下午1:36.
 * 功能描述: 达人时光 按月日分组
 */
public class TalentTimesAdapter extends SectionedBaseAdapter {

    private List<TalentTimesGroup> list;
    private Context mContext;
    private LinearLayout.LayoutParams bannerParams;
    private String dayDot;

    public TalentTimesAdapter(Context mContext) {
        this.mContext = mContext;
        int dx = mContext.getResources().getDisplayMetrics().widthPixels;
        bannerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dx);
        dayDot = mContext.getString(R.string.day_dot);
    }

    public void setList(List<TalentTimesGroup> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<TalentTimesGroup> alist) {
        if (alist != null && alist.size() > 0) {
            if (list != null && list.size() > 0) {
                /**
                 * 如果原始列表的最后一个和新列表的第一个年月日相同 则合并
                 */
                int lastIndex = list.size() - 1;
                TalentTimesGroup lastOne = list.get(lastIndex);
                List<TalentImagesDetail> lastTimes = lastOne.getTimes();
                TalentTimesGroup firstOne = alist.get(0);
                List<TalentImagesDetail> firstTimes = firstOne.getTimes();
                if (TextUtils.equals(lastOne.gettYear(), firstOne.gettYear()) && TextUtils.equals(lastOne.gettMonth(), firstOne.gettMonth()) && TextUtils.equals(lastOne.gettDay(), firstOne.gettDay())) {
                    if (lastTimes != null && lastTimes.size() > 0 && firstTimes != null && firstTimes.size() > 0) {
                        lastTimes.addAll(firstTimes);
                        alist.remove(0);
                        list.addAll(alist);
                    } else {
                        list.addAll(alist);
                    }
                } else {
                    list.addAll(alist);
                }
                notifyDataSetChanged();
            }
        }
    }


    @Override
    public TalentImagesDetail getItem(int section, int position) {
        return getGroup(section).getTimes().get(position);
    }

    public void removeItem(int section, int position) {
        List<TalentImagesDetail> times = getGroup(section).getTimes();
        if (times != null && times.size() > 0) {
            times.remove(position);
            notifyDataSetChanged();
        }
    }

    public TalentTimesGroup getGroup(int section) {
        return list.get(section);
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public int getCountForSection(int section) {
        TalentTimesGroup group = getGroup(section);
        List<TalentImagesDetail> times = group.getTimes();
        if (times == null)
            return 0;
        return times.size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_talent_times, null);
            new ItemViewHolder(convertView);
        }

        final ItemViewHolder holder = (ItemViewHolder) convertView.getTag();

        final TalentImagesDetail detail = getItem(section, position);
        boolean isOwn = detail.getIs_own() == 1;

        holder.tvDesc.setText(detail.getContent());
        holder.tvCollect.setText(detail.getCount_text());

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        TalentTagAdapter adapter = new TalentTagAdapter(mContext);
        adapter.setList(detail.getTags());
        holder.recyclerView.setAdapter(adapter);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(detail.getTime_id());
                }
            }
        };

        holder.tvDesc.setOnClickListener(clickListener);

        TalentImageClickListener talentImageClickListener = new TalentImageClickListener() {
            @Override
            public void onImageClick(ViewGroup view) {
                if (listener != null) {
                    listener.onImageClick(view,section, position);
                }
            }
        };

        View bannerView = BaseFunc.getTalentBanner(mContext, detail.getImages(), talentImageClickListener, detail.getTalentMemberId());
        if (bannerView != null) {
            holder.LBanner.setVisibility(View.VISIBLE);
            holder.LBanner.removeAllViews();
            holder.LBanner.addView(bannerView);
        } else {
            holder.LBanner.setVisibility(View.GONE);
        }

        if (listener != null) {
            holder.ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMore(holder.ivMore, section, position);
                }
            });

            holder.LShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShare(v,section, position);
                }
            });

            holder.LComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onComment(section, position);
                }
            });

            holder.LCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCollect(section, position);
                }
            });
        }


        boolean isLastSection = (section == getSectionCount() - 1);
        boolean isLastPosition = (position == getCountForSection(section) - 1);

        if (isLastSection && isLastPosition) {
            holder.vLine.setVisibility(View.VISIBLE);
            holder.vDivider.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.GONE);
            holder.vDivider.setVisibility(View.VISIBLE);
        }

        holder.tvShareNum.setText(detail.getShareCountDesc());
        holder.tvCommentNum.setText(detail.getCommentCountDesc());
        holder.tvCollectNum.setText(detail.getCollectCountDesc());
        holder.tvCollectNum.setSelected(detail.isCollected());

        holder.tvBuy.setText(detail.getCount_text2());

        if (isOwn) {
            holder.LTalent.setVisibility(View.VISIBLE);
            holder.LVisitor.setVisibility(View.GONE);
        } else {
            holder.LTalent.setVisibility(View.GONE);
            holder.LVisitor.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_talent_secion_header, null);
            new HeaderViewHolder(convertView);
        }
        HeaderViewHolder holder = (HeaderViewHolder) convertView.getTag();
        TalentTimesGroup group = getGroup(section);
        //这里只显示月日
        holder.tvYear.setText(group.gettMonthZh());
        holder.tvMonth.setText(group.gettDay());
        holder.tvDesc.setText(dayDot);
        holder.vLine.setVisibility(View.VISIBLE);
        return convertView;
    }

    class ItemViewHolder {
        LinearLayout LBanner, LTalent;
        TextView tvDesc;
        RecyclerView recyclerView;
        TextView tvCollect, tvBuy;
        ImageView ivMore;
        View vDivider, vLine;
        LinearLayout LVisitor, LShare, LComment, LCollect;
        TextView tvShareNum, tvCommentNum, tvCollectNum;

        public ItemViewHolder(View itemView) {
            LTalent = (LinearLayout) itemView.findViewById(R.id.LTalent);
            LBanner = (LinearLayout) itemView.findViewById(R.id.LBanner);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            tvCollect = (TextView) itemView.findViewById(R.id.tvCollect);
            tvBuy = (TextView) itemView.findViewById(R.id.tvBuy);
            ivMore = (ImageView) itemView.findViewById(R.id.ivMore);
            vDivider = itemView.findViewById(R.id.vDivider);
            vLine = itemView.findViewById(R.id.vLine);

            LVisitor = (LinearLayout) itemView.findViewById(R.id.LVisitor);
            LShare = (LinearLayout) itemView.findViewById(R.id.LShare);
            LComment = (LinearLayout) itemView.findViewById(R.id.LComment);
            LCollect = (LinearLayout) itemView.findViewById(R.id.LCollect);
            tvShareNum = (TextView) itemView.findViewById(R.id.tvShareNum);
            tvCommentNum = (TextView) itemView.findViewById(R.id.tvCommentNum);
            tvCollectNum = (TextView) itemView.findViewById(R.id.tvCollectNum);

            LBanner.setLayoutParams(bannerParams);

            itemView.setTag(this);
        }
    }

    class HeaderViewHolder {
        TextView tvMonth, tvYear, tvDesc;
        View vLine;

        public HeaderViewHolder(View itemView) {
            tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            vLine = itemView.findViewById(R.id.vLine);
            itemView.setTag(this);
        }
    }

    private TalentTimesAdapterListener listener;

    public void setListener(TalentTimesAdapterListener listener) {
        this.listener = listener;
    }

    public interface TalentTimesAdapterListener {
        void onMore(View view, int section, int position);

        void onItemClick(String talentId);

        void onShare(View view,int section, int position);

        void onComment(int section, int position);

        void onCollect(int section, int position);

        void onImageClick(ViewGroup view,int section, int position);
    }
}
