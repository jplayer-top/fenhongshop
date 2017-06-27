package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.listener.TalentImageClickListener;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.TalentImagesDetail;
import com.fanglin.fenhong.microbuyer.base.model.TalentInfo;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TalentTagAdapter;
import com.fanglin.fhui.CircleImageView;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author: Created by Plucky on 16/6/23-上午8:51.
 * 功能描述: 达人发现首页
 */
public class TalentFindAdapter extends SectionedBaseAdapter {

    private List<TalentImagesDetail> times;//时光
    private Context mContext;
    private LinearLayout.LayoutParams bannerParams;

    public TalentFindAdapter(Context mContext) {
        this.mContext = mContext;
        int dx = mContext.getResources().getDisplayMetrics().widthPixels;
        bannerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dx);
    }

    public void setTimes(List<TalentImagesDetail> times) {
        this.times = times;
        notifyDataSetChanged();
    }

    public void addTimes(List<TalentImagesDetail> list) {
        if (list != null && list.size() > 0) {
            if (times != null && times.size() > 0) {
                this.times.addAll(list);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public TalentImagesDetail getItem(int section, int position) {
        return times.get(section);
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        if (times == null)
            return 0;
        return times.size();
    }

    @Override
    public int getCountForSection(int section) {
        return 1;
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_talentfind_times, null);
            new ItemViewHolder(convertView);
        }
        ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        TalentImagesDetail detail = getItem(section, 0);
        if (detail != null) {
            holder.tvDesc.setText(detail.getContent());

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            TalentTagAdapter adapter = new TalentTagAdapter(mContext);
            adapter.setList(detail.getTags());
            holder.recyclerView.setAdapter(adapter);

            View.OnClickListener imgClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(section, position);
                    }
                }
            };

            TalentImageClickListener talentImageClickListener = new TalentImageClickListener() {
                @Override
                public void onImageClick(ViewGroup view) {
                    if (listener != null) {
                        listener.onImageClick(view,section, position);
                    }
                }
            };

            holder.tvDesc.setOnClickListener(imgClickListener);

            View bannerView = BaseFunc.getTalentBanner(mContext, detail.getImages(), talentImageClickListener, detail.getTalentMemberId());
            if (bannerView != null) {
                holder.LBanner.setVisibility(View.VISIBLE);
                holder.LBanner.removeAllViews();
                holder.LBanner.addView(bannerView);
            } else {
                holder.LBanner.setVisibility(View.GONE);
            }

            boolean isLastSection = (section == getSectionCount() - 1);
            boolean isLastPosition = (position == getCountForSection(section) - 1);

            if (isLastSection && isLastPosition) {
                holder.vDivider.setVisibility(View.GONE);
            } else {
                holder.vDivider.setVisibility(View.VISIBLE);
            }

            holder.tvShareNum.setText(detail.getShareCountDesc());
            holder.tvCommentNum.setText(detail.getCommentCountDesc());
            holder.tvCollectNum.setText(detail.getCollectCountDesc());
            holder.tvCollectNum.setSelected(detail.isCollected());

            if (listener != null) {

                holder.LShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onShareItem(v,section, position);
                    }
                });

                holder.LComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCommentItem(section, position);
                    }
                });

                holder.LCollect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCollectItem(section, position);
                    }
                });
            }
        }
        return convertView;
    }

    @Override
    public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_talentimagedtl_focus, null);
            new HeaderViewHolder(convertView);
        }

        TalentImagesDetail detail = getItem(section, 0);
        if (detail != null) {
            HeaderViewHolder holder = (HeaderViewHolder) convertView.getTag();
            TalentInfo info = detail.getTalent();
            if (info != null) {
                holder.tvName.setText(info.getTalent_name());
                String talentType = info.getTalent_type();
                if (TextUtils.isEmpty(talentType)) {
                    holder.tvDesc.setVisibility(View.GONE);
                } else {
                    holder.tvDesc.setText(talentType);
                    holder.tvDesc.setVisibility(View.VISIBLE);
                }
                new FHImageViewUtil(holder.circleImageView).setImageURI(info.getTalent_avatar(), FHImageViewUtil.SHOWTYPE.AVATAR);
            }
            holder.btnFocus.setVisibility(View.INVISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onHeaderClick(section, 0);
                    }
                }
            });
        }
        return convertView;
    }


    class ItemViewHolder {
        LinearLayout LBanner;
        TextView tvDesc;
        RecyclerView recyclerView;
        LinearLayout LVisitor, LShare, LComment, LCollect;
        TextView tvShareNum, tvCommentNum, tvCollectNum;
        View vDivider;

        public ItemViewHolder(View view) {
            LBanner = (LinearLayout) view.findViewById(R.id.LBanner);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            LVisitor = (LinearLayout) view.findViewById(R.id.LVisitor);
            LShare = (LinearLayout) view.findViewById(R.id.LShare);
            LComment = (LinearLayout) view.findViewById(R.id.LComment);
            LCollect = (LinearLayout) view.findViewById(R.id.LCollect);
            tvShareNum = (TextView) view.findViewById(R.id.tvShareNum);
            tvCommentNum = (TextView) view.findViewById(R.id.tvCommentNum);
            tvCollectNum = (TextView) view.findViewById(R.id.tvCollectNum);
            vDivider = view.findViewById(R.id.vDivider);

            LBanner.setLayoutParams(bannerParams);

            view.setTag(this);
        }
    }

    class HeaderViewHolder {
        CircleImageView circleImageView;
        TextView tvName, tvDesc;
        Button btnFocus;

        public HeaderViewHolder(View view) {
            circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            btnFocus = (Button) view.findViewById(R.id.btnFocus);
            view.setTag(this);
        }
    }

    private TalentFindAdapterListener listener;

    public void setListener(TalentFindAdapterListener listener) {
        this.listener = listener;
    }

    public interface TalentFindAdapterListener {
        void onItemClick(int section, int position);

        void onShareItem(View view,int section, int position);

        void onCommentItem(int section, int position);

        void onCollectItem(int section, int position);

        void onHeaderClick(int section, int position);

        void onImageClick(ViewGroup view,int section, int position);
    }
}
