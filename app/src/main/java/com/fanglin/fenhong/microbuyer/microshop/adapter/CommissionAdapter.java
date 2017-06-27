package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.Commission;
import com.fanglin.fenhong.microbuyer.base.model.WSCommission;
import com.fanglin.fenhong.microbuyer.microshop.DepositeActivity;
import com.hb.views.PinnedSectionListView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 我的团队 list适配器
 * Created by admin on 2015/11/5.
 */
public class CommissionAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_PINNED = 1;
    public static final int TYPE_ITEM = 2;

    private Context mContext;
    private ArrayList<Commission> list;
    private WSCommission menuDatas;
    DecimalFormat df;

    private int curIndex;
    Typeface iconFont, iconFontExtra;

    public CommissionAdapter(Context mContext, ArrayList<Commission> list) {
        this.mContext = mContext;
        this.list = list;
        df = new DecimalFormat("#0.00");
        iconFont = BaseFunc.geticonFontType(mContext);
        iconFontExtra = BaseFunc.geticonFontTypeExtra(mContext);
    }

    public ArrayList<Commission> getList() {
        return list;
    }

    public void setList(ArrayList<Commission> list) {
        this.list = list;
    }

    public void setCurIndex(int index) {
        this.curIndex = index;
    }

    public void setMenuDatas(WSCommission menuDatas) {
        this.menuDatas = menuDatas;
    }

    public void addList(ArrayList<Commission> list) {
        if (null != list && list.size() > 0)
            this.list.addAll(list);
    }

    @Override
    public int getCount() {
        if (list == null) return 2;
        return list.size() + 2;
    }

    @Override
    public Commission getItem(int position) {
        return list.get(position - 2);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (convertView == null) {
            if (viewType == TYPE_HEADER) {
                convertView = View.inflate(mContext, R.layout.item_commisson_header, null);
                new HeaderViewHolder(convertView);
            } else if (viewType == TYPE_PINNED) {
                convertView = View.inflate(mContext, R.layout.item_commisson_pinnedsection, null);
                new PinnedViewHolder(convertView);
            } else {
                convertView = View.inflate(mContext, R.layout.item_commission, null);
                new ItemViewHolder(convertView);
            }
        }


        if (viewType == TYPE_HEADER) {
            HeaderViewHolder viewHolder = (HeaderViewHolder) convertView.getTag();
            if (menuDatas != null) {
                viewHolder.tvAccount.setText(df.format(menuDatas.user_acount));
                viewHolder.tvAccountIn.setText(df.format(menuDatas.freeze_deduct));
                viewHolder.tvAccountOut.setText(df.format(menuDatas.available_predeposit));
            }
        } else if (viewType == TYPE_PINNED) {
            PinnedViewHolder viewHolder = (PinnedViewHolder) convertView.getTag();
            viewHolder.llTradeAll.setSelected(curIndex == 0);
            viewHolder.llTradeProcessing.setSelected(curIndex == 1);
            viewHolder.llTradeFinished.setSelected(curIndex == 2);
            viewHolder.llTradeInvalid.setSelected(curIndex == 3);
        } else {
            ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
            Commission commission = getItem(position);
            if (commission != null) {
                viewHolder.tvMoney.setText(String.format(mContext.getString(R.string.yuan_), commission.getGoods_price()));
                viewHolder.tvOrderDate.setText(String.format(mContext.getString(R.string.create_time), commission.getOrder_date()));
                if (!TextUtils.isEmpty(commission.getChange_date())) {
                    viewHolder.tvOrderSettleDate.setText(String.format(mContext.getString(R.string.settle_time), commission.getChange_date()));
                    viewHolder.tvOrderSettleDate.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvOrderSettleDate.setVisibility(View.INVISIBLE);
                }
                viewHolder.tvGoodsName.setText(commission.getGoods_name());
                viewHolder.tvCommission.setText(String.format(mContext.getString(R.string.yuan_), commission.getDeduct_price()));
                viewHolder.tvGoodsNum.setText(commission.getGoodsNum4Display(mContext));
                viewHolder.tvOrderState.setText(commission.getOrder_state());
                viewHolder.tvOrderState.setBackgroundResource(commission.getOrderStateBackground(mContext));
            }
        }

        return convertView;
    }

    class ItemViewHolder {
        private TextView tvOrderDate, tvOrderSettleDate, tvMoney, tvOrderState, tvGoodsName, tvCommission, tvGoodsNum;

        public ItemViewHolder(View convertView) {
            tvOrderDate = (TextView) convertView.findViewById(R.id.tv_order_create_date);
            tvOrderSettleDate = (TextView) convertView.findViewById(R.id.tv_order_settle_date);
            tvOrderState = (TextView) convertView.findViewById(R.id.tv_order_state);
            tvMoney = (TextView) convertView.findViewById(R.id.tv_money);//交易金额
            tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            tvCommission = (TextView) convertView.findViewById(R.id.tv_commission);//奖金
            tvGoodsNum = (TextView) convertView.findViewById(R.id.tv_goods_num);//购买数量
            convertView.setTag(this);
        }
    }

    class HeaderViewHolder {
        RelativeLayout rlWithdraw;
        TextView tvAccount, tvIconWithdraw;
        TextView tvAccountIn, tvAccountOut;

        public HeaderViewHolder(View view) {
            rlWithdraw = (RelativeLayout) view.findViewById(R.id.rlWithdraw);
            tvAccount = (TextView) view.findViewById(R.id.tvAccount);
            tvIconWithdraw = (TextView) view.findViewById(R.id.tvIconWithdraw);
            tvAccountIn = (TextView) view.findViewById(R.id.tvAccountIn);
            tvAccountOut = (TextView) view.findViewById(R.id.tvAccountOut);
            BaseFunc.setFont(tvIconWithdraw);
            rlWithdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoActivity(mContext, DepositeActivity.class, null);
                }
            });
            view.setTag(this);
        }
    }

    class PinnedViewHolder {
        LinearLayout llTradeAll, llTradeProcessing, llTradeFinished, llTradeInvalid;

        public PinnedViewHolder(View view) {
            llTradeAll = (LinearLayout) view.findViewById(R.id.llTradeAll);
            llTradeProcessing = (LinearLayout) view.findViewById(R.id.llTradeProcessing);
            llTradeFinished = (LinearLayout) view.findViewById(R.id.llTradeFinished);
            llTradeInvalid = (LinearLayout) view.findViewById(R.id.llTradeInvalid);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack != null)
                        switch (v.getId()) {
                            case R.id.llTradeAll:
                                mCallBack.onTitleChange(0);
                                break;
                            case R.id.llTradeProcessing:
                                mCallBack.onTitleChange(1);
                                break;
                            case R.id.llTradeFinished:
                                mCallBack.onTitleChange(2);
                                break;
                            case R.id.llTradeInvalid:
                                mCallBack.onTitleChange(3);
                                break;
                        }
                }
            };

            llTradeAll.setOnClickListener(clickListener);
            llTradeProcessing.setOnClickListener(clickListener);
            llTradeFinished.setOnClickListener(clickListener);
            llTradeInvalid.setOnClickListener(clickListener);

            view.setTag(this);
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TYPE_PINNED;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1) {
            return TYPE_PINNED;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setCallBack(TitleChangeCallBack callBack) {
        this.mCallBack = callBack;
    }

    private TitleChangeCallBack mCallBack;

    public interface TitleChangeCallBack {
        void onTitleChange(int index);
    }
}
