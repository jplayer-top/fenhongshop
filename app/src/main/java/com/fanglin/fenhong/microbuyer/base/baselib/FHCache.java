package com.fanglin.fenhong.microbuyer.base.baselib;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.base.event.CartActionEvent;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.MessageNum;
import com.fanglin.fenhong.microbuyer.base.model.SearchKey;
import com.fanglin.fhlib.other.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者： Created by Plucky on 15-11-16.
 * 分红全球购所有缓存数据
 * modify by lizhixin on 2016/02/27 购物车数量缓存
 */
public class FHCache {

    public static Member getMember(Activity act) {
        if (act == null) return null;
        FHApp app = (FHApp) act.getApplication();
        return getMember(app);
    }

    public static Member getMember(FHApp app) {
        Member member = app.member;
        if (member == null || TextUtils.isEmpty(member.member_id)) {
            String memStr = PreferenceUtils.getPrefString(app, BaseVar.VLOGIN, "");
            try {
                member = new Gson().fromJson(memStr, Member.class);
            } catch (Exception e) {
                member = null;
            }
        }
        return member;
    }

    public static String getMemberStringFromCache(Context context) {
        return PreferenceUtils.getPrefString(context, BaseVar.VLOGIN, "");
    }

    public static boolean setMember(Activity act, Member m) {
        if (act == null) return false;
        if (m == null || TextUtils.isEmpty(m.member_id)) return false;
        PreferenceUtils.setPrefString(act, BaseVar.VLOGIN, new Gson().toJson(m));
        FHApp app = (FHApp) act.getApplication();
        app.member = m;
        return true;
    }

    /**
     * 从沙盒取默认定位信息
     */
    public static FHLocation getLocationFromSandBox(Context c) {
        try {
            String json = PreferenceUtils.getPrefString(c, BaseVar.DEFAREA, "");
            return new Gson().fromJson(json, FHLocation.class);
        } catch (Exception e) {
            return new FHLocation();
        }
    }

    /**
     * 設置默认定位信息
     */
    public static void setLocation2SandBox(Context c, FHLocation loc) {
        PreferenceUtils.setPrefString(c, BaseVar.DEFAREA, new Gson().toJson(loc));
    }


    /**
     * 判断是不是第一次
     */
    public static boolean getFirst(Context c) {
        //是否为第一次启动APP
        return PreferenceUtils.getPrefboolean(c, BaseVar.FIRST, true);
    }

    public static void setNotFirst(Context c) {
        PreferenceUtils.setPrefboolean(c, BaseVar.FIRST, false);
    }

    /**
     * 清空與個人信息相關的緩存信息
     */
    public static void clearUserCache(Context c) {

        PreferenceUtils.setPrefString(c, BaseVar.DEFAREA, "");
        PreferenceUtils.setPrefString(c, BaseVar.VLOGIN, "");
        FHCache.setMemberType(c, 0);
        FHApp app = (FHApp) ((Activity) c).getApplication();
        app.member = null;

        PreferenceUtils.setPrefString(c, BaseVar.UCENTER, "");//清空个人中心数字

        EventBus.getDefault().post(new CartActionEvent(null));

        PreferenceUtils.setPrefString(c, BaseVar.MSGNUM, "");//清空消息推送
    }


    /**
     * APP推送消息相關
     */
    public static void setNum(Context c, MessageNum mn) {
        String json = new Gson().toJson(mn);
        PreferenceUtils.setPrefString(c, BaseVar.MSGNUM, json);
    }

    public static MessageNum getNum(Context c) {
        MessageNum num;
        try {
            String json = PreferenceUtils.getPrefString(c, BaseVar.MSGNUM, "");
            num = new Gson().fromJson(json, MessageNum.class);
        } catch (Exception e) {
            num = null;
        }
        return num;
    }

    public static void addAMsg(Context c, int type, String lastMsg) {
        MessageNum num = getNum(c);
        if (num == null) {
            num = new MessageNum();
        }
        switch (type) {
            case 12:
                num.goods_activity_num++;
                num.goods_activity_msg = lastMsg;
                break;
            case 13:
                num.order_notice_num++;
                num.order_notice_msg = lastMsg;
                break;
            case 14:
                num.delivery_notice_num++;
                num.delivery_notice_msg = lastMsg;
                break;
            case 15:
                num.income_num++;
                num.income_msg = lastMsg;
                break;
            case 9:
                num.sys_msg_num++;
                num.sys_msg = lastMsg;
                break;
            case 16:
                num.team_num++;
                num.team_msg = lastMsg;
                break;
            case 17:
                num.msg17_num++;
                num.msg17_msg = lastMsg;
                break;
            case 18:
                num.msg18Num++;
                num.msg18Msg = lastMsg;
                break;
            case 19:
                num.msg19Num++;
                num.msg19Msg = lastMsg;
                break;
        }
        setNum(c, num);
    }

    public static void onMsgClick(Context c, int type) {
        MessageNum num = getNum(c);
        if (num == null) {
            num = new MessageNum();
        }
        switch (type) {
            case 12:
                num.goods_activity_num = 0;
                break;
            case 13:
                num.order_notice_num = 0;
                break;
            case 14:
                num.delivery_notice_num = 0;
                break;
            case 15:
                num.income_num = 0;
                break;
            case 9:
                num.sys_msg_num = 0;
                break;
            case 16:
                num.team_num = 0;
                break;
            case 17:
                num.msg17_num = 0;
                break;
            case 18:
                num.msg18Num = 0;
                break;
            case 19:
                num.msg19Num = 0;
                break;
        }
        setNum(c, num);
    }

    public static void recordGPS(FHApp fhapp, Member m, FHLocation location) {
        if (fhapp != null && location != null && m != null) {
            try {
                location.member_id = m.member_id;
                fhapp.fhdb.saveOrUpdate(location);
            } catch (Exception e) {
                //
            }
        }
    }

    public static List<FHLocation> getGPS(FHApp fhapp) {
        if (fhapp != null) {
            List<FHLocation> gps;
            try {
                gps = fhapp.fhdb.findAll(FHLocation.class);
            } catch (Exception e) {
                gps = null;
            }
            return gps;
        } else {
            return null;
        }
    }


    /**
     * 未登录情况下的搜索缓存
     */
    public static List<SearchKey> getSearchList(Context context) {
        List<SearchKey> lst;
        try {
            String cacheStr = PreferenceUtils.getPrefString(context, BaseVar.SEARCH_CACHE, "");
            lst = new Gson().fromJson(cacheStr, new TypeToken<List<SearchKey>>() {
            }.getType());
        } catch (Exception e) {
            lst = null;
        }
        return lst;
    }

    public static List<SearchKey> getReversedSearchList(Context context) {
        List<SearchKey> lst = getSearchList(context);
        if (lst != null) {
            Collections.reverse(lst);
        }
        return lst;
    }

    public static void addSearch(Context c, String str) {
        List<SearchKey> list = getSearchList(c);
        if (list == null) list = new ArrayList<>();

        int sameIndex = -1;
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SearchKey key = list.get(i);
                if (TextUtils.equals(key.search_keywords, str)) {
                    sameIndex = i;
                    break;
                }
            }
        }

        if (sameIndex >= 0) {
            list.remove(sameIndex);
        }

        SearchKey searchKey = new SearchKey();
        searchKey.search_id = 1;
        searchKey.search_keywords = str;
        list.add(searchKey);

        PreferenceUtils.setPrefString(c, BaseVar.SEARCH_CACHE, new Gson().toJson(list));
    }

    public static void clearSearch(Context c) {
        PreferenceUtils.setPrefString(c, BaseVar.SEARCH_CACHE, "");
    }

    /**
     * 0代表 非买手 10代表 普通 买手 20代表 vip 买手
     */
    public static int getMemberType(Context c) {
        return PreferenceUtils.getPrefInt(c, BaseVar.MEMBER_TYPE_CACHE, 0);
    }

    public static void setMemberType(Context c, int memberType) {
        PreferenceUtils.setPrefInt(c, BaseVar.MEMBER_TYPE_CACHE, memberType);
    }

}