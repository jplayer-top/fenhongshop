package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.common.adapter.GoodsRecommendAdapter;
import com.fanglin.fhui.ListViewinScroll;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/5.
 */
public class LayoutGoodsRecommend {
    private Context mContext;
    private ViewGroup view;
    private ListViewinScroll goods_flow;
    private LinearLayout LTop;
    private GoodsRecommendAdapter adapter;

    public LayoutGoodsRecommend(Context c) {
        this.mContext = c;
        view = (ViewGroup) View.inflate(mContext, R.layout.layout_goods_recommend, null);
        goods_flow = (ListViewinScroll) view.findViewById(R.id.goods_flow);
        LTop = (LinearLayout) view.findViewById(R.id.LTop);
        BaseFunc.setFont(LTop);
        adapter = new GoodsRecommendAdapter(mContext);
        goods_flow.setAdapter(adapter);
    }

    public void setList(List<GoodsScheme> lst) {
        adapter.setList(lst);
        adapter.notifyDataSetChanged();
    }

    public void addList(List<GoodsScheme> lst) {
        if (lst != null && lst.size() > 0) {
            adapter.addList(lst);
            adapter.notifyDataSetChanged();
        }
    }

    public View getView() {
        return view;
    }
}
