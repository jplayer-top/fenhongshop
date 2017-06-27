package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.Express100;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-27.
 */
public class ExpressAdapter extends BaseAdapter {

    private Context mContext;
    private List<Express100.ExpData> list;

    public ExpressAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<Express100.ExpData> list) {
        this.list = list;
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_express100, null);
            new ViewHolder (convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag ();
        if (position == 0) {
            holder.vline_top.setVisibility (View.INVISIBLE);
            holder.vdot.setSelected (true);
            holder.tv_title.setTextColor (mContext.getResources ().getColor (R.color.color_lv));
            holder.tv_subtitle.setTextColor (mContext.getResources ().getColor (R.color.color_lv));
        } else {
            holder.vline_top.setVisibility (View.VISIBLE);
            holder.vdot.setSelected (false);
            holder.tv_title.setTextColor (mContext.getResources ().getColor (R.color.color_99));
            holder.tv_subtitle.setTextColor (mContext.getResources ().getColor (R.color.color_99));
        }

        holder.tv_title.setText (getItem (position).context);
        holder.tv_subtitle.setText (getItem (position).time);

        if (position == getCount () - 1) {
            holder.vline_bot.setVisibility (View.GONE);
        } else {
            holder.vline_bot.setVisibility (View.VISIBLE);
        }
        return convertView;
    }


    public Express100.ExpData getItem (int position) {
        return list.get (position);
    }


    @Override
    public long getItemId (int position) {
        return position;
    }

    class ViewHolder {
        public View vline_top;
        public View vdot;
        public TextView tv_title;
        public TextView tv_subtitle;
        public View vline_bot;

        public ViewHolder (View itemView) {
            vline_top = itemView.findViewById (R.id.vline_top);
            vdot = itemView.findViewById (R.id.vdot);
            tv_title = (TextView) itemView.findViewById (R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById (R.id.tv_subtitle);
            vline_bot = itemView.findViewById (R.id.vline_bot);

            itemView.setTag (this);
        }
    }
}
