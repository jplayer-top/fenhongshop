package cn.sharesdk.fhshare.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.R;
import cn.sharesdk.fhshare.FHShareDialog;
import cn.sharesdk.fhshare.FHShareManyPicsActivity;
import cn.sharesdk.fhshare.FHTCNUtils;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享 Item 实体类
 * 每一个种类的点击事件都在这里设置
 * Created by lizhixin on 2015/12/10.
 */
public class FHShareItem {
    public static final int TYPE_QQ = 2;
    public static final int TYPE_SinaWeibo = 3;
    public static final int TYPE_WechatMoments = 4;
    public static final int TYPE_Wechat = 5;
    public static final int TYPE_QZone = 6;
    public static final int TYPE_Email = 8;
    public static final String COPYLINK = "fhshare_copylink";
    public static final String MANYPICS = "fhshare_manypics";

    private String name;
    private int icon;//图标代号
    private int iconColor;//图标颜色
    private String link;//分享链接
    private View.OnClickListener listener;//点击事件，谁用谁set

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 分
     * 享
     * 渠
     * 道
     * 开
     * 始
     */
    public static void shareAppType(Context mContext, FHShareData sharedata, PlatformActionListener actionListener, BeforeShareActionListener beforeShareActionListener, int platType) {
        switch (platType) {
            case TYPE_Wechat://微信好友分享
                shareByWeChat(mContext, sharedata, actionListener, beforeShareActionListener);
                break;
            case TYPE_QZone://QQ空间
                shareByQzone(mContext, sharedata, actionListener, beforeShareActionListener);
                break;
            case TYPE_QQ://QQ
                shareByQQ(mContext, sharedata, actionListener, beforeShareActionListener);
                break;
            case TYPE_SinaWeibo://新浪微博
                shareBySina(mContext, sharedata, actionListener, beforeShareActionListener);
                break;
        }
    }

    /**
     * 单独微信好友分享
     */
    public static void shareByWeChat(Context mContext, FHShareData sharedata, PlatformActionListener actionListener, BeforeShareActionListener beforeShareActionListener) {

        if (sharedata == null) {
            return;
        }

        if (beforeShareActionListener != null) {
            beforeShareActionListener.onbeforeShare(Wechat.NAME, sharedata);
        }

        Wechat.ShareParams params = new Wechat.ShareParams();
        params.setTitle(sharedata.title);
        params.setText(sharedata.content);
        params.setImageUrl(sharedata.imgs);
        if (!TextUtils.isEmpty(sharedata.imagePath)) {
            params.setImagePath(sharedata.imagePath);
        }

        if (sharedata.imageData != null) {
            params.setImageData(sharedata.imageData);
        }

        params.setUrl(sharedata.url);
        params.setShareType(sharedata.getShareType());

        Wechat wechat = new Wechat(mContext);
        wechat.setPlatformActionListener(actionListener);
        wechat.share(params);

    }

    /**
     * 单独微信朋友圈分享
     */
    public static void shareByWeChatMoments(Context mContext, FHShareData sharedata, PlatformActionListener actionListener, BeforeShareActionListener beforeShareActionListener) {
        if (sharedata == null) {
            return;
        }

        if (beforeShareActionListener != null) {
            beforeShareActionListener.onbeforeShare(WechatMoments.NAME, sharedata);
        }

        WechatMoments.ShareParams params = new WechatMoments.ShareParams();
        String shareDesc = sharedata.title + "\n" + sharedata.content;//朋友圈 标题+文字
        params.setTitle(shareDesc);
        params.setText(shareDesc);
        params.setImageUrl(sharedata.imgs);
        if (!TextUtils.isEmpty(sharedata.imagePath)) {
            params.setImagePath(sharedata.imagePath);
        }

        if (sharedata.imageData != null) {
            params.setImageData(sharedata.imageData);
        }
        params.setUrl(sharedata.url);
        params.setShareType(sharedata.getShareType());

        WechatMoments wechatMoments = new WechatMoments(mContext);
        wechatMoments.setPlatformActionListener(actionListener);
        wechatMoments.share(params);
    }

    /**
     * 微信收藏
     */
    public static void shareByWechatCollect(Context mContext, FHShareData sharedata, PlatformActionListener actionListener, BeforeShareActionListener beforeShareActionListener) {
        if (sharedata == null) {
            return;
        }

        if (beforeShareActionListener != null) {
            beforeShareActionListener.onbeforeShare(WechatFavorite.NAME, sharedata);
        }

        WechatFavorite.ShareParams params = new WechatFavorite.ShareParams();
        params.setTitle(sharedata.title);
        params.setText(sharedata.content);
        params.setImageUrl(sharedata.imgs);
        if (!TextUtils.isEmpty(sharedata.imagePath)) {
            params.setImagePath(sharedata.imagePath);
        }

        if (sharedata.imageData != null) {
            params.setImageData(sharedata.imageData);
        }

        params.setUrl(sharedata.url);
        params.setShareType(sharedata.getShareType());

        WechatFavorite wechatFavorite = new WechatFavorite(mContext);
        wechatFavorite.setPlatformActionListener(actionListener);
        wechatFavorite.share(params);
    }

    /**
     * 多图分享
     */
    public static void shareManyPics(Context mContext, FHShareData sharedata, BeforeShareActionListener beforeShareActionListener) {
        if (sharedata == null) {
            return;
        }

        if (beforeShareActionListener != null) {
            beforeShareActionListener.onbeforeShare(MANYPICS, sharedata);
        }

        Intent intent = new Intent(mContext, FHShareManyPicsActivity.class);
        intent.putExtra("FHShareData", sharedata);
        mContext.startActivity(intent);
    }


    /**
     * 复制链接功能
     */
    public static void copyLink(FHShareData sharedata, FHTCNUtils.tcnCallBack cb, BeforeShareActionListener beforeShareActionListener) {
        Log.d("lizx->copylink", sharedata == null ? "no share data" : "=================" + sharedata.url);
        if (sharedata == null) {
            return;
        }

        if (beforeShareActionListener != null) {
            beforeShareActionListener.onbeforeShare(COPYLINK, sharedata);
        }

        String url = sharedata.url;
        new FHTCNUtils().setCallBack(cb).getShortUrl(url);
    }


    /**
     * QQ分享
     */
    public static void shareByQQ(Context mContext, FHShareData sharedata, PlatformActionListener actionListener, BeforeShareActionListener beforeShareActionListener) {
        if (sharedata == null) {
            return;
        }

        if (beforeShareActionListener != null) {
            beforeShareActionListener.onbeforeShare(QQ.NAME, sharedata);
        }

        QQ.ShareParams params = new QQ.ShareParams();
        params.setTitle(sharedata.title);
        params.setText(sharedata.content);
        params.setImageUrl(sharedata.imgs);
        if (!TextUtils.isEmpty(sharedata.imagePath)) {
            params.setImagePath(sharedata.imagePath);
        }

        if (sharedata.imageData != null) {
            params.setImageData(sharedata.imageData);
        }

        params.setUrl(sharedata.url);
        params.setTitleUrl(sharedata.url);
        params.setShareType(sharedata.getShareType());

        QQ qq = new QQ(mContext);
        qq.setPlatformActionListener(actionListener);
        qq.share(params);
    }

    /**
     * QQ空间分享
     */
    public static void shareByQzone(Context mContext, FHShareData sharedata, PlatformActionListener actionListener, BeforeShareActionListener beforeShareActionListener) {

        if (sharedata == null) {
            return;
        }

        if (beforeShareActionListener != null) {
            beforeShareActionListener.onbeforeShare(QZone.NAME, sharedata);
        }

        QZone.ShareParams params = new QZone.ShareParams();
        params.setTitle(sharedata.title);
        params.setText(sharedata.content);
        params.setImageUrl(sharedata.imgs);
        if (!TextUtils.isEmpty(sharedata.imagePath)) {
            params.setImagePath(sharedata.imagePath);
        }

        if (sharedata.imageData != null) {
            params.setImageData(sharedata.imageData);
        }

        params.setUrl(sharedata.url);
        params.setTitleUrl(sharedata.url);
        params.setShareType(sharedata.getShareType());

        QZone qZone = new QZone(mContext);
        qZone.setPlatformActionListener(actionListener);
        qZone.share(params);
    }

    /**
     * 新浪微博分享
     */
    public static void shareBySina(Context mContext, FHShareData sharedata, PlatformActionListener actionListener, BeforeShareActionListener beforeShareActionListener) {
        if (sharedata == null) {
            return;
        }

        if (beforeShareActionListener != null) {
            beforeShareActionListener.onbeforeShare(SinaWeibo.NAME, sharedata);
        }

        SinaWeibo.ShareParams params = new SinaWeibo.ShareParams();
        params.setTitle(sharedata.title);
        params.setTitleUrl(sharedata.url);
        params.setText(sharedata.content);
        params.setImageUrl(sharedata.imgs);
        if (!TextUtils.isEmpty(sharedata.imagePath)) {
            params.setImagePath(sharedata.imagePath);
        }

        if (sharedata.imageData != null) {
            params.setImageData(sharedata.imageData);
        }

        params.setUrl(sharedata.url);
        params.setShareType(sharedata.getShareType());

        SinaWeibo sinaWeibo = new SinaWeibo(mContext);
        sinaWeibo.setPlatformActionListener(actionListener);
        sinaWeibo.share(params);
    }

    /**
     * 分
     * 享
     * 渠
     * 道
     * 结
     * 束
     *
     */


    /**
     * 测
     * 试
     * 用
     * 例
     * 数
     * 据
     * <p/>
     * <p/>
     * 分享——所有渠道
     */
    public static List<FHShareItem> getAll(final Context mContext, final FHShareData sharedata, final PlatformActionListener actionListener, final BeforeShareActionListener beforeShareActionListener) {
        if (sharedata == null) {
            return null;
        }

        List<FHShareItem> defaultList = new ArrayList<>();

        if (isAvilible(mContext, "com.tencent.mm")) {
            final FHShareItem item1 = new FHShareItem();
            item1.setIcon(cn.sharesdk.R.drawable.fhshare_weixin_friend);
            item1.setIconColor(111);
            item1.setName("微信好友");
            item1.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareByWeChat(mContext, sharedata, actionListener, beforeShareActionListener);
                }
            });
            FHShareItem item2 = new FHShareItem();
            item2.setIcon(cn.sharesdk.R.drawable.fhshare_friends);
            item2.setIconColor(111);
            item2.setName("朋友圈");
            item2.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareByWeChatMoments(mContext, sharedata, actionListener, beforeShareActionListener);
                }
            });

            FHShareItem item3 = new FHShareItem();
            item3.setIcon(cn.sharesdk.R.drawable.fhshare_weixin_collect);
            item3.setIconColor(111);
            item3.setName("微信收藏");
            item3.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareByWechatCollect(mContext, sharedata, actionListener, beforeShareActionListener);
                }
            });

            defaultList.add(item1);
            defaultList.add(item2);
            defaultList.add(item3);
        }


        if (isQQAvaliable(mContext)) {
            FHShareItem item4 = new FHShareItem();
            item4.setIcon(cn.sharesdk.R.drawable.fhshare_qq_friend);
            item4.setIconColor(111);
            item4.setName("QQ好友");
            item4.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareByQQ(mContext, sharedata, actionListener, beforeShareActionListener);
                }
            });

            FHShareItem item5 = new FHShareItem();
            item5.setIcon(cn.sharesdk.R.drawable.fhshare_qq_zone);
            item5.setIconColor(111);
            item5.setName("QQ空间");
            item5.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareByQzone(mContext, sharedata, actionListener, beforeShareActionListener);
                }
            });

            defaultList.add(item4);
            defaultList.add(item5);
        }

        FHShareItem item6 = new FHShareItem();
        item6.setIcon(cn.sharesdk.R.drawable.fhshare_sinaweibo);
        item6.setIconColor(111);
        item6.setName("新浪微博");
        item6.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBySina(mContext, sharedata, actionListener, beforeShareActionListener);
            }
        });

        FHShareItem item7 = new FHShareItem();
        item7.setIcon(cn.sharesdk.R.drawable.fhshare_copy_link);
        item7.setIconColor(111);
        item7.setName("复制链接");
        item7.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyLink(sharedata, new FHTCNUtils.tcnCallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onEnd(int status, String data) {
                        FHTCNUtils.copy(mContext, data);
                        Toast.makeText(mContext, "链接已复制", Toast.LENGTH_SHORT).show();
                    }
                }, beforeShareActionListener);
            }
        });


        defaultList.add(item6);
        defaultList.add(item7);

        if (isAvilible(mContext, "com.tencent.mm")) {
            FHShareItem item8 = new FHShareItem();

            item8.setIcon(R.drawable.fhshare_mulimg);
            item8.setIconColor(111);
            item8.setName("多图分享");
            item8.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareManyPics(mContext, sharedata, beforeShareActionListener);
                }
            });

            defaultList.add(item8);
        }

        return defaultList;
    }

    public static boolean isQQAvaliable(Context mContext) {
        return FHShareDialog.isAvilible(mContext, "com.tencent.mobileqq") || FHShareDialog.isAvilible(mContext, "com.tencent.qqlite");
    }

    /**
     * 测
     * 试
     * 用
     * 例
     * 数
     * 据
     * <p/>
     * 分享----微信相关
     */
    public static List<FHShareItem> getWechatAll(final Context mContext, final FHShareData sharedata, final PlatformActionListener actionListener, final BeforeShareActionListener beforeShareActionListener) {
        if (sharedata == null) {
            return null;
        }
        List<FHShareItem> wechats = new ArrayList<>();

        if (isAvilible(mContext, "com.tencent.mm")) {
            /** 微信好友*/
            final FHShareItem wechat = new FHShareItem();
            wechat.setIcon(R.drawable.fhshare_weixin_friend);
            wechat.setIconColor(111);
            wechat.setName("微信好友");
            wechat.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareByWeChat(mContext, sharedata, actionListener, beforeShareActionListener);
                }
            });
            wechats.add(wechat);

            /** 微信朋友圈*/
            final FHShareItem moments = new FHShareItem();
            moments.setIcon(R.drawable.fhshare_friends);
            moments.setIconColor(111);
            moments.setName("朋友圈");
            moments.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareByWeChatMoments(mContext, sharedata, actionListener, beforeShareActionListener);
                }
            });
            wechats.add(moments);


            FHShareItem item3 = new FHShareItem();
            item3.setIcon(cn.sharesdk.R.drawable.fhshare_weixin_collect);
            item3.setIconColor(111);
            item3.setName("微信收藏");
            item3.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareByWechatCollect(mContext, sharedata, actionListener, beforeShareActionListener);
                }
            });
            wechats.add(item3);
        }

        FHShareItem item7 = new FHShareItem();
        item7.setIcon(cn.sharesdk.R.drawable.fhshare_copy_link);
        item7.setIconColor(111);
        item7.setName("复制链接");
        item7.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyLink(sharedata, new FHTCNUtils.tcnCallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onEnd(int status, String data) {
                        FHTCNUtils.copy(mContext, data);
                        Toast.makeText(mContext, "链接已复制", Toast.LENGTH_SHORT).show();
                    }
                }, beforeShareActionListener);
            }
        });

        if (isAvilible(mContext, "com.tencent.mm")) {
            FHShareItem item8 = new FHShareItem();
            item8.setIcon(R.drawable.fhshare_mulimg);
            item8.setIconColor(111);
            item8.setName("多图分享");
            item8.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareManyPics(mContext, sharedata, beforeShareActionListener);
                }
            });
            wechats.add(item8);
        }

        return wechats;
    }

    /**
     * 判断是否安装了某个应用
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) return true;
        }
        return false;
    }
}
