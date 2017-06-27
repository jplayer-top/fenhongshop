package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 意见反馈实体类
 * Created by admin on 2015/11/17.
 */
public class FeedBack {

    private String content;//意见内容
    private String contact;//联系方式
    private AppInfo appInfo;//App信息
    private String type;//反馈类型 -- 预留字段

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
