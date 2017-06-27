package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.Category;
import com.fanglin.fenhong.microbuyer.buyer.adapter.Category2Adapter;
import com.fanglin.fenhong.microbuyer.common.LayoutMoreVertical;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/8.
 * 商品二级分类
 */
public class Category2Activity extends BaseFragmentActivityUI {

    @ViewInject(R.id.rcv)
    RecyclerView rcv;
    Category2Adapter adapter;
    Category c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(mContext, R.layout.activity_category2, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    private void initView() {
        try {
            c1 = new Gson().fromJson(getIntent().getStringExtra("VAL"), Category.class);
        } catch (Exception e) {
            c1 = null;
        }

        setHeadTitle(c1 != null ? c1.gc_name : "All");
        enableIvMore(0);

        adapter = new Category2Adapter(mContext);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        layoutManager.setSmoothScrollbarEnabled(true);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapter);

        adapter.setListener(new Category2Adapter.Category2Listener() {
            @Override
            public void onItemClick(Category c2, int position) {
                BaseFunc.gotoGoodsListActivity(Category2Activity.this,null,0,c2.gc_id);
            }
        });

        getData();
    }

    @Override
    public void onivMoreClick() {
        super.onivMoreClick();
        LayoutMoreVertical layoutMoreVertical = new LayoutMoreVertical(vBottomLine);
        layoutMoreVertical.setShareData(null);
        layoutMoreVertical.show();
    }

    private void getData() {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<Category> list = null;
                if (isSuccess) {
                    list = new Gson().fromJson(data, new TypeToken<List<Category>>() {
                    }.getType());
                }

                if (list != null && list.size() > 0) {
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
                } else {
                    refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        }).get_goods_class((c1 != null ? c1.gc_id : null), (c1 != null ? c1.gc_area : null));
    }
}
