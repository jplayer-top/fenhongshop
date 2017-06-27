package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Share;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/29.
 */
public class ShareAdapter extends BaseAdapter {

    private Context mContext;
    private List<Share> list;

    public ShareAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<Share> list) {
        this.list = list;
    }

    public void addList (List<Share> list) {
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
    public Share getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate (mContext, R.layout.item_share, null);
            new ViewHolder (convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag ();

        new FHImageViewUtil (holder.fiv).setImageURI (getItem (position).trace_imgurl, FHImageViewUtil.SHOWTYPE.DEFAULT);

        holder.fiv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.tv_title.setText (getItem (position).trace_title);
        BaseFunc.setFont (holder.LIcon);
        boolean[] flag = getItem (position).getIconStatus ();
        holder.tv_moments.setSelected (flag[0]);
        holder.tv_wechat.setSelected (flag[1]);
        holder.tv_qzone.setSelected (flag[2]);
        holder.tv_qq.setSelected (flag[3]);
        holder.tv_weibo.setSelected (flag[4]);
        String fmt = mContext.getString (R.string.vistor_num);
        holder.tv_visit_num.setText (String.format (fmt, getItem (position).vistor_num));
        holder.tv_time.setText (getItem (position).trace_addtime);
        if (position == getCount () - 1) {
            holder.vline.setVisibility (View.GONE);
        } else {
            holder.vline.setVisibility (View.VISIBLE);
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView fiv;
        public TextView tv_title;
        public LinearLayout LIcon;
        public TextView tv_moments, tv_wechat, tv_qzone, tv_qq, tv_weibo;
        public TextView tv_visit_num, tv_time;
        public View vline;

        public ViewHolder (View v) {
            fiv = (ImageView) v.findViewById (R.id.fiv);
            tv_title = (TextView) v.findViewById (R.id.tv_title);

            LIcon = (LinearLayout) v.findViewById (R.id.LIcon);
            tv_moments = (TextView) v.findViewById (R.id.tv_moments);
            tv_wechat = (TextView) v.findViewById (R.id.tv_wechat);
            tv_qzone = (TextView) v.findViewById (R.id.tv_qzone);
            tv_qq = (TextView) v.findViewById (R.id.tv_qq);
            tv_weibo = (TextView) v.findViewById (R.id.tv_weibo);

            tv_visit_num = (TextView) v.findViewById (R.id.tv_visit_num);
            tv_time = (TextView) v.findViewById (R.id.tv_time);
            vline = v.findViewById (R.id.vline);
            v.setTag (this);
        }
    }
}
