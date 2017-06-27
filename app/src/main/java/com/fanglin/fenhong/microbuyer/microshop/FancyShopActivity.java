package com.fanglin.fenhong.microbuyer.microshop;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.PopUpListView;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.event.CartActionEvent;
import com.fanglin.fenhong.microbuyer.base.model.FancyShopCls;
import com.fanglin.fenhong.microbuyer.base.model.FancyShopGoods;
import com.fanglin.fenhong.microbuyer.base.model.FancyShopInfo;
import com.fanglin.fenhong.microbuyer.base.model.Favorites;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.CartActivity;
import com.fanglin.fenhong.microbuyer.microshop.adapter.FancyShopDtlGoodsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Plucky on 2015/10/13.
 * 精品微店详情页
 */
public class FancyShopActivity extends BaseFragmentActivityUI implements FancyShopInfo.FSIModelCallBack, FancyShopGoods.FSGModelCallBack, PopUpListView.PopUpListViewCallBack, Favorites.FavModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    @ViewInject(R.id.LBottom)
    LinearLayout LBottom;

    FancyShopInfo info;
    FancyShopGoods goods;

    String shop_id, mid, shop_member_id;
    FancyShopDtlGoodsAdapter adapter;


    //分类显示
    PopUpListView plv;
    @ViewInject(R.id.LClass)
    LinearLayout LClass;
    @ViewInject(R.id.tv_class)
    TextView tv_class;
    List<FancyShopCls> goods_classes;

    @ViewInject(R.id.tv_cartnum)
    TextView tv_cartnum;

    Favorites fav;
    @ViewInject(R.id.tv_collect_status)
    TextView tv_collect_status;
    @ViewInject(R.id.tv_collect)
    TextView tv_collect;
    @ViewInject(R.id.Lcollect)
    LinearLayout Lcollect;

    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_fancyshop, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        EventBus.getDefault().register(this);

        try {
            shop_id = getIntent().getStringExtra("VAL");
            if (member != null) {
                mid = member.member_id;
            }
        } catch (Exception e) {
            shop_id = null;
        }

        if (shop_id == null) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        fhApp.sendCartActionByLocal(0);

        enableTvMore(R.string.if_share, true);
        setHeadTitle(R.string.fancyshop);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0)
                    return 2;
                return 1;
            }
        });

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
        pullToRefreshRecycleView.getRefreshableView().addItemDecoration(decoration);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(layoutManager);

        adapter = new FancyShopDtlGoodsAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        BaseFunc.setFont(LBottom);

        info = new FancyShopInfo();
        goods = new FancyShopGoods();
        goods.shop_id = shop_id;
        info.setModelCallBack(this);
        goods.setModelCallBack(this);

        plv = new PopUpListView(LClass);
        plv.setCallBack(this);

        fav = new Favorites();
        fav.setModelCallBack(this);

        onPullDownToRefresh(pullToRefreshRecycleView);
    }


    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        ShareData.fhShare(this, shareData, null);
    }

    @OnClick(value = {R.id.LClass, R.id.LCart, R.id.Lcollect})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.LClass:
                plv.show();
                break;
            case R.id.LCart:
                BaseFunc.gotoActivity(this, CartActivity.class, "1");
                break;
            case R.id.Lcollect:
                doCollect();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        /** 刷新店铺*/
        info.getData(shop_id, mid);

        /** 刷新商品*/
        goods.curpage = 1;
        goods.getList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        goods.curpage++;
        goods.getList();
    }

    /**
     * 微店商品列表
     */
    @Override
    public void onFSGError(String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (goods.curpage > 1) {
            pullToRefreshRecycleView.showNoMore();
        } else {
            BaseFunc.showMsg(mContext, getString(R.string.no_data));
            adapter.setList(null);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFSGList(List<GoodsList> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (goods.curpage > 1) {
            if (list != null && list.size() > 0) {
                adapter.addList(list);
                adapter.notifyDataSetChanged();
                pullToRefreshRecycleView.onAppendData();
            } else {
                pullToRefreshRecycleView.showNoMore();
            }
        } else {
            if (list != null && list.size() > 0) {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
            }
        }
    }

    /**
     * 微店详情
     */
    @Override
    public void onFSIError(String errcode) {
    }

    @Override
    public void onFSIData(FancyShopInfo info) {
        if (info != null) {
            adapter.setInfo(info);
            adapter.notifyDataSetChanged();

            /** 设定分享格式*/
            shareData = new ShareData();
            shareData.content = getString(R.string.share_content_microshop);
            shareData.url = String.format(BaseVar.SHARE_FANCYSHOP, shop_id);
            shareData.title = info.shop_name;

            if (BaseFunc.isValidUrl(info.shop_logo)) {
                shareData.imgs = info.shop_logo;
            }

            if (!TextUtils.isEmpty(info.shop_name)) {
                setHeadTitle(info.shop_name);
            }

            shop_member_id = info.member_id;
            if (member != null) {
                if (TextUtils.equals(member.member_id, info.member_id)) {
                    Lcollect.setVisibility(View.GONE);
                }
                if (info.if_collected == 1) {
                    RefreshCollect(true, false);
                } else {
                    RefreshCollect(false, false);
                }

            } else {
                RefreshCollect(false, false);
            }
            goods_classes = info.goods_classes;
            List<String> slist = new ArrayList<>();
            slist.add(getString(R.string.all_class));
            if (goods_classes != null && goods_classes.size() > 0) {
                for (int i = 0; i < goods_classes.size(); i++) {
                    slist.add(goods_classes.get(i).wclass_name);
                }
            }
            plv.setList(slist);
        }
    }

    @Override
    public void onItemClick(int index, String txt) {
        tv_class.setText(txt);
        if (index == 0) {
            //如果是全部分类
            goods.cid = null;
        } else {
            if (goods_classes != null && (index - 1) < goods_classes.size()) {
                goods.cid = goods_classes.get(index - 1).wclass_id;
            }
        }
        goods.curpage = 1;
        goods.getList();
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    private void doCollect() {
        if (member == null) {
            BaseFunc.gotoLogin(this);
            return;
        }

        if (TextUtils.equals(shop_member_id, member.member_id)) {
            BaseFunc.showMsg(mContext, getString(R.string.tips_collect_microshop));
            return;
        }

        tv_collect_status.setSelected(!tv_collect_status.isSelected());
        if (tv_collect_status.isSelected()) {
            fav.add_favorites(mContext, member, shop_id, "microshop");
        } else {
            fav.delete_favorites(mContext, member, shop_id, "microshop");
        }
    }

    @Override
    public void onFavError(String errcode) {
        Lcollect.setEnabled(true);
        if (TextUtils.equals("0", errcode)) {
            switch (fav.actionNum) {
                case 0:
                    RefreshCollect(true, true);
                    break;
                case 1:
                    RefreshCollect(false, true);
                    break;
            }
        }
    }

    @Override
    public void onFavStart() {
        Lcollect.setEnabled(false);
    }

    @Override
    public void onFavList(List<Favorites> list) {

    }

    public void RefreshCollect(boolean isadd, boolean showmsg) {
        if (isadd) {
            tv_collect.setText(R.string.collect_already);
            tv_collect_status.setText(R.string.if_love_full);
            tv_collect_status.setSelected(true);
            if (showmsg) {
                BaseFunc.showMsg(mContext, getString(R.string.collect_already));
            }
        } else {
            tv_collect.setText(R.string.collect);
            tv_collect_status.setText(R.string.if_love_empty);
            tv_collect_status.setSelected(false);
            if (showmsg) {
                BaseFunc.showMsg(mContext, getString(R.string.uncollect_already));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handlerCartAction(CartActionEvent event) {
        if (event != null) {
            if (event.getCartAction() != null) {
                BaseFunc.setCartNumofTv(tv_cartnum, event.getCartAction().getNum());
            } else {
                BaseFunc.setCartNumofTv(tv_cartnum, 0);
            }
        }
    }
}
