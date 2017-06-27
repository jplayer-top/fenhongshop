package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.Points;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/29.
 */
public class PointsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Points> list;

    public PointsAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<Points> list) {
        this.list = list;
    }

    public void addList (List<Points> list) {
        if (list != null && list.size () > 0) {
            this.list.addAll (list);
        }
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public Points getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_points, null);
            new ViewHolder (convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag ();
        holder.tv_desc.setText (getItem (position).pl_desc);
        if (getItem (position).pl_points > 0) {
            holder.tv_points.setText ("+" + getItem (position).pl_points);
        } else {
            holder.tv_points.setText ("" + getItem (position).pl_points);
        }

        holder.tv_atime.setText (getItem (position).pl_addtime);

        if (position == getCount () - 1) {
            holder.vline.setVisibility (View.GONE);
        } else {
            holder.vline.setVisibility (View.VISIBLE);
        }
        return convertView;
    }

    public class ViewHolder {

        public TextView tv_desc;
        public TextView tv_points;
        public TextView tv_atime;
        public View vline;

        public ViewHolder (View v) {
            tv_desc = (TextView) v.findViewById (R.id.tv_desc);
            tv_points = (TextView) v.findViewById (R.id.tv_points);
            tv_atime = (TextView) v.findViewById (R.id.tv_atime);
            vline = v.findViewById (R.id.vline);

            v.setTag (this);
        }
    }
}
