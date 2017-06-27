package com.fanglin.fenhong.microbuyer.common.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.fanglin.fenhong.microbuyer.base.listener.OrderCallBack;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BaseGoods;
import com.fanglin.fenhong.microbuyer.base.model.BundlingOrderEntity;
import com.fanglin.fenhong.microbuyer.base.model.OrderGoods;
import com.fanglin.fenhong.microbuyer.base.model.OrderList;
import com.fanglin.fenhong.microbuyer.buyer.adapter.RecycleImgAdapter;
import com.fanglin.fenhong.microbuyer.common.OrderActivity;
import com.fanglin.fenhong.microbuyer.common.OrderDtlActivity;
import com.fanglin.fenhong.microbuyer.common.OrderMergeActivity;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import com.google.gson.Gson;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/24-下午3:24.
 * 功能描述:按分区重构我的订单
 */
public class OrderSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder, RecyclerView.ViewHolder> {

    public static final int HEADER_NORMAL = 3;
    public static final int HEADER_MERGE = 4;//合并付款的时候不显示

    public static final int FOOTER_NORMAL = 5;
    public static final int FOOTER_MERGE = 6;//合并付款

    public static final int ITEM_NORMAL = 0;//普通商品
    public static final int ITEM_BUNDLING = 1;//套装
    public static final int ITEM_MERGESINGLE = 2;//合并付款中的单个店铺(订单)
    private DecimalFormat df;


    private Context mContext;
    private List<OrderList> list;

    public OrderSectionAdapter(Context c) {
        this.mContext = c;
        df = new DecimalFormat("¥#0.00");
    }

    public void setList(List<OrderList> list) {
        this.list = list;
    }

    public void addList(List<OrderList> list) {
        if (list == null) return;
        this.list.addAll(list);
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        boolean isMerge = getItem(section).isMerge();
        if (isMerge) {
            return ITEM_MERGESINGLE;
        } else {
            if (getItem(section).isOrderGoodsBundling(position)) {
                return ITEM_BUNDLING;
            } else {
                return ITEM_NORMAL;
            }
        }
    }

    @Override
    protected int getSectionHeaderViewType(int section) {
        boolean isMerge = getItem(section).isMerge();
        return isMerge ? HEADER_MERGE : HEADER_NORMAL;
    }

    @Override
    protected boolean isSectionHeaderViewType(int viewType) {
        return viewType == HEADER_NORMAL || viewType == HEADER_MERGE;
    }

    @Override
    protected int getSectionFooterViewType(int section) {
        boolean isMerge = getItem(section).isMerge();
        return isMerge ? FOOTER_MERGE : FOOTER_NORMAL;
    }

    @Override
    protected boolean isSectionFooterViewType(int viewType) {
        return viewType == FOOTER_NORMAL || viewType == FOOTER_MERGE;
    }

    @Override
    protected int getSectionCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public OrderList getItem(int position) {
        return list.get(position);
    }

    @Override
    protected int getItemCountForSection(int section) {
        boolean isMerge = getItem(section).isMerge();
        if (isMerge) {
            return getItem(section).getMergeCount();
        } else {
            return getItem(section).getOrderGoodsCount();
        }
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_NORMAL) {
            View view = View.inflate(mContext, R.layout.order_header_section, null);
            return new HeaderViewHolder(view);
        } else {
            View view = View.inflate(mContext, R.layout.order_header_section_none, null);
            return new HeaderNoneViewHolder(view);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_NORMAL) {
            View view = View.inflate(mContext, R.layout.order_footer_section, null);
            return new FooterViewHolder(view);
        } else {
            View view = View.inflate(mContext, R.layout.order_footer_merge_section, null);
            return new FooterMergeViewHolder(view);
        }

    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_BUNDLING) {
            //套装
            View view = View.inflate(mContext, R.layout.item_cartcheck_buildings, null);
            return new ItemBundlingViewHolder(view);
        } else if (viewType == ITEM_MERGESINGLE) {
            //合并付款
            View view = View.inflate(mContext, R.layout.order_mergeitem_section, null);
            return new ItemMergeViewHolder(view);
        } else {
            //普通商品
            View view = View.inflate(mContext, R.layout.item_order_goods, null);
            return new ItemNormalViewHolder(view);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerView.ViewHolder holder, final int section) {
        int viewType = getSectionHeaderViewType(section);
        if (viewType == HEADER_NORMAL) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tv_store.setText(getItem(section).store_name);
            headerViewHolder.tv_statu.setText(getItem(section).getOrderStateDesc());
            headerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(mContext, StoreActivity.class, getItem(section).store_id);
                }
            });
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, final int section) {
        int viewType = getSectionFooterViewType(section);
        if (viewType == FOOTER_NORMAL) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tv_order_lbl.setText(getItem(section).getCalculateLable(mContext));

            footerViewHolder.tv_goods_money.setText(String.valueOf(getItem(section).getPayMoney()));
            footerViewHolder.tv_goods_num.setText(String.format(mContext.getString(R.string.count_num), getItem(section).goods_num));
            footerViewHolder.tv_goods_freight.setText(String.format(mContext.getString(R.string.freight_all), getItem(section).shipping_fee));

            /**按钮的处理*/
            parseButton(section, footerViewHolder.tv_contact, footerViewHolder.tv_cancel, footerViewHolder.tv_topay, footerViewHolder.tvRed);
        }

        if (viewType == FOOTER_MERGE) {
            FooterMergeViewHolder mergeViewHolder = (FooterMergeViewHolder) holder;
            mergeViewHolder.tvMoney.setText(String.valueOf(getItem(section).all_real_amount));
            if (getItem(section).validity_pay_time == 0) {
                mergeViewHolder.tvRed.setEnabled(false);
            } else {
                mergeViewHolder.tvRed.setEnabled(true);
            }

            mergeViewHolder.tvRed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mcb != null) {
                        mcb.onPay(section);
                    }
                }
            });
        }

    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int section, int position) {
        int viewType = getSectionItemViewType(section, position);
        if (viewType == ITEM_NORMAL) {
            final OrderGoods goods = getItem(section).extend_order_goods.get(position);
            ItemNormalViewHolder normalViewHolder = (ItemNormalViewHolder) holder;
            //普通商品
            normalViewHolder.view_separator.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

            if (TextUtils.equals(goods.goods_type, "5")) {
                normalViewHolder.tv_mansong_desc.setVisibility(View.VISIBLE);
                normalViewHolder.tv_activity_desc.setVisibility(View.GONE);//隐藏限时折扣提示
                normalViewHolder.tv_subtitle.setVisibility(View.GONE);//隐藏副标题
            } else if (TextUtils.equals(goods.goods_type, "3")) {
                //限时折扣提示
                normalViewHolder.tv_activity_desc.setText(goods.xianshi == null ? "" : goods.xianshi.getLowerLimitText());
                normalViewHolder.tv_activity_desc.setVisibility(View.VISIBLE);
                normalViewHolder.tv_mansong_desc.setVisibility(View.GONE);//隐藏满送提示
                normalViewHolder.tv_subtitle.setVisibility(View.GONE);//隐藏副标题
            } else {
                normalViewHolder.tv_mansong_desc.setVisibility(View.GONE);//隐藏满送提示
                normalViewHolder.tv_activity_desc.setVisibility(View.GONE);//隐藏限时折扣提示
                normalViewHolder.tv_subtitle.setVisibility(View.VISIBLE);//没有优惠活动，显示商品规格
            }
            normalViewHolder.tv_service.setVisibility(View.GONE);
            new FHImageViewUtil(normalViewHolder.sdv).setImageURI(goods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
            String goods_spec = BaseGoods.parseSpec(goods.goods_spec);
            normalViewHolder.tv_title.setText(goods.goods_name);
            normalViewHolder.tv_subtitle.setText(goods_spec);//商品规格
            normalViewHolder.tv_price.setText(df.format(goods.goods_price));
            normalViewHolder.tv_memo.setText(String.format(mContext.getString(R.string.num_buy), goods.goods_num));

            normalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity4Result((Activity) mContext, OrderDtlActivity.class, getItem(section).order_id, OrderActivity.REQDTLCODE);
                }
            });
        }

        if (viewType == ITEM_BUNDLING) {
            BundlingOrderEntity bundling = getItem(section).extend_order_goods.get(position).bundling;
            String[] imgs = bundling.getImageStr();

            ItemBundlingViewHolder bundlingViewHolder = (ItemBundlingViewHolder) holder;
            //套装
            bundlingViewHolder.tv_title.setText(bundling.bl_name);
            bundlingViewHolder.tv_price.setText(df.format(bundling.bl_price));
            String fmt = mContext.getString(R.string.num_buy);
            String memo = String.format(fmt, bundling.bl_num);
            bundlingViewHolder.tv_memo.setText(memo);

            bundlingViewHolder.tvBundlingFlag.setVisibility(View.VISIBLE);
            if (bundling.getBLState() == 0) {
                //如果套装内有商品失效，则标题与价格置灰
                bundlingViewHolder.tv_title.setEnabled(false);
                bundlingViewHolder.tv_price.setEnabled(false);
                bundlingViewHolder.tvBundlingFlag.setEnabled(false);
            } else {
                bundlingViewHolder.tv_title.setEnabled(true);
                bundlingViewHolder.tv_price.setEnabled(true);
                bundlingViewHolder.tvBundlingFlag.setEnabled(true);
            }

            RecycleImgAdapter imgAdapter = new RecycleImgAdapter(mContext);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            bundlingViewHolder.rc_img.setLayoutManager(linearLayoutManager);
            imgAdapter.setList(imgs);
            bundlingViewHolder.rc_img.setAdapter(imgAdapter);

            bundlingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity4Result((Activity) mContext, OrderDtlActivity.class, getItem(section).order_id, OrderActivity.REQDTLCODE);
                }
            });
        }

        if (viewType == ITEM_MERGESINGLE) {
            final LayoutInflater mInflater = LayoutInflater.from(mContext);
            //合并付款
            final ItemMergeViewHolder mergeViewHolder = (ItemMergeViewHolder) holder;
            final OrderList orderList = getItem(section).getMergeOrder(position);

            if (position == 0) {
                mergeViewHolder.vTop.setVisibility(View.VISIBLE);
            } else {
                mergeViewHolder.vTop.setVisibility(View.GONE);
            }

            mergeViewHolder.tvStore.setText(orderList.store_name);
            mergeViewHolder.tvStatu.setText(orderList.getOrderStateDesc());

            mergeViewHolder.tv_order_lbl.setText(getItem(section).getCalculateLable(mContext));

            mergeViewHolder.tv_goods_money.setText(String.valueOf(orderList.real_amount));
            mergeViewHolder.tv_goods_num.setText(String.format(mContext.getString(R.string.count_num), orderList.goods_num));
            mergeViewHolder.tv_goods_freight.setText(String.format(mContext.getString(R.string.freight_all), orderList.shipping_fee));

            mergeViewHolder.goodsFlow.setAdapter(new TagAdapter(orderList.extend_order_goods) {
                @Override
                public View getView(FlowLayout parent, int index, Object o) {
                    View view;
                    OrderGoods goods = (OrderGoods) o;
                    if (goods.isBundling()) {
                        /*有优惠套装列表*/
                        BundlingOrderEntity bundling = goods.bundling;
                        String[] imgs = bundling.getImageStr();

                        view = mInflater.inflate(R.layout.item_cartcheck_buildings, mergeViewHolder.goodsFlow, false);

                        RecyclerView rc_img = (RecyclerView) view.findViewById(R.id.rc_img);
                        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                        TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                        TextView tv_icon = (TextView) view.findViewById(R.id.tv_icon);
                        TextView tvBundlingFlag = (TextView) view.findViewById(R.id.tvBundlingFlag);

                        tv_title.setText(bundling.bl_name);
                        tv_price.setText(df.format(bundling.bl_price));
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

                    } else {
                        /** 没有优惠套装列表*/
                        view = mInflater.inflate(R.layout.item_order_goods, mergeViewHolder.goodsFlow, false);

                        /**
                         * 为适配特殊机型，手动设置item_goods的宽度，使其能够正确的纵向绘制各个布局 —— lizhixin 20151202
                         */
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BaseFunc.getDisplayMetrics(mContext).widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
                        view.setLayoutParams(params);

                        View view_separator = view.findViewById(R.id.white_separator);//白色分隔线
                        ImageView sdv = (ImageView) view.findViewById(R.id.sdv);
                        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                        TextView tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
                        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                        TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                        TextView tv_service = (TextView) view.findViewById(R.id.tv_service);
                        TextView tv_mansong_desc = (TextView) view.findViewById(R.id.tv_mansong_desc);//满送提示语
                        TextView tv_activity_desc = (TextView) view.findViewById(R.id.tv_activity_desc);//限时折扣提示语

                        view_separator.setVisibility(index == 0 ? View.GONE : View.VISIBLE);

                        if (TextUtils.equals(goods.goods_type, "5")) {
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
                            tv_mansong_desc.setVisibility(View.GONE);//隐藏满送提示
                            tv_activity_desc.setVisibility(View.GONE);//隐藏限时折扣提示
                            tv_subtitle.setVisibility(View.VISIBLE);//没有优惠活动，显示商品规格
                        }

                        tv_service.setVisibility(View.GONE);

                        new FHImageViewUtil(sdv).setImageURI(goods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

                        String goods_spec = BaseGoods.parseSpec(goods.goods_spec);

                        tv_title.setText(goods.goods_name);
                        tv_subtitle.setText(goods_spec);//商品规格
                        tv_price.setText(df.format(goods.goods_price));
                        tv_memo.setText(String.format(mContext.getString(R.string.num_buy), goods.goods_num));
                    }

                    return view;
                }
            });

            mergeViewHolder.LStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(mContext, StoreActivity.class, orderList.store_id);
                }
            });
            mergeViewHolder.goodsFlow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity4Result((Activity) mContext, OrderMergeActivity.class, new Gson().toJson(orderList), OrderActivity.REQDTLCODE);
                }
            });
            mergeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity4Result((Activity) mContext, OrderMergeActivity.class, new Gson().toJson(orderList), OrderActivity.REQDTLCODE);
                }
            });

        }
    }


    /**
     *
     *
     *
     *
     *
     *  控
     *
     *
     *  件
     *
     *
     *  部
     *
     *
     *
     *  分
     *
     *
     *
     *
     */


    /**
     * 头部视图
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout LIcon;
        public TextView tv_store;
        public TextView tv_statu;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            LIcon = (LinearLayout) itemView.findViewById(R.id.LIcon);
            tv_store = (TextView) itemView.findViewById(R.id.tv_store);
            tv_statu = (TextView) itemView.findViewById(R.id.tv_statu);
            BaseFunc.setFont(LIcon);
        }
    }

    public class HeaderNoneViewHolder extends RecyclerView.ViewHolder {
        public HeaderNoneViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 分区底部视图
     */
    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_goods_num;
        public TextView tv_goods_money;
        public TextView tv_order_lbl;
        public TextView tv_goods_freight;

        //按钮
        public TextView tv_cancel;
        public TextView tv_topay;
        public TextView tv_contact;
        public TextView tvRed;

        public FooterViewHolder(View itemView) {
            super(itemView);

            tv_goods_num = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tv_goods_money = (TextView) itemView.findViewById(R.id.tv_goods_money);
            tv_order_lbl = (TextView) itemView.findViewById(R.id.tv_order_lbl);
            tv_goods_freight = (TextView) itemView.findViewById(R.id.tv_goods_freight);

            tv_cancel = (TextView) itemView.findViewById(R.id.tv_cancel);
            tv_topay = (TextView) itemView.findViewById(R.id.tv_topay);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_contact);
            tvRed = (TextView) itemView.findViewById(R.id.tvRed);
        }
    }

    public class FooterMergeViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMoney;
        public TextView tvRed;

        public FooterMergeViewHolder(View itemView) {
            super(itemView);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
            tvRed = (TextView) itemView.findViewById(R.id.tvRed);
        }
    }

    /**
     * 普通商品ViewHolder
     * R.layout.item_order_goods
     */
    public class ItemNormalViewHolder extends RecyclerView.ViewHolder {
        View view_separator;
        ImageView sdv;
        TextView tv_title;
        TextView tv_subtitle;
        TextView tv_price;
        TextView tv_memo;
        TextView tv_service;
        TextView tv_mansong_desc;
        TextView tv_activity_desc;

        public ItemNormalViewHolder(View itemView) {
            super(itemView);
            view_separator = itemView.findViewById(R.id.white_separator);//白色分隔线
            sdv = (ImageView) itemView.findViewById(R.id.sdv);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_memo = (TextView) itemView.findViewById(R.id.tv_memo);
            tv_service = (TextView) itemView.findViewById(R.id.tv_service);
            tv_mansong_desc = (TextView) itemView.findViewById(R.id.tv_mansong_desc);//满送提示语
            tv_activity_desc = (TextView) itemView.findViewById(R.id.tv_activity_desc);//限时折扣提示语
        }
    }

    /**
     * 套装ViewHolder
     * R.layout.item_cartcheck_buildings
     */
    public class ItemBundlingViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rc_img;
        TextView tv_title;
        TextView tv_price;
        TextView tv_memo;
        TextView tv_icon;
        TextView tvBundlingFlag;

        public ItemBundlingViewHolder(View itemView) {
            super(itemView);
            rc_img = (RecyclerView) itemView.findViewById(R.id.rc_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_memo = (TextView) itemView.findViewById(R.id.tv_memo);
            tv_icon = (TextView) itemView.findViewById(R.id.tv_icon);
            tvBundlingFlag = (TextView) itemView.findViewById(R.id.tvBundlingFlag);
            BaseFunc.setFont(tv_icon);
        }
    }

    /**
     * 合并付款时的视图
     */
    public class ItemMergeViewHolder extends RecyclerView.ViewHolder {
        public View vTop;
        public LinearLayout LStore;
        public TextView tvIcon;
        public TextView tvStore;
        public TextView tvStatu;
        public TagFlowLayout goodsFlow;

        public TextView tv_order_lbl;
        public TextView tv_goods_num;
        public TextView tv_goods_money;
        public TextView tv_goods_freight;

        public ItemMergeViewHolder(View itemView) {
            super(itemView);
            vTop = itemView.findViewById(R.id.vTop);
            LStore = (LinearLayout) itemView.findViewById(R.id.LStore);
            tvIcon = (TextView) itemView.findViewById(R.id.tvIcon);
            tvStore = (TextView) itemView.findViewById(R.id.tvStore);
            tvStatu = (TextView) itemView.findViewById(R.id.tvStatu);
            goodsFlow = (TagFlowLayout) itemView.findViewById(R.id.goodsFlow);

            tv_order_lbl = (TextView) itemView.findViewById(R.id.tv_order_lbl);
            tv_goods_num = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tv_goods_money = (TextView) itemView.findViewById(R.id.tv_goods_money);
            tv_goods_freight = (TextView) itemView.findViewById(R.id.tv_goods_freight);

            BaseFunc.setFont(tvIcon);
        }
    }

    private OrderCallBack mcb;

    public void setCallBack(OrderCallBack cb) {
        this.mcb = cb;
    }


    /**
     * 根据订单状态,处理三个按钮的业务逻辑
     * 需要优化
     */
    private void parseButton(final int section, TextView btn_left, TextView btn_0, TextView btn_1, TextView tvRed) {
        int order_state = getItem(section).order_state;
        long _validity_pay_time = getItem(section).validity_pay_time;
        String _evaluation_state = getItem(section).evaluation_state;
        switch (order_state) {
            case 0://已取消
                tvRed.setVisibility(View.GONE);
                btn_left.setVisibility(View.GONE);
                btn_0.setVisibility(View.VISIBLE);
                btn_0.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                btn_1.setVisibility(View.GONE);
                btn_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mcb != null) {
                            mcb.onDelete(section);
                        }
                    }
                });
                break;
            case 10://未付款
                tvRed.setVisibility(View.VISIBLE);
                btn_left.setVisibility(View.GONE);
                btn_0.setVisibility(View.GONE);//在列表页暂时不显示取消订单
                btn_0.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                btn_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mcb != null) {
                            mcb.onCancel(section);
                        }
                    }
                });

                btn_1.setVisibility(View.GONE);
                //付款按钮样式改成与合并付款样式一致
                tvRed.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                //如果未付款的订单是过期的
                if (getItem(section).validity_pay_time == 0) {
                    tvRed.setEnabled(false);
                } else {
                    tvRed.setEnabled(true);
                }

                tvRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (list.get(section).getOrderGoodsState() == 0) {
                            //存在失效商品
                            BaseFunc.showMsg(mContext, mContext.getString(R.string.order_checkout_valid));

                        } else {
                            if (mcb != null) {
                                mcb.onPay(section);
                            }
                        }
                    }
                });
                break;
            case 20://已付款
                tvRed.setVisibility(View.GONE);
                btn_left.setVisibility(View.GONE);
                btn_0.setVisibility(View.GONE);
                btn_0.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                btn_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //在列表暂不显示 退款相关的按钮 只有进入详情才可以 退款
                    }
                });

                btn_1.setVisibility(View.VISIBLE);
                btn_1.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                btn_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mcb != null) mcb.onDelivery(section);
                    }
                });
                break;
            case 30://已发货
                tvRed.setVisibility(View.GONE);
                btn_left.setVisibility(View.GONE);
                btn_0.setVisibility(View.VISIBLE);
                btn_1.setVisibility(View.VISIBLE);
                btn_0.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                btn_1.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                btn_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mcb != null) mcb.onDelivery(section);
                    }
                });
                btn_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mcb != null) mcb.onSubmit(section);
                    }
                });
                break;
            case 40://已收货
                tvRed.setVisibility(View.GONE);
                btn_left.setVisibility(View.VISIBLE);
                btn_0.setVisibility(View.VISIBLE);
                btn_1.setVisibility(View.VISIBLE);
                btn_left.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[0]);
                btn_0.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                btn_1.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                btn_0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mcb != null) mcb.onDelivery(section);
                    }
                });
                btn_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mcb.onDelete(section);//删除订单
                    }
                });
                if (TextUtils.equals("0", getItem(section).evaluation_state)) {
                    btn_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mcb != null) mcb.onEvaluate(section, 0);//去评价
                        }
                    });
                } else if (TextUtils.equals("1", getItem(section).evaluation_state)) {
                    btn_1.setBackgroundResource(R.drawable.shape_gray66_stroke_corner);
                    btn_1.setTextColor(mContext.getResources().getColor(R.color.color_66));
                    btn_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mcb != null) mcb.onEvaluate(section, 1);//追加评价
                        }
                    });
                } else {
                    btn_1.setVisibility(View.GONE);
                }
                break;
            case 50://已失败
                tvRed.setVisibility(View.GONE);
                btn_left.setVisibility(View.GONE);
                btn_0.setVisibility(View.GONE);
                btn_0.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[1]);
                btn_1.setText(OrderList.getOrderStateDesc(order_state, _validity_pay_time, _evaluation_state)[2]);
                btn_1.setVisibility(View.VISIBLE);
                btn_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.startQQChat(mContext, BaseVar.DEFQQKF);
                    }
                });
                break;
        }
    }

}
