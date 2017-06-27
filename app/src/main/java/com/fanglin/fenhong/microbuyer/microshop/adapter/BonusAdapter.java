package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.Bonus;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Plucky on 2015/11/28.
 * 优惠券列表
 */
public class BonusAdapter extends BaseAdapter {

    private List<Bonus> list;
    private Context mContext;
    public int actionid = -1;//0 可使用 1 不可使用
    public boolean isred = false;


    public BonusAdapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<Bonus> list) {
        this.list = list;
    }

    public void addList(List<Bonus> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public Bonus getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_bonus, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.cb.setVisibility(View.GONE);
        holder.tv_price.setText(String.valueOf(getItem(position).coupon_price));
        holder.tv_title.setText(getItem(position).coupon_name);
        holder.tv_subtitle.setText(getItem(position).getUseDesc());
        BaseFunc.setFont(holder.tv_icon);
        if (actionid > -1) {
            if (actionid == 0) {
                holder.LBG.setBackgroundResource(R.drawable.bg_item_coupon);
                holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.fh_red));
                holder.tv_icon.setVisibility(View.VISIBLE);
                holder.ivSpliter.setImageResource(R.drawable.red_spliter_coupons);
            } else {
                holder.LBG.setBackgroundResource(R.drawable.bg_item_coupon_gray);
                holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.color_33));
                holder.tv_icon.setVisibility(View.INVISIBLE);
                holder.ivSpliter.setImageResource(R.drawable.grag_spliter_coupons);
            }
        } else {
            if (isred) {
                holder.LBG.setBackgroundResource(R.drawable.bg_item_coupon);
                holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.fh_red));
                holder.tv_icon.setVisibility(View.VISIBLE);
                holder.ivSpliter.setImageResource(R.drawable.red_spliter_coupons);
            } else {
                holder.LBG.setBackgroundResource(R.drawable.bg_item_coupon_gray);
                holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.color_33));
                holder.tv_icon.setVisibility(View.INVISIBLE);
                holder.ivSpliter.setImageResource(R.drawable.grag_spliter_coupons);
            }
        }


        holder.tv_desc.setText(getItem(position).getStoreDesc());
        holder.tv_time.setText(getItem(position).getDeadlineTime());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionid > -1) {
                    if (actionid == 0) {
                        Activity act = (Activity) mContext;
                        if (act != null) {
                            Intent intent = new Intent();
                            intent.putExtra("VAL", new Gson().toJson(getItem(position)));
                            act.setResult(Activity.RESULT_OK, intent);
                            act.finish();
                        }
                    }
                } else {
                    if (isred) {
                        BaseFunc.gotoCouponGoodsPage(mContext, getItem(position).store_id, null);
                    }
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {

        public CheckBox cb;
        public TextView tv_title;
        public TextView tv_subtitle;
        public TextView tv_price_mark, tv_price;
        public TextView tv_icon;
        public TextView tv_desc;
        public TextView tv_time;
        public LinearLayout LBG;
        ImageView ivSpliter;

        public ViewHolder(View view) {
            cb = (CheckBox) view.findViewById(R.id.cb);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
            tv_price_mark = (TextView) view.findViewById(R.id.tv_price_mark);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_icon = (TextView) view.findViewById(R.id.tv_icon);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            LBG = (LinearLayout) view.findViewById(R.id.LBG);
            ivSpliter = (ImageView) view.findViewById(R.id.ivSpliter);
            view.setTag(this);
        }
    }
}
