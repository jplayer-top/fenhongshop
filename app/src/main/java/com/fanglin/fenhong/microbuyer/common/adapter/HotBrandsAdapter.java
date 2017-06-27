package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.HotBrands;
import com.fanglin.fenhong.microbuyer.common.GroupActivity;

import java.util.List;

/**
 * Created by Plucky on 2015/10/12.
 * 热门品牌适配器
 */
public class HotBrandsAdapter extends RecyclerView.Adapter<HotBrandsAdapter.ViewHolder> {

    private Context mContext;
    private List<HotBrands> list;
    LinearLayout.LayoutParams paramsBrandPic;

    public HotBrandsAdapter(Context c) {
        this.mContext = c;

        int screenWidth = BaseFunc.getDisplayMetrics(c).widthPixels;
        int brandWidth = (screenWidth - 10) / 2;
        int brandHeight = 230 * brandWidth / 350;//品牌馆图片高度
        paramsBrandPic = new LinearLayout.LayoutParams(brandWidth, brandHeight);
    }

    public void setList(List<HotBrands> list) {
        this.list = list;
    }

    public void addList(List<HotBrands> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_hotbrand, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        new FHImageViewUtil(holder.fiv_pic).setImageURI(list.get(position).brand_pic, FHImageViewUtil.SHOWTYPE.NEWHOME_BRAND);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseFunc.isValidUrl(list.get(position).brand_url)) {
                    BaseFunc.urlClick(mContext, list.get(position).brand_url);
                } else {
                    BaseFunc.gotoActivity(mContext, GroupActivity.class, list.get(position).brand_id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fiv_pic;

        public ViewHolder(View v) {
            super(v);
            fiv_pic = (ImageView) v.findViewById(R.id.fiv_pic);
            fiv_pic.setLayoutParams(paramsBrandPic);
        }
    }
}
