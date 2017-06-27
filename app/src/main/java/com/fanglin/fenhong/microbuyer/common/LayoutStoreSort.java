package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fhlib.other.FHLib;

/**
 * 作者： Created by Plucky on 2015/12/10.
 */
public class LayoutStoreSort implements View.OnClickListener {

    private View view;

    LinearLayout LGrp, LHome, LAll, LNew;

    LinearLayout LBotStikky, LSort;

    LinearLayout Lprice;
    TextView tv_default, tv_sales, tv_up, tv_down, tv_popular;

    TextView tv_all, tv_last;//全部 + 上新

    private int index;


    public LayoutStoreSort (Context c) {
        view = View.inflate (c, R.layout.layout_store_sort, null);
        LGrp = (LinearLayout) view.findViewById (R.id.LGrp);
        LHome = (LinearLayout) view.findViewById (R.id.LHome);
        LAll = (LinearLayout) view.findViewById (R.id.LAll);
        LNew = (LinearLayout) view.findViewById (R.id.LNew);

        LBotStikky = (LinearLayout) view.findViewById (R.id.LBotStikky);
        LSort = (LinearLayout) view.findViewById (R.id.LSort);

        Lprice = (LinearLayout) view.findViewById (R.id.Lprice);
        tv_default = (TextView) view.findViewById (R.id.tv_default);
        tv_sales = (TextView) view.findViewById (R.id.tv_sales);
        tv_up = (TextView) view.findViewById (R.id.tv_up);
        tv_down = (TextView) view.findViewById (R.id.tv_down);
        tv_popular = (TextView) view.findViewById (R.id.tv_popular);

        tv_all = (TextView) view.findViewById (R.id.tv_all);
        tv_last = (TextView) view.findViewById (R.id.tv_last);

        BaseFunc.setFont (LHome);
        BaseFunc.setFont (tv_up);
        BaseFunc.setFont (tv_down);

        LHome.setOnClickListener (this);
        LAll.setOnClickListener (this);
        LNew.setOnClickListener (this);
        tv_default.setOnClickListener (this);
        tv_sales.setOnClickListener (this);
        Lprice.setOnClickListener (this);
        tv_popular.setOnClickListener (this);

        LHome.setSelected (true);
        tv_default.setSelected (true);
        LSort.setVisibility (View.GONE);
    }

    public void setData (int all, int last) {
        tv_all.setText (all + "");
        tv_last.setText (last + "");
    }

    public void setClickEnable (boolean flag) {
        FHLib.EnableViewGroup (LGrp, flag);
        FHLib.EnableViewGroup (LSort, flag);
    }

    public View getView () {
        return view;
    }

    public interface SortCallBack {
        void onGrpChange (int grp);

        void onSortChange (int grp, int sort, int order);
    }

    private SortCallBack mcb;

    public void setCallBack (SortCallBack cb) {
        this.mcb = cb;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.LHome:
                changeGrp (0);
                break;
            case R.id.LAll:
                changeGrp (1);
                break;
            case R.id.LNew:
                changeGrp (2);
                break;
            case R.id.tv_default:
                changeSort (0);
                break;
            case R.id.tv_sales:
                changeSort (1);
                break;
            case R.id.Lprice:
                changeSort (2);
                break;
            case R.id.tv_popular:
                changeSort (3);
                break;
        }
    }

    /* 改变Grp 状态 */
    private void changeGrp (int grp_index) {
        this.index = grp_index;
        for (int i = 0; i < 3; i++) {
            LGrp.getChildAt (i).setSelected (false);
        }
        LGrp.getChildAt (grp_index).setSelected (true);
        switch (grp_index) {
            case 0:
                LSort.setVisibility (View.GONE);
                break;
            case 1:
                LSort.setVisibility (View.VISIBLE);
                break;
            case 2:
                LSort.setVisibility (View.GONE);
                changeSort (0);
                break;
        }

        if (mcb != null) {
            mcb.onGrpChange (grp_index);
        }
    }

    int order = 1;

    /* 改变排序状态 */
    private void changeSort (int sort_index) {
        tv_down.setSelected (false);
        tv_up.setSelected (false);
        for (int i = 0; i < 4; i++) {
            LSort.getChildAt (i).setSelected (false);
        }
        LSort.getChildAt (sort_index).setSelected (true);

        int sort = 3; //order (1为升序，2为降序）
        switch (sort_index) {
            case 0:
                sort = 3;//3为综合推荐
                order = 1;
                break;
            case 1:
                sort = 1;//1为销量
                order = 1;
                break;
            case 2:
                sort = 2;//2为商品价格
                order = order == 2 ? 1 : 2;
                break;
            case 3:
                sort = 4;//4为人气
                order = 1;
                break;
        }
        if (sort_index == 2) {
            if (order == 1) {
                tv_up.setSelected (true);
                tv_down.setSelected (false);
            } else {
                tv_down.setSelected (true);
                tv_up.setSelected (false);
            }
        }

        if (mcb != null) {
            mcb.onSortChange (index, sort, order);
        }
    }
}
