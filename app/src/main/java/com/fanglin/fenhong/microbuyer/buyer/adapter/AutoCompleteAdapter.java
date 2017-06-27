package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.FHAutoComplete;
import com.fanglin.fenhong.microbuyer.buyer.SearchActivity;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/13-上午11:52.
 * 功能描述: 搜索自动联想适配器
 */
public class AutoCompleteAdapter extends BaseAdapter {

    private SearchActivity searchActivity;
    private List<FHAutoComplete> list;

    public AutoCompleteAdapter(SearchActivity mContext) {
        this.searchActivity = mContext;
    }

    public void setList(List<FHAutoComplete> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public FHAutoComplete getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(searchActivity, R.layout.item_auto_complete, null);
            new AutoViewHolder(convertView);
        }

        AutoViewHolder holder = (AutoViewHolder) convertView.getTag();
        final FHAutoComplete item = getItem(position);
        holder.tvKey.setText(item.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseFunc.isValidUrl(item.getUrl())) {
                    BaseFunc.urlClick(searchActivity, item.getUrl());
                } else {
                    /**进入搜索列表页*/
                    BaseFunc.gotoGoodsListActivity(searchActivity,item.getName(),0,null);
                }
                BaseFunc.add_search(searchActivity, item.getName(), searchActivity.member);
            }
        });

        return convertView;
    }

    public class AutoViewHolder {
        TextView tvKey;

        public AutoViewHolder(View view) {
            tvKey = (TextView) view.findViewById(R.id.tvKey);
            view.setTag(this);
        }
    }
}
