package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.common.adapter.GoodsFancyAdapter;
import com.fanglin.fhui.ListViewinScroll;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/5.
 * 精选推荐
 */
public class LayoutFancy implements View.OnClickListener {
    View view;
    private Context mContext;
    private ListViewinScroll mflow;
    private LinearLayout LTop;
    private TextView tv_icon;
    private GoodsFancyAdapter adapter;
    int area = 0;

    public LayoutFancy (Context c, int area) {
        this.mContext = c;
        this.area = area;
        view = View.inflate (mContext, R.layout.layout_fancy, null);
        mflow = (ListViewinScroll) view.findViewById (R.id.mflow);
        LTop = (LinearLayout) view.findViewById (R.id.LTop);
        tv_icon = (TextView) view.findViewById (R.id.tv_icon);
        BaseFunc.setFont (LTop);
        LTop.setOnClickListener (this);
        adapter = new GoodsFancyAdapter (mContext);
        mflow.setAdapter (adapter);

        /*暂不区分国内外*/
        //if (area == 1) {
        //如果是國外 則不顯示且不能點擊
        //LTop.setClickable (false);
        //tv_icon.setVisibility (View.INVISIBLE);
        //}
    }

    public void setList (List<GoodsScheme> lst) {
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
                BaseFunc.gotoActivity (mContext, FancyListActivity.class, String.valueOf (area));
                break;
        }
    }
}
