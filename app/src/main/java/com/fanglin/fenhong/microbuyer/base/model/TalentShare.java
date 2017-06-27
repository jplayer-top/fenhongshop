package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author;//Created by Plucky on 16/6/14-下午5;//03.
 * 功能描述;// 达人首页及达人详情分享数据格式
 */
public class TalentShare {
    private String title;//标题
    private String image;//达人头像
    private String content;//简介
    private String url;//分享链接

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
