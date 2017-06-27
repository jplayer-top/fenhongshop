package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.event.WifiUnconnectHintAfter;
import com.fanglin.fenhong.microbuyer.base.model.Category;
import com.fanglin.fenhong.microbuyer.base.model.CategoryEntity;
import com.fanglin.fenhong.microbuyer.common.adapter.CategorySectionAdapter;
import com.fanglin.fhlib.other.FHLib;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 分类页面UI优化
 * modify by lizhixin 2016/04/21
 */
public final class FragmentCategory extends BaseFragment implements PullToRefreshBase.OnRefreshListener {

    @ViewInject(R.id.llRrefresh)
    LinearLayout llRefresh;
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.etKey)
    TextView etKey;
    @ViewInject(R.id.tvIcon)
    TextView tvIcon;

    CategorySectionAdapter adapter;
    private static final String KEY_CONTENT = "FragmentCategory:Content";

    public static FragmentCategory newInstance(String url) {
        FragmentCategory fragment = new FragmentCategory();
        fragment.murl = url;

        return fragment;
    }

    private String murl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            murl = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ViewUtils.inject(this, layout);
        initView();
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, murl);
    }

    private void initView() {
        EventBus.getDefault().register(this);

        adapter = new CategorySectionAdapter(act);
        GridLayoutManager layoutManager = new GridLayoutManager(act, 2);
        GridLayoutManager.SpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(layoutManager);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        BaseFunc.setFont(tvIcon);
        etKey.setHint(act.getString(R.string.home_title_search_text));

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);
        onRefresh(pullToRefreshRecycleView);
        /**
         * 只在第一次加载的时候去判断是否断网，来显示这两个控件
         */
        if (FHLib.isNetworkConnected(getActivity()) == 0) {
            llRefresh.setVisibility(View.VISIBLE);
        } else {
            llRefresh.setVisibility(View.GONE);
        }
    }

    @OnClick(value = {R.id.LSearch})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.LSearch:
                BaseFunc.gotoActivity(act, SearchActivity.class, "");
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        getData();
    }

    private void getData() {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                pullToRefreshRecycleView.onRefreshComplete();
                if (isSuccess) {
                    llRefresh.setVisibility(View.GONE);
                    List<Category> list_china = new ArrayList<>();
                    List<Category> list_global = new ArrayList<>();
                    try {
                        JSONArray ja = new JSONArray(data);
                        list_china = new Gson().fromJson(ja.getString(0), new TypeToken<List<Category>>() {
                        }.getType());
                        list_global = new Gson().fromJson(ja.getString(1), new TypeToken<List<Category>>() {
                        }.getType());
                    } catch (Exception e) {
                        //
                    }
                    DrawList(list_china, list_global);
                }
            }
        }).get_goods_class(null, "2");
    }

    private void DrawList(List<Category> list_china, List<Category> list_global) {
        List<CategoryEntity> list = new ArrayList<>();
        if (list_global != null && list_global.size() > 0) {
            CategoryEntity entity = new CategoryEntity();
            entity.type = getString(R.string.category_sea);
            entity.list = list_global;
            list.add(entity);
        }
        if (list_china != null && list_china.size() > 0) {
            CategoryEntity entity = new CategoryEntity();
            entity.type = getString(R.string.category_china);
            entity.list = list_china;
            list.add(entity);
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleNoWfi(WifiUnconnectHintAfter wifiUnconnectHintEntity) {
        if (adapter.list == null || adapter.list.size() == 0) {
            onRefresh(pullToRefreshRecycleView);
        }
    }

}
