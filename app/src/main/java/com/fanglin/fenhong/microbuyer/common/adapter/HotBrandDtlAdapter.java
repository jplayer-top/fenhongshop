package com.fanglin.fenhong.microbuyer.common.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.UpdateAction;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.HotBrandDtl;
import com.fanglin.fenhong.microbuyer.base.model.HotBrands;
import com.hb.views.PinnedSectionListView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/12.
 * modify by lizhixin on 2015/12/21.
 */
public class HotBrandDtlAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_PINNED = 1;
    public static final int TYPE_ITEM = 2;

    private Context mContext;
    private List<HotBrandDtl> list;
    private HotBrands aBrand;
    DecimalFormat df;
    LinearLayout.LayoutParams params;

    //筛选视图中谁处于选中状态 以及升序还是降序
    private int pinnedIndex, sortIndex;

    public HotBrandDtlAdapter(Context c) {
        this.mContext = c;
        df = new DecimalFormat("¥#0.00");
        int w = BaseFunc.getDisplayMetrics(mContext).widthPixels;
        int h = w * 230 / 350;//适配尺寸
        params = new LinearLayout.LayoutParams(w, h);
    }

    public void setData(HotBrands aBrand) {
        this.aBrand = aBrand;
    }

    public void setList(List<HotBrandDtl> list) {
        this.list = list;
    }

    public void addList(List<HotBrandDtl> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }

    public void refreshIndex(int pinnedIndex, int sortIndex) {
        this.pinnedIndex = pinnedIndex;
        this.sortIndex = sortIndex;
    }

    @Override
    public int getCount() {
        if (list == null) return 2;
        return list.size() + 2;
    }

    @Override
    public HotBrandDtl getItem(int position) {
        return list.get(position - 2);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (convertView == null) {
            if (viewType == TYPE_HEADER) {
                convertView = View.inflate(mContext, R.layout.item_hotbrands_header, null);
                new HeaderViewHolder(convertView);
            } else if (viewType == TYPE_ITEM) {
                convertView = View.inflate(mContext, R.layout.item_hotbrand_dtl, null);
                new ViewHolder(convertView);
            } else {
                convertView = View.inflate(mContext, R.layout.item_hotbrands_pinned, null);
                new PinnedViewHolder(convertView);
            }
        }


        if (viewType == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) convertView.getTag();
            new FHImageViewUtil(headerViewHolder.ivBanner).setImageURI(aBrand != null ? aBrand.brand_banner : "", FHImageViewUtil.SHOWTYPE.NEWHOME_BRAND);
        } else if (viewType == TYPE_ITEM) {
            ViewHolder holder = (ViewHolder) convertView.getTag();

            new FHImageViewUtil(holder.fiv_pic).setImageURI(getItem(position).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);

            /**
             * 国旗及描述行
             */
            holder.tv_global.setText(getItem(position).goods_promise);
            new FHImageViewUtil(holder.iv_flag).setImageURI(getItem(position).nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);

            /**
             * 全球购标志
             */
            if (getItem(position).goods_source > 0) {
                holder.iv_flag_global.setVisibility(View.VISIBLE);
            } else {
                holder.iv_flag_global.setVisibility(View.GONE);
            }

            if (getItem(position).sale_stop == 1) {
                holder.tv_addcart.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_addcart.setVisibility(View.VISIBLE);
            }

            /**
             * 已售罄、已下架等透明图片
             */
            holder.iv_top.setImageResource(getItem(position).getGoodsSaleState());

            /**
             * 添加标签后缀
             */
            holder.tv_title.setText(getItem(position).getGoodsName(mContext));
            BaseFunc.setFont(holder.tv_title);

            holder.tv_price.setText(df.format(getItem(position).goods_price));
            holder.tv_memo.setText(df.format(getItem(position).goods_marketprice));
            holder.tv_memo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            BaseFunc.setFont(holder.LIcon);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.gotoGoodsDetail(mContext, getItem(position).goods_id, getItem(position).resource_tags,null);
                }
            });

            holder.tv_addcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateAction.add2Cart((Activity) mContext, getItem(position).goods_id, 1, getItem(position).goods_source);
                }
            });
        } else {
            PinnedViewHolder pinnedViewHolder = (PinnedViewHolder) convertView.getTag();

            pinnedViewHolder.tvDefault.setSelected(pinnedIndex == 0);
            pinnedViewHolder.tvSales.setSelected(pinnedIndex == 1);
            pinnedViewHolder.LPrice.setSelected(pinnedIndex == 2);
            pinnedViewHolder.tvPopular.setSelected(pinnedIndex == 3);

            if (pinnedIndex == 2) {
                if (sortIndex == 1) {
                    pinnedViewHolder.tvUp.setSelected(true);
                    pinnedViewHolder.tvDown.setSelected(false);
                } else {
                    pinnedViewHolder.tvUp.setSelected(false);
                    pinnedViewHolder.tvDown.setSelected(true);
                }
            } else {
                pinnedViewHolder.tvUp.setSelected(false);
                pinnedViewHolder.tvDown.setSelected(false);
            }

        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView fiv_pic;
        public LinearLayout LGlobal;
        public ImageView iv_flag, iv_top, iv_flag_global;
        public TextView tv_global;
        public TextView tv_title;
        public LinearLayout LIcon;
        public TextView tv_price;
        public TextView tv_memo;
        public TextView tv_addcart;

        public ViewHolder(View view) {
            fiv_pic = (ImageView) view.findViewById(R.id.fiv_pic);
            LGlobal = (LinearLayout) view.findViewById(R.id.LGlobal);
            iv_flag = (ImageView) view.findViewById(R.id.iv_flag);
            iv_top = (ImageView) view.findViewById(R.id.iv_top);
            iv_flag_global = (ImageView) view.findViewById(R.id.iv_flag_global);
            tv_global = (TextView) view.findViewById(R.id.tv_global);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            LIcon = (LinearLayout) view.findViewById(R.id.LIcon);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_memo = (TextView) view.findViewById(R.id.tv_memo);
            tv_addcart = (TextView) view.findViewById(R.id.tv_addcart);

            view.setTag(this);
        }
    }

    class HeaderViewHolder {
        ImageView ivBanner;

        public HeaderViewHolder(View view) {
            ivBanner = (ImageView) view.findViewById(R.id.ivBanner);
            ivBanner.setLayoutParams(params);
            view.setTag(this);
        }
    }

    class PinnedViewHolder {
        LinearLayout LGrp;
        TextView tvDefault;
        TextView tvSales;
        LinearLayout LPrice;
        TextView tvUp, tvDown;
        TextView tvPopular;

        public PinnedViewHolder(View view) {
            LGrp = (LinearLayout) view.findViewById(R.id.LGrp);
            tvDefault = (TextView) view.findViewById(R.id.tvDefault);
            tvSales = (TextView) view.findViewById(R.id.tvSales);
            LPrice = (LinearLayout) view.findViewById(R.id.LPrice);
            tvUp = (TextView) view.findViewById(R.id.tvUp);
            tvDown = (TextView) view.findViewById(R.id.tvDown);
            tvPopular = (TextView) view.findViewById(R.id.tvPopular);

            BaseFunc.setFont(tvUp);
            BaseFunc.setFont(tvDown);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemCallBack != null) {
                        switch (v.getId()) {
                            case R.id.tvDefault:
                                itemCallBack.onItemClick(0, LGrp, tvUp, tvDown);
                                break;
                            case R.id.tvSales:
                                itemCallBack.onItemClick(1, LGrp, tvUp, tvDown);
                                break;
                            case R.id.LPrice:
                                itemCallBack.onItemClick(2, LGrp, tvUp, tvDown);
                                break;
                            case R.id.tvPopular:
                                itemCallBack.onItemClick(3, LGrp, tvUp, tvDown);
                                break;
                        }
                    }
                }
            };
            tvDefault.setOnClickListener(clickListener);
            tvSales.setOnClickListener(clickListener);
            LPrice.setOnClickListener(clickListener);
            tvPopular.setOnClickListener(clickListener);

            view.setTag(this);
        }
    }


    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TYPE_PINNED;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1) {
            return TYPE_PINNED;
        } else {
            return TYPE_ITEM;
        }
    }

    public interface PinnedItemCallBack {
        void onItemClick(int index, LinearLayout LGrp, View viewUp, View viewDown);
    }

    public PinnedItemCallBack itemCallBack;

    public void setItemCallBack(PinnedItemCallBack callBack) {
        this.itemCallBack = callBack;
    }
}
