package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/13-上午11:40.
 * 功能描述;//达人添加的带标签控件的图片
 */
public class TalentTagImage {
    private String time_image_id;//3,
    private String time_id;//1,
    private String time_image;//http://bizhi.33lc.com/uploadfile/2014/0331/20140331102904423.jpg,
    private String image_sort;//0,
    private List<TalentImageTag> goods;//图片上的标签 可能有多个标签 最多三个

    public String getTime_image_id() {
        return time_image_id;
    }

    public void setTime_image_id(String time_image_id) {
        this.time_image_id = time_image_id;
    }

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

    public String getImage_sort() {
        return image_sort;
    }

    public void setImage_sort(String image_sort) {
        this.image_sort = image_sort;
    }

    public List<TalentImageTag> getGoods() {
        return goods;
    }

    public void setGoods(List<TalentImageTag> goods) {
        this.goods = goods;
    }
}
