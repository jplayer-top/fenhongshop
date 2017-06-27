package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.google.gson.internal.LinkedTreeMap;

import java.text.DecimalFormat;

/**
 * 商品 Item 基础属性类
 * Created by lizhixin on 2015/12/18.
 */
public class BaseGoods extends APIUtil {
    public String goods_id;//商品id
    public String goods_commonid;//
    public long add_time;
    public double goods_promotion_price;//

    public String goods_name;//商品名称
    public String goods_desc;//    商品描述

    public double goods_price;//商品商城价格
    public double goods_marketprice;//商品市场价格
    public String goods_image;//商品图片
    public int goods_num;//商品数量
    public int goods_source;//    商品来源国家（0 国内，大于0 为海外直邮商品)
    public String goods_origin;
    public MiaoshaTag seckilling;//秒杀
    public int is_own_shop;//是否是自营商品, 1 是, 0 否
    public String goods_badge;//分红优选
    public int is_presell;//是否是预售商品, 1 是, 0 否
    public int is_import;//是否是进口商品, 1 是, 0 否

    public String nation_name;//国家地区名称
    public String nation_flag;//国家地区旗帜图片
    public String goods_promise;   //商品保证标语

    public int sale_stop;//是否停售 （1 是  0 否）
    public int goods_storage;//商品库存数

    public int goods_state = 1;// 商品状态 ( 0 已下架  1 正常 )
    public int is_activity;// 是否参加平台活动（如优惠券红包）（0 否   1 是）

    public int groupbuy_flag;//  是否是团购价 ( 0  否  1 是 ）
    public int xianshi_flag;//  是否是限时折扣价 ( 0  否  1 是 ）
    public int sole_flag;//  是否是手机专享价 ( 0  否  1 是 ）
    public int seckilling_flag;//  是否是手机秒杀 ( 0  否  1 是 ）

    //新增字段 奖金相关（主要用于列表）
    private String goods_reward_ratio;// 商品返回奖金比例  字符串 比如 “12.5%”
    private double goods_reward_money;// 商品返回奖金数额 比如 7.80
    private String goods_share_intro;//  商品分享提示 “分享赚钱”

    // 自定义分享字段
    public String share_title;//             分享标题
    public String share_describe;//          分享描述
    public String share_img;//               分享图片

    public int getGoodsSaleState() {

        if (goods_state == 0) {
            //已下架
            return R.drawable.flag_sale_withdrawn;
        } else {
            if (sale_stop == 1) {
                //停售
                return R.drawable.flag_sale_stop;
            } else {
                if (is_presell == 1) {
                    //预售
                    return R.drawable.flag_sale_presell;
                } else {
                    if (goods_storage == 0) {
                        if (seckilling != null) {
                            //已售罄  Modify By Plucky
                            if (seckilling.killing_stock == 0) {
                                return R.drawable.flag_sale_over;
                            } else {
                                return -1;
                            }
                        } else {
                            return R.drawable.flag_sale_over;
                        }
                    } else {
                        return -1;
                    }
                }
            }
        }

    }

    /**
     * 商品价格
     */
    public String getGoodspriceDesc() {
        DecimalFormat df = new DecimalFormat("¥#0.00");
        return df.format(goods_price);
    }

    /**
     * 市场价格
     */
    public String getMarketpriceDesc(boolean simple) {
        DecimalFormat df = new DecimalFormat("¥#0.00");
        if (simple) {
            return df.format(goods_marketprice);
        } else {
            return String.format("市场参考价%1$s", df.format(goods_marketprice));
        }
    }

    /**
     * 解析商品规格
     */
    public static String parseSpec(LinkedTreeMap goods_spec) {
        if (goods_spec == null || goods_spec.size() == 0) {
            return "";
        } else {
            String goods_spec_str = String.valueOf(goods_spec.values());
            if (goods_spec_str != null) {
                goods_spec_str = goods_spec_str.replaceAll("\\[", "");
                goods_spec_str = goods_spec_str.replaceAll("\\]", "");
                goods_spec_str = goods_spec_str.replaceAll(",", "/");
                goods_spec_str = goods_spec_str.replaceAll(" ", "");
            }
            return goods_spec_str;
        }
    }

    public int getGoodsOrigin() {
        if (BaseFunc.isInteger(goods_origin)) {
            return Integer.valueOf(goods_origin);
        } else {
            return 0;
        }
    }

    /**
     * 获取商品名称
     */
    public String getGoodsName(Context mContext) {
        return goods_name;
    }

    /**
     * 商品详情页标题
     */
    public CharSequence getGoodsNameForDetail(Context mContext) {
        if (is_own_shop == 1) {
            return FHSpanned.convertNormalStringToSpannableString(mContext, goods_name + " [fown]");
        } else {
            return goods_name;
        }
    }


    public String getActivityDesc(Context c) {
        if (is_activity == 0 || seckilling != null) {
            return c.getString(R.string.fmt_goods_use_bonus);
        } else {
            return null;
        }
    }

    public String getGoodsNumDesc() {
        return "x " + goods_num;
    }

    //新增字段 奖金相关

    public String getGoods_share_intro() {
        if (TextUtils.isEmpty(goods_share_intro)) {
            return "分享赚钱";
        }
        return goods_share_intro;
    }

    public String getGoodsRewardMoney() {
        return "赚 ¥" + goods_reward_money;
    }

    public String getMaxRewordRatio() {
        return "最高赚" + getGoods_reward_ratio();
    }

    public double getGoods_reward_money() {
        return goods_reward_money;
    }

    /**
     * 是否显示分享赚钱的视图
     *
     * @return boolean
     */
    public boolean isShowReward() {
        Member member = FHCache.getMember(FHApp.getInstance());
        return member != null && member.if_shoper == 1;
    }

    public String getGoods_reward_ratio() {
        if (TextUtils.isEmpty(goods_reward_ratio)) {
            return "0%";
        }
        return goods_reward_ratio;
    }

    public String getFanliDesc() {
        return "返" + getGoods_reward_ratio();
    }

    public String getFanli() {
        return "/ 返利" + getGoods_reward_ratio();
    }

    public float getRewordRatio() {
        try {
            String tmp = goods_reward_ratio.replace("%", "");
            return Float.parseFloat(tmp);
        } catch (Exception e) {
            return 0;
        }
    }
}
