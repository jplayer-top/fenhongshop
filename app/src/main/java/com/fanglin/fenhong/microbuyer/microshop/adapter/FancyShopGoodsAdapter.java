package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/8/31.
 */
public class FancyShopGoodsAdapter extends RecyclerView.Adapter<FancyShopGoodsAdapter.ViewHolder> {
    private Context mContext;
    private List<GoodsList> list;

    public FancyShopGoodsAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<GoodsList> list) {
        this.list = list;
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public int getItemCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {
        holder.tv.setText (list.get (position).goods_name);
        holder.tv.setVisibility (View.GONE);

        new FHImageViewUtil (holder.sdv).setImageURI (list.get (position).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

        holder.sdv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                BaseFunc.gotoGoodsDetail(mContext,list.get (position).goods_id,null,null);
                if (mcb != null) mcb.onGoodsClick (position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = View.inflate (mContext, R.layout.item_fancyshop_goods, null);
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

    private FSGACallBack mcb;

    public void setCallBack (FSGACallBack cb) {
        this.mcb = cb;
    }

    public interface FSGACallBack {
        void onGoodsClick (int index);
    }
}
