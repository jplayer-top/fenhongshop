package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Category;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/8.
 */
public class Category2Adapter extends RecyclerView.Adapter<Category2Adapter.ViewHolder> {

    private Context mContext;
    private List<Category> list = new ArrayList<> ();

    public Category2Adapter (Context c) {
        mContext = c;
    }

    public void setList (List<Category> list) {
        this.list = list;
    }

    @Override
    public int getItemCount () {
        return list.size ();
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {
        holder.tv_title.setText (list.get (position).gc_name);

        new FHImageViewUtil (holder.sdv).setImageURI (list.get (position).gc_img, FHImageViewUtil.SHOWTYPE.DEFAULT);

        holder.itemView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (listener != null) {
                    listener.onItemClick (list.get (position), position);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = View.inflate (mContext, R.layout.item_category_2, null);
        return new ViewHolder (view);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sdv;
        TextView tv_title;

        public ViewHolder (View itemView) {
            super (itemView);
            sdv = (ImageView) itemView.findViewById (R.id.sdv);
            tv_title = (TextView) itemView.findViewById (R.id.tv_title);
        }
    }


    private Category2Listener listener;

    public void setListener (Category2Listener l) {
        this.listener = l;
    }

    public interface Category2Listener {
        void onItemClick (Category c2, int position);
    }
}
