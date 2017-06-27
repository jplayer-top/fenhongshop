package com.fanglin.fenhong.microbuyer.buyer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.Favorites;
import com.fanglin.fenhong.microbuyer.buyer.adapter.FavoritesAdapter;
import com.fanglin.fenhong.microbuyer.common.GroupActivity;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fenhong.microbuyer.microshop.FancyShopActivity;
import com.fanglin.fenhong.microbuyer.microshop.TalentActivity;
import com.fanglin.fhui.FHHintDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.List;

/**
 * 收藏 fragment基类
 * Create by lizhixin on 2016/6/6
 */
public class FavoritesCommonFragment extends BaseFragment implements Favorites.FavModelCallBack, PullToRefreshBase.OnRefreshListener2, View.OnClickListener, FavoritesAdapter.FavCallBack {

    protected LinearLayout LDel;
    protected ImageView ivCheck;
    protected TextView tv_del;
    protected PullToRefreshRecycleView recycleView;

    protected FavoritesAdapter adapter;
    public Favorites fav;

    private int curpage = 1;
    protected String cType;//类别
    protected boolean isFootprint;//是否为我的足迹

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_goods, container, false);
        recycleView = (PullToRefreshRecycleView) view.findViewWithTag("recyclerview");
        LDel = (LinearLayout) view.findViewById(R.id.LDel);
        ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
        tv_del = (TextView) view.findViewById(R.id.tv_del);
        LDel.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        initAdapter();

        fav = new Favorites();
        fav.setModelCallBack(this);

        recycleView.getRefreshableView().setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setMode(PullToRefreshBase.Mode.BOTH);
        recycleView.setScrollingWhileRefreshingEnabled(true);
        recycleView.setOnRefreshListener(this);
        if (adapter != null) {
            adapter.setCallBack(this);
            recycleView.getRefreshableView().setAdapter(adapter);
        }

        onPullDownToRefresh(recycleView);
    }

    /**
     * 留给子类去创建自己的adapter
     */
    protected void initAdapter() {
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        if (member != null) {
            if (isFootprint)
                fav.get_browse_list(getActivity(), member, curpage);
            else
                fav.get_favorites_list(getActivity(), member, cType, curpage);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        if (member != null) {
            if (isFootprint)
                fav.get_browse_list(getActivity(), member, curpage);
            else
                fav.get_favorites_list(getActivity(), member, cType, curpage);
        }
    }

    @Override
    public void onFavStart() {

    }

    @Override
    public void onFavList(List<Favorites> list) {
        recycleView.onRefreshComplete();
        if (curpage > 1) {
            if (list != null && list.size() > 0) {
                adapter.addList(list);
                adapter.notifyDataSetChanged();
                recycleView.onAppendData();
            } else {
                recycleView.showNoMore();
            }
        } else {
            adapter.setList(list);
            recycleView.getRefreshableView().getAdapter().notifyDataSetChanged();
            recycleView.resetPull(PullToRefreshBase.Mode.BOTH);
        }
    }

    @Override
    public void onFavError(String errcode) {
        switch (fav.actionNum) {
            case 2:
                recycleView.onRefreshComplete();
                if (curpage > 1) {
                    recycleView.showNoMore();
                } else {
                    adapter.setList(null);
                    recycleView.getRefreshableView().getAdapter().notifyDataSetChanged();
                }
                break;
            case 1:
                if (TextUtils.equals(errcode, "0")) {
                    onPullDownToRefresh(recycleView);
                }
                break;
        }
    }

    private void confirmDelete() {
        final FHHintDialog fhd = new FHHintDialog(getActivity());
        fhd.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                doDelete(isFootprint);
            }
        });
        fhd.setTvContent(getString(R.string.hint_delete));
        fhd.show();
    }

    private void doDelete(boolean isHistory) {
        String[] res = adapter.getSelectedIds();
        String ids = res[0];
        if (TextUtils.isEmpty(ids)) {
            BaseFunc.showMsg(getActivity(), getString(R.string.please_select_one));
        } else {

            if (isHistory) {
                if (member == null) {
                    BaseFunc.gotoLogin(getActivity());
                    return;
                }
                new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
                    @Override
                    public void onStart(String data) {

                    }

                    @Override
                    public void onEnd(boolean isSuccess, String data) {
                        if (isSuccess) {
                            onPullDownToRefresh(recycleView);
                        }
                    }
                }).delete_browse(member.token, member.member_id, "0", ids);
            } else {
                fav.delete_favorites(getActivity(), member, ids, cType);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LDel:
                ivCheck.setSelected(!ivCheck.isSelected());
                adapter.setSelected(ivCheck.isSelected());
                break;
            case R.id.tv_del:
                confirmDelete();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int type, int index) {
        switch (type) {
            case 0:
                BaseFunc.gotoGoodsDetail(act, adapter.getItem(index).goods_id, null, null);
                break;
            case 1:
                String store_id = adapter.getItem(index).store_id;
                if (store_id == null) return;
                BaseFunc.gotoActivity(getActivity(), StoreActivity.class, store_id);
                break;
            case 2:
                BaseFunc.gotoActivity(getActivity(), FancyShopActivity.class, adapter.getItem(index).shop_id);
                break;
            case 3:
                BaseFunc.gotoGoodsDetail(act, adapter.getItem(index).goods_id, null, null);
                break;
            case 4://收藏时光
                String imageId = adapter.getItem(index).time_id;
                BaseFunc.gotoTalentImageDetail(act, imageId, false);
                break;
            case 5://收藏达人
                String talentId = adapter.getItem(index).talent_id;
                BaseFunc.gotoActivity(act, TalentActivity.class, talentId);
                break;
            case 6:
                String brand_id = adapter.getItem(index).brand_id;
                BaseFunc.gotoActivity(act, GroupActivity.class, brand_id);
                break;
        }
    }

}
