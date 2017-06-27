package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.SeqGoodsModel;
import com.fanglin.fenhong.microbuyer.base.model.SequenceModel;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;

import java.text.DecimalFormat;
import java.util.List;


/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/7-下午5:54.
 * 功能描述:
 */
public class SeqGoodsAdapter extends SectionedBaseAdapter {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SINGLE = 1;

    private Context mContext;
    private SequenceModel mseq;
    private List<SeqGoodsModel> list;
    private DecimalFormat dformat;

    public SeqGoodsAdapter(Context context) {
        super();
        this.mContext = context;
        dformat = new DecimalFormat("¥#0.00");
    }

    @Override
    public SeqGoodsModel getItem(int section, int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return 1;
    }

    @Override
    public int getItemViewTypeCount() {
        return 2;
    }

    public void setList(List<SeqGoodsModel> lst) {
        this.list = lst;
    }

    public void setMseq(SequenceModel mseq) {
        this.mseq = mseq;
    }

    @Override
    public int getItemViewType(int section, int position) {
        if (getCountForSection(section) == 1) {
            return TYPE_SINGLE;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCountForSection(int section) {
        if (list == null)
            return 0;
        return list.size();
    }


    //神秘商品 --- begin

    /**
     * 是否为神秘商品
     *
     * @return true、false
     */
    public boolean isCover() {
        return mseq != null && mseq.if_cover == 1;
    }

    public String getPrices4Cover(int section, int position) {
        if (isCover()) {
            return "?";
        } else {
            return dformat.format(getItem(section, position).killing_price);
        }
    }

    public String getOriginPrices4Cover(int section, int position) {
        if (isCover()) {
            return "?";
        } else {
            return dformat.format(getItem(section, position).goods_price);
        }
    }

    public String getCoverTitle(int section, int position) {
        if (isCover()) {
            return getItem(section, position).cover_title;
        } else {
            return getItem(section, position).killing_title;
        }
    }

    public String getCoverPic(int section, int position) {
        if (isCover()) {
            return getItem(section, position).cover_pic;
        } else {
            return getItem(section, position).killing_pic;
        }
    }
    //神秘商品 --- end


    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(section, position);
        if (convertView == null) {
            if (viewType == TYPE_SINGLE) {
                convertView = View.inflate(mContext, R.layout.item_sequence_single, null);
            } else {
                convertView = View.inflate(mContext, R.layout.item_seq_normal, null);
            }

            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        new FHImageViewUtil(holder.iv_goods_img).setImageURI(getCoverPic(section, position), FHImageViewUtil.SHOWTYPE.DEFAULT);
        holder.tv_goodsname.setText(getCoverTitle(section, position));

        holder.tv_goods_price.setText(getPrices4Cover(section, position));
        holder.tv_goods_origin_price.setText(getOriginPrices4Cover(section, position));
        holder.tv_goods_origin_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.pb_goods_percent.setProgress(getItem(section, position).killed_pct);
        holder.tv_sales_status.setText(getItem(section, position).getKillPctDesc());
        // countdown==0 表示已经开场
        if (getItem(section, position).countdown == 0) {
            //如果还没结束
            if (getItem(section, position).is_end == 0) {
                holder.tv_sales_status.setVisibility(View.VISIBLE);
                holder.pb_goods_percent.setVisibility(View.VISIBLE);
                if (getItem(section, position).isSaleOut()) {
                    holder.tv_saleout_flag.setText("已抢光");
                    holder.tv_saleout_flag.setVisibility(View.VISIBLE);
                    holder.tv_btn.setBackgroundResource(R.drawable.shape_redstroke_corner);
                    holder.tv_btn.setText(mContext.getString(R.string.miaosha_more));
                    holder.tv_btn.setTextColor(mContext.getResources().getColor(R.color.fh_red));
                    holder.tv_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoActivity(mContext, StoreActivity.class, getItem(section, position).store_id);
                        }
                    });
                } else {
                    holder.tv_saleout_flag.setVisibility(View.INVISIBLE);
                    holder.tv_btn.setBackgroundResource(R.drawable.shape_redsolid_corner);
                    holder.tv_btn.setText(mContext.getString(R.string.miaosha_rightnow));
                    holder.tv_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isCover()) return;
                            BaseFunc.gotoGoodsDetail(mContext,getItem (section,position).goods_id,null,null);
                        }
                    });
                    holder.tv_btn.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            } else {
                holder.tv_saleout_flag.setText("已结束");
                holder.tv_saleout_flag.setVisibility(View.VISIBLE);
                holder.tv_btn.setBackgroundResource(R.drawable.shape_redstroke_corner);
                holder.tv_btn.setText(mContext.getString(R.string.miaosha_more));
                holder.tv_btn.setTextColor(mContext.getResources().getColor(R.color.fh_red));
                holder.tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoActivity(mContext, StoreActivity.class, getItem(section, position).store_id);
                    }
                });
                /**
                 *   结束时 如果卖光了则显示进度条
                 *   否则不显示进度条 为了数据好看
                 */
                if (getItem(section, position).isSaleOut()) {
                    holder.tv_sales_status.setVisibility(View.VISIBLE);
                    holder.pb_goods_percent.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_sales_status.setVisibility(View.INVISIBLE);
                    holder.pb_goods_percent.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            holder.tv_saleout_flag.setVisibility(View.INVISIBLE);
            holder.tv_btn.setBackgroundResource(0);
            holder.tv_btn.setTextColor(mContext.getResources().getColor(R.color.color_lv));
            holder.tv_btn.setText(mContext.getString(R.string.miaosha_coming));
            holder.tv_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.showMsg(mContext, "啊哦,活动还未开始,请稍后再来~");
                }
            });
            holder.tv_sales_status.setVisibility(View.INVISIBLE);
            holder.pb_goods_percent.setVisibility(View.INVISIBLE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCover()) return;
                BaseFunc.gotoGoodsDetail(mContext,getItem (section,position).goods_id,null,null);
            }
        });

        if (position == getCountForSection(section) - 1) {
            holder.vline.setVisibility(View.GONE);
        } else {
            holder.vline.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_header_homenav_style0, null);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_goods_img;
        TextView tv_saleout_flag;
        TextView tv_goodsname;
        TextView tv_goods_price, tv_goods_origin_price;
        TextView tv_btn;
        TextView tv_sales_status;
        ProgressBar pb_goods_percent;
        View vline;

        public ViewHolder(View itemView) {
            iv_goods_img = (ImageView) itemView.findViewById(R.id.iv_goods_img);
            tv_saleout_flag = (TextView) itemView.findViewById(R.id.tv_saleout_flag);
            tv_goodsname = (TextView) itemView.findViewById(R.id.tv_goodsname);
            tv_goods_price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tv_goods_origin_price = (TextView) itemView.findViewById(R.id.tv_goods_origin_price);
            tv_btn = (TextView) itemView.findViewById(R.id.tv_btn);
            tv_sales_status = (TextView) itemView.findViewById(R.id.tv_sales_status);
            pb_goods_percent = (ProgressBar) itemView.findViewById(R.id.pb_goods_percent);
            vline = itemView.findViewById(R.id.vline);
            itemView.setTag(this);
        }
    }
}
