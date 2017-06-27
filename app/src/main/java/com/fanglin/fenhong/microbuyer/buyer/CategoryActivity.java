package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.Category;
import com.fanglin.fenhong.microbuyer.base.model.CategoryEntity;
import com.fanglin.fenhong.microbuyer.common.LayoutMoreVertical;
import com.fanglin.fenhong.microbuyer.common.adapter.CategorySectionAdapter;
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

/**
 * 作者： Created by Plucky on 2015/9/8.
 * 商品分类页面
 */
public class CategoryActivity extends BaseFragmentActivity implements TextView.OnEditorActionListener, PullToRefreshBase.OnRefreshListener {

    @ViewInject(R.id.LTop)
    LinearLayout LTop;

    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;
    @ViewInject(R.id.et_key)
    EditText et_key;

    CategorySectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        BaseFunc.setFont(LTop);
        et_key.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        et_key.setOnEditorActionListener(this);

        adapter = new CategorySectionAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        GridLayoutManager.SpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(layoutManager);
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        onRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        getData();
    }

    @OnClick(value = {R.id.ivBack, R.id.tv_more})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tv_more://更多选项
                LayoutMoreVertical layoutMoreVertical = new LayoutMoreVertical(LTop);
                layoutMoreVertical.setShareData(null);
                layoutMoreVertical.show();
                break;
        }
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
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.no_data));
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
         /** 记录搜索动作*/
        BaseFunc.add_search(mContext,et_key.getText().toString(), member);
        BaseFunc.toggleSoftInput(mContext);

        /** 进入搜索列表页*/
        BaseFunc.gotoGoodsListActivity(this,et_key.getText().toString(),0,null);
        return false;
    }
}
