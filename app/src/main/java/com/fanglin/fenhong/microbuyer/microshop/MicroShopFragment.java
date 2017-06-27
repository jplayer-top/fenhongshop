package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedSectionListView;
import com.fanglin.fenhong.microbuyer.base.event.MicroshopInfoEvent;
import com.fanglin.fenhong.microbuyer.base.model.ShopGoods;
import com.fanglin.fenhong.microbuyer.microshop.adapter.PinnedMicroshopAdapter;
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
 * author:Created by Plucky on 16/6/3-下午1:30.
 * 功能描述: 我的微店
 */
public class MicroShopFragment extends BaseFragment implements ShopGoods.SGModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    private static final String KEY_CONTENT = "MicroShopFragment:Content";
    private String mContent = "???";

    @ViewInject(R.id.pullToRefreshPinnedSectionListView)
    PullToRefreshPinnedSectionListView pullToRefreshPinnedSectionListView;
    private PinnedMicroshopAdapter adapter;
    int curpage = 1;
    ShopGoods shopGoodsRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册EventBus
        EventBus.getDefault().register(this);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microshop_shop, container, false);
        ViewUtils.inject(this, view);
        initView();
        return view;
    }

    private void initView() {
        pullToRefreshPinnedSectionListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshPinnedSectionListView.setOnRefreshListener(this);
        pullToRefreshPinnedSectionListView.setScrollingWhileRefreshingEnabled(true);

        adapter = new PinnedMicroshopAdapter(act);
        pullToRefreshPinnedSectionListView.getRefreshableView().setAdapter(adapter);
        shopGoodsRequest = new ShopGoods();
        shopGoodsRequest.setModelCallBack(this);

        onPullDownToRefresh(pullToRefreshPinnedSectionListView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    public static MicroShopFragment newInstance(String content) {
        MicroShopFragment fragment = new MicroShopFragment();
        fragment.mContent = content;
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 数据请求
     */
    @Override
    public void onSGList(List<ShopGoods> goodsList) {
        pullToRefreshPinnedSectionListView.onRefreshComplete();
        if (curpage == 1) {
            adapter.setList(goodsList);
            pullToRefreshPinnedSectionListView.resetPull(PullToRefreshBase.Mode.BOTH);
        } else {
            if (goodsList != null && goodsList.size() > 0) {
                adapter.addList(goodsList);
                pullToRefreshPinnedSectionListView.onAppendData(200);
            } else {
                pullToRefreshPinnedSectionListView.showNoMore();
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        /**
         * 获取微店商品
         */
        curpage = 1;
        shopGoodsRequest.getList(member_id, curpage);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        shopGoodsRequest.getList(member_id, curpage);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleMicroshopInfo(MicroshopInfoEvent event) {
        if (event != null) {
            adapter.setMicroShop(event.getMicroshopInfo());
            adapter.notifyDataSetChanged();
        }
    }
}
