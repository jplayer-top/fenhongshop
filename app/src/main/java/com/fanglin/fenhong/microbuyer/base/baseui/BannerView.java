package com.fanglin.fenhong.microbuyer.base.baseui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Banner视图的处理
 * Created by Plucky on 2015/8/9.
 */
public class BannerView {

    LinearLayout.LayoutParams rl;
    private Context mContext;
    private FHImageViewUtil.SHOWTYPE showType = FHImageViewUtil.SHOWTYPE.BANNER;

    public BannerView(Context context) {
        this.mContext = context;

    }

    public void setHeightPxOld(int hpx) {
        int h = hpx * BaseFunc.getDisplayMetrics(mContext).widthPixels / 720;
        rl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
    }

    public void setShowType(FHImageViewUtil.SHOWTYPE showType) {
        this.showType = showType;
    }

    public void setHeightPxNew(int hpx) {
        int h = hpx * BaseFunc.getDisplayMetrics(mContext).widthPixels / 750;
        rl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
    }

    public static int getBannerHeight(Context mContext, int hpx) {
        return hpx * BaseFunc.getDisplayMetrics(mContext).widthPixels / 750;
    }

    public View getMainBannerView(final List<Banner> banners) {

        if (banners == null || banners.size() == 0) {
            return new View(mContext);
        }
        int size = banners.size();
        final View headerView;

        if (size > 2) {
            //自动轮播
            headerView = View.inflate(mContext, R.layout.view_custom_header, null);
        } else {
            if (size == 1) {
                //不自动轮播且不显示指示器
                headerView = View.inflate(mContext, R.layout.view_custom_header_single, null);
            } else {
                //不自动轮播
                headerView = View.inflate(mContext, R.layout.view_custom_header_double, null);
            }
        }

        final BGABanner banner = (BGABanner) headerView.findViewById(R.id.banner);
        banner.setTransitionEffect(BGABanner.TransitionEffect.Default);

        List<View> views = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final String image_url = banners.get(i).image_url;
            // 指定图片
            ImageView v = (ImageView) View.inflate(mContext, R.layout.view_image, null);
            new FHImageViewUtil(v).setImageURI(image_url, showType);
            // tag为Url
            v.setTag(banners.get(i).link_url);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String aurl = String.valueOf(v.getTag());
                    BaseFunc.urlClick(mContext, aurl);
                }
            });
            views.add(v);
        }

        if (views.size() > 0) {
            banner.setViews(views);
        }

        if (rl != null) {
            banner.setLayoutParams(rl);
        }
        return headerView;
    }
}
