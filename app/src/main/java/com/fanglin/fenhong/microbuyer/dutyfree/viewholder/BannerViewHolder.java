package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BannerView;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Banner;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/14-下午1:34.
 * 功能描述:Banner
 */
public class BannerViewHolder extends RecyclerView.ViewHolder {

    LinearLayout LBanner;
    boolean runTest;

    public static final int BANNER_HEIGHT = 460;

    public BannerViewHolder(View itemView, boolean runTest) {
        super(itemView);
        this.runTest = runTest;
        LBanner = (LinearLayout) itemView.findViewById(R.id.LBanner);
    }

    public static View getView(Context mContext) {
        return View.inflate(mContext, R.layout.item_banner_container, null);
    }

    public static BannerViewHolder getHolder(Context mContext, boolean runTest) {
        View view = getView(mContext);
        return new BannerViewHolder(view, runTest);
    }

    public void refreshView(List<Banner> banners) {
        LBanner.removeAllViews();
        BannerView bannerView = new BannerView(LBanner.getContext());
        bannerView.setHeightPxNew(BANNER_HEIGHT);
        bannerView.setShowType(FHImageViewUtil.SHOWTYPE.NEWHOME_BANNER);
        if (runTest) {
            LBanner.addView(bannerView.getMainBannerView(Banner.getTest()));
        } else {
            if (banners != null && banners.size() > 0) {
                LBanner.addView(bannerView.getMainBannerView(banners));
            }
        }
    }
}
