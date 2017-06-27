package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.BaseGoods;
import com.fanglin.fenhong.microbuyer.base.model.ReturnGoodsList;
import com.fanglin.fenhong.microbuyer.common.OrderDtlActivity;
import com.fanglin.fenhong.microbuyer.common.ReturnGoodsProcessActivity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 退货列表外层Item 店铺信息
 * Created by lizhixin on 2015/11/16.
 * Fixed By Plucky
 */
public class ReturnGoodsListAdapter extends BaseAdapter {

    private List<ReturnGoodsList> list;
    private Context context;
    private DecimalFormat df;

    public ReturnGoodsListAdapter (Context context) {
        this.context = context;
        df = new DecimalFormat ("¥#0.00");
    }

    public void setList (List<ReturnGoodsList> list) {
        this.list = list;
    }

    public void addList (List<ReturnGoodsList> list) {
        if (list != null && list.size () > 0) {
            this.list.addAll (list);
        }
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public ReturnGoodsList getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (context, R.layout.item_return_goods_list, null);
            new ViewHolder (convertView);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag ();

        BaseFunc.setFont (viewHolder.tvIconShop);
        viewHolder.tvShopName.setText (list.get (position).store_name);
        final String[] res = ReturnGoodsList.getProgressDescAndColor (getItem (position).progress, getItem (position).refund_type);
        viewHolder.tv_shop_status.setText (res[0]);
        viewHolder.tv_shop_status.setTextColor (Color.parseColor (res[1]));

        /*如果是全部退款情况*/
        if (TextUtils.equals (getItem (position).is_all, "1")) {
            viewHolder.tv_all_desc.setVisibility (View.VISIBLE);
            viewHolder.LGoods.setVisibility (View.GONE);
            CharSequence desc = Html.fromHtml (getItem (position).goods_name);
            viewHolder.tv_all_desc.setText (desc);
            viewHolder.tv_all_desc.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent i = new Intent (context, OrderDtlActivity.class);
                    i.putExtra ("VAL", getItem (position).order_id);
                    i.putExtra ("STATUES_CODE", getItem (position).progress);
                    i.putExtra ("STATUES_DESC", res[0]);
                    i.putExtra ("STATUES_COLOR", res[1]);
                    context.startActivity (i);
                }
            });
            viewHolder.LGoods.setOnClickListener (null);
        } else {
            viewHolder.tv_all_desc.setVisibility (View.GONE);
            viewHolder.LGoods.setVisibility (View.VISIBLE);
            viewHolder.tv_all_desc.setOnClickListener (null);
            new FHImageViewUtil (viewHolder.iv_goods).setImageURI (getItem (position).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
            viewHolder.tv_title.setText (getItem (position).goods_name);
            viewHolder.tv_subtitle.setText (BaseGoods.parseSpec (getItem (position).goods_spec));
            viewHolder.tv_price.setText (df.format (getItem (position).refund_amount));
            String strtmp;
            if (getItem (position).goods_num > 0) {
                strtmp = "X" + getItem (position).goods_num;
            } else {
                strtmp = "";
            }
            viewHolder.tv_memo.setText (strtmp);

            viewHolder.LGoods.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent i = new Intent (context, OrderDtlActivity.class);
                    i.putExtra ("VAL", getItem (position).order_id);
                    i.putExtra ("STATUES_CODE", getItem (position).progress);
                    i.putExtra ("STATUES_DESC", res[0]);
                    i.putExtra ("STATUES_COLOR", res[1]);
                    context.startActivity (i);
                }
            });
        }

        viewHolder.tvSeeProcess.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (context, ReturnGoodsProcessActivity.class);
                intent.putExtra ("ID", getItem (position).refund_id);
                intent.putExtra ("STATE", getItem (position).progress);
                intent.putExtra ("TYPE", getItem (position).refund_type);
                intent.putExtra ("REC", getItem (position).order_goods_id);
                intent.putExtra ("ORDER", getItem (position).order_id);
                context.startActivity (intent);
            }
        });

        viewHolder.tv_refund_sn.setText (getItem (position).refund_sn);
        viewHolder.tvRefundMoney.setText (df.format (list.get (position).refund_amount));

        return convertView;
    }

    private class ViewHolder {
        public TextView tvIconShop;
        public TextView tvShopName;
        public TextView tv_shop_status;

        public TextView tv_all_desc;
        public LinearLayout LGoods;

        public ImageView iv_goods;
        public TextView tv_title;
        public TextView tv_subtitle;
        public TextView tv_price;
        public TextView tv_memo;

        public TextView tv_refund_sn;
        public TextView tvRefundMoney;
        public TextView tvSeeProcess;

        public ViewHolder (View view) {
            tvIconShop = (TextView) view.findViewById (R.id.tv_icon_shop);
            tvShopName = (TextView) view.findViewById (R.id.tv_shop_name);
            tv_shop_status = (TextView) view.findViewById (R.id.tv_shop_status);

            tv_all_desc = (TextView) view.findViewById (R.id.tv_all_desc);
            LGoods = (LinearLayout) view.findViewById (R.id.LGoods);

            iv_goods = (ImageView) view.findViewById (R.id.iv_goods);
            tv_title = (TextView) view.findViewById (R.id.tv_title);
            tv_subtitle = (TextView) view.findViewById (R.id.tv_subtitle);
            tv_price = (TextView) view.findViewById (R.id.tv_price);
            tv_memo = (TextView) view.findViewById (R.id.tv_memo);

            tv_refund_sn = (TextView) view.findViewById (R.id.tv_refund_sn);
            tvRefundMoney = (TextView) view.findViewById (R.id.tv_refund_money);
            tvSeeProcess = (TextView) view.findViewById (R.id.tv_see_process);
            view.setTag (this);
        }


    }
}
