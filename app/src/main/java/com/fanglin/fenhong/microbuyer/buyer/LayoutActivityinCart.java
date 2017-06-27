package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baselib.CartGoodsComparator;
import com.fanglin.fenhong.microbuyer.base.model.CalculateData;
import com.fanglin.fenhong.microbuyer.base.model.Carts;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromMansongRules;
import com.fanglin.fenhong.microbuyer.base.model.GoodsinCart;
import com.fanglin.fenhong.microbuyer.base.model.RenxuanRule;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutManjianAll;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/31.
 * 购物车活动处理类
 */
public class LayoutActivityinCart implements View.OnClickListener {

    private View view, vRenxuan;
    private Context mContext;


    //包邮
    LinearLayout LBaoyou, LManjianAll, LRenxuan;
    TextView tvBaoyou, tvIconBaoyou, tvRenxuanIcon, tvRenxuan;

    LinearLayout LShow;

    private DecimalFormat df;
    private CalculateData calculateData;

    private String storeId;

    public LayoutActivityinCart(Context c) {
        mContext = c;
        df = new DecimalFormat("#0.00");

        view = View.inflate(mContext, R.layout.layout_activity_in_carts, null);
        LShow = (LinearLayout) view.findViewById(R.id.LShow);

        LBaoyou = (LinearLayout) view.findViewById(R.id.LBaoyou);
        tvBaoyou = (TextView) view.findViewById(R.id.tvBaoyou);
        tvIconBaoyou = (TextView) view.findViewById(R.id.tvIconBaoyou);

        LManjianAll = (LinearLayout) view.findViewById(R.id.LManjianAll);

        vRenxuan = view.findViewById(R.id.vRenxuan);
        LRenxuan = (LinearLayout) view.findViewById(R.id.LRenxuan);
        tvRenxuanIcon = (TextView) view.findViewById(R.id.tvRenxuanIcon);
        tvRenxuan = (TextView) view.findViewById(R.id.tvRenxuan);

        vRenxuan.setVisibility(View.GONE);
        LRenxuan.setVisibility(View.GONE);
        BaseFunc.setFont(tvRenxuanIcon);
        LRenxuan.setOnClickListener(this);
        tvRenxuan.setText("");

        LShow.setVisibility(View.VISIBLE);

        BaseFunc.setFont(tvIconBaoyou);
        LBaoyou.setOnClickListener(this);

        LManjianAll.setOnClickListener(this);
    }

    public void setData(CalculateData calculateData, Carts carts, boolean isEditMode) {
        this.calculateData = calculateData;
        if (calculateData != null) {
            storeId = calculateData.store_id;
            if (carts.hasActivity) {
                view.setVisibility(View.VISIBLE);

                /**
                 * 包邮活动
                 */
                double freight = carts.storeFreeFreight;
                if (freight > 0) {
                    LBaoyou.setVisibility(View.VISIBLE);
                } else {
                    LBaoyou.setVisibility(View.GONE);
                }
                this.calculateData.free_freight_limit = freight;
                double left = freight - calculateData.money;
                left = left < 0 ? 0 : left;
                if (left > 0) {
                    tvBaoyou.setText(String.format("还差%1s元就可以包邮", df.format(left)));
                } else {
                    tvBaoyou.setText("已包邮");
                }

                /**
                 * 满送活动
                 */
                LManjianAll.removeAllViews();
                List<GoodsDtlPromMansongRules> manlist = carts.mangSongRule;
                if (manlist != null && manlist.size() > 0) {
                    LManjianAll.setVisibility(View.VISIBLE);
                    this.calculateData.manlist = manlist;
                    LayoutManjianAll manjianAll = new LayoutManjianAll(mContext);
                    manjianAll.setiSGoodsDtl(false);
                    manjianAll.setList(manlist);
                    LManjianAll.addView(manjianAll.getView());
                } else {
                    LManjianAll.setVisibility(View.GONE);
                }


                /**
                 * 满即选
                 */

                RenxuanRule renxuanRule = carts.renxuanRule;
                if (renxuanRule != null && renxuanRule.rulesSize() > 0) {
                    RenxuanRule.RuleAndTips ruleAndTips = renxuanRule.getTips(calculateData.xnum);
                    String tips = ruleAndTips.tips;

                    if (!TextUtils.isEmpty(tips)) {
                        tvRenxuan.setText(tips);
                    }

                    vRenxuan.setVisibility(View.VISIBLE);
                    LRenxuan.setVisibility(View.VISIBLE);
                } else {
                    vRenxuan.setVisibility(View.GONE);
                    LRenxuan.setVisibility(View.GONE);
                }
            } else {
                view.setVisibility(View.GONE);
            }
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LBaoyou:
                BaseFunc.gotoActivity(mContext, FreeFreightActivity.class, new Gson().toJson(calculateData));
                break;
            case R.id.LManjianAll:
                BaseFunc.gotoActivity(mContext, MansongActivity.class, new Gson().toJson(calculateData));
                break;
            case R.id.LRenxuan:
                String url = String.format(BaseVar.URL_RENXUAN, null, storeId);
                BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, url);
                break;
        }
    }

    public View getView() {
        return view;
    }

    /**
     * 算单个店铺关于满任选的优惠
     * Added By Plucky 2016-08-19 15:40
     *
     * @param list         店铺商品列表
     * @param renxuanNum   当前满足的任选数量
     * @param renxuanMoney 当前任选活动需要支付的钱
     * @param isEditMode   是否处于编辑模式（编辑模式下不需要计算）
     * @return $
     */
    public static double getStoreYouhui(List<GoodsinCart> list, int renxuanNum, double renxuanMoney, boolean isEditMode) {
        if (list != null && list.size() > 0) {
            Object[] sortArray = list.toArray();
            Arrays.sort(sortArray, new CartGoodsComparator(true));

            int deleteNum = 0;//已经算过的数量
            double goodsmoney = 0;
            for (Object goodsObj : sortArray) {
                GoodsinCart goods = (GoodsinCart) goodsObj;
                //如果当前商品是满任选商品且已经选中了
                int leftNum = renxuanNum - deleteNum;
                if (goods.getIsSelected(isEditMode) && goods.isNyuanRenxuan() && leftNum > 0) {
                    if (goods.goods_num <= leftNum) {
                        deleteNum += goods.goods_num;
                        goodsmoney += goods.goods_price * goods.goods_num;
                    } else {
                        deleteNum += leftNum;
                        goodsmoney += leftNum * goods.goods_price;
                    }
                }
            }
            double youhui = goodsmoney - renxuanMoney;
            return youhui >= 0 ? youhui : 0;
        } else {
            return 0;
        }
    }
}
