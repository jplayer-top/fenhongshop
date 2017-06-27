package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.ActivityList;
import com.fanglin.fenhong.microbuyer.common.adapter.ActListAdapter;
import com.fanglin.fhui.ListViewinScroll;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/8.
 */
public class LayoutActList implements View.OnClickListener {

    private Context mContext;
    private ViewGroup view;
    private LinearLayout LTop;
    private TextView tv_icon;
    private ListViewinScroll mflow;
    ActListAdapter adapter;
    int area = 0;

    public LayoutActList (Context c, int area) {
        this.mContext = c;
        this.area = area;
        view = (ViewGroup) View.inflate (mContext, R.layout.layout_actlist, null);
        LTop = (LinearLayout) view.findViewById (R.id.LTop);
        tv_icon = (TextView) view.findViewById (R.id.tv_icon);
        mflow = (ListViewinScroll) view.findViewById (R.id.mflow);
        BaseFunc.setFont (tv_icon);
        adapter = new ActListAdapter (mContext);
        mflow.setAdapter (adapter);
        LTop.setOnClickListener (this);
        if (area == 1) {
            //如果是國外 則不顯示且不能點擊
            LTop.setClickable (false);
            tv_icon.setVisibility (View.INVISIBLE);
        }
    }

    public void setList (List<ActivityList> list) {
        adapter.setList (list);
        adapter.notifyDataSetChanged ();
        if (list == null || list.size () == 0) {
            view.setVisibility (View.GONE);
        }
    }

    public View getView () {
        return view;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.LTop:
                BaseFunc.gotoActivity (mContext, ActListActivity.class, String.valueOf (area));
                break;
        }
    }
}
