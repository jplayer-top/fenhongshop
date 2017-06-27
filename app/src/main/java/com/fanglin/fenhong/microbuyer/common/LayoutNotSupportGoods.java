package com.fanglin.fenhong.microbuyer.common;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.BaseGoods;
import com.fanglin.fenhong.microbuyer.common.adapter.NotSupportGoodsAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/15-下午4:01.
 * 功能描述: 不支持配送的商品
 */
public class LayoutNotSupportGoods implements View.OnClickListener {

    private Dialog dialog;
    private NotSupportGoodsAdapter adapter;

    public LayoutNotSupportGoods(Context context) {
        View view = View.inflate(context, R.layout.layout_notsupport_goods, null);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        view.findViewById(R.id.tvModify).setOnClickListener(this);
        view.findViewById(R.id.tvCart).setOnClickListener(this);

        dialog = new Dialog(context, R.style.InnerDialog);
        dialog.setContentView(view);

        adapter = new NotSupportGoodsAdapter(context);
        listView.setAdapter(adapter);
    }

    public void setList(List<BaseGoods> list) {
        adapter.setList(list);
    }

    public int getGoodsNum() {
        return adapter.getCount();
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvModify:
                dialog.dismiss();
                if (listener != null) {
                    listener.onModify();
                }
                break;
            case R.id.tvCart:
                dialog.dismiss();
                if (listener != null) {
                    listener.onCart();
                }
                break;
        }
    }

    public interface NotSupportGoodsListener {
        void onModify();

        void onCart();
    }

    private NotSupportGoodsListener listener;

    public void setListener(NotSupportGoodsListener listener) {
        this.listener = listener;
    }
}
