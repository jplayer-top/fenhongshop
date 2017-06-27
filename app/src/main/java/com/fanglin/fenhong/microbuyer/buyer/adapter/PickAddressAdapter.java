package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.Address;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/15-上午9:07.
 * 功能描述: 商品详情页选择地址
 */
public class PickAddressAdapter extends BaseAdapter {

    private List<Address> list;
    private Context mContext;
    private String currentSelected;//当前选中的地址

    public PickAddressAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<Address> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(Address address) {
        if (list != null) {
            list.add(address);
            notifyDataSetChanged();
        }
    }

    public void setCurrentSelected(String currentSelected) {
        this.currentSelected = currentSelected;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public Address getDefaultAddress() {
        // 如果存在默认地址就返回默认地址  否则返回第一个地址
        for (Address addr : list) {
            if (addr.isDefault()) {
                return addr;
            }
        }
        return list.get(0);
    }

    @Override
    public Address getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDefault(int position) {
        int c = getCount();
        for (int i = 0; i < c; i++) {
            getItem(i).is_default = (i == position) ? "1" : "0";
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_pickaddress, null);
            new ItemViewHolder(convertView);
        }

        ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
        Address address = getItem(position);

        holder.tvAddr.setText(address.getAddressDescofPick());
        boolean isSelected = address.isDefault();//TextUtils.equals(address.area_info, currentSelected);

        holder.ivCheck.setVisibility(isSelected ? View.VISIBLE : View.INVISIBLE);

        return convertView;
    }


    class ItemViewHolder {

        TextView tvAddr;
        ImageView ivCheck;
        View vLine;

        public ItemViewHolder(View view) {
            tvAddr = (TextView) view.findViewById(R.id.tvAddr);
            ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
            vLine = view.findViewById(R.id.vLine);

            view.setTag(this);
        }
    }
}
