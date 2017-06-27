package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.GoodsSpec;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;
import com.fanglin.fhui.flowlayout.TagFlowLayout;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

/**
 * 作者： Created by Plucky on 2015/9/14.
 */
public class GoodsSpecAdapter extends RecyclerView.Adapter<GoodsSpecAdapter.ViewHolder> {

    private List<GoodsSpec> list;
    private int[] selectedIds;
    private Context mContext;

    public GoodsSpecAdapter(Context c) {
        this.mContext = c;
    }

    public List<GoodsSpec> getList() {
        return list;
    }

    public void setJson(LinkedTreeMap json, LinkedTreeMap goods_spec) {
        if (json != null && json.size() > 0) {
            list = new ArrayList<>();
            Object[] keys = json.keySet().toArray();
            Object[] values = json.values().toArray();
            selectedIds = new int[keys.length];
            for (int i = 0; i < keys.length; i++) {
                GoodsSpec gs = new GoodsSpec();
                selectedIds[i] = -1;
                gs.key = String.valueOf(keys[i]);
                gs.value = new Gson().toJson(values[i]);
                gs.subItems = parseTags(gs.value);
                list.add(gs);

                if (gs.subItems != null && gs.subItems.size() > 0 && goods_spec != null) {
                    for (int j = 0; j < gs.subItems.size(); j++) {
                        String aid = gs.subItems.get(j).key;
                        if (goods_spec.containsKey(aid)) {
                            selectedIds[i] = j;
                            break;
                        }
                    }
                }
            }
        } else {
            list = null;
        }
    }

    public GoodsSpec getItem(int position) {
        return list.get(position);
    }


    private boolean nochk() {
        for (int i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] > -1) return false;
        }
        return true;
    }

    public boolean checkTips(Boolean show) {
        if (getItemCount() == 0) return false;
        if (nochk()) {
            if (show)
                BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_of_select_a_spec));
            return false;
        }
        int index = -1;
        a:
        for (int i = 0; i < getItemCount(); i++) {
            if (selectedIds[i] == -1) {
                index = i;
                break a;//跳出循环体
            }
        }
        if (index > -1) {
            if (show)
                BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_select) + getItem(index).key);
            return false;
        } else {
            return true;
        }
    }

    public String getTitles() {
        StringBuilder sb = new StringBuilder();
        sb.append("选择 ");
        for (int i = 0; i < getItemCount(); i++) {
            sb.append(getItem(i).key);
            sb.append(" ");
        }
        return sb.toString();
    }


    /**
     * 获取规格id串 123|321
     *  "3495": "闪耀深金粉色"
     * */
    private String getSelectedTags(boolean iskey) {
        if (!checkTips(false)) return null;
        /**缺陷*/
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            SubItem si = getItem(i).subItems.get(selectedIds[i]);
            if (sb.length() == 0) {
                if (iskey) {
                    sb.append(si.key);
                } else {
                    sb.append(si.value);
                }
            } else {
                if (iskey) {
                    sb.append("|");
                    sb.append(si.key);
                } else {
                    sb.append(" ");
                    sb.append(si.value);
                }
            }
        }
        return sb.toString();
    }

    private List<SubItem> parseTags(String json) {
        List<SubItem> subItems = new ArrayList<>();//子集
        try {
            JSONObject jObj = new JSONObject(json);
            for (int i = 0; i < jObj.length(); i++) {
                try {
                    SubItem si = new SubItem();
                    String key = jObj.names().getString(i);
                    String val = jObj.getString(key);
                    si.key = key;
                    si.value = val;
                    subItems.add(si);
                } catch (Exception e) {
                    //
                }
            }
        } catch (Exception e) {
            //
        }
        return subItems;
    }

    /** 规格与goods_id 映射表*/
    private LinkedTreeMap spec_relation;

    public void setSpecRelation(LinkedTreeMap spec_relation) {
        this.spec_relation = spec_relation;
    }

    public String getGoodsId() {
        String selTag = getSelectedTags(true);//選中的標籤
        String sort_tag;
        try {
            if (selTag != null) {
                String[] arr_string = selTag.split("\\|");
                int[] arr_int = BaseFunc.StrArr2Int(arr_string);
                Arrays.sort(arr_int);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arr_int.length; i++) {
                    if (i == 0) {
                        sb.append(arr_int[i]);
                    } else {
                        sb.append("|");
                        sb.append(arr_int[i]);
                    }
                }
                sort_tag = sb.toString();
            } else {
                sort_tag = null;
            }

        } catch (Exception e) {
            sort_tag = selTag;
        }


        return String.valueOf(spec_relation.get(sort_tag));
    }


    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_title.setText(getItem(position).key);
        final List<String> tags = getItem(position).getTags();
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        holder.id_flowlayout.setAdapter(new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int index, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, holder.id_flowlayout, false);
                tv.setText(s);
                return tv;
            }
        }, selectedIds[position]);

        holder.id_flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int index, FlowLayout parent) {
                boolean flag = ((TextView) view).getCurrentTextColor() == mContext.getResources().getColor(R.color.white);
                selectedIds[position] = flag ? index : -1;
                if (checkTips(false)) {
                    if (mcb != null) {
                        mcb.onSelected(getGoodsId(), getSelectedTags(false));
                    }
                }

                return false;
            }
        });
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(mContext, R.layout.item_goods_spec, null);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TagFlowLayout id_flowlayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            id_flowlayout = (TagFlowLayout) itemView.findViewById(R.id.id_flowlayout);
        }
    }

    public class SubItem {
        public String key;
        public String value;
    }

    private onSpecCallBack mcb;

    public void setCallBack(onSpecCallBack cb) {
        this.mcb = cb;
    }

    public interface onSpecCallBack {
        void onSelected(String goods_id, String tags);
    }
}
