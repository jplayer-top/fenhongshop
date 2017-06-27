package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyBrandAll;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyBrandListViewHolder;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午1:31.
 * 功能描述: 极速免税店所有品牌
 */
public class DutyBrandAllAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private List<DutyBrandAll> brandAll;

    public DutyBrandAllAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBrandAll(List<DutyBrandAll> brandAll) {
        this.brandAll = brandAll;
        notifyDataSetChanged();
    }

    @Override
    public int getCountForSection(int section) {
        List<BrandMessage> list = brandAll.get(section).list;
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        if (brandAll == null)
            return 0;
        return brandAll.size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = DutyBrandListViewHolder.getView(mContext);
            DutyBrandListViewHolder.recyclerViewHolder(convertView);
        }
        BrandMessage brandMessage = brandAll.get(section).list.get(position);
        DutyBrandListViewHolder brandHolder = (DutyBrandListViewHolder) convertView.getTag();
        brandHolder.refreshView(brandMessage);
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_duty_brandall_header, null);
            new HeaderViewHolder(convertView);
        }

        DutyBrandAll item = brandAll.get(section);

        HeaderViewHolder holder = (HeaderViewHolder) convertView.getTag();
        holder.tvName.setText(item.title);
        return convertView;
    }

    public int getPositionByTitle(String title) {
        if (TextUtils.isEmpty(title)) return 0;
        int section = getSectionOfTitle(title);
        if (section == -1) return -1;
        return getPositionOfSection(section);
    }

    private int getSectionOfTitle(String title) {
        for (int i = 0; i < brandAll.size(); i++) {
            DutyBrandAll item = brandAll.get(i);
            if (TextUtils.equals(title, item.title)) {
                FHLog.d("Plucky", "sec:" + i + " title:" + title);
                return i;
            }
        }
        //如果没有匹配到 则为-1
        return -1;
    }

    //获取
    private int getPositionOfSection(int section) {
        int position = 0;
        for (int i = 0; i < section; i++) {
            position += getCountForSection(i) + 1;
        }
        return position;
    }

    class HeaderViewHolder {

        TextView tvName;

        public HeaderViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tvName);
            view.setTag(this);
        }
    }
}
