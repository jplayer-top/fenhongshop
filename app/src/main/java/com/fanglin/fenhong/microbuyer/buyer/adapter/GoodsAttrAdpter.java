package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.TextShowDialog;
import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;

/**
 * 单品参数
 * Created by Plucky on 2015/9/12.
 */
public class GoodsAttrAdpter extends BaseAdapter {
    private LinkedTreeMap json;
    private Object[] keys = new Object[]{}, values = new Object[]{};
    private Context mContext;
    TextShowDialog textShowDialog;

    public GoodsAttrAdpter(Context c) {
        this.mContext = c;
        textShowDialog = new TextShowDialog(c);
    }

    public void setJson(LinkedTreeMap js) {
        this.json = js;
        if (json != null && json.size() > 0) {
            keys = json.keySet().toArray();
            values = json.values().toArray();
        }
    }

    @Override
    public int getCount() {
        if (json == null) {
            return 0;
        } else {
            return json.size();
        }

    }

    @Override
    public HashMap<String, String> getItem(int position) {
        String key, val;
        HashMap<String, String> key_val = new HashMap<>();
        try {
            key = String.valueOf(keys[position]);
            val = String.valueOf(values[position]);
        } catch (Exception e) {
            key = "";
            val = "";
        }
        key_val.put("key", key);
        key_val.put("value", val);
        return key_val;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_goods_attr, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tv_key.setText(getItem(position).get("key"));
        holder.tv_value.setText(getItem(position).get("value"));
        if (position == getCount() - 1) {
            holder.vLine.setVisibility(View.INVISIBLE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tv_key, tv_value;
        public View vLine;

        public ViewHolder(View v) {
            tv_key = (TextView) v.findViewById(R.id.tv_key);
            tv_value = (TextView) v.findViewById(R.id.tv_value);
            vLine = v.findViewById(R.id.vLine);
            tv_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textShowDialog.setText(tv_value.getText().toString());
                    textShowDialog.show();
                }
            });
            v.setTag(this);
        }
    }
}
