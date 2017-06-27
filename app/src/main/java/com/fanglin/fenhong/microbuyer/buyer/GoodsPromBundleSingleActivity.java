package com.fanglin.fenhong.microbuyer.buyer;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.VarInstance;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.GoodsBundling;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsPromBundleSingleAdapter;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 商品促销 优惠套装(捆绑销售) Item 页
 * Created by lizhixin on 2015/12/31.
 */
public class GoodsPromBundleSingleActivity extends BaseFragmentActivityUI implements View.OnClickListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView recyclerView;
    @ViewInject(R.id.tv_index)
    private TextView tvIndex;
    @ViewInject(R.id.tv_price_taozhuang)
    private TextView tvPriceTaoZhuang;
    @ViewInject(R.id.tv_price_original)
    private TextView tvPriceOriginal;
    @ViewInject(R.id.tv_save)
    private TextView tvSave;
    @ViewInject(R.id.tv_add_to_cart)
    private TextView tvAddToCart;

    private GoodsBundling goodsBundling;
    private int goods_source;//0 国内, 1 国外

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(mContext, R.layout.layout_goods_promotion_taozhuang, null);
        LHold.addView(view);
        setHeadTitle(R.string.goods_detail_prom_bundle_head);
        ViewUtils.inject(this, view);

        String str = getIntent().getStringExtra("VAL");
        goods_source = getIntent().getIntExtra("goods_source", 0);

        if (!TextUtils.isEmpty(str)) {
            try {
                goodsBundling = new Gson().fromJson(str, GoodsBundling.class);
            } catch (Exception e) {
                goodsBundling = null;
            }
        }

        initView();
    }

    private void initView() {
        tvAddToCart.setOnClickListener(this);

        if (goodsBundling != null) {
            tvIndex.setText(String.format(getString(R.string.goods_detail_prom_bundle_taozhuang_index), goodsBundling.position));
            tvPriceTaoZhuang.setText(BaseFunc.fromHtml(String.format(getString(R.string.goods_detail_prom_bundle_price_taozhuang), goodsBundling.current_price)));
            tvPriceOriginal.setText(String.format(getString(R.string.yuan_), goodsBundling.cost_price));
            tvPriceOriginal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvSave.setText(String.format(getString(R.string.goods_detail_prom_im_save), BaseFunc.truncDouble(goodsBundling.cost_price - goodsBundling.current_price, 2)));

            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            GoodsPromBundleSingleAdapter adapter = new GoodsPromBundleSingleAdapter(mContext);
            adapter.setList(goodsBundling.goods_list);
            recyclerView.setAdapter(adapter);

            recyclerView.setFocusable(false);
        }
    }

    @Override
    protected void onResume() {
        skipChk = true;//跳过登录检查
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_to_cart:
                if (member == null) {

                    BaseFunc.gotoLogin(mContext);

                } else if (goodsBundling != null) {

                    new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
                        @Override
                        public void onStart(String data) {

                        }

                        @Override
                        public void onEnd(boolean isSuccess, String data) {
                            if (isSuccess) {
                                VarInstance.getInstance().showMsg(R.string.add_success);
                                finish();
                            }
                        }
                    }).add_cart(member.member_id, member.token, null, null, 1, goods_source, goodsBundling.bl_id, null);

                }
                break;
            default:
                break;
        }
    }
}
