package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;

/**
 * 国家馆 商品列表 子布局 筛选头部 只作为 UI展示，渲染， 不处理数据
 * Created by lizhixin on 2015/11/30.
 */
public class LayoutNavFilter implements View.OnClickListener {

    private TextView tvPriceUp, tvPriceDown;
    public LinearLayout llFilterHeader;

    private LinearLayout view;

    private int curFilterIndex = 1;//筛选条件下标 1 综合，2 销量，3 价格，4人气
    private int curPriceFilter = 2;//价格下标

    OnItemClickCallBack clickCallBack;

    public void setClickCallBack(OnItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public LayoutNavFilter(Context c) {
        view = (LinearLayout) View.inflate(c, R.layout.layout_top_national_pav_goods_list, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BaseFunc.dip2px(c, 50));
        view.setLayoutParams(params);

        initView(view);
    }

    private void initView(ViewGroup view) {
        tvPriceUp = (TextView) view.findViewById(R.id.tv_price_arrow_up);
        tvPriceDown = (TextView) view.findViewById(R.id.tv_price_arrow_down);
        BaseFunc.setFont(tvPriceUp);
        BaseFunc.setFont(tvPriceDown);

        llFilterHeader = (LinearLayout) view.findViewById(R.id.ll_filter_header);
        TextView tvFilterDefault = (TextView) view.findViewById(R.id.tv_filter_default);
        TextView tvFilterSalsnum = (TextView) view.findViewById(R.id.tv_filter_salesnum);
        TextView tvFilterPrice = (TextView) view.findViewById(R.id.tv_filter_price);
        TextView tvFilterPopular = (TextView) view.findViewById(R.id.tv_filter_popular);
        tvFilterDefault.setOnClickListener(this);
        tvFilterSalsnum.setOnClickListener(this);
        tvFilterPrice.setOnClickListener(this);
        tvFilterPopular.setOnClickListener(this);
        tvFilterDefault.setSelected(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_filter_default:
                if (curFilterIndex != 1) {
                    changeFiltersBg(1, curPriceFilter);
                    if (clickCallBack != null) {
                        clickCallBack.onItemCLick(curFilterIndex, curPriceFilter);
                    }
                }
                break;
            case R.id.tv_filter_salesnum:
                if (curFilterIndex != 2) {
                    changeFiltersBg(2, curPriceFilter);
                    if (clickCallBack != null) {
                        clickCallBack.onItemCLick(curFilterIndex, curPriceFilter);
                    }
                }
                break;
            case R.id.tv_filter_price:
                if (curPriceFilter == 1) {
                    curPriceFilter = 2;
                } else {
                    curPriceFilter = 1;
                }
                changeFiltersBg(3, curPriceFilter);
                if (clickCallBack != null) {
                    clickCallBack.onItemCLick(curFilterIndex, curPriceFilter);
                }
                break;
            case R.id.tv_filter_popular:
                if (curFilterIndex != 4) {
                    changeFiltersBg(4, curPriceFilter);
                    if (clickCallBack != null) {
                        clickCallBack.onItemCLick(curFilterIndex, curPriceFilter);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 改变 筛选条件的背景
     */
    public void changeFiltersBg(int index, int priceIndex) {
        curFilterIndex = index;
        for (int i = 0; i < 4; i++) {
            llFilterHeader.getChildAt(i).setSelected(false);
        }
        llFilterHeader.getChildAt(index - 1).setSelected(true);

        //价格 升降 选项
        if (index == 3) {
            tvPriceUp.setSelected(priceIndex == 1);
            tvPriceDown.setSelected(priceIndex == 2);
        } else {
            tvPriceUp.setSelected(false);
            tvPriceDown.setSelected(false);
        }
    }

    public LinearLayout getView() {
        return view;
    }

    /**
     * 回掉接口，更新双胞胎中另一方的UI
     */
    public interface OnItemClickCallBack {

        void onItemCLick(int index, int priceIndex);
    }


}
