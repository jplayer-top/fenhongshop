package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.RefundProcess;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-11-25.
 */
public class RefundProcessAdapter extends BaseAdapter {

    private Context mContext;
    private List<RefundProcess> list;
    private String refund_id, order_id, rec_id;

    public RefundProcessAdapter (Context c, String refund_id, String order_id, String rec_id) {
        mContext = c;
        this.refund_id = refund_id;
        this.order_id = order_id;
        this.rec_id = rec_id;
    }

    public void setList (List<RefundProcess> list) {
        this.list = list;
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public RefundProcess getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_refund_process, null);
            new ViewHolder (convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag ();
        BaseFunc.setFont (holder.tv_icon);
        holder.tv_icon.setText (mContext.getString (getItem (position).if_icon));
        holder.tv_icon.setTextColor (mContext.getResources ().getColor (getItem (position).icon_color));
        holder.tv_desc.setText (mContext.getString (getItem (position).refund_desc));

        if (getItem (position).icon_color == R.color.color_bb) {
            holder.tv_desc.setTextColor (mContext.getResources ().getColor (R.color.color_bb));
        } else {
            holder.tv_desc.setTextColor (mContext.getResources ().getColor (R.color.color_33));
        }

        if (position == 0) {
            holder.line_top.setVisibility (View.INVISIBLE);
            holder.line_bot.setVisibility (View.VISIBLE);
        } else if (position == getCount () - 1) {
            holder.line_top.setVisibility (View.VISIBLE);
            holder.line_bot.setVisibility (View.INVISIBLE);
        } else {
            holder.line_top.setVisibility (View.VISIBLE);
            holder.line_bot.setVisibility (View.VISIBLE);
        }
        holder.line_top_fix.setVisibility (holder.line_top.getVisibility ());
        convertView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getItem (position).gotoActivity (mContext, refund_id, order_id, rec_id);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public View line_top, line_top_fix;
        public TextView tv_icon;
        public View line_bot;
        public TextView tv_desc;

        public ViewHolder (View view) {
            line_top = view.findViewById (R.id.line_top);
            line_top_fix = view.findViewById (R.id.line_top_fix);
            tv_icon = (TextView) view.findViewById (R.id.tv_icon);
            line_bot = view.findViewById (R.id.line_bot);
            tv_desc = (TextView) view.findViewById (R.id.tv_desc);
            view.setTag (this);
        }
    }
}
