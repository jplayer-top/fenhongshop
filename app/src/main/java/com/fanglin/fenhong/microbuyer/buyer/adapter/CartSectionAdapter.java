package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Bundlings;
import com.fanglin.fenhong.microbuyer.base.model.CalculateData;
import com.fanglin.fenhong.microbuyer.base.model.CartCheckEntity;
import com.fanglin.fenhong.microbuyer.base.model.Carts;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromXianshi;
import com.fanglin.fenhong.microbuyer.base.model.GoodsinCart;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.MiaoshaTag;
import com.fanglin.fenhong.microbuyer.base.model.RenxuanRule;
import com.fanglin.fenhong.microbuyer.base.model.SerializationCarts;
import com.fanglin.fenhong.microbuyer.buyer.LayoutActivityinCart;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 16-01-04.
 * 购物车第三次重构  按分区显示
 * modify by lizhixin on 2016/02/29
 */
public class CartSectionAdapter extends SectionedRecyclerViewAdapter<CartSectionAdapter.HeaderViewHolder, CartSectionAdapter.ItemViewHolder, CartSectionAdapter.FooterViewHolder> {

    public Context mContext;
    public List<Carts> list = new ArrayList<>();
    private boolean isChina = true;
    private SerializationCarts cartData;
    private DecimalFormat df;
    private ItemCheckedListener itemCheckedListener;
    private boolean isEditMode = false;//是否为编辑状态下

    public CartSectionAdapter(Context c) {
        this.mContext = c;
        df = new DecimalFormat("¥#0.00");
    }

    public void setItemCheckedListener(ItemCheckedListener itemCheckedListener) {
        this.itemCheckedListener = itemCheckedListener;
    }

    public GoodsinCart getItem(int section, int position) {
        return list.get(section).subitems.get(position);
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public void setCartData(SerializationCarts cartData) {
        this.cartData = cartData;
    }

    public void setList(JSONObject json, boolean isChina) {
        this.isChina = isChina;
        if (json != null && json.length() > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                try {
                    String key = json.names().getString(i);
                    String val = json.getString(key);
                    List<GoodsinCart> goods = new Gson().fromJson(val, new TypeToken<List<GoodsinCart>>() {
                    }.getType());

                    Carts carts = new Carts();
                    carts.store_id = key;
                    carts.store_name = key;

                    carts.hasActivity = cartData.hasActivity(carts.store_id);
                    carts.storeFreeFreight = cartData.getFreeFreight(carts.store_id);
                    carts.mangSongRule = cartData.getMansong(carts.store_id);
                    carts.renxuanRule = cartData.getRenXuan(carts.store_id);

                    if (goods != null && goods.size() > 0) {

                        carts.store_name = goods.get(0).store_name;
                        carts.store_source = goods.get(0).goods_source;
                        for (int j = 0; j < goods.size(); j++) {
                            carts.goods_num += goods.get(j).goods_num;

                            //秒杀处理  初始化
                            goods.get(j).storageLimit = goods.get(j).goods_storage;
                            goods.get(j).priceForShow = goods.get(j).goods_price;
                        }
                    }
                    carts.subitems = goods;
                    list.add(carts);
                } catch (Exception e) {
                    FHLog.e("CartAdapter", String.valueOf(e.getMessage()));
                }
            }
        } else {
            list = null;
        }
    }

    /**
     * 是否合法商品
     */
    public boolean isValid() {
        if (list == null || list.size() == 0) return false;
        int c = list.size();

        for (int i = 0; i < c; i++) {
            List<GoodsinCart> goods = list.get(i).subitems;
            if (goods != null) {
                for (int j = 0; j < goods.size(); j++) {
                    if (goods.get(j).normalSelected) {
                        boolean f = goods.get(j).isStateNormal();
                        if (!f) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 针对单个店铺的全选功能
     */
    public void SelectOne(int position, boolean flag) {
        if (list == null || list.size() == 0) return;
        if (list.get(position).subitems == null) return;

        for (int i = 0; i < list.get(position).subitems.size(); i++) {
            if (isEditMode) {
                list.get(position).subitems.get(i).editSelected = flag;
            } else {
                list.get(position).subitems.get(i).normalSelected = flag && list.get(position).subitems.get(i).isStateNormal();
            }
        }
    }

    /**
     * 对所有店铺的所有商品进行选择
     */
    public void selectAll(boolean flag) {
        if (list == null || list.size() == 0) return;
        int listsize = list.size();
        for (int i = 0; i < listsize; i++) {
            if (list.get(i).subitems != null) {
                int subsize = list.get(i).subitems.size();
                for (int j = 0; j < subsize; j++) {
                    if (isEditMode) {
                        list.get(i).subitems.get(j).editSelected = flag;
                    } else {
                        list.get(i).subitems.get(j).normalSelected = flag && list.get(i).subitems.get(j).isStateNormal();
                    }
                }
            }
        }
    }

    /**
     * 重置编辑模式
     */
    public void clearEditSelected() {
        if (list == null || list.size() == 0) return;
        int listsize = list.size();
        for (int i = 0; i < listsize; i++) {
            if (list.get(i).subitems != null) {
                int subsize = list.get(i).subitems.size();
                for (int j = 0; j < subsize; j++) {
                    list.get(i).subitems.get(j).editSelected = false;
                }
            }
        }
    }

    @Override
    protected int getSectionCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        List<GoodsinCart> goods = list.get(section).subitems;
        if (goods == null) return 0;
        return goods.size();
    }

    @Override
    protected ItemViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_cartgoods, null);
        return new ItemViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(final ItemViewHolder holder, final int section, final int position) {

        //套装与单品均有这个决定是否失效
        final boolean normalState = getItem(section, position).isStateNormal();

        //商品名称+标签
        holder.tv_title.setText(getItem(section, position).getGoodsName(mContext));
        BaseFunc.setFont(holder.tv_title);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BaseFunc.getDisplayMetrics(mContext).widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        holder.itemView.setLayoutParams(params);

        holder.tv_price.setText(df.format(getItem(section, position).priceForShow));
        new FHImageViewUtil(holder.sdv).setImageURI(getItem(section, position).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

        //国旗及描述行
        holder.tv_nation_desc.setText(getItem(section, position).goods_promise);
        new FHImageViewUtil(holder.iv_nation_flag).setImageURI(getItem(section, position).nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);

        //有效或失效，透明图片
        int pic_top = getItem(section, position).getGoodsSaleState();
        holder.iv_top.setImageResource(pic_top);
        if (pic_top == -1) {
            //有效
            holder.ll_price_and_count.setVisibility(View.VISIBLE);
            holder.tv_subtitle.setVisibility(View.GONE);
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.color_33));
        } else {
            //商品失效
            holder.ll_price_and_count.setVisibility(View.GONE);
            holder.tv_subtitle.setVisibility(View.VISIBLE);
            holder.tv_subtitle.setText(getItem(section, position).getGoodsStateDes(mContext));
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.color_99));
        }

        //如果有 国旗与国家，正常显示
        if (!TextUtils.isEmpty(getItem(section, position).nation_name) && !TextUtils.isEmpty(getItem(section, position).nation_flag)) {
            holder.ll_nation.setVisibility(View.VISIBLE);

        } else {//如果没有查到国旗与国家，表明商品已被删除
            holder.ll_nation.setVisibility(View.GONE);
            //直接显示商品失效的提示，显示与隐藏的判断在上面进行
            holder.tv_subtitle.setText(mContext.getString(R.string.sale_invalid));
        }

        //秒杀标签  Added By Plucky
        refreshStorageAndPrice(section, position);

        holder.goodsCheckBox.setChecked(getItem(section, position).getIsSelected(isEditMode));
        /**
         *  只有非失效的商品 或者处于编辑状态下的商品 才显示goodsCheckBox
         */
        if (normalState || isEditMode) {
            holder.goodsCheckBox.setVisibility(View.VISIBLE);
        } else {
            holder.goodsCheckBox.setVisibility(View.INVISIBLE);
        }

        int goods_num = getItem(section, position).goods_num;
        holder.tv_buy_num.setText(String.valueOf(goods_num));
        holder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buy_num = Integer.valueOf(holder.tv_buy_num.getText().toString());
                buy_num--;
                if (buy_num < 1) {
                    return;
                }
                UpdateCart(section, position, v, buy_num, 0);

            }
        });
        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buy_num = Integer.valueOf(holder.tv_buy_num.getText().toString());
                buy_num++;
                UpdateCart(section, position, v, buy_num, 1);
            }
        });

        holder.goodsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Carts cartsItem = list.get(section);
                cartsItem.subitems.get(position).setSelected(isEditMode, holder.goodsCheckBox.isChecked());

                cartsItem.store_youhui = getRenxuanYouhui(section);

                notifyDataSetChanged();
                if (itemCheckedListener != null)
                    itemCheckedListener.onItemChecked();
            }
        });

        holder.sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoGoodsDetail(mContext, getItem(section, position).goods_id, null, null);
            }
        });

        holder.LClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoGoodsDetail(mContext, getItem(section, position).goods_id, null, null);
            }
        });

        if (position == getItemCountForSection(section) - 1) {
            holder.vline.setVisibility(View.GONE);
        } else {
            holder.vline.setVisibility(View.VISIBLE);
        }

        //Added By Plucky
        if (getItem(section, position).goods_source > 0) {
            holder.iv_flag_global.setVisibility(View.VISIBLE);
        } else {
            holder.iv_flag_global.setVisibility(View.GONE);
        }

        List<Bundlings> bundlings = getItem(section, position).bundlings;
        GoodsDtlPromXianshi xianshi = getItem(section, position).xianshi;
        if (xianshi != null) {
            if (getItem(section, position).goods_num >= xianshi.lower_limit) {
                holder.tv_price.setText(df.format(xianshi.price));
            } else {
                holder.tv_price.setText(df.format(xianshi.origin_price));
            }
        } else {
            //关于秒杀UI的调整
            holder.tv_price.setText(df.format(getItem(section, position).priceForShow));
        }

        if (bundlings != null && bundlings.size() > 0) {
            holder.ll_building_count.setVisibility(View.VISIBLE);
            holder.LNormal.setVisibility(View.GONE);
            holder.buildingCheckBox.setChecked(getItem(section, position).getIsSelected(isEditMode));
            holder.tv_building_price.setText(df.format(getItem(section, position).goods_price));
            holder.tv_building_buy_num.setText(String.valueOf(goods_num));
            holder.tv_building_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int buy_num = Integer.valueOf(holder.tv_building_buy_num.getText().toString());
                    buy_num--;
                    if (buy_num < 1) {
                        return;
                    }
                    UpdateCart(section, position, v, buy_num, 0);

                }
            });
            holder.tv_building_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int buy_num = Integer.valueOf(holder.tv_building_buy_num.getText().toString());
                    buy_num++;
                    UpdateCart(section, position, v, buy_num, 1);

                }
            });

            holder.buildingCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(section).subitems.get(position).setSelected(isEditMode, holder.buildingCheckBox.isChecked());
                    list.get(section).store_youhui = getRenxuanYouhui(section);
                    notifyDataSetChanged();
                    if (itemCheckedListener != null)
                        itemCheckedListener.onItemChecked();
                }
            });

            /** 套装失效状态控制*/
            if (normalState) {
                holder.tvBundlingFlag.setEnabled(true);

                holder.tv_building_price.setEnabled(true);
                holder.tv_building_minus.setEnabled(true);
                holder.tv_building_buy_num.setEnabled(true);
                holder.tv_building_plus.setEnabled(true);
            } else {
                holder.tvBundlingFlag.setEnabled(false);
                holder.tv_building_price.setEnabled(false);
                holder.tv_building_minus.setEnabled(false);
                holder.tv_building_buy_num.setEnabled(false);
                holder.tv_building_plus.setEnabled(false);
            }

            /**
             * 只有正常状态非失效的套装或者处于编辑状态下 buildingCheckBox才显示
             */
            if (normalState || isEditMode) {
                holder.buildingCheckBox.setVisibility(View.VISIBLE);
            } else {
                holder.buildingCheckBox.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.ll_building_count.setVisibility(View.GONE);
            holder.LNormal.setVisibility(View.VISIBLE);
        }

        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        holder.goods_flow.setAdapter(new TagAdapter(bundlings) {
            @Override
            public View getView(FlowLayout parent, int index, Object o) {
                final Bundlings goods = (Bundlings) o;
                View view = mInflater.inflate(R.layout.item_building_goods, holder.goods_flow, false);

                /**
                 * 为适配特殊机型，手动设置item_goods的宽度，使其能够正确的纵向绘制各个布局 —— lizhixin 20151202
                 */
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BaseFunc.getDisplayMetrics(mContext).widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);

                ImageView ivNationFlag = (ImageView) view.findViewById(R.id.ivNationFlag);
                TextView tvNationDesc = (TextView) view.findViewById(R.id.tvNationDesc);

                ImageView sdv = (ImageView) view.findViewById(R.id.sdv);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                TextView tv_bonus_desc = (TextView) view.findViewById(R.id.tv_bonus_desc);

                new FHImageViewUtil(ivNationFlag).setImageURI(goods.nation_flag, FHImageViewUtil.SHOWTYPE.BANNER);
                tvNationDesc.setText(goods.goods_promise);

                new FHImageViewUtil(sdv).setImageURI(goods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
                tv_title.setText(goods.goods_name);
                tv_memo.setText("");
                tv_price.setText(df.format(goods.goods_price));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoGoodsDetail(mContext, goods.goods_id, null, null);
                    }
                });

                if (normalState) {
                    tv_title.setEnabled(true);
                    tv_price.setEnabled(true);
                    tv_memo.setEnabled(true);
                } else {
                    tv_title.setEnabled(false);
                    tv_price.setEnabled(false);
                    tv_memo.setEnabled(false);
                }

                BaseFunc.setFont(tv_bonus_desc);
                String actDesc = goods.getActivityDesc(mContext);
                if (actDesc != null) {
                    tv_bonus_desc.setVisibility(View.VISIBLE);
                    tv_bonus_desc.setText(actDesc);
                } else {
                    tv_bonus_desc.setVisibility(View.GONE);
                }

                return view;
            }
        });

        BaseFunc.setFont(holder.tv_bonus_desc);
        String actDesc = getItem(section, position).getActivityDesc(mContext);
        if (actDesc != null) {
            holder.tv_bonus_desc.setVisibility(View.VISIBLE);
            holder.tv_bonus_desc.setText(actDesc);
        } else {
            holder.tv_bonus_desc.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderViewHolder viewHolder, final int i) {
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectOne(i, viewHolder.cb.isChecked());
                list.get(i).store_youhui = getRenxuanYouhui(i);
                notifyDataSetChanged();
                if (itemCheckedListener != null)
                    itemCheckedListener.onItemChecked();
            }
        });

        if (i == 0) {
            viewHolder.vStoreTop.setVisibility(View.GONE);
        } else {
            viewHolder.vStoreTop.setVisibility(View.VISIBLE);
        }

        viewHolder.tv_title.setText(list.get(i).store_name);
        CalculateData cd = getCalculate(i);
        cd.store_id = list.get(i).store_id;
        viewHolder.cb.setChecked(cd.count_selected_id == cd.count_all_id);
        viewHolder.cb.setVisibility(isChina ? View.VISIBLE : View.GONE);

        viewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String store_id = list.get(i).store_id;
                if (store_id == null) return;
                BaseFunc.gotoActivity(mContext, StoreActivity.class, store_id);
            }
        });

        LayoutActivityinCart layoutActivityinCart = new LayoutActivityinCart(mContext);
        layoutActivityinCart.setData(cd, list.get(i), isEditMode);

        viewHolder.LActivity.removeAllViews();
        viewHolder.LActivity.addView(layoutActivityinCart.getView());
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_cartsection_header, null);
        return new HeaderViewHolder(view);
    }

    @Override
    protected boolean hasFooterInSection(int i) {
        return !isChina;
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_cartsection_footer, null);
        return new FooterViewHolder(view);
    }

    @Override
    protected void onBindSectionFooterViewHolder(final FooterViewHolder footerViewHolder, final int i) {
        BaseFunc.setFont(footerViewHolder.tv_msg);
        /** 結算單元的邏輯*/
        final CalculateData cd = getCalculate(i);

        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footerViewHolder.cb_all.setSelected(!footerViewHolder.cb_all.isSelected());

                if (footerViewHolder.cb_all.isSelected()) {
                    //如果是全选
                    selectAll(false);
                    SelectOne(i, true);
                    list.get(i).store_youhui = getRenxuanYouhui(i);

                    notifyDataSetChanged();
                    if (itemCheckedListener != null)
                        itemCheckedListener.onItemChecked();
                } else {
                    //如果是反选
                    SelectOne(i, false);
                    list.get(i).store_youhui = getRenxuanYouhui(i);

                    notifyDataSetChanged();
                    if (itemCheckedListener != null)
                        itemCheckedListener.onItemChecked();
                }

            }
        };

        double purchaseLimit = cartData.getStoreLimit(list.get(i).store_id);
        String alimit = df.format(purchaseLimit);
        footerViewHolder.tv_msg.setText(String.format(mContext.getString(R.string.hint_seatax), alimit, alimit));
        if (cd.money > purchaseLimit && purchaseLimit > 0) {
            footerViewHolder.tv_msg.setVisibility(View.VISIBLE);
            footerViewHolder.tv_calculate.setEnabled(false);
        } else {
            footerViewHolder.tv_msg.setVisibility(View.GONE);
            /** 控制结算按钮是否能点击*/
            if (cd.num > 0) {
                footerViewHolder.tv_calculate.setEnabled(true);
            } else {
                footerViewHolder.tv_calculate.setEnabled(false);
            }
        }

        footerViewHolder.tv_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.num == 0) return;
                if (isValid()) {
                    CartCheckEntity entity = new CartCheckEntity();
                    entity.setGoodsIdNum(cd.selected_id_num);
                    entity.setGoodsSource(1);
                    entity.setIfCart(1);
                    BaseFunc.gotoCartCheckActivity((BaseFragmentActivity) mContext, entity, 0);

                } else {
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.sale_invalid_tips));
                }

            }
        });

        footerViewHolder.LChk.setOnClickListener(l);

        footerViewHolder.tv_goods_fee.setText(df.format(cd.money));
        footerViewHolder.tv_activityfee.setText(df.format(0));
        footerViewHolder.tv_taxfee.setText(df.format(cd.taxfee));
        String goods_pay_cal;

        goods_pay_cal = String.format(mContext.getString(R.string.fmt_cart_footer_money), df.format(cd.money + cd.taxfee));
        footerViewHolder.tv_taxfee.getPaint().setFlags(0);
        footerViewHolder.tv_money.setText(BaseFunc.fromHtml(goods_pay_cal));

        if (cd.num > 99) {
            footerViewHolder.tv_calculate.setText(String.format(mContext.getString(R.string.fmt_pay_cart), "99+"));
        } else {
            footerViewHolder.tv_calculate.setText(String.format(mContext.getString(R.string.fmt_pay_cart), String.valueOf(cd.num)));
        }

        /**
         * 按钮的全选状态取决于子集
         */
        footerViewHolder.cb_all.setSelected(cd.count_selected_id == list.get(i).subitems.size());

        /**
         * 编辑状态下结算按钮不应该显示
         */
        footerViewHolder.tv_calculate.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout LNormal;
        CheckBox goodsCheckBox;
        ImageView sdv;
        LinearLayout LClick;
        TextView tv_title, tv_subtitle;
        TextView tv_price;
        TextView tv_minus;
        TextView tv_buy_num;
        TextView tv_plus;
        View vline;
        LinearLayout ll_nation;
        ImageView iv_nation_flag;//国旗图标
        TextView tv_nation_desc;//国旗旁边的描述
        ImageView iv_flag_global;//商品图片左上角的全球购标志
        ImageView iv_top;//已售罄、已下架等透明图片
        LinearLayout ll_price_and_count;//价格与数量

        //优惠套装时显示
        LinearLayout ll_building_count;
        CheckBox buildingCheckBox;
        TextView tv_building_price;
        TextView tv_building_minus;
        TextView tv_building_buy_num;
        TextView tv_building_plus;
        TextView tvBundlingFlag;
        TagFlowLayout goods_flow;
        TextView tv_bonus_desc;


        public ItemViewHolder(View itemView) {
            super(itemView);

            LNormal = (LinearLayout) itemView.findViewById(R.id.LNormal);

            goodsCheckBox = (CheckBox) itemView.findViewById(R.id.cb);
            LClick = (LinearLayout) itemView.findViewById(R.id.LClick);
            sdv = (ImageView) itemView.findViewById(R.id.sdv);

            ll_nation = (LinearLayout) itemView.findViewById(R.id.ll_nation);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_minus = (TextView) itemView.findViewById(R.id.tv_minus);
            tv_buy_num = (TextView) itemView.findViewById(R.id.tv_buy_num);
            tv_plus = (TextView) itemView.findViewById(R.id.tv_plus);
            vline = itemView.findViewById(R.id.vline);
            iv_nation_flag = (ImageView) itemView.findViewById(R.id.iv_nation_flag);
            tv_nation_desc = (TextView) itemView.findViewById(R.id.tv_nation_desc);
            iv_flag_global = (ImageView) itemView.findViewById(R.id.iv_flag_global);
            iv_top = (ImageView) itemView.findViewById(R.id.iv_top);
            ll_price_and_count = (LinearLayout) itemView.findViewById(R.id.ll_price_and_count);

            /** 优惠套装*/
            ll_building_count = (LinearLayout) itemView.findViewById(R.id.ll_building_count);
            buildingCheckBox = (CheckBox) itemView.findViewById(R.id.building_cb);
            tv_building_price = (TextView) itemView.findViewById(R.id.tv_building_price);
            tv_building_minus = (TextView) itemView.findViewById(R.id.tv_building_minus);
            tv_building_buy_num = (TextView) itemView.findViewById(R.id.tv_building_buy_num);
            tv_building_plus = (TextView) itemView.findViewById(R.id.tv_building_plus);
            tvBundlingFlag = (TextView) itemView.findViewById(R.id.tvBundlingFlag);

            goods_flow = (TagFlowLayout) itemView.findViewById(R.id.goods_flow);

            tv_bonus_desc = (TextView) itemView.findViewById(R.id.tv_bonus_desc);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        View vStoreTop;
        CheckBox cb;
        TextView tv_title;
        LinearLayout LActivity;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            vStoreTop = itemView.findViewById(R.id.vStoreTop);
            cb = (CheckBox) itemView.findViewById(R.id.cb);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            LActivity = (LinearLayout) itemView.findViewById(R.id.LActivity);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        //结算单元
        TextView tv_goods_fee;
        TextView tv_activityfee;
        TextView tv_taxfee;
        TextView tv_msg;
        LinearLayout LChk;
        TextView cb_all;
        TextView tv_cb, tv_money, tv_calculate;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tv_goods_fee = (TextView) itemView.findViewById(R.id.tv_goods_fee);
            tv_activityfee = (TextView) itemView.findViewById(R.id.tv_activityfee);
            tv_taxfee = (TextView) itemView.findViewById(R.id.tv_taxfee);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
            LChk = (LinearLayout) itemView.findViewById(R.id.LChk);
            cb_all = (TextView) itemView.findViewById(R.id.cb_all);
            tv_cb = (TextView) itemView.findViewById(R.id.tv_cb);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            tv_calculate = (TextView) itemView.findViewById(R.id.tv_calculate);
        }
    }

    /**
     * @param position  p
     * @param index     i
     * @param clickView c
     * @param num       n
     * @param opType    操作类型 减 0 ， 加 1
     */
    public void UpdateCart(final int position, final int index, final View clickView, final int num, final int opType) {
        if (list == null) return;
        if (num == 0) return;
        final Member member = FHCache.getMember((Activity) clickView.getContext());
        if (member == null) return;
        GoodsinCart gic = list.get(position).subitems.get(index);
        clickView.setEnabled(false);
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                clickView.setEnabled(false);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                clickView.setEnabled(true);
                if (isSuccess) {
                    list.get(position).subitems.get(index).goods_num = num;
                    list.get(position).store_youhui = getRenxuanYouhui(position);

                    refreshStorageAndPrice(position, index);
                    notifyDataSetChanged();

                    FHApp.getInstance().sendCartActionByLocal(opType == 0 ? -1 : +1);
                }
            }

        }).update_cart(member.member_id, member.token, gic.cart_id, (isChina ? 0 : 1), num);
    }

    /**
     * position -1 表示对所有的进行统计 否则对其中一个分区进行统计
     * 77777
     */
    public CalculateData getCalculate(int position) {
        CalculateData cd = new CalculateData();
        cd.isEditMode = isEditMode;
        if (list != null && list.size() > 0) {
            StringBuilder sb_id = new StringBuilder();
            StringBuilder sb_id_num = new StringBuilder();
            if (position == -1) {
                for (int i = 0; i < list.size(); i++) {
                    List<GoodsinCart> goods = list.get(i).subitems;

                    if (goods != null && goods.size() > 0) {
                        cd.count_all_id += goods.size();
                        for (int j = 0; j < goods.size(); j++) {
                            GoodsinCart agoods = goods.get(j);
                            if (agoods.getIsSelected(isEditMode)) {

                                cd.count_selected_id++;
                                cd.money += agoods.goods_num * agoods.priceForShow;
                                cd.num += agoods.goods_num;
                                cd.taxfee += agoods.goods_num * agoods.priceForShow * agoods.hs_rate;

                                /**
                                 * 记录N元任选的商品数量 Added By Plucky 2016-08-18 16:38
                                 */
                                if (agoods.isNyuanRenxuan()) {
                                    cd.xnum += agoods.goods_num;
                                }

                                /** 记录id*/
                                sb_id.append(agoods.cart_id);
                                sb_id.append(",");

                                /** 记录id|num*/
                                sb_id_num.append(agoods.cart_id);
                                sb_id_num.append("|");
                                sb_id_num.append(agoods.goods_num);

                                sb_id_num.append(",");
                            }
                        }
                    }

                    /**
                     * 统计店铺优惠
                     * Added By Plucky
                     */
                    cd.youhui += list.get(i).store_youhui;
                }
            } else {
                List<GoodsinCart> goods = list.get(position).subitems;

                if (goods != null && goods.size() > 0) {
                    cd.count_all_id = goods.size();
                    for (int j = 0; j < goods.size(); j++) {
                        GoodsinCart agoods = goods.get(j);
                        if (agoods.getIsSelected(isEditMode)) {
                            cd.money += agoods.goods_num * agoods.priceForShow;
                            cd.num += agoods.goods_num;
                            cd.taxfee += agoods.goods_num * agoods.priceForShow * agoods.hs_rate;

                            cd.count_selected_id++;

                            /**
                             * 记录N元任选的商品数量 Added By Plucky 2016-08-18 16:40
                             */
                            if (agoods.isNyuanRenxuan()) {
                                cd.xnum += agoods.goods_num;
                            }


                            /** 记录id*/
                            sb_id.append(agoods.cart_id);
                            sb_id.append(",");

                            /** 记录id|num*/
                            sb_id_num.append(agoods.cart_id);
                            sb_id_num.append("|");
                            sb_id_num.append(agoods.goods_num);

                            sb_id_num.append(",");
                        }
                    }
                }

                /**
                 * 统计店铺优惠
                 * Added By Plucky
                 */
                cd.youhui += list.get(position).store_youhui;
            }

            if (sb_id.length() > 0) {
                /** 删除最后一个逗号*/
                sb_id.deleteCharAt(sb_id.length() - 1);
            }
            if (sb_id_num.length() > 0) {
                sb_id_num.deleteCharAt(sb_id_num.length() - 1);
            }

            cd.selected_id = sb_id.toString();
            cd.selected_id_num = sb_id_num.toString();
        }

        double finalMoney = cd.money - cd.youhui;
        cd.money = finalMoney >= 0 ? finalMoney : 0;

        return cd;
    }

    /**
     * 秒杀与限时折扣的时候会影响库存、价格的变化
     *
     * @param section  分区
     * @param position 索引
     *                 Added By Plucky 计算表示UI逻辑
     */
    private void refreshStorageAndPrice(int section, int position) {
        GoodsDtlPromXianshi xianshi = getItem(section, position).xianshi;
        if (xianshi != null) {
            if (getItem(section, position).goods_num >= xianshi.lower_limit) {
                getItem(section, position).priceForShow = xianshi.price;//限时价格
            } else {
                getItem(section, position).priceForShow = xianshi.origin_price;//原价格
            }
        } else {
            MiaoshaTag miaosha = getItem(section, position).seckilling;
            if (miaosha != null) {
                MiaoshaTag.Miaosha4Display mdisplay = miaosha.getDisplay(getItem(section, position).goods_num, getItem(section, position).goods_storage, miaosha.origin_price);
                getItem(section, position).storageLimit = mdisplay.storageLimit;
                getItem(section, position).priceForShow = mdisplay.price;
                getItem(section, position).miaoshaDrawable = mdisplay.drawableLeft;
            } else {
                getItem(section, position).storageLimit = getItem(section, position).goods_storage;//普通库存数
                getItem(section, position).priceForShow = getItem(section, position).goods_price;//原价格
                getItem(section, position).miaoshaDrawable = 0;
            }
        }
    }

    public interface ItemCheckedListener {
        void onItemChecked();
    }

    /**
     * 计算店铺任选活动优惠的金额
     * 数量变化和选择均会产生变化
     *
     * @param section 分区
     * @return double
     */
    public double getRenxuanYouhui(int section) {
        Carts cartsItem = list.get(section);
        RenxuanRule renxuanRule = cartsItem.renxuanRule;
        if (renxuanRule != null && renxuanRule.rulesSize() > 0) {
            RenxuanRule.RuleAndTips ruleAndTips = renxuanRule.getTips(getCalculate(section).xnum);
            // 如果刚好命中的话
            if (ruleAndTips.isHit) {
                return LayoutActivityinCart.getStoreYouhui(cartsItem.subitems, ruleAndTips.dtl.getManselect_nums(), ruleAndTips.dtl.getManselect_money(), isEditMode);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}