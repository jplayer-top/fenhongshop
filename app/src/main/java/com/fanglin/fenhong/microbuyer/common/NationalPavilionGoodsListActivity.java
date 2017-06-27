package com.fanglin.fenhong.microbuyer.common;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.NationalPavGoodsEntity;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.WSNatPavGetActivityGoods;
import com.fanglin.fenhong.microbuyer.common.adapter.NationalPavGoodsAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.ArrayList;

/**
 * 国家馆 商品列表 activity
 * Created by lizhixin on 2015/11/30.
 */
public class NationalPavilionGoodsListActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.pullToRefreshRecycleView)
    private PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.btn)
    Button btn;

    private TextView tvPriceUp, tvPriceDown;
    private LinearLayout llFilterHeader;
    private NationalPavGoodsAdapter adapter;

    private String activity_id;
    private String class_id;
    private int curpage = 1;
    private int curFilterIndex = 1;//当前查询过滤字段的顺序  默认1 综合
    private int curPriceFilter = 2;//当前查询 价格 过滤字段的顺序，up or down ， 默认 2 价格降序
    private ShareData shareData;
    private String resource_tags;//统计用


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadTitle(R.string.national_pavilion);
        enableTvMore(R.string.if_share, true);

        View view = View.inflate(this, R.layout.activity_national_pavilion_goodslist, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        activity_id = getIntent().getStringExtra("activity_id");
        class_id = getIntent().getStringExtra("class_id");
        resource_tags = getIntent().getStringExtra("resource_tags");
        String class_name = getIntent().getStringExtra("class_name");
        if (!TextUtils.isEmpty(class_name)) {
            setHeadTitle(class_name);
        }
        String strShare = getIntent().getStringExtra("share_data");
        if (!TextUtils.isEmpty(strShare)) {
            shareData = new Gson().fromJson(strShare, ShareData.class);
        }

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    private void initView() {
        tvPriceUp = (TextView) findViewById(R.id.tv_price_arrow_up);
        tvPriceDown = (TextView) findViewById(R.id.tv_price_arrow_down);
        BaseFunc.setFont(tvPriceUp);
        BaseFunc.setFont(tvPriceDown);

        llFilterHeader = (LinearLayout) findViewById(R.id.ll_filter_header);
        TextView tvFilterDefault = (TextView) findViewById(R.id.tv_filter_default);

        tvFilterDefault.setSelected(true);
    }

    private void initData() {
        adapter = new NationalPavGoodsAdapter(mContext);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(layoutManager);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
        final int[] total_y = {0};
        pullToRefreshRecycleView.getRefreshableView().setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (btn != null) {
                    total_y[0] += dy;

                    if (total_y[0] > BaseFunc.getDisplayHeight(NationalPavilionGoodsListActivity.this)) {
                        btn.setVisibility(View.VISIBLE);
                    } else {
                        btn.setVisibility(View.GONE);
                    }
                }
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

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        onPullDownToRefresh(pullToRefreshRecycleView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @OnClick({R.id.tv_filter_default, R.id.tv_filter_salesnum, R.id.tv_filter_price, R.id.tv_filter_popular, R.id.btn})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_filter_default:
                if (curFilterIndex != 1) {
                    curFilterIndex = 1;
                    changeFiltersBg(1);
                    sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, 1, curFilterIndex, curPriceFilter);
                }
                break;
            case R.id.tv_filter_salesnum:
                if (curFilterIndex != 2) {
                    curFilterIndex = 2;
                    changeFiltersBg(2);
                    sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, 1, curFilterIndex, curPriceFilter);
                }
                break;
            case R.id.tv_filter_price:
                if (curPriceFilter == 1) {
                    curPriceFilter = 2;
                } else {
                    curPriceFilter = 1;
                }
                curFilterIndex = 3;
                changeFiltersBg(3);
                sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, 1, curFilterIndex, curPriceFilter);
                break;
            case R.id.tv_filter_popular:
                if (curFilterIndex != 4) {
                    curFilterIndex = 4;
                    changeFiltersBg(4);
                    sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, 1, curFilterIndex, curPriceFilter);
                }
                break;
            case R.id.btn://浮动置顶按钮
                pullToRefreshRecycleView.getRefreshableView().smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        ShareData.fhShare(this, shareData, null);
    }

    /**
     * 发送请求获取商品列表
     */
    public void sendRequestToGetGoodsList(String activity_id, String class_id, int num, final int curpage, int sort, int order) {
        WSNatPavGetActivityGoods getActivityGoodsHandler = new WSNatPavGetActivityGoods();
        getActivityGoodsHandler.setWSGetActivityGoodsCallBack(new WSNatPavGetActivityGoods.WSGetActivityGoodsCallBack() {
            @Override
            public void onWSGetActivityGoodsError(String errcode) {
                pullToRefreshRecycleView.onRefreshComplete();
                if (curpage == 1) {
                    refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
                } else {
                    pullToRefreshRecycleView.showNoMore();
                }
            }

            @Override
            public void onWSGetActivityGoodsSuccess(final ArrayList<NationalPavGoodsEntity> activity_goods) {
                pullToRefreshRecycleView.onRefreshComplete();
                refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
                if (curpage == 1) {
                    pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
                    setList(activity_goods);
                } else {
                    addList(activity_goods);
                }
            }

        });
        getActivityGoodsHandler.getActivityGoods(activity_id, class_id, num, curpage, sort, order);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, curpage, 1, 1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, curpage, 1, 1);
    }

    /**
     * 改变查询过滤器文字的背景
     */
    private void changeFiltersBg(int index) {
        for (int i = 0; i < 4; i++) {
            llFilterHeader.getChildAt(i).setSelected(false);
        }
        llFilterHeader.getChildAt(index - 1).setSelected(true);

        //最后改变价格升降箭头的颜色
        if (index == 3) {
            tvPriceUp.setSelected(curPriceFilter == 1);
            tvPriceDown.setSelected(curPriceFilter == 2);
        } else {
            tvPriceUp.setSelected(false);
            tvPriceDown.setSelected(false);
        }
    }

    public void setList(ArrayList<NationalPavGoodsEntity> lst) {
        adapter.setList(lst, resource_tags);
        adapter.notifyDataSetChanged();
    }

    private void addList(ArrayList<NationalPavGoodsEntity> list) {
        adapter.addList(list, resource_tags);
        adapter.notifyDataSetChanged();
        pullToRefreshRecycleView.onAppendData();
    }


}
