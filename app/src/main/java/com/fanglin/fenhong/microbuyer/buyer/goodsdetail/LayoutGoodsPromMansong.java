package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsPromMansongItemAdapter;
import com.fanglin.fhui.FHDialog;

import java.util.List;

/**
 * 商品促销 满额优惠 列表弹框
 * Created by lizhixin on 2016/1/6.
 */
public class LayoutGoodsPromMansong implements View.OnClickListener {

    private Context mContext;
    private FHDialog dlg;
    private ListView listView;
    private GoodsPromMansongItemAdapter adapter;
    private List list;

    public LayoutGoodsPromMansong(Context context) {
        this.mContext = context;

        View view = View.inflate(mContext, R.layout.layout_goods_prom_mansong, null);

        int height = BaseFunc.getDisplayMetrics(mContext).heightPixels * 2 / 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams (params);

        listView = (ListView) view.findViewById(R.id.listView);
        TextView tvClose = (TextView) view.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(this);

        adapter = new GoodsPromMansongItemAdapter(mContext);
        listView.setAdapter(adapter);

        dlg = new FHDialog(mContext);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setBotView(view, 0);
    }

    public void showView() {
        if (list != null && list.size() > 0) {
            dlg.show();
        }
    }

    public void setRules(List list) {
        this.list = list;
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                dlg.dismiss();
                break;
            default:
                break;
        }
    }

}
