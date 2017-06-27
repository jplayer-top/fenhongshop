package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.GoodsBundling;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsPromBundleListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 商品促销 优惠套装(捆绑销售)列表页
 * Created by lizhixin on 2015/12/31.
 */
public class GoodsPromBundleListActivity extends BaseFragmentActivityUI {


    @ViewInject(R.id.pullToRefreshListView)
    PullToRefreshListView pullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(mContext, R.layout.activity_goodsprobundle, null);
        LHold.addView(view);
        setHeadTitle(R.string.goods_detail_prom_bundle_head);
        ViewUtils.inject(this, view);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);

        String str = getIntent().getStringExtra("VAL");
        int goods_source = getIntent().getIntExtra("goods_source", 0);

        if (!TextUtils.isEmpty(str)) {
            //转化优惠套装列表
            List<GoodsBundling> bundling = new Gson().fromJson(str, new TypeToken<List<GoodsBundling>>() {
            }.getType());

            GoodsPromBundleListAdapter adapter = new GoodsPromBundleListAdapter(this);
            adapter.setList(bundling, goods_source);
            pullToRefreshListView.setAdapter(adapter);
        }

    }

    @Override
    protected void onResume() {
        skipChk = true;//跳过登录检查
        super.onResume();
    }
}
