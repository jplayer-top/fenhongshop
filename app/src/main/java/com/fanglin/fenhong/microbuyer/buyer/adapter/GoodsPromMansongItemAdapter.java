package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlPromMansongRules;

import java.util.List;

/**
 * 促销商品 满额优惠 弹框 数据适配
 * Created by lizhixin on 2016/1/6.
 */
public class GoodsPromMansongItemAdapter extends BaseAdapter {
    
    private Context mContext;
    private List list;

    public GoodsPromMansongItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 4;//三种视图 + 一种空白视图
    }

    @Override
    public int getItemViewType(int position) {
        Object object = getItem(position);

        if (object != null && object instanceof GoodsDtlPromMansongRules) {

            GoodsDtlPromMansongRules rule = (GoodsDtlPromMansongRules) object;

            if (!TextUtils.isEmpty(rule.minus_amount) && rule.gift != null) {
                //既有满减又有满赠
                return 3;
            } else if (!TextUtils.isEmpty(rule.minus_amount)) {
                //只有满减
                return 2;
            } else if (rule.gift != null) {
                //只有满赠
                return 1;
            } else {
                //提示可以用空白视图
                return 0;
            }

        } else {
            return 0;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Object object = getItem(position);
        if (object != null && object instanceof GoodsDtlPromMansongRules) {

            final GoodsDtlPromMansongRules rule = (GoodsDtlPromMansongRules) object;

            switch (getItemViewType(position)) {
                case 3: //既有满减又有满赠
                    convertView = View.inflate(mContext, R.layout.item_goods_prom_mansong3, null);
                    TextView tvTitle3 = (TextView) convertView.findViewById(R.id.tv_title);
                    TextView tvGoodsName3 = (TextView) convertView.findViewById(R.id.tv_goods_name);
                    TextView tvArrow3 = (TextView) convertView.findViewById(R.id.tv_arrow);
                    ImageView imageView3 = (ImageView) convertView.findViewById(R.id.image);

                    tvTitle3.setText(
                            BaseFunc.fromHtml(
                                    String.format(
                                            mContext.getString(R.string.goods_detail_prom_mansong_item_title3),
                                            position + 1,
                                            BaseFunc.truncDouble(Double.parseDouble(rule.egt_amount), 1),
                                            BaseFunc.truncDouble(Double.parseDouble(rule.minus_amount), 1))));
                    tvGoodsName3.setText(rule.gift.goods_name);
                    new FHImageViewUtil(imageView3).setImageURI(rule.gift.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    BaseFunc.setFont(tvArrow3);
                    break;

                case 2://只有满减
                    convertView = View.inflate(mContext, R.layout.item_goods_prom_mansong2, null);
                    TextView tvTitle2 = (TextView) convertView.findViewById(R.id.tv_title);
                    tvTitle2.setText(
                            BaseFunc.fromHtml(
                                    String.format(
                                        mContext.getString(R.string.goods_detail_prom_mansong_item_title2),
                                        position + 1,
                                        BaseFunc.truncDouble(Double.parseDouble(rule.egt_amount), 1),
                                        BaseFunc.truncDouble(Double.parseDouble(rule.minus_amount), 1))));
                    break;

                case 1://只有满赠
                    convertView = View.inflate(mContext, R.layout.item_goods_prom_mansong1, null);
                    TextView tvTitle1 = (TextView) convertView.findViewById(R.id.tv_title);
                    TextView tvGoodsName1 = (TextView) convertView.findViewById(R.id.tv_goods_name);
                    TextView tvArrow1 = (TextView) convertView.findViewById(R.id.tv_arrow);
                    ImageView imageView1 = (ImageView) convertView.findViewById(R.id.image);

                    tvTitle1.setText(
                            BaseFunc.fromHtml(
                                    String.format(
                                            mContext.getString(R.string.goods_detail_prom_mansong_item_title1),
                                            position + 1,
                                            BaseFunc.truncDouble(Double.parseDouble(rule.egt_amount), 1))));
                    tvGoodsName1.setText(rule.gift.goods_name);
                    new FHImageViewUtil(imageView1).setImageURI(rule.gift.goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
                    BaseFunc.setFont(tvArrow1);
                    break;

                case 0://提示可以用空白视图
                    convertView = null;
                    break;
                default:
                    break;
            }

            if (convertView != null && rule.gift != null) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoGoodsDetail(mContext,rule.gift.goods_id,null,null);
                    }
                });
            }

        } else {
            convertView = null;
        }

        return convertView;
    }

}
