package com.fanglin.fenhong.microbuyer.buyer.adapter;

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
import com.fanglin.fenhong.microbuyer.base.model.AppMsgList;
import com.fanglin.fhlib.other.FHLib;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/27.
 */
public class AppMsgAdapter extends BaseAdapter {
    private Context mContext;
    private List<AppMsgList> list;
    LinearLayout.LayoutParams params;

    public AppMsgAdapter(Context c) {
        this.mContext = c;
        int w = BaseFunc.getDisplayMetrics(mContext).widthPixels;
        int h = w * 375 / 1080;
        params = new LinearLayout.LayoutParams(w, h);
    }

    public void setList(List<AppMsgList> list) {
        this.list = list;
    }

    public void addList(List<AppMsgList> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public AppMsgList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_appmsg, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        //适配尺寸
        holder.fiv.setLayoutParams(params);

        new FHImageViewUtil(holder.fiv).setImageURI(getItem(position).images_url, FHImageViewUtil.SHOWTYPE.GROUP);
        if (BaseFunc.isValidImgUrl(getItem(position).images_url)) {
            holder.fiv.setVisibility(View.VISIBLE);
        } else {
            holder.fiv.setVisibility(View.GONE);
        }


        String title = BaseFunc.formatHtml(getItem(position).message_title);
        holder.tv_title.setText(title);
        String content = BaseFunc.formatHtml(getItem(position).message);
        holder.tv_content.setText(content);
        holder.tv_time.setText(FHLib.getTimeStrByTimestamp(getItem(position).send_time));

        BaseFunc.setFont(holder.LIcon);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.urlClick(mContext, getItem(position).url);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        public ImageView fiv;
        public TextView tv_title;
        public TextView tv_content;
        public TextView tv_time;
        public LinearLayout LIcon;

        public ViewHolder(View v) {
            fiv = (ImageView) v.findViewById(R.id.fiv);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            LIcon = (LinearLayout) v.findViewById(R.id.LIcon);
            v.setTag(this);
        }
    }
}
