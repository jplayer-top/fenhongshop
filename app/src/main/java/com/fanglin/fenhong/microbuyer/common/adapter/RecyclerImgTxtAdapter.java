package com.fanglin.fenhong.microbuyer.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;

/**
 * 作者： Created by Plucky on 2015/8/31.
 */
public class RecyclerImgTxtAdapter extends RecyclerView.Adapter<RecyclerImgTxtAdapter.ViewHolder> {

    public RecyclerImgTxtAdapter () {

    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public int getItemCount () {
        return 10;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        holder.tv.setText (String.valueOf (position));
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = View.inflate (parent.getContext (), R.layout.image_txt_item, null);
        return new ViewHolder (v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView sdv;
        public TextView tv;

        public ViewHolder (View itemView) {
            super (itemView);
            sdv = (ImageView) itemView.findViewById (R.id.sdv);
            tv = (TextView) itemView.findViewById (R.id.tv);
        }
    }
}
