package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.model.HotBrands;
import com.fanglin.fenhong.microbuyer.common.adapter.HotBrandsAdapter;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/5.
 */
public class LayoutHotBrands implements View.OnClickListener {
    View view;
    private Context mContext;
    private RecyclerView rcv;
    private LinearLayout LTop;
    private TextView tv_icon;
    int area = 0;
    HotBrandsAdapter adapter;

    public LayoutHotBrands (Context c, int area) {
        this.mContext = c;
        this.area = area;
        view = View.inflate (mContext, R.layout.layout_hot_brands, null);
        rcv = (RecyclerView) view.findViewById (R.id.rcv);
        LTop = (LinearLayout) view.findViewById (R.id.LTop);
        tv_icon = (TextView) view.findViewById (R.id.tv_icon);
        BaseFunc.setFont (LTop);
        LTop.setOnClickListener (this);
        AutoGridLayoutManager am = new AutoGridLayoutManager (mContext, 2);
        rcv.setLayoutManager (am);
        adapter = new HotBrandsAdapter (mContext);
        rcv.setAdapter (adapter);

        if (area == 1) {
            //如果是國外 則不顯示且不能點擊
            LTop.setClickable (false);
            tv_icon.setVisibility (View.INVISIBLE);
        }
    }

    public void setList (List<HotBrands> lst) {
        adapter.setList (lst);
        adapter.notifyDataSetChanged ();
    }

    public View getView () {
        return view;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.LTop:
                BaseFunc.gotoActivity (mContext, HotBrandsActivity.class, String.valueOf (area));
                break;
        }
    }
}
