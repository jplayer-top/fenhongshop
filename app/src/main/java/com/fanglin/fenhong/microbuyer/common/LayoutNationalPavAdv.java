package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.model.NationalPavAdvEntity;
import com.fanglin.fenhong.microbuyer.common.adapter.NationalPavAdvAdapter;

import java.util.ArrayList;

/**
 * 国家馆 广告 子模块
 * Created by lizhixin on 2015/11/30.
 */
public class LayoutNationalPavAdv {
    private Context mContext;
    private ViewGroup view;
    private RecyclerView recyclerView;
    private NationalPavAdvAdapter adapter;

    public LayoutNationalPavAdv(Context c) {
        this.mContext = c;
        view = (ViewGroup) View.inflate(mContext, R.layout.layout_son_national_pav_cls_and_adv, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        adapter = new NationalPavAdvAdapter(mContext);

        AutoGridLayoutManager layoutManager = new AutoGridLayoutManager(mContext, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setList(ArrayList<NationalPavAdvEntity> lst) {
        if (lst != null) {
            adapter.setList(lst);
            adapter.notifyDataSetChanged();
        }
    }

    public View getView() {
        return view;
    }
}
