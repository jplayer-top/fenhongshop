package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.WSHomeNationalPavList;
import com.fanglin.fenhong.microbuyer.common.NationalPavilionActivity;

import java.util.List;

/**
 * 首页 国家馆 item
 * 作者： Created by Plucky on 2015/10/8.
 */
public class NationalPavListAdapter extends RecyclerView.Adapter<NationalPavListAdapter.ViewHolder> {

    private Context mContext;
    private List<WSHomeNationalPavList> list;

    public NationalPavListAdapter(Context c) {
        this.mContext = c;
    }

    public void setList (List<WSHomeNationalPavList> lst) {
        this.list = lst;
    }

    public void addList (List<WSHomeNationalPavList> lst) {
        if (lst != null && lst.size () > 0) {
            this.list.addAll (lst);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_home_national_pav_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WSHomeNationalPavList nationalPavList = list.get(position);
        if (nationalPavList != null) {
            new FHImageViewUtil(holder.imageView).setImageURI(nationalPavList.activity_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NationalPavilionActivity.class);
                    intent.putExtra("activity_id", nationalPavList.activity_id);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_item_home_nat_pav);
        }
    }
}
