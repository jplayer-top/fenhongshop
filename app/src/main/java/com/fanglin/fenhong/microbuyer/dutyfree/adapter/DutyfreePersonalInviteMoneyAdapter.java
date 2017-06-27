package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreePersonalMessageBean;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyfreePersonalMessageHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/6.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */
public class DutyfreePersonalInviteMoneyAdapter extends SectionedRecyclerViewAdapter<DutyfreePersonalInviteMoneyAdapter.DutyfreePersonalCountsHolder, DutyfreePersonalMessageHolder, DutyfreePersonalMessageHolder> {
    private Context mContext;
    private List<DutyfreePersonalMessageBean.AwardListBean> awardList;
    private DutyfreePersonalMessageBean personalMessageBean;

    public DutyfreePersonalInviteMoneyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setVIPMessage(DutyfreePersonalMessageBean personalMessageBean) {
        if (personalMessageBean != null && personalMessageBean.award_list.size() > 0) {
            this.personalMessageBean = personalMessageBean;
            this.awardList = personalMessageBean.award_list;
            notifyDataSetChanged();
        }
    }

    public void addVIPMessage(DutyfreePersonalMessageBean personalMessageBean) {
        if (personalMessageBean != null && awardList.size() > 0) {
            awardList.addAll(personalMessageBean.award_list);
            notifyDataSetChanged();
        }
    }


    @Override
    protected int getSectionCount() {
        return 1;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return personalMessageBean == null ? 0 : personalMessageBean.award_list.size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected DutyfreePersonalCountsHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_allpersonal, null);
        return new DutyfreePersonalCountsHolder(itemView);
    }

    @Override
    protected DutyfreePersonalMessageHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected DutyfreePersonalMessageHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return DutyfreePersonalMessageHolder.getHolder(mContext);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(DutyfreePersonalCountsHolder holder, int section) {
        holder.llAllPersonal.setVisibility(View.INVISIBLE);
        holder.tvPersonalCounts.setText(personalMessageBean.total_person);
    }

    @Override
    protected void onBindSectionFooterViewHolder(DutyfreePersonalMessageHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(DutyfreePersonalMessageHolder holder, int section, int position) {
        DutyfreePersonalMessageBean.AwardListBean awardListBean = personalMessageBean.award_list.get(position);
        holder.bindData2ItemHolder(position, awardListBean);
    }

    public class DutyfreePersonalCountsHolder extends RecyclerView.ViewHolder {
        TextView tvPersonalCounts;
        LinearLayout llAllPersonal;

        public DutyfreePersonalCountsHolder(View itemView) {
            super(itemView);
            tvPersonalCounts = (TextView) itemView.findViewById(R.id.tvPersonalCounts);
            llAllPersonal = (LinearLayout) itemView.findViewById(R.id.llAllPersonal);
            itemView.setTag(this);
        }
    }

}
