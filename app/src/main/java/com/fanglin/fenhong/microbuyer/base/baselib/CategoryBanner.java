package com.fanglin.fenhong.microbuyer.base.baselib;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 分类按轮播的形式展示
 * Created by Plucky on 2016/11/13.
 */
public class CategoryBanner {

    private Context mContext;

    public CategoryBanner(Context context) {
        this.mContext = context;

    }

    public View getView(final List<DutyfreeCategory> categories) {
        int total = categories.size();
        int pageNum = total % 8 == 0 ? total / 8 : total / 8 + 1;

        View headerView;
        if (pageNum == 1) {
            headerView = View.inflate(mContext, R.layout.view_categories_single, null);
        } else {
            headerView = View.inflate(mContext, R.layout.view_categories, null);
        }

        final BGABanner banner = (BGABanner) headerView.findViewById(R.id.banner);
        banner.setTransitionEffect(BGABanner.TransitionEffect.Default);

        List<View> views = new ArrayList<>();
        for (int i = 0; i < pageNum; i++) {
            View view = View.inflate(mContext, R.layout.view_category_recyclerview, null);
            RecyclerView v = (RecyclerView) view.findViewById(R.id.recyclerView);
            GridLayoutManager manager = new GridLayoutManager(mContext, 4);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            v.addItemDecoration(new MutiItemDecoration(MutiItemDecoration.Type.ALL, 8));
            v.setLayoutManager(manager);

            List<DutyfreeCategory> items;
            int start = 8 * i;
            int end;
            if (i == pageNum - 1) {
                end = total;
            } else {
                end = start + 8;
            }

            items = categories.subList(start, end);

            CategoryAdapter adapter = new CategoryAdapter(mContext);
            adapter.setList(items);
            v.setAdapter(adapter);
            views.add(view);
        }

        banner.setVisibility(View.VISIBLE);
        banner.setViews(views);

        return headerView;
    }
}
