package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Bonus;
import com.fanglin.fenhong.microbuyer.base.model.CartCheck;
import com.fanglin.fenhong.microbuyer.base.model.CartCheckData;
import com.fanglin.fenhong.microbuyer.base.model.CartCheckGoods;
import com.fanglin.fenhong.microbuyer.base.model.FHSwitch;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromMansongRules;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromXianshi;
import com.fanglin.fenhong.microbuyer.base.model.MiaoshaTag;
import com.fanglin.fenhong.microbuyer.buyer.TaozhuangActivity;
import com.fanglin.fenhong.microbuyer.buyer.adapter.RecycleImgAdapter;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/23.
 * 核对购物车
 */
public class CartCheckAdapter extends RecyclerView.Adapter<CartCheckAdapter.ViewHoder> {

    private Context mContext;
    private boolean isChina;
    private List<CartCheck> list = new ArrayList<>();
    private CartCheckData mData;
    public Bonus SelectedBonus;
    public boolean isAccountSelected = false;
    private AgreementCheckboxCallBack agreementCheckboxCallBack;
    DecimalFormat df;
    private String seckilling_goods = "";

    public CartCheckAdapter(Context context) {
        this.mContext = context;
        df = new DecimalFormat("¥#0.00");
    }

    public void setAgreementCheckboxCallBack(AgreementCheckboxCallBack agreementCheckboxCallBack) {
        this.agreementCheckboxCallBack = agreementCheckboxCallBack;
    }

    public String getSeckillingGoodsIDs() {
        return seckilling_goods;
    }

    public void setList(CartCheckData data, boolean isChina) {
        this.isChina = isChina;
        this.list = data != null ? data.store_cart_list : null;
        mData = data;
        if (mData != null) mData.isChina = isChina;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                double money = 0;
                int num = 0;

                List<CartCheckGoods> goods_gifts = new ArrayList<>();//免费赠送的商品

                List<CartCheckGoods> goods_list = list.get(i).goods_list;
                if (goods_list != null && goods_list.size() > 0) {
                    for (int j = 0; j < goods_list.size(); j++) {
                        num += goods_list.get(j).goods_num;
                        money += goods_list.get(j).goods_price * goods_list.get(j).goods_num;

                        /** 处理免费送的商品*/
                        List<CartCheckGoods> gift_tmp = goods_list.get(j).convertGift2CartCheckGoodsList();
                        if (gift_tmp != null && gift_tmp.size() > 0) {
                            goods_gifts.addAll(gift_tmp);
                        }

                        /** Added By Plucky 组合秒杀商品Id*/
                        MiaoshaTag miaoshaTag = goods_list.get(j).seckilling;
                        if (miaoshaTag != null) {
                            if (TextUtils.isEmpty(seckilling_goods)) {
                                seckilling_goods = goods_list.get(j).goods_id;
                            } else {
                                seckilling_goods += "," + goods_list.get(j).goods_id;
                            }
                        }
                    }
                }

                if (goods_gifts.size() > 0) {
                    list.get(i).goods_list.addAll(goods_gifts);
                }

                /** 针对于满送的处理 */
                if (mData != null) {
                    GoodsDtlPromMansongRules manSong = mData.getMansong(list.get(i).store_id);
                    if (manSong != null) {
                        list.get(i).mansong_minus_amount = manSong.getMinus_amount();
                        list.get(i).mansong_desc = "满" + manSong.egt_amount + "元";
                        list.get(i).mansong_goods = manSong.convertGift2CartCheckGoods(list.get(i).mansong_desc);

                        /** 将满送的商品塞入*/
                        if (list.get(i).mansong_goods != null) {
                            list.get(i).goods_list.add(list.get(i).mansong_goods);
                        }
                    }
                }

                list.get(i).goods_money = money;
                list.get(i).store_num = num;
                list.get(i).store_tax = mData.getTaxFeeOfStore(list.get(i).store_id);

            }
        } else {
            list = null;
        }
    }

    public double[] getPayAndNum() {
        double[] data = new double[]{0, 0, 0};
        if (mData != null) {
            double[] fees = mData.getMoneyAndNumAndFeeAndTaxAndYouhui();
            double useBonus = mData.getBonusMoney(SelectedBonus);
            double useAmount = mData.getDepositeMoney(isAccountSelected, SelectedBonus);
            data[0] = fees[0] + fees[2] + fees[3] - (useBonus + useAmount) - fees[4];//减去店铺优惠的金额
            data[0] = data[0] - mData.micro_shoper_save_money;//减去微店主身份优惠的钱
            if (data[0] <= 0) {
                data[0] = 0;
            }
            data[1] = fees[1];
            data[2] = useBonus + useAmount;//使用余额和优惠券的金额
        }
        return data;
    }


    @Override
    public int getItemCount() {
        if (mData == null || list == null) return 0;
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_cartcheck, null);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHoder holder, final int position) {
        CartCheck cartCheckItem = list.get(position);
        final String store_id = cartCheckItem.store_id;
        holder.tv_store.setText(cartCheckItem.store_name);
        BaseFunc.setFont(holder.LIcon);

        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        List<CartCheckGoods> store_goods = cartCheckItem.goods_list;
        holder.goods_flow.setAdapter(new TagAdapter(store_goods) {
            @Override
            public View getView(FlowLayout parent, int index, Object o) {
                final View view;
                final CartCheckGoods agoods = (CartCheckGoods) o;
                String[] imgs = agoods.getImageStr();
                //套装
                if (imgs != null && imgs.length > 0) {
                    view = mInflater.inflate(R.layout.item_cartcheck_buildings, holder.goods_flow, false);
                    RecyclerView rc_img = (RecyclerView) view.findViewById(R.id.rc_img);
                    TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                    TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                    TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                    TextView tv_icon = (TextView) view.findViewById(R.id.tv_icon);
                    TextView tv_bonus_desc = (TextView) view.findViewById(R.id.tv_bonus_desc);

                    tv_title.setText(agoods.goods_name);
                    tv_price.setText(df.format(agoods.goods_price));
                    String fmt = mContext.getString(R.string.num_buy);
                    String memo = String.format(fmt, agoods.goods_num);
                    tv_memo.setText(memo);
                    BaseFunc.setFont(tv_icon);

                    RecycleImgAdapter imgAdapter = new RecycleImgAdapter(mContext);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                    rc_img.setLayoutManager(linearLayoutManager);
                    imgAdapter.setList(imgs);
                    rc_img.setAdapter(imgAdapter);

                    imgAdapter.setCallBack(new RecycleImgAdapter.RecyclerImgCallBack() {
                        @Override
                        public void onItemClick(int i) {
                            BaseFunc.gotoActivity(mContext, TaozhuangActivity.class, new Gson().toJson(agoods.bl_list));
                        }
                    });

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoActivity(mContext, TaozhuangActivity.class, new Gson().toJson(agoods.bl_list));
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
                    view = mInflater.inflate(R.layout.item_cartcheck_goods, holder.goods_flow, false);
                    ImageView sdv = (ImageView) view.findViewById(R.id.sdv);
                    TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                    TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                    TextView tv_memo = (TextView) view.findViewById(R.id.tv_memo);
                    TextView tv_activity_desc = (TextView) view.findViewById(R.id.tv_activity_desc);
                    TextView tv_mansong_desc = (TextView) view.findViewById(R.id.tv_mansong_desc);
                    TextView tv_bonus_desc = (TextView) view.findViewById(R.id.tv_bonus_desc);

                    //限时提示
                    GoodsDtlPromXianshi xianshi = agoods.xianshi;
                    if (xianshi != null) {
                        tv_activity_desc.setText(xianshi.getLowerLimitText());
                        tv_activity_desc.setVisibility(View.VISIBLE);
                        tv_activity_desc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.promotion_xian, 0, 0, 0);
                    } else {
                        //秒杀标签 Added By Plucky 2016-3-10 22:18
                        MiaoshaTag seckilling = agoods.seckilling;
                        if (seckilling != null) {
                            tv_activity_desc.setText("");
                            tv_activity_desc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_miaosha, 0, 0, 0);
                            tv_activity_desc.setVisibility(View.VISIBLE);
                        } else {
                            tv_activity_desc.setText("");
                            tv_activity_desc.setVisibility(View.GONE);
                        }
                    }

                    //满送提示
                    if (agoods.is_mansong) {
                        tv_mansong_desc.setText(agoods.mansong_desc);
                        tv_mansong_desc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.promotion_song, 0, 0, 0);
                        tv_mansong_desc.setVisibility(View.VISIBLE);
                    } else {
                        //如果是免费赠送的商品
                        if (agoods.is_freegift) {
                            tv_mansong_desc.setText("");
                            tv_mansong_desc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.promotion_zeng, 0, 0, 0);
                            tv_mansong_desc.setVisibility(View.VISIBLE);
                        } else {
                            tv_mansong_desc.setVisibility(View.GONE);
                        }
                    }

                    tv_title.setText(agoods.goods_name);
                    tv_price.setText("¥" + agoods.goods_price);
                    String fmt = mContext.getString(R.string.num_buy);
                    String memo = String.format(fmt, agoods.goods_num);
                    tv_memo.setText(memo);
                    new FHImageViewUtil(sdv).setImageURI(agoods.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

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

        holder.LIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(store_id)) return;
                BaseFunc.gotoActivity(mContext, StoreActivity.class, store_id);
            }
        });

        holder.tv_store_freight.setText(df.format(cartCheckItem.store_freight));
        holder.tv_store_money.setText(df.format(cartCheckItem.goods_money));

        holder.tvWeight.setVisibility(View.VISIBLE);
        holder.tvWeight.setText(cartCheckItem.getShipping_weight());

        /** 包邮的显示*/
        holder.iv_freight.setText(mData.getBaoyouDesc(store_id));
        if (cartCheckItem.store_freight > 0) {
            //店铺有运费时不显示包邮提示
            holder.iv_freight.setVisibility(View.INVISIBLE);
        } else {
            //店铺运费为0时，当商品总额大于等于包邮限额时，显示包邮提示
            if (mData.getBaoyoumoney(store_id) > 0 && cartCheckItem.goods_money >= mData.getBaoyoumoney(store_id)) {
                holder.iv_freight.setVisibility(View.VISIBLE);
            } else {
                holder.iv_freight.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * 店铺优惠的钱
         * manselect_minus_amount 满任选
         * mansong_minus_amount   满送
         */
        double store_youhui = cartCheckItem.mansong_minus_amount + cartCheckItem.manselect_minus_amount;
        String store_youhui_desc = "-" + df.format(store_youhui);
        holder.tv_store_free_money.setText(store_youhui_desc);

        if (isChina) {
            holder.LTaxfee.setVisibility(View.GONE);
        } else {
            String taxfeeStr = df.format(mData.getTaxFeeOfStore(cartCheckItem.store_id));
            holder.tv_taxfee.setText(taxfeeStr);
            holder.LTaxfee.setVisibility(View.VISIBLE);
        }

        if (position == getItemCount() - 1) {
            holder.LPay.setVisibility(View.VISIBLE);
            holder.LLast.setVisibility(View.VISIBLE);
        } else {
            holder.LPay.setVisibility(View.GONE);
            holder.LLast.setVisibility(View.GONE);
        }

        BaseFunc.setFont(holder.tv_icon);
        BaseFunc.setFont(holder.tv_check);
        holder.tv_bonus_display.setText(mData.getBonusShow(SelectedBonus));
        holder.tv_amount_display.setText(mData.getDepositShow());
        holder.tv_check.setVisibility(isAccountSelected ? View.VISIBLE : View.INVISIBLE);

        holder.tv_amount_use.setText(mData.getDepositeMoneyUse(isAccountSelected, SelectedBonus));
        holder.tv_bonus_use.setText(mData.getBonusMoneyUse(SelectedBonus));
        holder.LSelectedBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.getAvailableCount() > 0) {
                    if (mcb != null) mcb.onSelectBonus(mData.coupon_list);
                } else {
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.no_available_bonus));
                }
            }
        });

        final FHSwitch Hongbao = FHApp.getInstance().getHongBao();
        if (Hongbao != null) {
            holder.LLock.setVisibility(View.VISIBLE);
            holder.tv_lockdesc.setText(Hongbao.message);
            if (BaseFunc.isValidUrl(Hongbao.url)) {
                holder.LLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.urlClick(mContext, Hongbao.url);
                    }
                });
            } else {
                holder.LLock.setOnClickListener(null);
            }
        } else {
            holder.LLock.setVisibility(View.GONE);
            holder.tv_lockdesc.setText(mContext.getString(R.string.bonus_tips));
        }

        if (holder.LLock.getVisibility() == View.VISIBLE) {
            holder.LSelectedBonus.setEnabled(false);
        } else {
            holder.LSelectedBonus.setEnabled(true);
        }

        holder.LSelectedAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcb != null) mcb.onSelectPay();
            }
        });

        if (mData.getDeposit_disable()) {
            holder.LLockAmount.setVisibility(View.VISIBLE);
            holder.LSelectedAmount.setEnabled(false);
        } else {
            holder.LLockAmount.setVisibility(View.GONE);
            holder.LSelectedAmount.setEnabled(true);
        }

        holder.LBonusUseStatus.setVisibility(View.GONE);

        holder.tv_coupon_on_goods_amount.setText(df.format(mData.coupon_on_goods_amount));
        holder.tv_coupon_off_goods_amount.setText(df.format(mData.coupon_off_goods_amount));

        //是否同意条款checkbox
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (agreementCheckboxCallBack != null) {
                    agreementCheckboxCallBack.onAgreementChanged(isChecked);
                }
            }
        });
        //个人申报协议
        holder.tv_personal_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, BaseVar.PERSONAL_AGREEMENT);
            }
        });
        //商家服务政策
        holder.tv_merchant_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, BaseVar.MERCHANT_POLICY);
            }
        });

        //红人店铺优惠显示
        if (mData.showMicroShopSave()) {
            holder.LMicroShopSave.setVisibility(View.VISIBLE);
            holder.vMicroShopSave.setVisibility(View.VISIBLE);
            holder.tvMicroShopSaveDesc.setText(mData.getMicroShoperSaveDesc());
            holder.tvMicroShopSaveMoney.setText(mData.getMicroShoperSaveMoney());
        } else {
            holder.LMicroShopSave.setVisibility(View.GONE);
            holder.vMicroShopSave.setVisibility(View.GONE);
        }
    }

    private CartCheckCallBack mcb;

    public void setCallBack(CartCheckCallBack cb) {
        this.mcb = cb;
    }

    public interface CartCheckCallBack {
        void onSelectPay();

        void onSelectBonus(CartCheckData.COUPON_LIST coupon_list);
    }

    class ViewHoder extends RecyclerView.ViewHolder {
        public LinearLayout LIcon;
        public TextView tv_store;
        public TagFlowLayout goods_flow;
        public TextView tv_store_freight;
        public TextView tvWeight;
        public TextView iv_freight;
        public TextView tv_store_money;

        public LinearLayout LTaxfee;
        public TextView tv_taxfee;

        /**
         * 使用余额和优惠券逻辑
         */
        public LinearLayout LPay;
        public LinearLayout LLock, LLockAmount, LSelectedBonus, LSelectedAmount;
        public TextView tv_lockdesc, tv_bonus_display, tv_amount_display, tv_check, tv_icon;
        public LinearLayout LLast;
        public TextView tv_bonus_use, tv_amount_use;
        public LinearLayout LBonusUseStatus;
        public TextView tv_coupon_on_goods_amount, tv_coupon_off_goods_amount;

        /**
         * 优惠活动
         */
        public TextView tv_store_free_money;

        public CheckBox checkBox;
        public TextView tv_personal_agreement;
        public TextView tv_merchant_policy;


        //红人店铺优惠显示
        public LinearLayout LMicroShopSave;
        public View vMicroShopSave;
        public TextView tvMicroShopSaveDesc, tvMicroShopSaveMoney;

        public ViewHoder(View itemView) {
            super(itemView);
            LIcon = (LinearLayout) itemView.findViewById(R.id.LIcon);
            tv_store = (TextView) itemView.findViewById(R.id.tv_store);
            goods_flow = (TagFlowLayout) itemView.findViewById(R.id.goods_flow);
            tv_store_freight = (TextView) itemView.findViewById(R.id.tv_store_freight);
            tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);
            tv_store_money = (TextView) itemView.findViewById(R.id.tv_store_money);
            iv_freight = (TextView) itemView.findViewById(R.id.iv_freight);

            LTaxfee = (LinearLayout) itemView.findViewById(R.id.LTaxfee);
            tv_taxfee = (TextView) itemView.findViewById(R.id.tv_taxfee);

            LPay = (LinearLayout) itemView.findViewById(R.id.LPay);
            LLock = (LinearLayout) itemView.findViewById(R.id.LLock);
            LLockAmount = (LinearLayout) itemView.findViewById(R.id.LLockAmount);
            LSelectedBonus = (LinearLayout) itemView.findViewById(R.id.LSelectedBonus);
            LSelectedAmount = (LinearLayout) itemView.findViewById(R.id.LSelectedAmount);
            tv_lockdesc = (TextView) itemView.findViewById(R.id.tv_lockdesc);
            tv_bonus_display = (TextView) itemView.findViewById(R.id.tv_bonus_display);
            tv_amount_display = (TextView) itemView.findViewById(R.id.tv_amount_display);
            tv_check = (TextView) itemView.findViewById(R.id.tv_check);
            tv_icon = (TextView) itemView.findViewById(R.id.tv_icon);
            LLast = (LinearLayout) itemView.findViewById(R.id.LLast);
            tv_bonus_use = (TextView) itemView.findViewById(R.id.tv_bonus_use);
            tv_amount_use = (TextView) itemView.findViewById(R.id.tv_amount_use);

            LBonusUseStatus = (LinearLayout) itemView.findViewById(R.id.LBonusUseStatus);
            tv_coupon_on_goods_amount = (TextView) itemView.findViewById(R.id.tv_coupon_on_goods_amount);
            tv_coupon_off_goods_amount = (TextView) itemView.findViewById(R.id.tv_coupon_off_goods_amount);

            tv_store_free_money = (TextView) itemView.findViewById(R.id.tv_store_free_money);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tv_personal_agreement = (TextView) itemView.findViewById(R.id.tv_personal_agreement);
            tv_merchant_policy = (TextView) itemView.findViewById(R.id.tv_merchant_policy);


            LMicroShopSave = (LinearLayout) itemView.findViewById(R.id.LMicroShopSave);
            vMicroShopSave = itemView.findViewById(R.id.vMicroShopSave);
            tvMicroShopSaveDesc = (TextView) itemView.findViewById(R.id.tvMicroShopSaveDesc);
            tvMicroShopSaveMoney = (TextView) itemView.findViewById(R.id.tvMicroShopSaveMoney);
        }
    }

    public interface AgreementCheckboxCallBack {
        void onAgreementChanged(boolean isChecked);
    }
}
