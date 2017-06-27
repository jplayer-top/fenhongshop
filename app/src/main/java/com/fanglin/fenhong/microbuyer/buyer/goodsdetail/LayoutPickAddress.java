package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.Address;
import com.fanglin.fenhong.microbuyer.buyer.adapter.PickAddressAdapter;
import com.fanglin.fhui.FHDialog;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author: Created by Plucky on 16/8/12-下午5:49.
 * 功能描述: 选择配送地址
 */
public class LayoutPickAddress implements View.OnClickListener {
    private FHDialog fhDialog;
    private PickAddressAdapter adapter;
    private String currentAreaInfo;

    public LayoutPickAddress(Context context) {
        View view = View.inflate(context, R.layout.layout_pick_address, null);
        view.findViewById(R.id.btnAdd).setOnClickListener(this);
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        view.findViewById(R.id.tvClose).setOnClickListener(this);

        fhDialog = new FHDialog(context);
        fhDialog.setBotView(view, 0);

        adapter = new PickAddressAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fhDialog.dismiss();
                if (listener != null) {
                    Address address = adapter.getItem(position);
                    currentAreaInfo = address.area_info;
                    adapter.setDefault(position);
                    listener.onPick(address);
                }
            }
        });
    }

    public void setList(List<Address> list) {
        adapter.setList(list);
    }

    public void addList(Address address) {
        adapter.addList(address);
    }

    public void show() {
        adapter.setCurrentSelected(currentAreaInfo);
        fhDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                if (listener != null) {
                    listener.onAdd();
                    fhDialog.dismiss();
                }
                break;
            case R.id.tvClose:
                fhDialog.dismiss();
                break;
        }
    }

    public Address getDefaultAddress() {
        if (adapter.getCount() > 0) {
            return adapter.getDefaultAddress();
        } else {
            return null;
        }
    }

    public void setCurrentAreaInfo(String currentAreaInfo) {
        this.currentAreaInfo = currentAreaInfo;
    }

    private PickAddressListener listener;

    public void setListener(PickAddressListener listener) {
        this.listener = listener;
    }

    public interface PickAddressListener {
        void onAdd();

        void onPick(Address address);
    }
}
