package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.GoodsArrivalNotice;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Author: Created by lizhixin on 2016/4/5 15:04.
 * 我的提醒 商品列表 adapter
 */
public class AOGRemindListAdapter extends RecyclerView.Adapter<AOGRemindListAdapter.AOGRemindViewHolder> {

    private Context mContext;
    private ArrayList<GoodsArrivalNotice> list;
    private RemindListener listener;

    public AOGRemindListAdapter(Context mContext, ArrayList<GoodsArrivalNotice> listTemp) {
        this.mContext = mContext;
        this.list = listTemp;
    }

    public ArrayList<GoodsArrivalNotice> getList() {
        return list;
    }

    public void setList(ArrayList<GoodsArrivalNotice> list) {
        this.list = list;
    }

    public void addList(ArrayList<GoodsArrivalNotice> listTemp) {
        if (this.list != null && listTemp != null) {
            this.list.addAll(listTemp);
        }
    }

    public void removeItem(String goodsId) {
        if (this.list != null) {
            Iterator<GoodsArrivalNotice> iterator = list.iterator();
            while (iterator.hasNext()) {
                GoodsArrivalNotice entity = iterator.next();
                if (TextUtils.equals(entity.goods_id, goodsId)) {
                    iterator.remove();
                }
            }
        }
    }

    public void setCheckBox(boolean checked, boolean displayed) {
        for (GoodsArrivalNotice entity : list) {
            entity.setChecked(checked);
            entity.setDisplay(displayed);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }

    @Override
    public AOGRemindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_aog_remind, null);
        return new AOGRemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AOGRemindViewHolder holder, final int position) {
        final GoodsArrivalNotice entity = list.get(position);
        if (entity != null) {
            new FHImageViewUtil(holder.ivGoodsImage).setImageURI(entity.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
            new FHImageViewUtil(holder.ivFlagNation).setImageURI(entity.nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);
            holder.tvTitle.setText(BaseFunc.fromHtml(String.format(mContext.getString(R.string.goods_title_nav), entity.nation_name, entity.goods_name.replaceAll("x", "ｘ"))));
            holder.tvPrice.setText(String.valueOf(entity.goods_price));

            holder.checkedChangeListener.setPosition(position);
            holder.checkBox.setVisibility(entity.isDisplay() ? View.VISIBLE : View.GONE);
            holder.checkBox.setChecked(entity.isChecked());

            holder.tvCancelNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCancelRemind(entity.goods_id, String.valueOf(position));
                    }
                }
            });
            holder.clickListener.setGoodsId(entity.goods_id);
        }
    }

    class AOGRemindViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llDesc;
        private CheckBox checkBox;
        private ImageView ivGoodsImage, ivFlagNation;
        private TextView tvTitle, tvPrice, tvCancelNotice;
        private RemindCheckedChangeListener checkedChangeListener;
        private RemindGoodsClickListener clickListener;

        public AOGRemindViewHolder(View itemView) {
            super(itemView);
            llDesc = (LinearLayout) itemView.findViewById(R.id.llDesc);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            ivGoodsImage = (ImageView) itemView.findViewById(R.id.ivGoodsImage);
            ivFlagNation = (ImageView) itemView.findViewById(R.id.ivFlagNation);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvCancelNotice = (TextView) itemView.findViewById(R.id.tvCancelNotice);

            checkedChangeListener = new RemindCheckedChangeListener();
            checkBox.setOnCheckedChangeListener(checkedChangeListener);

            clickListener = new RemindGoodsClickListener();
            llDesc.setOnClickListener(clickListener);
            ivGoodsImage.setOnClickListener(clickListener);
        }
    }

    class RemindCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            list.get(position).setChecked(isChecked);
        }
    }

    class RemindGoodsClickListener implements View.OnClickListener {
        private String goodsId;

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        @Override
        public void onClick(View v) {
            BaseFunc.gotoGoodsDetail(mContext, goodsId, null,null);
        }
    }

    public void setRemindListener(RemindListener listener) {
        this.listener = listener;
    }

    public interface RemindListener {
        void onCancelRemind(String goodsId, String position);
    }

}
