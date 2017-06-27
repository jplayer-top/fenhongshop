package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/13-下午3:07.
 * 功能描述:达人时光标签
 */
public class TalentTagAdapter extends RecyclerView.Adapter<TalentTagAdapter.ItemViewHolder> {

    private Context mContext;
    private List<String> list;
    private boolean isEditMode = false;
    private int maxSize = 0;

    public static final int TYPE_ADD = 0;
    public static final int TYPE_NORMAL = 1;


    public TalentTagAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getTagCountStr() {
        return getItemCount() - 1 + "/"+maxSize;
    }

    public void addItem(String item) {
        if (list != null) {
            list.add(item);
            notifyDataSetChanged();
        }
    }

    public List<String> getList() {
        return list;
    }

    public void removeItem(int position) {
        if (list != null) {
            list.remove(position - 1);
            notifyDataSetChanged();
        }
    }

    public String getItem(int pos) {
        return list.get(pos - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_ADD;
        return TYPE_NORMAL;
    }

    /**
     * 编辑模式与最大数
     *
     * @param isEditMode boolean
     * @param maxSize    int
     */
    public void setEditModeAndMaxSize(boolean isEditMode, int maxSize) {
        this.isEditMode = isEditMode;
        this.maxSize = maxSize;
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ADD) {
            view = View.inflate(mContext, R.layout.item_talent_addtags, null);
        } else {
            view = View.inflate(mContext, R.layout.item_talent_tags, null);
        }
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final int viewType = getItemViewType(position);
        if (viewType == TYPE_ADD) {
            if (isEditMode) {
                if (getItemCount() - 1 < maxSize) {
                    holder.tvName.setVisibility(View.VISIBLE);
                } else {
                    holder.tvName.setVisibility(View.GONE);
                }
            } else {
                holder.tvName.setVisibility(View.GONE);
            }
        } else {
            holder.tvName.setText(getItem(position));
        }

        if (mListener != null) {
            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(viewType == TYPE_ADD,position);
                }
            });
            holder.tvName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemLongClick(viewType == TYPE_ADD,position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 1;
        return list.size() + 1;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }

    private TalentTagAdapterListener mListener;

    public void setListener(TalentTagAdapterListener listener) {
        this.mListener = listener;
    }

    public interface TalentTagAdapterListener {
        void onItemClick(boolean isAddBtn, int position);

        void onItemLongClick(boolean isAddBtn, int position);
    }
}
