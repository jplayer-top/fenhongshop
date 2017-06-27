package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/17.
 * modify by lizhixin on 2015/12/21
 */
public class GoodsinCart extends BaseGoods {
    /** 已确定字段*/
    public String cart_id;  //【购物车商品】对应的id 主键，更新及删除操作均基于此id
    public String buyer_id;//用户id
    public String store_id;//商铺id
    public String store_name;//商铺名称

    public boolean normalSelected = false;//选中状态
    public boolean editSelected = false;

    public double hs_rate;//海关税率
    public String hs_code;//海关编码
    public String hsid;//海关编码ID
    public String state;//商品状态  （套装只显示有效无效即可）
    public String storage_state;//true代表 正常， false代表库存不足

    public GoodsDtlPromXianshi xianshi;//限时折扣

    public double down_price;//套装优惠的总金额
    public List<Bundlings> bundlings;//优惠套装

    public String goods_activity_state;//是否是N元任选活动内商品  （1是  2否）

    //秒杀数据源控制 Added By Plucky
    public int storageLimit;//库存限制数
    public double priceForShow;//显示价格
    public int miaoshaDrawable;//秒杀标签图

    @Override
    public int getGoodsSaleState() {
        if (isStateNormal()) {
            return -1;
        } else {
            return R.drawable.flag_sale_invalid;
        }
    }

    public boolean isStateNormal() {
        return TextUtils.equals(state, "true");//正常  已失效
    }

    public String getGoodsStateDes(Context context) {
        String result;
        switch (super.getGoodsSaleState()) {
            case R.drawable.flag_sale_withdrawn://已下架
                result = context.getString(R.string.sale_withdraw);
                break;
            case R.drawable.flag_sale_stop://已停售
                result = context.getString(R.string.sale_stop);
                break;
            case R.drawable.flag_sale_presell://已停售
                result = context.getString(R.string.sale_presell);
                break;
            case R.drawable.flag_sale_over://已售罄
                result = context.getString(R.string.sale_over);
                break;
            default:
                result = context.getString(R.string.sale_invalid);
                break;
        }
        return result;
    }

    public boolean getIsSelected(boolean editMode) {
        return editMode ? editSelected : normalSelected;
    }

    public void setSelected(boolean editMode, boolean flag) {
        if (editMode) {
            editSelected = flag;
        } else {
            normalSelected = flag;
        }
    }

    public boolean isNyuanRenxuan() {
        return TextUtils.equals("1", goods_activity_state);
    }
}
