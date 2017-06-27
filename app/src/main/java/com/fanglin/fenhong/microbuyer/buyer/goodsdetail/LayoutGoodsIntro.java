package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.GoodsIntro;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsIntroAdapter;
import com.fanglin.fhui.FHDialog;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/29-下午3:16.
 * 功能描述: 商品说明
 */
public class LayoutGoodsIntro implements View.OnClickListener {

    private FHDialog fhDialog;
    GoodsIntroAdapter adapter;

    public LayoutGoodsIntro(Context context) {
        View view = View.inflate(context, R.layout.layout_goods_intro, null);
        view.findViewById(R.id.tvClose).setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.listView);

        fhDialog = new FHDialog(context);
        fhDialog.setBotView(view, 0);

        adapter = new GoodsIntroAdapter(context);
        listView.setAdapter(adapter);
    }

    public void setList(List<GoodsIntro> list) {
        adapter.setList(list);
    }

    public void show() {
        fhDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                fhDialog.dismiss();
                break;
        }
    }
}
