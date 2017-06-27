package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.BankCard;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-11-1.
 */
public class BankCardAdapter extends BaseAdapter {
    private Context mContext;
    private List<BankCard> list;

    public BankCardAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<BankCard> list) {
        this.list = list;
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public BankCard getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_bankcard, null);
            new ViewHolder (convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag ();
        BaseFunc.setFont (holder.LIcon);
        holder.tv_icon.setText (BaseFunc.getBankIconByName (mContext, getItem (position).bank_name));
        holder.tv_cardinfo.setText (getItem (position).getTitle ());
        if (getItem (position).getDefault ()) {
            holder.tv_check.setTextColor (mContext.getResources ().getColor (R.color.fh_red));
        } else {
            holder.tv_check.setTextColor (mContext.getResources ().getColor (R.color.color_bb));
        }

        holder.tv_edit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mcb != null) {
                    mcb.onEdit (getItem (position));
                }
            }
        });

        if (position == getCount () - 1) {
            holder.vline.setVisibility (View.GONE);
        } else {
            holder.vline.setVisibility (View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        public LinearLayout LIcon;
        public TextView tv_icon;
        public TextView tv_cardinfo, tv_check, tv_edit;
        public View vline;

        public ViewHolder (View view) {
            LIcon = (LinearLayout) view.findViewById (R.id.LIcon);
            tv_icon = (TextView) view.findViewById (R.id.tv_icon);
            tv_cardinfo = (TextView) view.findViewById (R.id.tv_cardinfo);
            tv_check = (TextView) view.findViewById (R.id.tv_check);
            tv_edit = (TextView) view.findViewById (R.id.tv_edit);
            vline = view.findViewById (R.id.vline);
            view.setTag (this);
        }
    }

    private BCAdapterCallBack mcb;

    public void setCallBack (BCAdapterCallBack cb) {
        this.mcb = cb;
    }

    public interface BCAdapterCallBack {
        void onEdit (BankCard bankCard);
    }
}
