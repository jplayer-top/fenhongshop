package com.fanglin.fenhong.microbuyer.base.baseui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/14.
 */
public class PopUpListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> list;

    public PopUpListViewAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public String getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_popuplist, null);
            new ViewHolder (convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag ();
        holder.tv_title.setText (getItem (position));
        /*分隔线*/
        if (position == getCount () - 1) {
            holder.vline.setVisibility (View.GONE);
        } else {
            holder.vline.setVisibility (View.GONE);
        }

        /*处理异形背景*/
        if (getCount () == 1) {
            convertView.setBackgroundResource (R.drawable.fhitem_single_trail);
        } else {
            if (position == 0) {
                convertView.setBackgroundResource (R.drawable.fhitem_top);
            } else if (position == getCount () - 1) {
                convertView.setBackgroundResource (R.drawable.fhitem_bottom_trail);
            } else {
                convertView.setBackgroundResource (R.drawable.fhitem_middle);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_title;
        public View vline;

        public ViewHolder (View view) {
            tv_title = (TextView) view.findViewById (R.id.tv_title);
            vline = view.findViewById (R.id.vline);
            view.setTag (this);
        }

    }
}
