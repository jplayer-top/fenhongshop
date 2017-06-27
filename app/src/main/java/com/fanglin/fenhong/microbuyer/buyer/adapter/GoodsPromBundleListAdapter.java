package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.GoodsBundling;
import com.fanglin.fenhong.microbuyer.buyer.GoodsPromBundleSingleActivity;
import com.google.gson.Gson;
import java.util.List;

/**
 * 促销商品 优惠套装列表 item
 * Created by lizhixin on 2015/12/31.
 */
public class GoodsPromBundleListAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsBundling> list;//优惠套装
    private int goods_source;//0 国内, 1 国外

    public GoodsPromBundleListAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<GoodsBundling> list, int goods_source) {
        this.list = list;
        this.goods_source = goods_source;
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public GoodsBundling getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final BundleViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_goods_prom_bundle_list, null);

            new BundleViewHolder(convertView);
        }
        viewHolder = (BundleViewHolder) convertView.getTag();

        final GoodsBundling goodsBundling = list.get(position);
        if (goodsBundling != null) {

            goodsBundling.position = position + 1;//标明是套装几,也方便传值到下个套装详情页面

            BaseFunc.setFont(viewHolder.tvArrow);
            //标题
            viewHolder.tvTitle.setText(
                    BaseFunc.fromHtml(
                            String.format(mContext.getString(R.string.goods_detail_prom_bundle_list_title),
                                    goodsBundling.position,
                                    BaseFunc.truncDouble(goodsBundling.current_price, 1),
                                    BaseFunc.truncDouble(goodsBundling.cost_price - goodsBundling.current_price, 1))));

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder.recView.setLayoutManager(layoutManager);

            /**
             * 商品横向列表
             */
            GoodsPromBundleListItemAdapter adapter = new GoodsPromBundleListItemAdapter(mContext);
            adapter.setList(goodsBundling.goods_list);
            viewHolder.recView.setAdapter(adapter);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodsPromBundleSingleActivity.class);
                    intent.putExtra("VAL", new Gson().toJson(goodsBundling));
                    intent.putExtra("goods_source", goods_source);
                    mContext.startActivity(intent);
                }
            };
            convertView.setOnClickListener(listener);
            adapter.setOnItemClickListener(new GoodsPromBundleListItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(mContext, GoodsPromBundleSingleActivity.class);
                    intent.putExtra("VAL", new Gson().toJson(goodsBundling));
                    intent.putExtra("goods_source", goods_source);
                    mContext.startActivity(intent);
                }
            });

        }

        return convertView;
    }

    class BundleViewHolder {
        private TextView tvTitle;
        private TextView tvArrow;
        private RecyclerView recView;

        public BundleViewHolder(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvArrow = (TextView) convertView.findViewById(R.id.tv_arrow);
            recView = (RecyclerView) convertView.findViewById(R.id.recyclerview_prom);

            convertView.setTag(this);
        }
    }
}
