package com.fanglin.fenhong.microbuyer.dutyfree;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.event.DutyfreeVipEvent;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.BaseProduct;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.HomeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.CategoryOneAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/11-下午5:46.
 * 功能描述: 一级分类 向下页面
 */
public class CategoryOneFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, HomeCategory.HomeCategoryRequestCallBack {

    private static final String KEY_CONTENT = "CategoryOneFragment:Content";
    private String mContent = "???";
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    CategoryOneAdapter adapter;

    HomeCategory categoryReq;
    int curpage = 1;

    public CategoryOneFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categoryone, container, false);
        ViewUtils.inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static CategoryOneFragment newInstance(String content) {
        CategoryOneFragment fragment = new CategoryOneFragment();
        fragment.mContent = content;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    private void initView() {
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        adapter = new CategoryOneAdapter(act);

        GridLayoutManager manager = new GridLayoutManager(act, 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanSize(position);
            }
        });

        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        categoryReq = new HomeCategory();
        categoryReq.setReqCallback(this);

        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        categoryReq.getData(member, mContent, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        categoryReq.getData(member, mContent, curpage);
    }

    @Override
    public void onHomeCategoryReqData(HomeCategory data) {
        pullToRefreshRecycleView.onRefreshComplete();
        pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
        if (data != null) {
            if (curpage == 1) {
                adapter.setData(data);
            } else {
                List<BaseProduct> products = data.getGoodsList();
                if (products != null && products.size() > 0) {
                    adapter.addProducts(products);
                    pullToRefreshRecycleView.onAppendData();
                } else {
                    pullToRefreshRecycleView.showNoMore();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleDutyfreeVipEvent(DutyfreeVipEvent event) {
        if (event != null && event.isVip()) {
            onPullDownToRefresh(pullToRefreshRecycleView);
        }
    }
}