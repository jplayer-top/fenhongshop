package cn.sharesdk.fhshare.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import cn.sharesdk.framework.Platform;

/**
 * Created by Plucky on 2015/12/16.
 * 分享数据实体
 */
public class FHShareData implements Serializable {
    public String title;
    public String content;
    public String imgs;//普通单张图片
    public String imagePath;//文件本地路径
    public List<String> mul_img;//朋友圈多图分享
    public String url;
    public boolean shareImage;
    public Bitmap imageData;
    public boolean record;
    public int platType;//渠道type 参考：ShareRecord.calculateShareType
    public int getShareType() {
        return shareImage ? Platform.SHARE_IMAGE : Platform.SHARE_WEBPAGE;
    }

    public static void displayByUniversal(ImageView imgView, String url) {
        if (imgView == null) return;
        DisplayImageOptions options = new DisplayImageOptions.Builder().
                cacheInMemory(true).
                cacheOnDisk(true).
                bitmapConfig(Bitmap.Config.RGB_565).
                build();
        ImageLoader.getInstance().displayImage(url, imgView, options);
    }

    public static File getCacheByUrl(String url) {
        return ImageLoader.getInstance().getDiskCache().get(url);
    }

    @Override
    public String toString() {
        return "title:" + title + ",content:" + content + ",imgs:" + imgs + ",mul_img:" + mul_img + ",url:" + url;
    }
}
