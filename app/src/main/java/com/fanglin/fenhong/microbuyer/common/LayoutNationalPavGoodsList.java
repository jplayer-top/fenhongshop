package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.model.NationalPavGoodsEntity;
import com.fanglin.fenhong.microbuyer.common.adapter.NationalPavGoodsAdapter;

import java.util.ArrayList;

/**
 * 国家馆 商品列表 子布局
 * Created by lizhixin on 2015/11/30.
 */
public class LayoutNationalPavGoodsList {

    public LinearLayout llFilterHeader;//盛装 筛选头部

    private Context mContext;
    private LinearLayout view;
    private RecyclerView recyclerView;
    private NationalPavGoodsAdapter adapter;

    public LayoutNationalPavGoodsList(Context c) {
        this.mContext = c;
        view = (LinearLayout) View.inflate(mContext, R.layout.layout_son_national_pav_goods_list, null);
        llFilterHeader = (LinearLayout) view.findViewById(R.id.ll_filter_header);
        initView(view);
    }

    private void initView(ViewGroup view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        AutoGridLayoutManager layoutManager = new AutoGridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NationalPavGoodsAdapter(mContext);
        recyclerView.setAdapter(adapter);
        MutiItemDecoration decoration = new MutiItemDecoration(MutiItemDecoration.Type.ALL, 2);
        decoration.setListener(new MutiItemDecoration.DecorationListener() {
            @Override
            public void onGetOffsets(Rect outRect, int position) {
                if (outRect != null) {
                    int[] px = adapter.getOffsets(position);
                    outRect.set(px[0], px[1], px[2], px[3]);
                }
            }
        });
        recyclerView.addItemDecoration(decoration);
    }

    public void setList(ArrayList<NationalPavGoodsEntity> list, String resource_tags) {
        adapter.setList(list, resource_tags);
        adapter.notifyDataSetChanged();
    }

    public void addList(ArrayList<NationalPavGoodsEntity> list, String resource_tags) {
        recyclerView.setFocusable(false);
        adapter.addList(list, resource_tags);
        adapter.notifyDataSetChanged();
    }

    public LinearLayout getView() {
        return view;
    }

}
