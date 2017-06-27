package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-18.
 * modify by lizhixin on 2013/03/30
 */
public class GoodsDetail extends BaseGoods {

    //public String goods_country;// 商品所属国家
    //public String goods_custom;//  商品所属保税仓
    public Object goods_images; //BaseGoods goods_image字段表示单图片  在商品详情页为 商品图片数组

    // 规格相关
    public LinkedTreeMap spec_list;//  规格列表   { "规格类别1" : {"规格id" : "规格值" ,...} ,...}  具体：{"颜色":{"123":"蓝色" ,...} ,...}
    public LinkedTreeMap spec_relation;//    规格映射，由规格列表中的规格id拼成字符串对应goods_id  {"规格id1|规格id2|..." : "商品id" ,...}   具体：{"123|321...": "1105" ,...}   为什么要这样？：把用户选的规格拼成这种竖线隔开的字符串对应着找到商品id,然后用商品id去查询对应商品的库存而且购买和加入购物车都需要这个商品id。注意：不要对当前商品详情页对应的商品id进行操作，而是用通过这个映射找到的商品id
    public LinkedTreeMap goods_spec;//   当前商品规格 {规格id:规格值 ,...}

    // 参数相关
    public LinkedTreeMap goods_attr;//    商品参数  {参数名 : 参数值 ,...}

    // 快递相关
    public Object area_list;//       省份地区列表   {"地区id":"地区名称" ,...}  地区id和transport_id用来查询地区运费
    public int transport_id;//       运费模板id（查询地区运费必传，为0则说明运费固定，否则运费会随着选择地区不同而变化）
    public double goods_freight;//   默认商品运费（运费模板transport_id如果存在并且用户选择地区，请用调用地区运费查询接口获取）
    public double hs_rate;//         关税税率

    // 评价相关
    //public float goods_comments_percent;// 好评度（百分比数字，如100）
    public int goods_comments_num;//         评论总数
    public Object goods_comments;//          两条最新商品评论数组

    // 店铺相关
    public String store_id;//                    店铺id
    public String store_logo;//                  店铺logo图片链接
    public String store_name;//                  店铺名称
    public String store_baidusales;//            百度商桥链接
    public String store_notice;//                店铺公告
    //public String store_desc;//                  店铺服务说明(国内商品时用)
    public double store_desccredit = 0.0;//      店铺描述相符评分
    public double store_servicecredit = 0.0;//   店铺服务态度评分
    public double store_deliverycredit = 0.0;//  店铺发货速度评分

    // 会员相关
    public int if_fav = 0;//          是否已收藏（1 是 0  否）（只有传递mid才返回）
    //public double goods_commission;// 奖金

    // 提示标签
    public int refund_whatever;//是否支持7天无理由退货  1是, 0否
    public int goods_vat;      //是否提供发票  1是, 0否

    // 包邮
    public double store_free_freight_amount;//满额包邮 （有才返回）
    //public String store_free_freight_explain;//满额包邮说明文字

    // 限制折扣
    public GoodsDtlPromXianshi xianshi;

    // 满额优惠
    public GoodsDtlPromMansong mansong;

    public String no_rate_tip;// 万国邮联税费提示

    public String resource_tags;//统计数据，透明传输 -- lizhixin
    public String if_notice;//是否已经添加过 到货通知/预售提醒 1:是 |0:否 -- lizhixin
    public String gc_id;//商品分类

    public long seckilling_countup;//距秒杀开始
    public double oversea_per_purchase_limit;//海外购买单次交易额限制

    public String brand_id;// 品牌id
    public String brand_name;// 品牌名称
    public String brand_pic;// 品牌图片
    public String brand_intro;// 品牌描述
    public int brand_goods_storage;// 品牌下的在售商品数


    // 满多少元任选
    public List<RenxuanRule> manselect_rule;

    // 配送相关
    public String warehouse_name;//           配送仓库
    public String not_deliver_areas;//        不配送地区
    public String not_deliver_remark;//       不配送说明

    private List<GoodsIntro> goods_intro_info;//商品说明信息

    private String micro_shop_name;//微店店铺等级
    private double goods_save_money;//省多少钱
    private PopupInfo popup_info;//加入购物车弹窗提示

    public boolean isShowMicroLvl() {
        return !TextUtils.isEmpty(micro_shop_name) && goods_save_money > 0;
    }

    public String getMicro_shop_name() {
        return micro_shop_name;
    }

    public String getSaveMoneyDesc() {
        return "省 " + goods_save_money;
    }

    /**
     * 原价 赋值
     */
    @Override
    public String getGoodspriceDesc() {
        //如果有折扣价则优先显示折扣价，否则显示原价
        if (xianshi == null) {
            return super.getGoodspriceDesc();
        } else {
            return "¥" + BaseFunc.truncDouble(xianshi.price, 2);
        }
    }

    public double getGoodsPrice4Show() {
        if (xianshi == null) {
            return goods_price;
        } else {
            return xianshi.price;
        }
    }

    public double getMarketPrice4Show() {
        if (xianshi == null) {
            return goods_marketprice;
        } else {
            return goods_price;
        }
    }

    /**
     * 市场价 赋值
     */
    @Override
    public String getMarketpriceDesc(boolean simple) {
        //如果有折扣活动则优先显示原价，否则显示市场价
        if (xianshi == null) {
            return super.getMarketpriceDesc(simple);
        } else {
            return super.getGoodspriceDesc();
        }
    }


    /**
     * 获取快递描述
     */
    public CharSequence[] getAreaXAndFreight(Context c, String to, double afreight) {
        CharSequence[] res = new CharSequence[]{"", ""};

        res[0] = String.format("%1$s 至 %2$s", warehouse_name, to);

        if (afreight > 0) {
            String fmt = c != null ? c.getString(R.string.count_fee_color) : "¥%1$s";
            String f = String.format(fmt, afreight);
            res[1] = Html.fromHtml(f);
        } else {
            String f = c != null ? c.getString(R.string.goods_freight_free_color) : "卖家承担运费";
            res[1] = Html.fromHtml(f);
        }
        return res;
    }

    /**
     * 获取当前商品对应的规格
     */
    public String getCurrentGoodsspec() {
        return BaseGoods.parseSpec(goods_spec);
    }

    /**
     * 获取评价相关
     */
    public List<GoodsComments> getGoodsCommects() {
        try {
            return new Gson().fromJson(new Gson().toJson(goods_comments), new TypeToken<List<GoodsComments>>() {
            }.getType());

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取商品图片--轮播
     */
    public List<String> getGoodsImage() {
        try {
            return new Gson().fromJson(new Gson().toJson(goods_images), new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

    public List<Banner> getGoodsImageBanner() {
        List<String> imgs = getGoodsImage();
        if (imgs != null && imgs.size() > 0) {
            List<Banner> banners = new ArrayList<>();
            for (int i = 0; i < imgs.size(); i++) {
                Banner b = new Banner();
                b.index = i;
                if (BaseFunc.isValidUrl(imgs.get(i))) {
                    b.image_url = imgs.get(i);
                } else {
                    b.image_url = BaseVar.DEFAULT_BANNER;
                }
                banners.add(b);
            }
            return banners;
        }
        return null;
    }

    /**
     * 获取轮播图第一张
     */
    public String getAGoodsImage() {
        List<String> imgs = getGoodsImage();
        if (imgs != null && imgs.size() > 0) {
            return imgs.get(0);
        }
        return null;
    }

    /**
     * 获取支持配送的地区
     */
    public LinkedTreeMap getAreaList() {
        try {
            return (LinkedTreeMap) area_list;

        } catch (Exception e) {
            return null;
        }
    }

    public List<Area> getAreaArray() {
        LinkedTreeMap json = getAreaList();

        if (json == null || json.size() == 0) {
            return null;
        }

        Object[] keys, values;
        try {
            keys = json.keySet().toArray();
            values = json.values().toArray();
        } catch (Exception e) {
            keys = null;
            values = null;
        }


        List<Area> list = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            try {
                if (keys != null) {
                    Area a = new Area();
                    a.area_id = String.valueOf(keys[i]);
                    a.area_name = String.valueOf(values[i]);
                    list.add(a);
                }
            } catch (Exception e) {
                //
            }
        }
        return list;
    }

    /**
     * 通过位置名称
     *
     * @param areaName String
     * @return Area
     */
    public Area getAreaByName(String areaName) {
        if (TextUtils.isEmpty(areaName)) return null;
        List<Area> alist = getAreaArray();
        if (alist != null && alist.size() > 0) {
            int index = -1;
            for (int i = 0; i < alist.size(); i++) {
                if (areaName.contains(alist.get(i).area_name) || alist.get(i).area_name.contains(areaName)) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                return alist.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * 是否支持配送
     *
     * @param areaName 配送地区名称
     * @return true、false
     */
    public boolean isSupportArea(String areaName) {
        areaName = TextUtils.isEmpty(areaName) ? areaName : areaName.split(" ")[0];
        return TextUtils.isEmpty(not_deliver_areas) || !not_deliver_areas.contains(areaName);
    }

    public CharSequence getRefundTips(Context c) {
        if (goods_intro_info != null && goods_intro_info.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (GoodsIntro intro : goods_intro_info) {
                sb.append("[fwarn]");
                sb.append(" ");
                sb.append(intro.getTitle());
                sb.append(" ");
            }
            return FHSpanned.convertNormalStringToSpannableString(c, sb.toString());
        } else {
            return "";
        }
    }

    public String getCommentCountDesc() {
        return "(" + goods_comments_num + ")";
    }


    /**
     * 满多少元任选多少件描述文字
     *
     * @return String
     */
    public String getRenxuanRuleDesc() {
        if (manselect_rule != null && manselect_rule.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (RenxuanRule rule : manselect_rule) {
                if (sb.length() == 0) {
                    sb.append(rule.getRule_name());
                } else {
                    sb.append("，");
                    sb.append(rule.getRule_name());
                }
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    public ActivityList genBrandActivityList() {
        if (!TextUtils.isEmpty(brand_id)) {
            ActivityList activityList = new ActivityList();
            activityList.activity_url = "http://t.cn/?val=" + brand_id + "!group";
            return activityList;
        } else {
            return null;
        }
    }

    public List<GoodsIntro> getGoods_intro_info() {
        return goods_intro_info;
    }

    public PopupInfo getPopupInfo() {
        return popup_info;
    }
}
