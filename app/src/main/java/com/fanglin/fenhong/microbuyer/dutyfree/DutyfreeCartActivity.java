package com.fanglin.fenhong.microbuyer.dutyfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.DutyfreeBO;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.event.CartPaySuccess;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCart;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartTips;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.DutyfreeCartAdapter;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartGoodsViewHolder;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhui.FHHintDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/15-上午11:29.
 * 功能描述: 极速免税店 购物车
 */
public class DutyfreeCartActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener, DutyCart.DutyCartRequestCallback, DutyCartGoodsViewHolder.DutyCartGoodsListener {

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.tvSelect)
    TextView tvSelect;
    @ViewInject(R.id.tvMoney)
    TextView tvMoney;
    @ViewInject(R.id.tvCalculate)
    TextView tvCalculate;

    @ViewInject(R.id.LActivityTips)
    LinearLayout LActivityTips;
    @ViewInject(R.id.tvActivityLabel)
    TextView tvActivityLabel;
    @ViewInject(R.id.tvActivityDesc)
    TextView tvActivityDesc;

    DutyfreeCartAdapter adapter;
    DutyCart dutyCartReq;
    String activityUrl;

    public static final int REQCHECK = 0x001;
    private FHHintDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        redTop = true;
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(mContext, R.layout.activity_dutyfree_cart, null);
        LHold.addView(rootView);
        ViewUtils.inject(this, rootView);
        EventBus.getDefault().register(this);
        dialog = new FHHintDialog(this);
        dialog.setTvLeft("返回");
        dialog.setTvRight("查看");
        dialog.setTvTitle("");
        dialog.setTvContent("商品购买成功");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setMember(member);
    }

    private void initView() {
        tvHead.setText("购物车");
        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.setOnRefreshListener(this);

        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DutyfreeCartAdapter(mContext);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                String numDesc = "结算";
                String moneyDesc = "¥0.00";
                DutyCartTips cartTips = null;
                DutyCart cart = adapter.getCart();
                if (cart != null) {
                    numDesc = "结算(" + cart.getTotalNum() + ")";
                    moneyDesc = cart.getTotalPrice();
                    cartTips = cart.getDisparity();
                }
                tvMoney.setText(moneyDesc);
                tvCalculate.setText(numDesc);
                tvSelect.setSelected(adapter.getSelectedAll());

                if (cartTips != null) {
                    LActivityTips.setVisibility(View.VISIBLE);
                    tvActivityLabel.setText(cartTips.getDisparityName());
                    tvActivityDesc.setText(cartTips.getDisparityPrice());
                    activityUrl = cartTips.getDisparityUrl();
                } else {
                    LActivityTips.setVisibility(View.GONE);
                    activityUrl = null;
                }
            }
        });
        adapter.setMember(member);
        adapter.setGoodsListener(this);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        enableTvMore(R.string.delete, false);

        dutyCartReq = new DutyCart();
        dutyCartReq.setDutyCartRequestCallback(this);
        onRefresh(pullToRefreshRecycleView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);
    }

    @OnClick(value = {R.id.tvCalculate, R.id.tvSelect, R.id.LActivityTips})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvSelect:
                onSelectAll(!tvSelect.isSelected());
                break;
            case R.id.tvCalculate:
                String cartInfo = adapter.getCartInfo();
                FHLog.d("Plucky", cartInfo);
                if (!TextUtils.isEmpty(cartInfo) && cartInfo.length() > 4) {
                    BaseFunc.dutyCartCheck(DutyfreeCartActivity.this, cartInfo, "1");
                } else {
                    BaseFunc.showMsg(mContext, "请先选择商品");
                }
                break;
            case R.id.LActivityTips:
                if (BaseFunc.isValidUrl(activityUrl)) {
                    BaseFunc.urlClick(mContext, activityUrl);
                }
                break;
        }
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        String cartIds = adapter.getSelectedCartIds();
        if (member == null || TextUtils.isEmpty(cartIds)) return;
        FHLog.d("Plucky", cartIds);
        onDelete(member, cartIds);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (member == null) return;
        dutyCartReq.getData(member);
    }

    @Override
    public void onDutyCartData(DutyCart data) {
        pullToRefreshRecycleView.onRefreshComplete();
        if (data != null) {
            refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
            adapter.setCart(data);
        } else {
            refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    private void onSelectAll(final boolean flag) {
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    //全选成功之后 重新请求接口
                    onRefresh(pullToRefreshRecycleView);
                }
            }
        }).selectAllCart(member, flag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQCHECK:
                onRefresh(pullToRefreshRecycleView);
                break;
        }
    }

    private void onDelete(Member member, String cartId) {
        new DutyfreeBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    onRefresh(pullToRefreshRecycleView);
                }
            }
        }).delCart(member, cartId);
    }

    @Override
    public void onDeleteCartGoods(DutyCartProduct product) {
        if (member == null || product == null) return;
        onDelete(member, product.getCartId());
    }

    /**
     * 余额支付成功后的处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void isSuccess(CartPaySuccess event) {
        if (event.isSuccess) {
            dialog.show();
            dialog.setHintListener(new FHHintDialog.FHHintListener() {
                @Override
                public void onLeftClick() {
                    finish();
                }

                @Override
                public void onRightClick() {
                    BaseFunc.gotoActivity(mContext, OrderListActivity.class, "0");
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
