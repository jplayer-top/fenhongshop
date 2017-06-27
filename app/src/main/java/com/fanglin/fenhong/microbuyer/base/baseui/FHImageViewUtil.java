package com.fanglin.fenhong.microbuyer.base.baseui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.Settings;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhui.CircleImageView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * 作者： Created by Plucky on 15-10-3.
 * 分红全球购图片显示
 */
public class FHImageViewUtil {

    public final static String THUMB = "!thumb";

    public enum SHOWTYPE {
        LOADING, BANNER, ADV, DEFAULT, AVATAR, SEQBANNER, GROUP, REC,
        THEMEONE_CATEGORY, COUNTRY, NEWHOME_BANNER, THEMEONE_BANNER,
        NEWHOME_ADVRIGHT, NEWHOME_BRAND, THEMEONE_HOTGOODS_ONLYBANNER,
        DEFAULT_CAPTCHA, TALENTBG, TALENTTIME, BGFIND, GRP_BANNER, STORE_BANNER, VIPBUYER,
        VIPSEASON, VIPYEAR
    }


    private Settings mset;
    private ImageView imgView;
    private FHApp app;
    private boolean docache = true;

    public FHImageViewUtil(ImageView imgView) {
        this.imgView = imgView;
        app = (FHApp) ((Activity) imgView.getContext()).getApplication();
        if (imgView instanceof CircleImageView) {
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        mset = app.getSettings();
    }

    public FHImageViewUtil enableCache(boolean docache) {
        this.docache = docache;
        return this;
    }

    public void setImageURI(String url, SHOWTYPE atype) {
        url = getQualityUri(url, atype);
        try {
            displayByUniversal(atype, url);
            FHLog.d("FHImageViewUtil", url);
        } catch (Exception e) {
            FHLog.d("FHImageViewUtil", url + ":OOM");
        }
    }


    /**
     * 在http://pic.fenhongshop.com
     * !small80  !pic600 !small120
     * 根据分红商场的图片格式进行画质处理
     * 域名http://img.fenhongshop.com 下：!small-!medium-!big
     * !medium 200×200 像素 作为普通画质
     * 原始尺寸作为 高画质
     */
    private String getQualityUri(String url, SHOWTYPE atype) {
        if (BaseFunc.isValidUrl(url)) {
            String domain_img = "http://img.fenhongshop.com";
            String doming_pic = "http://pic.fenhongshop.com";
            boolean is_img = url.contains(domain_img);
            boolean is_pic = url.contains(doming_pic);

            if (!url.contains("!")) {
                /** 如果是普通画质*/
                if (mset == null || !mset.highQuality) {
                    if (atype != null && (is_img || is_pic)) {
                        if (atype != SHOWTYPE.LOADING) {
                            url += THUMB;
                        }
                    }
                } else {
                    /** 高画质下主要判断 商品图*/
                    if (atype != null && (is_img || is_pic)) {
                        if (atype != SHOWTYPE.LOADING && atype != SHOWTYPE.TALENTBG && atype != SHOWTYPE.TALENTTIME) {
                            url += THUMB;
                        }
                    }
                }
            }

        }
        return url;
    }

    private int getPlaceHolder(SHOWTYPE type) {

        if (type == SHOWTYPE.REC) {
            return R.drawable.default_left_banner;
        }

        if (type == SHOWTYPE.BANNER) {
            return R.drawable.default_banner;
        }

        if (type == SHOWTYPE.GROUP) {
            return R.drawable.default_grp_bak;
        }

        if (type == SHOWTYPE.ADV) {
            return R.drawable.default_msg_center;
        }

        if (type == SHOWTYPE.DEFAULT) {
            return R.drawable.default_goods_img;
        }

        if (type == SHOWTYPE.AVATAR) {
            return R.drawable.default_avatar;
        }

        if (type == SHOWTYPE.LOADING) {
            return R.drawable.bg_loading;
        }

        if (type == SHOWTYPE.SEQBANNER) {
            return R.drawable.default_seq_banner;
        }
        if (type == SHOWTYPE.THEMEONE_CATEGORY) {
            return R.drawable.default_themeone_category;
        }
        if (type == SHOWTYPE.COUNTRY) {
            return R.drawable.default_country;
        }
        if (type == SHOWTYPE.NEWHOME_BANNER) {
            return R.drawable.default_newhome_banner;
        }
        if (type == SHOWTYPE.THEMEONE_BANNER) {
            return R.drawable.default_themeon_banner;
        }

        if (type == SHOWTYPE.NEWHOME_ADVRIGHT) {
            return R.drawable.default_newhome_advright;
        }

        if (type == SHOWTYPE.NEWHOME_BRAND) {
            return R.drawable.default_newhome_brand;
        }

        if (type == SHOWTYPE.THEMEONE_HOTGOODS_ONLYBANNER) {
            return R.drawable.default_themeone_hotgoods_onlybanner;
        }

        if (type == SHOWTYPE.DEFAULT_CAPTCHA) {
            return R.drawable.default_captcha;
        }

        if (type == SHOWTYPE.TALENTBG) {
            return R.drawable.bg_talent;
        }

        if (type == SHOWTYPE.TALENTTIME) {
            return R.drawable.default_goods_img;
        }

        if (type == SHOWTYPE.BGFIND) {
            return R.drawable.bg_find;
        }

        if (type == SHOWTYPE.GRP_BANNER) {
            return R.drawable.default_grpbg;
        }

        if (type == SHOWTYPE.STORE_BANNER) {
            return R.drawable.default_storebg;
        }
        if (type == SHOWTYPE.VIPBUYER) {
            return R.drawable.img_vipbuyer;
        }
        if (type == SHOWTYPE.VIPSEASON) {
            return R.drawable.img_vipcard_season;
        }
        if (type == SHOWTYPE.VIPYEAR) {
            return R.drawable.img_vipcard_year;
        }

        return R.drawable.default_goods_img;
    }

    public static String getThumbData(String imgs) {
        if (BaseFunc.isValidUrl(imgs)) {
            String res = imgs;
            if (!imgs.contains("!")) {
                String domain_img = "http://img.fenhongshop.com";
                String doming_pic = "http://pic.fenhongshop.com";
                boolean is_img = imgs.contains(domain_img);
                boolean is_pic = imgs.contains(doming_pic);
                if (is_img || is_pic) {
                    res += THUMB;
                }
            }
            return res;
        } else {
            return imgs;
        }
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 通过XUtils显示图片
     */
    private void displayByXUtils(SHOWTYPE atype, String url) {
        if (imgView == null) return;
        BitmapUtils bitmapUtils = new BitmapUtils(app);
        int defResId = getPlaceHolder(atype);
        bitmapUtils.configDefaultLoadingImage(defResId);
        bitmapUtils.configDefaultLoadFailedImage(defResId);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(imgView.getContext()));
        bitmapUtils.display(imgView, url);
    }

    private void displayByUniversal(SHOWTYPE atype, String url) {
        if (imgView == null) return;
        if (!docache) {
            ImageLoader.getInstance().getDiskCache().remove(url);
        }

        int defResId = getPlaceHolder(atype);
        DisplayImageOptions options = new DisplayImageOptions.Builder().
                showImageOnLoading(defResId).
                showImageForEmptyUri(defResId).
                showImageOnFail(defResId).
                cacheInMemory(docache).
                cacheOnDisk(docache).
                        /*considerExifParams (true).*/
                                bitmapConfig(Bitmap.Config.RGB_565).
                        /*displayer (new RoundedBitmapDisplayer (20)).*/
                                build();
        ImageLoader.getInstance().displayImage(url, imgView, options);
    }
}
