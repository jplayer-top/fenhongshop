package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Category;
import com.fanglin.fenhong.microbuyer.base.model.CategoryEntity;
import com.fanglin.fenhong.microbuyer.buyer.Category2Activity;
import com.google.gson.Gson;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * 分类按分区重构 Added By Plucky
 * Created by lizhixin on 2016/1/26 14:32.
 */
public class CategorySectionAdapter extends SectionedRecyclerViewAdapter<CategorySectionAdapter.HeaderViewHolder, CategorySectionAdapter.ItemViewHolder, RecyclerView.ViewHolder> {

    private Context mContext;
    public List<CategoryEntity> list;

    public CategorySectionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<CategoryEntity> list) {
        this.list = list;
    }

    @Override
    protected int getSectionCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    protected int getItemCountForSection(int i) {
        if (list.get(i) != null && list.get(i).list != null) {
            return list.get(i).list.size();
        }
        return 0;
    }

    @Override
    protected boolean hasFooterInSection(int i) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.adapter_header_category, null);
        return new HeaderViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    protected ItemViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_category_1, null);
        return new ItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int i) {
        holder.tv.setText(list.get(i).type);
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, int i, int i1) {
        final Category category = list.get(i).list.get(i1);
        holder.tv_title.setText(category.gc_name);
        new FHImageViewUtil(holder.sdv).setImageURI(category.gc_img, FHImageViewUtil.SHOWTYPE.DEFAULT);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(mContext, Category2Activity.class, new Gson().toJson(category));
            }
        });

        if (i1 % 2 == 0) {
            holder.placeholderRight.setVisibility(View.GONE);
        } else {
            holder.placeholderRight.setVisibility(View.VISIBLE);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView sdv;
        public TextView tv_title;
        public View placeholderRight;

        public ItemViewHolder(View itemView) {
            super(itemView);
            sdv = (ImageView) itemView.findViewById(R.id.sdv);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            placeholderRight = itemView.findViewById(R.id.placeholderRight);
        }
    }

}
