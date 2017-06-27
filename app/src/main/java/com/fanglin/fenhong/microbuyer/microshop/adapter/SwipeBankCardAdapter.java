package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.BankCard;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/4.
 */
public class SwipeBankCardAdapter extends BaseSwipeAdapter {
    private List<BankCard> list;
    private Context mContext;

    public SwipeBankCardAdapter (Context c) {
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
    public void fillValues (final int position, View convertView) {
        ViewHolder holder = (ViewHolder) convertView.getTag ();
        holder.tv_icon.setText (BaseFunc.getBankIconByName (mContext, getItem (position).bank_name));
        holder.tv_cardinfo.setText (getItem (position).getTitle ());
        holder.tv_check_desc.setText (getItem (position).getDefaultDesc (mContext));
        if (position == getCount () - 1) {
            holder.vline.setVisibility (View.GONE);
        } else {
            holder.vline.setVisibility (View.VISIBLE);
        }

        holder.tv_yellow.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mcb != null) {
                    mcb.onEdit (getItem (position));
                }
            }
        });

        holder.tv_red.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mcb != null) {
                    mcb.onDelete (getItem (position));
                }
            }
        });
        BaseFunc.setFont (holder.tv_icon);
    }

    @Override
    public int getSwipeLayoutResourceId (int i) {
        return R.id.mswipe;
    }

    @Override
    public View generateView (final int position, ViewGroup viewGroup) {
        View convertView = View.inflate (mContext, R.layout.item_bankcard_swipe, null);
        new ViewHolder (convertView);

        return convertView;
    }

    @Override
    public BankCard getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    public class ViewHolder {
        public SwipeLayout mswipe;
        public TextView tv_check_desc;
        public TextView tv_icon;
        public TextView tv_cardinfo;
        public TextView tv_yellow;
        public TextView tv_red;
        public View vline;

        public ViewHolder (View view) {
            mswipe = (SwipeLayout) view.findViewById (R.id.mswipe);
            tv_check_desc = (TextView) view.findViewById (R.id.tv_check_desc);
            tv_icon = (TextView) view.findViewById (R.id.tv_icon);
            tv_cardinfo = (TextView) view.findViewById (R.id.tv_cardinfo);
            tv_yellow = (TextView) view.findViewById (R.id.tv_yellow);
            tv_red = (TextView) view.findViewById (R.id.tv_red);
            vline = view.findViewById (R.id.vline);
            view.setTag (this);
        }
    }

    public SwipeBankCardCallBack mcb;

    public void setCallBack (SwipeBankCardCallBack cb) {
        this.mcb = cb;
    }

    public interface SwipeBankCardCallBack {
        void onEdit (BankCard bankCard);

        void onDelete (BankCard bankCard);
    }
}
