package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyCategoryDetailActivity;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/13-上午11:59.
 * 功能描述: 极速免税 分类适配器
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<DutyfreeCategory> list;

    public CategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public void setList(List<DutyfreeCategory> list) {
        this.list = list;
    }

    public DutyfreeCategory getItem(int position) {
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final DutyfreeCategory item = getItem(position);
        holder.tvName.setText(item.getTypeName());
        new FHImageViewUtil(holder.ivImage).setImageURI(item.getImg(), FHImageViewUtil.SHOWTYPE.DEFAULT);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = item.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    if (BaseFunc.isValidUrl(item.getUrl())) {
                        BaseFunc.urlClick(mContext, item.getUrl());
                    } else {
                        BaseFunc.gotoActivity(mContext, DutyCategoryDetailActivity.class, item.getId());
                    }
                } else {
                    BaseFunc.showMsg(mContext, msg);
                }
            }
        });
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_category_one, null);
        return new CategoryViewHolder(view);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;
        public TextView tvName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }
}
