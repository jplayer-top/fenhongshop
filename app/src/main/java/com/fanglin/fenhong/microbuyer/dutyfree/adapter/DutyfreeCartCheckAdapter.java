package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.event.DutyAddShowEvent;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartCheck;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;
import com.fanglin.fenhong.microbuyer.dutyfree.CartCheckCache;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyAddrListActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeBindAddrActivity;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartCheckAddAddressHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartCheckAddressViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartCheckCalculateViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartCheckFooterViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.DutyCartCheckHeaderViewHolder;
import com.fanglin.fenhong.microbuyer.dutyfree.viewholder.SpliterViewHolder;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import org.json.JSONObject;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/16-下午3:58.
 * 功能描述:急速免税店 核对订单
 */
public class DutyfreeCartCheckAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder, DutyCartCheckFooterViewHolder> {

    public static final int TYPE_HEADER_GOODS = 0;
    public static final int TYPE_HEADER_NULL = 1;
    public static final int TYPE_ITEM_ADDR = 2;
    public static final int TYPE_ITEM_CALCULATE = 3;
    public static final int TYPE_ITEM_ADDADDRESS = 4;
    public boolean isShow;

    private Context context;
    private DutyCartCheck cartCheck;
    private List<DutyCartProduct> goodsList;
    private DutyCartCheckCalculateViewHolder calculateViewHolder;
    private DutyCartCheckAddAddressHolder dutyCartCheckAddAddressHolder;
    private List<DutyAddress> addrList;

    public DutyfreeCartCheckAdapter(Context context) {
        this.context = context;
        isShow = false;
    }

    @Override
    protected int getItemCountForSection(int section) {
        int headerType = getSectionHeaderViewType(section);
        if (headerType == TYPE_HEADER_GOODS) {
            final DutyCartProduct product = goodsList.get(section);
            List<DutyAddress> address = product.getAddress4Logic();
            if (address == null) return 0;
            return address.size();
        } else if (headerType == TYPE_ITEM_ADDADDRESS) {
            return 0;
        } else {
            return 1;
        }
    }

    public String getTotalPriceDesc() {
        if (cartCheck != null) {
            return cartCheck.getTotalPriceDesc();
        }
        return "";
    }

    public String getAllNumDesc() {
        if (cartCheck != null) {
            return cartCheck.getAllNumDesc();
        }
        return "";
    }

    public boolean hasChecked() {
        return cartCheck != null && cartCheck.isHasChecked();
    }

    public void setCartCheck(DutyCartCheck cartCheck) {
        this.cartCheck = cartCheck;
        if (this.cartCheck != null) {
            this.cartCheck.setHasChecked(true);
            this.goodsList = cartCheck.getGoodsList();
        } else {
            this.goodsList = null;
        }
        notifyDataSetChanged();
    }

    public void refreshCartData4BindAddress() {
        //取本地缓存的关联地址
        if (this.goodsList != null && this.goodsList.size() > 0) {
            for (DutyCartProduct product : this.goodsList) {
                String cartId = product.getCartId();
                List<DutyAddress> addrList = CartCheckCache.getAddrList(cartId);
                product.setAddrList(addrList);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 构造CartInfo
     *
     * @return String
     */
    public String getCartInfo() {
        if (this.goodsList != null && this.goodsList.size() > 0) {
            try {
                JSONObject json = new JSONObject();
                for (DutyCartProduct product : this.goodsList) {
                   // List<DutyAddress> addrList = product.getAddrList();
                    JSONObject cartJSON = new JSONObject();
//                    for (DutyAddress addr : addrList) {
//                        cartJSON.put(addr.getAddress_id(), addr.getSelectNum());
//                    }
                    cartJSON.put(addrList.get(0).getAddress_id(), product.getProduct_num());

                    json.put(product.getCartId(), cartJSON);
                }
                return json.toString();
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    protected int getSectionCount() {
        if (goodsList == null) return 0;
        return goodsList.size() + 2;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        int headerType = getSectionHeaderViewType(section);
        return headerType == TYPE_HEADER_GOODS && getItemCountForSection(section) > 0;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER_GOODS) {
            return DutyCartCheckHeaderViewHolder.getHolder(context);
        } else if (viewType == TYPE_ITEM_ADDADDRESS) {
            return DutyCartCheckAddAddressHolder.getHolder(context);
        } else {
            return SpliterViewHolder.getHolder(context);
        }
    }

    @Override
    protected DutyCartCheckFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        //Obl-change
        return DutyCartCheckFooterViewHolder.getHolder(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
//Obl-change
        if (viewType == TYPE_ITEM_ADDR) {
            return DutyCartCheckAddressViewHolder.getHolder(context);
        } else {
            return DutyCartCheckCalculateViewHolder.getHolder(context);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        int headerType = getSectionHeaderViewType(section);
        if (headerType == TYPE_HEADER_NULL) {
            SpliterViewHolder spliter = (SpliterViewHolder) holder;
            spliter.vSpliter.setVisibility(View.GONE);
        } else if (headerType == TYPE_ITEM_ADDADDRESS) {
            dutyCartCheckAddAddressHolder = (DutyCartCheckAddAddressHolder) holder;
            dutyCartCheckAddAddressHolder.refreshView(isShow, null);
            dutyCartCheckAddAddressHolder.llRelevance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(context, DutyAddrListActivity.class, "1");
                }
            });
        } else {
            final DutyCartProduct product = goodsList.get(section);
            DutyCartCheckHeaderViewHolder goodsHolder = (DutyCartCheckHeaderViewHolder) holder;
            goodsHolder.tvAddr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(context, DutyfreeBindAddrActivity.class, product.getCartId());
                }
            });
            goodsHolder.refreshView(product);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(DutyCartCheckFooterViewHolder holder, int section) {
        final DutyCartProduct product = goodsList.get(section);
        holder.refreshView(product.isExpanded());
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setExpanded(!product.isExpanded());
                notifyDataSetChanged();
            }
        });
    }

    private DutyCartCheckCalculateViewHolder.DutyCartCheckCalculateListener calculateListener;

    public void setCalculateListener(DutyCartCheckCalculateViewHolder.DutyCartCheckCalculateListener calculateListener) {
        this.calculateListener = calculateListener;
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int section, final int position) {
        int viewType = getSectionItemViewType(section, position);
        if (viewType == TYPE_ITEM_CALCULATE) {
            calculateViewHolder = (DutyCartCheckCalculateViewHolder) holder;
            calculateViewHolder.refreshView(cartCheck);
            calculateViewHolder.setCalculateListener(calculateListener);
            calculateViewHolder.ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartCheck.setHasChecked(!cartCheck.isHasChecked());
                    notifyDataSetChanged();
                }
            });
        } else {
            DutyCartProduct product = goodsList.get(section);
            final List<DutyAddress> addressList = product.getAddress4Logic();
            final DutyAddress address = addressList.get(position);
            DutyCartCheckAddressViewHolder addrHolder = (DutyCartCheckAddressViewHolder) holder;
            addrHolder.setTotalNum(product.getProduct_num());
            addrHolder.refreshView(address);
            addrHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean hasDeleted = CartCheckCache.removeBindedAddress(address);
                    if (hasDeleted) {
                        addressList.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    protected int getSectionHeaderViewType(int section) {
        int sectionCount = getSectionCount();
        if (section == sectionCount - 1) {
            return TYPE_HEADER_NULL;
        } else if (section == sectionCount - 2) {
            return TYPE_ITEM_ADDADDRESS;
        } else {
            return TYPE_HEADER_GOODS;
        }
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        int headerViewType = getSectionHeaderViewType(section);
        if (headerViewType == TYPE_HEADER_NULL) {
            return TYPE_ITEM_CALCULATE;
        } else {
            return TYPE_ITEM_ADDR;
        }
    }

    @Override
    protected boolean isSectionHeaderViewType(int viewType) {
        return viewType == TYPE_HEADER_NULL || viewType == TYPE_HEADER_GOODS || viewType == TYPE_ITEM_ADDADDRESS;
    }

    public boolean checkBind() {
      /*  if (goodsList == null || goodsList.size() == 0) return false;
        for (DutyCartProduct product : goodsList) {
            int totalNum = product.getProduct_num();
            List<DutyAddress> addresses = product.getAddrList();
            int num = 0;
            if (addresses != null && addresses.size() > 0) {
                for (DutyAddress address : addresses) {
                    num += address.getSelectNum();
                }
            }
            if (num != totalNum) {
                return false;
            }
        }
        return true;*/
        if (addrList == null || addrList.size() == 0) {
            return false;
        }
        return true;
    }

    public int getBalanceSelected() {
        if (cartCheck == null) return 0;
        return cartCheck.getIs_selected();
    }

    public void showAddress(DutyAddShowEvent event) {
        isShow = true;
        List<DutyAddress> addrList = CartCheckCache.getAddrList("1");
        this.addrList = addrList;
        dutyCartCheckAddAddressHolder.refreshView(isShow, addrList.get(0));
        dutyCartCheckAddAddressHolder.llArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartCheckCache.removeLocalAddress();
                BaseFunc.gotoActivity(context, DutyAddrListActivity.class, "1");
            }
        });
        notifyDataSetChanged();
    }

}
