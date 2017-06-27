package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BaseGoods;
import com.fanglin.fenhong.microbuyer.base.model.BundlingOrderEntity;
import com.fanglin.fenhong.microbuyer.base.model.OrderDtl;
import com.fanglin.fenhong.microbuyer.base.model.OrderGoods;
import com.fanglin.fenhong.microbuyer.buyer.TaozhuangActivity;
import com.fanglin.fenhong.microbuyer.buyer.adapter.RecycleImgAdapter;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.ReturnGoodsActivity;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/23.
 * modify by lizhixin on 2016/1/8
 */
public class OrderDtlAdapter extends RecyclerView.Adapter<OrderDtlAdapter.ViewHoder> {

    private Context mContext;
    OrderDtl orderDtl;

    private static final int NORMAL_SECTION = 1;//正常布局
    private static final int FOOTER_SECTION = 2;//底部布局

    public OrderDtlAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(OrderDtl dtl) {
        this.orderDtl = dtl;
    }

    @Override
    public int getItemCount() {
        if (orderDtl == null) return 0;
        return 2;//正常商品布局 + 底部布局
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return NORMAL_SECTION;
        } else {
            return FOOTER_SECTION;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMAL_SECTION) {
            view = View.inflate(mContext, R.layout.item_cartcheck, null);
        } else {
            view = View.inflate(mContext, R.layout.item_order_detail_footer, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
        }
        return new ViewHoder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHoder holder, final int position) {
        if (position == 0 && holder.getItemViewType() == NORMAL_SECTION) {
            holder.ll_agreement_policy.setVisibility(View.GONE);
            /**
             * 正常商品布局
             */
            holder.tv_store.setText(orderDtl.store_name);
            holder.tv_store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String store_id = orderDtl.store_id;
                    if (store_id == null) return;
                    BaseFunc.gotoActivity(mContext, StoreActivity.class, store_id);
                }
            });
            BaseFunc.setFont(holder.LIcon);
            final LayoutInflater mInflater = LayoutInflater.from(mContext);

            List<OrderGoods> goodses = orderDtl.order_goods;

            holder.goods_flow.setAdapter(new TagAdapter(goodses) {
                @Override
                public View getView(FlowLayout parent, int position, Object o) {

                    View view;
                    final OrderGoods goods = (OrderGoods) o;

                    if (goods.bundling != null && goods.bundling.getImageStr() != null && goods.bundling.getImageStr().length > 0) {
                        /** 有优惠套装列表*/
                        final BundlingOrderEntity bundling = goods.bundling;
                        String[] imgs = bundling.getImageStr();

                        view = mInflater.inflate(R.layout.item_cartcheck_buildings, holder.goods_flow, false);

                        RecyclerView rc_img = (RecyclerView) view.findViewById(R.id.rc_img);
                        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                        TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                        TextView tv_icon = (TextView) view.findViewById(R.id.tv_icon);
                        TextView tvBundlingFlag = (TextView) view.findViewById(R.id.tvBundlingFlag);
                        TextView tv_bonus_desc = (TextView) view.findViewById(R.id.tv_bonus_desc);

                        tv_title.setText(bundling.bl_name);
                        tv_price.setText("¥" + bundling.bl_price);
                        String fmt = mContext.getString(R.string.num_buy);
                        String memo = String.format(fmt, bundling.bl_num);
                        tv_memo.setText(memo);
                        BaseFunc.setFont(tv_icon);

                        tvBundlingFlag.setVisibility(View.VISIBLE);
                        if (bundling.getBLState() == 0) {
                            //如果套装内有商品失效，则标题与价格置灰
                            tv_title.setEnabled(false);
                            tv_price.setEnabled(false);
                            tvBundlingFlag.setEnabled(false);
                        } else {
                            tv_title.setEnabled(true);
                            tv_price.setEnabled(true);
                            tvBundlingFlag.setEnabled(true);
                        }

                        RecycleImgAdapter imgAdapter = new RecycleImgAdapter(mContext);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                        rc_img.setLayoutManager(linearLayoutManager);
                        imgAdapter.setList(imgs);
                        rc_img.setAdapter(imgAdapter);

                        imgAdapter.setCallBack(new RecycleImgAdapter.RecyclerImgCallBack() {
                            @Override
                            public void onItemClick(int position) {
                                BaseFunc.gotoActivity(mContext, TaozhuangActivity.class, new Gson().toJson(bundling.bl_list));
                            }
                        });

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BaseFunc.gotoActivity(mContext, TaozhuangActivity.class, new Gson().toJson(bundling.bl_list));
                            }
                        });

                        BaseFunc.setFont(tv_bonus_desc);
                        Spanned actDesc = goods.getBundlingActivityDesc(mContext);
                        if (actDesc != null) {
                            tv_bonus_desc.setVisibility(View.VISIBLE);
                            tv_bonus_desc.setText(actDesc);
                        } else {
                            tv_bonus_desc.setVisibility(View.GONE);
                        }

                    } else {
                        /** 没有优惠套装列表*/
                        view = mInflater.inflate(R.layout.item_order_goods, holder.goods_flow, false);

                        /**
                         * 为适配特殊机型，手动设置item_goods的宽度，使其能够正确的纵向绘制各个布局 —— lizhixin 20151202
                         */
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BaseFunc.getDisplayMetrics(mContext).widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
                        view.setLayoutParams(params);

                        ImageView sdv = (ImageView) view.findViewById(R.id.sdv);
                        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                        TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                        TextView tv_service = (TextView) view.findViewById(R.id.tv_service);
                        TextView tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
                        TextView tv_activity_desc = (TextView) view.findViewById(R.id.tv_activity_desc);
                        TextView tv_mansong_desc = (TextView) view.findViewById(R.id.tv_mansong_desc);
                        TextView tv_bonus_desc = (TextView) view.findViewById(R.id.tv_bonus_desc);

                        tv_title.setText(goods.goods_name);
                        tv_price.setText("¥" + goods.goods_price);
                        String fmt = mContext.getString(R.string.num_buy);
                        String memo = String.format(fmt, goods.goods_num);
                        tv_memo.setText(memo);
                        tv_subtitle.setText(BaseGoods.parseSpec(goods.goods_spec));

                        if (TextUtils.equals(goods.goods_type, "5")) {
                            //满送提示
                            tv_mansong_desc.setVisibility(View.VISIBLE);
                            tv_activity_desc.setVisibility(View.GONE);//隐藏限时折扣提示
                            tv_subtitle.setVisibility(View.GONE);//隐藏副标题
                        } else if (TextUtils.equals(goods.goods_type, "3")) {
                            //限时折扣提示
                            tv_activity_desc.setText(goods.xianshi == null ? "" : goods.xianshi.getLowerLimitText());
                            tv_activity_desc.setVisibility(View.VISIBLE);
                            tv_mansong_desc.setVisibility(View.GONE);//隐藏满送提示
                            tv_subtitle.setVisibility(View.GONE);//隐藏副标题
                        } else {
                            tv_mansong_desc.setVisibility(View.GONE);
                            tv_activity_desc.setVisibility(View.GONE);
                            tv_subtitle.setVisibility(View.VISIBLE);//显示商品规格
                        }

                        tv_service.setVisibility(goods.ifRefund(orderDtl.order_state) ? View.VISIBLE : View.GONE);

                        new FHImageViewUtil(sdv).setImageURI(goods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

                        tv_service.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, ReturnGoodsActivity.class);
                                intent.putExtra("REC", goods.rec_id);
                                intent.putExtra("ORDER", orderDtl.order_id);
                                mContext.startActivity(intent);
                            }
                        });
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BaseFunc.gotoGoodsDetail(mContext, goods.goods_id, null, null);
                            }
                        });

                        BaseFunc.setFont(tv_bonus_desc);
                        String actDesc = goods.getActivityDesc(mContext);
                        if (actDesc != null) {
                            tv_bonus_desc.setVisibility(View.VISIBLE);
                            tv_bonus_desc.setText(actDesc);
                        } else {
                            tv_bonus_desc.setVisibility(View.GONE);
                        }
                    }

                    return view;
                }
            });

            DecimalFormat df = new DecimalFormat("¥#0.00");
            holder.tv_store_freight.setText(df.format(orderDtl.shipping_fee));
            holder.tv_store_money.setText(df.format(orderDtl.goods_amount));
            if (orderDtl.shipping_fee > 0) {
                //有运费
                holder.iv_freight.setVisibility(View.GONE);
            } else {

                if (orderDtl.free_freight > 0 && orderDtl.goods_amount >= orderDtl.free_freight) {
                    //免运费
                    holder.iv_freight.setVisibility(View.VISIBLE);
                    holder.iv_freight.setText(String.format(mContext.getString(R.string.order_detail_item_baoyou_hint), orderDtl.free_freight));

                } else {
                    holder.iv_freight.setVisibility(View.GONE);
                }
            }

            //店铺优惠金额
            holder.tv_store_free_money.setText(String.format(mContext.getString(R.string.order_detail_item_manjian_money), BaseFunc.truncDouble(orderDtl.store_promotion_total, 2)));

            holder.tvWeight.setVisibility(View.VISIBLE);
            holder.tvWeight.setText(orderDtl.getShipping_weight());

            /** 国内、国外区分*/
            if (orderDtl.country_source == 0) {
                holder.LTaxfee.setVisibility(View.GONE);
            } else {
                holder.LTaxfee.setVisibility(View.VISIBLE);
                holder.tv_taxfee.setText(df.format(orderDtl.duty_fee));
            }

            holder.tv_line.setVisibility(View.VISIBLE);
            holder.LPay.setVisibility(View.GONE);

            holder.tv_amount_use.setText(orderDtl.getPd_amountDesc());
            holder.tv_bonus_use.setText(orderDtl.getCoupon_amountDesc());

            holder.LBonusUseStatus.setVisibility(View.GONE);

            holder.tv_coupon_on_goods_amount.setText(df.format(orderDtl.coupon_on_goods_amount));
            holder.tv_coupon_off_goods_amount.setText(df.format(orderDtl.coupon_off_goods_amount));

            //红人店铺优惠显示
            if (orderDtl.showMicroShopSave()) {
                holder.LMicroShopSave.setVisibility(View.VISIBLE);
                holder.vMicroShopSave.setVisibility(View.VISIBLE);
                holder.tvMicroShopSaveDesc.setText(orderDtl.getMicroShoperSaveDesc());
                holder.tvMicroShopSaveMoney.setText(orderDtl.getMicroShoperSaveMoney());
            } else {
                holder.LMicroShopSave.setVisibility(View.GONE);
                holder.vMicroShopSave.setVisibility(View.GONE);
            }

        } else {
            /**
             * position == 1 && holder.getItemViewType() == FOOTER_SECTION
             * 底部布局 平台客服与联系卖家 + 申请退款
             */
            holder.tv_platform_service.setText(BaseFunc.fromHtml(mContext.getString(R.string.platform_service)));
            BaseFunc.setFont(holder.tv_platform_service);//平台客服
            holder.tv_platform_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.startQQChat(mContext, BaseVar.DEFQQKF);
                }
            });
            holder.tv_contact_seller.setText(BaseFunc.fromHtml(mContext.getString(R.string.contact_seller)));
            BaseFunc.setFont(holder.tv_contact_seller);//联系卖家
            holder.tv_contact_seller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseFunc.isValidUrl(orderDtl.store_baidusales)) {
                        BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, orderDtl.store_baidusales);
                    } else {
                        BaseFunc.showMsg(mContext, mContext.getString(R.string.seller_out_of_line));
                    }
                }
            });
            //申请退款只在待发货状态下显示
            if (orderDtl.order_state == 20) {
                holder.tv_apply_refund.setVisibility(View.VISIBLE);
                holder.tv_apply_refund.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderDtl.refund_enabled == 1) {

                            Intent intent = new Intent(mContext, ReturnGoodsActivity.class);
                            intent.putExtra("ORDER", orderDtl.order_id);
                            intent.putExtra("REC", "");
                            mContext.startActivity(intent);
                        } else {
                            BaseFunc.showSelfToast(mContext, mContext.getString(R.string.hint_unsupport_refund_import_goods));
                        }
                    }
                });
            } else {
                holder.tv_apply_refund.setVisibility(View.GONE);
            }
        }

    }

    class ViewHoder extends RecyclerView.ViewHolder {
        /**
         * ------------  NORMAL SECTION  ------------
         */
        public LinearLayout LIcon;
        public TextView tv_store;
        public TagFlowLayout goods_flow;
        public TextView tv_store_freight;
        public TextView tvWeight;
        public TextView iv_freight;
        public TextView tv_store_money;
        public TextView tv_store_free_money;

        public LinearLayout LTaxfee;
        public TextView tv_taxfee;

        public TextView tv_line;

        public LinearLayout LPay, LBonusUseStatus, ll_agreement_policy;
        public TextView tv_bonus_use, tv_amount_use;
        public TextView tv_coupon_on_goods_amount, tv_coupon_off_goods_amount;

        //红人店铺优惠显示
        public LinearLayout LMicroShopSave;
        public View vMicroShopSave;
        public TextView tvMicroShopSaveDesc, tvMicroShopSaveMoney;

        /**   ------------  NORMAL SECTION  ------------ */

        /**
         * ------------  FOOTER SECTION  ------------
         */
        public TextView tv_platform_service;
        public TextView tv_contact_seller;
        public TextView tv_apply_refund;

        /**
         * ------------  FOOTER SECTION  ------------
         */

        public ViewHoder(View itemView, int viewType) {
            super(itemView);
            if (viewType == NORMAL_SECTION) {
                //商品布局
                LIcon = (LinearLayout) itemView.findViewById(R.id.LIcon);
                tv_store = (TextView) itemView.findViewById(R.id.tv_store);
                goods_flow = (TagFlowLayout) itemView.findViewById(R.id.goods_flow);
                tv_store_freight = (TextView) itemView.findViewById(R.id.tv_store_freight);
                tv_store_money = (TextView) itemView.findViewById(R.id.tv_store_money);
                iv_freight = (TextView) itemView.findViewById(R.id.iv_freight);
                tv_store_free_money = (TextView) itemView.findViewById(R.id.tv_store_free_money);

                tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);

                LTaxfee = (LinearLayout) itemView.findViewById(R.id.LTaxfee);
                tv_taxfee = (TextView) itemView.findViewById(R.id.tv_taxfee);

                tv_line = (TextView) itemView.findViewById(R.id.tv_line);

                LPay = (LinearLayout) itemView.findViewById(R.id.LPay);

                tv_bonus_use = (TextView) itemView.findViewById(R.id.tv_bonus_use);
                tv_amount_use = (TextView) itemView.findViewById(R.id.tv_amount_use);

                LBonusUseStatus = (LinearLayout) itemView.findViewById(R.id.LBonusUseStatus);
                tv_coupon_on_goods_amount = (TextView) itemView.findViewById(R.id.tv_coupon_on_goods_amount);
                tv_coupon_off_goods_amount = (TextView) itemView.findViewById(R.id.tv_coupon_off_goods_amount);
                ll_agreement_policy = (LinearLayout) itemView.findViewById(R.id.ll_agreement_policy);


                LMicroShopSave = (LinearLayout) itemView.findViewById(R.id.LMicroShopSave);
                vMicroShopSave = itemView.findViewById(R.id.vMicroShopSave);
                tvMicroShopSaveDesc = (TextView) itemView.findViewById(R.id.tvMicroShopSaveDesc);
                tvMicroShopSaveMoney = (TextView) itemView.findViewById(R.id.tvMicroShopSaveMoney);
            } else {
                //底部布局
                tv_platform_service = (TextView) itemView.findViewById(R.id.tv_platform_service);
                tv_contact_seller = (TextView) itemView.findViewById(R.id.tv_contact_seller);
                tv_apply_refund = (TextView) itemView.findViewById(R.id.tv_apply_refund);
            }
        }
    }
}
