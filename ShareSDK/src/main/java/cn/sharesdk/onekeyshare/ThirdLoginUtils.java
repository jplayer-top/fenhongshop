package cn.sharesdk.onekeyshare;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.model.QQAuth;
import cn.sharesdk.onekeyshare.model.SinaAuth;
import cn.sharesdk.onekeyshare.model.WechatAuth;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 第三方授权工具
 * Created by Administrator on 2015/7/24.
 */
public class ThirdLoginUtils {
    public final static int ERROR = 10;
    public final static int SUCCESS = 11;
    public final static int CANCEL = 12;
    private Platform plat;

    public ThirdLoginUtils(Context c, final String platname) {
        ShareSDK.initSDK(c.getApplicationContext());
        final Message msg = new Message();
        plat = ShareSDK.getPlatform(platname);
        if (plat == null) return;
        plat.SSOSetting(false);
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                msg.what = SUCCESS;
                if (TextUtils.equals(platform.getName(), SinaWeibo.NAME)) {
                    SinaAuth sa = new SinaAuth();
                    sa.id = hashMap.get("id").toString();// 1828463307
                    sa.name = hashMap.get("name").toString();// 昵称 _Plucky
                    sa.avatar_large = hashMap.get("avatar_large").toString();// 头像
                    sa.gender = hashMap.get("gender").toString();// 性别 m
                    sa.description = hashMap.get("description").toString();// 签名
                    sa.province = hashMap.get("province").toString();// 37
                    sa.city = hashMap.get("city").toString();// 2
                    sa.location = hashMap.get("location").toString();// 山东青岛
                    msg.obj = sa;
                } else if (TextUtils.equals(platform.getName(), QQ.NAME)) {
                    QQAuth qa = new QQAuth();
                    qa.city = hashMap.get("city").toString();
                    qa.figureurl_qq_2 = hashMap.get("figureurl_qq_2").toString();
                    qa.gender = hashMap.get("gender").toString();
                    qa.nickname = hashMap.get("nickname").toString();
                    qa.province = hashMap.get("province").toString();
                    msg.obj = qa;
                } else if (TextUtils.equals(platform.getName(), Wechat.NAME)) {
                    WechatAuth wa = new WechatAuth();
                    wa.unionid = hashMap.get("unionid").toString();
                    wa.openid = hashMap.get("openid").toString();
                    wa.headimgurl = hashMap.get("headimgurl").toString();
                    wa.nickname = hashMap.get("nickname").toString();
                    wa.sex = hashMap.get("sex").toString();
                    wa.country = hashMap.get("country").toString();
                    wa.province = hashMap.get("province").toString();
                    wa.city = hashMap.get("city").toString();
                    msg.obj = wa;
                } else {
                    msg.obj = hashMap;
                }

                handler.sendMessage(msg);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                msg.what = ERROR;
                msg.obj = null;
                handler.sendMessage(msg);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                msg.what = CANCEL;
                msg.obj = null;
                handler.sendMessage(msg);
            }
        });
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    if (mcb != null) {
                        mcb.Executed(SUCCESS, plat.getName(), msg.obj);
                    }
                    break;
                case CANCEL:
                    if (mcb != null) {
                        mcb.Executed(CANCEL, plat.getName(), msg.obj);
                    }
                    break;
                case ERROR:
                    if (mcb != null) {
                        mcb.Executed(ERROR, plat.getName(), msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void login() {
        if (mcb != null) {
            mcb.onStart();
        }
        if (plat == null) return;
        plat.removeAccount(false);
        plat.showUser(null);
    }

    private TLCallBack mcb;

    public ThirdLoginUtils setCallBack(TLCallBack cb) {
        this.mcb = cb;
        return this;
    }

    public interface TLCallBack {
        void onStart();

        void Executed(int type, String platname, Object data);

    }
}
