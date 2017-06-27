package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.GoodsFilter;
import com.fanglin.fenhong.microbuyer.base.model.GoodsList;
import com.fanglin.fenhong.microbuyer.base.model.MessageNum;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsListAdapter;
import com.fanglin.fenhong.microbuyer.common.LayoutMoreVertical;
import com.fanglin.fhlib.other.FHLib;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/8.
 */
public class GoodsListActivity extends BaseFragmentActivity implements TextView.OnEditorActionListener, GoodsList.GoodsListModelCallBack, TextWatcher, PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.tvSearchIcon)
    TextView tvSearchIcon;
    @ViewInject(R.id.LGrp)
    LinearLayout LGrp;
    @ViewInject(R.id.LNoresult)
    LinearLayout LNoresult;
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    GoodsListAdapter adapter;
    GoodsFilter gf;
    GoodsList goodsList;

    @ViewInject(R.id.et_key)
    EditText et_key;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;
    @ViewInject(R.id.tv_up)
    TextView tv_up;
    @ViewInject(R.id.tv_down)
    TextView tv_down;
    @ViewInject(R.id.tv_msg_num_top)
    TextView tv_msg_num_top;
    @ViewInject(R.id.ivMore)
    ImageView ivMore;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;

    //顶部更多按钮弹框
    LayoutMoreVertical layoutMoreVertical;

    String searchKey, gcId;
    int isOwn;
    MessageNum msgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodslist);
        ViewUtils.inject(this);

        searchKey = getIntent().getStringExtra("key");
        gcId = getIntent().getStringExtra("gc_id");
        isOwn = getIntent().getIntExtra("is_own", 0);

        msgNum = FHCache.getNum(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        msgNum = FHCache.getNum(this);
        if (msgNum != null && msgNum.getTotalNum() > 0) {
            tv_msg_num_top.setVisibility(View.VISIBLE);
        } else {
            tv_msg_num_top.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            ivClear.setVisibility(View.VISIBLE);
        } else {
            ivClear.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        BaseFunc.toggleSoftInput(mContext);
        gf.curpage = 1;
        gf.key = et_key.getText().toString();

        onPullDownToRefresh(pullToRefreshRecycleView);

        /** 记录搜索动作*/
        BaseFunc.add_search(mContext, et_key.getText().toString(), member);

        return false;
    }

    private void initView() {

        BaseFunc.setFont(tvSearchIcon);
        BaseFunc.setFont(LGrp);
        BaseFunc.setFont(LNoresult);

        //初始化更多选项弹框
        layoutMoreVertical = new LayoutMoreVertical(ivMore);
        layoutMoreVertical.setIsSearchShow(false);
        if (msgnum != null)
            layoutMoreVertical.setMsgNum(msgnum.getTotalNum());

        if (msgNum != null && msgNum.getTotalNum() > 0) {
            tv_msg_num_top.setVisibility(View.VISIBLE);
        } else {
            tv_msg_num_top.setVisibility(View.INVISIBLE);
        }

        gf = new GoodsFilter();
        gf.gc_id = gcId;
        gf.gc_deep = 2;
        gf.is_own = isOwn;
        et_key.setText(searchKey);
        if (gf.is_own == 1) {
            et_key.setHint("分红全球购自营店");
        }

        et_key.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        et_key.setOnEditorActionListener(this);
        et_key.addTextChangedListener(this);

        adapter = new GoodsListAdapter(mContext);
        adapter.setListener(new GoodsListAdapter.GoodsListCallBack() {
            @Override
            public void onItemClick(GoodsList c1, int position) {
                BaseFunc.gotoGoodsDetail(GoodsListActivity.this, c1.goods_id, null, null);
            }
        });
        goodsList = new GoodsList();
        goodsList.setModelCallBack(this);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(new LinearLayoutManager(mContext));
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
        pullToRefreshRecycleView.setBackUpView(btnBackTop);

        chgStatus(0);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        gf.curpage = 1;
        goodsList.get_goods(gf);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        gf.curpage++;
        goodsList.get_goods(gf);
    }

    @OnClick(value = {R.id.ivBack, R.id.tv_default, R.id.tv_sales, R.id.Lprice, R.id.tv_popular, R.id.ivMore, R.id.ivClear})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                BaseFunc.hideSoftInput(mContext, et_key);
                finish();
                break;
            case R.id.tv_default:
                chgStatus(0);
                break;
            case R.id.tv_sales:
                chgStatus(1);
                break;
            case R.id.Lprice:
                chgStatus(2);
                break;
            case R.id.tv_popular:
                chgStatus(3);
                break;
            case R.id.ivMore:
                layoutMoreVertical.show();
                break;
            case R.id.ivClear:
                et_key.getText().clear();
                break;
            default:
                break;
        }
    }

    @Override
    public void onGLMCError(String errcode) {
        DrawList(null, gf.curpage == 1);
    }

    @Override
    public void onGLMCList(List<GoodsList> list) {
        DrawList(list, gf.curpage == 1);
    }

    private void DrawList(final List<GoodsList> list, final boolean isRefresh) {
        FHLib.EnableViewGroup(LGrp, true);//请求完毕 可继续点击
        pullToRefreshRecycleView.onRefreshComplete();
        if (isRefresh) {
            pullToRefreshRecycleView.resetPull(PullToRefreshBase.Mode.BOTH);
            if (list != null && list.size() > 0) {
                adapter.setList(list);
                LNoresult.setVisibility(View.GONE);
                pullToRefreshRecycleView.setVisibility(View.VISIBLE);
            } else {
                //显示无商品的提示
                adapter.Clear();
                pullToRefreshRecycleView.getRefreshableView().getAdapter().notifyDataSetChanged();
                LNoresult.setVisibility(View.VISIBLE);
                pullToRefreshRecycleView.setVisibility(View.GONE);
            }
        } else {
            if (list != null && list.size() > 0) {
                adapter.addList(list);
                pullToRefreshRecycleView.onAppendData();
            } else {
                pullToRefreshRecycleView.showNoMore();
            }
        }
    }

    private void chgStatus(int index) {
        FHLib.EnableViewGroup(LGrp, false);//如果正在请求--不让继续点击
        for (int i = 0; i < 4; i++) {
            LGrp.getChildAt(i).setSelected(false);
        }
        LGrp.getChildAt(index).setSelected(true);

        switch (index) {
            case 0:
                gf.sort = 3;
                gf.order = 1;
                break;
            case 1:
                gf.sort = 1;
                gf.order = 1;
                break;
            case 2:
                gf.sort = 2;
                gf.order = gf.order == 1 ? 2 : 1;
                tv_down.setSelected(gf.order == 2);
                tv_up.setSelected(gf.order == 1);
                break;
            case 3:
                gf.sort = 4;
                gf.order = 1;
                break;
        }

        gf.curpage = 1;
        gf.key = et_key.getText().toString();
        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        InputMethodManager im = BaseFunc.getInputMethodManager(this);
        if (im.isActive()) {
            im.hideSoftInputFromWindow(et_key.getWindowToken(), 0);
        }
    }

}
