package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/14.
 */
public class RecycleImgAdapter extends RecyclerView.Adapter<RecycleImgAdapter.ViewHolder> {
    private Context mContext;
    private String[] list = new String[]{};

    public RecycleImgAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (String[] list) {
        this.list = list;
    }

    /*imgs 以逗号隔开*/
    public void setList (String imgs) {
        if (imgs != null) {
            list = imgs.split (",");
        }
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public int getItemCount () {
        if (list == null) return 0;
        return list.length;
    }

    private List<String> getList () {
        List<String> l = new ArrayList<> ();
        if (list != null && list.length > 0) {
            for (int i = 0; i < list.length; i++) {
                l.add (list[i]);
            }
        }
        return l;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {
        new FHImageViewUtil (holder.sdv).setImageURI (list[position], FHImageViewUtil.SHOWTYPE.DEFAULT);
        holder.sdv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mcb != null) {
                    mcb.onItemClick (position);
                } else {
                    FileUtils.BrowserOpenL (mContext, getList (), list[position]);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = View.inflate (mContext, R.layout.item_image, null);
        return new ViewHolder (view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView sdv;

        public ViewHolder (View itemView) {
            super (itemView);
            sdv = (ImageView) itemView.findViewById (R.id.sdv);
        }
    }


    public interface RecyclerImgCallBack {
        void onItemClick (int position);
    }

    private RecyclerImgCallBack mcb;

    public void setCallBack (RecyclerImgCallBack cb) {
        this.mcb = cb;
    }

}
