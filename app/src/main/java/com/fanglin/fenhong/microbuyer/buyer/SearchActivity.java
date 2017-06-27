package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.FHAutoComplete;
import com.fanglin.fenhong.microbuyer.buyer.adapter.AutoCompleteAdapter;
import com.fanglin.fenhong.microbuyer.buyer.adapter.SearchKeyAdapter;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/8.
 * modify by lizhixin on 2016/02/04
 */
public class SearchActivity extends BaseFragmentActivity implements TextView.OnEditorActionListener, SearchKeyAdapter.SearchKeyCallBack, TextWatcher, FHAutoComplete.FHAutoCompleteModelCallBack {

    @ViewInject(R.id.rcv_history)
    RecyclerView rcv_history;
    @ViewInject(R.id.et_key)
    EditText et_key;
    @ViewInject(R.id.ivClear)
    ImageView ivClear;

    @ViewInject(R.id.tv_search_icon)
    TextView tv_search_icon;
    @ViewInject(R.id.tv_hot_icon)
    TextView tv_hot_icon;
    @ViewInject(R.id.tv_search_history_icon)
    TextView tv_search_history_icon;
    @ViewInject(R.id.tv_icon_delete)
    TextView tv_icon_delete;
    @ViewInject(R.id.ll_hot_search)
    LinearLayout ll_hot_search;
    @ViewInject(R.id.tfl_hot_search)
    TagFlowLayout tfl_hot_search;

    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.scrollView)
    ScrollView scrollView;

    SearchKeyAdapter adapter_his;
    AutoCompleteAdapter autoCompleteAdapter;

    FHAutoComplete autoCompleteReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {

        String searchText = getIntent().getStringExtra("VAL");
        if (!TextUtils.isEmpty(searchText)) {
            et_key.setHint(searchText);//优先使用首页传递过来的提示文字
        }

        tv_search_icon.setTypeface(iconfont);
        BaseFunc.setFont(tv_hot_icon);
        BaseFunc.setFont(tv_search_history_icon);
        BaseFunc.setFont(tv_icon_delete);

        et_key.setOnEditorActionListener(this);
        et_key.addTextChangedListener(this);

        AutoGridLayoutManager agm = new AutoGridLayoutManager(this, 1);
        agm.setOrientation(LinearLayoutManager.VERTICAL);
        agm.setSmoothScrollbarEnabled(true);

        rcv_history.setLayoutManager(agm);
        adapter_his = new SearchKeyAdapter(mContext);
        adapter_his.setCallBack(this);
        rcv_history.setAdapter(adapter_his);

        getHots();//热搜
        get_search_history_list();//搜索历史

        autoCompleteAdapter = new AutoCompleteAdapter(this);
        listView.setAdapter(autoCompleteAdapter);

        autoCompleteReq = new FHAutoComplete();
        autoCompleteReq.setCallBack(this);
    }

    @Override
    public void onFHAutoList(List<FHAutoComplete> list) {
        if (list != null && list.size() > 0) {
            autoCompleteAdapter.setList(list);
            listView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
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
            //autoCompleteReq.getList();
        } else {
            ivClear.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(value = {R.id.tv_cancel, R.id.ll_clear, R.id.ivClear})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.ll_clear:
                if (adapter_his.getItemCount() == 0) return;

                final FHHintDialog fhd = new FHHintDialog(SearchActivity.this);
                fhd.setHintListener(new FHHintDialog.FHHintListener() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        delete_search_history();
                    }
                });
                fhd.setTvContent(getString(R.string.hint_delete_all));
                fhd.show();
                break;
            case R.id.ivClear:
                et_key.getText().clear();
                break;
            default:
                break;
        }
    }

    /**
     * 获取热搜词汇
     */
    private void getHots() {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<String> hots;
                try {
                    hots = new Gson().fromJson(data, new TypeToken<List<String>>() {
                    }.getType());
                } catch (Exception e) {
                    hots = null;
                }

                if (hots != null && hots.size() > 0) {

                    final List<String> finalHots = hots;
                    tfl_hot_search.setAdapter(new TagAdapter(finalHots) {
                        @Override
                        public View getView(FlowLayout parent, final int position, Object o) {
                            View view = LayoutInflater.from(mContext).inflate(R.layout.item_tv_search, tfl_hot_search, false);
                            TextView tvKey = (TextView) view.findViewById(R.id.key);
                            tvKey.setText(finalHots.get(position));
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onItemClick(finalHots.get(position), position);
                                }
                            });
                            return view;
                        }
                    });

                    ll_hot_search.setVisibility(View.VISIBLE);
                    tfl_hot_search.setVisibility(View.VISIBLE);
                } else {
                    ll_hot_search.setVisibility(View.GONE);
                    tfl_hot_search.setVisibility(View.GONE);
                }
            }
        }).get_hot_search();
    }

    /**
     * 获取搜索历史
     */
    private void get_search_history_list() {
        adapter_his.setList(FHCache.getReversedSearchList(mContext));
        adapter_his.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(String key, int position) {
        /**进入搜索列表页*/
        BaseFunc.gotoGoodsListActivity(this, key, 0, null);
    }

    /**
     * 清空搜索历史
     */
    private void delete_search_history() {
        FHCache.clearSearch(mContext);
        get_search_history_list();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /** 记录搜索动作*/
        BaseFunc.add_search(mContext, TextUtils.isEmpty(et_key.getText().toString().trim()) ? et_key.getHint().toString().trim() : et_key.getText().toString().trim(), member);

        BaseFunc.toggleSoftInput(mContext);
        /** 进入搜索列表页*/
        String key = TextUtils.isEmpty(et_key.getText().toString().trim()) ? et_key.getHint().toString().trim() : et_key.getText().toString().trim();//关键字
        BaseFunc.gotoGoodsListActivity(this, key, 0, null);
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        InputMethodManager im = BaseFunc.getInputMethodManager(this);
        if (im.isActive()) {
            im.hideSoftInputFromWindow(et_key.getWindowToken(), 0);
        }
        finish();
    }
}
