package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDetail;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromXianshi;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.MiaoshaTag;
import com.fanglin.fenhong.microbuyer.buyer.GoodsDetailsActivity;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsSpecAdapter;
import com.fanglin.fhui.FHDialog;

import java.text.DecimalFormat;


/**
 * 商品规格选择
 * 第四次重构
 * Added By plucky 2016-4-27 20:19
 */
public class LayoutGoodsSpec implements View.OnClickListener, GoodsSpecAdapter.onSpecCallBack, ActionChangeListener {

    LinearLayout vSpec;
    GoodsDetail _detail;
    Member member;
    GoodsDetailsActivity goodsDetailsActivity;
    FHDialog dlg;

    private ImageView ivImage;
    private TextView tvSpec, tvPrice, tvStorage;
    private RecyclerView recyclerView;
    private View vSpecLine, vLine;
    private TextView tvBuyNum;
    private TextView tvGoodsPromHint;

    private TextView tvTips;
    public TextView tvLeft, tvRight;

    GoodsSpecAdapter goodsSpecAdapter;

    private int storageLimit;
    private int buyNum = 1;
    private int shoppingAction = GoodsButtonHandler.SHOPPINGACTION_BOTH;

    DecimalFormat df;
    int screenHeight;

    public void setMember(Member member) {
        this.member = member;
    }

    public LayoutGoodsSpec(GoodsDetailsActivity goodsDetailsActivity) {

        this.goodsDetailsActivity = goodsDetailsActivity;
        df = new DecimalFormat("¥#0.00");
        screenHeight = BaseFunc.getDisplayMetrics(goodsDetailsActivity).heightPixels;

        vSpec = (LinearLayout) View.inflate(goodsDetailsActivity, R.layout.layout_goods_spec, null);

        dlg = new FHDialog(goodsDetailsActivity);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setBotView(vSpec, 0);


        ivImage = (ImageView) vSpec.findViewById(R.id.ivImage);
        tvSpec = (TextView) vSpec.findViewById(R.id.tvSpec);
        tvPrice = (TextView) vSpec.findViewById(R.id.tvPrice);
        tvStorage = (TextView) vSpec.findViewById(R.id.tvStorage);
        recyclerView = (RecyclerView) vSpec.findViewById(R.id.recyclerView);
        vSpecLine = vSpec.findViewById(R.id.vSpecLine);
        vLine = vSpec.findViewById(R.id.vLine);

        tvGoodsPromHint = (TextView) vSpec.findViewById(R.id.tvGoodsPromHint);

        tvBuyNum = (TextView) vSpec.findViewById(R.id.tvBuyNum);

        tvTips = (TextView) vSpec.findViewById(R.id.tvTips);
        tvLeft = (TextView) vSpec.findViewById(R.id.tvLeft);
        tvRight = (TextView) vSpec.findViewById(R.id.tvRight);

        TextView tvClose = (TextView) vSpec.findViewById(R.id.tvClose);
        TextView tvMinus = (TextView) vSpec.findViewById(R.id.tvMinus);
        TextView tvPlus = (TextView) vSpec.findViewById(R.id.tvPlus);

        BaseFunc.setFont(tvClose);
        tvMinus.setOnClickListener(this);
        tvPlus.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvClose.setOnClickListener(this);

        goodsSpecAdapter = new GoodsSpecAdapter(goodsDetailsActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(goodsDetailsActivity));
        recyclerView.setAdapter(goodsSpecAdapter);
        goodsSpecAdapter.setCallBack(this);
    }

    /**
     * @param actionId GoodsButtonHandler.SHOPPINGACTION
     */
    public void show(int actionId) {
        //如果数据未传输过来则直接放弃操作
        if (_detail == null) return;

        shoppingAction = actionId;

        GoodsButtonHandler.refreshBtnStatus(_detail.getGoodsSaleState(), TextUtils.equals(_detail.if_notice, "1"), tvLeft, tvRight, null, shoppingAction, this);
        /** 海外商品2000的提示*/
        refreshGlobalTips();

        int h;
        if (goodsSpecAdapter.getItemCount() > 0) {
            h = screenHeight * 2 / 3;
        } else {
            h = screenHeight / 2;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        vSpec.setLayoutParams(params);
        dlg.show();
    }


    /**
     * 处理活动引起的价格及库存的显示逻辑
     * 按照产品经理的规定：
     * 如果商品参加了活动，则不会参加秒杀活动，属互斥事件
     * Added By Plucky 2016-11-02 10:04
     */
    private void handleActivity() {
        if (_detail == null) return;
        GoodsDtlPromXianshi xianshi = _detail.xianshi;
        MiaoshaTag tag = _detail.seckilling;
        double priceShow;
        int storageShow;
        boolean isNormalStorage;
        if (xianshi != null) {
            if (buyNum < xianshi.lower_limit) {
                priceShow = _detail.goods_price;
                tvGoodsPromHint.setText("");
            } else {
                priceShow = xianshi.price;
                tvGoodsPromHint.setText(xianshi.getLowerLimitText());
            }
            storageLimit = _detail.goods_storage;
            storageShow = _detail.goods_storage;
            isNormalStorage = true;
        } else if (tag != null) {
            MiaoshaTag.Miaosha4Display mdisplay = tag.getDisplay(buyNum, _detail.goods_storage, _detail.goods_price);
            priceShow = mdisplay.price;
            storageLimit = mdisplay.storageLimit;
            storageShow = mdisplay.storageShow;
            isNormalStorage = mdisplay.isNormalStorage;
        } else {
            priceShow = _detail.goods_price;
            storageLimit = _detail.goods_storage;
            storageShow = _detail.goods_storage;
            isNormalStorage = true;
        }

        if (storageShow > 0) {
            String goods_storage_fmt = (isNormalStorage ? "库存: " : "秒杀库存: ") + storageShow + "件";
            tvStorage.setText(goods_storage_fmt);
        } else {
            tvStorage.setText("商品已售罄");
        }
        tvPrice.setText(df.format(priceShow));
    }

    public void setData(GoodsDetail detail) {
        _detail = detail;
        lastGoodsId = _detail.goods_id;

        /** 图片显示*/
        new FHImageViewUtil(ivImage).setImageURI(_detail.getAGoodsImage(), FHImageViewUtil.SHOWTYPE.DEFAULT);

        /** 商品规格*/
        if (goodsSpecAdapter.getList() == null) {
            goodsSpecAdapter.setSpecRelation(_detail.spec_relation);
            goodsSpecAdapter.setJson(_detail.spec_list, _detail.goods_spec);
            recyclerView.getAdapter().notifyDataSetChanged();
            if (_detail.spec_list == null || _detail.spec_list.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                tvSpec.setText("");
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvSpec.setText(goodsSpecAdapter.getTitles());
            }
            vSpecLine.setVisibility(recyclerView.getVisibility());
        }

        /** 已选数量的显示*/
        tvBuyNum.setText(String.valueOf(buyNum));

        GoodsButtonHandler.refreshBtnStatus(_detail.getGoodsSaleState(), TextUtils.equals(_detail.if_notice, "1"), tvLeft, tvRight, null, shoppingAction, this);

        /** 影响价格及库存的显示*/
        handleActivity();
        /** 海外商品2000的提示*/
        refreshGlobalTips();
    }


    private String lastGoodsId;

    @Override
    public void onSelected(final String goods_id, String tags) {
        if (goods_id == null) return;
        //防止重复点击刷新数据
        if (TextUtils.equals(goods_id, lastGoodsId)) return;

        if (listener != null) listener.onSelected(goods_id);
        this.buyNum = 1;
        tvBuyNum.setText("1");
    }

    @Override
    public void onClick(View v) {
        int buyNum = Integer.valueOf(tvBuyNum.getText().toString());
        switch (v.getId()) {
            case R.id.tvClose:
                if (dlg != null) {
                    dlg.dismiss();
                }
                break;
            case R.id.tvMinus:
                buyNum--;
                if (buyNum < 1) {
                    buyNum = 1;
                }
                tvBuyNum.setText(String.valueOf(buyNum));
                this.buyNum = buyNum;

                handleActivity();
                refreshGlobalTips();

                if (listener != null) {
                    listener.onBuyNumChanged(buyNum);
                }
                break;
            case R.id.tvPlus:
                buyNum++;
                if (buyNum > storageLimit) {
                    buyNum = storageLimit;
                }
                if (buyNum == 0) buyNum = 1;
                tvBuyNum.setText(String.valueOf(buyNum));
                this.buyNum = buyNum;

                handleActivity();
                refreshGlobalTips();

                if (listener != null) {
                    listener.onBuyNumChanged(buyNum);
                }
                break;
            case R.id.tvLeft:
                onLeftAction();
                break;
            case R.id.tvRight:
                onRightAction();
                break;
            default:
                break;
        }
    }

    /**
     * 确认
     *
     * @param buy_goods_id 商品id
     * @param buy_num      购买数量
     * @param isBuy        是否为购买
     */
    private void doCommit(String buy_goods_id, int buy_num, boolean isBuy) {
        if (goodsDetailsActivity.member == null) {
            BaseFunc.gotoLogin(goodsDetailsActivity);
            return;
        }

        if (storageLimit == 0) {
            BaseFunc.showMsg(goodsDetailsActivity, goodsDetailsActivity.getString(R.string.goods_storage_0));
            return;
        }

        /** 0 已下架  1 正常 */
        if (_detail.goods_state != 1) {
            BaseFunc.showSelfToast(goodsDetailsActivity, goodsDetailsActivity.getString(R.string.cart_err_goods_not_exsits));
            return;
        }

        if (_detail.sale_stop == 1) {
            /** 出现商品停售的*/
            BaseFunc.showSelfToast(goodsDetailsActivity, goodsDetailsActivity.getString(R.string.tips_goods_waiting));
            return;
        }

        if (tvTips.getVisibility() == View.VISIBLE) {
            return;
        }

        //有规格选择的情况下
        if (goodsSpecAdapter.getItemCount() > 0) {
            //给出相应的提示 必须要选择哪些规格
            if (!goodsSpecAdapter.checkTips(true)) {
                return;
            }

            buy_goods_id = goodsSpecAdapter.getGoodsId();
        }

        if (dlg != null) {
            dlg.dismiss();
        }

        if (isBuy) {
            if (listener != null) {
                listener.onBuy(buy_goods_id, buy_num);
            }
        } else {
            if (listener != null) {
                listener.onAdd(buy_goods_id, buy_num);
            }
        }
    }


    public void setCallBack(SpecListener l) {
        this.listener = l;
    }

    private SpecListener listener;

    public interface SpecListener {
        void onBuy(String goodsid, int num);

        void onAdd(String goodsid, int num);

        void onSelected(String goodsid);

        void onAddArrivalNotice();

        void onCancelArrivalNotice();

        void onSeeSimilarGoods();

        void onBuyNumChanged(int buyNum);
    }

    /**
     * 左侧按钮点击事件
     */
    private void onLeftAction() {
        switch (currentLeft) {
            case GoodsButtonHandler.LEFT_ACTION_CART:
                doCommit(_detail.goods_id, buyNum, false);
                break;
            case GoodsButtonHandler.LEFT_ACTION_NOTICE:
                if (listener != null) {
                    listener.onAddArrivalNotice();
                }
                if (dlg != null) {
                    dlg.dismiss();
                }
                break;
            case GoodsButtonHandler.LEFT_ACTION_CANCELNOTICE:
                if (listener != null) {
                    listener.onCancelArrivalNotice();
                }
                if (dlg != null) {
                    dlg.dismiss();
                }
                break;
            default:
                doCommit(_detail.goods_id, buyNum, false);
                break;
        }
    }

    /**
     * 右侧按钮点击事件
     */
    private void onRightAction() {
        switch (currentRight) {
            case GoodsButtonHandler.RIGHT_ACTION_BUY:
                doCommit(_detail.goods_id, buyNum, true);
                break;
            case GoodsButtonHandler.RIGHT_ACTION_CART:
                doCommit(_detail.goods_id, buyNum, false);
                break;
            case GoodsButtonHandler.RIGHT_ACTION_SIMILAR:
                if (listener != null) {
                    listener.onSeeSimilarGoods();
                }
                if (dlg != null) {
                    dlg.dismiss();
                }
                break;
            default:
                doCommit(_detail.goods_id, buyNum, true);
                break;
        }
    }

    /**
     * 刷新直邮商品限购的提示
     */
    private void refreshGlobalTips() {
        double money = buyNum * _detail.goods_price;

        if ((_detail.goods_source > 0) && (money > _detail.oversea_per_purchase_limit) && (_detail.oversea_per_purchase_limit > 0)) {
            String alimit = df.format(_detail.oversea_per_purchase_limit);
            tvTips.setVisibility(View.VISIBLE);
            tvTips.setText(String.format(goodsDetailsActivity.getString(R.string.hint_seatax), alimit, alimit));
            vLine.setVisibility(View.VISIBLE);
        } else {
            tvTips.setVisibility(View.INVISIBLE);
            tvTips.setText("");
            vLine.setVisibility(View.GONE);
        }

        //如果超过了2000的提示
        boolean isOverGlobal = tvTips.getVisibility() == View.VISIBLE;

        if (currentLeft == GoodsButtonHandler.LEFT_ACTION_CART && isOverGlobal) {
            tvLeft.setEnabled(false);
        } else {
            tvLeft.setEnabled(true);
        }

        if ((currentRight == GoodsButtonHandler.RIGHT_ACTION_BUY
                || currentRight == GoodsButtonHandler.RIGHT_ACTION_CART) && isOverGlobal) {
            tvRight.setEnabled(false);
        } else {
            if (currentRight == GoodsButtonHandler.RIGHT_ACTION_COMINGSOON
                    || currentRight == GoodsButtonHandler.RIGHT_ACTION_XIAJIA) {
                tvRight.setEnabled(false);
            } else {
                tvRight.setEnabled(true);
            }
        }
    }

    private int currentLeft, currentRight;

    @Override
    public void onChange(int leftAction, int rightAction) {
        currentLeft = leftAction;
        currentRight = rightAction;
    }
}
