package com.fanglin.fenhong.microbuyer.base.model;

import java.net.URLEncoder;

/**
 * 国家馆 分类 list item
 */
public class NationalPavClassifyEntity {
    private String class_id;
    private String class_name;
    private String class_pic;
    private String share_title;
    private String share_desc;
    private String share_img;

    public String getClass_id() {
        return class_id;
    }

    public String getEncodeClassName() {
        try {
            return URLEncoder.encode(class_name, "utf-8");
        } catch (Exception e) {
            return "android";
        }
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_pic() {
        return class_pic;
    }

    public void setClass_pic(String class_pic) {
        this.class_pic = class_pic;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_desc() {
        return share_desc;
    }

    public void setShare_desc(String share_desc) {
        this.share_desc = share_desc;
    }

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }
}
