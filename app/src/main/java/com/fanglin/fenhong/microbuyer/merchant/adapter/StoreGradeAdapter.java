package com.fanglin.fenhong.microbuyer.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.StoreGrade;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/21.
 */
public class StoreGradeAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreGrade> list = new ArrayList<> ();

    public StoreGradeAdapter(Context c) {
        this.mContext = c;
    }

    public void setList (List<StoreGrade> list) {
        this.list = list;
    }


    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView (mContext);
        }

        TextView tv = (TextView) convertView;
        tv.setTextColor (mContext.getResources ().getColor (R.color.color_33));
        tv.setTextSize (14);
        tv.setPadding (0, 20, 0, 20);
        tv.setText (getItem (position).sg_desc);
        return convertView;
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public StoreGrade getItem (int position) {
        return list.get (position);
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

}
