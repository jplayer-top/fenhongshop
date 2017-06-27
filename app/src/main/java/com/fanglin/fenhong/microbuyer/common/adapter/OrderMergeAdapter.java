package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BaseGoods;
import com.fanglin.fenhong.microbuyer.base.model.BundlingOrderEntity;
import com.fanglin.fenhong.microbuyer.base.model.OrderGoods;
import com.fanglin.fenhong.microbuyer.base.model.OrderList;
import com.fanglin.fenhong.microbuyer.buyer.TaozhuangActivity;
import com.fanglin.fenhong.microbuyer.buyer.adapter.RecycleImgAdapter;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.FHExpressActivity;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fhlib.other.FHLib;
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
public class OrderMergeAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderList> list;

    public OrderMergeAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<OrderList> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public OrderList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_ordermerge, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        BaseFunc.setFont(holder.LIcon);

        final OrderList orderList=getItem(position);

        holder.tv_store.setText(orderList.store_name);
        holder.tv_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String store_id = list.get(position).store_id;
                if (store_id == null) return;
                BaseFunc.gotoActivity(mContext, StoreActivity.class, store_id);
            }
        });
        holder.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcb != null) mcb.onCancel(position);
            }
        });

        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        holder.goods_flow.setAdapter(new TagAdapter(orderList.extend_order_goods) {
            @Override
            public View getView(FlowLayout parent, int index, Object o) {
                final OrderGoods agoods = (OrderGoods) o;
                View view;
                if (agoods.bundling != null && agoods.bundling.getImageStr() != null && agoods.bundling.getImageStr().length > 0) {
                    /** 有优惠套装列表*/
                    final BundlingOrderEntity bundling = agoods.bundling;
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
                    Spanned actDesc = agoods.getBundlingActivityDesc(mContext);
                    if (actDesc != null) {
                        tv_bonus_desc.setVisibility(View.VISIBLE);
                        tv_bonus_desc.setText(actDesc);
                    } else {
                        tv_bonus_desc.setVisibility(View.GONE);
                    }

                } else {

                    view = mInflater.inflate(R.layout.item_order_goods, holder.goods_flow, false);

                    /**
                     * 为适配特殊机型，手动设置item_goods的宽度，使其能够正确的纵向绘制各个布局 —— lizhixin 20151202
                     */
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BaseFunc.getDisplayMetrics(mContext).widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
                    view.setLayoutParams(params);

                    ImageView sdv = (ImageView) view.findViewById(R.id.sdv);
                    TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                    TextView tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
                    TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                    TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                    TextView tv_service = (TextView) view.findViewById(R.id.tv_service);
                    TextView tv_activity_desc = (TextView) view.findViewById(R.id.tv_activity_desc);
                    TextView tv_mansong_desc = (TextView) view.findViewById(R.id.tv_mansong_desc);
                    TextView tv_bonus_desc = (TextView) view.findViewById(R.id.tv_bonus_desc);

                    tv_title.setText(agoods.goods_name);
                    tv_price.setText("¥" + agoods.goods_price);
                    String fmt = mContext.getString(R.string.num_buy);
                    String memo = String.format(fmt, agoods.goods_num);
                    tv_memo.setText(memo);
                    tv_subtitle.setText(BaseGoods.parseSpec(agoods.goods_spec));

                    if (TextUtils.equals(agoods.goods_type, "5")) {
                        tv_mansong_desc.setVisibility(View.VISIBLE);
                        tv_activity_desc.setVisibility(View.GONE);//隐藏限时折扣提示
                        tv_subtitle.setVisibility(View.GONE);//隐藏副标题
                    } else if (TextUtils.equals(agoods.goods_type, "3")) {
                        //限时折扣提示
                        tv_activity_desc.setText(agoods.xianshi == null ? "" : agoods.xianshi.getLowerLimitText());
                        tv_activity_desc.setVisibility(View.VISIBLE);
                        tv_mansong_desc.setVisibility(View.GONE);//隐藏满送提示
                        tv_subtitle.setVisibility(View.GONE);//隐藏副标题
                    } else {
                        tv_mansong_desc.setVisibility(View.GONE);
                        tv_activity_desc.setVisibility(View.GONE);
                        tv_subtitle.setVisibility(View.VISIBLE);//显示商品规格
                    }

                    tv_service.setVisibility(agoods.ifRefund(list.get(position).order_state) ? View.VISIBLE : View.GONE);

                    new FHImageViewUtil(sdv).setImageURI(agoods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoGoodsDetail(mContext, agoods.goods_id, null, null);
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
                }

                return view;
            }
        });

        DecimalFormat df = new DecimalFormat("#0.00");
        String fmt_order = mContext.getString(R.string.fmt_order_id);
        String order_id = String.format(fmt_order, orderList.order_sn);
        holder.tv_order_id.setText(order_id);

        String store_freight = "¥" + df.format(orderList.shipping_fee);
        holder.tv_all_freight.setText(store_freight);//单店铺运费

        holder.tvWeight.setVisibility(View.VISIBLE);
        holder.tvWeight.setText(orderList.getShipping_weight());

        String fmt_store_money = mContext.getString(R.string.count_ordermoney);
        String store_money = String.format(fmt_store_money, df.format(orderList.real_amount));
        holder.tv_order_money.setText(store_money);
        holder.tv_order_time.setText(FHLib.getTimeStrByTimestamp(orderList.add_time));

        String fmt_count_num = mContext.getString(R.string.count_num);
        String all_goods_num = String.format(fmt_count_num, orderList.all_goods_num);
        holder.tv_all_goods_num.setText(all_goods_num);

        String all_goods_amount_str = "¥" + df.format(list.get(position).all_goods_amount);
        holder.tv_all_goods_amount.setText(all_goods_amount_str);//总金额
        String freight_total = "¥" + df.format(orderList.all_goods_shipping_fee);
        holder.tv_freight_total.setText(freight_total);//总运费

        //包邮提示
        if (list.get(position).shipping_fee > 0) {
            //有运费
            holder.tv_freight_money.setVisibility(View.GONE);
        } else {
            if (list.get(position).free_freight > 0 && list.get(position).all_goods_amount >= list.get(position).free_freight) {
                //免运费
                holder.tv_freight_money.setVisibility(View.VISIBLE);
                holder.tv_freight_money.setText(String.format(mContext.getString(R.string.order_detail_item_baoyou_hint), list.get(position).free_freight));
            } else {
                holder.tv_freight_money.setVisibility(View.GONE);
            }
        }
        //店铺优惠金额 满减金额
        if (list.get(position).mansong != null) {
            holder.tv_store_free_money.setText(String.format(mContext.getString(R.string.order_detail_item_manjian_money), BaseFunc.truncDouble(list.get(position).mansong.getMinus_amount(), 2)));
        } else {
            holder.tv_store_free_money.setText(mContext.getString(R.string.minus_zero));
        }

        if (orderList.order_state == 10) {
            holder.tv_cancel.setVisibility(View.VISIBLE);
        } else {
            holder.tv_cancel.setVisibility(View.GONE);
        }

        if (orderList.order_state == 20) {
            holder.tv_delivery.setVisibility(View.VISIBLE);
        } else {
            holder.tv_delivery.setVisibility(View.GONE);
        }

        holder.tv_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(mContext, FHExpressActivity.class, orderList.order_id);
            }
        });

        if (position == getCount() - 1) {
            holder.LAll.setVisibility(View.VISIBLE);
        } else {
            holder.LAll.setVisibility(View.GONE);
        }

        holder.LPayCal.setVisibility(View.VISIBLE);
        holder.tv_bonus_use.setText(orderList.getCoupon_amountDesc());
        holder.tv_amount_use.setText(orderList.getPd_amountDesc());

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
                if (BaseFunc.isValidUrl(orderList.store_baidusales)) {
                    BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, orderList.store_baidusales);
                } else {
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.seller_out_of_line));
                }
            }
        });

        //红人店铺优惠显示
        if (orderList.showMicroShopSave()) {
            holder.LMicroShopSave.setVisibility(View.VISIBLE);
            holder.vMicroShopSave.setVisibility(View.VISIBLE);
            holder.tvMicroShopSaveDesc.setText(orderList.getMicroShoperSaveDesc());
            holder.tvMicroShopSaveMoney.setText(orderList.getMicroShoperSaveMoney());
        } else {
            holder.LMicroShopSave.setVisibility(View.GONE);
            holder.vMicroShopSave.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        public LinearLayout LIcon;
        public TextView tv_store;
        public TextView tv_cancel;
        public TextView tv_delivery;
        public TagFlowLayout goods_flow;

        public TextView tv_order_id;
        public TextView tvWeight;
        public TextView tv_order_money, tv_order_time;

        public TextView tv_all_goods_num;
        public TextView tv_all_goods_amount;
        public TextView tv_freight_total;//总运费
        public TextView tv_all_freight;
        public TextView tv_freight_money;//包邮提示
        public TextView tv_store_free_money;//店铺优惠金额

        public LinearLayout LAll;

        public LinearLayout LPayCal;
        public TextView tv_bonus_use, tv_amount_use;

        //红人店铺优惠显示
        public LinearLayout LMicroShopSave;
        public View vMicroShopSave;
        public TextView tvMicroShopSaveDesc, tvMicroShopSaveMoney;

        ///////////////////////////////////FOOTER SECTION/////////////////////////////
        public TextView tv_platform_service;
        public TextView tv_contact_seller;

        public ViewHolder(View itemView) {
            LIcon = (LinearLayout) itemView.findViewById(R.id.LIcon);
            tv_store = (TextView) itemView.findViewById(R.id.tv_store);
            tv_cancel = (TextView) itemView.findViewById(R.id.tv_cancel);
            tv_delivery = (TextView) itemView.findViewById(R.id.tv_delivery);
            goods_flow = (TagFlowLayout) itemView.findViewById(R.id.goods_flow);
            tv_order_id = (TextView) itemView.findViewById(R.id.tv_order_id);

            tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);

            tv_order_money = (TextView) itemView.findViewById(R.id.tv_order_money);
            tv_order_time = (TextView) itemView.findViewById(R.id.tv_order_time);

            tv_all_goods_num = (TextView) itemView.findViewById(R.id.tv_all_goods_num);
            tv_all_goods_amount = (TextView) itemView.findViewById(R.id.tv_all_goods_amount);
            tv_freight_total = (TextView) itemView.findViewById(R.id.tv_freight_total);//总运费
            tv_all_freight = (TextView) itemView.findViewById(R.id.tv_all_freight);
            tv_freight_money = (TextView) itemView.findViewById(R.id.tv_freight_money);

            tv_store_free_money = (TextView) itemView.findViewById(R.id.tv_store_free_money);

            LAll = (LinearLayout) itemView.findViewById(R.id.LAll);

            LPayCal = (LinearLayout) itemView.findViewById(R.id.LPayCal);
            tv_bonus_use = (TextView) itemView.findViewById(R.id.tv_bonus_use);
            tv_amount_use = (TextView) itemView.findViewById(R.id.tv_amount_use);

            LMicroShopSave = (LinearLayout) itemView.findViewById(R.id.LMicroShopSave);
            vMicroShopSave = itemView.findViewById(R.id.vMicroShopSave);
            tvMicroShopSaveDesc = (TextView) itemView.findViewById(R.id.tvMicroShopSaveDesc);
            tvMicroShopSaveMoney = (TextView) itemView.findViewById(R.id.tvMicroShopSaveMoney);

            //底部布局
            tv_platform_service = (TextView) itemView.findViewById(R.id.tv_platform_service);
            tv_contact_seller = (TextView) itemView.findViewById(R.id.tv_contact_seller);

            itemView.setTag(this);
        }
    }

    private OrderMergeAdapterCallBack mcb;

    public void setCallBack(OrderMergeAdapterCallBack cb) {
        this.mcb = cb;
    }

    public interface OrderMergeAdapterCallBack {
        void onCancel(int position);
    }
}
