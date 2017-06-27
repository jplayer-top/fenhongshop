package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromMansongRules;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/4/27-下午2:05.
 * 功能描述: 满减活动拆分
 */
public class LayoutManjianAll {
    private Context mContext;
    LinearLayout view;
    private boolean isGoodsDtl = true;//是否在购物车 商品详情

    public LayoutManjianAll(Context context) {
        this.mContext = context;
        view = (LinearLayout) View.inflate(context, R.layout.layout_manjian_all, null);
    }

    public void setiSGoodsDtl(boolean isGoodsDtl) {
        this.isGoodsDtl = isGoodsDtl;
    }

    public void setList(List<GoodsDtlPromMansongRules> list) {
        view.removeAllViews();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final GoodsDtlPromMansongRules rules = list.get(i);
                if (rules.getMinus_amount() > 0) {
                    View vMinus;
                    if (isGoodsDtl) {
                        vMinus = View.inflate(mContext, R.layout.item_manjian, null);

                    } else {
                        vMinus = View.inflate(mContext, R.layout.item_manjiancart, null);

                        View vManjian = vMinus.findViewById(R.id.vManjian);
                        if (i == list.size() - 1) {
                            vManjian.setVisibility(View.INVISIBLE);
                        } else {
                            vManjian.setVisibility(View.VISIBLE);
                        }
                    }

                    TextView tvIcon = (TextView) vMinus.findViewById(R.id.tvIcon);
                    BaseFunc.setFont(tvIcon);

                    TextView tvManjianLbl = (TextView) vMinus.findViewById(R.id.tvManjianLbl);
                    TextView tvManjian = (TextView) vMinus.findViewById(R.id.tvManjian);

                    tvManjianLbl.setText("满减");
                    tvManjian.setText(rules.getManjian(mContext));

                    if (isGoodsDtl) {
                        tvIcon.setVisibility(View.INVISIBLE);
                    } else {
                        tvIcon.setVisibility(View.VISIBLE);
                    }

                    view.addView(vMinus);
                }

                if (rules.gift != null) {
                    View vSong;
                    if (isGoodsDtl) {
                        vSong = View.inflate(mContext, R.layout.item_manjian, null);
                    } else {
                        vSong = View.inflate(mContext, R.layout.item_manjiancart, null);
                        View vManjian = vSong.findViewById(R.id.vManjian);
                        if (i == list.size() - 1) {
                            vManjian.setVisibility(View.INVISIBLE);
                        } else {
                            vManjian.setVisibility(View.VISIBLE);
                        }
                    }

                    TextView tvIcon = (TextView) vSong.findViewById(R.id.tvIcon);
                    BaseFunc.setFont(tvIcon);
                    tvIcon.setVisibility(View.VISIBLE);

                    TextView tvManjian = (TextView) vSong.findViewById(R.id.tvManjian);
                    TextView tvManjianLbl = (TextView) vSong.findViewById(R.id.tvManjianLbl);

                    tvManjianLbl.setText("满赠");
                    tvManjian.setText(rules.getMansong(mContext));
                    if (isGoodsDtl) {
                        vSong.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BaseFunc.gotoGoodsDetail(mContext, rules.gift.goods_id, null,null);
                            }
                        });
                    }
                    view.addView(vSong);
                }
            }
        }
    }

    public View getView() {
        return view;
    }
}
