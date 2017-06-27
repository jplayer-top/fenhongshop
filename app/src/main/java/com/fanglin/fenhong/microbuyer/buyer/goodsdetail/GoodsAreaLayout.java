package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.Area;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsAreaAdapter;
import com.fanglin.fhui.FHDialog;
import com.google.gson.internal.LinkedTreeMap;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/22.
 */
public class GoodsAreaLayout implements View.OnClickListener {

    Context mContext;
    private FHDialog dlg;
    ListView LV;
    private GoodsAreaAdapter adapter;

    public GoodsAreaLayout(Context c) {
        mContext = c;
        View view = View.inflate(mContext, R.layout.layout_goodsarea, null);
        LV = (ListView) view.findViewById(R.id.LV);

        View comtop = view.findViewById(R.id.comtop);
        ImageView ivBack = (ImageView) comtop.findViewById(R.id.ivBack);
        TextView tvHead = (TextView) comtop.findViewById(R.id.tvHead);


        tvHead.setText(mContext.getString(R.string.deliver_to));
        ivBack.setOnClickListener(this);

        adapter = new GoodsAreaAdapter(mContext);
        LV.setAdapter(adapter);
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setClick(position);
                adapter.notifyDataSetChanged();
                if (dlg != null) {
                    dlg.dismiss();
                }
                if (mcb != null) {
                    mcb.onSelected(adapter.getSelectedItem());
                }
            }
        });

        dlg = new FHDialog(mContext);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setBotView(view, 1);
    }

    public void RefreshList(LinkedTreeMap json) {
        adapter.setJson(json);
        adapter.notifyDataSetChanged();
    }

    public List<Area> getAreas() {
        return adapter.getList();
    }

    public Area getAreaByName(String areaName) {
        if (TextUtils.isEmpty(areaName)) return null;
        List<Area> alist = getAreas();
        if (alist != null && alist.size() > 0) {
            int index = -1;
            for (int i = 0; i < alist.size(); i++) {
                if (areaName.contains(alist.get(i).area_name) || alist.get(i).area_name.contains(areaName)) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                return alist.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void show() {
        if (adapter.getCount() == 0) return;
        dlg.show();
    }

    @Override
    public void onClick(View v) {
        if (dlg != null) {
            dlg.dismiss();
        }
    }

    private AreaLayoutCallBack mcb;

    public void setCallBack(AreaLayoutCallBack cb) {
        this.mcb = cb;
    }

    public interface AreaLayoutCallBack {
        void onSelected(Area a);
    }
}
