package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.event.LinkGoods4ImageTagEvent;
import com.fanglin.fenhong.microbuyer.base.event.LinkGoodsEvent;
import com.fanglin.fenhong.microbuyer.base.model.LinkGoods;
import com.fanglin.fenhong.microbuyer.microshop.adapter.LinkGoodsAdapter;
import com.fanglin.fhui.FHHintDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/16-上午9:06.
 * 功能描述: 时光关联商品
 */
public class LinkGoodsActivity extends BaseFragmentActivity implements TextWatcher, TextView.OnEditorActionListener, PullToRefreshBase.OnRefreshListener2, LinkGoods.LinkGoodsModelCallBack, LinkGoodsAdapter.LinkGoodsAdapterListener {

    @ViewInject(R.id.etSearch)
    EditText etSearch;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;
    @ViewInject(R.id.tvBuyed)
    TextView tvBuyed;
    @ViewInject(R.id.tvCarted)
    TextView tvCarted;
    @ViewInject(R.id.tvFocused)
    TextView tvFocused;
    @ViewInject(R.id.tvExplorered)
    TextView tvExplorered;

    @ViewInject(R.id.vSpliterLine)
    View vSpliterLine;
    @ViewInject(R.id.LActiton)
    LinearLayout LActiton;
    @ViewInject(R.id.LBottom)
    LinearLayout LBottom;
    @ViewInject(R.id.multiStateView)
    MultiStateView multiStateView;

    @ViewInject(R.id.tvLinked)
    TextView tvLinked;

    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;
    LinkGoodsAdapter adapter;
    private int curpage = 1;
    private String curKey;//当前搜索的关键字
    private String curType;

    public static final String FROMIMAGE = "imageTagLinkGoods";
    private boolean isFromImageTag = false;
    LinkGoods linkGoodsRequest;

    List<LinkGoods> tmpBind = new ArrayList<>();
    List<LinkGoods> tmpUnBind = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkgoods);
        ViewUtils.inject(this);
        isFromImageTag = TextUtils.equals(FROMIMAGE, getIntent().getStringExtra("VAL"));
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            finish();
        }
        adapter.notifyDataSetChanged();
        int all = LinkGoodsEvent.getLocalCount();
        tvLinked.setText(LinkGoodsEvent.formatLinkedStr(all, 10));
    }

    @OnClick(value = {R.id.ivBack, R.id.ivClear,
            R.id.tvBuyed, R.id.tvCarted, R.id.tvFocused, R.id.tvExplorered,
            R.id.LBottom, R.id.btnLink})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                confirmExit();
                break;
            case R.id.ivClear:
                etSearch.getText().clear();
                break;
            case R.id.tvBuyed:
                refreshFilterIndex(0);
                curpage = 1;
                getGoods();
                break;
            case R.id.tvCarted:
                refreshFilterIndex(1);
                curpage = 1;
                getGoods();
                break;
            case R.id.tvFocused:
                refreshFilterIndex(2);
                curpage = 1;
                getGoods();
                break;
            case R.id.tvExplorered:
                refreshFilterIndex(3);
                curpage = 1;
                getGoods();
                break;
            case R.id.btnLink:
                finish();
                break;
            case R.id.LBottom:
                BaseFunc.gotoActivity(mContext, LinkedGoodsActivity.class, null);
                break;
        }
    }

    private void initView() {
        if (isFromImageTag) {
            LBottom.setVisibility(View.GONE);
        } else {
            LBottom.setVisibility(View.VISIBLE);
        }
        adapter = new LinkGoodsAdapter(mContext);
        adapter.setListener(this);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setShowIndicator(false);
        pullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setAdapter(adapter);

        linkGoodsRequest = new LinkGoods();
        linkGoodsRequest.setModelCall(this);

        refreshFilterIndex(0);
        curpage = 1;
        getGoods();
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        etSearch.addTextChangedListener(this);
        etSearch.setOnEditorActionListener(this);
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    private void refreshFilterIndex(int index) {
        tvBuyed.setSelected(index == 0);
        tvCarted.setSelected(index == 1);
        tvFocused.setSelected(index == 2);
        tvExplorered.setSelected(index == 3);
        switch (index) {
            case 0:
                curType = "bought";
                break;
            case 1:
                curType = "cart";
                break;
            case 2:
                curType = "collected";
                break;
            case 3:
                curType = "browsed";
                break;
        }
    }

    /**
     * etSearch 事件监听
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        curKey = etSearch.getText().toString();
        if (!TextUtils.isEmpty(curKey)) {
            vSpliterLine.setVisibility(View.GONE);
            LActiton.setVisibility(View.GONE);
            curpage = 1;
            getGoods();
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        }

        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etSearch.length() > 0) {
            ivClear.setVisibility(View.VISIBLE);
        } else {
            ivClear.setVisibility(View.GONE);
            LActiton.setVisibility(View.VISIBLE);
            vSpliterLine.setVisibility(View.VISIBLE);

            curKey = null;
            curpage = 1;
            getGoods();
        }
    }

    private void getGoods() {
        /**
         * key和curType互斥
         * 即 搜索和切换标签互斥
         * 搜索时不用传curType
         */
        String tmp = TextUtils.isEmpty(curKey) ? curType : null;
        linkGoodsRequest.getList(member, curKey, tmp, null, curpage);
    }

    @Override
    public void onLinkGoodsList(List<LinkGoods> goodsList) {
        pullToRefreshListView.onRefreshComplete();
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        if (curpage == 1) {
            pullToRefreshListView.resetPull(PullToRefreshBase.Mode.BOTH);
            adapter.setGoodsList(goodsList);
        } else {
            if (goodsList != null && goodsList.size() > 0) {
                adapter.addGoodsList(goodsList);
            } else {
                pullToRefreshListView.showNoMore();
            }
        }

    }

    /**
     * 不管是切换标签或者搜索均执行刷新和上拉加载的动作
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        curpage = 1;
        getGoods();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        getGoods();
    }

    @Override
    public void onBound(int position) {
        final LinkGoods aGoods = adapter.getItem(position);
        final int all = LinkGoodsEvent.getLocalCount();
        if (all == 10) {
            BaseFunc.showMsg(mContext, getString(R.string.hint_overlinklimit));
            return;
        }

        if (isFromImageTag) {
            LayoutInputDialog layoutInputDialog = new LayoutInputDialog(mContext);
            layoutInputDialog.setListener(new LayoutInputDialog.LayoutInputDialogListener() {
                @Override
                public void onData(String content) {
                    aGoods.setTime_image_id_local(FROMIMAGE);
                    aGoods.setShortName(content);
                    EventBus.getDefault().post(new LinkGoods4ImageTagEvent(true, aGoods));
                    adapter.notifyDataSetChanged();
                    tvLinked.setText(LinkGoodsEvent.formatLinkedStr(all + 1, 10));
                    finish();
                }

                @Override
                public void onBeforeDismiss(EditText editText) {
                    BaseFunc.hideSoftInput(mContext,editText);
                }
            });
            layoutInputDialog.show();
        } else {
            EventBus.getDefault().post(new LinkGoodsEvent(true, aGoods));
            tmpBind.add(aGoods);
            adapter.notifyDataSetChanged();
            tvLinked.setText(LinkGoodsEvent.formatLinkedStr(all + 1, 10));
        }
    }

    @Override
    public void onUnBound(int position) {
        LinkGoods aGoods = adapter.getItem(position);
        int all = LinkGoodsEvent.getLocalCount();
        if (isFromImageTag) {
            BaseFunc.showMsg(mContext, getString(R.string.tips_cannot_linkgoods));
        } else {
            if (!aGoods.isBindedImageOfLocal()) {
                EventBus.getDefault().post(new LinkGoodsEvent(false, aGoods));
                tmpUnBind.add(aGoods);
                adapter.notifyDataSetChanged();
            } else {
                confirmTips();
            }
        }
        tvLinked.setText(LinkGoodsEvent.formatLinkedStr(all - 1, 10));
    }

    private void confirmTips() {
        FHHintDialog fhHintDialog = new FHHintDialog(mContext);
        fhHintDialog.setTvContent(getString(R.string.tips_of_unlink_picgoods));
        fhHintDialog.show();
    }

    private void confirmExit() {
        //如果是从图片裁剪过来的 则直接退出
        if (isFromImageTag) {
            finish();
            return;
        }
        FHHintDialog fhHintDialog = new FHHintDialog(mContext);
        fhHintDialog.setTvContent(getString(R.string.tips_linkgoods_exit));
        fhHintDialog.setTvRight(getString(R.string.tips_linkgoods_exit_ok));
        fhHintDialog.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                //撤销操作
                LinkGoodsEvent.opListGoodsByAll(false, tmpBind);
                LinkGoodsEvent.opListGoodsByAll(true, tmpUnBind);
                EventBus.getDefault().post(new LinkGoodsEvent(1));
                finish();
            }
        });
        fhHintDialog.show();
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }
}
