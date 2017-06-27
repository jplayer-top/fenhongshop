package com.fanglin.fenhong.microbuyer.base.model;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baselib.VarInstance;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutShareDeduct;
import com.fanglin.fhlib.ypyun.api.Base64Coder;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.fhshare.FHShareDialog;
import cn.sharesdk.fhshare.model.BeforeShareActionListener;
import cn.sharesdk.fhshare.model.FHShareData;
import cn.sharesdk.fhshare.model.FHShareItem;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * 作者： Created by Plucky on 15-10-4.
 * WAP页面调用app分享传入的数据
 */
public class ShareData {
    public int shareType;//
    public String title;//分享的标题
    public String content;//分享的内容
    public String imgs;//分享的图片链接
    public List<String> mul_img;//多图分享时需要传入
    public String url;//分享链接
    public boolean show;
    public double deductMoney;
    public double price;
    public TalentShareFlag talentShareFlag;//区分达人及时光详情分享以及普通分享

    public boolean justWechat;
    public boolean shareImage;
    public boolean record;//是否调用share接口记录
    public boolean cutLastOne;
    public StoreHomeInfo storeInfo;

    public ShareData() {
        title = "分红全球购";
        content = "购全球，享分红";
        imgs = BaseVar.APP_LOGO;
        url = BaseVar.HOST;
        show = true;
        record = true;
    }

    /**
     * 静态可点击的图标，进行跳转
     *
     * @param member
     * @param shareJson
     * @return
     */
    public static FHShareData shareByItems(Member member, String shareJson) {
        if (!TextUtils.isEmpty(shareJson)) {
            shareJson = Base64Coder.safeDecodeString(shareJson);
            if (!TextUtils.isEmpty(shareJson)) {
                final ShareData shareData = new Gson().fromJson(shareJson, ShareData.class);
                if (shareData == null) return null;
                else {
                    final FHShareData fhShareData = new FHShareData();
                    String img;
                    if (BaseFunc.isValidUrl(shareData.imgs)) {
                        img = shareData.imgs;
                    } else {
                        img = BaseVar.APP_LOGO;
                    }
                    //需要压缩图片--否则分享的时候会出错
                    img = FHImageViewUtil.getThumbData(img);

                    fhShareData.imgs = img;//单张图片
                    fhShareData.mul_img = shareData.mul_img;//多图分享--微信多图分享会用到
                    fhShareData.title = BaseFunc.formatHtml(shareData.title);
                    fhShareData.content = BaseFunc.formatHtml(shareData.content);

                    //add
                    fhShareData.platType = shareData.shareType;
                    fhShareData.record = shareData.record;


                    String rurl = ShareData.getRurl(shareData.url, member, shareData.shareType);
                    fhShareData.url = ShareData.getShareUrl(rurl);
                    return fhShareData;
                }

            }
        }
        return null;
    }

    public static FHShareDialog shareByItems(final BaseFragmentActivity act, final ShareData sd, PlatformActionListener actionListener, final FHBrowserShareListener shareListener, FHShareItem... items) {
        if (sd != null && act != null) {
            ShareSDK.initSDK(act.getApplicationContext());
            /** 注册回写*/
            final Member member = FHCache.getMember(act);
            final ShareRecord shareRecord = new ShareRecord();
            shareRecord.member = member;
            shareRecord.talentShareFlag = sd.talentShareFlag;

            final FHShareData fhShareData = new FHShareData();

            String img;
            if (BaseFunc.isValidUrl(sd.imgs)) {
                img = sd.imgs;
            } else {
                img = BaseVar.APP_LOGO;
            }
            //需要压缩图片--否则分享的时候会出错
            img = FHImageViewUtil.getThumbData(img);


            fhShareData.imgs = img;//单张图片
            fhShareData.mul_img = sd.mul_img;//多图分享--微信多图分享会用到

            fhShareData.title = BaseFunc.formatHtml(sd.title);
            fhShareData.content = BaseFunc.formatHtml(sd.content);
            if (TextUtils.isEmpty(fhShareData.title)) {
                fhShareData.title = act.getString(R.string.app_name);
            }
            if (TextUtils.isEmpty(fhShareData.content)) {
                fhShareData.content = act.getString(R.string.app_concept);
            }

            if (actionListener == null) {
                actionListener = new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        if (handler != null) handler.sendEmptyMessage(0);
                        if (sd.record) {
                            shareRecord.share();
                        }

                        if (shareListener != null) {
                            int shareType = ShareRecord.calculateShareType(platform.getName());
                            shareListener.onBrowserShare(0, shareType, platform.getName());
                        }
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        if (handler != null) handler.sendEmptyMessage(2);

                        if (shareListener != null) {
                            shareListener.onBrowserShare(2, 0, platform.getName());
                        }
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        if (handler != null) handler.sendEmptyMessage(1);

                        if (shareListener != null) {
                            shareListener.onBrowserShare(1, 0, platform.getName());
                        }
                    }
                };
            }

            BeforeShareActionListener beforeShareActionListener = new BeforeShareActionListener() {
                @Override
                public void onbeforeShare(String platform, FHShareData shareData) {
                    /** 点击分享渠道按钮时生成rurl */
                    String rurl;
                    /** 组合分享链接*/
                    String shareUrl;

                    int shareType = ShareRecord.calculateShareType(platform);
                    rurl = getRurl(sd.url, member, shareType);
                    shareUrl = getShareUrl(rurl);

                    fhShareData.url = (shareUrl);

                    /** 所有信息生成完毕时 */
                    shareRecord.title = shareData.title;
                    shareRecord.content = shareData.content;
                    shareRecord.img = shareData.imgs;
                    shareRecord.rurl = rurl;

                    /** 如果是复制链接或者多图分享 则直接记录分享*/
                    if (TextUtils.equals(platform, FHShareItem.COPYLINK) || TextUtils.equals(platform, FHShareItem.MANYPICS)) {
                        if (sd.record) {
                            shareRecord.share();
                        }

                        if (shareListener != null) {
                            shareListener.onBrowserShare(0, shareType, platform);
                        }
                    }

                }
            };
            List<FHShareItem> shareItems;
            fhShareData.shareImage = sd.shareImage;
            if (sd.justWechat) {
                shareItems = FHShareItem.getWechatAll(act, fhShareData, actionListener, beforeShareActionListener);
            } else {
                shareItems = FHShareItem.getAll(act, fhShareData, actionListener, beforeShareActionListener);
            }

            int lastIndex = shareItems.size() - 1;
            if (sd.cutLastOne) {
                shareItems.remove(lastIndex);
            }

            if (items != null && items.length > 0) {
                shareItems.addAll(Arrays.asList(items));
            }

            FHShareDialog shareDialog = new FHShareDialog(act, shareItems);
            if (member != null && member.if_shoper == 1) {
                if (sd.deductMoney > 0) {
                    shareDialog.setTitleView(new LayoutShareDeduct(act, sd.price, sd.deductMoney).getView());
                }
            }

            if (sd.show) {
                // 启动分享GUI
                shareDialog.show();
            }
            return shareDialog;
        } else {
            return null;
        }
    }

    public static FHShareDialog fhShareByItems(final BaseFragmentActivity act, final ShareData sd, PlatformActionListener actionListener, FHShareItem... items) {
        return shareByItems(act, sd, actionListener, null, items);
    }

    public static FHShareDialog fhShare(final BaseFragmentActivity act, final ShareData sd, PlatformActionListener actionListener) {
        return fhShareByItems(act, sd, actionListener);
    }


    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://成功
                    VarInstance.getInstance().showMsg(R.string.share_completed);
                    break;
                case 1://取消
                    VarInstance.getInstance().showMsg(R.string.share_canceled);
                    break;
                case 2://失败
                    VarInstance.getInstance().showMsg(R.string.share_failed);
                    break;
            }
        }
    };

    public static String getRurl(String originalUrl, Member m, int shareType) {
        if (!originalUrl.contains("?")) originalUrl += "?fromType=android";
        String res = originalUrl + "&sharetype=" + shareType;
        if (m != null) {
            String deductid = m.member_id;
            String vid = ShareRecord.calculateVid(m);
            res += "&deductid=" + deductid + "&vid=" + vid;
        }
        return res;
    }

    public static String getShareUrl(String rurl) {
        String encodeRurl = BaseFunc.UrlEncode(rurl);
        String fmt = BaseVar.API_SHARE_URL_FMT;
        return String.format(fmt, encodeRurl);
    }

    public interface FHBrowserShareListener {
        void onBrowserShare(int error, int shareType, String platform);
    }
}
