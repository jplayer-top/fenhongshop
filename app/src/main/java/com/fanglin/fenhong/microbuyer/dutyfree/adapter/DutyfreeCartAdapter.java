package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCart;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartEditResult;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartFooterViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartGoodsViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.SpliterViewHolder;
import com.fanglin.fhlib.other.FHLog;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import org.json.JSONObject;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-上午11:36.
 * 功能描述: 极速免税店 购物车适配器
 */
public class DutyfreeCartAdapter extends SectionedRecyclerViewAdapter<SpliterViewHolder, DutyCartGoodsViewHolder, DutyCartFooterViewHolder> implements DutyCartGoodsViewHolder.CartGoodsDataChangeListener {

    private Context mContext;
    private DutyCart cart;
    private Member member;

    public DutyfreeCartAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected int getItemCountForSection(int section) {
        List<DutyCartProduct> goodsList = cart.getGoodsList();
        if (goodsList == null) return 0;
        return goodsList.size();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setCart(DutyCart cart) {
        this.cart = cart;
        notifyDataSetChanged();
    }

    public DutyCart getCart() {
        return cart;
    }

    @Override
    protected int getSectionCount() {
        if (cart == null) return 0;
        List<DutyCartProduct> goodsList = cart.getGoodsList();
        if (goodsList == null || goodsList.size() == 0) return 0;
        return 1;
    }

    public boolean getSelectedAll() {
        if (cart == null) return false;
        List<DutyCartProduct> goodsList = cart.getGoodsList();
        if (goodsList == null || goodsList.size() == 0) return false;

        for (DutyCartProduct goods : goodsList) {
            if (!goods.isSelected()) {
                return false;
            }
        }
        return true;
    }

    public void selectAll(boolean seleced) {
        if (cart == null) return;
        List<DutyCartProduct> goodsList = cart.getGoodsList();
        if (goodsList == null || goodsList.size() == 0) return;

        for (DutyCartProduct goods : goodsList) {
            goods.setSelected(seleced);
        }
    }

    public String getSelectedCartIds() {
        if (cart == null) return "";
        List<DutyCartProduct> goodsList = cart.getGoodsList();
        if (goodsList == null || goodsList.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (DutyCartProduct goods : goodsList) {
            if (goods.isSelected()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(goods.getCartId());
            }
        }
        return sb.toString();
    }

    public String getCartInfo() {
        if (cart == null) return "";
        List<DutyCartProduct> goodsList = cart.getGoodsList();
        if (goodsList == null || goodsList.size() == 0) return "";
        JSONObject json = new JSONObject();
        for (DutyCartProduct goods : goodsList) {
            if (goods.isSelected()) {
                try {
                    json.put(goods.getCartId(), goods.getProduct_num());
                } catch (Exception e) {
                    FHLog.d("Plucky", e.getMessage());
                }
            }
        }
        return json.toString();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected SpliterViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return SpliterViewHolder.getHolder(mContext);
    }

    @Override
    protected DutyCartFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return DutyCartFooterViewHolder.getHolder(mContext);
    }

    @Override
    protected DutyCartGoodsViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return DutyCartGoodsViewHolder.getHolder(mContext, member);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(SpliterViewHolder holder, int section) {
        holder.vSpliter.setVisibility(View.GONE);
    }

    @Override
    protected void onBindSectionFooterViewHolder(DutyCartFooterViewHolder holder, int section) {
        holder.refreshView(cart);
    }

    @Override
    protected void onBindItemViewHolder(DutyCartGoodsViewHolder holder, final int section, int position) {
        List<DutyCartProduct> goodsList = cart.getGoodsList();
        final DutyCartProduct product = goodsList.get(position);
        holder.setGoodsListener(goodsListener);
        holder.setGoodsChangeListener(this);
        holder.refreshView(product);
    }

    public void refreshByEditResult(DutyCartEditResult result) {
        if (cart != null && result != null) {
            cart.refreshByEditResult(result);
            notifyDataSetChanged();
        }
    }

    private DutyCartGoodsViewHolder.DutyCartGoodsListener goodsListener;

    public void setGoodsListener(DutyCartGoodsViewHolder.DutyCartGoodsListener goodsListener) {
        this.goodsListener = goodsListener;
    }

    @Override
    public void onCartGoodsChange(DutyCartEditResult result) {
        refreshByEditResult(result);
    }
}
