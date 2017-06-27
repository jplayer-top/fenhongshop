package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.ActivityList;
import com.fanglin.fenhong.microbuyer.common.ActListDtlActivity;

import java.util.List;

/**
 * 国家馆和团购列表
 * 作者： Created by Plucky on 2015/10/8.
 */
public class ActListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ActivityList> list;
    private int screenWidth;

    public ActListAdapter(Context c) {
        this.mContext = c;
        screenWidth = BaseFunc.getDisplayMetrics(mContext).widthPixels;
    }

    public void setList(List<ActivityList> lst) {
        this.list = lst;
    }

    public void addList(List<ActivityList> lst) {
        if (lst != null && lst.size() > 0) {
            this.list.addAll(lst);
        }
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public ActivityList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_grpbuy, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        int w = screenWidth;
        int h = w * 450 / 1080;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        holder.sdv.setLayoutParams(params);
        new FHImageViewUtil(holder.sdv).setImageURI(getItem(position).activity_banner, FHImageViewUtil.SHOWTYPE.GROUP);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aurl = list.get(position).activity_url;
                if (BaseFunc.isValidUrl(aurl)) {
                    BaseFunc.urlClick(mContext,aurl);
                } else {
                    BaseFunc.gotoActivity(mContext, ActListDtlActivity.class, list.get(position).activity_id);
                }
            }
        });

        return convertView;
    }


    public class ViewHolder {
        public ImageView sdv;

        public ViewHolder(View view) {
            sdv = (ImageView) view.findViewById(R.id.sdv);
            view.setTag(this);
        }
    }
}
