package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.fanglin.fenhong.microbuyer.base.model.MicroshopList;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.microshop.FancyShopActivity;
import com.fanglin.fhui.CircleImageView;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/5.
 */
public class MicroshopAdapter extends BaseAdapter {

    private List<MicroshopList> list;
    private Context mContext;

    public MicroshopAdapter (Context c) {
        mContext = c;
    }

    public void setList (List<MicroshopList> list) {
        this.list = list;
    }

    public void addList (List<MicroshopList> list) {
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
    public MicroshopList getItem (int position) {
        MicroshopList res = list.get (position);
        /*如果是微店主，但是没有管理过自己的微店 则显示示例微店*/
        if ((res.goods == null || res.goods.size () == 0) && TextUtils.isEmpty (res.fork_id)) {
            res = MicroshopList.getTestData ();
        }
        return res;
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_microshop, null);
            new ViewHolder (convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag ();

        new FHImageViewUtil (holder.fiv_pic).setImageURI (getItem (position).shop_banner, FHImageViewUtil.SHOWTYPE.AVATAR);

        BaseFunc.setFont (holder.LTitle);

        holder.tv_title.setText (getItem (position).getShop_name ());
        holder.tv_memo.setText (getItem (position).getShop_scope ());

        if (getItem (position).isFancy ()) {
            holder.tv_vip.setVisibility (View.VISIBLE);
        } else {
            holder.tv_vip.setVisibility (View.INVISIBLE);
        }

        holder.LMicroshop.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                BaseFunc.gotoActivity (mContext, FancyShopActivity.class, getItem (position).shop_id);
            }
        });
        holder.Lshare.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                ShareData shareData = new ShareData ();
                shareData.content = mContext.getString (R.string.share_content_microshop);
                shareData.url = String.format (BaseVar.SHARE_FANCYSHOP, getItem (position).shop_id);
                shareData.title = getItem (position).shop_name;
                shareData.imgs = getItem (position).shop_banner;
                //ShareData.share (v, shareData);
                ShareData.fhShare ((BaseFragmentActivity) mContext, shareData, null);
            }
        });
        BaseFunc.setFont (holder.LMicroshop);
        BaseFunc.setFont (holder.Lshare);

        LinearLayoutManager lm = new LinearLayoutManager (mContext);
        lm.setOrientation (LinearLayoutManager.HORIZONTAL);
        holder.goods_flow.setLayoutManager (lm);
        FancyShopGoodsAdapter adapter = new FancyShopGoodsAdapter (mContext);
        adapter.setList (getItem (position).goods);
        holder.goods_flow.setAdapter (adapter);
        View.OnClickListener l = new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                BaseFunc.gotoActivity (mContext, FancyShopActivity.class, getItem (position).shop_id);
            }
        };
        convertView.setOnClickListener (l);
        return convertView;
    }

    private class ViewHolder {
        public CircleImageView fiv_pic;
        public LinearLayout LTitle;
        public TextView tv_title, tv_vip;
        public TextView tv_memo;
        public RecyclerView goods_flow;
        public LinearLayout LMicroshop;
        public LinearLayout Lshare;

        public ViewHolder (View v) {
            fiv_pic = (CircleImageView) v.findViewById (R.id.fiv_pic);
            LTitle = (LinearLayout) v.findViewById (R.id.LTitle);
            tv_title = (TextView) v.findViewById (R.id.tv_title);
            tv_vip = (TextView) v.findViewById (R.id.tv_vip);
            goods_flow = (RecyclerView) v.findViewById (R.id.goods_flow);
            tv_memo = (TextView) v.findViewById (R.id.tv_memo);
            LMicroshop = (LinearLayout) v.findViewById (R.id.LMicroshop);
            Lshare = (LinearLayout) v.findViewById (R.id.Lshare);

            v.setTag (this);
        }
    }
}
