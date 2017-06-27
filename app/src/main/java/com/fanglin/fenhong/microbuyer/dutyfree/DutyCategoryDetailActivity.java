package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCategoryDetail;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyCategoryDetailAdapter;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.FloorViewHolderListener;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.FloorViewHolder;
import com.fanglin.fhlib.other.FHLog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午6:06.
 * 功能描述: 极速免税店 分类详情页
 */
public class DutyCategoryDetailActivity extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener, DutyCategoryDetail.DutyCategoryDetailRequestCallBack, FloorViewHolderListener {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.LFloor)
    LinearLayout LFloor;

    GridLayoutManager manager;
    DutyCategoryDetailAdapter adapter;
    FloorViewHolder holder;
    DutyCategoryDetail categoryDetail;
    String id;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutyfree_category_detail);
        ViewUtils.inject(this);
        id = getIntent().getStringExtra("VAL");
        initView();
    }

    private void initView() {
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        View view = FloorViewHolder.getView(mContext);
        holder = FloorViewHolder.getHolderByView(view);
        holder.setFloorListener(this);
        holder.setType(0);
        LFloor.addView(view);

        height = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_of_42);

        adapter = new DutyCategoryDetailAdapter(mContext);
        adapter.setFloorListener(this);
        manager = new GridLayoutManager(mContext, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanSize(position);
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                holder.refreshView(adapter.getCategoryList());
            }
        });
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);

        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
        pullToRefreshRecycleView.getRefreshableView().addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //向下滑动去最后一个 向上滑动去第一个
                int position = manager.findLastVisibleItemPosition();
                if (position > 9) {
                    LFloor.setVisibility(View.VISIBLE);
                } else {
                    LFloor.setVisibility(View.GONE);
                }
            }
        });

        categoryDetail = new DutyCategoryDetail();
        categoryDetail.setCategoryDetailRequestCallBack(this);
        onRefresh(pullToRefreshRecycleView);
    }

    @OnClick(value = {R.id.ivBack, R.id.tvSearch})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvSearch:
                BaseFunc.gotoActivity(mContext, DutyfreeSearchActivity.class, null);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        categoryDetail.getList(member, id);
    }

    @Override
    public void onDutyCategoryDetailList(DutyCategoryDetail detail) {
        pullToRefreshRecycleView.onRefreshComplete();
        adapter.setDetail(detail);
    }

    @Override
    public void onFloorClick(int position) {
        adapter.setIndex(position);
        holder.changePositon(position);
        int pos = adapter.getPositionOfSection(position);
        FHLog.d("Plucky", "pos:" + pos);
        manager.scrollToPositionWithOffset(pos, height);
    }
}
