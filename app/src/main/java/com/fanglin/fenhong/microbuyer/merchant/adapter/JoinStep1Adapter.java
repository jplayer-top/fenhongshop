package com.fanglin.fenhong.microbuyer.merchant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.fanglin.fenhong.microbuyer.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-30.
 */
public class JoinStep1Adapter extends RecyclerView.Adapter<JoinViewHolder> {

    private Context mContext;
    private final static int linetextcolor = Color.rgb(0xa9, 0xa9, 0xa9);
    List<List<String>> list = new ArrayList<>();
    private List<String> rate = new ArrayList<>();

    public JoinStep1Adapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<List<String>> list, List<String> rate) {
        this.list = list;
        this.rate = rate;
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    private String parseList(int position, int index) {
        String res;
        try {
            res = list.get(position).get(index);
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    private String parseRate(int position) {
        String res;
        try {
            res = rate.get(position);
        } catch (Exception e) {
            res = "";
        }
        return res + "%";
    }

    @Override
    public void onBindViewHolder(JoinViewHolder holder, int position) {
        holder.tv_cls1.setText(parseList(position, 0));
        holder.tv_cls2.setText(parseList(position, 1));
        holder.tv_cls3.setText(parseList(position, 2));
        holder.tv_op.setText(parseRate(position));
        holder.tv_op.setTextColor(linetextcolor);
    }

    @Override
    public JoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_merchant_join, null);
        return new JoinViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}
