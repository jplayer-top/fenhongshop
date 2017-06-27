package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.GoodsCommentData;
import com.fanglin.fenhong.microbuyer.base.model.GoodsComments;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsCommentsAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by Plucky on 2015/9/11.
 * 商品评价列表
 */
public class GoodsRecommentsActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.LDoing)
    LinearLayout LDoing;

    @ViewInject(R.id.rating)
    private RatingBar rating;
    @ViewInject(R.id.tvAll)
    private TextView tvAll;
    @ViewInject(R.id.tvGood)
    private TextView tvGood;
    @ViewInject(R.id.tvAverage)
    private TextView tvAverage;
    @ViewInject(R.id.tvBad)
    private TextView tvBad;
    @ViewInject(R.id.tvImgs)
    private TextView tvImgs;

    @ViewInject(R.id.pullToRefreshRecycleView)
    private PullToRefreshRecycleView pullToRefreshRecycleView;
    private GoodsCommentsAdapter adapter;
    String goods_id, type;
    int curpage = 1;

    @OnClick(value = {R.id.tvAll, R.id.tvGood, R.id.tvAverage,
            R.id.tvBad, R.id.tvImgs})
    public void onViewClick(View v) {
        setFalse();
        v.setSelected(true);
        curpage = 1;
        switch (v.getId()) {
            case R.id.tvAll:
                type = null;
                break;
            case R.id.tvGood:
                type = "1";
                break;
            case R.id.tvAverage:
                type = "2";
                break;
            case R.id.tvBad:
                type = "3";
                break;
            case R.id.tvImgs:
                type = "4";
                break;
        }
        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_goods_recomments, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        setHeadTitle(R.string.evaluate);

        goods_id = getIntent().getStringExtra("VAL");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(layoutManager);
        adapter = new GoodsCommentsAdapter(mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);


        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        setFalse();
        tvAll.setSelected(true);

        LDoing.setVisibility(View.VISIBLE);
        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    private void getData() {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                LDoing.setVisibility(View.GONE);
                GoodsCommentData goodsCommentData;
                if (isSuccess) {
                    try {
                        goodsCommentData = new Gson().fromJson(data, GoodsCommentData.class);
                    } catch (Exception e) {
                        goodsCommentData = null;
                    }
                } else {
                    goodsCommentData = null;
                }
                RefreshView(goodsCommentData);
            }
        }).get_goods_comments(goods_id, curpage, type);
    }

    private void RefreshView(GoodsCommentData data) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (data == null) {
            return;
        }
        tvGood.setText(data.getGood_Count());
        tvBad.setText(data.getBad_count());
        tvAll.setText(data.getAll_count());
        tvAverage.setText(data.getNormal_count());
        tvImgs.setText(data.getHasimage_count());
        rating.setRating(data.star_average);
        final List<GoodsComments> comments = data.comments;
        if (curpage > 1) {
            if (comments != null && comments.size() > 0) {
                adapter.addList(comments);
                adapter.notifyDataSetChanged();
                pullToRefreshRecycleView.onAppendData();
            } else {
                pullToRefreshRecycleView.showNoMore();
            }
        } else {
            adapter.setList(comments);
            adapter.notifyDataSetChanged();
            pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
        }
    }

    private void setFalse() {
        tvAll.setSelected(false);
        tvGood.setSelected(false);
        tvAverage.setSelected(false);
        tvBad.setSelected(false);
        tvImgs.setSelected(false);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        getData();
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }
}
