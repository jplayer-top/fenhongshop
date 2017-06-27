package com.fanglin.fenhong.microbuyer.dutyfree.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.PictureView;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.FHWebView;
import com.fanglin.fenhong.microbuyer.base.model.Banner;
import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;
import com.fanglin.fenhong.microbuyer.base.model.GoodsIntro;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.Favorable;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.ProductDetail;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsAttrAdpter;
import com.fanglin.fenhong.microbuyer.dutyfree.DutyfreeBrandDtlActivity;
import com.hb.views.PinnedSectionListView;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/8-下午7:13.
 * 功能描述: 极速免税店 适配器
 */
public class DFGoodsDetailAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_MAIN = 1;
    private static final int TYPE_NOTICE = 2;
    private static final int TYPE_BRAND = 3;
    private static final int TYPE_PROMISE = 4;
    private static final int TYPE_HEAD = 5;
    private static final int TYPE_DETAIL = 6;

    private Context mContext;
    private AbsListView.LayoutParams detailParams;
    private int index = 0;
    private int red, white, black;
    private Typeface iconfont;

    private ProductDetail detail;

    public DFGoodsDetailAdapter(Context mContext) {
        this.mContext = mContext;
        DisplayMetrics metrics = BaseFunc.getDisplayMetrics(mContext);
        int screenH = metrics.heightPixels;
        int hh = screenH - mContext.getResources().getDimensionPixelOffset(R.dimen.dp_of_175);
        detailParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, hh);
        red = mContext.getResources().getColor(R.color.fh_red);
        white = mContext.getResources().getColor(R.color.white);
        black = mContext.getResources().getColor(R.color.color_33);
        iconfont = BaseFunc.geticonFontType(mContext);
    }

    public void setDetail(ProductDetail detail) {
        this.detail = detail;
        notifyDataSetChanged();
    }

    public boolean hasPopUpInfo() {
        return detail != null && detail.getPopupInfo() != null;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TYPE_HEAD;
    }

    @Override
    public int getCount() {
        if (detail == null) return 0;
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 7;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (convertView == null) {
            switch (viewType) {
                case TYPE_BANNER:
                    convertView = View.inflate(mContext, R.layout.item_banner_container, null);
                    new BannerViewHolder(convertView);
                    break;
                case TYPE_MAIN:
                    convertView = View.inflate(mContext, R.layout.item_goods_msg_main, null);
                    new MainViewHolder(convertView);
                    break;
                case TYPE_NOTICE:
                    convertView = View.inflate(mContext, R.layout.item_goods_msg_notice, null);
                    new NoticeViewHolder(convertView);
                    break;
                case TYPE_BRAND:
                    convertView = View.inflate(mContext, R.layout.item_goods_msg_brand, null);
                    new BrandViewHolder(convertView);
                    break;
                case TYPE_PROMISE:
                    convertView = View.inflate(mContext, R.layout.item_goods_msg_promise, null);
                    new PromiseViewHolder(convertView);
                    break;
                case TYPE_HEAD:
                    convertView = View.inflate(mContext, R.layout.item_goods_msg_head, null);
                    new HeadViewHolder(convertView);
                    break;
                case TYPE_DETAIL:
                    convertView = View.inflate(mContext, R.layout.item_goods_msg_detail, null);
                    new DetailViewHolder(convertView);
                    break;
            }
        }

        switch (viewType) {
            case TYPE_BANNER:
                BannerViewHolder banner = (BannerViewHolder) convertView.getTag();
                banner.LBanner.removeAllViews();
                List<Banner> banners = detail.getImageBanner();
                if (banners != null && banners.size() > 0) {
                    PictureView pictureView = new PictureView(mContext);
                    pictureView.setShowType(FHImageViewUtil.SHOWTYPE.DEFAULT);
                    pictureView.setHeightPx(750);
                    banner.LBanner.addView(pictureView.getView(banners));
                }
                break;
            case TYPE_MAIN:
                MainViewHolder mainer = (MainViewHolder) convertView.getTag();
                mainer.refreshView();
                break;
            case TYPE_NOTICE:
                NoticeViewHolder noticer = (NoticeViewHolder) convertView.getTag();
                noticer.refreshView();
                break;
            case TYPE_BRAND:
                BrandViewHolder brander = (BrandViewHolder) convertView.getTag();
                BrandMessage brandMessage = detail.getBrand();
                brander.refreshView(brandMessage);
                break;
            case TYPE_PROMISE:
                PromiseViewHolder promiser = (PromiseViewHolder) convertView.getTag();
                promiser.refreshView();
                break;
            case TYPE_HEAD:
                HeadViewHolder header = (HeadViewHolder) convertView.getTag();
                header.refreshView();
                break;
            case TYPE_DETAIL:
                DetailViewHolder detailer = (DetailViewHolder) convertView.getTag();
                detailer.refreshView();
                break;
        }
        return convertView;
    }


    private class BannerViewHolder {
        LinearLayout LBanner;

        BannerViewHolder(View itemView) {
            LBanner = (LinearLayout) itemView.findViewById(R.id.LBanner);
            itemView.setTag(this);
        }
    }

    private class MainViewHolder {
        LinearLayout LPrice;
        View vPrice;
        TextView tvPriceDollar, tvPriceRmb;
        TextView tvGoodsName;
        TextView tvVIP;
        LinearLayout LNotice;
        TextView tvNotice;
        ImageView ivNationFlag;
        TextView tvNationDesc;
        LinearLayout LTaxfee;
        TextView tvTaxLbl, tvTaxfeeIcon;
        //VIPBuyer
        FrameLayout FFavorable;
        TextView tvFavorablePriceDesc, tvFavorableLabel, tvNormalPriceDesc;
        ImageView ivVipBuyer;
        View vFavorableDesc;
        TextView tvFavorableDesc;


        MainViewHolder(View itemView) {
            LPrice = (LinearLayout) itemView.findViewById(R.id.LPrice);
            vPrice = itemView.findViewById(R.id.vPrice);
            tvPriceDollar = (TextView) itemView.findViewById(R.id.tvPriceDollar);
            tvPriceRmb = (TextView) itemView.findViewById(R.id.tvPriceRmb);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            LNotice = (LinearLayout) itemView.findViewById(R.id.LNotice);
            tvNotice = (TextView) itemView.findViewById(R.id.tvNotice);
            ivNationFlag = (ImageView) itemView.findViewById(R.id.ivNationFlag);
            tvNationDesc = (TextView) itemView.findViewById(R.id.tvNationDesc);
            LTaxfee = (LinearLayout) itemView.findViewById(R.id.LTaxfee);
            tvTaxLbl = (TextView) itemView.findViewById(R.id.tvTaxLbl);
            tvTaxfeeIcon = (TextView) itemView.findViewById(R.id.tvTaxfeeIcon);
            tvVIP = (TextView) itemView.findViewById(R.id.tvVIP);

            FFavorable = (FrameLayout) itemView.findViewById(R.id.FFavorable);
            tvFavorablePriceDesc = (TextView) itemView.findViewById(R.id.tvFavorablePriceDesc);
            tvFavorableLabel = (TextView) itemView.findViewById(R.id.tvFavorableLabel);
            tvNormalPriceDesc = (TextView) itemView.findViewById(R.id.tvNormalPriceDesc);
            tvFavorableDesc = (TextView) itemView.findViewById(R.id.tvFavorableDesc);
            ivVipBuyer = (ImageView) itemView.findViewById(R.id.ivVipBuyer);
            vFavorableDesc = itemView.findViewById(R.id.vFavorableDesc);

            tvTaxLbl.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvTaxfeeIcon.setTypeface(iconfont);
            LTaxfee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailListener != null) {
                        detailListener.onTaxClick();
                    }
                }
            });
            tvFavorableDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (detailListener != null) {
                        detailListener.onBuyVipClick();
                    }
                }
            });
            itemView.setTag(this);
        }

        public void refreshView() {
            tvGoodsName.setText(detail.getProductName());
            String notice = detail.getGoodsNotice();
            if (!TextUtils.isEmpty(notice)) {
                tvNotice.setText(notice);
                LNotice.setVisibility(View.VISIBLE);
            } else {
                LNotice.setVisibility(View.GONE);
            }

            if (detail.showItem()) {
                tvPriceDollar.setText(detail.getItemPrice());
                tvVIP.setVisibility(View.VISIBLE);
                tvVIP.setText(detail.getItemLabel());
                tvPriceRmb.setText(detail.getPriceGray());
                tvPriceRmb.setTextColor(mContext.getResources().getColor(R.color.color_99));
            } else {
                tvPriceDollar.setText(detail.getPriceDollar4Show());
                tvVIP.setVisibility(View.GONE);
                tvPriceRmb.setText(detail.getPriceRmb4Detail());
                tvPriceRmb.setTextColor(mContext.getResources().getColor(R.color.fh_red));
            }

            new FHImageViewUtil(ivNationFlag).setImageURI(detail.getNationFlag(), FHImageViewUtil.SHOWTYPE.BANNER);
            tvNationDesc.setText(detail.getGoodsPromise());

            Favorable favorable = detail.getFavorable();
            if (favorable != null) {
                FFavorable.setVisibility(View.VISIBLE);
                vPrice.setVisibility(View.GONE);
                LPrice.setVisibility(View.GONE);

                tvFavorablePriceDesc.setText(favorable.getFavorablePriceDesc());
                tvFavorableLabel.setText(favorable.getFavorableLabel());
                tvNormalPriceDesc.setText(favorable.getNormalPriceDesc());
                new FHImageViewUtil(ivVipBuyer).setImageURI(favorable.getFavorableImg(), FHImageViewUtil.SHOWTYPE.VIPBUYER);

                String favorableDesc = favorable.getFavorableDesc();
                if (!TextUtils.isEmpty(favorableDesc)) {
                    tvFavorableDesc.setVisibility(View.VISIBLE);
                    vFavorableDesc.setVisibility(View.VISIBLE);
                    tvFavorableDesc.setText(favorableDesc);
                } else {
                    tvFavorableDesc.setVisibility(View.GONE);
                    vFavorableDesc.setVisibility(View.GONE);
                }
            } else {
                FFavorable.setVisibility(View.GONE);
                vPrice.setVisibility(View.VISIBLE);
                LPrice.setVisibility(View.VISIBLE);
            }
        }
    }

    private class NoticeViewHolder {
        View vSpecLine;
        LinearLayout LSpec;
        TextView tvSpec;
        View vDiscount;
        LinearLayout LDiscount;
        TextView tvDiscountDesc;
        LinearLayout LFreight;
        TextView tvFreight;
        ImageView ivFreight;
        LinearLayout LReadMe;
        TextView tvReadMe;
        View vReadMe;

        NoticeViewHolder(View itemView) {
            vSpecLine = itemView.findViewById(R.id.vSpecLine);
            LSpec = (LinearLayout) itemView.findViewById(R.id.LSpec);
            tvSpec = (TextView) itemView.findViewById(R.id.tvSpec);
            vDiscount = itemView.findViewById(R.id.vDiscount);
            LDiscount = (LinearLayout) itemView.findViewById(R.id.LDiscount);
            tvDiscountDesc = (TextView) itemView.findViewById(R.id.tvDiscountDesc);
            LFreight = (LinearLayout) itemView.findViewById(R.id.LFreight);
            tvFreight = (TextView) itemView.findViewById(R.id.tvFreight);
            ivFreight = (ImageView) itemView.findViewById(R.id.ivFreight);
            LReadMe = (LinearLayout) itemView.findViewById(R.id.LReadMe);
            tvReadMe = (TextView) itemView.findViewById(R.id.tvReadMe);
            vReadMe = itemView.findViewById(R.id.vReadMe);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.LSpec:
                            if (detailListener != null) {
                                detailListener.onSpecClick();
                            }
                            break;
                        case R.id.LReadMe:
                            if (detailListener != null) {
                                detailListener.onReadMeClick();
                            }
                            break;
                        case R.id.LFreight:
                            if (detailListener != null) {
                                if (ivFreight.getVisibility() == View.VISIBLE) {
                                    detailListener.onFreightClick();
                                }
                            }
                            break;
                    }
                }
            };
            LSpec.setOnClickListener(listener);
            LReadMe.setOnClickListener(listener);
            LFreight.setOnClickListener(listener);
            itemView.setTag(this);
        }

        public void refreshView() {
            LSpec.setVisibility(View.GONE);
            vSpecLine.setVisibility(View.GONE);
            String discountDesc = detail.getGoodsDiscountDesc();
            if (!TextUtils.isEmpty(discountDesc)) {
                vDiscount.setVisibility(View.VISIBLE);
                LDiscount.setVisibility(View.VISIBLE);
                tvDiscountDesc.setText(BaseFunc.fromHtml(discountDesc));
            } else {
                vDiscount.setVisibility(View.GONE);
                LDiscount.setVisibility(View.GONE);
            }
            Spanned freightDesc = detail.getGoodsFreightDesc();
            if (!TextUtils.isEmpty(freightDesc)) {
                LFreight.setVisibility(View.VISIBLE);
                tvFreight.setText(freightDesc);
                int visibility = TextUtils.isEmpty(detail.getGoodsFreightIntro()) ? View.GONE : View.VISIBLE;
                ivFreight.setVisibility(visibility);
            } else {
                LFreight.setVisibility(View.GONE);
            }
            List<GoodsIntro> intros = detail.getGoodsIntroInfo();
            if (intros != null && intros.size() > 0) {
                tvReadMe.setText(detail.getRefundTips(tvReadMe.getContext()));
                LReadMe.setVisibility(View.VISIBLE);
            } else {
                LReadMe.setVisibility(View.GONE);
            }
            vReadMe.setVisibility(LReadMe.getVisibility());
        }
    }

    private class BrandViewHolder {
        LinearLayout LContainer;
        ImageView ivBrandLogo;
        TextView tvBrandName, tvBrandGoodsNum, tvIntoBrand, tvBrandDesc;
        RecyclerView recyclerView;

        BrandViewHolder(View itemView) {
            LContainer = (LinearLayout) itemView.findViewById(R.id.LContainer);
            ivBrandLogo = (ImageView) itemView.findViewById(R.id.ivBrandLogo);
            tvBrandName = (TextView) itemView.findViewById(R.id.tvBrandName);
            tvBrandGoodsNum = (TextView) itemView.findViewById(R.id.tvBrandGoodsNum);
            tvIntoBrand = (TextView) itemView.findViewById(R.id.tvIntoBrand);
            tvBrandDesc = (TextView) itemView.findViewById(R.id.tvBrandDesc);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            itemView.setTag(this);
        }

        public void refreshView(final BrandMessage brand) {
            if (brand != null) {
                LContainer.setVisibility(View.VISIBLE);
                final Context mContext = ivBrandLogo.getContext();
                new FHImageViewUtil(ivBrandLogo).setImageURI(brand.getBrand_pic(), FHImageViewUtil.SHOWTYPE.NEWHOME_BRAND);
                tvBrandName.setText(brand.getBrand_name());
                tvBrandGoodsNum.setText(brand.getGoodsStorage(mContext));
                if (!TextUtils.isEmpty(brand.getBrand_intro())) {
                    tvBrandDesc.setText(brand.getBrand_intro());
                    tvBrandDesc.setVisibility(View.VISIBLE);
                } else {
                    tvBrandDesc.setVisibility(View.GONE);
                }

                tvIntoBrand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoActivity(v.getContext(), DutyfreeBrandDtlActivity.class, brand.getBrand_id());
                    }
                });

                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(manager);
                DutyBrandGoodsAdapter adapter = new DutyBrandGoodsAdapter(mContext);
                adapter.setProducts(brand.getGoodslist());
                recyclerView.setAdapter(adapter);
            } else {
                LContainer.setVisibility(View.GONE);
            }
        }
    }

    private class PromiseViewHolder {
        ImageView ivGlobalService;

        PromiseViewHolder(View itemView) {
            ivGlobalService = (ImageView) itemView.findViewById(R.id.ivGlobalService);
            itemView.setTag(this);
        }

        public void refreshView() {
            ivGlobalService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, BaseVar.URL_QUALITY);
                }
            });
        }
    }

    private class HeadViewHolder {
        TextView tvDetail, tvAttr;
        View vDetail, vAttr;

        HeadViewHolder(View itemView) {
            tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
            tvAttr = (TextView) itemView.findViewById(R.id.tvAttr);
            vDetail = itemView.findViewById(R.id.vDetail);
            vAttr = itemView.findViewById(R.id.vAttr);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = v.getId() == R.id.tvDetail ? 0 : 1;
                    notifyDataSetChanged();
                }
            };
            tvDetail.setOnClickListener(listener);
            tvAttr.setOnClickListener(listener);
            itemView.setTag(this);
        }

        public void refreshView() {
            tvDetail.setTextColor(index == 0 ? red : black);
            vDetail.setBackgroundColor(index == 0 ? red : white);
            tvAttr.setTextColor(index == 1 ? red : black);
            vAttr.setBackgroundColor(index == 1 ? red : white);
        }
    }

    private class DetailViewHolder {
        LinearLayout LHold;
        FHWebView webView;
        ListView listView;

        DetailViewHolder(View itemView) {
            LHold = (LinearLayout) itemView.findViewById(R.id.LHold);
            webView = (FHWebView) itemView.findViewById(R.id.webView);
            listView = (ListView) itemView.findViewById(R.id.listView);
            LHold.setLayoutParams(detailParams);

            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setSupportZoom(true);
            settings.setDisplayZoomControls(false);
            settings.setBuiltInZoomControls(true);
            settings.setUserAgentString(settings.getUserAgentString() + "FHMall_Android");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    webView.loadUrl(BaseVar.FAILEDWEBNULL);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    webView.loadUrl(BaseVar.FAILEDWEBNULL);
                }
            });
            webView.loadUrl(detail.getGoodsDtlUrl());

            /** 单品参数*/
            GoodsAttrAdpter attrAdpter = new GoodsAttrAdpter(mContext);
            attrAdpter.setJson(detail.getGoods_attr());
            listView.setAdapter(attrAdpter);


            itemView.setTag(this);
        }

        public void refreshView() {
            webView.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
            listView.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
            webView.setRequestScroll(true);
        }
    }

    private DetailClickListener detailListener;

    public void setDetailListener(DetailClickListener detailListener) {
        this.detailListener = detailListener;
    }

    public interface DetailClickListener {
        void onTaxClick();

        void onFreightClick();

        void onSpecClick();

        void onReadMeClick();

        void onBuyVipClick();
    }
}
