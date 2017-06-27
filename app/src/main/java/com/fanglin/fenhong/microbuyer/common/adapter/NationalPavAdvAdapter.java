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
import com.fanglin.fenhong.microbuyer.base.model.NationalPavAdvEntity;
import java.util.ArrayList;

/**
 * 国家馆 —— 广告 —— 数据适配器
 * Created by lizhixin on 2015/11/27.
 */
public class NationalPavAdvAdapter extends RecyclerView.Adapter<NationalPavAdvAdapter.AdvViewHolder> {

    private Context context;
    private ArrayList<NationalPavAdvEntity> list;

    public NationalPavAdvAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<NationalPavAdvEntity> list) {
        this.list = list;
    }

    @Override
    public AdvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_national_pav_adv, null);
        return new AdvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdvViewHolder holder, int position) {
        final NationalPavAdvEntity advEntity = list.get(position);
        if (advEntity != null) {
            int w = BaseFunc.getDisplayMetrics(context).widthPixels;
            int h = w * 450 / 1080;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
            new FHImageViewUtil(holder.image).setImageURI(advEntity.getPic(), FHImageViewUtil.SHOWTYPE.GROUP);
            holder.image.setLayoutParams(params);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.urlClick(context,advEntity.getLink());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    class AdvViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public AdvViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_item_adv);
        }
    }

}
