package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 作者： Created by Plucky on 15-10-8.
 */
public class HorizonalTagAdapter extends RecyclerView.Adapter<HorizonalTagAdapter.ViewHolder> {


    private Context mContext;

    public HorizonalTagAdapter (Context c) {
        this.mContext = c;
    }


    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = View.inflate (mContext, R.layout.item_grpbuy_dtl_cls, null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount () {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout LDef;
        public TextView tv_title;
        public View vline;

        public ViewHolder (View itemView) {
            super (itemView);
            LDef = (LinearLayout) itemView.findViewById (R.id.LDef);
            tv_title = (TextView) itemView.findViewById (R.id.tv_title);
            vline = itemView.findViewById (R.id.vline);
        }
    }

    public HorizonalTagListener mlistener;

    public void setListener (HorizonalTagListener listener) {
        this.mlistener = listener;
    }

    public interface HorizonalTagListener {
        void onItemClick (int position, int goods_position);
    }
}
