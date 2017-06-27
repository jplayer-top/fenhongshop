package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.GoodsBundling;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDetail;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromMansongRules;
import com.fanglin.fenhong.microbuyer.base.model.WSGoodsDtlBundling;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fhlib.other.CountDown;

import java.util.List;

/**
 * 商品促销单元
 * Added By Plucky 2016-4-24
 */
public class LayoutGoodsPromotion implements View.OnClickListener, WSGoodsDtlBundling.WSGoodsDtlBundlingCallBack {

    private Context mContext;
    private ViewGroup viewGroup;
    private LinearLayout LManjianAll, LBaoyou, LTaozhuang, LXianshi, LMiaoshaTag, LRenxuan;
    private TextView tvBaoyou, tvTaozhuang, tvXianshi, tvMiaoshaTag, tvRenxuan;
    private View vBaoyou, vTaozhuang, vXianshi, vMiaoshaTag, vTop, vRenxuan;


    private List<GoodsBundling> bundling;//优惠套装
    private int goods_source;//0 国内, 1 国外 -- 用于套装加入购物车时的参数
    boolean hasActivity = false;
    private String storeId, goodsId;

    public LayoutGoodsPromotion(Context mContext) {
        this.mContext = mContext;
        viewGroup = (ViewGroup) View.inflate(mContext, R.layout.layout_goods_promotion, null);
        initView();
    }

    private void initView() {
        LManjianAll = (LinearLayout) viewGroup.findViewById(R.id.LManjianAll);

        LBaoyou = (LinearLayout) viewGroup.findViewById(R.id.LBaoyou);
        tvBaoyou = (TextView) viewGroup.findViewById(R.id.tvBaoyou);
        vBaoyou = viewGroup.findViewById(R.id.vBaoyou);


        LTaozhuang = (LinearLayout) viewGroup.findViewById(R.id.LTaozhuang);
        tvTaozhuang = (TextView) viewGroup.findViewById(R.id.tvTaozhuang);
        vTaozhuang = viewGroup.findViewById(R.id.vTaozhuang);


        LXianshi = (LinearLayout) viewGroup.findViewById(R.id.LXianshi);
        tvXianshi = (TextView) viewGroup.findViewById(R.id.tvXianshi);
        vXianshi = viewGroup.findViewById(R.id.vXianshi);

        LMiaoshaTag = (LinearLayout) viewGroup.findViewById(R.id.LMiaoshaTag);
        tvMiaoshaTag = (TextView) viewGroup.findViewById(R.id.tvMiaoshaTag);
        vMiaoshaTag = viewGroup.findViewById(R.id.vMiaoshaTag);

        LRenxuan = (LinearLayout) viewGroup.findViewById(R.id.LRenxuan);
        tvRenxuan = (TextView) viewGroup.findViewById(R.id.tvRenxuan);
        vRenxuan = viewGroup.findViewById(R.id.vRenxuan);

        vTop = viewGroup.findViewById(R.id.vTop);

        LBaoyou.setOnClickListener(this);
        LTaozhuang.setOnClickListener(this);
        LRenxuan.setOnClickListener(this);

        TextView tvIconBonus = (TextView) viewGroup.findViewById(R.id.tvIconBonus);
        TextView tvIconTaozhuang = (TextView) viewGroup.findViewById(R.id.tvIconTaozhuang);
        TextView tvIconHuangou = (TextView) viewGroup.findViewById(R.id.tvIconHuangou);
        TextView tvInStoreIcon = (TextView) viewGroup.findViewById(R.id.tvInStoreIcon);
        TextView tvRenxuanIcon = (TextView) viewGroup.findViewById(R.id.tvRenxuanIcon);

        BaseFunc.setFont(tvIconBonus);
        BaseFunc.setFont(tvIconTaozhuang);
        BaseFunc.setFont(tvIconHuangou);
        BaseFunc.setFont(tvInStoreIcon);
        BaseFunc.setFont(tvRenxuanIcon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //套装
            case R.id.LTaozhuang:
                if (bundling != null) {
                    BaseFunc.gotoGoodsBundle(mContext, bundling, goods_source);
                }
                break;
            //包邮
            case R.id.LBaoyou:
                BaseFunc.gotoActivity(mContext, StoreActivity.class, storeId);
                break;
            //任选
            case R.id.LRenxuan:
                if (((BaseFragmentActivity) mContext).member != null) {
                    String url = String.format(BaseVar.URL_RENXUAN, goodsId, storeId);
                    BaseFunc.urlClick(mContext, url);
                } else {
                    BaseFunc.gotoLogin(mContext);
                }
                break;
            default:
                break;
        }
    }

    public void setDatas(final GoodsDetail detail) {

        //发送请求获取优惠套装与推荐组合数据
        sendRequestToGetBundling(detail.goods_id);
        this.goods_source = detail.goods_source;
        storeId = detail.store_id;
        goodsId = detail.goods_id;

        //包邮
        if (detail.store_free_freight_amount > 0) {
            hasActivity = true;
            LBaoyou.setVisibility(View.VISIBLE);
            vBaoyou.setVisibility(View.VISIBLE);
            tvBaoyou.setText(BaseFunc.fromHtml(String.format(mContext.getString(R.string.goods_detail_baoyou_title), detail.store_free_freight_amount + "")));
        } else {
            LBaoyou.setVisibility(View.GONE);
            vBaoyou.setVisibility(View.GONE);
        }

        //限时折扣
        if (detail.xianshi != null) {
            hasActivity = true;
            LXianshi.setVisibility(View.VISIBLE);
            vXianshi.setVisibility(View.VISIBLE);
            final String discount = BaseFunc.truncDouble(detail.xianshi.price / detail.goods_price * 10, 1);
            if (detail.xianshi.countdown > 0) {
                new CountDown(detail.xianshi.countdown).start(new CountDown.CountDownListener() {
                    @Override
                    public void onChange(long atime) {
                        long[] times = BaseFunc.getD_H_M_S(atime);
                        tvXianshi.setText(BaseFunc.fromHtml(String.format(mContext.getString(R.string.goods_detail_xianshi_title), discount, "" + times[0], times[1] + "", times[2] + "", times[3] + "")));
                    }

                    @Override
                    public void onStop() {
                        LXianshi.setVisibility(View.GONE);
                    }
                });
            } else {
                tvXianshi.setText(String.format(mContext.getString(R.string.goods_detail_prom_xianshi_title_simple), discount));
            }

        } else {
            LXianshi.setVisibility(View.GONE);
            vXianshi.setVisibility(View.GONE);
        }

        //满额优惠
        if (detail.mansong != null && detail.mansong.rules != null && detail.mansong.rules.size() > 0) {
            hasActivity = true;
            LManjianAll.setVisibility(View.VISIBLE);
            initViewMansong(detail.mansong.rules);
        } else {
            LManjianAll.setVisibility(View.GONE);
        }
        //秒杀
        if (detail.seckilling != null) {
            if (detail.seckilling.top_limit == 0) {
                LMiaoshaTag.setVisibility(View.GONE);
                vMiaoshaTag.setVisibility(View.GONE);
            } else {
                hasActivity = true;
                LMiaoshaTag.setVisibility(View.VISIBLE);
                vMiaoshaTag.setVisibility(View.VISIBLE);
                tvMiaoshaTag.setText(detail.seckilling.getMiaoshaDesc());
            }
        } else {
            LMiaoshaTag.setVisibility(View.GONE);
            vMiaoshaTag.setVisibility(View.GONE);
        }
        //任选
        if (detail.manselect_rule != null && detail.manselect_rule.size() > 0) {
            LRenxuan.setVisibility(View.VISIBLE);
            vRenxuan.setVisibility(View.VISIBLE);
            tvRenxuan.setText(detail.getRenxuanRuleDesc());
        } else {
            LRenxuan.setVisibility(View.GONE);
            vRenxuan.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化满额优惠布局
     */
    private void initViewMansong(List<GoodsDtlPromMansongRules> rules) {
        LayoutManjianAll manjianAll = new LayoutManjianAll(mContext);
        manjianAll.setList(rules);
        LManjianAll.removeAllViews();
        LManjianAll.addView(manjianAll.getView());
    }

    /**
     * 获取优惠套装 与 推荐组合数据
     */
    private void sendRequestToGetBundling(String goods_id) {
        WSGoodsDtlBundling wsGoodsDtlBundling = new WSGoodsDtlBundling();
        wsGoodsDtlBundling.setWSGoodsDtlBundlingCallBack(this);
        wsGoodsDtlBundling.getBundlingList(goods_id);
    }

    @Override
    public void onWSGoodsDtlBundlingError(String errcode) {
        LTaozhuang.setVisibility(View.GONE);
        vTaozhuang.setVisibility(View.GONE);
    }

    @Override
    public void onWSGoodsDtlBundlingSuccess(WSGoodsDtlBundling data) {
        if (data != null) {
            this.bundling = data.bundling;

            if (data.bundling != null && data.bundling.size() > 0) {
                //显示标题，格式如 共1款套装，最高节省2.22
                tvTaozhuang.setText(String.format(mContext.getString(R.string.goods_detail_prom_bundle_title), data.bundling.size() + "", data.bundling_save_money + ""));
                LTaozhuang.setVisibility(View.VISIBLE);
                vTaozhuang.setVisibility(View.VISIBLE);
                hasActivity = true;
            } else {
                LTaozhuang.setVisibility(View.GONE);
                vTaozhuang.setVisibility(View.GONE);
            }

            if (promotionListener != null) {
                promotionListener.onBundling(bundling);
            }
        } else {
            LTaozhuang.setVisibility(View.GONE);
            vTaozhuang.setVisibility(View.GONE);
        }
    }

    public ViewGroup getViewGroup() {
        /**
         * 手动设置宽高以使MATCH_PARENT生效
         */
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (hasActivity) {
            vTop.setVisibility(View.VISIBLE);
        } else {
            vTop.setVisibility(View.GONE);
        }
        viewGroup.setLayoutParams(params);
        return viewGroup;
    }

    private GoodsPromotionListener promotionListener;

    public void setPromotionListener(GoodsPromotionListener promotionListener) {
        this.promotionListener = promotionListener;
    }

    public interface GoodsPromotionListener {
        void onBundling(List<GoodsBundling> bundling);
    }
}
