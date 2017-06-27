package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-20.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddrViewHolder> {

    private Context mContext;
    private List<Address> list = new ArrayList<> ();

    public AddressAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<Address> lst) {
        this.list = lst;
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public int getItemCount () {
        if (list == null) return 0;
        return list.size ();
    }

    public Address getItem (int position) {
        return list.get (position);
    }

    @Override
    public void onBindViewHolder (final AddrViewHolder holder, final int position) {
        holder.tv_name.setText (getItem (position).name);
        holder.tv_num.setText (BaseFunc.getMaskMobile (getItem (position).mob_phone));
        holder.tv_areainfo.setText (getItem (position).area_info);
        if (TextUtils.equals ("1", getItem (position).is_default)) {
            holder.tv_check.setVisibility (View.VISIBLE);
        } else {
            holder.tv_check.setVisibility (View.GONE);
        }

        holder.itemView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mcb != null) {
                    mcb.onItemClick (position, getItem (position));
                }
            }
        });
        holder.LEdit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mcb != null) {
                    mcb.onEdit (position, getItem (position));
                }
            }
        });
        String addr_cert_num;
        String cert_num = getItem (position).getMaskIdCard ();
        if (TextUtils.isEmpty (cert_num)) {
            addr_cert_num = getItem (position).address;
        } else {
            addr_cert_num = getItem (position).address + "\n" + cert_num;
        }
        holder.tv_addr.setText (addr_cert_num);

        BaseFunc.setFont (holder.LEdit);
    }

    @Override
    public AddrViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = View.inflate (mContext, R.layout.item_address, null);
        return new AddrViewHolder (view);
    }

    public class AddrViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_check;//标志当前地址是否为默认
        public TextView tv_name;//收货人
        public TextView tv_num;//手机号
        public TextView tv_areainfo;//地区信息
        public TextView tv_addr;//详细地址
        public LinearLayout LEdit;//编辑按钮

        public AddrViewHolder (View itemView) {
            super (itemView);
            tv_check = (TextView) itemView.findViewById (R.id.tv_check);
            tv_name = (TextView) itemView.findViewById (R.id.tv_name);
            tv_num = (TextView) itemView.findViewById (R.id.tv_num);
            tv_areainfo = (TextView) itemView.findViewById (R.id.tv_areainfo);
            tv_addr = (TextView) itemView.findViewById (R.id.tv_addr);
            LEdit = (LinearLayout) itemView.findViewById (R.id.LEdit);
        }
    }

    private AddressCallBack mcb;

    public void setCallBack (AddressCallBack cb) {
        this.mcb = cb;
    }

    public interface AddressCallBack {
        void onEdit (int position, Address addr);

        void onItemClick (int position, Address addr);
    }
}
