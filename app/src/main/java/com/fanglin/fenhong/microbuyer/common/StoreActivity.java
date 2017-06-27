package com.fanglin.fenhong.microbuyer.common;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.Favorites;
import com.fanglin.fenhong.microbuyer.base.model.GoodsFilter;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.StoreFlow;
import com.fanglin.fenhong.microbuyer.base.model.StoreHomeInfo;
import com.fanglin.fenhong.microbuyer.common.adapter.StoreSectionAdater;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by Plucky on 2015/10/15.
 * 商家店铺详情页
 * 第一次重构
 */
public class StoreActivity extends BaseFragmentActivity implements TextWatcher, PullToRefreshBase.OnRefreshListener2, StoreHomeInfo.SHICallBack, Favorites.FavModelCallBack, StoreFlow.StoreFlowModelCallBack, GoodsList.GoodsListModelCallBack, TextView.OnEditorActionListener {

    @ViewInject(R.id.vNewDot)
    View vNewDot;
    @ViewInject(R.id.ivMore)
    ImageView ivMore;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;
    @ViewInject(R.id.tvSearchIcon)
    TextView tvSearchIcon;
    @ViewInject(R.id.etKey)
    EditText etKey;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.vMsgDot)
    View vMsgDot;

    LayoutMoreVertical layoutMoreVertical;

    StoreSectionAdater adapter;

    String storeId;
    StoreHomeInfo homeInfoReq;
    Favorites favReq;
    StoreFlow storeFlowReq;

    GoodsList goodsListReq;
    GoodsFilter gfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ViewUtils.inject(this);
        storeId = getIntent().getStringExtra("VAL");
        initView();
    }


    private void initView() {
        tvSearchIcon.setTypeface(iconfont);
        etKey.addTextChangedListener(this);
        etKey.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etKey.setOnEditorActionListener(this);

        layoutMoreVertical = new LayoutMoreVertical(ivMore);
        layoutMoreVertical.setIsSearchShow(false);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setBackUpView(btnBackTop);

        adapter = new StoreSectionAdater(mContext);

        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        GridLayoutManager.SpanSizeLookup lookup;
        lookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanSize(position);
            }
        };
        manager.setSpanSizeLookup(lookup);

        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(manager);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
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

        homeInfoReq = new StoreHomeInfo();
        homeInfoReq.setModelCallBack(this);

        favReq = new Favorites();
        favReq.setModelCallBack(this);

        storeFlowReq = new StoreFlow();
        storeFlowReq.setCallBack(this);

        goodsListReq = new GoodsList();
        goodsListReq.setModelCallBack(this);

        gfilter = new GoodsFilter();
        gfilter.sid = storeId;
        gfilter.curpage = 1;
        gfilter.sort = 3;
        gfilter.num = BaseVar.REQUESTNUM_10;
        gfilter.order = 1;

        adapter.setSectionListener(new StoreSectionAdater.StoreSectionListener() {
            @Override
            public void onCollect(boolean hasCollect) {
                if (hasCollect) {
                    favReq.delete_favorites(mContext, member, storeId, "shop");
                } else {
                    favReq.add_favorites(mContext, member, storeId, "shop");
                }
            }
        });

        onPullDownToRefresh(pullToRefreshRecycleView);
    }


    @OnClick(value = {R.id.LGoods, R.id.LNewGoods, R.id.LClass,
            R.id.ivBack, R.id.ivMore, R.id.ivClear})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.LGoods:
                gotoStoreGoodsListActivity(null);
                break;
            case R.id.LNewGoods:
                BaseFunc.gotoStoreNewGoodsActivity(this, storeId, new Gson().toJson(layoutMoreVertical.getShareData()), layoutMoreVertical.getContactUrl());

                //点击就隐藏红点
                vNewDot.setVisibility(View.GONE);
                break;
            case R.id.LClass:
                BaseFunc.gotoStoreClassActivity(this, storeId, new Gson().toJson(layoutMoreVertical.getShareData()), layoutMoreVertical.getContactUrl());
                break;
            case R.id.ivMore:
                layoutMoreVertical.show();
                break;
            case R.id.ivClear:
                etKey.getText().clear();
                break;
        }
    }


    /**
     * 监听EditText文字变化
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etKey.length() > 0) {
            ivClear.setVisibility(View.VISIBLE);
        } else {
            ivClear.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新控件
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        gfilter.curpage = 1;
        homeInfoReq.getInfo(member, storeId);
        storeFlowReq.getStoreFlow(storeId);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        gfilter.curpage++;
        goodsListReq.get_goods(gfilter);
    }

    /**
     * 店铺信息回调
     */
    @Override
    public void onSHIData(int errorcode, StoreHomeInfo info) {
        pullToRefreshRecycleView.onRefreshComplete();
        adapter.setInfo(info);
        if (info != null) {
            layoutMoreVertical.setContactUrl(info.store_baidusales);
            ShareData shareData = new ShareData();
            shareData.title = info.share_name;
            shareData.content = info.share_content;
            shareData.imgs = info.share_images;
            shareData.url = info.share_url;
            layoutMoreVertical.setShareData(shareData);

            if (info.isShowRedDot()) {
                vNewDot.setVisibility(View.VISIBLE);
            } else {
                vNewDot.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 店铺推荐流
     */
    @Override
    public void onList(List<StoreFlow> list) {
        adapter.setList(list);
    }

    /**
     * 收藏店铺
     */
    @Override
    public void onFavError(String errcode) {
        if (TextUtils.equals("0", errcode)) {
            adapter.setHasCollected(favReq.actionNum == 0);
        }
    }

    @Override
    public void onFavList(List<Favorites> list) {

    }

    @Override
    public void onFavStart() {

    }

    @Override
    public void onGLMCList(List<GoodsList> list) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (list != null && list.size() > 0) {
            adapter.addGoods(list);
            pullToRefreshRecycleView.onAppendData();
        } else {
            pullToRefreshRecycleView.getLoadingLayoutProxy().setPullLabel(getString(R.string.pullup_error_tips));
        }
    }

    @Override
    public void onGLMCError(String errcode) {
        pullToRefreshRecycleView.onRefreshComplete();
        pullToRefreshRecycleView.getLoadingLayoutProxy().setPullLabel(getString(R.string.pullup_error_tips));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (etKey.length() > 0) {
            gotoStoreGoodsListActivity(etKey.getText().toString());
        }
        return false;
    }

    private void gotoStoreGoodsListActivity(String search) {
        String contact = layoutMoreVertical.getContactUrl(), share = new Gson().toJson(layoutMoreVertical.getShareData());
        BaseFunc.gotoStoreGoodsListActivity(this, storeId, null, contact, share, search);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            vMsgDot.setVisibility(View.VISIBLE);
            layoutMoreVertical.setMsgNum(msgnum.getTotalNum());
        } else {
            vMsgDot.setVisibility(View.GONE);
            layoutMoreVertical.setMsgNum(0);
        }
    }
}
