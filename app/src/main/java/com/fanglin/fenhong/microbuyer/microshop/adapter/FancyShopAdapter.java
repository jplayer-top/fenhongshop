package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.FancyMShop;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.microshop.FancyShopActivity;
import com.fanglin.fhui.CircleImageView;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/13.
 */
public class FancyShopAdapter extends BaseAdapter {
    private Context mContext;
    private List<FancyMShop> list;

    public FancyShopAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<FancyMShop> list) {
        this.list = list;
    }

    public void addList (List<FancyMShop> list) {
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
    public FancyMShop getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_mshop, null);
            new ViewHolder (convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag ();
        new FHImageViewUtil (holder.fiv_pic).setImageURI (getItem (position).shop_logo, FHImageViewUtil.SHOWTYPE.AVATAR);

        BaseFunc.setFont (holder.tv_vip);
        BaseFunc.setFont (holder.LMicroshop);
        BaseFunc.setFont (holder.Lshare);

        holder.tv_title.setText (getItem (position).shop_name);
        holder.tv_memo.setText (getItem (position).shop_scope);
        holder.Lshare.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                ShareData shareData = new ShareData ();
                shareData.content = mContext.getString (R.string.share_content_microshop);
                shareData.url = String.format (BaseVar.SHARE_FANCYSHOP, getItem (position).shop_id);
                shareData.title = getItem (position).shop_name;
                shareData.imgs = getItem (position).shop_logo;
                //ShareData.share (v, shareData);
                ShareData.fhShare ((BaseFragmentActivity) mContext, shareData, null);
            }
        });

        LinearLayoutManager lm = new LinearLayoutManager (mContext);
        lm.setOrientation (LinearLayoutManager.HORIZONTAL);
        holder.goods.setLayoutManager (lm);
        FancyShopGoodsAdapter adapter = new FancyShopGoodsAdapter (mContext);
        adapter.setList (getItem (position).goods);
        holder.goods.setAdapter (adapter);
        View.OnClickListener l = new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                BaseFunc.gotoActivity (mContext, FancyShopActivity.class, getItem (position).shop_id);
            }
        };
        convertView.setOnClickListener (l);
        return convertView;
    }

    public class ViewHolder {
        public CircleImageView fiv_pic;
        public LinearLayout LTitle;
        public TextView tv_title;
        public TextView tv_vip;
        public TextView tv_memo;
        public RecyclerView goods;
        public LinearLayout LMicroshop;
        public LinearLayout Lshare;


        public ViewHolder (View view) {
            fiv_pic = (CircleImageView) view.findViewById (R.id.fiv_pic);
            LTitle = (LinearLayout) view.findViewById (R.id.LTitle);
            tv_title = (TextView) view.findViewById (R.id.tv_title);
            tv_vip = (TextView) view.findViewById (R.id.tv_vip);
            tv_memo = (TextView) view.findViewById (R.id.tv_memo);
            goods = (RecyclerView) view.findViewById (R.id.goods);
            LMicroshop = (LinearLayout) view.findViewById (R.id.LMicroshop);
            Lshare = (LinearLayout) view.findViewById (R.id.Lshare);
            view.setTag (this);
        }
    }
}
