package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.StoreHomeCls;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/7/14-上午11:49.
 * 功能描述:店铺商品分类适配器（按分区）
 */
public class StoreGoodsClassSectionedAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private List<StoreHomeCls> list;

    public StoreGoodsClassSectionedAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<StoreHomeCls> list) {
        this.list = list;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.add(0, StoreHomeCls.getDefault());
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int section, int position) {
        return section * 100 + position;
    }

    @Override
    public int getSectionCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public StoreHomeCls getSectionEntity(int section) {
        return list.get(section);
    }

    @Override
    public StoreHomeCls getItem(int section, int position) {
        return getSectionEntity(section).children.get(position);
    }

    @Override
    public int getCountForSection(int section) {
        List<StoreHomeCls> children = getSectionEntity(section).children;
        if (children == null)
            return 0;
        return 1;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_storeclass, null);
            new ItemViewHolder(convertView);
        }


        List<StoreHomeCls> children = getSectionEntity(section).children;
        final ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        holder.tflSubClass.setAdapter(new TagAdapter(children) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_store_subgoodsclass, holder.tflSubClass, false);
                TextView tvName = (TextView) view.findViewById(R.id.tvName);
                final StoreHomeCls homeCls = (StoreHomeCls) o;
                if (homeCls != null) {
                    tvName.setText(homeCls.stc_name);
                    tvName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (clickListener != null) {
                                clickListener.onItemClick(homeCls.stc_id);
                            }
                        }
                    });
                }
                return view;
            }
        });
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_storeclass_header, null);
            new HeaderViewHolder(convertView);
        }

        HeaderViewHolder holder = (HeaderViewHolder) convertView.getTag();

        final StoreHomeCls homeCls = getSectionEntity(section);
        holder.tvName.setText(homeCls.stc_name);
        if (homeCls.children != null && homeCls.children.size() > 0) {
            holder.tvMore.setText(R.string.lbl_storecls_4more);
        } else {
            holder.tvMore.setText(R.string.if_2right);
        }
        holder.clsId = homeCls.stc_id;

        return convertView;
    }


    class ItemViewHolder {
        TagFlowLayout tflSubClass;

        public ItemViewHolder(View view) {
            tflSubClass = (TagFlowLayout) view.findViewById(R.id.tflSubClass);
            view.setTag(this);
        }
    }

    class HeaderViewHolder {
        TextView tvName;
        TextView tvMore;

        String clsId;

        public HeaderViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvMore = (TextView) view.findViewById(R.id.tvMore);
            BaseFunc.setFont(tvMore);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onSectionClick(clsId);
                    }
                }
            });
            view.setTag(this);
        }
    }

    public interface OnGoodsClassClickListener {
        void onSectionClick(String classId);

        void onItemClick(String classId);
    }

    private OnGoodsClassClickListener clickListener;

    public void setListener(OnGoodsClassClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
