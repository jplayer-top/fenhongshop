package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页导航按钮实体类
 * 分 头部,中部,底部 三种
 * Created by lizhixin on 2015/12/23.
 */
public class HomeNavigation {

    public String nav_id; //
    private String nav_title; // 标题
    public String nav_url; // 链接
    public String nav_icon; // 图标
    public String nav_event; // 事件
    public String nav_new_open; // 是否新窗口打开 （0;否 | 1;是）
    public int nav_sort; // 排序（从小到大返回）
    public String nav_type; // 类型 （0;app | 1;wap）
    public String nav_location; // 位置 （0;头部 | 1;中部 | 2;底部）
    public String nav_icon_selected; // 选中状态图标
    public String nav_color; // 颜色值
    public String nav_color_selected;  //选中状态颜色值
    private String nav_desc;//极速免税

    public int getNav_color() {
        try {
            return Color.parseColor(nav_color);
        } catch (Exception e) {
            return Color.parseColor("#333333");
        }
    }

    public int getNav_color_selected() {
        try {
            return Color.parseColor(nav_color_selected);
        } catch (Exception e) {
            return Color.parseColor("#fa2855");
        }
    }

    public String getNavTitle(Context context) {
        //0代表 非买手 10代表 普通 买手 20代表 vip 买手
        int mType = FHCache.getMemberType(context);
        boolean isDutyfree = !TextUtils.isEmpty(nav_url) && nav_url.contains("!dutyfree");
        if (mType > 0 && isDutyfree) {
            return nav_desc;
        } else {
            return nav_title;
        }
    }

    public static List<HomeNavigation> getTest() {
        List<HomeNavigation> list = new ArrayList<>();
        HomeNavigation homeNavigation = new HomeNavigation();
        homeNavigation.nav_title = "擦擦";
        homeNavigation.nav_url = "http://www.baidu.com";
        list.add(homeNavigation);
        return list;
    }
}
