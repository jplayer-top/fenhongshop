package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/12-下午3:09.
 * 功能描述:
 */
public class GoodsButtonHandler {

    public final static int LEFT_ACTION_CART = 0;//添加购物袋
    public final static int LEFT_ACTION_NOTICE = 1;//到货通知
    public final static int LEFT_ACTION_CANCELNOTICE = 2;//取消到货通知
    public final static int LEFT_ACTION_NONE = -1;//不显示该按钮

    public final static int RIGHT_ACTION_BUY = 0;//立即购买
    public final static int RIGHT_ACTION_SIMILAR = 1;//查看相似
    public final static int RIGHT_ACTION_COMINGSOON = 2;//即将发售
    public final static int RIGHT_ACTION_XIAJIA = 3;//已下架
    public final static int RIGHT_ACTION_CART = 4;//右边按钮加入购物车（在存在规格时,加入购物车要弹框）


    public final static int SHOPPINGACTION_BOTH = 0;
    public final static int SHOPPINGACTION_CART = 1;//仅添加购物袋
    public final static int SHOPPINGACTION_BUY = 2;//仅立即购买按钮

    public final static int SHOPPINTACTION_NOTSUPPORT_AREA = -1;//不支持配送

    /**
     * 控制按钮的宽度
     *
     * @param tvRight       右边的按钮
     * @param isSingle      是否只显示一个按钮
     * @param needCalculate 是否需要设定高度
     */
    private static void refreshTvRightWidth(TextView tvRight, boolean isSingle, boolean needCalculate) {
        if (tvRight == null) return;
        if (!needCalculate) return;//如果是规格选择页面弹出的不需要设定按钮的宽度
        ViewGroup.LayoutParams params = tvRight.getLayoutParams();
        if (params != null) {
            int w = tvRight.getResources().getDimensionPixelOffset(R.dimen.dp_of_95);
            params.width = isSingle ? w : 2 * w;
            tvRight.setLayoutParams(params);
        }
    }

    /**
     * 处理两个按钮的逻辑
     * 1、添加购物袋 立即购买
     * 2、商品售罄事：到货通知(取消通知) 查看相似
     * 3、即将发售：此时只有一个按钮（tvRight 且Enable=false）
     * 4、选择的地区不支持配送  Added By Plucky 2016-08-15 13:37
     *
     * @param goodsStatus 商品当前的状态
     * @param ifNotice    是否已经点击了到货通知
     */
    public static void refreshBtnStatus(int goodsStatus, boolean ifNotice, TextView tvLeft, TextView tvRight, TextView tvSaleOutHint, int shoppingAction, ActionChangeListener listener) {
        int currentLeft, currentRight;
        switch (goodsStatus) {
            case R.drawable.flag_sale_over:
                //售罄 如果是已经通知过了 则显示取消通知
                currentLeft = ifNotice ? LEFT_ACTION_CANCELNOTICE : LEFT_ACTION_NOTICE;
                currentRight = RIGHT_ACTION_SIMILAR;
                break;
            case R.drawable.flag_sale_stop:
                // 停售商品显示即将发售
                currentLeft = LEFT_ACTION_NONE;
                currentRight = RIGHT_ACTION_COMINGSOON;
                break;
            case R.drawable.flag_sale_withdrawn:
                // 仅显示商品已下架
                currentLeft = LEFT_ACTION_NONE;
                currentRight = RIGHT_ACTION_XIAJIA;
                break;
            case -1:
            default:
                if (shoppingAction == SHOPPINTACTION_NOTSUPPORT_AREA) {
                    //缺货时 显示查看相似
                    currentLeft = LEFT_ACTION_NONE;
                    currentRight = RIGHT_ACTION_SIMILAR;
                } else {
                    if (shoppingAction > SHOPPINGACTION_BOTH) {
                        //在商品详情页点击加入购物袋或立即购买的情况
                        currentLeft = LEFT_ACTION_NONE;
                        currentRight = shoppingAction == SHOPPINGACTION_CART ? RIGHT_ACTION_CART : RIGHT_ACTION_BUY;
                    } else {
                        //默认情况为 添加购物袋和立即购买
                        currentLeft = LEFT_ACTION_CART;
                        currentRight = RIGHT_ACTION_BUY;
                    }
                }
                break;
        }

        if (listener != null)
            listener.onChange(currentLeft, currentRight);

        if (tvSaleOutHint != null)
            tvSaleOutHint.setVisibility(View.GONE);

        if (tvLeft == null || tvRight == null) return;

        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setEnabled(true);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setEnabled(true);
        refreshTvRightWidth(tvRight, true, tvSaleOutHint != null);
        //根据左右状态刷新界面
        switch (currentLeft) {
            case LEFT_ACTION_NONE:
                tvLeft.setVisibility(View.GONE);
                tvLeft.setText(tvLeft.getResources().getString(R.string.add_2_cart));
                refreshTvRightWidth(tvRight, false, tvSaleOutHint != null);
                break;
            case LEFT_ACTION_CART:
                tvLeft.setText(tvLeft.getResources().getString(R.string.add_2_cart));
                break;
            case LEFT_ACTION_NOTICE:
                tvLeft.setText(tvLeft.getResources().getString(R.string.arrival_notice));
                break;
            case LEFT_ACTION_CANCELNOTICE:
                tvLeft.setText(tvLeft.getResources().getString(R.string.arrival_notice_cancel));
                break;
            default:
                tvLeft.setText(tvLeft.getResources().getString(R.string.add_2_cart));
                break;
        }

        switch (currentRight) {
            case RIGHT_ACTION_BUY:
                tvRight.setText(tvRight.getResources().getString(R.string.buy_now));
                break;
            case RIGHT_ACTION_CART:
                tvRight.setText(tvRight.getResources().getString(R.string.add_2_cart));
                break;
            case RIGHT_ACTION_SIMILAR:
                //当查看相似时 这个商品售罄的提示要显示
                if (tvSaleOutHint != null) {
                    tvSaleOutHint.setVisibility(View.VISIBLE);
                    if (shoppingAction == SHOPPINTACTION_NOTSUPPORT_AREA) {
                        tvSaleOutHint.setText(R.string.not_support_area_text);
                    } else {
                        tvSaleOutHint.setText(R.string.sale_over_text);
                    }
                }
                tvRight.setText(tvRight.getResources().getString(R.string.see_similar));
                break;
            case RIGHT_ACTION_COMINGSOON:
                tvRight.setEnabled(false);
                tvRight.setText(tvRight.getResources().getString(R.string.lbl_sale_stop));
                break;
            case RIGHT_ACTION_XIAJIA:
                tvRight.setEnabled(false);
                tvRight.setText(tvRight.getResources().getString(R.string.cart_err_goods_not_exsits));
                break;
            default:
                tvRight.setText(tvRight.getResources().getString(R.string.buy_now));
                break;
        }
    }

}
