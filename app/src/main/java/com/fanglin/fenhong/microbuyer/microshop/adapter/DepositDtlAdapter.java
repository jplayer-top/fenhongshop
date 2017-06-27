package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.DepositDtl;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/1.
 */
public class DepositDtlAdapter extends BaseAdapter {

    private Context mContext;
    private List<DepositDtl> list;

    public DepositDtlAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<DepositDtl> list) {
        this.list = list;
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public DepositDtl getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_deposit_dtl, null);
            new ViewHolder (convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag ();
        BaseFunc.setFont (holder.LIcon);
        holder.tv_icon.setText (BaseFunc.getBankIconByName (mContext, getItem (position).pdc_bank_name));
        holder.tv_title.setText (getItem (position).getTitle ());
        DecimalFormat df = new DecimalFormat ("¥#0.00");
        holder.tv_subtitle.setText (df.format (getItem (position).pdc_amount));
        holder.tv_statu.setText (getItem (position).getStatus ());
        holder.tv_statu.setTextColor (getItem (position).getStatusColor (mContext));
        holder.tv_time.setText (getItem (position).pdc_payment_time);
        if (position == getCount () - 1) {
            holder.vline.setVisibility (View.GONE);
        } else {
            holder.vline.setVisibility (View.VISIBLE);
        }
        return convertView;
    }

    public class ViewHolder {
        public LinearLayout LIcon;
        public TextView tv_icon;
        public TextView tv_title;
        public TextView tv_subtitle;
        public TextView tv_statu;
        public TextView tv_time;
        public View vline;

        public ViewHolder (View view) {
            LIcon = (LinearLayout) view.findViewById (R.id.LIcon);
            tv_icon = (TextView) view.findViewById (R.id.tv_icon);
            tv_title = (TextView) view.findViewById (R.id.tv_title);
            tv_subtitle = (TextView) view.findViewById (R.id.tv_subtitle);
            tv_statu = (TextView) view.findViewById (R.id.tv_statu);
            tv_time = (TextView) view.findViewById (R.id.tv_time);
            vline = view.findViewById (R.id.vline);
            view.setTag (this);
        }
    }
}
