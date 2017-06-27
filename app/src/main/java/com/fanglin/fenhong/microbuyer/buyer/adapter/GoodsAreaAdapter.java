package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.Area;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/22.
 */
public class GoodsAreaAdapter extends BaseAdapter {

    private List<Area> list = new ArrayList<> ();
    private Context mContext;

    public GoodsAreaAdapter (Context c) {
        mContext = c;
    }

    public void setJson (LinkedTreeMap json) {
        if (json == null || json.size () == 0) {
            list = null;
            return;
        }

        Object[] keys, values;
        try {
            keys = json.keySet ().toArray ();
            values = json.values ().toArray ();
        } catch (Exception e) {
            keys = null;
            values = null;
        }


        list = new ArrayList<> ();
        for (int i = 0; i < json.size (); i++) {
            try {
                if (keys != null) {
                    Area a = new Area ();
                    a.area_id = String.valueOf (keys[i]);
                    a.area_name = String.valueOf (values[i]);
                    list.add (a);
                }
            } catch (Exception e) {
                //
            }
        }
    }

    public List<Area> getList () {
        return list;
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public Area getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    public void setClick (int position) {
        for (int i = 0; i < list.size (); i++) {
            list.get (i).isSelected = false;
        }
        list.get (position).isSelected = true;
    }

    public Area getSelectedItem () {
        if (list != null && list.size () > 0) {
            int position = 0;
            for (int i = 0; i < list.size (); i++) {
                if (list.get (i).isSelected) {
                    position = i;
                    break;
                }
            }
            return list.get (position);
        } else {
            return null;
        }
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_goods_area, null);
            new ViewHolder (convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag ();
        holder.tv_name.setText (getItem (position).area_name);
        holder.tv_name.setSelected (getItem (position).isSelected);
        if (getItem (position).isSelected) {
            holder.tv_check.setVisibility (View.VISIBLE);
        } else {
            holder.tv_check.setVisibility (View.GONE);
        }

        BaseFunc.setFont ((ViewGroup) convertView);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_name;
        public TextView tv_check;

        public ViewHolder (View v) {
            tv_name = (TextView) v.findViewById (R.id.tv_name);
            tv_check = (TextView) v.findViewById (R.id.tv_check);
            v.setTag (this);
        }
    }
}
