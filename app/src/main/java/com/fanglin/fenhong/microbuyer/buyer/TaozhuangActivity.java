package com.fanglin.fenhong.microbuyer.buyer;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Bundlings;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2016/1/5.
 * 核对购物车或者订单页点击进入 套装详情列表页
 * 词穷：taozhuang
 */
public class TaozhuangActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.goods_flow)
    TagFlowLayout goods_flow;
    @ViewInject(R.id.tv_price_taozhuang)
    TextView tv_price_taozhuang;
    @ViewInject(R.id.tv_price_original_title)
    TextView tv_price_original_title;
    @ViewInject(R.id.tv_save)
    TextView tv_save;

    public List<Bundlings> bl_list;//优惠套装

    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_taozhuang, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            bl_list = new Gson().fromJson(getIntent().getStringExtra("VAL"), new TypeToken<List<Bundlings>>() {
            }.getType());
        } catch (Exception e) {
            bl_list = null;
        }

        if (bl_list == null || bl_list.size() == 0) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
        }

        initView();
    }

    private void initView() {
        df = new DecimalFormat("¥#0.00");
        setHeadTitle(R.string.goods_detail_prom_bundle_head);

        final LayoutInflater mInflater = LayoutInflater.from(mContext);

        goods_flow.setAdapter(new TagAdapter(bl_list) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                final Bundlings agoods = (Bundlings) o;
                View view = mInflater.inflate(R.layout.item_building_goods, goods_flow, false);

                LinearLayout LFlag = (LinearLayout) view.findViewById(R.id.LFlag);
                ImageView ivNationFlag = (ImageView) view.findViewById(R.id.ivNationFlag);
                TextView tvNationDesc = (TextView) view.findViewById(R.id.tvNationDesc);
                if (!TextUtils.isEmpty(agoods.nation_flag) && !TextUtils.isEmpty(agoods.goods_promise)) {
                    LFlag.setVisibility(View.VISIBLE);
                    new FHImageViewUtil(ivNationFlag).setImageURI(agoods.nation_flag, FHImageViewUtil.SHOWTYPE.BANNER);
                    tvNationDesc.setText(agoods.goods_promise);
                } else {
                    LFlag.setVisibility(View.GONE);
                }

                CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
                ImageView sdv = (ImageView) view.findViewById(R.id.sdv);
                TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                TextView tv_price = (TextView) view.findViewById(R.id.tv_price);

                TextView tv_bonus_desc = (TextView) view.findViewById(R.id.tv_bonus_desc);

                cb.setVisibility(View.GONE);
                tv_memo.setText("单买价");
                tv_title.setText(agoods.goods_name);
                tv_price.setText(df.format(agoods.bl_goods_price + agoods.down_price));
                new FHImageViewUtil(sdv).setImageURI(agoods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoGoodsDetail(mContext,agoods.goods_id,null,null);
                    }
                });

                BaseFunc.setFont(tv_bonus_desc);
                String actDesc = agoods.getActivityDesc(mContext);
                if (actDesc != null) {
                    tv_bonus_desc.setVisibility(View.VISIBLE);
                    tv_bonus_desc.setText(actDesc);
                } else {
                    tv_bonus_desc.setVisibility(View.GONE);
                }

                return view;
            }
        });

        double original = Bundlings.getListOriginal(bl_list);
        double downprice = Bundlings.getListDownPrice(bl_list);
        tv_price_taozhuang.setText(df.format(original - downprice));
        tv_save.setText("立省" + df.format(downprice));
        tv_price_original_title.setText(df.format(original));
        tv_price_original_title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }
}
