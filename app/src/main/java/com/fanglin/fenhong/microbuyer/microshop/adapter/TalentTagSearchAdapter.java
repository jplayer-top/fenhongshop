package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/15-下午4:36.
 * 功能描述: 搜索标签适配器
 */
public class TalentTagSearchAdapter extends BaseAdapter {
    public static final int TYPE_ADD = 0;
    public static final int TYPE_NORMAL = 1;

    private Context mContext;
    private List<String> list;
    private String addTag;

    public TalentTagSearchAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 更新数据源
     *
     * @param list   相似列表
     * @param addTag 搜索的关键字 如果在相似列表中存在 则不传
     */
    public void setListAndTag(List<String> list, String addTag) {
        this.list = list;
        this.addTag = addTag;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list == null)
            return 1;
        return list.size() + 1;
    }

    @Override
    public String getItem(int position) {
        return list.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int viewType = getItemViewType(position);
        if (convertView == null) {
            if (viewType == TYPE_ADD) {
                convertView = View.inflate(mContext, R.layout.item_talenttag_search_add, null);
            } else {
                convertView = View.inflate(mContext, R.layout.item_talenttag_search_normal, null);
            }
            new TagViewHolder(convertView);
        }

        TagViewHolder holder = (TagViewHolder) convertView.getTag();
        if (viewType == TYPE_ADD) {
            if (TextUtils.isEmpty(addTag)) {
                holder.LTag.setVisibility(View.GONE);
                holder.vLine.setVisibility(View.GONE);
            } else {
                holder.LTag.setVisibility(View.VISIBLE);
                holder.vLine.setVisibility(View.VISIBLE);
                holder.tvName.setText(addTag);
            }
        } else {
            holder.tvName.setText(getItem(position));
            if (position == getCount() - 1) {
                holder.vLine.setVisibility(View.GONE);
            } else {
                holder.vLine.setVisibility(View.VISIBLE);
            }
        }

        if (listener != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewType == TYPE_ADD) {
                        listener.onItemClick(true, addTag);
                    } else {
                        listener.onItemClick(false, getItem(position));
                    }

                }
            });
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_ADD;
        return TYPE_NORMAL;
    }

    class TagViewHolder {
        LinearLayout LTag;
        TextView tvName;
        View vLine;

        public TagViewHolder(View view) {
            LTag = (LinearLayout) view.findViewById(R.id.LTag);
            tvName = (TextView) view.findViewById(R.id.tvName);
            vLine = view.findViewById(R.id.vLine);
            view.setTag(this);
        }
    }

    private TalentTagSearchAdapterListener listener;

    public void setListener(TalentTagSearchAdapterListener listener) {
        this.listener = listener;
    }

    public interface TalentTagSearchAdapterListener {
        void onItemClick(boolean isAdd, String key);
    }
}
