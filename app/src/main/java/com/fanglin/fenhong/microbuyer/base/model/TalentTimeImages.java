package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fhlib.other.FHLib;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/8-下午5:24.
 * 功能描述: 达人时光图片
 */
public class TalentTimeImages {
    private String time_id;// 14, 时光id
    private String time_image;//  时光主图
    private long add_time;// 1464832958 时光发布时间戳

    /**
     * 以下字段是前端添加的，方便逻辑计算使用
     */
    private String tMonth;//月份
    private String tYear;//年份

    public String getTime_id() {
        return time_id;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
    }

    public String getTime_image() {
        return time_image;
    }

    public void setTime_image(String time_image) {
        this.time_image = time_image;
    }

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public String gettMonth() {
        return FHLib.formatTimestamp(add_time, "MM");
    }

    public String gettYear() {
        return FHLib.formatTimestamp(add_time, "yyyy");
    }

    public void settMonth(String tMonth) {
        this.tMonth = tMonth;
    }

    public void genMonth() {
        this.tMonth = gettMonth();
    }

    public void genYear() {
        this.tYear = gettYear();
    }

    public void settYear(String tYear) {
        this.tYear = tYear;
    }


    public static List<TalentTimeImages> getTestList() {
        List<TalentTimeImages> list = new ArrayList<>();

        //5月份
        TalentTimeImages images = new TalentTimeImages();
        images.setAdd_time(1464168071);
        images.setTime_id("1");
        images.genMonth();
        images.genYear();

        list.add(images);

        images = new TalentTimeImages();
        images.setAdd_time(1464832381);
        images.setTime_id("2");
        images.genMonth();
        images.genYear();
        list.add(images);

        images = new TalentTimeImages();
        images.setAdd_time(1464832357);
        images.setTime_id("3");
        images.genMonth();
        images.genYear();
        list.add(images);

        images = new TalentTimeImages();
        images.setAdd_time(1464569896);
        images.setTime_id("4");
        images.genMonth();
        images.genYear();
        list.add(images);

        images = new TalentTimeImages();
        images.setAdd_time(1464331393);
        images.setTime_id("5");
        images.genMonth();
        images.genYear();
        list.add(images);

        //6月份
        images = new TalentTimeImages();
        images.setAdd_time(1465712400);
        images.setTime_id("6");
        images.genMonth();
        images.genYear();
        list.add(images);

        images = new TalentTimeImages();
        images.setAdd_time(1465712421);
        images.setTime_id("7");
        images.genMonth();
        images.genYear();
        list.add(images);

        images = new TalentTimeImages();
        images.setAdd_time(1465712459);
        images.setTime_id("8");
        images.genMonth();
        images.genYear();
        list.add(images);

        images = new TalentTimeImages();
        images.setAdd_time(1465712475);
        images.setTime_id("9");
        images.genMonth();
        images.genYear();
        list.add(images);

        //7月份
        images = new TalentTimeImages();
        images.setAdd_time(1468304400);
        images.setTime_id("10");
        images.genMonth();
        images.genYear();
        list.add(images);

        images = new TalentTimeImages();
        images.setAdd_time(1468304880);
        images.setTime_id("11");
        images.genMonth();
        images.genYear();
        list.add(images);
        return list;
    }
}
