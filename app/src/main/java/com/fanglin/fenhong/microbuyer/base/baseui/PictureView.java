package com.fanglin.fenhong.microbuyer.base.baseui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 单纯的图片轮播
 * Created by Plucky on 2016/4/12.
 */
public class PictureView {

    LinearLayout.LayoutParams rl;
    private Context mContext;
    private FHImageViewUtil.SHOWTYPE showType = FHImageViewUtil.SHOWTYPE.DEFAULT;

    public PictureView(Context context) {
        this.mContext = context;

    }

    public void setHeightPx(int hpx) {
        int h = hpx * BaseFunc.getDisplayMetrics(mContext).widthPixels / 750;
        rl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
    }

    public void setShowType(FHImageViewUtil.SHOWTYPE showType) {
        this.showType = showType;
    }


    public View getView(final List<Banner> banners) {
        //图片浏览器时不需要自动轮播 不需要考虑页数大于二的限制
        //且margin均为30dp
        View headerView = View.inflate(mContext, R.layout.view_pictures, null);
        final BGABanner banner = (BGABanner) headerView.findViewById(R.id.banner);

        banner.setTransitionEffect(BGABanner.TransitionEffect.Default);

        List<View> views = new ArrayList<>();
        for (int i = 0; i < banners.size(); i++) {
            final String image_url = banners.get(i).image_url;
            /** 指定图片*/
            ImageView v = (ImageView) View.inflate(mContext, R.layout.view_image, null);

            new FHImageViewUtil(v).setImageURI(image_url, showType);

            /** tag为Url */
            v.setTag(banners.get(i).link_url);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileUtils.BrowserOpenL(mContext, getBannerImgs(banners), image_url);
                }
            });

            views.add(v);
        }

        banner.setVisibility(View.VISIBLE);
        banner.setViews(views);
        if (rl != null) {
            banner.setLayoutParams(rl);
        }

        return headerView;
    }

    private List<String> getBannerImgs(List<Banner> banners) {
        if (banners != null && banners.size() > 0) {
            List<String> res = new ArrayList<>();
            for (int i = 0; i < banners.size(); i++) {
                res.add(banners.get(i).image_url);
            }
            return res;
        } else {
            return null;
        }
    }
}
